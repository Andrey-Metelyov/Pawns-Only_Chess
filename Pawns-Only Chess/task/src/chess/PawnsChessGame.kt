package chess

class PawnsChessGame(private val playerOne: String, private val playerTwo: String) {
    enum class BoardCell(val symbol: Char) {
        EMPTY(' '),
        WHITE('W'),
        BLACK('B')
    }

    enum class MoveResult {
        VALID,
        INVALID,
        WHITE_WINS,
        BLACK_WINS,
        STALEMATE
    }

    enum class PawnType {
        WHITE,
        BLACK
    }

    data class Position(val colLetter: Char, val rowDigit: Char) {
        constructor(cell: String) : this(cell[0], cell[1])
        constructor(row: Int, col: Int) : this("abcdefgh"[col], "87654321"[row])

        fun getRow() = "87654321".indexOf(rowDigit)
        fun getCol() = "abcdefgh".indexOf(colLetter)

        override fun toString(): String {
            return colLetter.toString() + rowDigit
        }
    }

    class Pawn(val color: PawnType, var position: Position) {
        fun move(toPosition: Position) {
            position = toPosition
        }
    }

    class Move(val posFrom: Position, val posTo: Position)

    object PawnChessBoard {
//        private val grid = arrayOf(
//            "        ".toCharArray(),
//            "BBBBBBBB".toCharArray(),
//            "        ".toCharArray(),
//            "        ".toCharArray(),
//            "        ".toCharArray(),
//            "        ".toCharArray(),
//            "WWWWWWWW".toCharArray(),
//            "        ".toCharArray()
//        )

        private val pawns = mutableListOf(
            Pawn(PawnType.WHITE, Position('a', '2')),
            Pawn(PawnType.WHITE, Position('b', '2')),
            Pawn(PawnType.WHITE, Position('c', '2')),
            Pawn(PawnType.WHITE, Position('d', '2')),
            Pawn(PawnType.WHITE, Position('e', '2')),
            Pawn(PawnType.WHITE, Position('f', '2')),
            Pawn(PawnType.WHITE, Position('g', '2')),
            Pawn(PawnType.WHITE, Position('h', '2')),
            Pawn(PawnType.BLACK, Position('a', '7')),
            Pawn(PawnType.BLACK, Position('b', '7')),
            Pawn(PawnType.BLACK, Position('c', '7')),
            Pawn(PawnType.BLACK, Position('d', '7')),
            Pawn(PawnType.BLACK, Position('e', '7')),
            Pawn(PawnType.BLACK, Position('f', '7')),
            Pawn(PawnType.BLACK, Position('g', '7')),
            Pawn(PawnType.BLACK, Position('h', '7'))
        )

        private var lastMove: Move? = null

        fun getCell(cell: String): BoardCell {
            val pos = Position(cell)
            return getCell(pos)
        }

        fun getCell(position: Position): BoardCell {
            val pawn = pawns.find { it.position == position }
            if (pawn == null) {
                return BoardCell.EMPTY
            }
            return when (pawn.color) {
                PawnType.BLACK -> BoardCell.BLACK
                PawnType.WHITE -> BoardCell.WHITE
            }
        }

        fun getPawn(pos: Position): Pawn? {
//            val pos = Position(cell)
            return pawns.find { it.position == pos }
        }

