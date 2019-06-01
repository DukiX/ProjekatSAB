package student;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import operations.OrderOperations;

public class pd150258_OrderOperations implements OrderOperations {

	@Override
	public int addArticle(int orderId, int articleId, int count) {

		Connection connection = DB.getInstance().getConnection();
		String proveraDovoljno = "select * from Article where Id = ? and ArticleCount >= ?";
		String samoDodavanje = "select Id from OrderItems where IdArticle = ? and IdOrder = ?";
		String update = "update OrderItems set Count = Count + ? where Id = ?";
		String insert = "insert into OrderItems(Count,IdArticle,IdOrder) values(?,?,?)";

		String smanji = "update Article set ArticleCount = ArticleCount - ? where Id = ?";

		try {
			PreparedStatement pd = connection.prepareStatement(proveraDovoljno);
			pd.setInt(1, articleId);
			pd.setInt(2, count);

			ResultSet pdrs = pd.executeQuery();

			// ima dovoljno artikala u prodavnici
			if (pdrs.next()) {

				PreparedStatement sd = connection.prepareStatement(samoDodavanje);
				sd.setInt(1, articleId);
				sd.setInt(2, orderId);

				ResultSet sdrs = sd.executeQuery();

				// samo dodavanje
				if (sdrs.next()) {
					int key = sdrs.getInt(1);

					PreparedStatement up = connection.prepareStatement(update);
					up.setInt(1, count);
					up.setInt(2, key);

					int cnt = up.executeUpdate();

					PreparedStatement smanj = connection.prepareStatement(smanji);
					smanj.setInt(1, count);
					smanj.setInt(2, articleId);
					smanj.executeUpdate();

					if (cnt > 0) {
						return key;
					} else {
						return -1;
					}

				} else {// kreiranje novog
					PreparedStatement ins = connection.prepareStatement(insert,
							PreparedStatement.RETURN_GENERATED_KEYS);
					ins.setInt(1, count);
					ins.setInt(2, articleId);
					ins.setInt(3, orderId);
					ins.executeUpdate();

					ResultSet insrs = ins.getGeneratedKeys();
					int key = -1;
					if (insrs.next()) {
						key = insrs.getInt(1);
					}

					PreparedStatement smanj = connection.prepareStatement(smanji);
					smanj.setInt(1, count);
					smanj.setInt(2, articleId);
					smanj.executeUpdate();

					return key;
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public int removeArticle(int orderId, int articleId) {
		Connection connection = DB.getInstance().getConnection();
		String kolko = "select Id,Count from OrderItems where IdOrder = ? and IdArticle = ?";
		String izvadi = "delete from OrderItems where Id = ?";
		String vrati = "update Article set ArticleCount = ArticleCount + ? where Id = ?";
		try {
			PreparedStatement kol = connection.prepareStatement(kolko);
			kol.setInt(1, orderId);
			kol.setInt(2, articleId);
			ResultSet kolrs = kol.executeQuery();

			if (kolrs.next()) {
				int idOrderItems = kolrs.getInt(1);
				int count = kolrs.getInt(2);

				PreparedStatement izv = connection.prepareStatement(izvadi);
				izv.setInt(1, idOrderItems);
				int cnt1 = izv.executeUpdate();

				PreparedStatement vr = connection.prepareStatement(vrati);
				vr.setInt(1, count);
				vr.setInt(2, articleId);
				int cnt2 = vr.executeUpdate();

				if (cnt1 > 0 && cnt2 > 0) {
					return 1;
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<Integer> getItems(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select Id from OrderItems where IdOrder = ?";
		LinkedList<Integer> lst = new LinkedList<>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
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
	public int completeOrder(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String updateStatus = "update [Order] set State = 'sent', SentTime = ? where Id = ?";
		String removeCredit = "update Buyer set Credit = Credit - ? where Id = (select IdBuyer from [Order] where Id = ?) and Credit >= ?";
		String newTransaction = "insert into [Transaction](Amount, IdOrder, TimeOfExecution, IdShopBuyer) values(?,?,?,?)";
		try {
			PreparedStatement status = connection.prepareStatement(updateStatus);
			Timestamp ts = new Timestamp(pd150258_GeneralOperations.currentTime.getTimeInMillis());
			status.setTimestamp(1, ts);
			status.setInt(2, orderId);
			int cnt1 = status.executeUpdate();
			if (cnt1 == 0) {
				return -1;
			}

			BigDecimal priceToPay = getFinalPriceComp(orderId);
			if (priceToPay == null) {
				return -1;
			}

			PreparedStatement remove = connection.prepareStatement(removeCredit);
			remove.setBigDecimal(1, priceToPay);
			remove.setInt(2, orderId);
			remove.setBigDecimal(3, priceToPay);

			int cnt2 = remove.executeUpdate();

			if (cnt2 == 0) {
				return -1;
			}
			
			PreparedStatement buy = connection.prepareStatement("select IdBuyer from [Order] where Id=?");
			buy.setInt(1, orderId);
			ResultSet res = buy.executeQuery();
			int idBuyer=0;
			if(res.next()) {
				idBuyer=res.getInt(1);
			}else {
				return -1;
			}
			
			PreparedStatement newTr = connection.prepareStatement(newTransaction);
			newTr.setBigDecimal(1, priceToPay);
			newTr.setInt(2, orderId);
			newTr.setTimestamp(3, new Timestamp(pd150258_GeneralOperations.currentTime.getTimeInMillis()));
			newTr.setInt(4, idBuyer);
			int cnt3 = newTr.executeUpdate();
			
			if (cnt3 == 0) {
				return -1;
			}
			
			boolean done = locationOperations(orderId);
			
			if(!done) {
				return -1;
			}
			//System.out.println("ok");
			return 1;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}
	
	public static boolean locationOperations(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String buyerCity = "select sb.IdCity "
				+ "from [Order] o join ShopBuyer sb on (sb.Id = o.IdBuyer) "
				+ "where o.Id = ?";
		String shopCities = "select distinct sb.IdCity "
				+ "from OrderItems oi "
				+ "join Article a on (a.Id = oi.IdArticle) "
				+ "join ShopBuyer sb on (sb.Id=a.IdShop) "
				+ "where oi.IdOrder = ?";
		int buyerCityId=0;
		LinkedList<Integer> shopCitiesIds = new LinkedList<>();
		try {
			PreparedStatement bc = connection.prepareStatement(buyerCity);
			bc.setInt(1, orderId);
			ResultSet rsbc = bc.executeQuery();
			
			if(rsbc.next()) {
				buyerCityId=rsbc.getInt(1);
			}else {
				return false;
			}
			
			PreparedStatement sc = connection.prepareStatement(shopCities);
			sc.setInt(1, orderId);
			
			ResultSet rssc = sc.executeQuery();
			while(rssc.next()) {
				shopCitiesIds.add(rssc.getInt(1));
				//System.out.println("a "+rssc.getInt(1));
			}
			
			int closestToBuyerPathLenght=10000;
			int closestToBuyerId=0;
			int furthestToBuyerPathLenght=0;
			int furthestToBuyerId=0;
			
			for(Integer id:shopCitiesIds) {
				int tmp = shortestPath(buyerCityId, id);
				if(tmp<=closestToBuyerPathLenght) {
					closestToBuyerPathLenght=tmp;
					closestToBuyerId=id;
				}
				if(tmp>furthestToBuyerPathLenght) {
					furthestToBuyerPathLenght=tmp;
					furthestToBuyerId=id;
				}
			}
			
			//System.out.println("najblizi razd"+closestToBuyerPathLenght);
			//System.out.println("najdalji razd"+furthestToBuyerPathLenght);
			
			int intermediateTime = shortestPath(closestToBuyerId,furthestToBuyerId);
			
			Calendar date1 = Calendar.getInstance();
			date1.setTimeInMillis(pd150258_GeneralOperations.currentTime.getTimeInMillis());
			date1.add(Calendar.DATE, intermediateTime);
			
			String insertOL = "insert into OrderLocation(IdCity,IdOrder,TimeOfArrival) values(?,?,?)";
			PreparedStatement psInsOL = connection.prepareStatement(insertOL);
			psInsOL.setInt(1, closestToBuyerId);
			psInsOL.setInt(2, orderId);
			psInsOL.setTimestamp(3, new Timestamp(date1.getTimeInMillis()));
			
			if(psInsOL.executeUpdate()==0) {
				return false;
			}
			
			String k2CityIdsStr = shortestPathString(closestToBuyerId, buyerCityId);
			
			List<Integer> k2CityIds = Stream.of(k2CityIdsStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			
			for(int i=1;i<k2CityIds.size();i++) {
				int time = shortestPath(k2CityIds.get(i-1),k2CityIds.get(i));
				date1.add(Calendar.DATE, time);
				
				psInsOL.setInt(1, k2CityIds.get(i));
				psInsOL.setInt(2, orderId);
				psInsOL.setTimestamp(3, new Timestamp(date1.getTimeInMillis()));
				
				if(psInsOL.executeUpdate()==0) {
					return false;
				}
			}
			
			//provara
			/*Statement stmt = connection.createStatement();
			ResultSet prov = stmt.executeQuery("select TimeOfArrival,IdCity from OrderLocation");
			while(prov.next()) {
				Timestamp ts = prov.getTimestamp(1);
				Calendar cal =(Calendar) pd150258_GeneralOperations.currentTime.clone();
				cal.setTimeInMillis(pd150258_GeneralOperations.currentTime.getTimeInMillis());

				cal.setTimeInMillis(ts.getTime());
				System.out.println("mes "+cal.get(Calendar.MONTH)+" dat "+cal.get(Calendar.DATE));
				
				System.out.println(prov.getInt(2));
			}*/
			//provara
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public BigDecimal getFinalPriceComp(int orderId) {

		String s = "";
		s = getState(orderId);
		if (s.equals("created")) {
			return null;
		}

		Connection connection = DB.getInstance().getConnection();
		String sql = "{call SP_FINAL_PRICE (?, ?, ?, ?)}";
		try {
			CallableStatement cs = connection.prepareCall(sql);
			cs.setBoolean(1, true);
			cs.setTimestamp(2, new Timestamp(pd150258_GeneralOperations.currentTime.getTimeInMillis()));
			cs.setInt(3, orderId);
			cs.registerOutParameter(4, Types.DECIMAL);

			cs.execute();

			BigDecimal bd = cs.getBigDecimal(4).setScale(3);
			
			return bd;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	public BigDecimal getFinalPrice(int orderId) {

		String s = "";
		s = getState(orderId);
		if (s.equals("created")) {
			return null;
		}

		Connection connection = DB.getInstance().getConnection();
		String sql = "{call SP_FINAL_PRICE (?, ?, ?, ?)}";
		try {
			CallableStatement cs = connection.prepareCall(sql);
			cs.setBoolean(1, false);
			cs.setTimestamp(2, new Timestamp(pd150258_GeneralOperations.currentTime.getTimeInMillis()));
			cs.setInt(3, orderId);
			cs.registerOutParameter(4, Types.DECIMAL);

			cs.execute();

			BigDecimal bd = cs.getBigDecimal(4).setScale(3);
			
			return bd;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public BigDecimal getDiscountSum(int orderId) {
		BigDecimal discountedPrice = getFinalPrice(orderId);
		if (discountedPrice == null) {
			return null;
		}

		Connection connection = DB.getInstance().getConnection();
		String sql = "select sum(a.ArticlePrice*oi.Count)" + "from OrderItems oi join Article a on(oi.IdArticle = a.Id)"
				+ "where oi.IdOrder = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				BigDecimal originalPrice = BigDecimal.valueOf(rs.getInt(1));
				return originalPrice.subtract(discountedPrice);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getState(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select State from [Order] where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getString(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Calendar getSentTime(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select SentTime from [Order] where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				Timestamp ts = rs.getTimestamp(1);
				if (ts == null) {
					return null;
				}
				Calendar cal =(Calendar) pd150258_GeneralOperations.currentTime.clone();
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
	public Calendar getRecievedTime(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select ReceivedTime from [Order] where Id = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Timestamp ts = rs.getTimestamp(1);
				if (ts == null) {
					return null;
				}
				Calendar cal = (Calendar) pd150258_GeneralOperations.currentTime.clone();
				cal.setTimeInMillis(pd150258_GeneralOperations.currentTime.getTimeInMillis());
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
	public int getBuyer(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "select IdBuyer from [Order] where Id = ?";
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
	public int getLocation(int orderId) {
		String s = "";
		s = getState(orderId);
		if (s.equals("created")) {
			return -1;
		}
		Connection connection = DB.getInstance().getConnection();
		String sql = "select IdCity from OrderLocation where IdOrder = ? and TimeOfArrival = "
				+ "(select max(TimeOfArrival) from OrderLocation where IdOrder = ? and TimeOfArrival <= ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderId);
			ps.setInt(2, orderId);
			ps.setTimestamp(3, new Timestamp(pd150258_GeneralOperations.currentTime.getTimeInMillis()));
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return rs.getInt(1);
			}else {
				String sql2 = "select IdCity from OrderLocation where IdOrder = ?";
				PreparedStatement ps2= connection.prepareStatement(sql2);
				ps2.setInt(1, orderId);
				
				ResultSet rs2=ps2.executeQuery();
				
				if(rs2.next()) {
					return rs2.getInt(1);
				}else {
					return -1;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return -1;
	}
	
	public static int shortestPath(int source, int destination) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "{call FindShortestGraphPath (?, ?, ?,?)}";
		try {
			CallableStatement cs = connection.prepareCall(sql);
			cs.setInt(1, source);
			cs.setInt(2, destination);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			
			cs.execute();
			
			int res = cs.getInt(3);
			String path = cs.getString(4);
			
			//System.out.println(res);
			//System.out.println(path);
			return res;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public static String shortestPathString(int source, int destination) {
		Connection connection = DB.getInstance().getConnection();
		String sql = "{call FindShortestGraphPath (?, ?, ?,?)}";
		try {
			CallableStatement cs = connection.prepareCall(sql);
			cs.setInt(1, source);
			cs.setInt(2, destination);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			
			cs.execute();
			
			int res = cs.getInt(3);
			String path = cs.getString(4);
			
			//System.out.println(path);
			
			return path;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
