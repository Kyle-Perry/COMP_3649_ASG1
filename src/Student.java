import java.util.ArrayList;
/**
 * Holds the information for a single student
 * @author Kyle Perry
 *
 */
public class Student {
	private int iD;
	private ArrayList<Course> courses;
	
	public Student(int iD) {
		this.iD = iD;
		this.courses = new ArrayList<Course>();
	}

	public int getiD(){return iD;}

	public Course getCourse(int index){return courses.get(index);}
	
	public void addCourse(Course toAdd){courses.add(toAdd);}
	
	public String toString(){return "" + iD;}
	
	/** 
	 * compares two students
	 * @param iD the id of the second student
	 * @return	true = they are the same, false = two different students
	 */
	public boolean compare(int iD){return this.iD == iD;}
	
	/**
	 * @return the number of courses a student is in	
	 */
	public int getNoCourses(){return courses.size();}
}
