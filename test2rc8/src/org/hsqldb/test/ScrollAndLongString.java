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

public class ScrollAndLongString {

	public static final int[] resultSetTypes = { TYPE_FORWARD_ONLY,
			TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE };

	public static final int[] resultSetConcurrencys = { CONCUR_READ_ONLY,
			CONCUR_UPDATABLE };

	public static final int[] resultSetHoldabilitys = {
			HOLD_CURSORS_OVER_COMMIT, CLOSE_CURSORS_AT_COMMIT };

	private Connection m_conn;

	public ScrollAndLongString(Connection m_conn) {
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
		if (moreOrLess == TestScrollAndLong.MORE)
			rs = st.executeQuery("SELECT * FROM T WHERE VALUE1 = 'hy, hsqldb!' ");
		else
			rs = st.executeQuery("SELECT * FROM T WHERE VALUE1 = 'hys' ");

		if (sensitive != TYPE_FORWARD_ONLY)
			if (forwad == TestScrollAndLong.NEXT)
				rs.next();
			else if (forwad == TestScrollAndLong.LAST)
				rs.last();
			else if (forwad == TestScrollAndLong.FIRST)
				rs.first();
			else if (forwad == TestScrollAndLong.LAST)
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

		if (havePlaceOrNot == TestScrollAndLong.HAVEPLACEHOLEDER) {
			ps = m_conn.prepareStatement("SELECT * FROM t  WHERE VALUE1 = ? ",
					curse, sensitive, result);
			if (moreOrLess == TestScrollAndLong.MORE)
				ps.setString(1, "hy, hsqldb!");
			else
				ps.setString(1, "hys");
		} else {
			if (moreOrLess == TestScrollAndLong.MORE)
				ps = m_conn.prepareStatement(
						"SELECT * FROM t  WHERE VALUE1 = 'hy, hsqldb!' ",
						curse, sensitive, result);
			else
				ps = m_conn.prepareStatement(
						"SELECT * FROM t  WHERE VALUE1 = 'hys' ", curse,
						sensitive, result);
		}
		rs = ps.executeQuery();

		// rs.next();
		if (sensitive != TYPE_FORWARD_ONLY)
			if (forwad == TestScrollAndLong.NEXT)
				rs.next();
			else if (forwad == TestScrollAndLong.LAST)
				rs.last();
			else if (forwad == TestScrollAndLong.FIRST)
				rs.first();
			else if (forwad == TestScrollAndLong.LAST)
				rs.last();

		rs.close();
	}

	public void test(int mode, int havePlaceOrNot, int moreOrLess, int forwad,
			int sensitve, int cur, int result) throws Exception {
		if (mode == TestScrollAndLong.STATEMENT)
			this.testLast_statement(havePlaceOrNot, moreOrLess, forwad,
					sensitve, cur, result);
		else
			this.testLast_preparedStatement(havePlaceOrNot, moreOrLess, forwad,
					sensitve, cur, result);
	}

	// 1. constraint Forword Only constraints with last qurey
	//

	public static void main(String[] args) throws Exception {

		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException(
					"<clinit> failed.  JDBC Driver class not in CLASSPATH");
		}

		Connection m_conn = DriverManager.getConnection("jdbc:hsqldb:mem:m",
				"SA", "");

		ScrollAndLongString test = new ScrollAndLongString(m_conn);
		test.test(TestScrollAndLong.STATEMENT,
				TestScrollAndLong.HAVEPLACEHOLEDER,
				TestScrollAndLong.LESS, TestScrollAndLong.LAST,
				TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, HOLD_CURSORS_OVER_COMMIT);

		m_conn.createStatement().executeUpdate("SHUTDOWN");
		// test.test(PREPAREMENT, HAVEPLACEHOLEDER, LESS, ,CONCUR_UPDATABLE);
		// test.test(PREPAREMENT, HAVEPLACEHOLEDER, LESS,
		// TYPE_FORWARD_ONLY,CONCUR_UPDATABLE);
		// test.test(PREPAREMENT, HAVEPLACEHOLEDER, LESS,
		// TYPE_FORWARD_ONLY,CONCUR_UPDATABLE);
	}
}
