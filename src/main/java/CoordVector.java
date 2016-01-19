package main.java;

/**
 * Created by petr on 13.12.15.
 */
public class CoordVector {
    Integer x=null;
    Integer y=null;
    public CoordVector(Integer x, Integer y){
        this.x=x;
        this.y=y;
    }

    public Integer getX() {
        return x;
    }

    @Override
    public String toString() {
        return "X=" + x + " Y=" + y;
    }

    @Override
    public boolean equals(Object obj){
        CoordVector coordVect = (CoordVector) obj;
        if (this.getX().equals(coordVect.getX()) & this.getY().equals(coordVect.getY())) {
            return true;
        } else {
            return false;
        }
    }

    public CoordVector minus(CoordVector cv) {
        return new CoordVector(this.getX()-cv.getX(),this.getY()-cv.getY());
    }

    public Integer getY() {
        return y;
    }
}
