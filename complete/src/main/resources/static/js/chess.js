let selectedPiece = null;
let validMoves = [];
let gameId = null;

document.addEventListener('DOMContentLoaded', function() {
    gameId = document.getElementById('gameId').value;
    setupPieceClickHandlers();
});

function setupPieceClickHandlers() {
    const squares = document.querySelectorAll('.square button');
    squares.forEach(button => {
        button.addEventListener('click', handleSquareClick);
    });
}

function handleSquareClick(event) {
    const button = event.currentTarget;
    const square = button.parentElement;
    const position = square.id;

    // If no piece is selected and this square has a piece
    if (!selectedPiece && button.querySelector('img')) {
        selectPiece(square, position);
    }
    // If a piece is selected and this is a valid move
    else if (selectedPiece && validMoves.includes(position)) {
        makeMove(selectedPiece.id, position);
    }
    // If clicking on a different piece of the same color
    else if (selectedPiece && button.querySelector('img')) {
        clearSelection();
        selectPiece(square, position);
    }
    // If clicking on an invalid square
    else {
        clearSelection();
    }
}

function selectPiece(square, position) {
    clearSelection();
    selectedPiece = square;
    square.classList.add('selected');
    
    // Get valid moves from server
    fetch(`/validMoves?pos=${position}&gameId=${gameId}`)
        .then(response => response.json())
        .then(moves => {
            validMoves = moves;
            highlightValidMoves(moves);
        });
}

function highlightValidMoves(moves) {
    moves.forEach(pos => {
        const square = document.getElementById(pos);
        if (square) {
            square.classList.add('valid-move');
        }
    });
}

function clearSelection() {
    if (selectedPiece) {
        selectedPiece.classList.remove('selected');
        selectedPiece = null;
    }
    validMoves.forEach(pos => {
        const square = document.getElementById(pos);
        if (square) {
            square.classList.remove('valid-move');
        }
    });
    validMoves = [];
}

function makeMove(from, to) {
    const moveString = `${from}->${to}`;
    const form = document.createElement('form');
    form.method = 'post';
    form.action = '/chess';

    const moveInput = document.createElement('input');
    moveInput.type = 'hidden';
    moveInput.name = 'move';
    moveInput.value = moveString;

    const gameIdInput = document.createElement('input');
    gameIdInput.type = 'hidden';
    gameIdInput.name = 'gameId';
    gameIdInput.value = gameId;

    form.appendChild(moveInput);
    form.appendChild(gameIdInput);
    document.body.appendChild(form);
    form.submit();
}

// Get chess notation for a square based on its position in the grid
function getSquareNotation(square) {
    const squareIndex = Array.from(squares).indexOf(square);
    const file = String.fromCharCode('a'.charCodeAt(0) + (squareIndex % 8));
    const rank = Math.floor(squareIndex / 8) + 1;
    return file + rank;
}

// Add hover effect
squares.forEach(square => {
    square.addEventListener('mouseenter', () => {
        const piece = square.querySelector('.piece');
        if (piece && piece.textContent.trim() !== '') {
            square.style.cursor = 'pointer';
        }
    });
}); 