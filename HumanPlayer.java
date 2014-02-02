

public class HumanPlayer extends Player {
	
	HumanPlayer(String name, Game game){
		super(name, game);
	}
	
	public synchronized void chooseAction(boolean bool){
		suggestStrategy();		
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
	
	public void suggestStrategy(){
		//search algorithm
	}
	
	
}
