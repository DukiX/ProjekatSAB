package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import operations.GeneralOperations;

public class pd150258_GeneralOperations implements GeneralOperations {
	
	private static Calendar initialTime;
	public static Calendar currentTime;

	@Override
	public void setInitialTime(Calendar time) {
		initialTime = (Calendar)time.clone();
		currentTime = (Calendar)time.clone();
	}

	@Override
	public Calendar time(int days) {
		currentTime.add(Calendar.DATE, days);
		
		Connection connection = DB.getInstance().getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select IdOrder, max(TimeOfArrival) from OrderLocation group by IdOrder");
			
			while(rs.next()) {
				int idOrder = rs.getInt(1);
				Timestamp ts = rs.getTimestamp(2);
				Calendar cal =(Calendar) pd150258_GeneralOperations.currentTime.clone();
				cal.setTimeInMillis(ts.getTime());
				
				if(cal.before(pd150258_GeneralOperations.currentTime) || cal.equals(pd150258_GeneralOperations.currentTime)) {
					PreparedStatement ps = connection.prepareStatement("update [Order] set ReceivedTime = ?, State = 'arrived' where Id = ?");
					ps.setTimestamp(1, ts);
					ps.setInt(2, idOrder);
					ps.executeUpdate();
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
