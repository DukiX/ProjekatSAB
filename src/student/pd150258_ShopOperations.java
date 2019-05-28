package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			int idCity=-1;
			if(rsSel.next()) {
				idCity = rsSel.getInt(1);
			}
			
			PreparedStatement psIns = connection.prepareStatement(insertQuery,PreparedStatement.RETURN_GENERATED_KEYS);
			psIns.setString(1, name);
			psIns.setInt(2, idCity);
			
			psIns.executeUpdate();
			
			ResultSet rs = psIns.getGeneratedKeys();
			
			int key=-1;
            
            if(rs.next()) {
            	key = rs.getInt(1);
            }
            
            if(key==-1) {
            	return -1;
            }
            
            PreparedStatement psIns2 = connection.prepareStatement(insertQuery2, PreparedStatement.RETURN_GENERATED_KEYS);
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCity(int shopId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setDiscount(int shopId, int discountPercentage) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int increaseArticleCount(int articleId, int increment) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getArticleCount(int shopId, int articleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> getArticles(int shopId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDiscount(int shopId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
