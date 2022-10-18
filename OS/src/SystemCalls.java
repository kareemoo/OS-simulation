import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SystemCalls {

	static Scanner sc = new Scanner(System.in);

	public static void assign(Process p, String variable, String value) {
		if (value.equals("input")) {
			System.out.println("Please enter a value:");
			value = sc.nextLine();
		}
		if (p.variables.containsKey("tmp") && p.variables.get("tmp").equals(value)) {
			p.variables.remove("tmp");
		}
		p.variables.put(variable, value);

	}

	public static void printFromTo(int start, int end) {

		if (start == end) {
			System.out.println(start);
		} else if (start < end) {
			for (int i = start; i <= end; i++)
				System.out.println(i);
		} else {
			for (int i = start; i >= end; i--)
				System.out.println(i);
		}

	}

	public static String readFile(String fileName) {
		try {
			File myObj = new File(fileName + ".txt");
			Scanner sc = new Scanner(myObj);
			StringBuilder data = new StringBuilder();
			while (sc.hasNextLine()) {
				data.append(sc.nextLine());
				if (sc.hasNextLine())
					data.append("\n");
			}
			sc.close();
			return data.toString();
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
//			e.printStackTrace();
			return null;
		}

	}

	public static void createFile(String fileName) {
		try {
			File myObj = new File(fileName + ".txt");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
//			e.printStackTrace();
		}
	}

	public static void writeToFile(String fileName, String content) {
		try {
			FileWriter myWriter = new FileWriter(fileName + ".txt");
			myWriter.write(content);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
//			e.printStackTrace();
		}
	}

	public static void semWait(String resource) {
		if (resource.equals("file")) {
			OS.file.semWait(OS.scheduler.runningProcess);
			if (OS.scheduler.blockedQueue.contains(OS.scheduler.runningProcess)) {
				OS.scheduler.FileblockedQueue.add(OS.scheduler.runningProcess);
			}
		} else if (resource.equals("userInput")) {
			OS.userInput.semWait(OS.scheduler.runningProcess);
			if (OS.scheduler.blockedQueue.contains(OS.scheduler.runningProcess)) {
				OS.scheduler.userInputblockedQueue.add(OS.scheduler.runningProcess);
			}
		} else if (resource.equals("userOutput")) {
			OS.userOutput.semWait(OS.scheduler.runningProcess);
			if (OS.scheduler.blockedQueue.contains(OS.scheduler.runningProcess)) {
				OS.scheduler.userOutputblockedQueue.add(OS.scheduler.runningProcess);
			}
		}
	}

	public static void semSignal(String resource) {
		if (resource.equals("file")) {
			OS.file.semSignal(OS.scheduler.runningProcess, OS.scheduler.FileblockedQueue.peek());
			OS.scheduler.FileblockedQueue.poll();
		} else if (resource.equals("userInput")) {
			OS.userInput.semSignal(OS.scheduler.runningProcess, OS.scheduler.userInputblockedQueue.peek());
			OS.scheduler.userInputblockedQueue.poll();
		} else if (resource.equals("userOutput")) {
			OS.userOutput.semSignal(OS.scheduler.runningProcess, OS.scheduler.userOutputblockedQueue.peek());
			OS.scheduler.userOutputblockedQueue.poll();
		}
	}

}
