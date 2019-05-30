package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import operations.GeneralOperations;

public class pd150258_GeneralOperations implements GeneralOperations {
	
	private Calendar initialTime;
	private Calendar currentTime;

	@Override
	public void setInitialTime(Calendar time) {
		initialTime = time;
		currentTime = time;
	}

	@Override
	public Calendar time(int days) {
		currentTime.add(Calendar.DATE, days);
		return currentTime;
	}

	@Override
	public Calendar getCurrentTime() {
		return currentTime;
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
