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
        game.move(input)
    }
    println("Bye!")
}