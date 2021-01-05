/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.naiveplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;



public class NaivePlayer extends Player {
	class Piece {
		int x;
		int y;
	Piece(int x, int y) {
		this.x = x;
		this.y = y;}
	
	};
	static final int MAX_VALUE = 100000;
	static final int MIN_VALUE = -100000;
	
	public static void main(String[] args) {}

    private Random random = new Random(0xdeadbeef);


    @Override
    public String getName() {
        return "Gracz Naiwny 84868";
    }

    @Override
    public Move nextMove(Board b) {
    	double value;
    	int depth = 3;
    	Board b1 = b.clone();
        List<Move> moves = b1.getMovesFor(getColor());
		double[][] movement_values = new double[moves.size()][3];
        for (int i =0; i < moves.size(); i++) {
        	b1.doMove(moves.get(i));
        	value = this.minValue(b1, depth -1 , Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        	b1.undoMove(moves.get(i));
        	movement_values[i][0]=value;
			movement_values[i][1]=i; //movement id number
        }
        return moves.get(0);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	public double maxValue(Board b, int depth, double alpha, double beta) {
		

		
		
		if (depth==1 || b.getWinner(getColor())!=Color.EMPTY||b.getWinner(getColor())!=null||b.getWinner(getOpponent(getColor()))!=Color.EMPTY || b.getWinner(getOpponent(getColor()))!=null)
				return evaluation_func(b,depth); // return the heuristic value
		
		List<Move> a = b.getMovesFor(getOpponent(getColor()));
		
		double value = Double.NEGATIVE_INFINITY;

     	for(int i=0;i<a.size();i++){
			    		
     		b.doMove(a.get(i));
     		
			value = Math.max(value, minValue( //
					b, depth-1, alpha, beta));
			b.undoMove(a.get(i));

		}
     	
		return value;
	}
	
	public double minValue(Board b, int depth, double alpha, double beta) {
		
		
		if (depth==1 || b.getWinner(getColor())!=Color.EMPTY||b.getWinner(getColor())!=null||b.getWinner(getOpponent(getColor()))!=Color.EMPTY || b.getWinner(getOpponent(getColor()))!=null)
				return evaluation_func(b,depth); // return the heuristic value
		
		double value = Double.POSITIVE_INFINITY;
		List<Move> a = b.getMovesFor(getOpponent(getColor()));
		
		for(int i=0;i<a.size();i++){
			
			b.doMove(a.get(i));
			
			value = Math.min(value, maxValue(b, depth-1, alpha, beta));
			b.undoMove(a.get(i));

		}
		
		return value;
	}

//The Evaluation function, which computes the heuristic value depending in which case the
	//minimax search has reached.. (opponent winning, me winning or maximum depth searched) 
	int evaluation_func(Board b,int depth) {
		
    	int value=0;
    	
    	if(b.getWinner(getColor())==getOpponent(getColor())){  //opponent win
    		value = MIN_VALUE*depth; // *** depth needs to be added here, to inform us how far is the win or the lose.. I have more details in my report..
    	}else if(b.getWinner(getColor()) ==getColor()){ ////my win
    		value = MAX_VALUE*depth;
    	}else{
    		
    		// I want to intone the Board positions in which I block more than one opponent's peggle
    		//to move...
    		int bl = (b.getMovesFor(getOpponent(getColor()))).size();
    		
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
}
