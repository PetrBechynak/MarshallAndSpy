package main.java;

/**
 * Created by pbechynak on 13.1.2016.
 */
public class Move {
    Figure figure=null;
    CoordVector move = null;
    Move(Figure fig, CoordVector move){
        this.figure=figure;
        this.move=move;
    }
}
