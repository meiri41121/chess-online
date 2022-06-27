package com.chess;

import com.chess.service.ChessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		return "chess";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/chess")
	public String chessMove(@RequestParam(name="move") String move, @RequestParam(name="gameId") long gameId, Model model) {
		System.out.println(move);
		return cs.move(model, gameId, move);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/newPawn")
	public String pawnChange(@RequestParam(name="newPawn") String newPawn, @RequestParam(name="gameId") long gameId, Model model) {
		System.out.println(newPawn);
		return cs.newPawn(model, gameId, newPawn);
	}

}

