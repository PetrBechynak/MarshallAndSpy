package main.java;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by petr on 25.12.15.
 */
public class AI {
    ProposedMove propMove = null;
    ArrayList<Node> nodes = null;
    Integer maxDepth = 0;

    public AI(Board board) {
        nodes = new ArrayList<Node>();
        Node node = new Node(board,0,UUID.randomUUID());
        nodes.add(node);
        System.out.println("Evaluation: " + nodes.get(nodes.size() - 1).evaluation);

    }

    public Integer getMaxDepth(){
    Integer depth = 0;
        for (Node N: nodes ) {
            if (N.getDepth()>depth) {
                depth = N.getDepth();
            }
        }
        return depth;
    }

    public Board getBestMove() {
        Integer maxDepth = getMaxDepth();
        double maxEval = 1000.0;
        Move move=null;
        Node bestLeaf=null;
        Node nextMove;
        Integer i=0;

        for (Node node:nodes) {
            if (node.getDepth().equals(maxDepth)){
                System.out.println("-------------------");
                System.out.println("Depth: " + node.getDepth());
                System.out.println(node.toString());
                System.out.println(node.getEvaluation());
                if (node.getEvaluation()<maxEval) {
                    bestLeaf=node;
                    maxEval=node.getEvaluation();
                }
            }
        }

        nextMove = bestLeaf;
        for (i=maxDepth;i>1;i--){
            for (Node parentNode: nodes) {
                System.out.println("i:" + i + " depth: " + parentNode.getDepth()+ " eval: "+parentNode.getEvaluation());

                if (parentNode.getId().equals(nextMove.getParentId()) & parentNode.getDepth()>0){
                    System.out.println("i:" + i + " depth: " + parentNode.getDepth()+ " eval: "+parentNode.getEvaluation());
                    nextMove=parentNode;
                }

            }
        }
        //System.out.print(nextMove.toString());
        return nextMove;

    }

    public void printEvaluations(Integer depth){
        for (Node node: this.nodes) {
            if (node.getDepth().equals(depth)) {
                System.out.println("Node "+node.id+" depth "+node.depth+" eval: " + node.evaluation);
            }

        }
    };

    public void addDeeperNodes(Integer depth){
        ArrayList<Node> nodesToAdd = new ArrayList<>();
        Node newNode = null;
        Player.Type me = null;
        if ((depth % 2) != 1) {
            me = Player.Type.COMPUTER;
        } else {
            me = Player.Type.HUMAN;
        }

        for (Node node: this.nodes){
            if (node.getDepth().equals(depth)){
              for (Figure fig: node){
                 for (CoordVector attackMove: fig.attackMoves) {

                    if (fig.getOwner().getType().equals(me) && node.isLegalAttackMoveComp(me, attackMove, fig)) {
                        newNode = new Node(node.getNewBoardMoveCompFig(attackMove, fig), node.depth + 1, node.id);
                        System.out.println("===== addDeeperNodes " +fig.getPosition().toString() + " " + fig.getType() + " " + fig.getOwner().getType() + " moved by " + attackMove.toString() + " to " + fig.getPosition().moveCoordinates(attackMove));
                        System.out.println(newNode.getId());
                        System.out.println(newNode.toString());
                        nodesToAdd.add(newNode);
                    }
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

        public Board getBoard() {
            return board;
        }

        public UUID getId() {
            return id;
        }

        public UUID getParentId() {
            return parentId;
        }

        public Integer getDepth() {
            return depth;
        }

        public double getEvaluation() {
            Random rnd = new Random();
            // add a small jitter so the AI doesn't repeat same moves
            return evaluation + rnd.nextDouble()/2;
        }

        Node(Board board, Integer depth, UUID parentId){
            //this.board = board.stream().map(d -> d.deepClone()).collect(toCollection(Board::new));
            for (Figure fig: board){
                this.add(fig.deepClone());
            }
            this.parentId = parentId;
            this.depth = depth;
            evaluation = evaluateNode();
        }

        public double evaluateNode (){
            double sum=0;
            for (Figure fig: this) {
                sum = sum + fig.value;
            }
            return sum;
        }

    }

    public ProposedMove getFigureMove(){
        return propMove;
    };
}
