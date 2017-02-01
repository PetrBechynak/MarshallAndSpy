/**
 * Created by pbechynak on 8.1.2016.
 */

import main.java.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTree {


    @Test
    public void testTree() {
        Board testBoard = new Board();
        Figure figComputer = new Figure(Figure.FigureType.MINER, new Player(Player.Type.COMPUTER), new Coordinates(1,1));
        Figure figHuman = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(1,0));

        // _  H1
        // _  C1
        // _  _

        testBoard.add(figComputer);
        testBoard.add(figHuman);
        AI testAI = new AI(testBoard);
        testAI.addDeeperNodes(0);

        TreeUnit tree = new TreeUnit();
        tree.fillTree(testAI.getNodes(),0);
        tree.fillTree(testAI.getNodes(),1);
    }


}