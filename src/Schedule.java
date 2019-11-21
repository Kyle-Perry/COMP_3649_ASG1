import java.io.File;
import java.util.*;
/**
 * 
 * @author Kyle Perry
 *
 *	Handles the creation and storage of an exam schedule
 *
 */
public class Schedule {
	private ArrayList<ArrayList<Exam>>	timetable;
	private ArrayList<Course>	courseList;
	private ArrayList<Room>	roomList;
	private ArrayList<Student>	studentList;

	public Schedule(int numTimeslots)
	{
		timetable = new ArrayList<ArrayList<Exam>>();
		for(int i = 0; i < numTimeslots; i++)
		{
			timetable.add(new ArrayList<Exam>());
		}
		courseList = new ArrayList<Course>();
		studentList = new ArrayList<Student>();
		roomList = new ArrayList<Room>();
	}

	/**
	 * Reads in the courses from a file specified in the first command line argument
	 * @param filename - the name of the file to read from
	 */
	public void readCourses(String filename)
	{
		Course toAdd;
		Student newStudent;
		int idNumber = 0;
		Exception emptyFile = new Exception("emptyFile");
		Exception invalidFileFormat = new Exception("incorrectFileFormat");
		Exception negativeID = new Exception("negativeStudentID");

		File readFile;
		Scanner fromFile;
		try
		{
			readFile = new File(filename);
			fromFile = new Scanner(readFile);

			if(!fromFile.hasNext())
				throw(emptyFile);
			while(fromFile.hasNext())
			{
				toAdd = new Course(fromFile.next());
				courseList.add(toAdd);
				if(!fromFile.hasNextInt())
					throw(invalidFileFormat);
				while(fromFile.hasNextInt())
				{
					idNumber = fromFile.nextInt();
					if(idNumber < 0)
						throw(negativeID);
					newStudent = new Student(idNumber);
					if(findStudent(newStudent) == null)
					{
						studentList.add(newStudent);
						toAdd.addStudent(newStudent);
						newStudent.addCourse(toAdd);
					}
					else
					{
						for(Student current: studentList)
							if(current.compare(newStudent.getiD()))
							{
								toAdd.addStudent(current);
								current.addCourse(toAdd);
								break;
							}
					}
				}
			}
			fromFile.close();	
		}
		catch(Exception e)
		{
			System.out.println("File read error in file: " + filename + "!\n" + e.toString());
			System.exit(-1);
		}
	}

	/**
	 * Reads in the rooms from a file specified by the second command line argument
	 * @param filename - the filename
	 */
	public void readRooms(String filename)
	{
		File readFile;
		Scanner fromFile;
		Exception emptyFile = new Exception("emptyFile");
		Exception invalidFileFormat = new Exception("incorrectFileFormat");
		String name = "";
		try
		{
			readFile = new File(filename);
			fromFile = new Scanner(readFile);
			if(!fromFile.hasNextLine())
				throw(emptyFile);
			while(fromFile.hasNext())
			{
				name = fromFile.next();
				if(!fromFile.hasNextInt())
					throw(invalidFileFormat);
				roomList.add(new Room(name, fromFile.nextInt()));
			}
			fromFile.close();
		}
		catch(Exception e)
		{
			System.out.println("File read error in file: " + filename + "!\n" + e.toString());
			System.exit(-1);
		}
		Collections.sort(roomList, new MyComparable());
	}

	/**
	 * Determines if a student already exists in the List of all students
	 * @param target	Student to find
	 * @return	the pointer to the student(will be different from the input if the student already exists)
	 */
	public Student findStudent(Student target)
	{
		Student found = null;
		for(Student current: studentList)
		{
			if(current.compare(target.getiD()))
			{
				found = current;
				break;
			}
		}
		return found;
	}

	/**
	 * used for sorting rooms
	 *
	 */
	public class MyComparable implements Comparator<Room>
	{
		public int compare(Room o1, Room o2) 
		{
			if(o1.getOccupancy() >= o2.getOccupancy())
				return 1;
			else
				return -1;
		}
	} 

