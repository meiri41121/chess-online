package com.chess.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chess.model.chessClasses.Game;
import com.chess.model.chessClasses.Move;
import com.chess.model.chessClasses.Pos;
// DataService is in com.chess.service.DataService, so this import is correct.

@ExtendWith(MockitoExtension.class)
public class ChessServiceTests {

    @Mock
    private DataService dataService;

    @Mock
    private Game mockGame;

    @InjectMocks
    private ChessService chessService;

    @Test
    void testGetValidMoves_PawnFromE2_ReturnsE3E4() {
        String gameId = "testGame1";
        String piecePosition = "E2"; // Algebraic position

        // Internal representation: Pos(file, rank) where A=1, B=2,...
        Pos posE2 = new Pos(5, 2);
        Pos posE3 = new Pos(5, 3);
        Pos posE4 = new Pos(5, 4);

        List<Move> gameValidMoves = Arrays.asList(
            new Move(posE2, posE3), // E2->E3
            new Move(posE2, posE4)  // E2->E4
        );

        when(dataService.loadGame(gameId)).thenReturn(mockGame);
        // game.allValidMoves() is void, it populates an internal list.
        // We don't need to mock its behavior unless it throws an exception or has other side effects we need to control.
        // If it's just populating the list that getValidMovesList returns, mocking getValidMovesList is sufficient.
        doNothing().when(mockGame).allValidMoves(); 
        when(mockGame.getValidMovesList()).thenReturn(gameValidMoves);

        List<String> validMoves = chessService.getValidMoves(piecePosition, gameId);

        assertNotNull(validMoves);
        assertEquals(2, validMoves.size());
        assertTrue(validMoves.contains("E3"), "Should contain E3");
        assertTrue(validMoves.contains("E4"), "Should contain E4");

        verify(dataService).loadGame(gameId);
        verify(mockGame).allValidMoves();
        verify(mockGame).getValidMovesList();
    }

    @Test
    void testGetValidMoves_QueenFromD4_MultipleMoves() {
        String gameId = "testGameQueen";
        String piecePosition = "D4"; // Pos(4,4)
        Pos queenPos = new Pos(4, 4);

        List<Move> gameValidMoves = Arrays.asList(
            new Move(queenPos, new Pos(4, 1)), // D4->D1
            new Move(queenPos, new Pos(4, 8)), // D4->D8
            new Move(queenPos, new Pos(1, 4)), // D4->A4
            new Move(queenPos, new Pos(8, 4)), // D4->H4
            new Move(queenPos, new Pos(1, 1)), // D4->A1
            new Move(queenPos, new Pos(7, 7))  // D4->G7 (example diagonal)
        );
        when(dataService.loadGame(gameId)).thenReturn(mockGame);
        doNothing().when(mockGame).allValidMoves();
        when(mockGame.getValidMovesList()).thenReturn(gameValidMoves);

        List<String> validMoves = chessService.getValidMoves(piecePosition, gameId);

        assertNotNull(validMoves);
        assertEquals(6, validMoves.size());
        assertTrue(validMoves.contains("D1"));
        assertTrue(validMoves.contains("D8"));
        assertTrue(validMoves.contains("A4"));
        assertTrue(validMoves.contains("H4"));
        assertTrue(validMoves.contains("A1"));
        assertTrue(validMoves.contains("G7"));
    }

    @Test
    void testGetValidMoves_BlockedPawn_NoMoves() {
        String gameId = "testGameBlocked";
        String piecePosition = "A2"; // Pos(1,2)
        Pos pawnPos = new Pos(1, 2); 

        // Simulate a scenario where the game's valid move list is empty for this piece
        // This could be because the pawn is blocked, or it's not its turn, etc.
        // For this test, we assume game.allValidMoves() runs, and then getValidMovesList()
        // might return moves for other pieces, but not for A2.
        List<Move> gameValidMovesFromGame = Arrays.asList(
             new Move(new Pos(5,7), new Pos(5,6)) // Some other piece's move
        );
        // Or, more directly for this piece, it returns an empty list if A2 is the ONLY piece
        // or if we filter specifically for A2's moves and it has none.
        // The current implementation of chessService.getValidMoves filters the list from game.getValidMovesList().

        when(dataService.loadGame(gameId)).thenReturn(mockGame);
        doNothing().when(mockGame).allValidMoves();
        when(mockGame.getValidMovesList()).thenReturn(gameValidMovesFromGame); // No moves for A2

        List<String> validMoves = chessService.getValidMoves(piecePosition, gameId);

        assertNotNull(validMoves);
        assertTrue(validMoves.isEmpty(), "Expected no valid moves for a blocked pawn or if no moves start from A2");
    }
    
