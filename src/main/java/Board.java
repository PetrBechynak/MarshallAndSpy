package main.java;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static java.util.stream.Collectors.toCollection;

/**
 * Created by petr on 9.12.15.
 */
public class Board extends ArrayList<Figure> {
    static Logger logger = Logger.getLogger(DrawingEngine.class);

    public Board() {

    }



    public void generateInitialFigures(Player p) {

        Integer i;
//        for (i=1;i<=8;i++) {
//            add(new Figure(Figure.FigureType.MINE, p, getRandomFreeStartPosition(p)));
//        }
//        for (i=1;i<=6;i++) {
//            add(new Figure(Figure.FigureType.MINER,p, getRandomFreeStartPosition(p)));
//        }
        for (i=1;i<=1;i++) {
            add(new Figure(Figure.FigureType.RAIDER, p, getRandomFreeStartPosition(p)));
        }
        for (i=1;i<=5;i++) {
            add(new Figure(Figure.FigureType.SHOOTER,p, getRandomFreeStartPosition(p)));
        }
//        for (i=1;i<=4;i++) {
//            add(new Figure(Figure.FigureType.CAPRAL,p, getRandomFreeStartPosition(p)));
//        }
//        for (i=1;i<=3;i++) {
//            add(new Figure(Figure.FigureType.CADET,p, getRandomFreeStartPosition(p)));
//        }
//        for (i=1;i<=2;i++) {
//            add(new Figure(Figure.FigureType.CAPITAN,p, getRandomFreeStartPosition(p)));
//        }
//        add(new Figure(Figure.FigureType.GENERAL,p, getRandomFreeStartPosition(p)));
//        add(new Figure(Figure.FigureType.MARSHAL,p, getRandomFreeStartPosition(p)));
//        add(new Figure(Figure.FigureType.SPY, p, getRandomFreeStartPosition(p)));
        add(new Figure(Figure.FigureType.FLAG, p, getRandomFreeStartPosition(p)));

    }

    public Coordinates getRandomFreeStartPosition(Player p){
        ArrayList<Coordinates> freePositions = new ArrayList<Coordinates>();
        Integer fromi, toi, i, j, index;
        boolean potentialFreePosition;

        Random rand = new Random();

        if (p.getType().equals(Player.Type.COMPUTER)){
            fromi=0;
            toi=3;
        } else {
            fromi=8;
            toi=11;
        }

        for (i=fromi;i<=toi;i++)
        {
            for (j=1;j<11;j++)
            {
                potentialFreePosition = true;
                for (Figure figure:Board.this) {
                    if (figure.getPosition().equals(new Coordinates(j,i))) {
                        potentialFreePosition = false;
                    }
                }
                if (potentialFreePosition) {
                    freePositions.add(new Coordinates(j, i));
                }
            }
        }

        index = rand.nextInt(freePositions.size());
        return freePositions.get(index);

    }

    public void manageClick(int x, int y) {
        logger.debug("manage click");
        if (clickedOnHumanFigure(x,y)) {
            unselectAllFigures();
            selectFigure(x, y);
        } else if (clickedOnComputerFigure(x,y) & humanFigureSelected()) {
            //System.out.println("attack");
            if(attack(x, y)) {
                checkWin();
                //randomComputerTurn();
                computerTurn();
                checkWin();
            //    System.out.println("Computers plays after attack...");
            }
        } else if (!clickedOnComputerFigure(x,y) & humanFigureSelected()) {
            //System.out.println("move");
            if (move(x,y)) {
                System.out.println("Computers plays after move...");
                checkWin();
                //randomComputerTurn();
                computerTurn();
                checkWin();
            };
        }


    }

    public void computerTurn(){
        AI ai = new AI(this);
        ai.addDeeperNodes(0);
        ai.printEvaluations(0);
        ai.addDeeperNodes(1);
        ai.printEvaluations(1);
        ai.addDeeperNodes(2);
        ai.printEvaluations(2);
        ai.addDeeperNodes(3);
        ai.printEvaluations(3);
        ai.addDeeperNodes(4);
        ai.printEvaluations(4);


        Board bestMove = ai.getBestMove();
        this.clear();
        this.addAll(bestMove);
    };

