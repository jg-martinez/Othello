import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Observable;

public class Game extends Observable {
	
	private ArrayList<Player> players;
	private int pointer;
	private int[][] reversi;
	private int round;
	
	public static void main(String[] args) {
		Game game = new Game();
		Board boardgame = new Board(game);
		boardgame.help();
		game.round = 1;
		while(!game.endGame()){				
			game.setChanged();
			game.notifyObservers("newRound");			
			game.getActualPlayer().chooseAction(true);
			game.pointer = (game.pointer + 1) % 2;
			game.round++;
		}		
		game.winnerIs();
	}
	
	public int[][] getReversi(){
		return this.reversi;
	}
	
	public void setReversi(int posX, int posY, int color){
		this.reversi[posY][posX] = color;
		
		for(int i = posX; i < 8; i++){ //to the right
			if(reversi[posY][i] == color){
				for(int k = posX; k <= i; k++){
					reversi[posY][k] = color;
				}
			}
		}
		for(int i = posX; i > 0; i--){ //to the left
			if(reversi[posY][i] == color){
				for(int k = posX; k >= i; k--){
					reversi[posY][k] = color;
				}
			}
		}
		for(int j = posY; j < 8; j++){ //to the bottom
			if(reversi[j][posX] == color){
				for(int k = posY; k <= j; k++){
					reversi[k][posX] = color;
				}
			}
		}
		for(int j = posY; j > 0; j--){ //to the top
			if(reversi[j][posX] == color){
				for(int k = posY; k >= j; k--){
					reversi[k][posX] = color;
				}
			}
		}
		for(int i = posX; i < 8 ; i++){ //diagonals
			if(this.reversi[7-i][i] == color){ //diagonal posX->top left
				for(int k = posX; k <= i; k++){
					reversi[7-k][k] = color;
				}
			}
			if(this.reversi[i][7-i] == color){ //diagonal posX->bottom right
				for(int k = posY; k <= i; k++){
					reversi[k][7-k] = color;
				}
			}
		}
		for(int i = posX; i > 0; i--){ //diagonals
			if(this.reversi[7-i][i] == 0){ //diagonal posX->top right
				for(int k = posX; k >= i; k++){
					reversi[7-k][k] = color;
				}
			}
			if(this.reversi[i][7-i] == 0){ //diagonal posX->bottom left
				for(int k = posX; k >= i; k++){
					reversi[k][7-k] = color;
				}
			}
		}
		updateScore();
	}
	
	public void updateScore(){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(reversi[i][j] == players.get(0).getColor()){
					players.get(0).incrementeScore();					
				} else if(reversi[i][j] == players.get(1).getColor()){
					players.get(1).incrementeScore();
				}
			}
		}
	}
	
	public Game(){
		this.reversi = new int[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				this.reversi[i][j] = ' ';
			}
		}
		this.reversi[3][3] = 'x';
		this.reversi[4][4] = 'x';
		this.reversi[3][4] = 'o';
		this.reversi[4][3] = 'o';
		
		this.players = new ArrayList<Player>();
		this.createPlayers();		
	}
	
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	
	
	public int getPointer(){
		return this.pointer;
	}
	
	public int getRound(){
		return this.round;
	}
	
	public Player getActualPlayer(){
		return this.players.get(this.pointer);
	}
	
	public void createPlayers(){
		System.out.println("Indicate the number of human player :");
		System.out.print(">");
		Scanner sc = new Scanner(System.in);
		int nbr=0;
		do {			
			try {
				nbr=sc.nextInt();
				if(nbr < 0 || nbr > 2){
					System.out.println("Please, enter a number between 1 and 2 :");
					System.out.print(">");
				}
			} catch (InputMismatchException e){
				sc.next();//Empty the buffer before reading another line
				System.out.println("Please, enter a number between 1 and 2 :");
				System.out.print(">");
			} 
		} while(nbr < 0 || nbr > 2);
		String name;
		sc.nextLine(); //Empty the buffer before reading another line
		for(int i=0; i < 2 - nbr; i++){ //We create the AI
			name = "AI" + (i+1);
			players.add(new ArtificialIntelligence(name, this));
		}
		for(int i=0; i < nbr; i++){ //We create the human players
				System.out.println("Choose the name of the human player " + (i+1) + " : ");
				System.out.print(">");
				name = sc.nextLine();	
				players.add(new HumanPlayer(name, this));			
		}
		Collections.shuffle(players); //Randomize player1 and player2
		players.get(0).setColor('x');
		players.get(1).setColor('o');
	}		
	
	public boolean endGame(){
		boolean hasEmpty = false, hasX = false, hasO = false;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.reversi[i][j] == ' '){
					hasEmpty = true; //if a case is empty, we can continue the game
				}
				if(this.reversi[i][j] == 'x'){
					hasX = true; //if a case is empty, we can continue the game
				}
				if(this.reversi[i][j] == 'o'){
					hasO = true; //if a case is empty, we can continue the game
				}				
			}
		}
		if(hasEmpty == false || hasX == false || hasO == false){
			return true;
		} else {
			return false;
		}
	}
	
	public void winnerIs(){
		if(this.players.get(0).getScore() > this.players.get(1).getScore()){
			System.out.println(this.players.get(0).getName() + "wins !");
		} else if(this.players.get(0).getScore() < this.players.get(1).getScore()){
			System.out.println(this.players.get(1).getName() + "wins !");
		} else {
			System.out.println("Draw.");
		}
		System.out.println(this.players.get(0).getName() + " : " + this.players.get(0).getScore() + "\t" + this.players.get(1).getName() + " : " + this.players.get(1).getScore());
	}
	
	
	public String toString(){
		String string = new String();
		string = this.players.get(0).toString() + this.players.get(1).toString(); 	
		string += "\n\t -a-b-c-d-e-f-g-h-\t";
		string += "\n\t -----------------\n\t1|";
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				string += Character.toString((char)this.reversi[i][j]) + "|";
			}
			if(i != 7) string += "\n\t -----------------\n\t" + (i+2) + "|";			
		}		
		string += "\n\t -a-b-c-d-e-f-g-h-\n\t";
		return string;
	}

}
