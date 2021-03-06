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
import java.nio.file.*;
import java.nio.charset.StandardCharsets;



class TaskLogEntry{

	String cmd;
	String data;
	String desc;
	String data2;
	LocalDateTime timeRN;

}

// The whole program relies on this class to run appropriately
public class TMModel implements ITMModel {

	private TreeSet<String> nombres;
	private Log log;
	private LocalDateTime timeRN;
	private LinkedList<TaskLogEntry> LineL;
	private ArrayList<String> AL;
	private TreeSet<String> variations;
	private long TT;

	// Initialize Size, for use in declaring tasks as L, XS, S, M, and so on...
	// set the String size to "" so it is initialized and compiles/runs accordingly
	public String Size = "";



	
	
	public TMModel() {
		try {
			log = new Log();
			TT = 0;
			timeRN = LocalDateTime.now();
			variations = new TreeSet<String>();
			nombres = new TreeSet<String>();
			LineL = log.readFile();
			for(TaskLogEntry enterohero : LineL) {
				nombres.add(enterohero.data);
				if (enterohero.cmd.equals("SIZE"))
					variations.add(enterohero.desc);
			}
		}
		catch (IOException err) {
			System.err.println("ERROR: Log File is unreadable");
		}
		catch (NullPointerException err) {
			System.err.println("ERROR: Communication between the application and the log file is faulty");
		}
	}




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




	@Override
	public boolean startTask(String name) {
		try {
			log.writeLine(timeRN + "_" + name + "_START");
		} catch (Exception err) {
			System.err.println("ERROR: Could not write to log file\n");
			return false;
		}
		return true;
	}



	@Override
	public boolean stopTask(String name) {
		try {
			log.writeLine(timeRN + "_" + name + "_STOP");
		} catch (Exception err) {
			System.err.println("ERROR: Could not write to log file\n");
			return false;
		}
		return true;
	}


	@Override
	public boolean describeTask(String name, String desc) {
		try {
			log.writeLine(timeRN + "_" + name + "_DESCRIBE_" + desc);
		} catch (Exception err) {
			System.err.println("ERROR: Could not write to log file\n");
			return false;
		}
		return true;
	}


	@Override
	public boolean sizeTask(String name, String size) {
		try {
			log.writeLine(timeRN + "_" + name + "_SIZE_" + size);
		} catch (Exception err) {
			System.err.println("ERROR: Could not write to log file\n");
			return false;
		}
		return true;
	}


	//TODO
	@Override
	public boolean deleteTask(String name) {
		List<String> newLines = new ArrayList<>();
				try {
					for (String line : Files.readAllLines(Paths.get("TM.log"), StandardCharsets.UTF_8)) {
					    if (line.contains("_"+ name + "_")) {
					       newLines.add(line.replace("_"+ name + "_", "_#REDACTED-Still-Able-To-Revert#_"));
					    } else {
					       newLines.add(line);
					    }
					}
					Files.write(Paths.get("TM.log"), newLines, StandardCharsets.UTF_8);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}	

	}



	@Override
	public boolean renameTask(String old, String New) {
		ArrayList<String> LineList = new ArrayList<>();
		// Supplied and Modified from Stack Overflow
		try {
			for (String line : Files.readAllLines(Paths.get("TM.log"), StandardCharsets.UTF_8)) {
				if(line.contains("_" + old + "_"))
					LineList.add(line.replace("_" + old + "_", "_" + New + "_"));
				else
					LineList.add(line);
			}
			Files.write(Paths.get("TM.log"), LineList, StandardCharsets.UTF_8);
		} catch (Exception err) {
			System.err.println("ERROR: Problem Occurred During Task Renaming Method");
			return false;
		}
		return true;
	}


	@Override
	public String taskElapsedTime(String name) {
		Task task = new Task(name, LineL);
		return task.totTime();
	}


	@Override
	public String taskSize(String name) {
		Task task = new Task(name, LineL);
		return task.SSize();
	}


