package student;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import operations.TransactionOperations;

public class pd150258_TransactionOperations implements TransactionOperations {

	@Override
	public BigDecimal getBuyerTransactionsAmmount(int buyerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getShopTransactionsAmmount(int shopId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getTransationsForBuyer(int buyerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionForBuyersOrder(int orderId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTransactionForShopAndOrder(int orderId, int shopId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> getTransationsForShop(int shopId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getTimeOfExecution(int transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getAmmountThatBuyerPayedForOrder(int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getAmmountThatShopRecievedForOrder(int shopId, int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getTransactionAmount(int transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getSystemProfit() {
		// TODO Auto-generated method stub
		return null;
	}

}
