
public class ArtificialIntelligence extends Player {
	
	ArtificialIntelligence(String name){
		super(name);
	}
	
	public void chooseAction(boolean bool, Game game){		
		createPossibleMove(game);	
		minMax(game);
		game.setReversi(this.getposXY()[0],this.getposXY()[1], game.getCurrentPlayer().getColor());		
	}	
}
