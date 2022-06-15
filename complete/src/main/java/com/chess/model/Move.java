package com.chess.model;

public class Move {
    Pos source;
    Pos target;

    Move(Pos s, Pos t){
        source=new Pos(s.posX,s.posY);target=new Pos(t.posX,t.posY);
    }
    public Pos getSource(){return source;}
    public Pos getTarget(){return target;}
}
