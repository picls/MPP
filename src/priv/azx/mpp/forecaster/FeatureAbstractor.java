package priv.azx.mpp.forecaster;

import java.util.ArrayList;
import java.util.List;

import priv.azx.mpp.data.DateData;

public class FeatureAbstractor {

	private static int netChangeRatioSeq = 0;
	private static int redGreenBodySeq = 1;
	private static int upShadowPCTSeq = 2;
	private static int downShadowPCTSeq = 3;
	private static int openPCTSeq = 4;
	private static int riseFallStopSeq = 5;
	private static int lowToLastSeq = 6;
	private static int highToLastSeq = 7;

	public static List<Double> getDayFeature(DateData d, DateData yd) {
		List<Double> feature = new ArrayList<Double>();

		feature.add(d.kLine.netChangeRatio);
		// feature.add(Math.floor(d.kLine.netChangeRatio / 100d) +
		// Math.ceil(d.kLine.netChangeRatio / 100d));
		feature.add(isRedBody(d) ? 1d : isGreenBody(d) ? -1d : 0d);
		feature.add(getUpShadowPCT(yd, d));
		feature.add(getDownShadowPCT(yd, d));
		feature.add(getOpenPCT(yd, d));
		feature.add(isRiseStop(d) ? 1d : isFallStop(d) ? -1d : 0d);
		feature.add(d.kLine.low > yd.kLine.high ? 1d : 0d);
		feature.add(d.kLine.high < yd.kLine.low ? 1d : 0d);
		feature.add(d.kLine.amount);

		return feature;
	}

	private static double getAbsDiff(List<Double> dl1, List<Double> dl2, int n) {
		return Math.abs((dl1.get(n) - dl2.get(n)));
	}

	private static double getPlusSqrt(double s, double p) {
		return Math.sqrt(s * p);
	}

	public static double getSimilarity(List<Double> dl1, List<Double> dl2) {
		double similarity = 0d;

		similarity += getPlusSqrt(getAbsDiff(dl1, dl2, netChangeRatioSeq), 5); // 10
		similarity += getPlusSqrt(getAbsDiff(dl1, dl2, redGreenBodySeq), 8); // 4
		similarity += getPlusSqrt(getAbsDiff(dl1, dl2, upShadowPCTSeq), 0.8); // 4
		similarity += getPlusSqrt(getAbsDiff(dl1, dl2, downShadowPCTSeq), 0.8); // 4
		similarity += getPlusSqrt(getAbsDiff(dl1, dl2, openPCTSeq), 1.6); // 4
		similarity += getPlusSqrt(getAbsDiff(dl1, dl2, riseFallStopSeq), 8); // 4
		similarity += getPlusSqrt(getAbsDiff(dl1, dl2, lowToLastSeq), 1); // 1
		similarity += getPlusSqrt(getAbsDiff(dl1, dl2, highToLastSeq), 1); // 1

		return similarity / 32;
	}

	public static boolean isRise(DateData d) {
		return isRiseHigher(d, 0);
	}

	public static boolean isRiseHigher(DateData d, double limit) {
		return isRiseBetween(d, limit, 20);
	}

	public static boolean isRiseLower(DateData d, double limit) {
		return isRiseBetween(d, 0, limit);
	}

	public static boolean isRiseBetween(DateData d, double floor, double ceiling) {
		if ((d.kLine.netChangeRatio > floor) && (d.kLine.netChangeRatio < ceiling))
			return true;
		return false;
	}

	public static boolean isRedBody(DateData d) {
		if (d.kLine.open < d.kLine.close)
			return true;
		return false;
	}

	public static boolean isFall(DateData d) {
		return isFallLower(d, 0);
	}

	public static boolean isFallHigher(DateData d, double limit) {
		return isFallBetween(d, 0, limit);
	}

	public static boolean isFallLower(DateData d, double limit) {
		return isFallBetween(d, limit, 20);
	}

