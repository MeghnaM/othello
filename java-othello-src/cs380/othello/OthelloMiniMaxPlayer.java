package cs380.othello;

import java.util.List;

public class OthelloMiniMaxPlayer extends OthelloPlayer {

	int depth = 0;
	int finaldepth;
	int playernum;
	
	// Constructor whose input is the depth up to which we want to search
	public OthelloMiniMaxPlayer(int d, int pnum)
	{
		this.finaldepth = d;
		this.playernum = pnum;
	}
	
	@Override
	public OthelloMove getMove(OthelloState state) 
	{
		// generate the list of moves:
        List<OthelloMove> moves = state.generateMoves();  
        
        OthelloMove bestmove = null;
        if (!moves.isEmpty())
        {
        	// get the optimal move using MiniMax 
        	bestmove = minimaxDecision(state, moves, depth, playernum);
        	
        	// return it
        	return bestmove;
        }
        
        // If there are no possible moves, just return "pass":
		return null;
	}
	
	public OthelloMove minimaxDecision(OthelloState state, List<OthelloMove> moves, int depth, int playernum)
	{
		// return best action for that player
		// decision changes based on whether first or second player is playing
		
		int minval;
		int maxval; 
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		OthelloState result;
		OthelloMove optmove = null; 
		
		// playernum 1 = get the min of all max
		if (playernum == 1) 
		{
			for (int i = 0; i < moves.size(); i ++)
			{
				// result = result of applying a move to the current state
				result = state.applyMoveCloning(moves.get(i));
			
				minval = minValue(result, depth);
				if (minval > max)
				{
					maxval = minval;
					max = minval;
					optmove = moves.get(i);
				}
			}
			
			return optmove;
		}
			
		// else get the max of all min
		else 
		{
			for (int i = 0; i < moves.size(); i ++)
			{
				// result = result of applying a move to the current state
				result = state.applyMoveCloning(moves.get(i));
			
				maxval = maxValue(result, depth);
				if (maxval < min)
				{
					minval = maxval;
					min = maxval;
					optmove = moves.get(i);
				}
			}
			
			return optmove;
		}
	}
	
	
	public int maxValue(OthelloState currstate, int depth)
	{
		// if game is complete or if it reaches said depth, return current state
		if (currstate.gameOver() || depth == finaldepth)
		{
			return currstate.score();
		}
		
		// generate the list of moves for the current state
        List<OthelloMove> currmoves = currstate.generateMoves();
		
		// increment the depth
		depth ++;
		
        int max = Integer.MIN_VALUE;
        int min;
        OthelloState result;
		for (int i = 0; i < currmoves.size(); i ++)
		{
			// result = result of applying a move to the current state
			result = currstate.applyMoveCloning(currmoves.get(i));
			
			// get the minValue of the resulting state
			min = minValue(result, depth);
			
			// store the maximum score out of all the states in the variable 'max'
			if (min > max) 
			{
				max = min;
			}
		}
		
		return max;
	}
	
	public int minValue(OthelloState currstate, int depth)
	{
		// if game is complete or if it reaches said depth, return current state
		if (currstate.gameOver() || depth == finaldepth)
		{
			return currstate.score();
		}
				
		// generate the list of moves for the current state
		List<OthelloMove> currmoves = currstate.generateMoves();
				
		// increment the depth
		depth ++;
		
		int min = Integer.MAX_VALUE;
		int max;
		OthelloState result;
		for (int i = 0; i < currmoves.size(); i ++)
		{
			// result = result of applying a move to the current state
			result = currstate.applyMoveCloning(currmoves.get(i));
					
			// get the maxValue of the resulting state
			max = maxValue(result, depth);
					
			// store the minimum score out of all the states in the variable 'min'
			if (max < min) 
			{
				min = max;
			}
		}
				
		return min;		
	}
	
}
