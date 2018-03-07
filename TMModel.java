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



class TaskLogEntry{

	LocalDateTime timeRN;
	String cmd;
	String data;
	String desc;

}

// The whole program relies on this class to run appropriately
public class TMModel implements ITMModel {

	LinkedList<TaskLogEntry> LineL;

	// Initialize Size, for use in declaring tasks as L, XS, S, M, and so on...
	// set the String size to "" so it is initialized and compiles/runs accordingly
	public String Size = "";


	// This is one of the TWO cmdSummary methods, this one only handles the whole output of the log file
	// There is no task supplied in order to run this command
	// Once again, no need for a try/catch block due to the error that will be thrown, if there is one
	void cmdSummary(Log log) throws IOException{

		// Set the initial value of 'null' so it can be used correctly (And will allow it to compile)
		// This is the scanner, I named it 'mine' in order to differentiate that it is reading MY log file that was supplied
		Scanner mine = null;
		// This sets a file named 'myfile' as a new File
		// This means it set the file, that we named "TM.log", as a type of File
		// This will then allow cmdSummary to correctly read the file
		File myfile = new File("TM.log");
		// Set 'mine' as a Scanner, with the File that we set previously
		// This allows the File, 'myfile', to be read by the Scanner class/operator
		mine = new Scanner(myfile);
		// This while loop will loops until there is no more data left to display out of the log file
		while(mine.hasNext()) {

			// This String is newly created every loop
			// Once created, it will set its value as the next line of the log file
			String lineOfFile = mine.nextLine();
			// This statement allows the String 'lineOfFile' to not display null data, allowing everything to be printed correctly
			// It is a fail safe to make sure that we don't try to display empty lines of the log file supplied
			if(!(lineOfFile.contains("null"))) {
				// Once verified the data is not 'null' print the line of the log file we are currently on
				System.out.println(lineOfFile);
			}
		}
//HELP
		// This is confusing I NEED TO COME BACK TO WHY THIS IS HERE
//HELP
		log.readFile();

	}
	
	
	
	
	

	// This will handle the other method of cmdSummary
	// It takes the data of the log file, a well as the Task that is requested to be summarized
	// If there is an IOException, it will be thrown once encountered, thus, ONCE AGAIN no need for a try/catch block
	long cmdSummary(String todo, Log log) throws IOException {

		// Read log file, gather entries
		LinkedList<TaskLogEntry> lines = log.readFile();
		
		// Create Task object based on task name, with entries from log
		Task sumTask = new Task(todo, lines);
		
		// Display the Summary of the task specified from the previous line of code
		// It does this while converting the Task to a String that is read-able for println()
		System.out.println(sumTask.toString());
		
		// Return the total time spent on the task, among the other details that were displayed alongside it
		return sumTask.totTime;
		
	}
	






	







	static class TimeUtil {

		static String toElapsedTime(long totSecs) {

			long hours = totSecs/3600;
			long mins = (totSecs % 3600) / 60;
			long secs = (totSecs % 60);

			String timeNow = (String.format("%02d:%02d:%02d", hours, mins, secs));
			return timeNow;

		}
	}

	class Task {

		private String name = "";
		private String desc = "";
		private String timeAhora = "";
		private long totTime = 0;

		public Task(String name, LinkedList<TaskLogEntry> entries) {
			this.name = name;
			//WHY IS THIS BEING WRITTEN AS THE GODDAMN DESCRIPTION
			this.desc = "";
			LocalDateTime lastStart = null;
			long timeOverall = 0;
			for(TaskLogEntry entry : entries) {
				if(entry.data.equals(name)) {
					switch(entry.cmd) {
						case "START":
							lastStart = entry.timeRN;
							break;
						case "STOP":
							if(lastStart != null) {
								timeOverall += taskDuration(lastStart, entry.timeRN);
							}
							lastStart = null;
							break;
						case "DESCRIBE":
							desc += " " + entry.desc;
					}
				}
			}
			this.timeAhora = TimeUtil.toElapsedTime(timeOverall);
			this.totTime = timeOverall;
		}


		public String toString() {
			String stringbean = ("\nSummary for Task: " + this.name + "\nDescription for Task: " + this.desc + Size + "\nDuration for Task: " + this.timeAhora );
			return stringbean;
		}

		long taskDuration(LocalDateTime start, LocalDateTime stop){
				long dur = ChronoUnit.SECONDS.between(start, stop);
				return dur;

		}

	}

}







//SHOULD NOT TOUCHY


// Here is the Log class
// This class is used for creating and maintaining the Log file's use
// The log file is TM.log
// In this file, it stores all the information that the user presents the program with
class Log{

	String type;
	String name;
	String input;

	// This is a public FileWriter, creatively name 'fwriter'
	// This allows all methods to access this variable, in order to use the Log file that will be created
	public FileWriter fwriter;

	// This is a public PrintWriter created with the name 'outFile'
	// This is a part of the Log file's use, allowing all methods to create and use the log file
	public PrintWriter outFile; 

	File myFile = new File("TM.log");

	// This is the constructor method for the Log class
	// If it encounters an IOException, it will throw the error, eliminating the use for a try/catch block
	public Log() throws IOException {
		if(!(myFile.exists())) {
			myFile.createNewFile();
		}
	}


	void writeLine(String line) throws IOException{

		fwriter = new FileWriter(myFile, true);
		fwriter.append(line);
		fwriter.write("\n");
		fwriter.close();
			
		
	}

	LinkedList<TaskLogEntry> readFile() throws IOException {

		LinkedList<TaskLogEntry> LineL = new LinkedList<TaskLogEntry>();
			
		File logF = new File("TM.log");
		Scanner file = new Scanner(logF);
			
		String thisLine;
		while(file.hasNextLine()) {

			TaskLogEntry entry = new TaskLogEntry();
			thisLine = file.nextLine();
			StringTokenizer stringTok = new StringTokenizer(thisLine, " ");
			entry.timeRN = LocalDateTime.parse(stringTok.nextToken());
			entry.data = stringTok.nextToken();
			entry.cmd = stringTok.nextToken();

			if(stringTok.hasMoreTokens())
				entry.desc = stringTok.nextToken();
				while(stringTok.hasMoreTokens()) {
					entry.desc += (" " + stringTok.nextToken());
				}

			LineL.add(entry);

		}
		file.close();
		return LineL;

	}
}
