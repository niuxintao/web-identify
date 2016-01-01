package org.hsqldb.test;

import static java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JoinAndDeleteRows {
	public static final int DEFAULT = 0;
	public static final int INDEFAULT = 1;

	public static final int DUP = 0;
	public static final int INDUP = 1;
	public static final int DEDUP = 2;

	public static final int[] resultSetTypes = { TYPE_FORWARD_ONLY,
			TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE };

	public static final int[] resultSetHoldabilitys = {
			HOLD_CURSORS_OVER_COMMIT, CLOSE_CURSORS_AT_COMMIT };

	public static final int[] resultSetConcurrencys = { CONCUR_READ_ONLY,
			CONCUR_UPDATABLE };

	private Connection m_conn;

	public JoinAndDeleteRows(Connection m_conn) {
		this.m_conn = m_conn;
	}

	public void testStatement(int duplicate, int defaultOrNot, int sensitive,
			int cur, int holid) throws Exception {

		String sql1 = "CREATE TABLE Table_1 (Col int)";
		String sql2 = "CREATE TABLE Table_2 (Col int)";
		String sql3 = "INSERT INTO Table_1 VALUES (1)";
		String sql4 = "INSERT INTO Table_2 VALUES (1)";

		m_conn.createStatement().executeUpdate(sql1);
		m_conn.createStatement().executeUpdate(sql2);
		m_conn.createStatement().executeUpdate(sql3);
		m_conn.createStatement().executeUpdate(sql4);

		String sql5;

		if (duplicate == INDUP) {
			sql5 = "SELECT * FROM Table_1 WHERE EXISTS ( SELECT * FROM (SELECT Col FROM Table_2) AS o JOIN (SELECT Col FROM Table_1) AS t ON o.Col=t.Col WHERE Table_1.Col=t.Col)";
		} else {
			sql5 = "SELECT * FROM Table_1 WHERE EXISTS ( SELECT * FROM (SELECT Col FROM Table_2) AS o JOIN (SELECT Col FROM Table_1 t1 WHERE Table_1.Col=t1.Col) AS t ON o.Col=t.Col WHERE Table_1.Col=t.Col)";
		}

		Statement stmtEx = m_conn.createStatement(sensitive, cur, holid);
		stmtEx.executeQuery(sql5);
		stmtEx.close();
		// PreparedStatement stmt = con.prepareStatement(sql5,
		// ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		// ResultSet rs = stmt.executeQuery();
		// stmt.close();

		int deletedRows = 0;

		String createSQL = "create table test ( num INTEGER PRIMARY KEY, str VARCHAR(25))";
		Statement createStmt = m_conn.createStatement();
		createStmt.execute(createSQL);
		createStmt.close();

		Statement stmt = m_conn.createStatement();

		for (int i = 0; i < 100; i++) {
			String ins2 = "insert into test (num,str) values (" + i + ","
					+ "'String" + i + "')";
			stmt.execute(ins2);
		}
		// there should now be 100 rows in the table
		ResultSet countRS = m_conn.createStatement().executeQuery(
				"Select count(*) as total from test");
		countRS.next();
		// System.out.println("rowCount = " + countRS.getInt("total"));
		String select = "SELECT * FROM test";

		if (defaultOrNot != DEFAULT) {
			stmt = m_conn.createStatement(sensitive,
					ResultSet.CONCUR_UPDATABLE,
					ResultSet.HOLD_CURSORS_OVER_COMMIT);
		} else {
			stmt = m_conn
					.createStatement(sensitive, ResultSet.CONCUR_UPDATABLE);
		}
		// error if :

		ResultSet rs = stmt.executeQuery(select);
		// rs.beforeFirst();
		while (rs.next()) {
			rs.deleteRow();
			deletedRows++;
		}

		stmt.close();

		if (deletedRows < 98)
			throw new Exception("not deleted!");

	}

	public void testPreparedStatement(int duplicate, int defaultOrNot,
			int sensitive, int cur, int holid) throws Exception {

		String sql1 = "CREATE TABLE Table_1 (Col int)";
		String sql2 = "CREATE TABLE Table_2 (Col int)";
		String sql3 = "INSERT INTO Table_1 VALUES (1)";
		String sql4 = "INSERT INTO Table_2 VALUES (1)";

		m_conn.createStatement().executeUpdate(sql1);
		m_conn.createStatement().executeUpdate(sql2);
		m_conn.createStatement().executeUpdate(sql3);
		m_conn.createStatement().executeUpdate(sql4);

		String sql5;

		if (duplicate == INDUP) {
			sql5 = "SELECT * FROM Table_1 WHERE EXISTS ( SELECT * FROM (SELECT Col FROM Table_2) AS o JOIN (SELECT Col FROM Table_1) AS t ON o.Col=t.Col WHERE Table_1.Col=t.Col)";
		} else {
			sql5 = "SELECT * FROM Table_1 WHERE EXISTS ( SELECT * FROM (SELECT Col FROM Table_2) AS o JOIN (SELECT Col FROM Table_1 t1 WHERE Table_1.Col=t1.Col) AS t ON o.Col=t.Col WHERE Table_1.Col=t.Col)";
		}
		PreparedStatement pstmt = m_conn.prepareStatement(sql5, sensitive, cur,
				holid);
		pstmt.executeQuery();
		pstmt.close();

		int deletedRows = 0;

		String createSQL = "create table test ( num INTEGER PRIMARY KEY, str VARCHAR(25))";
		Statement createStmt = m_conn.createStatement();
		createStmt.execute(createSQL);
		createStmt.close();

		String ins = "insert into test (num,str) values (?,?)";
		PreparedStatement pStmt = m_conn.prepareStatement(ins);
		for (int i = 0; i < 100; i++) {
			pStmt.setInt(1, i);
			pStmt.setString(2, "String" + i);
			pStmt.execute();
		}
		// there should now be 100 rows in the table
		ResultSet countRS = m_conn.createStatement().executeQuery(
				"Select count(*) as total from test");
		countRS.next();
		// System.out.println("rowCount = " + countRS.getInt("total"));
		String select = "SELECT * FROM test";
		PreparedStatement stmt;

		if (defaultOrNot != DEFAULT) {
			stmt = m_conn.prepareStatement(select, sensitive,
					ResultSet.CONCUR_UPDATABLE,
					ResultSet.HOLD_CURSORS_OVER_COMMIT);
		} else {
			stmt = m_conn.prepareStatement(select, sensitive,
					ResultSet.CONCUR_UPDATABLE);
		}
		// error if :

		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			rs.deleteRow();
			deletedRows++;
		}
		stmt.close();

		if (deletedRows < 98)
			throw new Exception("not deleted!");
	}

	public void test(int state, int duplicate, int defaultOrNot, int sensitive,
			int cur, int holid) throws Exception {
		if (state == TestJoinAndDelete.STATEMENT)
			this.testStatement(duplicate, defaultOrNot, sensitive, cur, holid);
		else
			this.testPreparedStatement(duplicate, defaultOrNot, sensitive, cur,
					holid);
	}

	public static void main(String[] args) throws Exception {

		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException(
					"<clinit> failed.  JDBC Driver class not in CLASSPATH");
		}

		Connection m_conn = DriverManager.getConnection("jdbc:hsqldb:mem:m",
				"SA", "");

		JoinAndDeleteRows test = new JoinAndDeleteRows(m_conn);
		test.test(TestJoinAndDelete.PREPAREDSTATEMENT,
				JoinAndDeleteRows.DUP, JoinAndDeleteRows.DEFAULT,
				TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, HOLD_CURSORS_OVER_COMMIT);

		m_conn.createStatement().executeUpdate("SHUTDOWN");
		// test.test(PREPAREMENT, HAVEPLACEHOLEDER, LESS, ,CONCUR_UPDATABLE);
		// test.test(PREPAREMENT, HAVEPLACEHOLEDER, LESS,
		// TYPE_FORWARD_ONLY,CONCUR_UPDATABLE);
		// test.test(PREPAREMENT, HAVEPLACEHOLEDER, LESS,
		// TYPE_FORWARD_ONLY,CONCUR_UPDATABLE);
	}
}
