package chess

class PawnsChessGame(val playerOne: String, val playerTwo: String) {
    private val grid = arrayOf(
        "        ".toCharArray(),
        "BBBBBBBB".toCharArray(),
        "        ".toCharArray(),
        "        ".toCharArray(),
        "        ".toCharArray(),
        "        ".toCharArray(),
        "WWWWWWWW".toCharArray(),
        "        ".toCharArray()
    )
    var currentPlayer: String = playerOne

    fun showBoard() {
        for (i in 8 downTo 1) {
            print("  ")
            println("+++++++++".toCharArray().joinToString("---"))
            print("$i ")
            for (j in 0..7) {
                print("| ${grid[8 - i][j]} ")
            }
            println("|")
        }
        print("  ")
        println("+++++++++".toCharArray().joinToString("---"))
        print("    ")
        println("abcdefgh".toCharArray().joinToString("   "))

    }

    fun move(move: String) {
        val regex = "[a-h][1-8][a-h][1-8]".toRegex()
        if (!move.matches(regex)) {
            println("Invalid Input")
            return
        }
        currentPlayer = if (currentPlayer == playerOne) playerTwo else playerOne
    }
}