    public void checkWin() {
        boolean humanFlagExists=false;
        boolean computerFlagExists=false;
        for (Figure fig: Board.this) {
            if (fig.getOwner().getType().equals(Player.Type.HUMAN) & fig.getType().equals(Figure.FigureType.FLAG)) {
                humanFlagExists = true;
            }
            if (fig.getOwner().getType().equals(Player.Type.COMPUTER) & fig.getType().equals(Figure.FigureType.FLAG)) {
                computerFlagExists = true;
            }
        }
        if (!humanFlagExists) {
            System.out.println("asdasd");
            EndMenu emenu = new EndMenu("YOU LOSE");
        }
        if (!computerFlagExists) {
            EndMenu emenu = new EndMenu("YOU WIN");

        }

    }
    @Override
    public String toString(){
    String s = "";
        Integer i,j;
        for (i=0;i<=11;i++) {
            for (j=0;j<=11;j++) {
                if (getFigureAtGrid(j,i)==null){
                    s=s+" ";
                } else {
                    s=s + this.getFigureAtGrid(j,i).toString();
                }
            }
            s=s+"\n";
        }
    return s;
    }

    public boolean attack(int x,int y){
        Integer newX;
        Integer newY;
        newX = x / DrawingEngine.FIGURE_SIZE;
        newY = y / DrawingEngine.FIGURE_SIZE;
        boolean successfulAttack = false;

        for (Figure figToMove: Board.this) {
            if (figToMove.isSelected()) {
                if (isLegalAttackMove(x, y, figToMove)){
                    if(attackerWin(x, y, figToMove)==null) {
                        //System.out.println("draw!");
                        break;
                    } else if (attackerWin(x, y, figToMove).equals(true)) {
                        removeFigureAt(x, y);
                        //System.out.println("attacker won!");
                        figToMove.setPosition(new Coordinates(newX, newY));
                        successfulAttack=true;
                        break;
                    } else if(attackerWin(x, y, figToMove).equals(false)) {
                        this.removeFigure(figToMove);
                        //System.out.println("defender won!");
                        successfulAttack=true;
                        break;
                    }

                }

            }
        }
        return successfulAttack;
    }

    public Boolean attackerWin(int xDefender, int yDefender, Figure attackerFigure)
    {
        Figure defender = getFigureAtPixel(xDefender, yDefender);
        return attackerFigure.beats(defender);
    }

    public void removeFigure(Figure fig){
        for (Iterator<Figure> it = this.iterator(); it.hasNext(); ) {
            Figure figToRemove = it.next();
            if (figToRemove.equals(fig)) {
                it.remove();
            }
        }

    }

    public void removeFigureAt(int x, int y){
        for (Figure fig : Board.this) {
            if (    x > fig.getPosition().getX()*DrawingEngine.FIGURE_SIZE &
                    x < (fig.getPosition().getX()+1)*DrawingEngine.FIGURE_SIZE &
                    y > fig.getPosition().getY()*DrawingEngine.FIGURE_SIZE &
                    y < (fig.getPosition().getY()+1)*DrawingEngine.FIGURE_SIZE ) {
                this.removeFigure(fig);
                break;
            }
        }
    }

    public boolean move(int x,int y){
        Integer newX;
        Integer newY;
        Boolean successfulMove = false;
        newX = x / DrawingEngine.FIGURE_SIZE;
        newY = y / DrawingEngine.FIGURE_SIZE;
        for (Figure figToMove: Board.this) {
            if (figToMove.isSelected()) {
                if (isLegalMove(x, y, figToMove)){
                    figToMove.setPosition(new Coordinates(newX, newY));
                    successfulMove = true;
                }

            }
        }
        return successfulMove;
    }

    public Board deepCopy(){
        Board newBoard = new Board();
        for (Figure fig: this) {
            newBoard.add(fig.deepClone());
        }
        return newBoard;
    };

