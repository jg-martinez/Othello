import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

/**
 * 
 * Thread which will interact with the user (Text view)
 *
 */
public class Board implements Runnable, Observer {
	
	public static String PLAY = "[a-h][1-8]";
	public static String PRINT = "print";
	public static String QUIT = "quit";
	public static String PROMPT = ">";
	public static String HELP = "help";
	private Game game;
	private Thread t;
	
	
	public Board(Game g){
		this.game = g;
		this.game.addObserver(this);
		t = new Thread(this);
		t.start();
	}
	
	public void run(){
		String command = null; 
		boolean quit = false;
		System.out.println("\n");
		do {
			if (this.game.getCurrentPlayer() instanceof HumanPlayer){
				command = this.readCommand();			
				if (command != null ) {
					command = command.toLowerCase();				
					if (command.matches("[a-h][1-8]")) {
						if(this.game.getCurrentPlayer().checkPossibleMove(command)){							
							int posX = Character.getNumericValue(command.charAt(0)) - 10; //convert the character [a-h] into indice value for the boardgame
							int posY = Character.getNumericValue(command.charAt(1)) - 1; //convert the character [1-8] into indice value for the boardgame
							this.game.setReversi(posX,posY, this.game.getCurrentPlayer().getColor());	
							this.game.getCurrentPlayer().chooseAction(false, this.game); //this boolean will reactivate the thread
						} else {
							this.game.toString();
							System.out.println("This move is not allowed. Choose another one.");
						}
					} else if(command.equalsIgnoreCase(PRINT)){
						this.game.toString();
					} else if (command.equalsIgnoreCase(HELP)) {
						help();	
						System.out.print(PROMPT);
					} else if (command.equalsIgnoreCase(QUIT)) {
						quit = true;					
					} else { 
						System.out.println("Command not found... Use \"help\" to print the command list.");
						System.out.print(PROMPT);
					}
				}
			}
		} while (quit == false && !this.game.endGame());
		if(quit == true) {
			System.exit(0);	
		}
	}
	
	/**
	 * Print the command list
	 */
	public void help() {
		System.out.println("Use : \n" + HELP + " to get the command list \n"
				+ PRINT + " : to print the board \n"
				+ PLAY + " : to play a move \n"
				+ QUIT + " : quit the game.");
	}
	
	public String readCommand() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String resultat = null;
		try {
			resultat = br.readLine().trim();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}		
		return resultat;
	}
	
	/**
	 * Update the text view, depending the argument arg.
	 */
	public void update(Observable o, Object arg) {
		if(arg.equals("BestMoveSelected")){
			System.out.println(this.game.toString());
			System.out.println("Turn " + this.game.getTurn() + " : " + "It is " + this.game.getCurrentPlayer().getName() + "'s turn (Player " + Character.toString((char)this.game.getCurrentPlayer().getColor()) +").");
			System.out.print(PROMPT);
		} else if(arg.equals("AIsTurn")){
			System.out.println("Turn " + this.game.getTurn() + " : " + this.game.getCurrentPlayer().getName() + " (" + Character.toString((char)this.game.getCurrentPlayer().getColor()) + ") " + " is going to play...");
		} else if(arg.equals("AIhasPlayed")){
			System.out.println("Turn " + this.game.getTurn() + " : " + this.game.getCurrentPlayer().getName() + " (" + Character.toString((char)this.game.getCurrentPlayer().getColor()) + ") " + " has played " + this.game.getCurrentPlayer().getLastMove());
		} else if(arg.equals("NoMoreMove")){
			System.out.println("Turn " + this.game.getTurn() + " : " + this.game.getCurrentPlayer().getName() + " (" + Character.toString((char)this.game.getCurrentPlayer().getColor()) + ") " + " can't play anymore !");
		}
	}

}
