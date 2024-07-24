package com.chess.model.storageEntity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Move")
public class StorageMove {

    @Id
    private String id;

    private String gameId;
    @Field("i")
    private int index;
    private int sourceX;
    private int sourceY;
    private int targetX;
    private int targetY;
    private char became;

    public StorageMove(int sourceX, int sourceY, int targetX, int targetY, String gameId, int index, char became)
    {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.gameId = gameId;
        this.index = index;
        this.became = became;
    }

    public StorageMove(int sourceX, int sourceY, int targetX, int targetY, String gameId, int index)
    {
        this(sourceX,sourceY,targetX,targetY,gameId,index,'P');
    }

    public StorageMove(){}

    public String getId(){ return id; }
    public int getSourceX(){ return sourceX; }
    public int getSourceY(){ return sourceY; }
    public int getTargetX(){ return targetX; }
    public int getTargetY(){ return targetY; }
    public String getGameId(){ return gameId; }
    public int getIndex(){ return index; }
    public char getBecame(){ return became; }

}
