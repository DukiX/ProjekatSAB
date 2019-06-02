package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import operations.ShopOperations;

public class pd150258_ShopOperations implements ShopOperations {

	@Override
	public int createShop(String name, String cityName) {
		Connection connection = DB.getInstance().getConnection();
		String selectCity = "select Id from City where Name = ?";
		String insertQuery = "insert into ShopBuyer(Name,IdCity) values(?,?)";
		String insertQuery2 = "insert into Shop(Id,DiscountPercentage) values(?,?)";
		try {
			PreparedStatement psSel = connection.prepareStatement(selectCity);
			psSel.setString(1, cityName);
			ResultSet rsSel = psSel.executeQuery();
			int idCity = -1;
			if (rsSel.next()) {
				idCity = rsSel.getInt(1);
			}

			PreparedStatement psIns = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			psIns.setString(1, name);
			psIns.setInt(2, idCity);

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
			psIns2.setInt(2, 0);
			psIns2.executeUpdate();

			return key;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int setCity(int shopId, String cityName) {
		Connection connection = DB.getInstance().getConnection();
		String selectCity = "select Id from City where Name = ?";
		String sql = "update ShopBuyer set IdCity = ? where Id = ?";
		try {
			PreparedStatement psSel = connection.prepareStatement(selectCity);
			psSel.setString(1, cityName);
			ResultSet rsSel = psSel.executeQuery();
			int idCity = -1;
			if (rsSel.next()) {
				idCity = rsSel.getInt(1);
			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, idCity);
			ps.setInt(2, shopId);

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
	public int getCity(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select IdCity from ShopBuyer where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, shopId);

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
	public int setDiscount(int shopId, int discountPercentage) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "update Shop set DiscountPercentage = ? where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, discountPercentage);
			ps.setInt(2, shopId);
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
	public int increaseArticleCount(int articleId, int increment) {
		Connection connection = DB.getInstance().getConnection();
		String increase = "update Article set ArticleCount = ArticleCount + ? where Id = ?";
		String select = "select ArticleCount from Article where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(increase, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, increment);
			ps.setInt(2, articleId);
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			int key = -1;

			if (rs.next()) {
				key = rs.getInt(1);
			}

			if (key == -1) {
				return -1;
			}

			PreparedStatement sel = connection.prepareStatement(select);
			sel.setInt(1, articleId);
			ResultSet res = sel.executeQuery();

			if (res.next()) {
				return res.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getArticleCount(int articleId) {
		Connection connection = DB.getInstance().getConnection();
		String select = "select ArticleCount from Article where Id = ?";
		try {
			PreparedStatement sel = connection.prepareStatement(select);
			sel.setInt(1, articleId);
			ResultSet res = sel.executeQuery();

			if (res.next()) {
				return res.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<Integer> getArticles(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		LinkedList<Integer> lst = new LinkedList<Integer>();
		String sql = "select Id from Article where IdShop = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, shopId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				lst.add(rs.getInt(1));
			}
			
			return lst;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getDiscount(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select DiscountPercentage from Shop where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, shopId);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return rs.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
