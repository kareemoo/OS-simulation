import java.util.*;

public class Scheduler {

	public int time = 0;
	public Queue<Process> readyQueue = new LinkedList<>();
	public Queue<Process> blockedQueue = new LinkedList<>();

	public Queue<Process> FileblockedQueue = new LinkedList<>();
	public Queue<Process> userOutputblockedQueue = new LinkedList<>();
	public Queue<Process> userInputblockedQueue = new LinkedList<>();

	public Process runningProcess;
	public int timeSlice = 2;

	public void schedule() {
		Collections.sort(OS.processes, (a, b) -> a.arrival - b.arrival);
		arrive();
		while (!readyQueue.isEmpty()) {
			run();
		}
	}

	public void run() {
		runningProcess = readyQueue.poll();
		System.out.println(runningProcess + " is chosen, " + "Ready Queue : " + readyQueue + ", Blocked Queue : "
				+ blockedQueue + ", File Blocked Queue : " + FileblockedQueue + ", UserInput Blocked Queue : "
				+ userInputblockedQueue + ", UserOutput Blocked Queue : " + userOutputblockedQueue);
		for (int i = 0; i < timeSlice; i++) {

			if (runningProcess.pc == runningProcess.instructions.size()) {
				System.out.println(runningProcess + " finished, " + "Ready Queue : " + readyQueue + ", Blocked Queue : "
						+ blockedQueue + ", File Blocked Queue : " + FileblockedQueue + ", UserInput Blocked Queue : "
						+ userInputblockedQueue + ", UserOutput Blocked Queue : " + userOutputblockedQueue);
				runningProcess = null;
				return;
			}
			
			System.out.println("current time : " + time + ", " + runningProcess.name + " instruction_"
					+ (runningProcess.instructions.get(runningProcess.pc).num));

			String[] instruction = runningProcess.instructions.get(runningProcess.pc).instruction;
			String function = instruction[0];
			String[] argument = split(instruction, 1, instruction.length);
			execute(function, argument);

			time++;
			arrive();
			
			runningProcess.pc++;
			if (blockedQueue.contains(runningProcess)) {
				System.out.println(runningProcess + " is blocked, " + "Ready Queue : " + readyQueue
						+ ", Blocked Queue : " + blockedQueue + ", File Blocked Queue : " + FileblockedQueue
						+ ", UserInput Blocked Queue : " + userInputblockedQueue + ", UserOutput Blocked Queue : "
						+ userOutputblockedQueue);
				runningProcess = null;
				return;
			}
		}
		if (runningProcess.pc != runningProcess.instructions.size()) {
			readyQueue.add(runningProcess);
			runningProcess = null;
		} else {
			System.out.println(runningProcess + " finished, " + "Ready Queue : " + readyQueue + ", Blocked Queue : "
					+ blockedQueue + ", File Blocked Queue : " + FileblockedQueue + ", UserInput Blocked Queue : "
					+ userInputblockedQueue + ", UserOutput Blocked Queue : " + userOutputblockedQueue);
		}
	}

	public void arrive() {
		while (OS.nextProcess != OS.processes.size() && time < OS.processes.get(0).arrival) {
			time++;
		}
		while (OS.nextProcess != OS.processes.size() && OS.processes.get(OS.nextProcess).arrival == time) {
			readyQueue.add(OS.processes.get(OS.nextProcess));
			OS.nextProcess++;
		}
	}

	public String execute(String function, String[] arguments) {
		// Getting the values of each variable
		for (int i = 0; i < arguments.length; i++) {
			// If the argument is in the variables hash map return its value else return the
			// argument as it is
			arguments[i] = runningProcess.variables.getOrDefault(arguments[i], arguments[i]);
		}

		switch (function) {
		case "print":
			System.out.println(arguments[0]);
			return null;
		case "assign":
			String variable = arguments[0];
			String value = arguments.length == 2 ? arguments[1]
					: this.execute(arguments[1], split(arguments, 2, arguments.length));
			SystemCalls.assign(runningProcess, variable, value);
			return null;
		case "writeFile":
			String fileName = arguments[0];
			String content = arguments[1];
			SystemCalls.createFile(fileName);
			SystemCalls.writeToFile(fileName, content);
			return null;
		case "readFile":
			String readFileName = arguments[0];
			String out = SystemCalls.readFile(readFileName);
			return out;

		case "printFromTo":
			try {
				int start = Integer.parseInt(arguments[0]);
				int end = Integer.parseInt(arguments[1]);
				SystemCalls.printFromTo(start, end);
			} catch (Exception e) {
				System.out.println("You didn't enter valid numbers");
			}
			return null;
		case "semWait":
			SystemCalls.semWait(arguments[0]);
			return null;
		case "semSignal":
			SystemCalls.semSignal(arguments[0]);
			return null;
		default:
			return null;

		}
	}

	public static String[] split(String[] arr, int start, int end) {

		String[] split = new String[end - start];

		for (int i = 0; i < split.length; i++) {
			split[i] = arr[start + i];
		}
		return split;
	}
}
