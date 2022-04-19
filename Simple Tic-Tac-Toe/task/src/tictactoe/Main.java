package tictactoe;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;

class OutBoundsMoveException extends Exception {}

class OccupiedCoordinateException extends Exception {}

class Coordinates {
    public final int x;
    public final int y;

    Coordinates(int x, int y) throws OutBoundsMoveException {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw new OutBoundsMoveException();
        }
        this.x = x;
        this.y = y;
    }
}

public class Main {

    public static void main(String[] args) {
        char[][] gameStates = createGameBoard();
        char player = 'X';
        printGameBoard(gameStates);

        do {
            processUserMove(gameStates, player);
            printGameBoard(gameStates);
            player = player == 'X' ? 'O': 'X';
        } while (Objects.equals(checkStatus(gameStates), ""));
        printStatus(checkStatus(gameStates));
    }

    public static String checkStatus(char[][] board){
        String status="";
        if(isImpossible(board)) {
            status = "Impossible";
        } else if (isDraw(board)) {
            status = "Draw";
        } else if (isWinX(board)) {
            status = "X wins";
        } else if (isWinO(board)) {
            status = "O wins";
        }
        return status;
    }

    public static void printStatus(String status) {
        System.out.println(status);
    }

    public static String readGameStates() {
        System.out.print("Enter cells: " );
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static Coordinates getUserMoveFromCommandLine() throws InputMismatchException, OutBoundsMoveException {
        Scanner scanner = new Scanner(System.in);
        int xCoord = scanner.nextInt()-1;
        int yCoord = scanner.nextInt()-1;
        return new Coordinates(xCoord, yCoord);
    }

    public static void makeUserMove(char[][] board, Coordinates coordinates, char player) throws OccupiedCoordinateException {

        if(board[coordinates.x][coordinates.y] == 'X' || board[coordinates.x][coordinates.y] == 'O') {
            throw new OccupiedCoordinateException();
        }
        board[coordinates.x][coordinates.y] = player;
    }

    public static void processUserMove(char[][] board, char player) {
        try {
            System.out.println("Enter the coordinates: ");
            Coordinates coordinates = getUserMoveFromCommandLine();
            makeUserMove(board, coordinates, player);
        } catch (InputMismatchException e) {
            System.out.println("You should enter numbers!");
            processUserMove(board, player);
        } catch (OutBoundsMoveException e) {
            System.out.println("Coordinates should be from 1 to 3!");
            processUserMove(board, player);
        } catch (OccupiedCoordinateException e) {
            System.out.println("This cell is occupied! Choose another one!");
            processUserMove(board, player);
        }

    }

    public static char[][] createGameBoard() {
        char[][] board = new char[3][3];
        for (char[] chars : board) {
            Arrays.fill(chars, ' ');
        }
        return board;
    }

    public static char[][] getColumns(char[][] board) {
        char[][] columns = new char[3][3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                columns[i][j] = board[j][i];
            }
        }
        return columns;
    }

    public static char[][] getRows(char[][] board) {
        return board;
    }

    public static boolean isLineOf(char player, char[] actualLine) {
        char[] expectedLine = new char[] {player, player, player};
        return Arrays.equals(actualLine, expectedLine);
    }

    public static boolean isLinesOf(char player, char[][] actualLines) {
        for(char[] actualLine: actualLines) {
            if(isLineOf(player, actualLine)) {
                return  true;
            }
        }
        return false;
    }

    public static boolean isRowOf(char player, char[][] board) {
        return isLinesOf(player, getRows(board));
    }

    public static boolean isColOf(char player, char[][] board) {
        return isLinesOf(player, getColumns(board));
    }

    public static boolean isRowOfX(char[][] board) {
        return isRowOf('X', board);
    }

    public static boolean isRowOfO(char[][] board) {
        return isRowOf('O', board);
    }

    public static boolean isColOfX(char[][] board) {
        return isColOf('X', board);
    }

    public static boolean isColOfO(char[][] board) {
        return isColOf('O', board);
    }

    public static char[] getLeftDiag(char[][] board) {
        return new char[] {board[0][0], board[1][1], board[2][2]};
    }

    public static char[] getRightDiag(char[][] board) {
        return new char[] {board[0][2], board[1][1], board[2][0]};
    }

    public static boolean isRightDiagOf(char player, char[][] board) {
        return isLineOf(player, getRightDiag(board));
    }

    public static boolean isLeftDiagOf(char player, char[][] board) {
        return isLineOf(player, getLeftDiag(board));
    }

    public static boolean isRightDiagOfX(char[][] board) {
        return isRightDiagOf('X', board);
    }
    public static boolean isRightDiagOfO(char[][] board) {
        return isRightDiagOf('O', board);
    }

    public static boolean isLeftDiagOfX(char[][] board) {
        return isLeftDiagOf('X', board);
    }

    public static boolean isLeftDiagOfO(char[][] board) {
        return isLeftDiagOf('O', board);
    }

    public static boolean isDiagonalOfX(char[][] board) {
        return isLeftDiagOfX(board) || isRightDiagOfX(board);
    }

    public static boolean isDiagonalOfO(char[][] board) {
        return isLeftDiagOfO(board) || isRightDiagOfO(board);
    }

    public static boolean isWinX(char[][] board) {
        return isColOfX(board) || isRowOfX(board) || isDiagonalOfX(board);
    }

    public static boolean isWinO(char[][] board) {
        return isColOfO(board) || isRowOfO(board) || isDiagonalOfO(board);
    }

    public static boolean isNoWinner(char[][] board){
        return !isWinX(board) && !isWinO(board);
    }

    public static boolean isMoveAvailable(char[][] board) {
        return Arrays.stream(board).flatMap(elements->IntStream.range(0, elements.length).mapToObj(i -> elements[i])).anyMatch(x-> x==' ');
    }

    public static int countPlayerMovies(char player, char[][] board) {
        return (int)Arrays.stream(board).flatMap(elements->IntStream.range(0, elements.length).mapToObj(i -> elements[i])).filter(x-> x==player).count();
    }

    public static boolean isWrongMoviesNumber(char[][] board) {
        return Math.abs(countPlayerMovies('X', board)-countPlayerMovies('O', board))>1;
    }


    public static boolean isDraw(char[][] board) {
        return isNoWinner(board) && !isMoveAvailable(board);
    }

    public static boolean isGameNotFinished(char[][] board) {
        return isNoWinner(board) && isMoveAvailable(board);
    }

    public static boolean isImpossible(char[][] board) {
        return isWinO(board) && isWinX(board) || isWrongMoviesNumber(board);
    }

    public static void printGameBoard(char[][] board) {
        System.out.println("---------");
        for (char[] letter : board) {
            System.out.print("| ");
            for (char c : letter) {
                System.out.print(c + " ");
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("---------");
    }

}