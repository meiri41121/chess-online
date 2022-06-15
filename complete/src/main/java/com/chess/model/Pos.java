package com.chess.model;

public class Pos {
    public int posX;
    public int posY;
    Pos(){posX=0;posY=0;}
    Pos(int x,int y){posX=x;posY=y;}
    void move(int targetX,int targetY) {posX=targetX; posY=targetY;}
    Boolean valid(){return (posX>0 && posX<9 && posY>0 && posY<9)? true:false;}
    Boolean equal(Pos p){return posX==p.posX && posY==p.posY;}
}
