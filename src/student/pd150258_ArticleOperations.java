package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import operations.ArticleOperations;

public class pd150258_ArticleOperations implements ArticleOperations {

	@Override
	public int createArticle(int shopId, String articleName, int articlePrice) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "insert into Article(ArticleName,ArticlePrice,ArticleCount,IdShop) values (?,?,0,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, articleName);
			ps.setInt(2, articlePrice);
			ps.setInt(3, shopId);
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			int key = -1;
			if(rs.next()) {
				key = rs.getInt(1);
			}
			
			return key;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
