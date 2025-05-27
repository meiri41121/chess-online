package com.chess;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify; // Corrected: Specific import for verify
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*; // For jsonPath matchers like hasSize and is

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType; // Added for content type checking
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.chess.service.ChessService;

@WebMvcTest(ChessController.class) // Specify the controller to test
public class ChessControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mocks ChessService and injects it into the application context
    private ChessService chessService;

    @Test
    void testGetValidMovesEndpoint_ReturnsMovesAsJson() throws Exception {
        String gameId = "game123";
        String piecePosition = "A2";
        List<String> expectedMoves = Arrays.asList("A3", "A4");

        // Define the behavior of the mocked ChessService
        when(chessService.getValidMoves(piecePosition, gameId)).thenReturn(expectedMoves);

        mockMvc.perform(get("/validMoves")
                .param("piecePosition", piecePosition)
                .param("gameId", gameId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verify content type
                .andExpect(jsonPath("$", hasSize(2))) // Check if the root JSON array has 2 elements
                .andExpect(jsonPath("$[0]", is("A3"))) // Check the first element
                .andExpect(jsonPath("$[1]", is("A4"))); // Check the second element

        // Verify that the service method was called with the correct parameters
        verify(chessService).getValidMoves(piecePosition, gameId);
    }

    @Test
    void testGetValidMovesEndpoint_NoValidMoves_ReturnsEmptyJsonArray() throws Exception {
        String gameId = "game456";
        String piecePosition = "H7"; // A piece that might have no moves
        List<String> expectedMoves = Collections.emptyList();

        when(chessService.getValidMoves(piecePosition, gameId)).thenReturn(expectedMoves);

        mockMvc.perform(get("/validMoves")
                .param("piecePosition", piecePosition)
                .param("gameId", gameId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))); // Expect an empty JSON array

        verify(chessService).getValidMoves(piecePosition, gameId);
    }
    
    @Test
    void testGetValidMovesEndpoint_ServiceReturnsNull_ReturnsEmptyJsonArray() throws Exception {
        // This scenario tests how the controller/Spring handles a null from the service.
        // The @ResponseBody and Spring's message converters usually convert null to an empty body
        // for collections, or handle it gracefully.
        String gameId = "game789";
        String piecePosition = "C3";

        when(chessService.getValidMoves(piecePosition, gameId)).thenReturn(null);

        mockMvc.perform(get("/validMoves")
                .param("piecePosition", piecePosition)
                .param("gameId", gameId))
                .andExpect(status().isOk()) 
                // Depending on Spring configuration, null might become an empty array or just empty content.
                // Testing for an empty array is a common expectation for collection endpoints.
                .andExpect(content().string("[]")); // Or check for empty content if that's the behavior

        verify(chessService).getValidMoves(piecePosition, gameId);
    }

    @Test
    void testGetValidMovesEndpoint_MissingPiecePositionParam_ReturnsBadRequest() throws Exception {
        String gameId = "game101";
        // Missing "piecePosition" parameter
        mockMvc.perform(get("/validMoves")
                .param("gameId", gameId))
                .andExpect(status().isBadRequest()); 
                // No need to verify chessService call as it shouldn't be reached
    }

    @Test
    void testGetValidMovesEndpoint_MissingGameIdParam_ReturnsBadRequest() throws Exception {
        String piecePosition = "D5";
        // Missing "gameId" parameter
        mockMvc.perform(get("/validMoves")
                .param("piecePosition", piecePosition))
                .andExpect(status().isBadRequest());
    }
}
