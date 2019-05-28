package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import operations.GeneralOperations;

public class pd150258_GeneralOperations implements GeneralOperations {

	@Override
	public void setInitialTime(Calendar time) {
		// TODO Auto-generated method stub

	}

	@Override
	public Calendar time(int days) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getCurrentTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eraseAll() {
		Connection connection = DB.getInstance().getConnection();
		String sql1 = "delete from Shop";
		String sql2 = "delete from ShopBuyer";
		String sql3 = "delete from Line";
		String sql4 = "delete from City";
		try {
			Statement s1 = connection.createStatement();
			s1.executeUpdate(sql1);

			Statement s2 = connection.createStatement();
			s2.executeUpdate(sql2);

			Statement s3 = connection.createStatement();
			s3.executeUpdate(sql3);

			Statement s4 = connection.createStatement();
			s4.executeUpdate(sql4);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
