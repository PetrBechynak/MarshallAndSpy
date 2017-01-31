/**
 * Created by pbechynak on 8.1.2016.
 */

import main.java.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBoard {

    static Board testBoard;

    @BeforeClass
    public static void testSetup() {
        testBoard = new Board();


    }

    @AfterClass
    public static void testCleanup() {
        // Do your cleanup here like close URL connection , releasing resources etc
    }

    @Test
    public void testIsLegalAttackMoveComp() {
        Figure c1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.COMPUTER), new Coordinates(1,1));
        Figure c2 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.COMPUTER), new Coordinates(1,2));
        Figure h1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(1,0));
        Figure h2 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,1));

        testBoard.add(c1);
        testBoard.add(c2);
        testBoard.add(h2);
        testBoard.add(h1);

        // _  H1
        // H2 C1
        // _  C2

//      C1->C2
        CoordVector move = new CoordVector(0,1);
        assertEquals("Result", false, testBoard.isLegalAttackOrMoveComp(Player.Type.COMPUTER,move, c1));
//      C1->H1
        move = new CoordVector(0,-1);
        assertEquals("Result", true, testBoard.isLegalAttackOrMoveComp(Player.Type.COMPUTER,move, c1));
//      C2->H2
        move = new CoordVector(-1,-1);
        assertEquals("Result", false, testBoard.isLegalAttackOrMoveComp(Player.Type.COMPUTER,move, c2));
//      C2-> XXX
        move = new CoordVector(50,0);
        assertEquals("Result", false, testBoard.isLegalAttackOrMoveComp(Player.Type.COMPUTER,move, c2));
//      C2-> out of board
        move = new CoordVector(-2,0);
        assertEquals("Result", false, testBoard.isLegalAttackOrMoveComp(Player.Type.COMPUTER,move, c2));
//      H1->C1
        move = new CoordVector(0,1);
        assertEquals("Result", true, testBoard.isLegalAttackOrMoveComp(Player.Type.HUMAN,move,h1));
//      H2->C1
        move = new CoordVector(1,0);
        assertEquals("Result", true, testBoard.isLegalAttackOrMoveComp(Player.Type.HUMAN,move,h2));
//      null->C1
        move = new CoordVector(1,0);
        assertEquals("Result", false, testBoard.isLegalAttackOrMoveComp(Player.Type.HUMAN,move,null));

    }

    @Test
    public void testRaidersLeapAttackOk(){
        Figure c1 = new Figure(Figure.FigureType.RAIDER, new Player(Player.Type.COMPUTER), new Coordinates(0,0));
        Figure c2 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.COMPUTER), new Coordinates(2,0));
        Figure h1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,2));
        Figure h2 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(3,0));
        Figure h3 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,3));

        testBoard.add(c1);
        testBoard.add(c2);
        testBoard.add(h1);
        testBoard.add(h2);
        testBoard.add(h3);

        // C1 _ C2  H2
        // _  _  _  _
        // H1 _  _  _
        // H3 _  _  _

        // C1 -> C2, i kdzy je to vlastni figura, testuje se jen volna cesta
        CoordVector move = new CoordVector(2,0);
        assertEquals("Result", true, testBoard.isFreePathInRaidersLeapAttack(c1,move));

        // C1 -> H2, i kdzy je to vlastni figura, testuje se jen volna cesta
        move = new CoordVector(3,0);
        assertEquals("Result", false, testBoard.isFreePathInRaidersLeapAttack(c1,move));

        // C1 -> H1, i kdzy je to vlastni figura, testuje se jen volna cesta
        move = new CoordVector(0,2);
        assertEquals("Result", true, testBoard.isFreePathInRaidersLeapAttack(c1,move));

        // C1 -> C2, i kdzy je to vlastni figura, testuje se jen volna cesta
        move = new CoordVector(0,3);
        assertEquals("Result", false, testBoard.isFreePathInRaidersLeapAttack(c1,move));


    }


}