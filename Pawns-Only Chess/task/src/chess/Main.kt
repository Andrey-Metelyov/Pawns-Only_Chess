package chess

fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    val playerOne = readLine()!!
    println("Second Player's name:")
    val playerTwo = readLine()!!
    val game = PawnsChessGame(playerOne, playerTwo)
    game.showBoard()
    while (true) {
        println("${game.currentPlayer}'s turn:")
        val input = readLine()!!
        if (input == "exit") {
            break
        }
        val res = game.move(input)
        if (res != PawnsChessGame.MoveResult.INVALID) {
            game.showBoard()
            when (res) {
                PawnsChessGame.MoveResult.WHITE_WINS -> {
                    println("White Wins!")
                    break;
                }
                PawnsChessGame.MoveResult.BLACK_WINS -> {
                    println("Black Wins!")
                    break;
                }
                PawnsChessGame.MoveResult.STALEMATE -> {
                    println("Stalemate!")
                    break;
                }
            }
        }
    }
    println("Bye!")
}
