import java.util.*;

public class OS {

	public static Interpreter interpreter = new Interpreter();
	public static Scheduler scheduler = new Scheduler();
	public static Mutex file = new Mutex();
	public static Mutex userInput = new Mutex();
	public static Mutex userOutput = new Mutex();
	public static ArrayList<Process> processes = new ArrayList<>();
	public static int nextProcess = 0;

	public static void main(String[] args) {
		interpreter.interpret("Program_1", 0);
		interpreter.interpret("Program_2", 1);
		interpreter.interpret("Program_3", 4);

		scheduler.schedule();
	}

}
