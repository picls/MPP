package priv.azx.mpp.spider;

import java.util.List;

import priv.azx.mpp.data.DateDataService;

@SuppressWarnings("unused")
public class GenerateThread implements Runnable {

	public ProcessAssigner processAssigner;

	public GenerateThread(ProcessAssigner processAssigner) {
		this.processAssigner = processAssigner;
	}

	public void run() {
		List<String> codeStringList = DateDataService.indexCodes;
		for(String code : codeStringList) {
			try {
				processAssigner.add(code);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * for (int i = 1; i <= 180; i++) { String code = "sh"; if (i < 10) code
		 * += "60000" + i; else if (i < 100) code += "6000" + i; else if (i <
		 * 1000) code += "600" + i; else code += "60" + i; try {
		 * processAssigner.add(code); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */
	}
	
	

}
