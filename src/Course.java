import java.util.ArrayList;
/**
 * Holds information for a single course
 * @author Kyle Perry
 *
 */
public class Course {
	private String name;
	private ArrayList<Student> students;
	
	public Course(String name)
	{
		this.name = name;
		students = new ArrayList<Student>();
	}
	
	public void addStudent(Student aStudent)
	{
		students.add(aStudent);
	}
	
	public Student getStudent(int index)
	{
		return students.get(index);
	}
	
	public int getClassSize()
	{
		return students.size();
	}
	
	public String toString()
	{
		return name;
	}
/**
 * checks if two courses have at least one student shared between them
 * @param other	second course to check with
 * @return	true = they share a student, false = they don't.
 */
	public boolean hasOverlap(Course other)
	{
		int otherSize = other.getClassSize();
		int count;
		boolean found = false;
		
		for(Student current: students)
		{
			count = 0;
			while(count < otherSize && !found)
			{
				found = current.compare(other.getStudent(count).getiD());
			}
			if(found)
				break;
		}
		
		return found;
	}
}
