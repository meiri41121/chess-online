package com.chess;

import com.chess.service.ChessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChessController {

	@Autowired
	private ChessService cs;

	@GetMapping("/")
	public String newGame(Model model) {
		return "newGame";
	}

	@PostMapping("/newGame")
	public String newGamePost(@RequestParam(name="name") String name, @RequestParam(name="difficulty") int difficulty, Model model) {
		cs.StartGame(model, name, difficulty);
		return "chess";
	}

	@PostMapping("/chess")
	public String chessMove(@RequestParam(name="move") String move, @RequestParam(name="gameId") long gameId, Model model) {
		System.out.println(move);
		return cs.move(model, gameId, move);
	}

	@PostMapping("/newPawn")
	public String pawnChange(@RequestParam(name="newPawn") String newPawn, @RequestParam(name="gameId") long gameId, Model model) {
		System.out.println(newPawn);
		return cs.newPawn(model, gameId, newPawn);
	}

}

