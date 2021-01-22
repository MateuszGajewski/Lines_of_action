/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.naiveplayer;
import java.util.ArrayList;
import java.util.List;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.linesofaction.LinesOfActionMove;




public class Barabasz extends Player {
	
	public static void main(String[] args) {}
	class Piece {
		int x;
		int y;
	Piece(int x, int y) {
		this.x = x;
		this.y = y;}
	
	};
	static final long MAX_VALUE = 10000000;
	static final long MIN_VALUE = -10000000;
	
	


    @Override
    public String getName() {
        return "Eryk Kosmala 141249 Mateusz Gajewski 141217";
    }

    @Override
    public Move nextMove(Board b) {
    	long start = System.currentTimeMillis();
    	long limit = getTime();
    	double value;
    	int depth = 3;
    	Board b1 = b.clone();
		double resultValue = depth*MIN_VALUE;
		int bestmove = 0;
        List<Move> moves = b1.getMovesFor(getColor());
		double[][] movement_values = new double[moves.size()][3];
		System.out.println(moves.size());
		for (int i =0; i < moves.size(); i++) {
        	movement_values[i][0]=resultValue;

		}
		
        for (int i =0; i < moves.size(); i++) {
        	b1.doMove(moves.get(i));
        	value = this.minValue(b1, depth -1 , Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, getOpponent(getColor()));
        	b1.undoMove(moves.get(i));
        	movement_values[i][0]=value;
			movement_values[i][1]=i; //movement id number
			if (value > resultValue) {
				bestmove = i;
				resultValue = value;	}
			if (System.currentTimeMillis() - start > limit - 3) {
				return moves.get(bestmove);
			}
        }
        movement_values=D_BubbleSort(movement_values); 
        bestmove =(int) movement_values[0][1];
		for(int bi=0;bi<5;bi++){
		
			
			
			b.doMove(moves.get((int)movement_values[bi][1]));
			//RESEARCHIN THE MOVE : ORISMOS https://chessprogramming.wikispaces.com/Singular+Extensions

			value = minValue(b,depth+1,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, getOpponent(getColor()));

			
			//updating the movement value...
			movement_values[bi][0]=value;
			if (value > resultValue) {
				bestmove = (int) movement_values[bi][1];
				resultValue = value;	}
			if (System.currentTimeMillis() - start > limit - 3) {
				return moves.get(bestmove);
			}
				
			b.undoMove(moves.get((int)movement_values[bi][1]));

		
		}		
		
		movement_values = D_BubbleSort(movement_values);

        return moves.get((int) movement_values[0][1]);
    }
    
    
    public double[][] D_BubbleSort( double [][] num ) //DESCEDING ORDER
	{
	     int j;
	     boolean flag = true;   // set flag to true to begin first pass
	     double temp;   //holding variable

	     while ( flag )
	     {
	            flag= false;    //set flag to false awaiting a possible swap
	            for( j=0;  j < num.length -1;  j++ )
	            {
	                   if ( num[ j ][0] < num[j+1][0] )   // change to > for ascending sort
	                   {
	                           temp = num[ j ][0];                //swap elements
	                           num[ j ][0] = num[ j+1 ][0];
	                           num[ j+1 ][0] = temp;
	                           temp = num[ j ][1];                //swap elements
	                           num[ j ][1] = num[ j+1 ][1];
	                           num[ j+1 ][1] = temp;
	                           temp = num[ j ][2];                //swap elements
	                           num[ j ][2] = num[ j+1 ][2];
	                           num[ j+1 ][2] = temp;
	                          flag = true;              //shows a swap occurred  
	                  } 
	            } 
	      }
	     return num;
	} 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	public double maxValue(Board b, int depth, double alpha, double beta, Color c) {
		

		
		
		if (depth==1 || b.getWinner(c)==Color.PLAYER1||b.getWinner(c)==Color.PLAYER2)
				return evaluation_func(b,depth, c); // return the heuristic value
		
		
		double value = Double.NEGATIVE_INFINITY;
		List<Move> a = b.getMovesFor(c);
		if (c == Color.PLAYER1) {
			c = Color.PLAYER2;
		}
		else {
			c = Color.PLAYER1;
		}
     	for(int i=0;i<a.size();i++){
			    		
     		b.doMove(a.get(i));
     		
			value = Math.max(value, minValue( 
					b, depth-1, alpha, beta, c));
			b.undoMove(a.get(i));

		}
     	if (value >= beta)
			return value;
		alpha = Math.max(alpha, value);
     	
		return value;
	}
	
