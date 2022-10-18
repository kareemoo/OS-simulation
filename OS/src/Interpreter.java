import java.util.*;
import java.io.*;

public class Interpreter {

	static Scanner sc = new Scanner(System.in);

	public void interpret(String file_name, int arrival) {
		try {
//            System.out.println("Running " + file_name);
			File myObj = new File(file_name + ".txt");
			Scanner sc = new Scanner(myObj);
			ArrayList<Instruction> instructions = new ArrayList<>();
			int instructionNum = 1;
			while (sc.hasNextLine()) {
				String[] instruction = sc.nextLine().split(" ");
				if (instruction[0].equals("assign")) {
					if (instruction.length == 4) {
						instructions.add(
								new Instruction(new String[] { instruction[0], "tmp", instruction[2], instruction[3] },
										instructionNum));
						instructions.add(new Instruction(new String[] { instruction[0], instruction[1], "tmp" },
								instructionNum));
					} else if (instruction[2].equals("input")) {
						instructions.add(new Instruction(new String[] { instruction[0], "tmp", instruction[2] },
								instructionNum));
						instructions.add(new Instruction(new String[] { instruction[0], instruction[1], "tmp" },
								instructionNum));
					} else {
						instructions.add(new Instruction(instruction, instructionNum));
					}
				} else
					instructions.add(new Instruction(instruction, instructionNum));
				instructionNum++;
			}
			Process p = new Process(file_name, instructions, arrival);
			OS.processes.add(p);
			sc.close();

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
//			e.printStackTrace();
		}

	}

}