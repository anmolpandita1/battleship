package battleship;

import java.util.Scanner;

class Coordinate {
    private int x;
    private int y;

    Coordinate(String coordinate) {
        int[] xy = this.resolve(coordinate);
        if (xy != null) {
            this.x = xy[0];
            this.y = xy[1];
        }
    }



    public boolean isValidCoordinate(int x, int y) throws Exception {
        if (x < 0 || x >= Board.ROWS || y < 0 || y > Board.COLS) {
            throw new Exception("Error! You entered the wrong coordinates! Try again:\n");
        } else {
            return true;
        }
    }

    public int[] resolve(String coordinate) {
        int resolvedXToInt = coordinate.charAt(0) - 'A';
        int resolvedYToInt = Integer.parseInt(coordinate.substring(1));
        try {
            boolean isValidCoordinate = isValidCoordinate(resolvedXToInt, resolvedYToInt);
            if (isValidCoordinate) return new int[]{resolvedXToInt, resolvedYToInt};
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public int[] getCoordinate() {
        return new int[]{x, y};
    }

}

class Ship {
    private final String name;
    private final int length;

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
}

class Board {
    public static final int ROWS = 10;
    public static final int COLS = 10;

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

    public void shoot(int x, int y) {
        if (this.board[x][y] == 'O') {
            this.board[x][y] = 'X';
            this.printBoard();
            System.out.print("You hit a ship!\n");
        } else {
            this.board[x][y] = 'M';
            this.printBoard();
            System.out.print("You missed!\n");
        }
    }


}


public class Main {

    public static int[][] getShipCoordinatesFromStdInFor(Ship ship) {
        System.out.print("Enter the coordinates of the " + ship.getName() + " (" + ship.getLength() + " cells):\n");

        int[][] coordinates = new int[2][2];
        int[] coordinatesX1Y1 = new int[2];
        int[] coordinatesX2Y2 = new int[2];
        Scanner input = new Scanner(System.in);

        if (input.hasNext()) {
            String[] splitStdInput = input.nextLine().split(" ");
            try {
                coordinatesX1Y1 = resolveCoordinate(splitStdInput[0]);
                coordinatesX2Y2 = resolveCoordinate(splitStdInput[1]);
                coordinates[0][0] = coordinatesX1Y1[0];
                coordinates[0][1] = coordinatesX1Y1[1];
                coordinates[1][0] = coordinatesX2Y2[0];
                coordinates[1][1] = coordinatesX2Y2[1];
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }

        }
        input.close();

        return coordinates;
    }

    public static int[] getOneCoordinateStringFromStdIn() throws Exception {
        Scanner input = new Scanner(System.in);
        int[] coordinate = null;
        if (input.hasNext()) {
            coordinate = resolveCoordinate(input.nextLine());
        }
        input.close();
        return coordinate;
    }

    public static int[] resolveCoordinate(String coordinateString) throws Exception {
        Coordinate inputCoordinate = new Coordinate(coordinateString);
        return inputCoordinate.getCoordinate();
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

        // place ships
        for (int i = 0; i < ships.length; i++) {

            try {
                int[][] coordinates = getShipCoordinatesFromStdInFor(ships[i]);

                int x1 = coordinates[0][0];
                int y1 = coordinates[0][1];
                int x2 = coordinates[1][0];
                int y2 = coordinates[1][1];

                board.placeShip(x1, y1, x2, y2, ships[i], 'O');
                board.printBoard();
            } catch (Exception e) {
                --i;
                System.out.print(e.getMessage());
            }
        }

        // start
        System.out.println("The game starts!");
        board.printBoard();
        System.out.println("Take a shot!");

        // shoot
        for (int i = 0; i < 2; i++) {
            try {
                int[] shootingCoordinates = getOneCoordinateStringFromStdIn();
                board.shoot(shootingCoordinates[0], shootingCoordinates[1] - 1);
            } catch (Exception e) {
                --i;
                System.out.print(e.getMessage());
            }
        }


    }
}



