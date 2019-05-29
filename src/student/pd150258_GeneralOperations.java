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
		try {
			connection.createStatement().executeUpdate("delete from OrderLocation");
			
			connection.createStatement().executeUpdate("delete from [Transaction]");
			
			connection.createStatement().executeUpdate("delete from OrderItems");
			
			connection.createStatement().executeUpdate("delete from Article");
			
			connection.createStatement().executeUpdate("delete from [Order]");
			
			connection.createStatement().executeUpdate("delete from Buyer");
			
			connection.createStatement().executeUpdate("delete from Shop");

			connection.createStatement().executeUpdate("delete from ShopBuyer");

			connection.createStatement().executeUpdate("delete from Line");

			connection.createStatement().executeUpdate("delete from City");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
