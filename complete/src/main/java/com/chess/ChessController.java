package com.chess;

import com.chess.service.ChessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChessController {
	
	@Autowired
	private ChessService cs;

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String newGame(Model model) {
		return "newGame";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/newGame")
	public String newGamePost(@RequestParam(name="name") String name, @RequestParam(name="difficulty") int difficulty, Model model) {
		cs.StartGame(model, name, difficulty);
		return "chess3";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/chess")
	public String chessMove(@RequestParam(name="move") String move, @RequestParam(name="gameId") String gameId, Model model) {
		System.out.println(move);
		return cs.move1(model, gameId, move);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/validMoves")
	@ResponseBody
	public List<String> getValidMoves(@RequestParam(name="pos") String pos, @RequestParam(name="gameId") String gameId) {
		return cs.getValidMovesForPiece(gameId, pos);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/newPawn")
	public String pawnChange(@RequestParam(name="newPawn") String newPawn, @RequestParam(name="gameId") String gameId, Model model) {
		System.out.println(newPawn);
		cs.change(gameId, newPawn);
		return cs.newPawn(model, gameId, newPawn);
	}

	// @PostMapping("/undo")
    // public String undoMove(Model model) {
	// 	System.out.println("undo");
	// 	return cs.move1(model, gameId, move);
    //     boolean success = chessService.undoLastMove(); // Call the service method to undo the move

    //     if (!success) {
    //         return "Error"; // Handle the case where undo fails (e.g., no moves to undo)
    //     }

    //     // Add attributes to the model to update the view
    //     model.addAttribute("A1", chessService.getPieceAt("A1"));
    //     model.addAttribute("B1", chessService.getPieceAt("B1"));
    //     // Continue for all other squares...

    //     // Optionally return a view name or redirect
    //     return "chessBoard"; // This should match the name of your Thymeleaf template
    // }

}

