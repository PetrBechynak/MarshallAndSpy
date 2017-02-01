package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by petr on 1.2.17.
 */

public class TreeUnit {
    TreeUnit parent;
    AI.Node node = null;
    Integer depth;
    List<TreeUnit> children = new ArrayList<>();
    Double eval;



    public void fillTree(List<AI.Node> nodes, Integer fillFromDepth){
        if (nodes.isEmpty()) return;
        if (fillFromDepth.equals(0)) {
            // nacti root s hloubkou 0
            depth = fillFromDepth;
            parent = null;
            node = nodes.stream().filter(a -> (a.getDepth().equals(0))).collect(Collectors.toList()).get(0);

            List<AI.Node> nodeBranches = nodes.stream()
                    .filter(a -> !(a.getDepth().equals(fillFromDepth)))
                    .filter(a -> a.getParentId().equals(node.getId()))
                    .collect(Collectors.toList());

            nodeBranches.forEach(a-> children.add(new TreeUnit(a,fillFromDepth+1,new ArrayList<TreeUnit>(),null, this)));

        } else {
            List<AI.Node> nodesToAssign = nodes.stream()
                    .filter(a -> a.getDepth().equals(fillFromDepth))
                    .filter(a -> a.getParentId().equals(node.getId()))
                    .collect(Collectors.toList());

            List<TreeUnit> roots = new ArrayList<>();
            roots.add(this);
            List<TreeUnit> treesToAssign =  getTreesOfLevel(roots, 1);

            for (TreeUnit iterTree : treesToAssign) {
                for (AI.Node iterNode : nodesToAssign ){
                    if (iterTree.node.getId().equals(iterNode.getId())){
                        iterTree.setNode(iterNode);
                    }
                }
            }
        }
    }

    public List<TreeUnit> getTreesOfLevel(List<TreeUnit> roots, Integer level) {
        if (level.equals(0)){
            return roots;
        } else {
            List<TreeUnit> leaves = new ArrayList<>();
            for (TreeUnit leaf : roots) {
                leaves.addAll(leaf.children);
            }
            return getTreesOfLevel(leaves, level-1);
        }
    }

    public TreeUnit(AI.Node node, Integer depth, List<TreeUnit> branches, Double eval, TreeUnit parent) {
        this.node = node;
        this.depth = depth;
        this.children = branches;
        this.eval = eval;
        this.parent = parent;
    }
    public TreeUnit getParent() {
        return parent;
    }

    public void setParent(TreeUnit parent) {
        this.parent = parent;
    }

    public AI.Node getNode() {
        return node;
    }

    public void setNode(AI.Node node) {
        this.node = node;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public List<TreeUnit> getChildren() {
        return children;
    }

    public void setChildren(List<TreeUnit> children) {
        this.children = children;
    }

    public Double getEval() {
        return eval;
    }

    public void setEval(Double eval) {
        this.eval = eval;
    }

    public TreeUnit() {

    }
}
