package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import operations.CityOperations;

public class pd150258_CityOperations implements CityOperations {

	@Override
	public int createCity(String name) {
		Connection connection = DB.getInstance().getConnection();
		String insertQuery = "insert into City(Name) values(?)";
		try {
			PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, name);
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			int key = -1;

			if (rs.next()) {
				key = rs.getInt(1);
			}

			return key;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<Integer> getCities() {
		LinkedList<Integer> lst = new LinkedList<>();
		Connection connection = DB.getInstance().getConnection();
		String selectQuery = "select Id from City";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(selectQuery);
			while (resultSet.next()) {
				lst.add(resultSet.getInt(1));
			}
			if (!lst.isEmpty()) {
				return lst;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int connectCities(int cityId1, int cityId2, int distance) {
		Connection connection = DB.getInstance().getConnection();
		String insertQuery = "insert into Line(IdCity1,IdCity2,Distance) values(?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, cityId1);
			ps.setInt(2, cityId2);
			ps.setInt(3, distance);
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			int key = -1;

			if (rs.next()) {
				key = rs.getInt(1);
			}

			return key;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<Integer> getConnectedCities(int cityId) {
		LinkedList<Integer> lst = new LinkedList<>();
		Connection connection = DB.getInstance().getConnection();
		String selectQuery = "select IdCity1 from Line where IdCity2 = ? UNION select IdCity2 from Line where IdCity1 = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(selectQuery);
			ps.setInt(1, cityId);
			ps.setInt(2, cityId);
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
	public List<Integer> getShops(int cityId) {
		LinkedList<Integer> lst = new LinkedList<>();
		Connection connection = DB.getInstance().getConnection();
		String selectQuery = "select Id from ShopBuyer where IdCity = ? and Id in (select id from Shop)";
		try {
			PreparedStatement ps = connection.prepareStatement(selectQuery);
			ps.setInt(1, cityId);
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

}
