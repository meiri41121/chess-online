package com.chess.model.storageEntity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Game")
public class StorageGame {

    @Id
    private String id;
    public String getId() {
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
