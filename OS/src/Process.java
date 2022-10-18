import java.util.*;

public class Process {

	int pc;
	ArrayList<Instruction> instructions;
	String name;
	int arrival;
	HashMap<String, String> variables;

	public Process(String name, ArrayList<Instruction> instructions,int arrival) {
		this.name = name;
		pc = 0;
		this.instructions = instructions;
		this.arrival = arrival;
		variables = new HashMap<>();
	}
	public String toString() {
		return name;
	}

}
