package main.java;

/**
 * Created by petr on 9.12.15.
 */
/**
 * Created by petr on 5.11.15.
 */
public class Coordinates {
    public enum Direction {LEFT, RIGHT, UP, DOWN}

    Integer x;
    Integer y;

    public Coordinates(int InputX, int InputY){
        this.x=InputX;
        this.y=InputY;
    }

    public Coordinates(){

        this.x=0;
        this.y=0;

    }

    public Coordinates set(Coordinates c) {
        return new Coordinates(c.getX(),c.getY());
    }

    public boolean isInsideBattlefield(){
        if (x<0 | x>11 | y<0 | y>11) {
            return false;
        } else {
            return true;
        }
    }

    public Coordinates moveCoordinates(CoordVector coordVect) {
        Coordinates newCoord = new Coordinates();
        newCoord.setXY(getX()+coordVect.getX(),getY()+coordVect.getY());
        return newCoord;
    }

    public String toString() {
        return "X=" + x.toString() + ", Y=" + y.toString();
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o){
        Coordinates coor = (Coordinates) o;
        if (x.equals(coor.x) & (y.equals(coor.y))) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x.hashCode();
        result = prime * result + y.hashCode();
        return result;
    }

    public Coordinates move(Direction dir){
        int newX=0;
        int newY=0;
        if (dir== Direction.UP) {newX=this.x; newY=this.y-1;}
        if (dir== Direction.DOWN) {newX=this.x; newY=this.y+1;}
        if (dir== Direction.LEFT) {newX=this.x-1; newY=this.y;}
        if (dir== Direction.RIGHT) {newX=this.x+1; newY=this.y;}

        return new Coordinates(newX,newY) ;
    }

    public Coordinates up(){return new Coordinates(this.x,this.y-1) ; }
    public Coordinates down(){return new Coordinates(this.x,this.y+1) ; }
    public Coordinates left(){return new Coordinates(this.x-1,this.y) ; }
    public Coordinates right(){return new Coordinates(this.x+1,this.y) ; }

}

;