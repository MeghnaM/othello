package cs380.othello;

import java.util.List;

public class Node {
	
	OthelloState state;
	Node parent;
	List<Node> children;
	OthelloMove move;
	int numvisited;
	int avgscore;
	int playernum;
	
	public Node (OthelloState state, 
			Node parent, 
			List<Node> children, 
			OthelloMove move, 
			int vnum, 
			int ascore, 
			int playernum) {
		
		this.state = state;
		this.parent = parent;
		this.children = children;
		this.move = move;
		this.numvisited = vnum;
		this.avgscore = ascore;
		this.playernum = playernum;
	}
}