        fun showBoard() {
            val grid = Array<CharArray>(8) { "        ".toCharArray() }
            for (pawn in pawns) {
                val x = pawn.position.getRow()
                val y = pawn.position.getCol()
                grid[x][y] = if (pawn.color == PawnType.BLACK) 'B' else 'W'
            }
//            for (wp in whitePawns) {
//                val x = wp.position.getRow()
//                val y = wp.position.getCol()
//                grid[x][y] = 'W'
//            }
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

        fun showBoardDebug() {
//            for (line in grid) {
//                for (el in line) {
//                    System.err.print(el)
//                }
//                System.err.println()
//            }
        }

        fun tryToMakeMove(isWhiteMove: Boolean, move: Move): MoveResult {
            val posFrom = move.posFrom
            val posTo = move.posTo
            val takingPassant = isTakingPassantMove(posTo)
            System.err.println("takingPassant = $takingPassant")
            val pawn = getPawn(posFrom)
            if (pawn == null || (pawn.color == PawnType.WHITE) != isWhiteMove) {
                println(if (isWhiteMove) "No white pawn at $posFrom" else "No black pawn at $posFrom")
                return MoveResult.INVALID
            }

            if (isWhiteMove) {
                if (getCell(posTo) == BoardCell.BLACK || takingPassant) {
                    if (posFrom.getRow() - posTo.getRow() != 1) {
                        System.err.println("1")
                        println("Invalid Input")
                        return MoveResult.INVALID
                    }
                    if (kotlin.math.abs(posFrom.getCol() - posTo.getCol()) != 1) {
                        System.err.println("2")
                        println("Invalid Input")
                        return MoveResult.INVALID
                    }
                    // ok, take the enemy
                    val blackPawn = if (!takingPassant) getPawn(posTo) else getPawn(lastMove!!.posTo)
                    pawns.remove(blackPawn)
                    pawn.move(posTo)
                    lastMove = move

                    return MoveResult.VALID
                }
                if (posFrom.colLetter != posTo.colLetter) {
                    System.err.println("3")
                    println("Invalid Input")
                    return MoveResult.INVALID
                }
                if (posFrom.rowDigit == '2') {
                    if (posTo.rowDigit != '3' && posTo.rowDigit != '4') {
                        System.err.println("4")
                        println("Invalid Input")
                        return MoveResult.INVALID
                    }
                    if (posTo.rowDigit == '4' && getCell(Position(posFrom.colLetter, '3')) != BoardCell.EMPTY) {
                        System.err.println("5")
                        println("Invalid Input")
                        return MoveResult.INVALID
                    }
                } else if (posFrom.getRow() - posTo.getRow() != 1) {
                    System.err.println("6")
                    println("Invalid Input")
                    return MoveResult.INVALID
                }
                pawn.move(posTo)
                lastMove = move
                return MoveResult.VALID
            } else {
                if (getCell(posTo) == BoardCell.WHITE || takingPassant) {
                    if (posTo.getRow() - posFrom.getRow() != 1) {
                        System.err.println("11")
                        println("Invalid Input")
                        return MoveResult.INVALID
                    }
                    if (kotlin.math.abs(posFrom.getCol() - posTo.getCol()) != 1) {
                        System.err.println("12")
                        println("Invalid Input")
                        return MoveResult.INVALID
                    }
                    // ok, take the enemy
                    val whitePawn = if (!takingPassant) getPawn(posTo) else getPawn(lastMove!!.posTo)
                    pawns.remove(whitePawn)
                    pawn.move(posTo)
                    lastMove = move

                    return MoveResult.VALID
                }
                if (posFrom.colLetter != posTo.colLetter) {
                    System.err.println("7")
                    println("Invalid Input")
                    return MoveResult.INVALID
                }
                if (posFrom.rowDigit == '7') {
                    if (posTo.rowDigit != '6' && posTo.rowDigit != '5') {
                        System.err.println("8")
                        println("Invalid Input")
                        return MoveResult.INVALID
                    }
                    if (posTo.rowDigit == '5' && getCell(Position(posFrom.colLetter, '6')) != BoardCell.EMPTY) {
                        System.err.println("9")
                        println("Invalid Input")
                        return MoveResult.INVALID
                    }
                } else if (posTo.getRow() - posFrom.getRow() != 1) {
                    System.err.println("10")
                    println("Invalid Input")
                    return MoveResult.INVALID
                }
                pawn.move(posTo)
                lastMove = move
                return MoveResult.VALID
            }
            return MoveResult.VALID
        }

        private fun isTakingPassantMove(moveTo: Position): Boolean {
            if (lastMove == null) {
                return false
            }
            val lastMoveFrom = lastMove!!.posFrom
            val lastMoveTo = lastMove!!.posTo
            if (kotlin.math.abs(lastMoveTo.getRow() - lastMoveFrom.getRow()) != 2) {
                return false
            }
            if (moveTo.getCol() != lastMoveTo.getCol()) {
                return false
            }
            if (moveTo.getRow() != (lastMoveFrom.getRow() + lastMoveTo.getRow()) / 2) {
                return false
            }
            return true
        }

        fun checkWinConditions(isWhiteMove: Boolean): MoveResult {
            if (pawns.filter { it.color == PawnType.WHITE }.isEmpty()) {
                return MoveResult.BLACK_WINS
            }
            if (pawns.filter { it.color == PawnType.BLACK }.isEmpty()) {
                return MoveResult.WHITE_WINS
            }
            for (pawn in pawns) {
                if (pawn.color == PawnType.WHITE && pawn.position.rowDigit == '8') {
                    return MoveResult.WHITE_WINS
                }
                if (pawn.color == PawnType.BLACK && pawn.position.rowDigit == '1') {
                    return MoveResult.BLACK_WINS
                }
            }
            val pawnsToCheck = if (isWhiteMove) pawns.filter { it.color == PawnType.WHITE } else pawns.filter { it.color == PawnType.BLACK }
            for (pawnToCheck in pawnsToCheck) {
                if (haveValidMove(pawnToCheck)) {
                    System.err.println("${pawnToCheck.position} has valid moves")
                    return MoveResult.VALID
                }
            }
            return MoveResult.STALEMATE
        }

        private fun haveValidMove(pawn: Pawn): Boolean {
            val pos = pawn.position
            val oppositeColor = if (pawn.color == PawnType.WHITE) BoardCell.BLACK else BoardCell.WHITE
            val moveDir = if (pawn.color == PawnType.WHITE) -1 else 1
            System.err.println("haveValidMove checks: ")
            System.err.println("for ${Position(pos.getRow() + moveDir, pos.getCol())}: " + getCell(Position(pos.getRow() + moveDir, pos.getCol())))
            if (pos.getCol() < 7) System.err.println(getCell(Position(pos.getRow() + moveDir, pos.getCol() + 1)))
            if (pos.getCol() > 0) System.err.println(getCell(Position(pos.getRow() + moveDir, pos.getCol() - 1)))
            if (pos.getCol() < 7) System.err.println(isTakingPassantMove(Position(pos.getRow() + moveDir, pos.getCol() + 1)))
            if (pos.getCol() > 0) System.err.println(isTakingPassantMove(Position(pos.getRow() + moveDir, pos.getCol() - 1)))
            if (getCell(Position(pos.getRow() + moveDir, pos.getCol())) == BoardCell.EMPTY ||
                pos.getCol() < 7 && getCell(Position(pos.getRow() + moveDir, pos.getCol() + 1)) == oppositeColor ||
                pos.getCol() > 0 && getCell(Position(pos.getRow() + moveDir, pos.getCol() - 1)) == oppositeColor ||
                pos.getCol() < 7 && isTakingPassantMove(Position(pos.getRow() + moveDir, pos.getCol() + 1)) ||
                pos.getCol() > 0 && isTakingPassantMove(Position(pos.getRow() + moveDir, pos.getCol() - 1))) {
                return true
            }
            return false
        }
    }

