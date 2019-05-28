package student;

import java.math.BigDecimal;
import java.util.List;

import operations.BuyerOperations;

public class pd150258_BuyerOperations implements BuyerOperations {

	@Override
	public int createBuyer(String name, int cityId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setCity(int buyerId, int cityId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCity(int buyerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal increaseCredit(int buyerId, BigDecimal credit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createOrder(int buyerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> getOrders(int buyerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getCredit(int buyerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
