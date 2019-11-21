/**
 * Holds information for a previous scheduling attempt (this class is used to store information to backtrack with)
 * @author Kyle Perry
 *
 */
public class Entry {
	private Exam scheduled;
	private int timeslot;

	public Entry (Exam anExam, int aTimeslot)
	{
		scheduled = anExam;
		timeslot = aTimeslot;
	}

	public Exam getScheduled() {
		return scheduled;
	}

	public int getTimeslot() {
		return timeslot;
	}
}
