import main.java.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by petr on 26.1.17.
 */
public class TestGetBestMove {
    static Board testBoard;

    @BeforeClass
    public static void testSetup() {
        testBoard = new Board();


    }

    @Ignore
    @Test
    public void testGetBestMove0(){
        testBoard = new Board();
        //    CoordVector attackMove, Figure parentMovingFigure
        Figure c1 = new Figure(Figure.FigureType.MARSHAL, new Player(Player.Type.COMPUTER), new Coordinates(0,0));
        Figure h1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,1));

        testBoard.add(c1);
        testBoard.add(h1);

        // C1 __
        // H1 __
        // __ __

        AI ai = new AI(testBoard);
        ai.addDeeperNodes(0);
        ai.printEvaluations(0);
        Board bestMove = ai.getBestMove();
        testBoard.clear();
        testBoard.addAll(bestMove);

        assertEquals(1, testBoard.size());
        assertEquals(new Coordinates(0,1),testBoard.get(0).getPosition());
        assertEquals(Figure.FigureType.MARSHAL,testBoard.get(0).getType());
        System.out.println();
    }

    @Test
    public void testGetBestMove1(){
        testBoard = new Board();
        //    CoordVector attackMove, Figure parentMovingFigure
        Figure c1 = new Figure(Figure.FigureType.MARSHAL, new Player(Player.Type.COMPUTER), new Coordinates(0,0));
        Figure h1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,1));

        testBoard.add(c1);
        testBoard.add(h1);

        // C1 __
        // __ __
        // H1 __

        AI ai = new AI(testBoard);
        ai.addDeeperNodes(0);
        ai.printEvaluations(0);
        ai.addDeeperNodes(1);
        ai.printEvaluations(1);
        Board bestMove = ai.getBestMove();
        testBoard.clear();
        testBoard.addAll(bestMove);


    }
}
