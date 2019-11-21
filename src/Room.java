/**
 * Holds information for a single room
 * @author Kyle Perry
 *
 */
public class Room {

	private String name;
	private int maxOccupancy;
	
	public Room(String name, int maxOccupancy)
	{
		this.name = name;
		this.maxOccupancy = maxOccupancy;
	}
	
	public int getOccupancy()
	{
		return maxOccupancy;
	}

	public String toString()
	{
		return name;
	}
}
