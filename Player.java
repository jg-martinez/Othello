public abstract class Player {
	private String name;
	private int score;
	private int color;
	private int[][] possibleMove;	
	private int[] posXY;
	
	public static char EMPTYBOX = ' ';
	
	public static int DEPTH_MAX = 10;
	
	public Player(String arg0){
		this.name = arg0;
		this.score = 0;
		this.color = 0;
		this.possibleMove = new int[8][8];
		this.posXY = new int[2];
	}
	
	public abstract void chooseAction(boolean bool, Game game);
	
		
	public void suggestStrategy(Game game){
		this.createPossibleMove(game);
		this.minMax(game);
		this.getPossibleMove()[this.getposXY()[1]][this.getposXY()[0]] = 'B';
	}		
	
	public void createPossibleMove(Game game){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				this.possibleMove[j][i] = 0;
			}
		}
		for(int i = 0; i < 8; i++){ //X
			for(int j = 0; j < 8; j++){ //Y
				if(game.getReversi()[j][i] == EMPTYBOX){ //if the box is empty, maybe we can play there
					int valueAction = 0;
					//we check if an adversary color is around this box
					if(i-1 >= 0 && j-1 >= 0 && game.getReversi()[j-1][i-1] != EMPTYBOX && game.getReversi()[j-1][i-1] != this.color){
						valueAction += checkTopLeft(i-1, j-1, game);//check top left diagonal
					}
					if(j-1 >= 0 && game.getReversi()[j-1][i] != 0 && game.getReversi()[j-1][i] != this.color){
						valueAction += checkTop(i, j-1, game); 
					}
					if(i+1 < 8 && j-1 >= 0 && game.getReversi()[j-1][i+1] != EMPTYBOX && game.getReversi()[j-1][i+1] != this.color){
						valueAction += checkTopRight(i+1, j-1, game);
					}
					if(i+1 < 8 && game.getReversi()[j][i+1] != EMPTYBOX && game.getReversi()[j][i+1] != this.color){
						valueAction += checkRight(i+1, j, game);
					}
					if(i+1 < 8 && j+1 < 8 && game.getReversi()[j+1][i+1] != EMPTYBOX && game.getReversi()[j+1][i+1] != this.color){
						valueAction += checkBottomRight(i+1, j+1, game);
					}
					if(j+1 < 8 && game.getReversi()[j+1][i] != EMPTYBOX && game.getReversi()[j+1][i] != this.color){
						valueAction += checkBottom(i, j+1, game);
					}
					if(i-1 >= 0 && j+1 < 8 && game.getReversi()[j+1][i-1] != EMPTYBOX && game.getReversi()[j+1][i-1] != this.color){
						valueAction += checkBottomLeft(i-1, j+1, game);
					}
					if(i-1 >= 0 && game.getReversi()[j][i-1] != EMPTYBOX && game.getReversi()[j][i-1] != this.color){
						valueAction += checkLeft(i-1, j, game);
					}
					if(valueAction != 0) {
						possibleMove[j][i] = valueAction+1;
					} else {
						possibleMove[j][i] = 0;
					}					
				}
			}
		}
	}
		
	public int checkTopLeft(int i, int j, Game game){
		int returnValue = 0;
		
		while(i >= 0 && j >= 0){
			if(game.getReversi()[j][i] != EMPTYBOX && game.getReversi()[j][i] != this.color){
				returnValue += 1;
			} else if(game.getReversi()[j][i] == this.color){
				return returnValue;
			} else {
				return 0;
			}
			j--;
			i--;
		}		
		return 0;
	}

	public int checkTop(int i, int j, Game game){
		int returnValue = 0;

		while(j >= 0){
			if(game.getReversi()[j][i] != EMPTYBOX && game.getReversi()[j][i] != this.color){
				returnValue += 1;
			} else if(game.getReversi()[j][i] == this.color){
				return returnValue;
			} else {
				return 0;
			}
			j--;
		}
		return 0;
	}
		
	public int checkTopRight(int i, int j, Game game){
		int returnValue = 0;
		
		while(i < 8 && j >= 0){
			if(game.getReversi()[j][i] != EMPTYBOX && game.getReversi()[j][i] != this.color){
				returnValue += 1;
			} else if(game.getReversi()[j][i] == this.color){
				return returnValue;
			} else {
				return 0;
			}
			j--;
			i++;
		}		
		return 0;
	}
	
	public int checkRight(int i, int j, Game game){
		int returnValue = 0;

		while(i < 8){
			if(game.getReversi()[j][i] != EMPTYBOX && game.getReversi()[j][i] != this.color){
				returnValue += 1;
			} else if(game.getReversi()[j][i] == this.color){
				return returnValue;
			} else {
				return 0;
			}
			i++;
		}
		return 0;
	}
	
	public int checkBottomRight(int i, int j, Game game){
		int returnValue = 0;
		
		while(i < 8 && j < 8){
			if(game.getReversi()[j][i] != EMPTYBOX && game.getReversi()[j][i] != this.color){
				returnValue += 1;
			} else if(game.getReversi()[j][i] == this.color){
				return returnValue;
			} else {
				return 0;
			}
			j++;
			i++;
		}		
		return 0;
	}
	
	public int checkBottom(int i, int j, Game game){
		int returnValue = 0;

		while(j < 8){
			if(game.getReversi()[j][i] != EMPTYBOX && game.getReversi()[j][i] != this.color){
				returnValue += 1;
			} else if(game.getReversi()[j][i] == this.color){
				return returnValue;
			} else {
				return 0;
			}
			j++;
		}
		return 0;
	}
	
	public int checkBottomLeft(int i, int j, Game game){
		int returnValue = 0;
		
		while(i >= 0 && j < 8){
			if(game.getReversi()[j][i] != EMPTYBOX && game.getReversi()[j][i] != this.color){
				returnValue += 1;
			} else if(game.getReversi()[j][i] == this.color){
				return returnValue;
			} else {
				return 0;
			}
			j++;
			i--;
		}		
		return 0;
	}
	
	public int checkLeft(int i, int j, Game game){
		int returnValue = 0;

		while(i >= 0){
			if(game.getReversi()[j][i] != EMPTYBOX && game.getReversi()[j][i] != this.color){
				returnValue += 1;
			} else if(game.getReversi()[j][i] == this.color){
				return returnValue;
			} else {
				return 0;
			}
			i--;
		}
		return 0;
	}
	
	public boolean checkPossibleMove(String move){
		int posX = Character.getNumericValue(move.charAt(0)) - 10;
		int posY = Character.getNumericValue(move.charAt(1)) - 1;			
		if(possibleMove[posY][posX] != 0){
			return true;
		} else {
			return false;
		}			
	}
	
	public boolean hasNoMoreMove(){
		boolean bool = true;
		for(int i = 0; i < 8; i++){
			for(int j =0; j < 8; j++){
				if(this.possibleMove[i][j] != 0) bool = false;
			}
		}
		return bool;
	}
	
	public void minMax(Game game){
		long T = System.currentTimeMillis();
		int depth = DEPTH_MAX;
		maxMove(game, T, depth);		
	}
	
	public int maxMove(Game game, long T, int depth){		
		if(game.endGame() || (int)(System.currentTimeMillis() - T) > game.getTimeLimit() || depth <= 0){
			return game.getCurrentPlayer().getScore();
		} else {
			int bestScore = 0; //we try to maximize the score
			int scoreTemp = 0;			
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){					
					if(this.getPossibleMove()[j][i] != 0){//if it's a possible move
						Game gamebis = new Game(game); //we duplicate the game
						gamebis.setReversi(posXY[1], posXY[0], this.getColor()); //we apply this move
						gamebis.nextPlayer(); //we will minimize the opponent move in minMove
						//we stay at the current player on game variable
						scoreTemp = minMove(gamebis, T, depth - 1);
						if(scoreTemp > bestScore){						
							this.posXY[0] = i; //we save the move on the actual player
							this.posXY[1] = j;	
							bestScore = scoreTemp;
						}
					}
				}
			}
			return bestScore;
		}		
	}
	
	public int minMove(Game game, long T, int depth){
		int bestScore = 500; //score to minimize
		int scoreTemp = 0;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.getPossibleMove()[j][i] != 0){ //if it's a possible move
					Game gamebis = new Game(game); //we duplicate the game
					gamebis.setReversi(posXY[1], posXY[0], this.getColor()); //we apply this move
					gamebis.nextPlayer(); //we will maximize the current player move in maxMove
					scoreTemp = maxMove(gamebis, T, depth - 1);
					//we stay at the opponent player on game variable
					if(scoreTemp < bestScore){						
						this.posXY[0] = i;
						this.posXY[1] = j;		
						bestScore = scoreTemp;
					}
				}
			}
		}
		return bestScore;		
	}
	
	public String getLastMove(){
		String string = new String();
		string = Character.toString((char)(this.posXY[0]+97));
		string += Character.toString((char)(this.posXY[1]+49));
		return string;		
	}
		
	public int getScore(){
		return this.score;
	}
	
	public int getColor(){
		return this.color;
	}
	
	public int[] getposXY(){
		return this.posXY;
	}	
	
	public String getName(){
		return this.name;
	}
	
	public int[][] getPossibleMove(){
		return this.possibleMove;
	}
	
	public void setColor(int arg0){
		this.color = arg0;
	}
	
	public void setScore(Game game){
		this.score = 0;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(game.getReversi()[i][j] == this.color){
					this.score++;
				}
			}
		}
	}	
	
	public String toString(){
		String string = new String();
		string = "\t" + this.getName() + " : " + this.getScore();
		return string;
	}
}
