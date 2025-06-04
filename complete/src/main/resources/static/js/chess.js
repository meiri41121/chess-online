document.addEventListener('DOMContentLoaded', () => {
    const squares = document.querySelectorAll('.square');
    const moveInput = document.getElementById('move');
    let selectedSquare = null;
    let moveStart = '';

    // Add click handlers to squares
    squares.forEach(square => {
        square.addEventListener('click', () => {
            const piece = square.querySelector('.piece');
            const hasPiece = piece && piece.textContent.trim() !== '';
            
            // If no square is selected and clicked square has a piece
            if (!selectedSquare && hasPiece) {
                selectedSquare = square;
                square.style.backgroundColor = 'var(--highlight)';
                moveStart = getSquareNotation(square);
            }
            // If a square is already selected
            else if (selectedSquare) {
                // Reset the previously selected square's color
                selectedSquare.style.removeProperty('background-color');
                
                // If it's a different square, complete the move
                if (selectedSquare !== square) {
                    const moveEnd = getSquareNotation(square);
                    moveInput.value = moveStart + moveEnd;
                    // Optional: Automatically submit the form
                    // document.querySelector('.move-form').submit();
                }
                selectedSquare = null;
            }
        });
    });

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
}); 