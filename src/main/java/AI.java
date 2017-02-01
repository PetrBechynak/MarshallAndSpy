package main.java;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by petr on 25.12.15.
 */
public class AI {
    ProposedMove propMove = null;
    ArrayList<Node> nodes = null;
    Integer maxDepth = 0;

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public AI(Board board) {
        nodes = new ArrayList<Node>();
        Node node = new Node(board,0,UUID.randomUUID());
        nodes.add(node);
        System.out.println("Pridan node:");
        System.out.println(node.toString());
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
        writeTreeEvaluation();
        // pristi tahy jsou ulozeny v nodech s hloubkou 1
        Optional<Node> maxEvaluatedNode = nodes.stream()
                .filter(a->a.getDepth().equals(1))
                .max(Comparator.naturalOrder());
        return maxEvaluatedNode.get();
    }

    public void writeTreeEvaluation(){

    }

    public void printEvaluations(Integer depth){
        for (Node node: this.nodes) {
            if (node.getDepth().equals(depth)) {
                System.out.println("depth "+node.depth+" eval: " + node.evaluation + " " + findParentDescription(node.parentId) + " " + node.description);
            }

        }
    };

    public String findParentDescription(java.util.UUID parentId) {
        Node foundNode=null;
        for (Node node:nodes) {
            if (node.getId().equals(parentId)){
                foundNode = node;
            }
        }
        if (foundNode != null && foundNode.getId().equals(parentId) & foundNode.getDepth()>=0) {
            return  findParentDescription(foundNode.parentId) +" "+ foundNode.getDescription();
        } else {
            return "";
        }
    }


    public void addDeeperNodes(Integer depth){
        ArrayList<Node> nodesToAdd = new ArrayList<>();
        Node newNode = null;
        double sumEval = 0;
        Player.Type me = ((depth % 2) != 1) ? Player.Type.COMPUTER : Player.Type.HUMAN;
        Player.Type finalMe = me;

        List<Node> previousNodes = this.nodes.stream().filter(a->a.getDepth().equals(depth)).collect(Collectors.toList());

        for (Node node: previousNodes){
            List<Figure> myFigures = node.stream().filter(a->a.getOwner().getType().equals(finalMe)).collect(Collectors.toList());
            for (Figure fig: myFigures){
                for (CoordVector attackMove: fig.allowedAttackMoves) {
                    // kdyz muzu zautocit s figurou fig
                    if (fig.hasOwner(me) && node.isLegalAttackOrMoveComp(me, attackMove, fig)) {
                        // vytvor novy uzel s touto figurou a timto tahem, hloubka je o 1 vyssi
                        newNode = new Node(node.getNewBoardMoveOrAttackCompFig(attackMove, fig), node.depth + 1, node.id);
                        newNode.setDescription(fig.toString() +"(" + fig.getPosition()+")" + "->" + attackMove.toString());
                        //System.out.println("===== addDeeperNodes " +fig.getPosition().toString() + " " + fig.getType() + " " + fig.getOwner().getType() + " moved by " + attackMove.toString() + " to " + fig.getPosition().moveCoordinates(attackMove));
                        //System.out.println(newNode.getId());
                        //System.out.println(newNode.toString());
                        nodesToAdd.add(newNode);
                        sumEval = sumEval + newNode.getEvaluation();
                    }
                }
            }

        }

        if (me.equals(Player.Type.COMPUTER)) {
            Collections.sort(nodesToAdd, (node1, node2) -> {
                int e1 = (int) node1.evaluation;
                int e2 = (int) node2.evaluation;
                return e1 - e2;
            });

        } else {
            Collections.sort(nodesToAdd, (node2, node1) -> {
                int e1 = (int) node1.evaluation;
                int e2 = (int) node2.evaluation;
                return e1 - e2;
            });
        }

        Integer i = 0;
        Integer nodesArraySize = nodesToAdd.size();
        Integer to;
        to = nodesArraySize<=500 ? nodesArraySize : 500;
        for (i=0;i<to;i++) {
            //System.out.println(to + " " +i);
            nodes.add(nodesToAdd.get(i));
        }

        //this.nodes.addAll(nodesToAdd);

    }


    public static class Node extends Board implements Comparable{
        Board board;
        UUID parentId = null;
        Integer depth = null;
        double evaluation;
        double minMaxEvaluation;
        UUID id = UUID.randomUUID();
        String description;

        @Override
        public String toString(){
            return "Depth:" + depth + ", size:" + this.size() + " evaluation: " + evaluation;
        }

        public double getMinMaxEvaluation() {
            return minMaxEvaluation;
        }

        public void setMinMaxEvaluation(double minMaxEvaluation) {
            this.minMaxEvaluation = minMaxEvaluation;
        }

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
            // add a small jitter so the AI doesn't repeat same allowedMoves
            return evaluation + rnd.nextDouble()/20;
        }

        public Node(Board board, Integer depth, UUID parentId){
            //this.board = board.stream().map(d -> d.deepClone()).collect(toCollection(Board::new));
            for (Figure fig: board){
                this.add(fig.deepClone());
            }
            this.parentId = parentId;
            this.depth = depth;
            evaluation = evaluateNode();
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double evaluateNode (){
            double sum=0;
            for (Figure fig: this) {
                sum = sum + fig.value;
            }
            return sum;
        }

        @Override
        public boolean equals(Object o){
            if (!(Node.class.equals(o.getClass()))) return false;
            Node node = (Node) o;
//            if (!this.getDepth().equals(node.depth)) return false;

            boolean equalLists = node.size()
                    == this.size() && node.containsAll(this);
            return equalLists;
        }

        @Override
        public int compareTo(Object o) {
            if (!o.getClass().equals(AI.Node.class)) return 0;
            AI.Node node = (AI.Node) o;
            if (((AI.Node) o).getMinMaxEvaluation()>this.getMinMaxEvaluation()) {
                return 1;
            } else if (((AI.Node) o).getMinMaxEvaluation()<this.getMinMaxEvaluation()) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    public ProposedMove getFigureMove(){
        return propMove;
    }

}

