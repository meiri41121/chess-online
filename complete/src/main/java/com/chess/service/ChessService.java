package com.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.chess.model.chessClasses.Pos; // Added
import com.chess.model.chessClasses.Move; // Added
import java.util.ArrayList; // Added
import java.util.List; // Added
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.chess.model.chessClasses.Game;

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

    public List<String> getValidMoves(String piecePosition, String gameId) {
        Game game = ds.loadGame(gameId);
        if (game == null) {
            // Log error or throw specific exception
            return new ArrayList<>(); 
        }

        // Validate input piecePosition
        if (piecePosition == null || piecePosition.length() != 2) {
            // Log error or throw specific exception for invalid format
            return new ArrayList<>(); 
        }
        char fileChar = Character.toUpperCase(piecePosition.charAt(0));
        char rankChar = piecePosition.charAt(1);

        if (fileChar < 'A' || fileChar > 'H' || rankChar < '1' || rankChar > '8') {
            // Log error or throw specific exception for out-of-bounds position
            return new ArrayList<>(); 
        }

        // Convert algebraic notation (e.g., "E2") to internal Pos (1-indexed)
        // 'A' becomes 1, 'B' becomes 2, ..., 'H' becomes 8
        // '1' becomes 1, '2' becomes 2, ..., '8' becomes 8
        int posX = fileChar - 'A' + 1;
        int posY = Character.getNumericValue(rankChar);
        Pos selectedPos = new Pos(posX, posY);

        // The Game.allValidMoves() method calculates all valid moves for the current player.
        // It considers the current board state, whose turn it is (game.col),
        // piece-specific rules, and ensures moves do not leave the King in check.
        // This method populates an internal list (game.validMovesList).
        game.allValidMoves(); 

        List<String> validTargetSquares = new ArrayList<>();
        // Retrieve the list of Move objects populated by game.allValidMoves()
        List<Move> allMovesForCurrentPlayer = game.getValidMovesList(); 

        if (allMovesForCurrentPlayer == null) {
            // This case should ideally not be reached if game.allValidMoves() initializes the list.
            // Log error or handle as an internal issue.
            return new ArrayList<>();
        }

        for (Move move : allMovesForCurrentPlayer) {
            // Check if the source of the move is the piece we're interested in
            if (move.getSource().equal(selectedPos)) {
                Pos targetPos = move.getTarget();
                // Convert target Pos (1-indexed) back to algebraic notation (e.g., "E4")
                // targetPos.posX: 1-8 -> 'A'-'H'
                // targetPos.posY: 1-8 -> '1'-'8'
                String targetSquare = String.valueOf((char)('A' + targetPos.posX - 1)) + String.valueOf(targetPos.posY);
                validTargetSquares.add(targetSquare);
            }
        }

        return validTargetSquares;
    }
}
