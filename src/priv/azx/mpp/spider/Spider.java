package priv.azx.mpp.spider;

import java.io.IOException;
import java.text.ParseException;

public class Spider {

	public static String LOCAL_URL = "http://gupiao.baidu.com/api/stocks/stockdaybar";

	public static void main(String args[])
			throws IOException, InstantiationException, IllegalAccessException, ParseException, InterruptedException {

		ProcessAssigner processAssigner = new ProcessAssigner(5);

		GenerateThread generateThread = new GenerateThread(processAssigner);
		new Thread(generateThread).start();

		for (int j = 1; j <= 7; j++) {
			ProcessThread processThread = new ProcessThread(processAssigner, j, LOCAL_URL);
			new Thread(processThread).start();
		}

		while (true) {
			;
			if (processAssigner == null)
				break;
		}

	}

}
