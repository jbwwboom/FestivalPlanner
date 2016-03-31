import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;



public class Agenda implements Serializable{
	private static final long serialVersionUID = 1L;
	ArrayList<Activity> activities = new ArrayList<Activity>();
	private ArrayList<Artist> artists = new ArrayList<Artist>();
	private ArrayList<Stage> stages = new ArrayList<Stage>();

	public Agenda() {

		stages.add(new Stage("Main stage"));
		stages.add(new Stage("Right stage"));
		stages.add(new Stage("Left stage"));
		
	}
	
	public ArrayList<Stage> getStages() {
		return stages;
	}
	
	public ArrayList<Artist> getArtists() {
		return artists;
	}
	
	public void addArtist(String name, int rating) {
		artists.add(new Artist(name, rating));
	}
	
	public void addActivity(String name, int startHour, int startMin, int endHour, int endMin,int rating, Stage stage)
	{
		activities.add(new Activity(name, startHour, startMin, endHour, endMin, rating, stage));
	}
	
	public ArrayList<Activity> getActivity()
	{
		return activities;
	}
	
	public void save(String filename) {
		try {
			OutputStream os = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void test() {
		
		for(Artist a : artists){
			System.out.println( a.getName());
			System.out.println( a.getPop());
		}
				
		for(Activity act : activities){
			System.out.println(act.getStartTime());
			System.out.println(act.getEndTime());
		}
		
	}
}