    var currentPlayer: String = playerOne
//    private var lastMove = ""

    fun move(move: String): MoveResult {
        PawnChessBoard.showBoardDebug()
        val moveFrom = move.substring(0..1)
        val moveTo = move.substring(2)

        val regex = "[a-h][1-8][a-h][1-8]".toRegex()
        if (!move.matches(regex)) {
            println("Invalid Input")
            return MoveResult.INVALID
        }
//        val (x1, y1, x2, y2) = getGridCoordinates(move)
//        System.err.println("$move: $x1,$y1 -> $x2,$y2")

        val thisMove = Move(Position(moveFrom), Position(moveTo))
        val res = PawnChessBoard.tryToMakeMove(currentPlayer == playerOne, thisMove)
        System.err.println(res)
        if (res == MoveResult.VALID) {
            currentPlayer = if (currentPlayer == playerOne) playerTwo else playerOne
            return PawnChessBoard.checkWinConditions(currentPlayer == playerOne)
        }
        return res
//        if (currentPlayer == playerOne) {
//            if (PawnChessBoard.getCell(moveFrom) != BoardCell.WHITE) {
//                println("No white pawn at $moveFrom")
//                return MoveResult.INVALID
//            }
//            // we have a white pawn at moveFrom position.
//            val pawn = PawnChessBoard.getPawn(moveFrom)
//
//
//            if (PawnChessBoard.getCell(moveTo) == BoardCell.BLACK || isPassantMove(lastMove) && isTakingPassantPawn(
//                    move,
//                    lastMove
//                )
//            ) {
//                System.err.println("try to capture figure at $moveTo ($x1, $y1) -> ($x2, $y2)")
//                if (x1 - x2 != 1 ||
//                    kotlin.math.abs(y2 - y1) != 1
//                ) {
//                    println("Invalid Input")
//                    return MoveResult.INVALID
//                }
//                if (isPassantMove(lastMove) && isTakingPassantPawn(move, lastMove)) {
//                    val (_, _, x2, y2) = getGridCoordinates(lastMove)
//                    grid[x2][y2] = ' '
//                }
//            } else {
//                if (y1 != y2 ||
//                    x1 - x2 > 2 ||
//                    x1 - x2 < 0 ||
//                    x1 != 6 && x2 != x1 - 1 ||
//                    grid[x2][y2] != ' '
//                ) {
//                    println("Invalid Input")
//                    return MoveResult.INVALID
//                }
//            }
//            grid[x2][y2] = 'W'
//            pawn.move(Position(move[2], move[3]))
//        }
//
//        if (currentPlayer == playerTwo) {
//            if (grid[x1][y1] != 'B') {
//                println("No black pawn at ${move[0]}${move[1]}")
//                return MoveResult.INVALID
//            }
//            if (grid[x2][y2] == 'W' || isPassantMove(lastMove) && isTakingPassantPawn(move, lastMove)) {
//                System.err.println("try to capture figure at ${move[2]}${move[3]} ($x1, $y1) -> ($x2, $y2)")
//                if (x2 - x1 != 1 ||
//                    kotlin.math.abs(y2 - y1) != 1
//                ) {
//                    println("Invalid Input")
//                    return MoveResult.INVALID
//                }
//                if (isPassantMove(lastMove) && isTakingPassantPawn(move, lastMove)) {
//                    val (_, _, x2, y2) = getGridCoordinates(lastMove)
//                    grid[x2][y2] = ' '
//                }
//            } else {
//                if (y1 != y2 ||
//                    x2 - x1 > 2 ||
//                    x2 - x1 < 0 ||
//                    x1 != 1 && x2 != x1 + 1 ||
//                    grid[x2][y2] != ' '
//                ) {
//                    println("Invalid Input")
//                    return MoveResult.INVALID
//                }
//            }
//            grid[x2][y2] = 'B'
//        }
//        lastMove = move
//        grid[x1][y1] = ' '
//        currentPlayer = if (currentPlayer == playerOne) playerTwo else playerOne
//        if (isEnded()) {
//            return MoveResult.GAME_END
//        }
//        return MoveResult.VALID
    }

//    private fun isEnded(): Boolean {
//        return lastMove[2] == '8' || lastMove[2] == '1' || !currentPlayerHaveMoves()
//    }

    private fun currentPlayerHaveMoves(): Boolean {
        if (currentPlayer == playerOne) {
//            val whites =
        }
        return false
    }

    private fun isTakingPassantPawn(move: String, lastMove: String): Boolean {
        val (_, _, mx2, my2) = getGridCoordinates(move)
        val (lmx1, _, lmx2, lmy2) = getGridCoordinates(lastMove)
        return lmy2 == my2 && mx2 == (lmx2 + lmx1) / 2
    }

    private fun getGridCoordinates(move: String): Array<Int> {
        val x = "87654321"
        val y = "abcdefgh"
        return arrayOf(
            x.indexOf(move[1]),
            y.indexOf(move[0]),
            x.indexOf(move[3]),
            y.indexOf(move[2])
        )
    }

    private fun isPassantMove(move: String): Boolean {
        if (move.isEmpty()) return false
        val (x1, y1, x2, y2) = getGridCoordinates(move)
        return (kotlin.math.abs(x2 - x1) == 2 && y2 == y1)
    }

    fun showBoard() {
        PawnChessBoard.showBoard()
    }
}
