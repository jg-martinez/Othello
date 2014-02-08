import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Observable;

/**
 * 
 * Definition of the Othello game.
 * 
 *
 */
public class Game extends Observable {
	/**
	 * Contain all the players
	 */
	private ArrayList<Player> players;
	
	/**
	 * Point to the current player
	 */
	private int pointer;
	
	/**
	 * BoardGame were we play
	 * reversi[Y][X] where Y is the [1-8] vertical axis, and X is the [a-h] horizontal axis
	 */
	private int[][] reversi;
	
	/**
	 * Time allocated to the minMax algorithm.
	 */
	private int timeLimit;
	
	/**
	 * Number of turns played / current turn
	 */
	private int turn;
	
	/**
	 * Entry buffer
	 */
	private Scanner sc;
	
	/**
	 * Constant that define an empty box
	 */
	public static char EMPTYBOX = ' ';
	
	public static void main(String[] args) {
		Game game = new Game();
		Board boardgame = new Board(game);
		boardgame.help(); //print the help for the user
		game.getPlayers().get(0).createPossibleMove(game); //initialize the possible move for player 0
		game.getPlayers().get(1).createPossibleMove(game); //initialize the possible move for player 1
		
		while(!game.endGame()){	//we check the end of the game			
			game.getCurrentPlayer().createPossibleMove(game); //we create the possible move for the current game	
			if(game.getCurrentPlayer().hasNoMoreMove()){ //we change to next player if the current player has no more move
				game.setChanged(); //Observable method
				game.notifyObservers("NoMoreMove"); //Observable method
				game.nextPlayer(); 
			} else {
				if(game.getCurrentPlayer() instanceof HumanPlayer){ 
					game.getCurrentPlayer().suggestStrategy(game); 
					game.updateScore();
					game.setChanged();
					game.notifyObservers("BestMoveSelected");
					game.getCurrentPlayer().chooseAction(true, game); //this boolean value will pause the tread if it's a human player
				} else {
					game.setChanged();
					game.notifyObservers("AIsTurn");
					game.getCurrentPlayer().chooseAction(true, game);
					game.setChanged();
					game.notifyObservers("AIhasPlayed");	
				}
				game.nextPlayer();
			}			
			
		}	
		game.winnerIs();
		game.sc.close();
	}
	
	public Game(){
		this.reversi = new int[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				this.reversi[i][j] = EMPTYBOX;
			}
		}
		this.reversi[3][3] = 'x';
		this.reversi[4][4] = 'x';
		this.reversi[3][4] = 'o';
		this.reversi[4][3] = 'o';
		
