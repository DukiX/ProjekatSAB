package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import operations.TransactionOperations;

public class pd150258_TransactionOperations implements TransactionOperations {

	@Override
	public BigDecimal getBuyerTransactionsAmmount(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select sum(Amount) from [Transaction] where IdShopBuyer = ?";

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getBigDecimal(1);
			}

			return new BigDecimal(0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new BigDecimal(-1);
	}

	@Override
	public BigDecimal getShopTransactionsAmmount(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select sum(Amount) from [Transaction] where IdShopBuyer = ?";

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, shopId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getBigDecimal(1);
			}

			return new BigDecimal(0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new BigDecimal(-1);
	}

	@Override
	public List<Integer> getTransationsForBuyer(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select Id from [Transaction] where IdShopBuyer = ?";
		LinkedList<Integer> lst = new LinkedList<>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

			if (lst.isEmpty()) {
				return null;
			}

			return lst;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int getTransactionForBuyersOrder(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select Id from [Transaction] where IdOrder = ? and IdShopBuyer in (select Id from Buyer)";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);

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
	public int getTransactionForShopAndOrder(int orderId, int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select Id from [Transaction] where IdOrder = ? and IdShopBuyer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);
			ps.setInt(2, shopId);

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
	public List<Integer> getTransationsForShop(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select Id from [Transaction] where IdShopBuyer = ?";
		LinkedList<Integer> lst = new LinkedList<>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, shopId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

			if (lst.isEmpty()) {
				return null;
			}

			return lst;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Calendar getTimeOfExecution(int transactionId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select TimeOfExecution from [Transaction] where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, transactionId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				Timestamp ts = rs.getTimestamp(1);
				if (ts == null) {
					return null;
				}
				Calendar cal = pd150258_GeneralOperations.currentTime;

				cal.setTimeInMillis(ts.getTime());
				return cal;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BigDecimal getAmmountThatBuyerPayedForOrder(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select Amount from [Transaction] where IdOrder = ? and IdShopBuyer in (select Id from Buyer)";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				BigDecimal bd = rs.getBigDecimal(1);
				if(bd!=null) {
					return bd;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BigDecimal(-1);
	}

	@Override
	public BigDecimal getAmmountThatShopRecievedForOrder(int shopId, int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select Amount from [Transaction] where IdOrder = ? and IdShopBuyer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);
			ps.setInt(2, shopId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				BigDecimal bd = rs.getBigDecimal(1);
				if(bd!=null) {
					return bd;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BigDecimal(-1);
	}

	@Override
	public BigDecimal getTransactionAmount(int transactionId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select Amount from [Transaction] where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, transactionId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				BigDecimal bd = rs.getBigDecimal(1);
				if(bd!=null) {
					return bd;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BigDecimal(-1);
	}

	@Override
	public BigDecimal getSystemProfit() {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select sum(t.Amount * (CASE When o.DodatniPopust = 1 Then 0.03 Else 0.05 End))"
				+ "from [Transaction] t join [Order] o on(t.IdOrder = o.Id) "
				+ "where t.IdShopBuyer in (select Id from Shop)";
		try {
			Statement st = connection.createStatement();

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				BigDecimal bd = rs.getBigDecimal(1);
				if(bd!=null) {
					return bd;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}
		return new BigDecimal(0);
	}

}
