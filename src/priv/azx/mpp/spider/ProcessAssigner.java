package priv.azx.mpp.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProcessAssigner {
	
	public BlockingQueue<String> queue;
	public List<ProcessThread> threadList;

	public ProcessAssigner(int size) {
		queue = new ArrayBlockingQueue<String>(size);
	}
	
	public ProcessAssigner(int size, int number) {
		queue = new ArrayBlockingQueue<String>(size);
		threadList = new ArrayList<ProcessThread>();
		for (int i = 0; i < number; i++) {
			ProcessThread processThread = new ProcessThread(this, i + 1, null);
			new Thread(processThread).start();
			threadList.add(processThread);
		}
	}

	public synchronized boolean isFull() {
		if (queue.size() >= 5)
			return true;
		else
			return false;
	}

	public void add(String code) throws InterruptedException {
		queue.put(code);
		System.out.println("code " + code + " into queue");
	}

	public String get() throws InterruptedException {
		String code = queue.take();
		System.out.println("code " + code + " out queue");
		return code;
	}

	public void process() throws InterruptedException {
		for (ProcessThread processThread : threadList) {
			if (processThread.code == null)
				processThread.code = this.get();
		}
	}

}