    @Test
    void testGetValidMoves_PieceAtEdge_H8Rook() {
        String gameId = "testGameEdge";
        String piecePosition = "H8"; // Pos(8,8)
        Pos rookPos = new Pos(8, 8);

        List<Move> gameValidMoves = Arrays.asList(
            new Move(rookPos, new Pos(8, 1)), // H8->H1
            new Move(rookPos, new Pos(1, 8))  // H8->A8
        );
        when(dataService.loadGame(gameId)).thenReturn(mockGame);
        doNothing().when(mockGame).allValidMoves();
        when(mockGame.getValidMovesList()).thenReturn(gameValidMoves);

        List<String> validMoves = chessService.getValidMoves(piecePosition, gameId);

        assertNotNull(validMoves);
        assertEquals(2, validMoves.size());
        assertTrue(validMoves.contains("H1"));
        assertTrue(validMoves.contains("A8"));
    }

    @Test
    void testGetValidMoves_PieceNotInGamesValidMoveListSource_ReturnsEmpty() {
        String gameId = "testGameMismatch";
        String piecePosition = "B1"; // Pos(2,1) - Knight
        // Game returns moves, but none start from B1
        List<Move> gameValidMoves = Arrays.asList(
            new Move(new Pos(5, 2), new Pos(5, 3)), // E2->E3
            new Move(new Pos(5, 2), new Pos(5, 4))  // E2->E4
        );

        when(dataService.loadGame(gameId)).thenReturn(mockGame);
        doNothing().when(mockGame).allValidMoves();
        when(mockGame.getValidMovesList()).thenReturn(gameValidMoves);

        List<String> validMoves = chessService.getValidMoves(piecePosition, gameId);

        assertNotNull(validMoves);
        assertTrue(validMoves.isEmpty());
    }
    
    @Test
    void testGetValidMoves_GameReturnsEmptyMoveList_ReturnsEmpty() {
        String gameId = "testGameEmptyList";
        String piecePosition = "E2";

        when(dataService.loadGame(gameId)).thenReturn(mockGame);
        doNothing().when(mockGame).allValidMoves();
        when(mockGame.getValidMovesList()).thenReturn(Collections.emptyList()); // Game itself has no valid moves for current player

        List<String> validMoves = chessService.getValidMoves(piecePosition, gameId);

        assertNotNull(validMoves);
        assertTrue(validMoves.isEmpty());
    }

    @Test
    void testGetValidMoves_InvalidPiecePositionFormat_ReturnsEmpty() {
        String gameId = "testGameInvalidPos";
        // No need to mock game interactions as it should fail fast
        List<String> validMoves = chessService.getValidMoves("E22", gameId); // Invalid format
        assertTrue(validMoves.isEmpty());

        validMoves = chessService.getValidMoves("Z2", gameId); // Invalid file
        assertTrue(validMoves.isEmpty());

        validMoves = chessService.getValidMoves("A9", gameId); // Invalid rank
        assertTrue(validMoves.isEmpty());
        
        validMoves = chessService.getValidMoves(null, gameId); // Null position
        assertTrue(validMoves.isEmpty());
    }
    
    @Test
    void testGetValidMoves_NullGameId_DataServiceReturnsNull_ReturnsEmpty() {
        // This test assumes loadGame can return null, or throws an exception.
        // If it throws, the test needs to change. Given current service code, it returns empty list.
        when(dataService.loadGame(null)).thenReturn(null);
        
        List<String> validMoves = chessService.getValidMoves("E2", null);
        assertTrue(validMoves.isEmpty());
        verify(dataService).loadGame(null); // Ensure loadGame was called
    }
}