		sc = new Scanner(System.in);
		this.turn = 1;
		this.players = new ArrayList<Player>();
		this.createPlayers();	
	}
	
	/**
	 * duplicate the gameToCpy variable in a new variable
	 * @param gameToCpy
	 */
	public Game(Game gameToCpy){
		this.reversi = new int[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				this.reversi[i][j] = gameToCpy.reversi[i][j];
			}
		}
		this.timeLimit = gameToCpy.timeLimit;
		this.turn = gameToCpy.turn;
		this.players = new ArrayList<Player>(gameToCpy.players);	
	}
	
	/**
	 * Change the current player and manage the turn variable
	 */
	public void nextPlayer(){
		if(this.getPointer() == 1) {
			this.turn++;
		}
		this.pointer = (this.pointer + 1) % 2;
	}
	
	public void createPlayers(){
		System.out.println("Indicate the number of human player :");
		System.out.print(">");
		
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
			players.add(new ArtificialIntelligence(name));
		}
		for(int i=0; i < nbr; i++){ //We create the human players
				System.out.println("Choose the name of the human player " + (i+1) + " : ");
				System.out.print(">");
				name = sc.nextLine();	
				players.add(new HumanPlayer(name));			
		}
		Collections.shuffle(players); //Randomize player1 and player2
		players.get(0).setColor('x');
		players.get(1).setColor('o');

		System.out.println("Indicate the time limit (in second) :");
		System.out.print(">");
		this.timeLimit = sc.nextInt(); 		
	}		
	
	/**
	 * 
	 * @return true if the game is over, false else.
	 */
	public boolean endGame(){
		boolean hasEmpty = false, hasX = false, hasO = false, noMoreMoveLeft = false;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.reversi[i][j] == ' '){
					hasEmpty = true; //if a box is empty, we can continue the game
				}
				if(this.reversi[i][j] == 'x'){
					hasX = true; //the x player still has some pieces
				}
				if(this.reversi[i][j] == 'o'){
					hasO = true; //the o player still has some pieces
				}				
			}
		}
		if(this.players.get(0).hasNoMoreMove() && this.players.get(1).hasNoMoreMove()){ //both player can't move anymore
			noMoreMoveLeft = true;
		}
		if(hasEmpty == false || hasX == false || hasO == false || noMoreMoveLeft == true){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Announce the winner of the game
	 */
	public void winnerIs(){
		this.updateScore();
		if(this.players.get(0).getScore() > this.players.get(1).getScore()){
			System.out.println(this.players.get(0).getName() + "wins !");
		} else if(this.players.get(0).getScore() < this.players.get(1).getScore()){
			System.out.println(this.players.get(1).getName() + "wins !");
		} else {
			System.out.println("Draw.");
		}
		System.out.println(this.toString());
		System.out.println(this.players.get(0).getName() + " : " + this.players.get(0).getScore() + "\t" + this.players.get(1).getName() + " : " + this.players.get(1).getScore());
	}
	
	public int[][] getReversi(){
		return this.reversi;
	}
	
	public int getTimeLimit(){
		return this.timeLimit;
	}	
	
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	
	public int getPointer(){
		return this.pointer;
	}
	
	public int getTurn(){
		return this.turn;
	}
	
	public Player getCurrentPlayer(){
		return this.players.get(this.pointer);
	}
	
	public void updateScore(){
		this.getPlayers().get(0).setScore(this);
		this.getPlayers().get(1).setScore(this);
	}
	
	public void setReversi(int posX, int posY, int color){
		this.reversi[posY][posX] = color;
		
		for(int i = posX; i < 8; i++){ //to the right
			if(reversi[posY][i] == color){
				for(int k = posX; k <= i; k++){
					reversi[posY][k] = color;
				}
			} else if(reversi[posY][i] == EMPTYBOX){
				break;
			}
		}
		for(int i = posX; i >= 0; i--){ //to the left
			if(reversi[posY][i] == color){
				for(int k = posX; k >= i; k--){
					reversi[posY][k] = color;
				}
			} else if(reversi[posY][i] == EMPTYBOX){
				break;
			}
		}
		for(int j = posY; j < 8; j++){ //to the bottom
			if(reversi[j][posX] == color){
				for(int k = posY; k <= j; k++){
					reversi[k][posX] = color;
				}
			} else if(reversi[j][posX] == EMPTYBOX){
				break;
			}
		}
		for(int j = posY; j >= 0; j--){ //to the top
			if(reversi[j][posX] == color){
				for(int k = posY; k >= j; k--){
					reversi[k][posX] = color;
				}
			} else if(reversi[j][posX] == EMPTYBOX){
				break;
			}
		}
		int i = 0;
		int j = 0;
		while(posX + i < 8 && posY + j < 8){ //bottom right
			if(this.reversi[posY+j][posX+i] == color){
				int k = 0;
				int l = 0;
				while(k <= i && l <= j){
					this.reversi[posY + l][posX + k] = color;
					k++;
					l++;
				}
			} else if(this.reversi[posY+j][posX+i] == EMPTYBOX){
				break;
			}
			i++;
			j++;
		}
		i = 0;
		j = 0;
		while(posX + i < 8 && posY - j >= 0){ //top right
			if(this.reversi[posY-j][posX+i] == color){
				int k = 0;
				int l = 0;
				while(k <= i && l <= j){
					this.reversi[posY - l][posX + k] = color;
					k++;
					l++;
				}
			} else if(this.reversi[posY-j][posX+i] == EMPTYBOX){
				break;
			}
			i++;
			j++;
		}
		i = 0;
		j = 0;
		while(posX - i >= 0 && posY - j >= 0){ //top left
			if(this.reversi[posY-j][posX-i] == color){
				int k = 0;
				int l = 0;
				while(k <= i && l <= j){
					this.reversi[posY - l][posX - k] = color;
					k++;
					l++;
				}
			} else if(this.reversi[posY-j][posX-i] == EMPTYBOX){
				break;
			}
			i++;
			j++;
		}
		i = 0;
		j = 0;
		while(posX - i >= 0 && posY + j < 8){ //bottom left
			if(this.reversi[posY+j][posX-i] == color){
				int k = 0;
				int l = 0;
				while(k <= i && l <= j){
					this.reversi[posY + l][posX - k] = color;
					k++;
					l++;
				}
			} else if(this.reversi[posY+j][posX-i] == EMPTYBOX){
				break;
			}
			i++;
			j++;
		}
	}	
		
	public String toString(){
		String string = new String();
		string = this.players.get(0).toString() + this.players.get(1).toString(); 	
		string += "\n\t -a-b-c-d-e-f-g-h-\t";
		string += "\n\t -----------------\n\t1|";
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.reversi[i][j] == EMPTYBOX){
					if(this.getCurrentPlayer().getPossibleMove()[i][j] != 0){
						if(this.getCurrentPlayer().getPossibleMove()[i][j] == 'B'){
							string += Character.toString((char)this.getCurrentPlayer().getPossibleMove()[i][j]) + "|";
						} else {
							string += this.getCurrentPlayer().getPossibleMove()[i][j] + "|";
						}
						
					} else {
						string += Character.toString((char)this.reversi[i][j]) + "|";
					}
				} else {
					string += Character.toString((char)this.reversi[i][j]) + "|";
				}				
			}
			if(i != 7) string += "\n\t -----------------\n\t" + (i+2) + "|";			
		}		
		string += "\n\t -a-b-c-d-e-f-g-h-\n\t";
		return string;
	}
}
