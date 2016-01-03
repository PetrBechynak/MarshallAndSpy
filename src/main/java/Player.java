package main.java;

/**
 * Created by petr on 9.12.15.
 */
public class Player {
    public enum Type{HUMAN, COMPUTER}
    Type type;

    public Player(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
