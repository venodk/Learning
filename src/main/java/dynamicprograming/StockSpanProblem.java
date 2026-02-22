package dynamicprograming;

public class StockSpanProblem {

	public static void main(String[] args) {
		int[] a = { 100, 180, 260, 310, 40, 535, 695 };
		StockSpanProblem.findMaxProfit(a, 2); // 865

		int[] b = { 10, 22, 5, 75, 65, 80 };
		StockSpanProblem.findMaxProfit(b, 2); // 87

		int[] c = { 2, 30, 15, 10, 8, 25, 80 };
		StockSpanProblem.findMaxProfit(c, 2); // 100

		int[] d = { 100, 30, 15, 10, 8, 25, 80 }; // 72
		StockSpanProblem.findMaxProfit(d, 2);

		int[] e = { 90, 80, 70, 60, 50 };
		StockSpanProblem.findMaxProfit(e, 2); // 0

		int[] f = { 12, 14, 17, 10, 14, 13, 12, 15 };
		StockSpanProblem.findMaxProfit(f, 3); // 12

		int[] g = { 100, 30, 15, 10, 8, 25, 80 };
		StockSpanProblem.findMaxProfit(g, 3); // 72

		int[] h = { 5, 75, 30, 42, 17, 22 };
		StockSpanProblem.findMaxProfit(h, 2); // 82
	}

	public static void findMaxProfit(int[] price, int k) {
		PurchaseList list = new PurchaseList(price);
		list.findMaxProfit(k);
		list.print();
	}
}

class PurchaseList {

	private static class PurchaseNode {
		int buy;
		int sell;
		PurchaseNode next;

		PurchaseNode(int b, int s) {
			buy = b;
			sell = s;
		}
	}

	PurchaseNode head;
	PurchaseNode tail;
	int count = 0;

	PurchaseList(int[] price) {
		int i = 0;
		while (i < price.length - 1) {
			while ((i < price.length - 1) && (price[i + 1] <= price[i]))
				i++;

			if (i == price.length - 1)
				break;

			int buy = price[i++];

			while ((i < price.length) && (price[i] >= price[i - 1]))
				i++;

			int sell = price[i - 1];
			add(buy, sell);
		}
	}

	private void add(int b, int s) {
		PurchaseNode e = new PurchaseNode(b, s);
		if (null == tail) {
			this.head = e;
        } else {
			this.tail.next = e;
        }
        this.tail = e;
        this.count++;
	}

	public void print() {
		PurchaseNode current = this.head;
		int sum = 0;
		while (null != current) {
			System.out.println(
					"Buy " + current.buy + " Sell " + current.sell + " Profit " + (current.sell - current.buy));
			sum += current.sell - current.buy;
			current = current.next;
		}
		System.out.println("Total Profit " + sum);
		System.out.println();
	}

	public void findMaxProfit(int k) {
		if (k < 2)
			return;

		while (count > k) {
			PurchaseNode minLoseNode = getMinLoseNode();
			PurchaseNode next = minLoseNode.next;
			int buy = minLoseNode.buy;
			int sell = minLoseNode.sell;
			if (next.sell - next.buy > sell - buy) {
				buy = next.buy;
				sell = next.sell;
			}
			if (next.sell - minLoseNode.buy > sell - buy) {
				buy = minLoseNode.buy;
				sell = next.sell;
			}
			minLoseNode.next = next.next;
			if (tail == next)
				tail = minLoseNode;

			minLoseNode.buy = buy;
			minLoseNode.sell = sell;
			count--;
		}
	}

	private PurchaseNode getMinLoseNode() {

		PurchaseNode current = this.head;
		PurchaseNode next;
		PurchaseNode minLoseNode = null;
		int maxProfit = Integer.MIN_VALUE;

		while (null != current && null != current.next) {
			next = current.next;
			int currentSaleProfit = current.sell - current.buy;
			int nextSaleProfit = next.sell - next.buy;
			int profitIfMerged = next.sell - current.buy;
			int combinedProfit = currentSaleProfit + nextSaleProfit;
			int profitAfterMerge = max(currentSaleProfit, nextSaleProfit, profitIfMerged) - combinedProfit;
			if (maxProfit < profitAfterMerge) {
				maxProfit = profitAfterMerge;
				minLoseNode = current;
			}
			current = next;
		}
		return minLoseNode;
	}

	public static int max(int a, int b, int c) {
		return Math.max(a, Math.max(b, c));
	}
}
