/**
 * Created by pbechynak on 12.1.2016.
 */
/**
 * Created by pbechynak on 8.1.2016.
 */

import main.java.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAI {

    static Board testBoard;

    @BeforeClass
    public static void testSetup() {
        testBoard = new Board();
        Figure figComputer = new Figure(Figure.FigureType.MINER, new Player(Player.Type.COMPUTER), new Coordinates(1,1));
        Figure figHuman = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(1,0));

        // _  H1
        // _  C1
        // _  _

        testBoard.add(figComputer);
        testBoard.add(figHuman);


    }

    @AfterClass
    public static void testCleanup() {
        // Do your cleanup here like close URL connection , releasing resources etc
    }

    @Test
    public void testIsLegalAttackMoveComp() {
        AI testAI = new AI(testBoard);
        testAI.addDeeperNodes(0);
        System.out.println("ok");
        //assertEquals("Result", true, testBoard.isLegalAttackOrMoveComp(aMove0,testBoard.get(0)));

    }


}