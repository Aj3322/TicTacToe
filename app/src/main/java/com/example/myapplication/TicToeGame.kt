package com.example.myapplication
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


var gameIsOver = false
@Composable
fun TicTacToeGame() {
    var gameState by remember { mutableStateOf(TicTacToeState()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tic Tac Toe",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            color = Color.White,
        )

        TicTacToeBoard(gameState) { updatedState ->
            gameState = updatedState
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                gameState = TicTacToeState()
                gameIsOver=false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Restart Game")
        }
    }
}

@Composable
fun TicTacToeBoard(
    gameState: TicTacToeState,
    onCellClick: (TicTacToeState) -> Unit

) {
    var winner by remember { mutableStateOf<CellState?>(null) }
    Column {
        for (i in 0 until 3) {
            Row {
                for (j in 0 until 3) {
                    val index = i * 3 + j
                    Cell(gameState.board[index], onCellClick, gameState, index, winner)
                }
            }
        }
    }
}


@Composable
fun Cell(
    cellState: CellState,
    onCellClick: (TicTacToeState) -> Unit,
    gameState: TicTacToeState,
    index: Int,
    winner: CellState?
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .clickable {
                if (!gameIsOver) {
                    val updatedState = gameState.copy(
                        currentPlayer = if (gameState.currentPlayer == CellState.X) CellState.O else CellState.X,
                        board = gameState.board
                            .toMutableList()
                            .apply {
                                this[index] =
                                    if (cellState == CellState.EMPTY) gameState.currentPlayer else cellState
                            }
                    )
                    // Check for a winner
                    val winner = updatedState.checkWinner()
                    if (winner != null) {
                        gameIsOver = true
                        // Log the winner
                        Log.d("TicTacToe", "Winner is ${winner.toString()}")
                    }
                    onCellClick.invoke(updatedState)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when (cellState) {
            CellState.EMPTY -> {
                // Empty cell
                Card {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.Gray)
                    )
                }
            }
            CellState.X -> {
                // X
                Text(
                    text = "X",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
            }
            CellState.O -> {
                // O
                Text(
                    text = "O",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }
        }

        // Display the winner if the game is over
        if (gameIsOver && winner != null) {
            Text(text = "Winner is $winner", color = Color.White)
        }
    }
}




sealed class CellState {
    object EMPTY : CellState()
    object X : CellState()
    object O : CellState()
}

data class TicTacToeState(
    val currentPlayer: CellState = CellState.X,
    val board: List<CellState> = List(9) { CellState.EMPTY }

){
    fun checkWinner(): CellState? {
        // Check rows
        for (i in 0 until 3) {
            if (board[i * 3] != CellState.EMPTY &&
                board[i * 3] == board[i * 3 + 1] &&
                board[i * 3] == board[i * 3 + 2]
            ) {
                return board[i * 3]
            }
        }

        // Check columns
        for (i in 0 until 3) {
            if (board[i] != CellState.EMPTY &&
                board[i] == board[i + 3] &&
                board[i] == board[i + 6]
            ) {
                return board[i]
            }
        }

        // Check diagonals
        if (board[0] != CellState.EMPTY &&
            board[0] == board[4] &&
            board[0] == board[8]
        ) {
            return board[0]
        }

        if (board[2] != CellState.EMPTY &&
            board[2] == board[4] &&
            board[2] == board[6]
        ) {
            return board[2]
        }

        // No winner
        return null
    }
}
