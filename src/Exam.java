/**
 * Holds the information for a single exam
 * @author Kyle Perry
 *
 */
public class Exam {
	private Course examCourse;
	private Room examRoom;
	
	public Exam(Course course, Room room)
	{
		examCourse = course;
		examRoom = room;
	}

	public Course getExamCourse() {
		return examCourse;
	}

	public Room getExamRoom() {
		return examRoom;
	}
	
	public String toString()
	{
		return examCourse.toString() + " - " + examRoom.toString();
	}
}
