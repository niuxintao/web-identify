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
import java.sql.SQLException;
import java.sql.Statement;

public class TestScroll {

	public static final int[] resultSetTypes = { TYPE_FORWARD_ONLY,
			TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE };

	public static final int[] resultSetConcurrencys = { CONCUR_READ_ONLY,
			CONCUR_UPDATABLE };

	public static final int[] resultSetHoldabilitys = {
			HOLD_CURSORS_OVER_COMMIT, CLOSE_CURSORS_AT_COMMIT };

	private Connection m_conn;

	public TestScroll(Connection m_conn) {
		this.m_conn = m_conn;
	}

	public void testLast_statement(int havePlaceOrNot, int moreOrLess,
			int forwad, int sensitive, int curse, int result)
			throws SQLException {
		Statement st = m_conn.createStatement();
		ResultSet rs;

		st.executeUpdate("CREATE TABLE t(VALUE1 VARCHAR( 4 ) )");
		st.executeUpdate("INSERT INTO t VALUES('ss')");
		st.close();

		st = m_conn.createStatement(sensitive, curse, result);
		if (moreOrLess == TestScrollForAllConfigs.MORE)
			rs = st.executeQuery("SELECT * FROM T WHERE VALUE1 = 'hys' ");
		else
			rs = st.executeQuery("SELECT * FROM T WHERE VALUE1 = 'hys' ");

		if (sensitive != TYPE_FORWARD_ONLY)
			if (forwad == TestScrollForAllConfigs.NEXT)
				rs.next();
			else if (forwad == TestScrollForAllConfigs.PREVIOUS)
				rs.previous();
			else if (forwad == TestScrollForAllConfigs.FIRST)
				rs.first();
			else if (forwad == TestScrollForAllConfigs.LAST)
				rs.last();

		rs.close();
	}

	public void testLast_preparedStatement(int havePlaceOrNot, int moreOrLess,
			int forwad, int sensitive, int curse, int result)
			throws SQLException {

		ResultSet rs;
		PreparedStatement ps;
		Statement st = m_conn.createStatement();

		st.executeUpdate("CREATE TABLE t(VALUE1 VARCHAR( 4 ) )");
		st.executeUpdate("INSERT INTO t VALUES('ss')");
		st.close();

		if (havePlaceOrNot == TestScrollForAllConfigs.HAVEPLACEHOLEDER) {
			ps = m_conn.prepareStatement("SELECT * FROM t  WHERE VALUE1 = ? ",
					curse, sensitive, result);
				ps.setString(1, "hys");
		} else {
				ps = m_conn.prepareStatement(
						"SELECT * FROM t  WHERE VALUE1 = 'hys' ", curse,
						sensitive, result);
		}
		rs = ps.executeQuery();

		// rs.next();
		if (sensitive != TYPE_FORWARD_ONLY)
			if (forwad == TestScrollForAllConfigs.NEXT)
				rs.next();
//			else if (forwad == TestScrollForAllConfigs.PREVIOUS)  //previous the same fault
//				rs.previous();
			else if (forwad == TestScrollForAllConfigs.FIRST)
				rs.first();
//			else if (forwad == TestScrollForAllConfigs.LAST)
//				rs.last();

		rs.close();
	}

	public void test(int mode, int havePlaceOrNot, int moreOrLess, int forwad,
			int sensitve, int cur, int result) throws Exception {
		if (mode == TestScrollForAllConfigs.STATEMENT)
			this.testLast_statement(havePlaceOrNot, moreOrLess, forwad,
					sensitve, cur, result);
		else
			this.testLast_preparedStatement(havePlaceOrNot, moreOrLess, forwad,
					sensitve, cur, result);
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

		TestScroll test = new TestScroll(m_conn);
		test.test(TestScrollForAllConfigs.STATEMENT,
				TestScrollForAllConfigs.HAVEPLACEHOLEDER, TestScrollForAllConfigs.LESS,
				TestScrollForAllConfigs.LAST, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY,
				HOLD_CURSORS_OVER_COMMIT);

		m_conn.createStatement().executeUpdate("SHUTDOWN");
	}
}
