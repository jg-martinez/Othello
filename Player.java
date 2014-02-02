import java.util.ArrayList;

public abstract class Player {
	private String name;
	private int score;
	private int color;
	private ArrayList<String> possibleMove;
	private Game game;
	
	
	public Player(String arg0, Game arg1){
		this.name = arg0;
		this.score = 0;
		this.color = 0;
		this.game = arg1;
	}
	
	public void play(String move){
		int posX = Character.getNumericValue(move.charAt(0)) - 10;
		int posY = Character.getNumericValue(move.charAt(1)) - 1;
		this.game.setReversi(posX,posY, this.color);
		
	}
	
	public abstract void chooseAction(boolean bool);
	
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
	
	public boolean checkPossibleMove(String move){
		/*for(int i = 0; i < possibleMove.size(); i++){
			if(possibleMove.get(i).toString() == move){
				return true;
			}
		}
		return false;*/
		return true;
	}
	
	public void incrementeScore(){
		this.score++;
	}
	
	public String toString(){
		String string = new String();
		string = "\t" + this.getName() + " : " + this.getScore();
		return string;
	}
}
