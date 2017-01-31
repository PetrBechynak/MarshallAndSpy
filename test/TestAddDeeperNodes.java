import main.java.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.java.AI.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by petr on 26.1.17.
 */
public class TestAddDeeperNodes {
    static Board testBoard;

    @Test
    public void testAddDeeperNodes() {
        testBoard = new Board();
        //    CoordVector attackMove, Figure parentMovingFigure
        Figure c1 = new Figure(Figure.FigureType.CADET, new Player(Player.Type.COMPUTER), new Coordinates(0, 0));
        Figure h1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0, 1));

        testBoard.add(c1);
        testBoard.add(h1);

        // C1 __
        // H1 __
        // __ __

        CoordVector attackMove = new CoordVector(0, 1);
        AI ai = new AI(testBoard);

        // Prvni tah - depth=0
        ai.addDeeperNodes(0);

        List<Node> expectedNodes = new ArrayList<>();
        Node expectedNode0 = new Node(testBoard, 1 , UUID.randomUUID());
        expectedNodes.add(expectedNode0);

        Board resultBoard1 = new Board();
        Figure x1 = new Figure(Figure.FigureType.CADET, new Player(Player.Type.COMPUTER), new Coordinates(1, 0));
        Figure y1 = new Figure(Figure.FigureType.MINER, new Player(Player.Type.HUMAN), new Coordinates(0, 1));
        resultBoard1.add(x1);
        resultBoard1.add(y1);
        Node expectedNode1 = new Node(resultBoard1, 1 , UUID.randomUUID());
        expectedNodes.add(expectedNode1);

        Board resultBoard2 = new Board();
        Figure x2 = new Figure(Figure.FigureType.CADET, new Player(Player.Type.COMPUTER), new Coordinates(0, 1));
        resultBoard2.add(x2);
        Node expectedNode2 = new Node(resultBoard2, 1 , UUID.randomUUID());
        expectedNodes.add(expectedNode2);

        assertEquals(3, ai.getNodes().size());
        boolean equalLists = expectedNodes.size() == ai.getNodes().size() && expectedNodes.containsAll(ai.getNodes());
        assertTrue("Vkladani hlubsich nodu, level 0: ", equalLists);
    }



}
