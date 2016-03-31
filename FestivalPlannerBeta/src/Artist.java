import java.io.Serializable;

public class Artist implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int rating;

	public Artist(String name, int rating) {
		this.name = name;
		this.rating = rating;
	}

	public int getPop() {
		return rating;
	}

	public String getName() {
		return name;
	}

}
