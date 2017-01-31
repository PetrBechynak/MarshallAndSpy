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
    @Override

    public boolean equals(Object o){
        if (!o.getClass().equals(Player.class)) return false;
        Player player = (Player) o;
        return this.getType()==player.type;
    }
}
