package dynamicprograming;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class MinimizeCashFlow {

	public static void main(String[] args){
		List<Transaction> transactions = new LinkedList<>();
		transactions.add(new Transaction(0, 1, 1));
		transactions.add(new Transaction(0, 2, 2));
		transactions.add(new Transaction(1, 2, 5));

		int[] a = MinimizeCashFlow.getNetAmount(transactions, 3);
		MinimizeCashFlow.test(a, 2);
		
		int[] b =  {-60, -30, -20, 50, 25, 35};
		MinimizeCashFlow.test(b, 4);
		
		int[] c =  {-8, -7, -6, 2, 2, 2, 2, 6, 7};
		MinimizeCashFlow.test(c, 6);
	}

	public static void test(int[] netAmount, int expectedCount) {
		/*
		 * IntStream.of(netAmount).forEach(System.out::print);
		 * System.out.println();
		 */
		List<Transaction> minTxns;
		try {
			minTxns = MinimizeCashFlow.getMinimumTransactions(netAmount);
			System.out.println("Expected Transactions " + expectedCount + ", Actual " + minTxns.size());
			System.out.println(minTxns.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static int[] getNetAmount(List<Transaction> trans, int size) {
		int[] netAmount = new int[size];
		for (Transaction t : trans) {
			netAmount[t.getSender()] -= t.getAmount();
			netAmount[t.getReceiver()] += t.getAmount();
		}
		return netAmount;
	}

	public static List<Transaction> getMinimumTransactions(int[] netAmount) throws Exception {
		int netAmountSum = IntStream.of(netAmount).sum();
		if (netAmountSum != 0)
			throw new Exception("Non Zero Net Sum : " + netAmountSum);

		List<Transaction> minTxns = new LinkedList<>();
		int minDebitValue = IntStream.of(netAmount).map(Math::abs).max().getAsInt();
		Mapper[] txnMapper = new Mapper[minDebitValue + 1];
		txnMapper[0] = new Mapper(-1, 0);

		for (int i = 0; i < netAmount.length; i++) {
			for (int j = txnMapper.length - 1; netAmount[i] < 0 && j >= 0; j--) {
				int amountToPay = -netAmount[i];
				if ((null != txnMapper[j]) && (j + amountToPay < txnMapper.length)
						&& ((null == txnMapper[j + amountToPay])
								|| (txnMapper[j + amountToPay].min > txnMapper[j].min + 1))) {
					txnMapper[j + amountToPay] = new Mapper(i, txnMapper[j].min + 1);
				}
			}
		}
		/*
		 * Stream.of(txnMapper).forEach(System.out::println);
		 * System.out.println();
		 */
		for (int i = 0; i < netAmount.length; i++) {
			if (netAmount[i] > 0) {
				if (null == txnMapper[netAmount[i]])
					throw new Exception("Failed to find solution for user " + i + " for value " + netAmount[i]);
				int mapperIndex = netAmount[i];
				while (mapperIndex > 0) {
					minTxns.add(new Transaction(txnMapper[mapperIndex].r, i, -netAmount[txnMapper[mapperIndex].r]));
					mapperIndex += netAmount[txnMapper[mapperIndex].r];
				}
			}
		}
		return minTxns;
	}
}

class Mapper {
	public int r;
	public int min;

	Mapper(int receiver, int minTxns) {
		this.r = receiver;
		this.min = minTxns;
	}

	@Override
	public String toString() {
		return "Receiver " + this.r + " min txns " + this.min;
	}
}

class Transaction {
	private int sender;
	private int receiver;
	private int amount;

	Transaction(int s, int r, int amount) {
		this.sender = s;
		this.receiver = r;
		this.amount = amount;
	}

	public int getSender() {
		return sender;
	}

	public int getReceiver() {
		return receiver;
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "Transfer " + this.amount + " from person " + this.sender + " to person " + this.receiver;
	}
}
