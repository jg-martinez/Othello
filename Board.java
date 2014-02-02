import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;




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
			if (this.game.getActualPlayer() instanceof HumanPlayer){
				command = this.readCommand();			
				if (command != null ) {
					command = command.toLowerCase();				
					if (command.matches("[a-h][1-8]")) {
						if(checkMove(command)){
							this.game.getActualPlayer().chooseAction(false);
							this.game.getActualPlayer().play(command);
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
		
	}
	
	public boolean checkMove(String move){		
		if(game.getActualPlayer().checkPossibleMove(move) == true){
			return true;
		} else { 
			return false;
		}		
	}
	
	public void help() {
		System.out.println("Use : \n" + HELP + " to get the command list \n"
				+ PRINT + " : to print the board \n"
				+ PLAY + " : to play a move \n"
				+ QUIT + " : quit the game.");
	}
	
	private String readCommand() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String resultat = null;
		try {
			resultat = br.readLine().trim();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}		
		return resultat;
	}
	
	public void update(Observable o, Object arg) {
		if(arg.equals("newRound")){
			System.out.println(this.game.toString());
			System.out.println("It is " + this.game.getActualPlayer().getName() + "'s turn (Player " + Character.toString((char)this.game.getActualPlayer().getColor()) +").");
			System.out.println(PROMPT);
		}
	}

}