	/**
	 * Makes the ideal schedule if it is possible and prints it or a message to the screen
	 */
	public void makeSchedule()
	{
		ArrayList<Course> unscheduled = new ArrayList<Course>(courseList);
		ArrayList<Entry>	previousEntries = new ArrayList<Entry>();
		Stack<Entry>	currentEntry = new Stack<Entry>();
		Entry toAdd = null;
		Exam newExam = null;
		Course curTarget = null;
		Room foundRoom = null;

		do
		{
			curTarget = unscheduled.get(0);
			for(int a = 0; a < timetable.size(); a++)
			{
				foundRoom = findRoom(curTarget, a, previousEntries);
				if(foundRoom != null)
				{
					unscheduled.remove(0);
					newExam = new Exam(curTarget, foundRoom);
					timetable.get(a).add(newExam);
					toAdd = new Entry(newExam, a);
					previousEntries.add(toAdd);
					currentEntry.push(toAdd);	
					a = timetable.size()+1;
				}
			}
			if(foundRoom == null)
			{
				if(!currentEntry.isEmpty())
				{
					toAdd = currentEntry.pop();
					timetable.get(toAdd.getTimeslot()).remove(toAdd.getScheduled());
					unscheduled.add(toAdd.getScheduled().getExamCourse());
				}
			}
		}
		while(!unscheduled.isEmpty() && !currentEntry.isEmpty());

		if(currentEntry.isEmpty())
		{
			System.out.println("Could not produce schedule.");
			return;
		}
		else
			printTimetable();


	}
	
/**
 * Finds a valid room if there is one available, ignores rooms that were used before backtracking
 * @param toSchedule	the course to schedule an exam for
 * @param timeslot	the timeslot in which to schedule
 * @param previous	list of all previous scheduled entries for dealing with backtracking
 * @return	the room if there is a valid one available, null if none
 */
	public Room findRoom(Course toSchedule, int timeslot, ArrayList<Entry> previous)
	{
		boolean foundRoom = false;
		int count = 0;
		Room current = null;

		if(!validateCourse(toSchedule, timeslot))
			if(!roomList.isEmpty())
			{
				while(count < roomList.size() && !foundRoom)
				{
					current = roomList.get(count);
					if(hasSeats(toSchedule, current, timeslot))
						foundRoom = newMove(toSchedule, current, timeslot, previous);
					count++;
				}
			}

		if(foundRoom)
			return current;
		else
			return null;

	}

	/**
	 * Checks if a room has the required number of seats
	 * @param desired	course to schedule to the room
	 * @param target	the room itself
	 * @param timeslot	the timeslot
	 * @return	true = has enough seats, false = not enough seats
	 */
	public boolean hasSeats(Course desired, Room target, int timeslot)
	{
		int available = target.getOccupancy();
		if(!timetable.get(timeslot).isEmpty())
			for(Exam current: timetable.get(timeslot))
			{
				if(current.getExamRoom() == target)
					available -= current.getExamCourse().getClassSize();
			}
		return desired.getClassSize() <= available;
	}

	/**
	 * Checks if there are any students that have an exam already at this time in the course given
	 * @param desired	course to schedule
	 * @param timeslot	the timeslot
	 * @return	true = a student has a conflict, false = no conflicts
	 */
	public boolean validateCourse(Course desired, int timeslot)
	{
		Course currentClass = null;

		if(!timetable.get(timeslot).isEmpty())
			for(Exam current: timetable.get(timeslot))
			{
				currentClass = current.getExamCourse();
				for(int x = 0; x < desired.getClassSize(); x++)
					for(int i = 0; i < currentClass.getClassSize(); i++)
						if(desired.getStudent(x).compare(currentClass.getStudent(i).getiD()))
							return true;
			}
		return false;
	}

	/**
	 * Checks if the exam to be scheduled in this timeslot before a backtrack was made
	 * @param toSchedule course to be scheduled
	 * @param target	room the exam will be in
	 * @param timeslot	the timeslot the exam will be in
	 * @param previous	list of previous scheduling attempts
	 * @return	true = new move, false = not a new move
	 */
	public boolean newMove(Course toSchedule, Room target, int timeslot, ArrayList<Entry> previous)
	{
		if(!previous.isEmpty())
		{
			for(Entry current: previous)
			{
				if(toSchedule == current.getScheduled().getExamCourse())
					if(target == current.getScheduled().getExamRoom())
						if(timeslot == current.getTimeslot())
							return false;
			}
		}

		return true;
	}

	/**
	 * Prints the timetable, unused timeslots will be ignored.
	 */
	public void printTimetable()
	{
		int currentSlot = 0;
		for(ArrayList<Exam> current: timetable)
		{
			if(!current.isEmpty())
			{
				System.out.println("Timeslot " + currentSlot + ":\n");
				for(Exam toPrint: current)
					System.out.println(toPrint.toString());
				System.out.println();
				currentSlot++;
			}
		}
	}
}