	public static boolean isFallBetween(DateData d, double floor, double ceiling) {
		if ((-d.kLine.netChangeRatio > floor) && (-d.kLine.netChangeRatio < ceiling))
			return true;
		return false;
	}

	public static boolean isGreenBody(DateData d) {
		if (d.kLine.open > d.kLine.close)
			return true;
		return false;
	}

	public static boolean isBetween(DateData d, double floor, double ceiling) {
		if ((d.kLine.netChangeRatio > floor) && (d.kLine.netChangeRatio < ceiling))
			return true;
		return false;
	}

	public static double getOpen(DateData d1, DateData d2) {
		return d1.kLine.close - d2.kLine.open;
	}

	public static double getOpenPCT(DateData d1, DateData d2) {
		return getOpen(d1, d2) / d1.kLine.close * 100;
	}

	public static boolean isOpenLow(DateData d1, DateData d2) {
		return isOpenLower(d1, d2, 0);
	}

	public static boolean isOpenLower(DateData d1, DateData d2, double limit) {
		if (-getOpenPCT(d1, d2) > limit)
			return true;
		return false;
	}

	// public static boolean isOpenLowBetween(DateData d1, DateData d2, double
	// floor, double ceiling) {

	// }

	public static boolean isOpenHigh(DateData d1, DateData d2) {
		return isOpenHigher(d1, d2, 0);
	}

	public static boolean isOpenHigher(DateData d1, DateData d2, double limit) {
		if (getOpenPCT(d1, d2) > limit)
			return true;
		return false;
	}

	public static boolean isStop(DateData d) {
		return (isRiseStop(d)) || (isFallStop(d));
	}

	public static boolean isRiseStop(DateData d) {
		if (d.kLine.netChangeRatio > 9.9)
			return true;
		return false;
	}

	public static boolean isFallStop(DateData d) {
		if (d.kLine.netChangeRatio < -9.9)
			return true;
		return false;
	}

	public static double getUpShadow(DateData d) {
		double highEnd = d.kLine.close;
		if (d.kLine.open > highEnd)
			highEnd = d.kLine.open;
		return d.kLine.high - highEnd;
	}

	public static double getUpShadowPCT(DateData d1, DateData d2) {
		double upShadow = getUpShadow(d2);
		return upShadow / d1.kLine.close * 100;
	}

	public static boolean isUpShadowShorter(DateData d1, DateData d2, double limit) {
		return isUpShadowBetween(d1, d2, 0, limit);
	}

	public static boolean isUpShadowLonger(DateData d1, DateData d2, double limit) {
		return isUpShadowBetween(d1, d2, limit, 20);
	}

	public static boolean isUpShadowBetween(DateData d1, DateData d2, double floor, double ceiling) {
		if ((getUpShadowPCT(d1, d2) > floor) && (getUpShadowPCT(d1, d2) < ceiling))
			return true;
		return false;
	}

	public static double getDownShadow(DateData dateData) {
		double lowEnd = dateData.kLine.close;
		if (dateData.kLine.open < lowEnd)
			lowEnd = dateData.kLine.open;
		return lowEnd - dateData.kLine.low;
	}

	public static double getDownShadowPCT(DateData d1, DateData d2) {
		double downShadow = getDownShadow(d2);
		return downShadow / d1.kLine.close * 100;
	}

	public static boolean isDownShadowShorter(DateData d1, DateData d2, double limit) {
		return isDownShadowBetween(d1, d2, 0, limit);
	}

	public static boolean isDownShadowLonger(DateData d1, DateData d2, double limit) {
		return isDownShadowBetween(d1, d2, limit, 20);
	}

	public static boolean isDownShadowBetween(DateData d1, DateData d2, double floor, double ceiling) {
		if ((getDownShadowPCT(d1, d2) > floor) && (getDownShadowPCT(d1, d2) < ceiling))
			return true;
		return false;
	}

}
