package com.chess.service;

import java.util.List;

import com.chess.model.chessClasses.Game;
import com.chess.model.chessClasses.Movemant;
import com.chess.model.repository.GameRepository;
import com.chess.model.repository.MoveRepository;
import com.chess.model.storageEntity.StorageGame;
import com.chess.model.storageEntity.StorageMove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataService {

    @Autowired
	private GameRepository gr;
    @Autowired
	private MoveRepository mr;

    public String addNewGame(String name, int difficulty, Game game){
        StorageGame sg = new StorageGame(name, difficulty, game.getMoves().size()==0);
        gr.save(sg);
        if(game.getMoves().size()==1)
            addMove(game.getMoves().get(0), sg.getId());
        return sg.getId();
    }

    public Game loadGame(String gameId){
        List<StorageMove> moves = mr.findByGameIdOrderByIndex(gameId);
        StorageGame sg = gr.findById(gameId).get();
        Game game = new Game(sg.getDifficulty());
        game.recoveryGame();
        for(StorageMove sm:moves)
            game.recoveryMove(sm.getTargetX(),sm.getTargetY(),sm.getSourceX(),sm.getSourceY(),sm.getBecame());
		return game;
	}

    public void addMove(Movemant m, String gameId){            
        StorageMove sm = new StorageMove(m.getSource().posX, m.getSource().posY, m.getTarget().posX, m.getTarget().posY, gameId, mr.countByGameId(gameId), m.getChange());
        mr.save(sm);
    }

    public void deleteLastMove(String gameId){
        int l=mr.countByGameId(gameId)-1;
        mr.deleteByIndexAndGameId(l, gameId);
    }

    public Boolean checkMat(String gameId, Boolean winner){
        StorageGame sg= gr.findById(gameId).get();
        sg.setEnd(true);
        sg.setWin(sg.getCol()==winner? true:false);
        gr.save(sg);
        return sg.getCol()==winner;
    }

    @Transactional
    public void updateUndo(String gameId,int currentSize) {
        int size = mr.countByGameId(gameId);
        for(int i=0;i<size-currentSize;i++)
            deleteLastMove(gameId);
    }
}
