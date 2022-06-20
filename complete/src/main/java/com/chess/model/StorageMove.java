package com.chess.model;
import javax.persistence.*;

@Entity
@Table(name = "MOVE")
public class StorageMove {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private long gameId;
    @Column(name="i")
    private int index;
    private int sourceX;
    private int sourceY;
    private int targetX;
    private int targetY;
    private char became;

    public StorageMove(int sourceX, int sourceY, int targetX, int targetY, Long gameId, int index, char became)
    {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.gameId = gameId;
        this.index = index;
        this.became = became;
    }

    public StorageMove(int sourceX, int sourceY, int targetX, int targetY, long gameId, int index)
    {
        this(sourceX,sourceY,targetX,targetY,gameId,index,'P');
    }

    public StorageMove(){}

    public long getId(){ return id; }
    public int getSourceX(){ return sourceX; }
    public int getSourceY(){ return sourceY; }
    public int getTargetX(){ return targetX; }
    public int getTargetY(){ return targetY; }
    public long getGameId(){ return gameId; }
    public int getIndex(){ return index; }
    public char getBecame(){ return became; }

}