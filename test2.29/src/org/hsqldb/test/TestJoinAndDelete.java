package org.hsqldb.test;

import static java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import static org.hsqldb.test.JoinAndDeleteRows.DEFAULT;
import static org.hsqldb.test.JoinAndDeleteRows.INDEFAULT;
import static org.hsqldb.test.JoinAndDeleteRows.DUP;
import static org.hsqldb.test.JoinAndDeleteRows.INDUP;
import static org.hsqldb.test.JoinAndDeleteRows.DEDUP;

import org.hsqldb.server.Server;
import org.hsqldb.server.WebServer;

public class TestJoinAndDelete {
	private Connection m_conn;

	private OutputSet outputRecord;

	private Server server = null;

	String url;
	String dbPath;

	String[] binParams = { "sql.enforce_strict_size", "sql.enforce_names",
			"sql.enforce_refs", "sql.enforce_size", "sql.enforce_types",
			"sql.enforce_tdc_delete", "sql.enforce_tdc_update" };

	String[] binValues = { "true", "false" };

	String[] otherParams = { "hsqldb.default_table_type", "hsqldb.tx",
			"hsqldb.tx_level" };

	String[][] otherValues = { { "CACHED", "MEMORY" },
			{ "LOCKS", "MVLOCKS", "MVCC" }, { "read_commited", "SERIALIZABLE" } };

	public static final int STATEMENT = 0;
	public static final int PREPAREDSTATEMENT = 1;

	public final static int SERVER = 0;
	public final static int WEBSERVER = 1;
	public final static int INPROCESS = 2;

	public final static int MEM = 0;
	public final static int FILE = 1;
	// public final static int RES = 2;

	public static int MORE = 1;
	public static int LESS = 0;

	public static int HAVEPLACEHOLEDER = 1;
	public static int NOPLACEHOLEDER = 0;

	public static int NEXT = 1;
	public static int PREVIOUS = 2;
	public static int FIRST = 3;
	public static int LAST = 4;

	public static int SINGLE = 1;
	public static int MULTIPLE = 2;

	public static final int[] stoveType = { MEM, FILE };

	public static final int[] ServerType = { SERVER, WEBSERVER, INPROCESS };

	public static final int[] resultSetTypes = { TYPE_FORWARD_ONLY,
			TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE };

	public static final int[] resultSetConcurrencys = { CONCUR_READ_ONLY,
			CONCUR_UPDATABLE };

	public static final int[] resultSetHoldabilitys = {
			HOLD_CURSORS_OVER_COMMIT, CLOSE_CURSORS_AT_COMMIT };

	public static final int[] defaultD = { DEFAULT, INDEFAULT };
	public static final int[] dup = { DUP, INDUP, DEDUP };

	public static final int[] multiple = { SINGLE, MULTIPLE };

	public static final int[] statMode = { STATEMENT, PREPAREDSTATEMENT };

	public static final int[] moreOrLess = { MORE, LESS };

	public static final int[] placeHolder = { HAVEPLACEHOLEDER, NOPLACEHOLEDER };

	public static final int[] cursorAction = { NEXT, PREVIOUS, FIRST, LAST };

