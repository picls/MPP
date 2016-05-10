package priv.azx.mpp.data;

public class KLine {

	public double open;
	public double high;
	public double low;
	public double close;
	public long volume;
	public double amount;
	public double netChangeRatio;

	public KLine() {

	}

	public KLine(double open, double high, double low, double close, long volume, double amount,
			double netChangeRatio) {
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.amount = amount;
		this.netChangeRatio = netChangeRatio;
	}

}
