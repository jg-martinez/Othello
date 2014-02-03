

public class HumanPlayer extends Player {
	
	HumanPlayer(String name){
		super(name);
	}
	
	public synchronized void chooseAction(boolean bool, Game game){
		suggestStrategy(game);		
		while (bool) {
			try {
				wait();
				if(bool) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();

	}	
	
}