	@Override
	public String taskDescription(String name) {
		Task task = new Task(name, LineL);
		return task.descript();
	}


	@Override
	public String minTimeForSize(String size) {
		long minimumT = Long.MAX_VALUE;
		for (String tasks : nombres ) {
			Task task = new Task(tasks, LineL);
			if (task.SS.equals(size)) {
				if (task.totTime < minimumT) {
					minimumT = task.totTime;
				}
			}
		}
		return TimeUtil.toElapsedTime(minimumT);
	}


	@Override
	public String maxTimeForSize(String size) {
		long maximumTime = 0;
		for (String nombre : nombres) {
			Task task = new Task(nombre, LineL);
			if (task.SS.equals(size) && task.totTime > maximumTime)
				maximumTime = task.totTime;
		}
		return TimeUtil.toElapsedTime(maximumTime);

	}


	
	@Override
	public String avgTimeForSize(String size) {
		long totalT = 0;
		int i = 0;
		for ( String nombre : nombres ) {
			Task task = new Task(nombre, LineL);
			if (task.SS.equals(size)) {
				totalT = totalT + task.totTime;
				i++;
			}
		}
		totalT = totalT/i;
		return TimeUtil.toElapsedTime(totalT);
	}


	@Override
	public Set<String> taskNamesForSize(String size) {
		Set<String> taskNamesForSize = new TreeSet<String>();
		for (String iTask : nombres) {
			Task task = new Task(iTask, LineL);
			if(task.SS.equals(size)) {
				taskNamesForSize.add(iTask);
			}
		}
		return taskNamesForSize;
	}


	
	@Override
	public String elapsedTimeForAllTasks() {
		long totalTime = 0;
		for (String tasks : taskNames() ) {
			Task task = new Task(tasks, LineL);
			totalTime += task.totTime;
		}
		return TimeUtil.toElapsedTime(totalTime);
	}



	@Override
	public Set<String> taskNames() {
		return nombres;
	}


	@Override
	public Set<String> taskSizes() {
		return variations;
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
		private StringBuilder desc = new StringBuilder("");
		private String timeAhora = "";
		private long totTime = 0;
		private String SS = "UNKNOWN";


		// Code helps my TMModel, as my code was not simplified by removing this as I used this
		//		in order to keep my Task() constructor, yet keep it useful if needed by my team
		public Task(String name, LinkedList<TaskLogEntry> entries) {
			this.name = name;
			//WHY IS THIS BEING WRITTEN AS THE GODDAMN DESCRIPTION
			//REDACTED
			//this.desc = "";
			//REDACTED
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
							if (desc.toString().equals(""))
								desc.append(" " + entry.desc);
							else
								desc.append("\n\t\t" + entry.desc);
							if (entry.data2 != null)
								SS = entry.data2;
							break;
						case "SIZE":
							SS = entry.desc;
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

		public String totTime() {
			return this.timeAhora;
		}

		public String SSize() {
			return this.SS;
		}

		public String descript() {
			return this.desc.toString();
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

		int stringTokCount = 0;
		LinkedList<TaskLogEntry> LineL = new LinkedList<TaskLogEntry>();
			
		File logF = new File("TM.log");
		Scanner file = new Scanner(logF);
			
		String thisLine;
		while(file.hasNextLine()) {

			TaskLogEntry entry = new TaskLogEntry();
			thisLine = file.nextLine();
			StringTokenizer stringTok = new StringTokenizer(thisLine, "_");
			entry.timeRN = LocalDateTime.parse(stringTok.nextToken());
			entry.data = stringTok.nextToken();
			entry.cmd = stringTok.nextToken();

			if(stringTok.hasMoreTokens()) {
				entry.desc = stringTok.nextToken();
			}
			if(stringTok.hasMoreTokens()) {
				entry.data2 = stringTok.nextToken();
			}

			LineL.add(entry);

		}
		file.close();
		return LineL;

	}
}
