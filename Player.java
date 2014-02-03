public abstract class Player {
	private String name;
	private int score;
	private int color;
	private int[][] possibleMove;	
	private int[] posXY;
	
	private static int MAX_ITERATION = 3;	
	public static char EMPTYBOX = ' ';
	
	
	public Player(String arg0){
		this.name = arg0;
		this.score = 0;
		this.color = 0;
		this.possibleMove = new int[8][8];
		this.posXY = new int[2];
	}
	
	public abstract void chooseAction(boolean bool, Game game);
	
	public void play(int posX, int posY, Game game){
		game.setReversi(posX,posY, this.color);
	}
	
	
	
	public void suggestStrategy(Game game){
		createPossibleMove(game);
		this.setPosXY(minMax(game));
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
				if(game.getReversi()[j][i] == EMPTYBOX){ //if the square is empty, maybe we can play here
					int valueAction = 0;
					//we check if an adversary color is around this square
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
	
	public int[] minMax(Game game){
		int[] posXY = new int[2];
		posXY = maxMove(game, 0);		
		return posXY;
	}
	
	public int[] maxMove(Game game, int depth_first){
		int[] posXY = new int[2];
		if(game.endGame() || depth_first >= MAX_ITERATION){
			return posXY;
		} else {
			int best_move = 0;
			Game gamebis = new Game(game);
			gamebis.setReversi(posXY[1], posXY[0], this.getColor());
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(this.getPossibleMove()[j][i] != 0){//if it's a possible move
						posXY = minMove(gamebis, depth_first+1);
						if(game.getReversi()[posXY[1]][posXY[0]] > best_move){						
							posXY[0] = i;
							posXY[1] = j;						
						}
					}
				}
			}
			return posXY;
		}		
	}
	
	public int[] minMove(Game game, int depth_first){
		int[] posXY = new int[2];
		int best_move = 0;
		Game gamebis = new Game(game);
		gamebis.setReversi(posXY[1], posXY[0], this.getColor());
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.getPossibleMove()[j][i] != 0){//if it's a possible move
					posXY = maxMove(gamebis, depth_first);
					if(game.getReversi()[posXY[1]][posXY[0]] > best_move){						
						posXY[0] = i;
						posXY[1] = j;						
					}
				}
			}
		}
		return posXY;		
	}
	
	public String getLastMove(){
		String string = new String();
		string = Character.toString((char)(this.posXY[1]+97));
		string += Character.toString((char)(this.posXY[0]+49));
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
	
	public void setPosXY(int[] posXY){
		this.posXY[0] = posXY[0];
		this.posXY[1] = posXY[1];
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