    public Board getNewBoardMoveCompFig(CoordVector attackMove, Figure parentMovingFigure){
        Board newBoard = this.stream().map(d -> d.deepClone()).collect(toCollection(Board::new));

        Figure movingFigure = newBoard.getFigureAtGrid(parentMovingFigure.getPosition());
        Figure figureAtDestination = newBoard.getFigureAtGrid(movingFigure.vectorMove(attackMove).getPosition());
        if (figureAtDestination==null) {
            movingFigure.setPosition(movingFigure.vectorMove(attackMove).getPosition());
        } else {
            //System.out.println(movingFigure);
            //System.out.println(figureAtDestination);
            if (movingFigure.beats(figureAtDestination)==null){

            }
            else if (movingFigure.beats(figureAtDestination))
            {
                newBoard.removeFigure(figureAtDestination);
                movingFigure.setPosition(figureAtDestination.getPosition());
            } else {
                newBoard.removeFigure(movingFigure);
            }
        }

        //System.out.println("movingFigure: " + movingFigure.getType() + " " + movingFigure.getOwner().getType() + " is now at " + movingFigure.getPosition().getX() + "," + movingFigure.getPosition().getY() + " move:" + attackMove.getX() + "," + attackMove.getY());
//        System.out.println("figureAtDestination: " + figureAtDestination.getType() + " " + figureAtDestination.getOwner().getType() + " " + figureAtDestination.getPosition().getX() + "," + figureAtDestination.getPosition().getY());

        return newBoard;

    }

    public boolean isLegalMove(Integer x, Integer y, Figure fig) {
        boolean foundLegalMove = false;
        Integer destinationX = x / DrawingEngine.FIGURE_SIZE;
        Integer destinationY = y / DrawingEngine.FIGURE_SIZE;

        for (CoordVector move: fig.moves) {
            if (fig.vectorMove(move).getPosition().equals(new Coordinates(destinationX, destinationY))) {
                foundLegalMove = true;
            }
        }
        return foundLegalMove;
    }

    public boolean isLegalAttackMove(Integer x, Integer y, Figure fig) {
        boolean foundLegalAttackMove = false;
        Integer destinationX = x / DrawingEngine.FIGURE_SIZE;
        Integer destinationY = y / DrawingEngine.FIGURE_SIZE;

        for (CoordVector attackMove: fig.attackMoves) {
            if (fig.vectorMove(attackMove).getPosition().equals(new Coordinates(destinationX,destinationY))) {
                foundLegalAttackMove = true;
            }
        }
        return foundLegalAttackMove;
    }

    public boolean raidersLeapAttackOk(Figure figRaider,CoordVector leapAttackMove){
        Integer xDir = leapAttackMove.getX()==0 ? 0 : leapAttackMove.getX() / Math.abs(leapAttackMove.getX());
        Integer yDir = leapAttackMove.getY()==0 ? 0 : leapAttackMove.getY() / Math.abs(leapAttackMove.getY());
        Integer xTimes =  Math.abs(leapAttackMove.getX());
        Integer yTimes =  Math.abs(leapAttackMove.getY());
        Integer times = Math.max(xTimes, yTimes);
        Integer i;
        CoordVector hop;
        boolean leapAttackOk=true;

        for (i=1;i<=times-1;i++) {
            hop = new CoordVector(i*xDir,i*yDir);
            if (!isFreePosition(figRaider.vectorMove(hop).getPosition())) {
                leapAttackOk = false;
            }
        }

        return leapAttackOk;
    }

    public boolean isLegalAttackMoveComp(Player.Type me, CoordVector potentialAttackMove, Figure fig) {
        boolean foundLegalAttackMove = false;

        Figure anotherFigureAt = getFigureAtGrid(fig.vectorMove(potentialAttackMove).getPosition().getX(), fig.vectorMove(potentialAttackMove).getPosition().getY());

        for (CoordVector attackMove: fig.attackMoves) {
            if (fig.vectorMove(potentialAttackMove).getPosition().getX()>=0 & fig.vectorMove(potentialAttackMove).getPosition().getY()>=0
                    & fig.vectorMove(potentialAttackMove).getPosition().getX()<=11 & fig.vectorMove(potentialAttackMove).getPosition().getY()<=11
                    & potentialAttackMove.equals(attackMove)
                    & raidersLeapAttackOk(fig,attackMove)) {
                if (anotherFigureAt==null)
                {
                    foundLegalAttackMove = true;
                }
                else if  (anotherFigureAt.getPosition().equals(fig.vectorMove(attackMove).getPosition())
                        & anotherFigureAt.getOwner().getType().equals(me)) {
                    foundLegalAttackMove = false;
                } else {
                    foundLegalAttackMove = true;
                }

            }
        }
        return foundLegalAttackMove;
    }


    public void unselectAllFigures(){
        for (Figure fig : Board.this) {
                    fig.setSelected(false);
        }

    }

