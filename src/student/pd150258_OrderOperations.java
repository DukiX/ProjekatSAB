package student;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import operations.OrderOperations;

public class pd150258_OrderOperations implements OrderOperations {

	@Override
	public int addArticle(int orderId, int articleId, int count) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeArticle(int orderId, int articleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> getItems(int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int completeOrder(int orderId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getFinalPrice(int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getDiscountSum(int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getState(int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getSentTime(int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getRecievedTime(int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBuyer(int orderId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLocation(int orderId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
