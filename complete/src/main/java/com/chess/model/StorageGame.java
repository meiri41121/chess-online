package com.chess.model;

import javax.persistence.*;

@Entity
@Table(name="GAME")
public class StorageGame {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    public long getId() {
      return id;
    }
    private String name;
    public String getName() {
        return name;
    }
    private int difficulty;
    public int getDifficulty(){
        return difficulty;
    }

    private boolean col;
    public boolean getCol(){
        return col;
    }

    private Boolean end = false;
    public boolean getEnd(){
        return end;
    }
    public void setEnd(Boolean end){this.end=end;}
    
    private Boolean win = false;
    public boolean getWin(){
        return win;
    }
    public void setWin(Boolean win){this.win=win;}

    public StorageGame(String name, int difficulty,boolean col) {
        this.name = name;
        this.difficulty = difficulty;
        this.col = col;
    }
    
    public StorageGame(){}
}