    public boolean clickedOnHumanFigure(Integer x,Integer y) {
        boolean result = false;
        for (Figure fig : Board.this) {
            if (    x > fig.getPosition().getX()*DrawingEngine.FIGURE_SIZE &
                    x < (fig.getPosition().getX()+1)*DrawingEngine.FIGURE_SIZE &
                    y > fig.getPosition().getY()*DrawingEngine.FIGURE_SIZE &
                    y < (fig.getPosition().getY()+1)*DrawingEngine.FIGURE_SIZE &
                    fig.getOwner().getType().equals(Player.Type.HUMAN)) {
                result = true;
            }
        }
        return result;
    }
    public boolean clickedOnComputerFigure(Integer x,Integer y) {
        boolean result = false;
        for (Figure fig : Board.this) {
            if (    x > fig.getPosition().getX()*DrawingEngine.FIGURE_SIZE &
                    x < (fig.getPosition().getX()+1)*DrawingEngine.FIGURE_SIZE &
                    y > fig.getPosition().getY()*DrawingEngine.FIGURE_SIZE &
                    y < (fig.getPosition().getY()+1)*DrawingEngine.FIGURE_SIZE &
                    fig.getOwner().getType().equals(Player.Type.COMPUTER)) {
                result = true;
            }
        }
        return result;
    }

    public void selectFigure(Integer x, Integer y) {
        for (Figure fig : Board.this) {
            if (    x > fig.getPosition().getX()*DrawingEngine.FIGURE_SIZE &
                    x < (fig.getPosition().getX()+1)*DrawingEngine.FIGURE_SIZE &
                    y > fig.getPosition().getY()*DrawingEngine.FIGURE_SIZE &
                    y < (fig.getPosition().getY()+1)*DrawingEngine.FIGURE_SIZE) {
                fig.turnSelected();
            }
        }
    }

    public Figure getFigureAtPixel(Integer x, Integer y) {
        Figure result=null;
        for (Figure fig : Board.this) {
            if (    x > fig.getPosition().getX()*DrawingEngine.FIGURE_SIZE &
                    x < (fig.getPosition().getX()+1)*DrawingEngine.FIGURE_SIZE &
                    y > fig.getPosition().getY()*DrawingEngine.FIGURE_SIZE &
                    y < (fig.getPosition().getY()+1)*DrawingEngine.FIGURE_SIZE) {
                result = fig;
            }
        }
        return result;
    }

    public Figure getFigureAtGrid(Integer x, Integer y) {
        Figure result=null;
        for (Figure fig : this) {
            if (    x.equals(fig.getPosition().getX()) &
                    y.equals(fig.getPosition().getY())) {
                result = fig;
            }
        }
        return result;
    }

    public Figure getFigureAtGrid(Coordinates coord) {
        Figure result=null;
        for (Figure fig : this) {
            if (    coord.getX().equals(fig.getPosition().getX()) &
                    coord.getY().equals(fig.getPosition().getY())) {
                result = fig;
            }
        }
        return result;
    }

    public boolean humanFigureSelected() {
        boolean result = false;
        for (Figure fig : Board.this) {
            if (fig.isSelected()) {
                result = true;
            }
        }
        return result;
    }

    public void draw (Graphics g) {
        BufferedImage imgBack = null;
        //logger.debug("Starting drawing");
        try {
            imgBack = ImageIO.read(new File("src/main/resources/background.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(imgBack, 0, 0, DrawingEngine.FIGURE_SIZE * 12, DrawingEngine.FIGURE_SIZE * 12, null);

        //logger.debug("Background drawn");
        for (Figure fig : Board.this) {
            fig.draw(g);
        }
        //logger.debug("All figures drawn");

    }

    public boolean isFreePosition(Coordinates coord) {
        boolean isFree=true;
        for (Figure fig: this) {
            if (fig.getPosition().equals(coord)){
                isFree=false;
            }
        }
        return isFree;
    }

    public void randomComputerTurn(){
        Figure movingFigure=null;
        Coordinates finalDest=null;
        for (Figure figToMove: Board.this) {
            if (figToMove.getOwner().getType().equals(Player.Type.COMPUTER)){
                for (CoordVector move: figToMove.moves ) {
                    Coordinates destination;
                    destination = figToMove.getPosition().moveCoordinates(move);
                    if (destination.isInsideBattlefield() & this.isFreePosition(destination)) {
                        movingFigure = figToMove;
                        finalDest = destination;
                    }
                }
            }
        }
        movingFigure.setPosition(finalDest);

    }
}
