/**
 * Created by pbechynak on 8.1.2016.
 */

import main.java.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestBoard {

    static Board testBoard;

    @BeforeClass
    public static void testSetup() {
        testBoard = new Board();
        Figure figComputer = new Figure(Figure.FigureType.MINER, new Player(Player.Type.COMPUTER), new Coordinates(1,1));
        Figure figComputer2 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.COMPUTER), new Coordinates(1,2));
        Figure figHuman = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(1,0));
        Figure figHuman2 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,1));

        // _  H1
        // H2 C1
        // _  C2

        testBoard.add(figComputer);
        testBoard.add(figComputer2);
        testBoard.add(figHuman);
        testBoard.add(figHuman2);

    }

    @AfterClass
    public static void testCleanup() {
        // Do your cleanup here like close URL connection , releasing resources etc
    }

    @Test
    public void testIsLegalAttackMoveComp() {
        CoordVector aMoveC2 = new CoordVector(0,1);
        CoordVector aMoveH1 = new CoordVector(0,-1);
        CoordVector aMoveH2 = new CoordVector(-1,0);
        CoordVector aMove0 = new CoordVector(1,0);
        System.out.println("ok");
        //assertEquals("Result", false, testBoard.isLegalAttackMoveComp(aMoveC2, testBoard.get(0)));
        //assertEquals("Result", true, testBoard.isLegalAttackMoveComp(aMoveH1, testBoard.get(0)));
        //assertEquals("Result", true, testBoard.isLegalAttackMoveComp(aMoveH2,testBoard.get(0)));
        //assertEquals("Result", true, testBoard.isLegalAttackMoveComp(aMove0,testBoard.get(0)));

    }

    @Test
    public void testGetNewBoardMoveCompFig(){
        CoordVector aMoveH1 = new CoordVector(0,-1);
        testBoard.getNewBoardMoveCompFig(aMoveH1,testBoard.get(0));
    }


}