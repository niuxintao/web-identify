package org.hsqldb.test;

import static java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT;
//import static java.sql.ResultSet.CONCUR_READ_ONLY;
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
import java.sql.Timestamp;

public class IncompatibleDataAndUpdateRow {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static final int[] resultSetTypes = { TYPE_FORWARD_ONLY,
			TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE };

	public static final int[] resultSetHoldabilitys = {
			HOLD_CURSORS_OVER_COMMIT, CLOSE_CURSORS_AT_COMMIT };

	private Connection m_conn;

	public IncompatibleDataAndUpdateRow(Connection m_conn) {
		this.m_conn = m_conn;
	}

	public void testStatement(int multiple, int havePlaceOrNot, int sensitive,
			int holid) throws SQLException {
		Statement stmt = m_conn.createStatement();
		int cnt = 0;
		ResultSet set = stmt
				.executeQuery("select to_number(to_char((select current_timestamp + c0  day from dual), 'YYYYMMDD')) from dual");
		if (set.next()) {
			cnt = set.getInt(1);
		}

		if (multiple > 1)
			stmt.executeUpdate("insert into dual values (3)");

		stmt.close();

		stmt = m_conn.createStatement(sensitive, CONCUR_UPDATABLE, holid);

		set = stmt.executeQuery("select c0 from dual");
		while (set.next()) {
			set.updateInt(1, cnt++);
			set.updateRow();
		}
		set.close();
		stmt.close();
	}

	public void testPreparementStatement(int multiple, int havePlaceOrNot,
			int sensitive, int holid) throws SQLException {
		PreparedStatement pstmt;
		if (havePlaceOrNot == TestIncompatibleDataAndUpdateRow.HAVEPLACEHOLEDER) {
			pstmt = m_conn
					.prepareStatement("select to_number(to_char((select ? + c0  day from dual), 'YYYYMMDD')) from dual");
			pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
		} else {
			pstmt = m_conn
					.prepareStatement("select to_number(to_char((select current_timestamp + c0  day from dual), 'YYYYMMDD')) from dual");
		}
		ResultSet set = pstmt.executeQuery();
		int cnt = 0;
		if (set.next()) {
			cnt = set.getInt(1);
		}

		if (multiple > 1){
			Statement stmt =  m_conn.createStatement();
			stmt.executeUpdate("insert into dual values (3)");
			stmt.close();
		}

		pstmt.close();
		String sql = "select c0 from dual";
		pstmt = m_conn
				.prepareStatement(sql, sensitive, CONCUR_UPDATABLE, holid);

		set = pstmt.executeQuery();
		while (set.next()) {
			set.updateInt("c0", cnt++);
			set.updateRow();
		}
		set.close();
		pstmt.close();
	}


	public void test(int multiple, int mode, int havePlaceOrNot, int sensitive,
			int holid) throws SQLException {
		Statement stmt = m_conn.createStatement();
		stmt.execute("create table dual (c0 integer)");
		stmt.executeUpdate("insert into dual values (2)");
		
		if (mode == TestIncompatibleDataAndUpdateRow.STATEMENT)
			this.testStatement(multiple, havePlaceOrNot, sensitive, holid);
		else
			this.testPreparementStatement(multiple, havePlaceOrNot, sensitive,
					holid);
	}

	public static void main(String[] args) throws Exception {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:db", "sa",
				"");

		// ResultSet set = stmt.executeQuery("select c0 from dual");
		IncompatibleDataAndUpdateRow test = new IncompatibleDataAndUpdateRow(
				c);
		test.test(2, TestIncompatibleDataAndUpdateRow.PREPAREDSTATEMENT,
				TestIncompatibleDataAndUpdateRow.HAVEPLACEHOLEDER, TYPE_SCROLL_INSENSITIVE,
				HOLD_CURSORS_OVER_COMMIT);

		c.createStatement().executeUpdate("SHUTDOWN");
		c.close();
	}

}
