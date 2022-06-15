package com.chess.service;

import java.util.List;

import javax.transaction.Transactional;

import com.chess.GameRepository;
import com.chess.MoveRepository;
import com.chess.model.*;
import com.chess.model.StorageMove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    @Autowired
	private GameRepository gr;

	@Autowired
	private MoveRepository mr;

    public long addNewGame(String name, int difficulty, Game game){
        StorageGame sg = new StorageGame(name, difficulty, game.getMoves().size()==0);//if the player is white we get false
        gr.save(sg);
        if(game.getMoves().size()==1)
            addMove(game.getMoves().get(0), sg.getId());
        return sg.getId();
    }

    public Game loadGame(long gameId){
		List<StorageMove> moves = mr.findByGameIdOrderByIndex(gameId);
        StorageGame sg = gr.findById(gameId);
        Game game = new Game(sg.getDifficulty());
        game.recoveryGame();
        for(StorageMove sm:moves)
            game.recoveryMove(sm.getTargetX(),sm.getTargetY(),sm.getSourceX(),sm.getSourceY(),sm.getBecame());
		return game;
	}

    public void addMove(Movemant m, long gameId){            
        StorageMove sm = new StorageMove(m.getSource().posX, m.getSource().posY, m.getTarget().posX, m.getTarget().posY, gameId, mr.countByGameId(gameId), m.getChange());
        mr.save(sm);
    }

    public void deleteLastMove(long gameId){
        int l=mr.countByGameId(gameId)-1;
        mr.deleteByIndexAndGameId(l, gameId);
    }

    public Boolean checkMat(long gameId, Boolean winner){
        StorageGame sg= gr.findById(gameId);
        sg.setEnd(true);
        sg.setWin(sg.getCol()==winner? true:false);
        gr.save(sg);
        return sg.getCol()==winner;
    }

    @Transactional
    public void updateUndo(long gameId,int currentSize) {
        int size = mr.countByGameId(gameId);
        for(int i=0;i<size-currentSize;i++)
            deleteLastMove(gameId);
    }

}
