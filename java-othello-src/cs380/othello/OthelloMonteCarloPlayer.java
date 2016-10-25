package cs380.othello;

import java.util.*;

public class OthelloMonteCarloPlayer extends OthelloPlayer {

	int iterations; 
	int playernum;
	
	public OthelloMonteCarloPlayer (int iter, int pnum) {
		this.iterations = iter;
		this.playernum = pnum;
	}
	
	@Override
	public OthelloMove getMove (OthelloState state) {
		Node root = createNode(state);
		Node node;
		OthelloState node2;
		int node2score;
		
		for (int i = 0; i < iterations; i ++) {
			node = treePolicy(root);
			
			if (node != null) {
				node2 = defaultPolicy(node);
				node2score = node2.score();
				backup(node, node2score);
			}
		}
		//System.out.println(root.move);
		//System.out.println(bestChild(root).move);
		return bestChild(root).move;	
	}	
	
	public Node createNode (OthelloState state) {
		Node parent = null;
		List<Node> nodeChildren = new ArrayList<Node>();
		OthelloMove move = new OthelloMove(0, 0, 0);
		int vnum = 0;
		int ascore = 0;
		int pn = 0;
		
		Node node = new Node(state, parent, nodeChildren, move, vnum, ascore, pn);
		return node;
	}
	
	public Node createChild (OthelloState state, Node parent, List<Node> children,
			OthelloMove move, int visited, int avgsc, int playernum) {
		
		Node nodechild = createNode(state);
		nodechild.parent = parent;
		nodechild.children = children; 
		nodechild.move = move; 
		nodechild.numvisited = visited;
		nodechild.avgscore = avgsc;
		nodechild.playernum = playernum;
		return nodechild;
	}
	
	public Node bestChild (Node node) {
		if (node.playernum == 1) {
			int maxavg = Integer.MIN_VALUE;
			List<Node> nodeChild = node.children;
			Node n = null;
			
			for (int i = 0; i < nodeChild.size(); i ++) {
				if (nodeChild.get(i).avgscore > maxavg) {
					maxavg = nodeChild.get(i).avgscore;
					n = nodeChild.get(i);
				}
			}
			return n;
		}
		
		else {
			int minavg = Integer.MAX_VALUE;
			List<Node> nodeChild = node.children;
			Node n = null;
			
			for (int i = 0; i < nodeChild.size(); i ++) {
				if (nodeChild.get(i).avgscore < minavg) {
					minavg = nodeChild.get(i).avgscore;
					n = nodeChild.get(i);
				}
			}
			return n;
		}
	}
	
	public Node treePolicy (Node node) {
		OthelloState s = node.state;
		List<OthelloMove> allmoves = s.generateMoves();
		List<Node> children = node.children;
		OthelloState newstate;
		Node newnode = null;
		Node nodetmp = null;
		OthelloMove currmove = null;
		Random r = new Random();
		
		// if node is a terminal node, then return node
		if (allmoves.size() == 0) {
			return node;
		}
		
		// else if node still has any children that are not in the tree, then ..
		List<Node> nchildren = new ArrayList<Node>();
		for (int i = 0; i < allmoves.size(); i ++) {
			currmove = allmoves.get(i);
			// generate one of the children 
			newstate = s.applyMoveCloning(currmove);
			 
			if (!children.contains(newstate)) {
				newnode = createChild(newstate, node, nchildren, currmove, 0, 0, playernum);
				// add newnode as a child to node
				node.children.add(newnode);
				return newnode;
			}
		}
		
		// else if all the children of the node are in the tree and node is not terminal
		// random number = ((10 - 0) + 1) + 0
		int choice = r.nextInt(11);
		
		if (choice < 9) {
			// 90% of the time 
			nodetmp = bestChild(node);
		}
		
		else {
			// 10% of the time
			OthelloMove om = allmoves.get(r.nextInt(allmoves.size()));
			OthelloState os = s.applyMoveCloning(om);
			nodetmp = createNode(os);
		}
		
		return treePolicy(nodetmp);
	}
	
	public OthelloState defaultPolicy (Node node) {
		OthelloState state = node.state;
		OthelloRandomPlayer randplayer = new OthelloRandomPlayer();
		OthelloMove move = null;
		
		while (!state.gameOver()) {
			move = randplayer.getMove(state);
			state = state.applyMoveCloning(move);
		}
		
		return state;
	}
	
	public int score (Node node) {
		OthelloState state = node.state;
		int sc = state.score();
		return sc;
	}
	
	public void backup (Node node, int score) {
		int v = node.numvisited;
		node.numvisited ++;
		
		int totalscore = (node.avgscore * v) + score;
		node.avgscore = totalscore / node.numvisited;
		
		if (node.parent != null) {
			backup (node.parent, score);
		}
	}

}
