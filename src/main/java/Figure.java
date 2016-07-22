package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by petr on 9.12.15.
 */
public class Figure {
    public enum FigureType{MINE, FLAG, CAPRAL, SHOOTER, MINER, SPY, CADET, MARSHAL, KADET, RIDER,RAIDER,GENERAL,CAPITAN};
    FigureType type;
    Player owner;
    Coordinates position;
    Double value;
    String strRepresentation;
    boolean selected;

    @Override
    public String toString(){
        return strRepresentation;
    }

    public void setAttackMoves(ArrayList<CoordVector> attackMoves) {
        this.attackMoves = attackMoves;
    }

    public void setType(FigureType type) {
        this.type = type;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setMoves(ArrayList<CoordVector> moves) {
        this.moves = moves;
    }

    public Figure deepClone(){
        Figure newFig=new Figure(this.getType(),this.getOwner(),this.getPosition());
        return newFig;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    ArrayList<CoordVector> moves = new ArrayList<CoordVector>();
    ArrayList<CoordVector> attackMoves = new ArrayList<CoordVector>();

    public Figure(FigureType type, Player owner, Coordinates position) {
        Integer i;
        this.type = type;
        this.owner = owner;
        this.position = position;
        this.selected = false;
        if (type.equals(FigureType.FLAG) | type.equals(FigureType.MINE)) {
            moves.clear();
        } else if(type.equals(FigureType.RAIDER)) {
            for (i=-11;i<12;i++) {
                moves.add(new CoordVector(0, i));
                moves.add(new CoordVector(i, 0));
            }
            moves.add(new CoordVector(-1, -1));
            moves.add(new CoordVector(-1, 1));
            moves.add(new CoordVector(1, 1));
            moves.add(new CoordVector(1, -1));
        } else {
            moves.add(new CoordVector(-1, -1));
            moves.add(new CoordVector(-1, 1));
            moves.add(new CoordVector(1, 1));
            moves.add(new CoordVector(1, -1));

            moves.add(new CoordVector(0, -1));
            moves.add(new CoordVector(0, 1));
            moves.add(new CoordVector(-1, 0));
            moves.add(new CoordVector(1, 0));

        }

        if (type.equals(FigureType.FLAG) | type.equals(FigureType.MINE)) {
            attackMoves.clear();
        } else if(type.equals(FigureType.RAIDER)) {
            for (i=-11;i<12;i++) {
                attackMoves.add(new CoordVector(0, i));
                attackMoves.add(new CoordVector(i, 0));
            }
        } else {
            attackMoves.add(new CoordVector(0, -1));
            attackMoves.add(new CoordVector(0, 1));
            attackMoves.add(new CoordVector(-1, 0));
            attackMoves.add(new CoordVector(1, 0));

        }

        if (this.type.equals(FigureType.FLAG)) {
            this.value = 1000.0;
            strRepresentation="F";
        }
        else if (this.type.equals(FigureType.SPY)) {
            this.value = 10.0;
            strRepresentation="S";
        }
        else if (this.type.equals(FigureType.MINE)) {
            this.value = 1.0;
            strRepresentation="B";
        }
        else if (this.type.equals(FigureType.MINER)) {
            this.value = 2.0;
            strRepresentation="M";
        }
        else if (this.type.equals(FigureType.RAIDER)) {
            this.value = 3.0;
            strRepresentation="R";
        }
        else if (this.type.equals(FigureType.SHOOTER)) {
            this.value = 4.0;
            strRepresentation="H";
        }
        else if (this.type.equals(FigureType.CAPRAL)) {
            this.value = 5.0;
            strRepresentation="C";
        }
        else if (this.type.equals(FigureType.CADET)) {
            this.value = 7.0;
            strRepresentation="T";
        }
        else if (this.type.equals(FigureType.CAPITAN)) {
            this.value = 10.0;
            strRepresentation="N";
        }
        else if (this.type.equals(FigureType.GENERAL)) {
            this.value = 15.0;
            strRepresentation="G";
        }
        else if (this.type.equals(FigureType.MARSHAL)) {
            this.value = 20.0;
            strRepresentation="L";
        }

        if (owner.getType().equals(Player.Type.COMPUTER)) {
            this.value = -this.value;
            strRepresentation=strRepresentation.toLowerCase();
        }

    }

    public void turnSelected() {
        this.selected = !this.selected;
    }

    public Figure vectorMove(CoordVector coordvector) {
        return new Figure(this.type,this.getOwner(),this.getPosition().moveCoordinates(coordvector));
    }

    public boolean isSelected() {
        return selected;
    }

    public FigureType getType() {
        return type;
    }

    public Player getOwner() {
        return owner;
    }

    public Coordinates getPosition() {
        return position;
    }

    public Boolean beats(Figure defender) {
        //System.out.println("beats");
        Boolean result = false;
        if (this.type.equals(defender.getType())) {return null;
        } else if(this.type.equals(FigureType.MARSHAL) & defender.getType().equals(FigureType.GENERAL)) {result = true;
        } else if(this.type.equals(FigureType.MARSHAL) & defender.getType().equals(FigureType.CAPITAN)) {result = true;
        } else if(this.type.equals(FigureType.MARSHAL) & defender.getType().equals(FigureType.CADET)) {result = true;
        } else if(this.type.equals(FigureType.MARSHAL) & defender.getType().equals(FigureType.CAPRAL)) {result = true;
        } else if(this.type.equals(FigureType.MARSHAL) & defender.getType().equals(FigureType.SHOOTER)) {result = true;
        } else if(this.type.equals(FigureType.MARSHAL) & defender.getType().equals(FigureType.RAIDER)) {result = true;
        } else if(this.type.equals(FigureType.MARSHAL) & defender.getType().equals(FigureType.MINER)) {result = true;
        } else if(this.type.equals(FigureType.GENERAL) & defender.getType().equals(FigureType.CAPITAN)) {result = true;
        } else if(this.type.equals(FigureType.GENERAL) & defender.getType().equals(FigureType.CADET)) {result = true;
        } else if(this.type.equals(FigureType.GENERAL) & defender.getType().equals(FigureType.CAPRAL)) {result = true;
        } else if(this.type.equals(FigureType.GENERAL) & defender.getType().equals(FigureType.SHOOTER)) {result = true;
        } else if(this.type.equals(FigureType.GENERAL) & defender.getType().equals(FigureType.RAIDER)) {result = true;
        } else if(this.type.equals(FigureType.GENERAL) & defender.getType().equals(FigureType.MINER)) {result = true;
        } else if(this.type.equals(FigureType.GENERAL) & defender.getType().equals(FigureType.SPY)) {result = true;
        } else if(this.type.equals(FigureType.CAPITAN) & defender.getType().equals(FigureType.CADET)) {result = true;
        } else if(this.type.equals(FigureType.CAPITAN) & defender.getType().equals(FigureType.CAPRAL)) {result = true;
        } else if(this.type.equals(FigureType.CAPITAN) & defender.getType().equals(FigureType.SHOOTER)) {result = true;
        } else if(this.type.equals(FigureType.CAPITAN) & defender.getType().equals(FigureType.RAIDER)) {result = true;
        } else if(this.type.equals(FigureType.CAPITAN) & defender.getType().equals(FigureType.MINER)) {result = true;
        } else if(this.type.equals(FigureType.CAPITAN) & defender.getType().equals(FigureType.SPY)) {result = true;
        } else if(this.type.equals(FigureType.CADET) & defender.getType().equals(FigureType.CAPRAL)) {result = true;
        } else if(this.type.equals(FigureType.CADET) & defender.getType().equals(FigureType.SHOOTER)) {result = true;
        } else if(this.type.equals(FigureType.CADET) & defender.getType().equals(FigureType.RAIDER)) {result = true;
        } else if(this.type.equals(FigureType.CADET) & defender.getType().equals(FigureType.MINER)) {result = true;
        } else if(this.type.equals(FigureType.CADET) & defender.getType().equals(FigureType.SPY)) {result = true;
        } else if(this.type.equals(FigureType.CAPRAL) & defender.getType().equals(FigureType.SHOOTER)) {result = true;
        } else if(this.type.equals(FigureType.CAPRAL) & defender.getType().equals(FigureType.RAIDER)) {result = true;
        } else if(this.type.equals(FigureType.CAPRAL) & defender.getType().equals(FigureType.MINER)) {result = true;
        } else if(this.type.equals(FigureType.CAPRAL) & defender.getType().equals(FigureType.SPY)) {result = true;
        } else if(this.type.equals(FigureType.SHOOTER) & defender.getType().equals(FigureType.RAIDER)) {result = true;
        } else if(this.type.equals(FigureType.SHOOTER) & defender.getType().equals(FigureType.MINER)) {result = true;
        } else if(this.type.equals(FigureType.SHOOTER) & defender.getType().equals(FigureType.SPY)) {result = true;
        } else if(this.type.equals(FigureType.RAIDER) & defender.getType().equals(FigureType.MINER)) {result = true;
        } else if(this.type.equals(FigureType.RAIDER) & defender.getType().equals(FigureType.SPY)) {result = true;
        } else if(this.type.equals(FigureType.MINER) & defender.getType().equals(FigureType.MINE)) {result = true;
        } else if(this.type.equals(FigureType.MINER) & defender.getType().equals(FigureType.SPY)) {result = true;
        } else if(this.type.equals(FigureType.SPY) & defender.getType().equals(FigureType.MARSHAL)) {result = true;
        } else if(defender.getType().equals(FigureType.FLAG)) {result = true;
        }
        return result;
    }

    private static ColorModel createColorModel(int n) {
        byte[] r = new byte[16];
        byte[] g = new byte[16];
        byte[] b = new byte[16];

        for (int i = 0; i < r.length; i++) {
            r[i] = (byte) n;
            g[i] = (byte) n;
            b[i] = (byte) n;
        }
        return new IndexColorModel(4, 16, r, g, b);
    }

    public void draw(Graphics g) {
        Integer ax=0,bx=0,ay=0,by=0;
        BufferedImage img = null;
        try {
            if (owner.getType().equals(Player.Type.COMPUTER)) {
                img = ImageIO.read(new File("src/main/resources/imagesAllBlack.bmp"));
            } else {
                img = ImageIO.read(new File("src/main/resources/imagesAll.bmp"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getType()== Figure.FigureType.CAPRAL){
            ax = 93; ay = 186; bx = 186; by = 279;
        } else if(getType()== Figure.FigureType.FLAG) {
            ax = 0; ay = 0; bx = 93; by = 93;
        } else if(getType()== FigureType.MARSHAL) {
            ax = 93; ay = 0; bx = 186; by = 93;
        } else if(getType()== Figure.FigureType.GENERAL) {
            ax = 186; ay = 0; bx = 279; by = 93;
        } else if(getType()== Figure.FigureType.CAPITAN) {
            ax = 93; ay = 93; bx = 186; by = 186;
        }else if(getType()== Figure.FigureType.RAIDER) {
            ax = 186; ay = 186; bx = 279; by = 279;
        }else if(getType()== Figure.FigureType.MINER) {
            ax = 279; ay = 0; bx = 362; by = 93;
        }else if(getType()== Figure.FigureType.SPY) {
            ax = 0; ay = 93; bx = 93; by = 186;
        }else if(getType()== Figure.FigureType.CADET) {
            ax = 186; ay = 93; bx = 279; by = 186;
        }else if(getType()== Figure.FigureType.MINE) {
            ax = 0; ay = 186; bx = 93; by = 279;
        }else if(getType()== Figure.FigureType.SHOOTER) {
            ax = 279; ay = 93; bx = 362; by = 186;
        }

        g.drawImage(img,
                getPosition().getX() * DrawingEngine.FIGURE_SIZE + DrawingEngine.FIGURE_BORDER,
                getPosition().getY() * DrawingEngine.FIGURE_SIZE + DrawingEngine.FIGURE_BORDER,
                getPosition().getX() * DrawingEngine.FIGURE_SIZE + DrawingEngine.FIGURE_SIZE - DrawingEngine.FIGURE_BORDER,
                getPosition().getY() * DrawingEngine.FIGURE_SIZE + DrawingEngine.FIGURE_SIZE - DrawingEngine.FIGURE_BORDER,
                ax, ay, bx, by, null);

        if (selected) {
            g.setColor(Color.red);
            g.drawRect(
                    getPosition().getX() * DrawingEngine.FIGURE_SIZE + DrawingEngine.FIGURE_BORDER,
                    getPosition().getY() * DrawingEngine.FIGURE_SIZE + DrawingEngine.FIGURE_BORDER,
                    DrawingEngine.FIGURE_SIZE - DrawingEngine.FIGURE_BORDER*2,
                    DrawingEngine.FIGURE_SIZE - DrawingEngine.FIGURE_BORDER*2
            );
            //System.out.println(getPosition().getX() + " " + getPosition().getY());
        }
    }

}
