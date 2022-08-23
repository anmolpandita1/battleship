package battleship;

import java.util.*;

class Ship {
    private String name;
    private int length;

    Ship(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return this.name;
    }

    public int getLength() {
        return this.length;
    }

    public boolean isValidLength(int length) {
        return this.length == length;
    }
}

class Board {
    private final int ROWS = 10;
    private final int COLS = 10;

    public char[][] board = new char[ROWS][COLS];

    Board() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                this.board[i][j] = '~';
            }
        }
    }

    public void printBoard() {
        System.out.print("  1 2 3 4 5 6 7 8 9 10 \n");
        char column = 'A';
        for (char[] i : board) {
            System.out.print(column++);
            for (char j : i) {
                System.out.print(" " + j);
            }
            System.out.print("\n");
        }
    }

    public void placeShip(int x1, int y1, int x2, int y2, Ship ship, char mark) throws Exception {

        // 7 - 2 is 5 but 2 and 7 are inclusive so add one
        int lengthOfSelectedShip = Math.abs(x1 - x2) + Math.abs(y1 - y2) + 1;

        // adjust for array length
        y1 = y1 - 1;
        y2 = y2 - 1;


        // calculate safe area around where ship is supposed to be placed
        int safeAreaX1 = Math.min(x1, x2) - 1 < 0 ? Math.min(x1, x2) : Math.min(x1, x2) - 1;
        int safeAreaX2 = Math.max(x1, x2) + 1 > 9 ? Math.max(x1, x2) : Math.max(x1, x2) + 1;
        int safeAreaY1 = Math.min(y1, y2) - 1 < 0 ? Math.min(y1, y2) : Math.min(y1, y2) - 1;
        int safeAreaY2 = Math.max(y1, y2) + 1 > 9 ? Math.max(y1, y2) : Math.max(y1, y2) + 1;

        // check if safe area already has some part of ship in it
        for (int i = safeAreaX1; i <= safeAreaX2; i++) {
            for (int j = safeAreaY1; j <= safeAreaY2; j++) {
                if (this.board[i][j] == mark) {
                    System.out.print(this.board[i][j]);
                    throw new Exception("Error! You placed it too close to another one. Try again:\n");
                }
            }
        }

        // the ship can not be placed diagonally
        // length of ship should match the length calculated from co-ordinates
        // finally place on the board
        if (x1 != x2 && y1 != y2) {
            throw new Exception("Error! Wrong Ship location! Try again:\n");
        } else if (lengthOfSelectedShip != ship.getLength()) {
            throw new Exception("Error! Wrong length of the " + ship.getName() + "! Try again:\n");
        } else {
            for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                for (int j = Math.min(y1, y2); j <= Math.max(y1, y2); j++) {
                    this.board[i][j] = mark;
                }
            }
        }


    }


}


public class Main {

    public static int[] resolveCoordinates(Ship ship) {
        System.out.print("Enter the coordinates of the " + ship.getName() + " (" + ship.getLength() + " cells):\n");
        Scanner input = new Scanner(System.in);
        if (input.hasNext()) {
            String[] splitInput = input.nextLine().split(" ");

            int x1 = splitInput[0].charAt(0) - 'A';
            int y1 = Integer.parseInt(splitInput[0].substring(1));
            int x2 = splitInput[1].charAt(0) - 'A';
            int y2 = Integer.parseInt(splitInput[1].substring(1));

            return new int[]{x1, y1, x2, y2};
        }
        input.close();
        return new int[]{};
    }


    public static void main(String[] args) {
        // setup board
        Board board = new Board();

        // initialize ships
        Ship[] ships = new Ship[5];
        ships[0] = new Ship("Aircraft Carrier", 5);
        ships[1] = new Ship("Battleship", 4);
        ships[2] = new Ship("Submarine", 3);
        ships[3] = new Ship("Cruiser", 3);
        ships[4] = new Ship("Destroyer", 2);

        // start
        for (int i = 0; i < ships.length; i++) {
            int[] coordinates = resolveCoordinates(ships[i]);

            int x1 = coordinates[0];
            int y1 = coordinates[1];
            int x2 = coordinates[2];
            int y2 = coordinates[3];


            try {
                board.placeShip(x1, y1, x2, y2, ships[i], 'O');
                board.printBoard();
            } catch (Exception e) {
                --i;
                System.out.print(e.getMessage());
            }
        }

    }
}




/*
public void placeAircraftCarrier(int x1, int y1, int x2, int y2, char value){
        placeShip(x1 , y1 - 1, x2 , y2 - 1, value); //adjust for array size
    }
    public void placeBattleship(int x1, int y1, int x2, int y2, char value){

    }
    public void placeSubmarine(int x1, int y1, int x2, int y2, char value){

    }
    public void placeCruiser(int x1, int y1, int x2, int y2, char value){

    }
    public void placeDestroyer(int x1, int y1, int x2, int y2, char value){

    }
 */