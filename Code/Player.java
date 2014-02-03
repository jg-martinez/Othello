
public abstract class Player {
	private String name;
	private int score;
	private int color;
	
	public Player(String arg0){
		this.name = arg0;
		this.score = 0;
	}
	
	public abstract void play();
	
	public int getScore(){
		return this.score;
	}
	
	public int getColor(){
		return this.color;
	}

	public void setColor(int arg0){
		this.color = arg0;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String toString(){
		String string = new String();
		string = "\n\t" + this.getName() + " : " + this.getScore();
		return string;
	}
}
