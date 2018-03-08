/**
*
*	Created by: David M Williams
*	Program's function: To keep track of certain and all tasks, allowing for data to be stored on each task
*	Team: TBA (Currently a solo project)
*	Class: Sacramento State University CSC 131
*
**/

/**
*
*	Import time functions for timing the operations,
*	as well as use them for calculations on timing for tasks
*	Import standard java.io for regular usage functions
*	Use java.util for certain operations to complete summary of tasks
*	as well as perhaps other smaller operations
*
**/
import java.io.*;
import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;



// The whole program relies on this class to run appropriately
public class TM {

	ITMModel tmModel = new TMModel();

	// This Usage() function is to be called once an error occurs in the program
	// It will state how to use the program correctly when called
	public void Usage() {
		System.err.println("To use this program you must run it by one of the following:");
		System.err.println("TM Start <Task>");
		System.err.println("TM Stop <Task>");
		System.err.println("TM Describe <Task> <Description, Using Quotes> [Optional]<Size>[Optional]");
		System.err.println("TM Summary <Task>");
		System.err.println("TM Summary");
		System.err.println("TM Size <Task>");
	}


	// This is the portion of the application that runs first
	public static void main(String[] args) throws IOException {
		// The only function of this main(...) is to run the next set of tasks
		// This is a simplified statement that creates a new TM() function
		// It then runs appMain with the args array that is supplied when the program is run
		new TM().appMain(args);
	}

	// appMain is called through the main(...) method
	// If there is an error it will throw an IOException,
	// Basically a try catch function without the need for all catch statements 
	void appMain(String [] args) throws IOException {

		// for the following comments: "~" = "Stands For"/"Represents"
		// Initialize cmd with empty quotes so it has a starting value in memory
		// cmd ~ command
		String cmd = "";

		// Create a new Log() function
		// This will allow all commands, task name, descriptions, etc. to be written to a log file and be stored
		Log log = new Log();

		// Create a new LocalDateTime with name of timeRN, this will get the exact current time when called
		// This will allow for time calculations and start and end times to be completed
		LocalDateTime timeRN = LocalDateTime.now();

		if(args.length == 0){
			Usage();
			return;
		}
		try{
			cmd = args[0];

			cmd = cmd.toUpperCase();
		}
		catch (Exception err)
		{
			System.err.println("Error encountered when setting command to upper case");
		}

		switch(cmd) {
			case "START":
				if (args.length == 2) {
					tmModel.startTask(args[1]);
				}
				break;
			case "STOP":
				if (args.length == 2) {
					tmModel.stopTask(args[1]);
				}
				break;
			case "SUMMARY":
				if (args.length == 1)
					summaryFull();
				else
					summary(args[1]);
			case "DESCRIBE":
				if (args.length == 3) {
					tmModel.describeTask(args[1], args[2]);
				}
				if (args.length == 4) {
					tmModel.sizeTask(args[1], args[3]);
				}
				break;
			case "DELETE":
				if (args.length == 2) {
					tmModel.deleteTask(args[1]);
				}
				break;
			case "RENAME":
				if (args.length == 3) {
					tmModel.renameTask(args[1], args[2]);
				}
				break;
			case "SIZE":
				if (args.length == 3) {
					tmModel.sizeTask(args[1], args[2]);
				}
				break;
			default:
				Usage();
		}
		return;
	}


	public void summaryFull() {
		Set<String> NOMBRES = tmModel.taskNames();
		Set<String> ElSizes = tmModel.taskSizes();
		for (String individual : NOMBRES) {
			summary( individual );
		}

		for (String single : ElSizes) {
			Set<String> strset = tmModel.taskNamesForSize(single);

			if (strset.size() >= 1) {
				System.out.println("Size: " + single + " Tasks: " + strset + "\n Min Time: " + tmModel.minTimeForSize(single)
								   + "\n Avg Time: " + tmModel.avgTimeForSize(single)
								   + "\n Max Time: " + tmModel.maxTimeForSize(single) + "\n");
			}
		}

	}


	public void summary ( String name ) {
		String size = tmModel.taskSize(name);
		String desc = tmModel.taskDescription(name);
		String timeOverall = tmModel.taskElapsedTime(name);

		System.out.println("==========================================\n");
		System.out.println("\n Task: " + name + "\n Task Description: " + desc
						   + "\n Task Size: " + size
						   + "\n Overall Time: " + timeOverall );
		System.out.println("==========================================\n");
	}


}