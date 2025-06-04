package com.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.chess.model.chessClasses.Game;
import com.chess.model.chessClasses.Piece;
import com.chess.model.chessClasses.Pos;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessService {
    
    @Autowired
    private DataService ds;

    // void addBoardAttribute(Model model, Game game)
	// {
	// 	for(int j=0;j<8;j++)
	// 		for(int i=0;i<8;i++)
	// 			model.addAttribute(((char)('A'+i))+""+(char)(j+'1'), game.printPieceByPosition(i+1,j+1,game.getB().getBoard()[j][i]));
	// }
    void addBoardAttribute(Model model, Game game)
	{
		for(int j=0;j<8;j++)
			for(int i=0;i<8;i++)
				model.addAttribute(((char)('A'+i))+""+(char)(j+'1'), game.printPieceByPosition(i+1,j+1,game.getB().getBoard()[j][i]));
	}

    public void StartGame(Model model, String name, int difficulty){
        Game game = new Game(difficulty);
        String gameId = ds.addNewGame(name, difficulty, game);
		model.addAttribute("turn", game.getTurn());
		model.addAttribute("gameId", gameId);
		addBoardAttribute(model, game);
    }

    public String move(Model model, String gameId, String move) {
        Game game = ds.loadGame(gameId);
        String s = game.play(move);
        if("undo".equals(s)){
            ds.updateUndo(gameId,game.getMoves().size());
        }
        model.addAttribute("gameId", gameId);
		if("true".equals(s) && game.getMoves().get(0).getChange()!='P'){
            ds.addMove(game.getMoves().get(0), gameId); 
            return "change";
        }
		if("true".equals(s)){//the player play valid move
            if(game.getMoves().get(0).getHazraha()) ds.addMove(game.getMoves().get(1), gameId);
            ds.addMove(game.getMoves().get(0), gameId); 
            if(game.checkMat()) return end(model,gameId,game);
            model.addAttribute("pcMove", game.doBestMove());
            if(game.getMoves().get(0).getHazraha()) ds.addMove(game.getMoves().get(1), gameId);
            ds.addMove(game.getMoves().get(0), gameId); 
            if(game.checkMat()) return end(model,gameId,game);
        }
		model.addAttribute("turn", game.getTurn());
		if(!"true".equals(s) && !"false".equals(s) && !"undo".equals(s)) 
			model.addAttribute("answer", s);
		addBoardAttribute(model, game);
		return "chess";
    }

    public String move1(Model model, String gameId, String move) {
        Game game = ds.loadGame(gameId);
        String s = game.play2(move);
        if("undo".equals(s)){
            ds.updateUndo(gameId,game.getMoves().size());
        }
        model.addAttribute("gameId", gameId);
		if("true".equals(s) && game.getMoves().get(0).getChange()!='P'){
            ds.addMove(game.getMoves().get(0), gameId); 
            return "change";
        }
		if("true".equals(s)){//the player play valid move
            if(game.getMoves().get(0).getHazraha()) ds.addMove(game.getMoves().get(1), gameId);
            ds.addMove(game.getMoves().get(0), gameId); 
            if(game.checkMat()) return end(model,gameId,game);
            model.addAttribute("pcMove", game.doBestMove());
            if(game.getMoves().get(0).getHazraha()) ds.addMove(game.getMoves().get(1), gameId);
            ds.addMove(game.getMoves().get(0), gameId); 
            if(game.checkMat()) return end(model,gameId,game);
        }
		model.addAttribute("turn", game.getTurn());
		if(!"true".equals(s) && !"false".equals(s) && !"undo".equals(s)) 
			model.addAttribute("answer", s);
		addBoardAttribute(model, game);
		return "chess3";
    }

    String end(Model model, String gameId, Game game){
        Boolean win = ds.checkMat(gameId, !game.getCol());
        model.addAttribute("winMassage", win?"congratulation!! you are the winner!": "you loose");
        return "endGame";
    }

    public String newPawn(Model model, String gameId, String newPawn) {
        Game game = ds.loadGame(gameId);
        if(game.getMoves().get(0).getChange()=='P')
            return null;
        model.addAttribute("gameId", gameId);
        if("Q".equals(newPawn) || "R".equals(newPawn) || "K".equals(newPawn) || "B".equals(newPawn)){
            game.change(newPawn);
            ds.deleteLastMove(gameId);
            ds.addMove(game.getMoves().get(0), gameId);
            if(game.checkMat()) return end(model,gameId,game);
            model.addAttribute("pcMove", game.doBestMove());
            if(game.getMoves().get(0).getHazraha()) ds.addMove(game.getMoves().get(1), gameId);
            ds.addMove(game.getMoves().get(0), gameId); 
            if(game.checkMat()) return end(model,gameId,game);
            model.addAttribute("turn", game.getTurn());
            addBoardAttribute(model, game);
            return "chess";
        }
        return "change";   
    }

    public void change(String gameId, String newPawn) {
        Game game = ds.loadGame(gameId);
        game.change(newPawn);
    }

    public List<String> getValidMovesForPiece(String gameId, String pos) {
        Game game = ds.loadGame(gameId);
        List<String> validMoves = new ArrayList<>();
        game.getValidMovesForPosition(pos.charAt(0) - 'A' + 1, Character.getNumericValue(pos.charAt(1)), validMoves);
        return validMoves;
    }
}
