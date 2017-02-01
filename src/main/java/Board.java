package main.java;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import static java.lang.Math.abs;
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
        for (i=1;i<=8;i++) {
            add(new Figure(Figure.FigureType.MINE, p, getRandomFreeStartPosition(p)));
        }
        for (i=1;i<=6;i++) {
            add(new Figure(Figure.FigureType.MINER,p, getRandomFreeStartPosition(p)));
        }
        for (i=1;i<=8;i++) {
            add(new Figure(Figure.FigureType.RAIDER, p, getRandomFreeStartPosition(p)));
        }
        for (i=1;i<=5;i++) {
            add(new Figure(Figure.FigureType.SHOOTER,p, getRandomFreeStartPosition(p)));
        }
        for (i=1;i<=4;i++) {
            add(new Figure(Figure.FigureType.CAPRAL,p, getRandomFreeStartPosition(p)));
        }
        for (i=1;i<=3;i++) {
            add(new Figure(Figure.FigureType.CADET,p, getRandomFreeStartPosition(p)));
        }
        for (i=1;i<=2;i++) {
            add(new Figure(Figure.FigureType.CAPITAN,p, getRandomFreeStartPosition(p)));
        }
        add(new Figure(Figure.FigureType.GENERAL,p, getRandomFreeStartPosition(p)));
        add(new Figure(Figure.FigureType.MARSHAL,p, getRandomFreeStartPosition(p)));
        add(new Figure(Figure.FigureType.SPY, p, getRandomFreeStartPosition(p)));
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

    public void manageClick(Integer x, Integer y) {
        logger.debug("manage click");
        if (clickedOnHumanFigure(x,y)) {
            unselectAllFigures();
            selectFigure(x,y);
        } else if (clickedOnComputerFigure(x,y) & humanFigureSelected()) {
            //System.out.println("attack");
            if(attack(x,y)) {
                checkWin();
//                randomComputerTurn();
                computerTurn();
                checkWin();
            //    System.out.println("Computers plays after attack...");
            }
        } else if (!clickedOnComputerFigure(x,y) & humanFigureSelected()) {
            //System.out.println("move");
            if (move(x,y)) {
                System.out.println("Computers plays after move...");
                checkWin();
//                randomComputerTurn();
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
//        ai.addDeeperNodes(2);
//        ai.printEvaluations(2);
//        ai.addDeeperNodes(3);
//        ai.printEvaluations(3);
//        ai.addDeeperNodes(4);
//        ai.printEvaluations(4);


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
                if (getFigureAtPosition(j,i)==null){
                    s=s+" ";
                } else {
                    s=s + this.getFigureAtPosition(j,i).toString();
                }
            }
            s=s+"\n";
        }
    return s;
    }

    public boolean attack(Integer xDestination,Integer yDestination){
        boolean successfulAttack = false;

        for (Figure figToMove: Board.this) {
            if (figToMove.isSelected()) {
                if (isLegalAttackMove(xDestination, yDestination, figToMove)){
                    if(attackerWin(xDestination, yDestination, figToMove)==null) {
                        //System.out.println("draw!");
                        if (figToMove.isType(Figure.FigureType.RAIDER)
                                && getFigureAtPosition(new Coordinates(xDestination,yDestination)).isType(Figure.FigureType.RAIDER) ){
                            figToMove.setRaiderPositionInFrontOfRaider(getFigureAtPosition(new Coordinates(xDestination,yDestination)));
                        }
                        break;
                    } else if (attackerWin(xDestination, yDestination, figToMove).equals(true)) {
                        removeFigureAt(xDestination, yDestination);
                        //System.out.println("attacker won!");
                        figToMove.setPosition(new Coordinates(xDestination, yDestination));
                        successfulAttack=true;
                        break;
                    } else if(attackerWin(xDestination, yDestination, figToMove).equals(false)) {
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

    public Boolean attackerWin(Integer xDefender, Integer yDefender, Figure attackerFigure)
    {
        Figure defender = getFigureAtPosition(xDefender, yDefender);
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

    public void removeFigureAt(Integer x, Integer y){
        for (Figure fig : Board.this) {
            if (    x.equals(fig.getPosition().getX()) &&
                    y.equals(fig.getPosition().getY()) ) {
                this.removeFigure(fig);
                break;
            }
        }
    }

    public boolean move(Integer x,Integer y){
        Boolean successfulMove = false;
        for (Figure figToMove: Board.this) {
            if (figToMove.isSelected()) {
                if (isLegalMove(new Coordinates(x,y), figToMove)){
                    figToMove.setPosition(new Coordinates(x, y));
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

    // vraci board takovej, jak by vypadal, kdyby figura movingFigure zautocila s vektorem attackMove
    public Board getNewBoardMoveOrAttackCompFig(CoordVector attackMove, Figure referenceFigure){
        Board newBoard = this.stream().map(Figure::deepClone).collect(toCollection(Board::new));
        Figure movingFigure = newBoard.getFigureAtPosition(referenceFigure.getPosition());
        assert(newBoard != this);
         Figure figureAtDestination = newBoard.getFigureAtPosition(movingFigure.vectorMove(attackMove).getPosition());
        // Nejezdec jde na volne pole
        if (figureAtDestination==null && !movingFigure.isType(Figure.FigureType.RAIDER)) {
            movingFigure.setPosition(movingFigure.vectorMove(attackMove).getPosition());
        // Jezdec na volne pole
        } else if (figureAtDestination==null && movingFigure.isType(Figure.FigureType.RAIDER)) {
            if (isFreePathInRaidersLeapAttack(movingFigure,attackMove))
                movingFigure.setPosition(movingFigure.vectorMove(attackMove).getPosition());
        // Bud to neni jezdec, anebo je to jezdec na obsazene pole s volnou cestou pred sebou
        } else if (!(movingFigure.isType(Figure.FigureType.RAIDER)) || isFreePathInRaidersLeapAttack(movingFigure, attackMove))
            {
            //System.out.println(movingFigure);
            //System.out.println(figureAtDestination);
            if (movingFigure.beats(figureAtDestination)==null){
            }
            else if (movingFigure.beats(figureAtDestination))
            {
                newBoard.getFigureAtPosition(movingFigure.getPosition()).setPosition(figureAtDestination.getPosition());
                newBoard.removeFigure(figureAtDestination);
            } else {
                newBoard.removeFigure(movingFigure);
            }
        }

        //System.out.println("movingFigure: " + movingFigure.getType() + " " + movingFigure.getOwner().getType() + " is now at " + movingFigure.getPosition().getX() + "," + movingFigure.getPosition().getY() + " move:" + attackMove.getX() + "," + attackMove.getY());
//        System.out.println("figureAtDestination: " + figureAtDestination.getType() + " " + figureAtDestination.getOwner().getType() + " " + figureAtDestination.getPosition().getX() + "," + figureAtDestination.getPosition().getY());

        return newBoard;

    }

    public boolean isLegalMove(Coordinates destination, Figure fig) {
        boolean foundLegalMove = false;
        for (CoordVector move: fig.allowedMoves) {
            if (fig.vectorMove(move).getPosition().equals(destination)) {
                foundLegalMove = true;
            }
            if (fig.isType(Figure.FigureType.RAIDER)){
                CoordVector vectorDiff= new CoordVector(destination.getX()-fig.getPosition().getX(), destination.getY()-fig.getPosition().getY());
                Integer vectorLen = abs(vectorDiff.getX()) + abs(vectorDiff.getY());
                CoordVector unitVector = new CoordVector(vectorDiff.getX()/vectorLen, vectorDiff.getY()/vectorLen);
                Coordinates inspectedRaiderPath = fig.getPosition();
                if (vectorDiff.getX()*vectorDiff.getY()==0) // pouze pro nediagonalni tahy
                    for (int i=1;i<=vectorLen-1;i++){
                        inspectedRaiderPath = inspectedRaiderPath.moveCoordinates(unitVector);
                        if (!isFreePosition(inspectedRaiderPath)) foundLegalMove=false;
                    }
            }
        }
        return foundLegalMove;
    }

    public boolean isLegalAttackMove(Integer x, Integer y, Figure fig) {
        boolean foundLegalAttackMove = false;

        for (CoordVector attackMove: fig.allowedAttackMoves) {
            if (fig.vectorMove(attackMove).getPosition().equals(new Coordinates(x,y))) {
                foundLegalAttackMove = true;
            }
        }
        return foundLegalAttackMove;
    }

    public boolean isFreePathInRaidersLeapAttack(Figure figRaider, CoordVector leapAttackMove){
        // Testuje pouze, jestli je volna cesta k cili, ne, jestli je mozne zautocit na treba vlastni figuru
        Integer xDir = leapAttackMove.getX()==0 ? 0 : leapAttackMove.getX() / abs(leapAttackMove.getX());
        Integer yDir = leapAttackMove.getY()==0 ? 0 : leapAttackMove.getY() / abs(leapAttackMove.getY());
        Integer xTimes =  abs(leapAttackMove.getX());
        Integer yTimes =  abs(leapAttackMove.getY());
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

    public boolean isLegalAttackOrMoveComp(Player.Type me, CoordVector potentialAttackMove, Figure realFig) {
        // vytvorim si virtualni figuru virtFig, abych nehybal s fig, jelikoy to je reference na skutecnou figuru
        // a behem testu na pohyby by se mi skutecne hybala

        Figure fig = realFig.deepClone();
        if (fig==null || potentialAttackMove ==null) return false;
        Figure anotherFigureAt = getFigureAtPosition(fig.vectorMove(potentialAttackMove).getPosition().getX(), fig.vectorMove(potentialAttackMove).getPosition().getY());

        if (!fig.allowedAttackMoves.contains(potentialAttackMove)) return false;
        if (fig.moveIsOutOfBoard(potentialAttackMove)) return false;
        if (!isFreePathInRaidersLeapAttack(fig,potentialAttackMove)) return false;
        if (anotherFigureAt==null) return true; //kdyz je na cilovem poli prazdno
        if (anotherFigureAt.getOwner().getType().equals(me)) return false;
        return true;
    }


    public void unselectAllFigures(){
        for (Figure fig : Board.this) {
                    fig.setSelected(false);
        }

    }

    public boolean clickedOnHumanFigure(Integer x,Integer y) {
        boolean result = false;
        for (Figure fig : Board.this) {
            if (    x.equals(fig.getPosition().getX()) &&
                    y.equals(fig.getPosition().getY()) &&
                    fig.getOwner().getType().equals(Player.Type.HUMAN)) {
                result = true;
            }
        }
        return result;
    }
    public boolean clickedOnComputerFigure(Integer x,Integer y) {
        boolean result = false;
        for (Figure fig : Board.this) {
            if (    x.equals(fig.getPosition().getX()) &&
                    y.equals(fig.getPosition().getY()) &&
                    fig.getOwner().getType().equals(Player.Type.COMPUTER)) {
                result = true;
            }
        }
        return result;
    }

    public void selectFigure(Integer x, Integer y) {
        for (Figure fig : Board.this) {
            if (    x.equals(fig.getPosition().getX()) &&
                    y.equals(fig.getPosition().getY())) {
                fig.turnSelected();
            }
        }
    }

    public Figure getFigureAtPosition(Integer x, Integer y) {
        Figure result=null;
        for (Figure fig : Board.this) {
            if (    x.equals(fig.getPosition().getX()) &&
                    y.equals(fig.getPosition().getY())) {
                result = fig;
            }
        }
        return result;
    }

    public Figure getFigureAtPosition(Coordinates coord) {
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
        Collections.shuffle(Board.this);
        for (Figure figToMove: Board.this) {
            if (figToMove.getOwner().getType().equals(Player.Type.COMPUTER)){
                Collections.shuffle(figToMove.allowedMoves);
                for (CoordVector move: figToMove.allowedMoves) {
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
