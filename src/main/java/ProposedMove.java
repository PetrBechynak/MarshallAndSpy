package main.java;

/**
 * Created by petr on 25.12.15.
 */
public class ProposedMove {
    Coordinates figPosition;
    CoordVector move;
    Coordinates destination;

    public ProposedMove(Coordinates figPosition, CoordVector move) {
        this.figPosition = figPosition;
        this.move = move;
        this.destination = figPosition.moveCoordinates(move);
    }

    public Coordinates getFigPosition() {
        return figPosition;
    }

    public CoordVector getMove() {
        return move;
    }

    public Coordinates getDestination() {
        return destination;
    }
}
