/**
 * COMP 3349
 * ASSIGNMENT 1
 * KYLE PERRY
 * 
 * Purpose: Create a program to make an ideal (see: no schedule conflicts) exam schedule using 2 input files
 * 
 * Method: Using backtracking, assign exams (a pairing of a class and a room) to a timetable (an arraylist of arraylists of exams)
 * 
 * Known bugs:
 * - not all error checking is complete:
 * 		- files do not handle symbols
 * 
 * @author Kyle Perry
 *
 */
public class driver {

	public static void main(String[] args)
	{
		int timeslots = Integer.parseInt(args[2]);

		Schedule examSchedule = new Schedule(timeslots);
		
		examSchedule.readCourses(args[0]);
		examSchedule.readRooms(args[1]);
		examSchedule.makeSchedule();
		
	}

}
