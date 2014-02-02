import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
	
	private ArrayList<Player> players;
	private int pointer;
	private int[][] reversi;
	
	public static void main(String[] args) {
		Game game = new Game();
		
		while(!game.endGame()){
			game.pointer = (game.pointer + 1)%2;
			game.players.get(game.pointer).play();
			System.out.println(game.toString());
		}		
		game.winnerIs();
	}
	
	public Game(){
		this.reversi = new int[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				this.reversi[i][j] = 0;
			}
		}
		this.reversi[3][3] = 1;
		this.reversi[4][4] = 1;
		this.reversi[3][4] = 2;
		this.reversi[4][3] = 2;
		
		this.players = new ArrayList<Player>();
		this.createPlayers();		
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
			players.add(new ArtificialIntelligence(name));
		}
		for(int i=0; i < nbr; i++){ //We create the human players
				System.out.println("Choose the name of the human player " + (i+1) + " : ");
				System.out.print(">");
				name = sc.nextLine();	
				players.add(new HumanPlayer(name));			
		}
		Collections.shuffle(players); //Randomize player1 and player2
		players.get(0).setColor(1);
		players.get(1).setColor(2);
		sc.close();
	}
		
	public boolean endGame(){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.reversi[i][j] == 0){
					return false; //if a case is empty, we can continue the game
				}
			}
		}
		return true; //if there is no empty case, game over
	}
	
	public void winnerIs(){
		if(this.players.get(0).getScore() > this.players.get(1).getScore()){
			System.out.println(this.players.get(0).getName() + "wins !");
		} else if(this.players.get(0).getScore() < this.players.get(1).getScore()){
			System.out.println(this.players.get(1).getName() + "wins !");
		} else {
			System.out.println("Draw.");
		}
	}
	
	public String toString(){
		String string = new String();
		string = this.players.get(0).toString() + this.players.get(1).toString(); 	
		
		string += "\n\n\t-----------------\n\t";
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				string += "|" + this.reversi[i][j];
			}
			string += "|\n\t-----------------\n\t";
		}					
		return string;
	}

}
