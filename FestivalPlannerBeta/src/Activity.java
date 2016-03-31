
import java.io.Serializable;
import java.util.ArrayList;

public class Activity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<Artist> artists = new ArrayList<Artist>();

	private int startHour;
	private int startMin;
	private int endHour;
	private int endMin;
	private Stage stage;
	private int popularity;
	private String name;
	private int rating;

	public Activity(String name, int startHour, int startMin, int endHour, int endMin, int rating, Stage stage) {

		this.startHour = ((startHour >= 0 && startHour < 24) ? startHour : 0);
		this.startMin = ((startMin >= 0 && startMin < 60) ? startMin : 0);
		this.endHour = ((endHour >= 0 && endHour < 24) ? endHour : 0);
		this.endMin = ((endMin >= 0 && endMin < 60) ? endMin : 0);
		this.name = name;
		this.stage = stage;
		this.rating = rating;

		switch (stage.getStage()) {
		case "Main stage":
			popularity = rating * 4 + 50;
			break;

		case "Right stage":
			popularity = rating * 4 + 10;
			break;

		default:
			popularity = rating * 4 + 5;
			break;
		}

	}

	public ArrayList<Artist> getArtists() {
		return artists;
	}

	public Stage getStage() {
		return stage;
	}

	public String getStartTime() {
		return String.format("%02d:%02d", startHour, startMin);
	}

	public int getStartHour() {
		return startHour;
	}

	public int getStartMin() {
		return startMin;
	}

	public String getEndTime() {
		return String.format("%02d:%02d", endHour, endMin);
	}

	public int getEndHour() {
		return endHour;
	}

	public int getEndMin() {
		return endMin;
	}

	public int getPopularity() {
		return popularity;
	}

	public int getRating() {
		return rating;
	}

	public String getName() {
		return name;
	}

	public void setStartTime(int startHour, int startMin) {
		this.startHour = ((startHour >= 0 && startHour < 24) ? startHour : 0);
		this.startMin = ((startMin >= 0 && startMin < 60) ? startMin : 0);
	}

	public void setStartHour(int startHour) {
		this.startHour = ((startHour >= 0 && startHour < 24) ? startHour : 0);
	}

	public void setStartMin(int startMin) {
		this.startMin = ((startMin >= 0 && startMin < 60) ? startMin : 0);
	}

	public void setEndTime(int endHour, int endMin) {
		this.endHour = ((endHour >= 0 && endHour < 24) ? endHour : 0);
		this.endMin = ((endMin >= 0 && endMin < 60) ? endMin : 0);
	}

	public void setEndHour(int endHour) {
		this.endHour = ((endHour >= 0 && endHour < 24) ? endHour : 0);
	}

	public void setEndMin(int endMin) {
		this.endMin = ((endMin >= 0 && endMin < 60) ? endMin : 0);
	}

	public void setArtists(ArrayList<Artist> artists) {
		this.artists = artists;
	}

	public void setPopularity(int newRating) {
		rating = newRating;
		switch (stage.getStage()) {
		case "Main stage":
			popularity = rating * 4 + 50;
			break;

		case "Right stage":
			popularity = rating * 4 + 10;
			break;

		default:
			popularity = rating * 4 + 5;
			break;
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setName(String name) {
		this.name = name;
	}

}
