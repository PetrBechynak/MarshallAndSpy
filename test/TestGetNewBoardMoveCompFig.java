import main.java.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by petr on 26.1.17.
 */
public class TestGetNewBoardMoveCompFig {
    static Board testBoard;

    @BeforeClass
    public static void testSetup() {
        testBoard = new Board();


    }
    @Test
    public void testGetNewBoardMoveCompFig(){
        testBoard = new Board();
        //    CoordVector attackMove, Figure parentMovingFigure
        Figure c1 = new Figure(Figure.FigureType.RAIDER, new Player(Player.Type.COMPUTER), new Coordinates(0,0));
        Figure h1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,2));

        testBoard.add(c1);
        testBoard.add(h1);

        // C1
        // _
        // H1

        CoordVector attackMove=new CoordVector(0,2);
        Board newBoard = testBoard.getNewBoardMoveOrAttackCompFig(attackMove,c1);

        assertEquals(1, newBoard.size());
        assertEquals(new Coordinates(0,2),newBoard.get(0).getPosition());
        assertEquals(Figure.FigureType.RAIDER, newBoard.get(0).getType());
    }

    @Test
    public void testGetNewBoardMoveCompFig2(){
        testBoard = new Board();
        //    CoordVector attackMove, Figure parentMovingFigure
        Figure c1 = new Figure(Figure.FigureType.RAIDER, new Player(Player.Type.COMPUTER), new Coordinates(0,0));
        Figure c2 = new Figure(Figure.FigureType.RAIDER, new Player(Player.Type.COMPUTER), new Coordinates(0,1));

        Figure h1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,2));

        testBoard.add(c1);
        testBoard.add(c2);
        testBoard.add(h1);

        // C1
        // C2
        // H1

        CoordVector attackMove=new CoordVector(0,2);
        Board newBoard = testBoard.getNewBoardMoveOrAttackCompFig(attackMove,c1);

        assertEquals(3, newBoard.size());
        assertEquals(new Coordinates(0,0),newBoard.get(0).getPosition());
        assertEquals(Figure.FigureType.RAIDER, newBoard.get(0).getType());
    }

    @Test
    public void testGetNewBoardMoveCompFig3(){
        testBoard = new Board();
        //    CoordVector attackMove, Figure parentMovingFigure
        Figure c1 = new Figure(Figure.FigureType.RAIDER, new Player(Player.Type.COMPUTER), new Coordinates(0,0));
        Figure h2 = new Figure(Figure.FigureType.MARSHAL, new Player(Player.Type.HUMAN), new Coordinates(0,1));

        Figure h1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0,2));

        testBoard.add(c1);
        testBoard.add(h2);
        testBoard.add(h1);

        // C1
        // H2
        // H1

        CoordVector attackMove=new CoordVector(0,2);
        Board newBoard = testBoard.getNewBoardMoveOrAttackCompFig(attackMove,c1);

        assertEquals(3, newBoard.size());
        assertEquals(new Coordinates(0,0),newBoard.get(0).getPosition());
        assertEquals(Figure.FigureType.RAIDER, newBoard.get(0).getType());
    }


}
