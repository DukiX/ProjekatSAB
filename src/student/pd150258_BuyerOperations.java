package student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import operations.BuyerOperations;

public class pd150258_BuyerOperations implements BuyerOperations {

	@Override
	public int createBuyer(String name, int cityId) {
		Connection connection = DB.getInstance().getConnection();
		String insertQuery = "insert into ShopBuyer(Name,IdCity) values(?,?)";
		String insertQuery2 = "insert into Buyer(Id,Credit) values(?,?)";
		try {
			PreparedStatement psIns = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			psIns.setString(1, name);
			psIns.setInt(2, cityId);

			psIns.executeUpdate();

			ResultSet rs = psIns.getGeneratedKeys();

			int key = -1;

			if (rs.next()) {
				key = rs.getInt(1);
			}

			if (key == -1) {
				return -1;
			}

			PreparedStatement psIns2 = connection.prepareStatement(insertQuery2,
					PreparedStatement.RETURN_GENERATED_KEYS);
			psIns2.setInt(1, key);
			psIns2.setBigDecimal(2, new BigDecimal(0));
			psIns2.executeUpdate();

			return key;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int setCity(int buyerId, int cityId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "update ShopBuyer set IdCity = ? where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, cityId);
			ps.setInt(2, buyerId);

			int cnt = ps.executeUpdate();

			if (cnt > 0) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getCity(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select IdCity from ShopBuyer where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, buyerId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public BigDecimal increaseCredit(int buyerId, BigDecimal credit) {
		Connection connection = DB.getInstance().getConnection();
		String update = "update Buyer set Credit = Credit + ? where Id = ?";
		String select = "select Credit from Buyer where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(update);
			ps.setBigDecimal(1, credit);
			ps.setInt(2, buyerId);
			ps.executeUpdate();

			PreparedStatement sel = connection.prepareStatement(select);
			sel.setInt(1, buyerId);
			ResultSet rs = sel.executeQuery();

			if (rs.next()) {
				return rs.getBigDecimal(1).setScale(0, RoundingMode.HALF_EVEN);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int createOrder(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String create = "insert into [Order] (State,IdBuyer,DodatniPopust) values ('created',?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(create,PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, buyerId);
			ps.setBoolean(2, false);
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();

			int key = -1;

			if (rs.next()) {
				key = rs.getInt(1);
			}
			
			return key;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<Integer> getOrders(int buyerId) {
		LinkedList<Integer> lst = new LinkedList<>();
		Connection connection = DB.getInstance().getConnection();
		String selectQuery = "select Id from [Order] where IdBuyer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(selectQuery);
			ps.setInt(1, buyerId);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				lst.add(resultSet.getInt(1));
			}
			return lst;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BigDecimal getCredit(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String select = "select Credit from Buyer where Id = ?";
		try {
			PreparedStatement sel = connection.prepareStatement(select);

			sel.setInt(1, buyerId);
			ResultSet rs = sel.executeQuery();

			if (rs.next()) {
				return rs.getBigDecimal(1).setScale(0, RoundingMode.HALF_EVEN);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