	static {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException(
					"<clinit> failed.  JDBC Driver class not in CLASSPATH");
		}
	}

	public TestJoinAndDelete() {
		outputRecord = new OutputSet();
	}

	protected void setUp(int serverState, int form, int[] binValue,
			int[] otherValue) throws Exception {
		this.configURL(serverState, form);
		this.configureServer(serverState, form);
		this.configureProperties(binValue, otherValue);

		m_conn = DriverManager.getConnection(url, "sa", "");
	}

	protected void tearDown() throws Exception {
		m_conn.createStatement().executeUpdate("SHUTDOWN");
		this.tearDownServer();
		this.deleteDatabase("test");
	}

	public void configureServer(int serverState, int form) {
		if (serverState == SERVER || serverState == WEBSERVER) {
			server = serverState == WEBSERVER ? new WebServer() : new Server();

			if (serverState == WEBSERVER) {
				server.setPort(8085);
			}

			if (form == MEM) {
				dbPath = "mem:test;sql.enforce_strict_size=true";
			} else if (form == FILE) {
				dbPath = "file:test;";
			}

			server.setDatabaseName(0, "test");
			server.setDatabasePath(0, dbPath);
			server.setLogWriter(null);
			server.setErrWriter(null);
			server.start();
		}
	}

	public void deleteDatabase(String path) {
		FileUtil.deleteOrRenameDatabaseFiles(path);
	}

	protected void tearDownServer() {

		if (server != null) {
			server.stop();
			server = null;
		}
	}

	protected void configURL(int serverState, int form) {
		if (serverState == SERVER) {
			// server
			// not need safe link
			url = "jdbc:hsqldb:hsql://localhost/test";
		} else if (serverState == WEBSERVER) {
			// http
			url = "jdbc:hsqldb:http://localhost:8085/test";
		} else if (serverState == INPROCESS) {
			// in process
			if (form == MEM) {
				url = "jdbc:hsqldb:mem:test";
			} else if (form == FILE) {
				url = "jdbc:hsqldb:file:test";
			}
		}
		// this.url = "jdbc:hsqldb:mem:test";
		// config in process
		// config in server
		// config in http web server

		// config in memory
		// config in file
		// config in res
	}

	public void configureProperties(int[] binValue, int[] otherValue) {
		// binary
		for (int i = 0; i < binValue.length; i++) {
			this.url += ";";
			this.url += binParams[i];
			this.url += "=";
			this.url += this.binValues[binValue[i]];
		}

		// other
		for (int i = 0; i < otherValue.length; i++) {
			this.url += ";";
			this.url += otherParams[i];
			this.url += "=";
			this.url += this.otherValues[i][otherValue[i]];
		}
	}

	public void testConfigs(int[] set, int[] part, int index, BufferedWriter bw) {
		int nextIndex = index + 1;
		for (int i = 0; i < set[index]; i++) {
			int[] partCur = new int[set.length];
			System.arraycopy(part, 0, partCur, 0, set.length);
			partCur[index] = i;
			if (nextIndex == set.length) {
				int[] set2 = { 2, 3, 2, 3, 2, 2 };
				int[] part2 = { 0, 0, 0, 0, 0, 0 };
				int index2 = 0;
				// for (int j : partCur)
				// System.out.print(j + " ");
				testStatWithResultSetBench(partCur[0], partCur[1],
						Arrays.copyOfRange(partCur, 2, partCur.length),
						Arrays.copyOfRange(partCur, partCur.length,
								partCur.length), set2, part2, index2, bw);
			} else {
				testConfigs(set, partCur, nextIndex, bw);
			}
		}
	}

	public String testStatWithResultSet(int statMode, int dup, int defau,
			int resultSetType, int cur, int resultSetHoldability) {

		String str = OutputSet.PASS;
		try {
			JoinAndDeleteRows tsl = new JoinAndDeleteRows(m_conn);
			tsl.test(statMode, dup, defau, resultSetType, cur,
					resultSetHoldability);

			this.outputRecord.pendingStr(OutputSet.PASS);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			String s = writer.toString();
			this.outputRecord.pendingStr(s);
			str = s;
		} catch (Exception e) {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			String s = writer.toString();
			this.outputRecord.pendingStr(s);
			str = s;
		}
		return str;
	}

	public void testStatWithResultSetBench(int serverState, int form,
			int[] binValue, int[] otherValue, int[] set, int[] part, int index,
			BufferedWriter bw) {
		int nextIndex = index + 1;
		for (int i = 0; i < set[index]; i++) {
			int[] partCur = new int[set.length];
			System.arraycopy(part, 0, partCur, 0, set.length);
			partCur[index] = i;
			if (nextIndex == set.length) {
				String str;
				try {
					setUp(serverState, form, binValue, otherValue);

					str = testStatWithResultSet(statMode[partCur[0]],
							dup[partCur[1]], defaultD[partCur[2]],
							resultSetTypes[partCur[3]],
							resultSetConcurrencys[partCur[4]],
							resultSetHoldabilitys[partCur[5]]);

					tearDown();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Writer writer = new StringWriter();
					PrintWriter printWriter = new PrintWriter(writer);
					e.printStackTrace(printWriter);
					String s = writer.toString();
					this.outputRecord.pendingStr(s);
					str = s;
				}

				try {
					bw.write(serverState + " " + form + " ");

					for (int bin : binValue)
						bw.write(bin + " ");
					for (int bin : otherValue)
						bw.write(bin + " ");
					for (int i1 : partCur) {
						bw.write(i1 + " ");
					}
					bw.write(":");

					bw.write(this.outputRecord.get(str) + " ");
					bw.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				testStatWithResultSetBench(serverState, form, binValue,
						otherValue, set, partCur, nextIndex, bw);
			}
		}
	}

	public void showResult() {
		try {
			FileWriter fw = new FileWriter("bugInfoJoinAndDelete.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < this.outputRecord.getCurIndex(); i++) {
				bw.write(i + " : " + this.outputRecord.get(i));
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test() {
		int[] set = { 3, 2, 2, 2, 2 };
		int[] part = { 0, 0, 0, 0, 0 };
		int index = 0;
		try {
			FileWriter fw = new FileWriter("resultDeleteANdjoin.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			try {
				this.testConfigs(set, part, index, bw);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw.flush();
			bw.close();
			fw.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		showResult();
	}

	public static void main(String[] args) throws Exception {
		TestJoinAndDelete tb = new TestJoinAndDelete();
		tb.test();
	}

}