	public double minValue(Board b, int depth, double alpha, double beta, Color c) {
		
		
		if (depth==1 || b.getWinner(c)==Color.PLAYER1||b.getWinner(c)==Color.PLAYER2)
				return evaluation_func(b,depth, c); // return the heuristic value
		
		double value = Double.POSITIVE_INFINITY;
		List<Move> a = b.getMovesFor(c);
		
		if (c == Color.PLAYER1) {
			c = Color.PLAYER2;
		}
		else {
			c = Color.PLAYER1;
		}
		
		for(int i=0;i<a.size();i++){
			
			b.doMove(a.get(i));
			
			value = Math.min(value, maxValue(b, depth-1, alpha, beta, c));
			b.undoMove(a.get(i));

		}
		if (value <= alpha)
			return value;
		beta = Math.min(beta, value);
		return value;
	}

//The Evaluation function, which computes the heuristic value depending in which case the
	//minimax search has reached.. (opponent winning, me winning or maximum depth searched) 
	long evaluation_func(Board b,int depth, Color c) {
		
    	long value=0;
    	
    	if(b.getWinner(c)==getOpponent(getColor())){  //opponent win
    		value = MIN_VALUE*depth; // *** depth needs to be added here, to inform us how far is the win or the lose.. I have more details in my report..
    	}else if(b.getWinner(c) ==getColor()){ ////my win
    		value = MAX_VALUE*depth;
    	}else{
    		
    		// I want to intone the Board positions in which I block more than one opponent's peggle
    		//to move...
    		int bl = getBlocked(b, getOpponent(getColor()));
    		
    		List<Piece> myCheckers = obtainAllPlayerPieces(b,getColor()); 
        	List<Piece> opponentCheckers = obtainAllPlayerPieces(b, getOpponent(getColor()));
    		int myDist = getSumDistance_betweenCheckers(myCheckers, getColor());
    		int opDist = getSumDistance_betweenCheckers(opponentCheckers, getOpponent(getColor()));
    		
    		//The main goal is to maximize the value 
    		//(To bring my peggles more closer to each other
    		// than opponent's, which i want to separate them off)
    		value = opDist - myDist + bl*10; 
    	}
  	
    	return value;
    }
	
	
	List<Piece> obtainAllPlayerPieces(Board b, Color c){
		List<Piece> ans = new ArrayList<Piece>();
		for (int x = 0; x < b.getSize(); x++) {
            for (int y = 0; y < b.getSize(); y++) {
                if (b.getState(x, y)==c) {
                	ans.add(new Piece(x, y));
                }}}
		return ans;
	}
	


	int getSumDistance_betweenCheckers(List<Piece> pieces, Color c) {
		int sumof_distances = 0;
    	for(int i = 0; i < pieces.size();i++){
    		for(int j = 0; j < i;j++){
    			// distance calculation between 2 checkers , with Pythagorean Theorem
    			// Distance = a^2 (line distance) + b^2 (row distance)
    			int a = (int) Math.pow(pieces.get(i).x-pieces.get(j).x,2);
    	    	int b = (int) Math.pow(pieces.get(i).y-pieces.get(j).y,2);
    	    	sumof_distances += Math.sqrt(a+b);
    		}
    	}
    	return sumof_distances;
		
	}
	int getBlocked(Board b, Color c) {
		int ans = 0;
		List<Piece> Checkers = obtainAllPlayerPieces(b,c);
		List<Move> moves = b.getMovesFor(c);
		int blocked = 0;
		for(int i = 0; i < Checkers.size(); i++) {
			blocked = 0;
			for(int j = 0; j < moves.size(); j++  ) {
		        LinesOfActionMove m = (LinesOfActionMove) moves.get(j);
				if(Checkers.get(i).x == m.getSrcX() && Checkers.get(i).y == m.getSrcY()) {
					blocked = 1;
				}
			}
			if (blocked == 0) {
				ans+=1;
			}
		}
		
		return ans;
	}
}
