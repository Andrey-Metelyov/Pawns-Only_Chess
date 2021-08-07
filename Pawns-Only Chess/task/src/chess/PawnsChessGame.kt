package chess

class PawnsChessGame(private val playerOne: String, private val playerTwo: String) {
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
    private var lastMove = ""

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
        val (x1, y1, x2, y2) = getGridCoordinates(move)
        System.err.println("$move: $x1,$y1 -> $x2,$y2")

        if (currentPlayer == playerOne) {
            if (grid[x1][y1] != 'W') {
                println("No white pawn at ${move[0]}${move[1]}")
                return false
            }
            if (grid[x2][y2] == 'B' || isPassantMove(lastMove) && isTakingPassantPawn(move, lastMove)) {
                System.err.println("try to capture figure at ${move[2]}${move[3]} ($x1, $y1) -> ($x2, $y2)")
                if (x1 - x2 != 1 ||
                    kotlin.math.abs(y2 - y1) != 1
                ) {
                    println("Invalid Input")
                    return false
                }
                if (isPassantMove(lastMove) && isTakingPassantPawn(move, lastMove)) {
                    val (_, _, x2, y2) = getGridCoordinates(lastMove)
                    grid[x2][y2] = ' '
                }
            } else {
                if (y1 != y2 ||
                    x1 - x2 > 2 ||
                    x1 - x2 < 0 ||
                    x1 != 6 && x2 != x1 - 1 ||
                    grid[x2][y2] != ' '
                ) {
                    println("Invalid Input")
                    return false
                }
            }
            grid[x2][y2] = 'W'
        }

        if (currentPlayer == playerTwo) {
            if (grid[x1][y1] != 'B') {
                println("No black pawn at ${move[0]}${move[1]}")
                return false
            }
            if (grid[x2][y2] == 'W' || isPassantMove(lastMove) && isTakingPassantPawn(move, lastMove)) {
                System.err.println("try to capture figure at ${move[2]}${move[3]} ($x1, $y1) -> ($x2, $y2)")
                if (x2 - x1 != 1 ||
                    kotlin.math.abs(y2 - y1) != 1
                ) {
                    println("Invalid Input")
                    return false
                }
                if (isPassantMove(lastMove) && isTakingPassantPawn(move, lastMove)) {
                    val (_, _, x2, y2) = getGridCoordinates(lastMove)
                    grid[x2][y2] = ' '
                }
            } else {
                if (y1 != y2 ||
                    x2 - x1 > 2 ||
                    x2 - x1 < 0 ||
                    x1 != 1 && x2 != x1 + 1 ||
                    grid[x2][y2] != ' '
                ) {
                    println("Invalid Input")
                    return false
                }
            }
            grid[x2][y2] = 'B'
        }
        lastMove = move
        grid[x1][y1] = ' '
        currentPlayer = if (currentPlayer == playerOne) playerTwo else playerOne
        return true
    }

    private fun isTakingPassantPawn(move: String, lastMove: String): Boolean {
        val (_, _, mx2, my2) = getGridCoordinates(move)
        val (lmx1, _, lmx2, lmy2) = getGridCoordinates(lastMove)
        return lmy2 == my2 && mx2 == (lmx2 + lmx1) / 2
    }

    private fun getGridCoordinates(move: String): Array<Int> {
        val x = "87654321"
        val y = "abcdefgh"
        return arrayOf(x.indexOf(move[1]),
            y.indexOf(move[0]),
            x.indexOf(move[3]),
            y.indexOf(move[2]))
    }

    private fun isPassantMove(move: String): Boolean {
        if (move.isEmpty()) return false
        val (x1, y1, x2, y2) = getGridCoordinates(move)
        return (kotlin.math.abs(x2 - x1) == 2 && y2 == y1)
    }
}
