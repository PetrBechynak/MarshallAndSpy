package main.java;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by petr on 25.12.15.
 */
public class AI {
    ProposedMove propMove = null;
    ArrayList<Node> nodes = null;
    Integer maxDepth = 0;

    AI(Board board) {
        nodes = new ArrayList<Node>();
        Node node = new Node(board,0,UUID.randomUUID());
        nodes.add(node);
        System.out.println("Evaluation: " + nodes.get(nodes.size() - 1).evaluation);
        addDeeperNodes();
        //maxDepth = AI.getMaxDepth();
        //AI.getBestLeafNode;
        printEvaluations();
    }

    public void printEvaluations(){
        for (Node node: this.nodes) {
            System.out.println("Node "+node.id+" depth "+node.depth+" eval: " + node.evaluation);

        }
    };

    public void addDeeperNodes(){
        ArrayList<Node> nodesToAdd = new ArrayList<>();
        for (Node node: this.nodes){
            for (Figure fig: node.board){
                for (CoordVector attackMove: fig.attackMoves) {
                    if (node.isLegalAttackMoveComp(attackMove,fig)) {
                        nodesToAdd.add(new Node(node.board.getNewBoardMoveCompFig(attackMove, fig),
                                node.depth + 1, node.id));
                    }
                }
            }
        }
        this.nodes.addAll(nodesToAdd);
    }

    class Node extends Board {
        Board board;
        UUID parentId = null;
        Integer depth = null;
        double evaluation;
        UUID id = UUID.randomUUID();
        Node(Board board, Integer depth, UUID parentId){
            this.board = board;
            this.parentId = parentId;
            this.depth = depth;
            evaluation = evaluateNode();
        }

        public double evaluateNode (){
            double sum=0;
            for (Figure fig: board) {
                sum = sum + fig.value;
            }
            return sum;
        }

    }

    public ProposedMove getFigureMove(){
        return propMove;
    };
}
