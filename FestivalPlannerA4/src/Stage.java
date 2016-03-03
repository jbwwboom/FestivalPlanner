import java.io.Serializable;

public class Stage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String stage;

	public Stage(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}
	
	public void setStage(String stage){
		this.stage = stage;
	}


}
