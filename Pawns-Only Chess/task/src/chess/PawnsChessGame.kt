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

    fun move(move: String): Boolean {
        for (line in grid) {
            for (el in line) {
                System.err.print(el)
            }
            System.err.println()
        }
        val regex = "[a-h][1-8][a-h][1-8]".toRegex()
        if (!move.matches(regex)) {
            println("Invalid Input")
            return false
        }
        val x1 = "87654321".indexOf(move[1])
        val y1 = "abcdefgh".indexOf(move[0])
        val x2 = "87654321".indexOf(move[3])
        val y2 = "abcdefgh".indexOf(move[2])
        System.err.println("$move: $x1,$y1 -> $x2,$y2")

        if (currentPlayer == playerOne) {
            if (grid[x1][y1] != 'W') {
                println("No white pawn at ${move[0]}${move[1]}")
                return false
            }
            if (y1 != y2 ||
                x1 - x2 > 2 ||
                x1 - x2 < 0 ||
                x1 != 6 && x2 != x1 - 1 ||
                grid[x2][y2] != ' ') {
                println("Invalid Input")
                return false
            }
            grid[x2][y2] = 'W'
        }

        if (currentPlayer == playerTwo) {
            if (grid[x1][y1] != 'B') {
                println("No black pawn at ${move[0]}${move[1]}")
                return false
            }
            if (y1 != y2 ||
                x2 - x1 > 2 ||
                x2 - x1 < 0 ||
                x1 != 1 && x2 != x1 + 1 ||
                grid[x2][y2] != ' ') {
                println("Invalid Input")
                return false
            }
            grid[x2][y2] = 'B'
        }
        grid[x1][y1] = ' '
        currentPlayer = if (currentPlayer == playerOne) playerTwo else playerOne
        return true
    }
}