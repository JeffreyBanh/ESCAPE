package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    private long seed;
    public static final int BUFFER = 5;
    public static final int NUMBEROFROOMS = 15;
    private int[][] coordinates;
    private int avatarXCoordinate;
    private int avatarYCoordinate;
    private int monsterXCoordinate;
    private int monsterYCoordinate;
    private String avatarName = Tileset.AVATAR.description();
    private boolean hasChest = false;
    Dijkstra dijkstra = new Dijkstra();
    private static int[][] maze;
    private boolean won = false;
    private String gamestateInfo = "";
    private boolean isDark = false;

    public static final int STATUSNEG = -8 * 2;
    public static final int TWOHUNDREDFIFTY = 250;
    public static final int FIVEHUNDRED = 500;
    public static final int THOUSAND = 1000;
    public static final int FONTBIGTWO = 30;
    public static final int SCALE = 16;
    public static final int EVENBIGGERFRONT = 60;
    public static final int SMALLFONT = 40;
    public static final int SETYSCALE = -30;
    public static final int RAND = 10;
    public static final int RANDTWO = 15;
    public static final int LENGTHOFSEED = 20;
    public static final int LENGTHOFLONG = 19;
    public static final int BIGFONT = 40;
    public static final int NEGTEN = -10;

    private static final Map<String, TETile> TILESET_MAP = new HashMap<String, TETile>();

    TETile currentAvatar = Tileset.AVATAR;

    String currentTile = "";

    public void interactWithKeyboard() {
        mainMenu();
        String input = input(1);
        if (input.equalsIgnoreCase("N")) {
            newGame();
        } else if (input.equalsIgnoreCase("L")) {
            try {
                loadGame();
            } catch (IOException ignored) {
                System.out.println();
            }
        } else if (input.equalsIgnoreCase("Q")) {
            System.exit(0);
        } else if (input.equalsIgnoreCase("P")) {
            changePlayerName();
            interactWithKeyboard();
        }
    }

    public void interactWithKeyboard(String input) {
        mainMenu();
        if (input.equalsIgnoreCase("N")) {
            newGame();
        } else if (input.equalsIgnoreCase("L")) {
            try {
                loadGame();
            } catch (IOException e) {
                System.out.println();
            }
        } else if (input.equalsIgnoreCase("Q")) {
            System.exit(0);
        } else if (input.equalsIgnoreCase("P")) {
            changePlayerName();
            interactWithKeyboard();
        }
    }

    public void changePlayerName() {
        int scale = 8 * 2;
        StdDraw.setCanvasSize(this.WIDTH * scale, this.HEIGHT * scale);
        int sizeOfFontBig = 5 * 2 * 4;
        Font fontBig = new Font("Monaco", Font.BOLD, sizeOfFontBig);
        StdDraw.setFont(fontBig);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(STATUSNEG, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Enter Avatar Name (Press '-' to confirm): ");
        avatarName = input(true);
        StdDraw.pause(FIVEHUNDRED);
        drawFrame("Loading...");
        StdDraw.pause(THOUSAND);
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FONTBIGTWO);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, s);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, s);
    }

    private void mainMenu() {
        StdDraw.setCanvasSize(this.WIDTH * SCALE, this.HEIGHT * SCALE);
        Font fontBig = new Font("Monaco", Font.BOLD, EVENBIGGERFRONT * 2);
        StdDraw.setFont(fontBig);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(SETYSCALE, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "ESCAPE");

        Font fontSmall = new Font("Monaco", Font.BOLD, SMALLFONT);
        StdDraw.setFont(fontSmall);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "New Game (N)");

        StdDraw.setYscale(RAND, this.HEIGHT);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Load Game (L)");

        StdDraw.setYscale(RAND + 6, this.HEIGHT);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Change Name (P)");

        StdDraw.setYscale(RANDTWO + 5, this.HEIGHT);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Quit (Q)");


    }

    private String input(int n) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                sb.append(input);
            }
        }
        return sb.toString();
    }

    private String input(boolean isForName) {
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StringBuilder sb = new StringBuilder();

        if (isForName) {
            while (sb.length() < LENGTHOFSEED) {
                if (sb.toString().contains("-")) {
                    return sb.toString();
                }
                if (StdDraw.hasNextKeyTyped()) {
                    char input = StdDraw.nextKeyTyped();
                    sb.append(input);
                    drawFrame(sb.toString());
                }
            }
        }


        while (sb.length() < LENGTHOFLONG) {
            if (sb.toString().contains("s") || sb.toString().contains("S")) {
                return sb.toString();
            }
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                sb.append(input);
                drawFrame(sb.toString());
            }
        }
        return sb.toString();
    }

    private void newGame() {
        StdDraw.setCanvasSize(this.WIDTH * SCALE, this.HEIGHT * SCALE);
        Font fontBig = new Font("Monaco", Font.BOLD, BIGFONT);
        StdDraw.setFont(fontBig);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(NEGTEN, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Enter a seed (Press S to Confirm):");
        String seedInput = input(false);
        StdDraw.pause(FIVEHUNDRED);
        drawFrame("Loading...");
        StdDraw.pause(THOUSAND);
        interactWithInputStringFromKeyboard(seedInput);
    }

    private void saveGame(TETile[][] world) {
        File file = new File("slot.txt");
        File fileTwo = new File("seedSlot.txt");
        try {
            PrintWriter writer = new PrintWriter(file);
            PrintWriter writerTwo = new PrintWriter(fileTwo);
            for (int i = 0; i < world.length; i++) {
                for (int j = 0; j < world[i].length; j++) {
                    writer.print(world[i][j].description() + ", ");
                }
                writer.println();
            }
            writerTwo.print(seed + " " + avatarName + " " + avatarXCoordinate + " " + avatarYCoordinate + " "
                    + monsterXCoordinate + " " + monsterYCoordinate + " "
                    + isDark + " " + currentTile + " " + hasChest);
            writerTwo.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public String createGamestate(String movements) {
        if (movements.equals(" ")) {
            return gamestateInfo;
        }
        gamestateInfo = gamestateInfo + ", " + movements;
        return gamestateInfo;
    }


    public void loadGame() throws IOException {
        TETile[][] savedWorld = new TETile[WIDTH][HEIGHT];
        In in = new In("slot.txt");
        In in2 = new In("seedSlot.txt");
        int counter = 0;
        TILESET_MAP.put("you", Tileset.AVATAR);
        TILESET_MAP.put("you left", Tileset.AVATAR_LEFT);
        TILESET_MAP.put("wall", Tileset.WALL);
        TILESET_MAP.put("floor", Tileset.FLOOR);
        TILESET_MAP.put("room", Tileset.ROOM);
        TILESET_MAP.put("monster", Tileset.MONSTER);
        TILESET_MAP.put("monster right", Tileset.MONSTER_RIGHT);
        TILESET_MAP.put("nothing", Tileset.NOTHING);
        TILESET_MAP.put("locked door", Tileset.LOCKED_DOOR);
        TILESET_MAP.put("ladder", Tileset.LADDER);
        TILESET_MAP.put("unlocked door", Tileset.UNLOCKED_DOOR);
        TILESET_MAP.put("chest closed", Tileset.CHEST_CLOSED);
        TILESET_MAP.put("special chest closed", Tileset.SPECIAL_CHEST_CLOSED);
        TILESET_MAP.put("chest open", Tileset.CHEST_OPEN);
        TILESET_MAP.put("chest open full", Tileset.CHEST_OPEN_FULL);
        while (in.hasNextLine() && !in.isEmpty()) {
            String info = in.readLine();
            if (info == null) {
                System.exit(0);
            }
            String[] parts = info.split(", ");
            for (int i = 0; i < parts.length; i++) {
                TETile tile = TILESET_MAP.get(parts[i]);
                savedWorld[counter][i] = tile;
            }
            counter += 1;
        }
        String[] parts = in2.readLine().split(" ");
        avatarName = parts[1];
        avatarXCoordinate = Integer.parseInt(parts[2]);
        avatarYCoordinate = Integer.parseInt(parts[3]);
        monsterXCoordinate = Integer.parseInt(parts[4]);
        monsterYCoordinate = Integer.parseInt(parts[5]);
        isDark = Boolean.parseBoolean(parts[6]);
        currentTile = parts[7];
        hasChest = Boolean.parseBoolean(parts[8]);
        loadingWorld(savedWorld);
    }

    public TETile[][] loadGame(String input) throws IOException {
        TETile[][] savedWorld = new TETile[WIDTH][HEIGHT];
        In in = new In("slot.txt");
        In in2 = new In("seedSlot.txt");
        int counter = 0;
        TILESET_MAP.put("you", Tileset.AVATAR);
        TILESET_MAP.put("you left", Tileset.AVATAR_LEFT);
        TILESET_MAP.put("wall", Tileset.WALL);
        TILESET_MAP.put("floor", Tileset.FLOOR);
        TILESET_MAP.put("room", Tileset.ROOM);
        TILESET_MAP.put("monster", Tileset.MONSTER);
        TILESET_MAP.put("monster right", Tileset.MONSTER_RIGHT);
        TILESET_MAP.put("nothing", Tileset.NOTHING);
        TILESET_MAP.put("locked door", Tileset.LOCKED_DOOR);
        TILESET_MAP.put("ladder", Tileset.LADDER);
        TILESET_MAP.put("unlocked door", Tileset.UNLOCKED_DOOR);
        TILESET_MAP.put("chest closed", Tileset.CHEST_CLOSED);
        TILESET_MAP.put("special chest closed", Tileset.SPECIAL_CHEST_CLOSED);
        TILESET_MAP.put("chest open", Tileset.CHEST_OPEN);
        TILESET_MAP.put("chest open full", Tileset.CHEST_OPEN_FULL);
        while (in.hasNextLine() && !in.isEmpty()) {
            String info = in.readLine();
            if (info == null) {
                System.exit(0);
            }
            String[] parts = info.split(", ");
            for (int i = 0; i < parts.length; i++) {
                TETile tile = TILESET_MAP.get(parts[i]);
                savedWorld[counter][i] = tile;
            }
            counter += 1;
        }
        String[] parts = in2.readLine().split(" ");
        avatarName = parts[1];
        avatarXCoordinate = Integer.parseInt(parts[2]);
        avatarYCoordinate = Integer.parseInt(parts[3]);
        monsterXCoordinate = Integer.parseInt(parts[4]);
        monsterYCoordinate = Integer.parseInt(parts[5]);
        isDark = Boolean.parseBoolean(parts[6]);
        return loadingWorld(savedWorld, input);
    }

    private void generatePov(int x, int y) {

        double[] x1 = {0, 0, x - 2, x - 2};
        double[] y1 = {0, HEIGHT, HEIGHT, 0};

        double[] x2 = {x - 2, x - 2, WIDTH, WIDTH};
        double[] y2 = {y + 2, HEIGHT, HEIGHT, y + 2};

        double[] x3 = {x + 2, x + 2, WIDTH, WIDTH};
        double[] y3 = {0, y + 2, y + 2, 0};

        double[] x4 = {x - 2, x - 2, x + 2, x + 2};
        double[] y4 = {0, y - 2, y - 2, 0};
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledPolygon(x1, y1);
        StdDraw.filledPolygon(x2, y2);
        StdDraw.filledPolygon(x3, y3);
        StdDraw.filledPolygon(x4, y4);
        StdDraw.show();
    }


    public void generateAvatar(TETile[][] finalWorldFrame, int x, int y) {
        finalWorldFrame[x][y] = Tileset.AVATAR;
    }

    public void generateMonster(TETile[][] finalWorldFrame, int x, int y) {
        finalWorldFrame[x][y] = Tileset.MONSTER;
    }

    public TETile[][] loadingWorld(TETile[][] finalWorldFrame) {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
        maze = makeMaze(finalWorldFrame);
        controlAvatar(finalWorldFrame);
        System.exit(0);
        return new TETile[WIDTH][HEIGHT];
    }

    public TETile[][] loadingWorld(TETile[][] finalWorldFrame, String input) {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
        maze = makeMaze(finalWorldFrame);
        return controlAvatarInput(finalWorldFrame, input);
    }

    public TETile[][] interactWithInputStringFromKeyboard(String input) {
        long seedNumber = Long.parseLong(input.replaceAll("[^0-9]", ""));
        this.seed = seedNumber;
        Random rand = new Random(seedNumber);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        ter.initialize(WIDTH, HEIGHT);
        initializeWorld(finalWorldFrame);
        generateRooms(finalWorldFrame, rand);
        createWall(finalWorldFrame);
        generateAvatar(finalWorldFrame, rand);
        ter.renderFrame(finalWorldFrame);
        maze = makeMaze(finalWorldFrame);
        controlAvatar(finalWorldFrame);
        System.exit(0);
        return new TETile[WIDTH][HEIGHT];
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    private TETile[][] interactwithInputStringGradescope(String input) {
        long seedNumber = Long.parseLong(input.replaceAll("[^0-9]", ""));
        String newIn = input.replaceAll("[^a-zA-Z:]", "");
        this.seed = seedNumber;
        Random rand = new Random(seedNumber);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        ter.initialize(WIDTH, HEIGHT);
        initializeWorld(finalWorldFrame);
        generateRooms(finalWorldFrame, rand);
        createWall(finalWorldFrame);
        generateAvatar(finalWorldFrame, rand);
        ter.renderFrame(finalWorldFrame);
        maze = makeMaze(finalWorldFrame);
        finalWorldFrame = controlAvatarInput(finalWorldFrame, newIn);
        return finalWorldFrame;
    }

    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        try {
            String newIn = input.replaceAll("[^a-zA-Z:]", "");
            if (newIn.substring(0, 1).toUpperCase().equals("N")) {
                long seedNumber = Long.parseLong(input.replaceAll("[^0-9]", ""));
                if (newIn.length() > 1) {
                    return interactwithInputStringGradescope(seedNumber + newIn.substring(2));
                } else {
                    return interactwithInputStringGradescope(String.valueOf(seedNumber));
                }
            } else if (newIn.substring(0, 1).toUpperCase().equals("L")) {
                return loadGame(newIn);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new TETile[WIDTH][HEIGHT];
    }


    private void createRoomSquare(TETile[][] world, int roomSize, int i, int j, boolean isSpecial) {
        for (int x = i - roomSize; x < i + roomSize; x++) {
            for (int y = j - roomSize; y < j + roomSize; y++) {
                if (x < WIDTH && y < HEIGHT) {
                    world[x][y] = Tileset.ROOM;
                }
            }
        }
        if (isSpecial) {
            world[i][j] = Tileset.SPECIAL_CHEST_CLOSED;
        } else {
            world[i][j] = Tileset.CHEST_CLOSED;
        }
    }

    private void createRoomRect(TETile[][] world, int roomSize, int i, int j, Random rand, boolean isSpecial) {
        int randomRect = rand.nextInt(2);

        if (randomRect == 0) {
            for (int x = i - roomSize * 2; x < i + roomSize * 2; x++) {
                for (int y = j - roomSize; y < j + roomSize; y++) {
                    if (x < WIDTH && y < HEIGHT) {
                        world[x][y] = Tileset.ROOM;
                    }
                }
            }
        } else {
            for (int x = i - roomSize; x < i + roomSize; x++) {
                for (int y = j - roomSize * 2; y < j + roomSize * 2; y++) {
                    if (x < WIDTH && y < HEIGHT) {
                        world[x][y] = Tileset.ROOM;
                    }
                }
            }
        }

        if (isSpecial) {
            world[i][j] = Tileset.SPECIAL_CHEST_CLOSED;
        } else {
            world[i][j] = Tileset.CHEST_CLOSED;
        }
    }

    private void createWall(TETile[][] world) {
        //iterates through the entire world
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j] == Tileset.ROOM || world[i][j] == Tileset.FLOOR) {
                    // iterates around the entire hallway or room
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (k < WIDTH && k >= 0 && l >= 0 && l < HEIGHT) {
                                if (world[k][l] == Tileset.NOTHING) {
                                    world[k][l] = Tileset.WALL;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean checkIfRoom(TETile[][] world, int i, int j) {
        return world[i][j] == Tileset.ROOM;
    }

    private boolean checkIfChest(TETile[][] world, int i, int j) {
        return world[i][j].description().contains("chest");
    }

    private boolean checkIfDoor(TETile[][] world, int i, int j) {
        return world[i][j].description().equals("locked door");
    }

    private void chooseRoom(TETile[][] world, int roomSize, int i, int j, Random rand, boolean isSpecial) {
        int tileNum = rand.nextInt(2);
        if (tileNum == 0) {
            createRoomRect(world, roomSize, i, j, rand, isSpecial);
        } else {
            createRoomSquare(world, roomSize, i, j, isSpecial);
        }
    }

    private void initializeWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private Integer randomRoomSize(Random rand) {
        return RandomUtils.uniform(rand, 1, 3);
    }

    private Integer randomRoomXCoordinate(Random rand, Integer roomSize) {
        return RandomUtils.uniform(rand, roomSize + BUFFER, WIDTH - (roomSize + BUFFER));
    }

    private Integer randomRoomYCoordinate(Random rand, Integer roomSize) {
        return RandomUtils.uniform(rand, roomSize + BUFFER, HEIGHT - (roomSize + BUFFER));
    }

    private boolean checkVertex(TETile[][] world, Integer xCoordinate, Integer yCoordinate, Integer roomSize) {
        return world[xCoordinate - roomSize][yCoordinate + roomSize] == Tileset.NOTHING
                && world[xCoordinate - roomSize][yCoordinate - roomSize] == Tileset.NOTHING
                && world[xCoordinate + roomSize][yCoordinate + roomSize] == Tileset.NOTHING
                && world[xCoordinate + roomSize][yCoordinate - roomSize] == Tileset.NOTHING
                && world[xCoordinate][yCoordinate + roomSize] == Tileset.NOTHING
                && world[xCoordinate][yCoordinate - roomSize] == Tileset.NOTHING;
    }

    private void generateRooms(TETile[][] world, Random rand) {
        int initialRoomSize = randomRoomSize(rand);
        int xInitialCoord = randomRoomXCoordinate(rand, initialRoomSize);
        int yInitialCoord = randomRoomYCoordinate(rand, initialRoomSize);
        boolean isSpecial = false;
        chooseRoom(world, initialRoomSize, xInitialCoord, yInitialCoord, rand, isSpecial);
        int randomNumOfRooms = RandomUtils.uniform(rand, NUMBEROFROOMS, NUMBEROFROOMS * 2);
        int specialChestIndex = RandomUtils.uniform(rand, 0, randomNumOfRooms);
        coordinates = new int[randomNumOfRooms + 1][2];
        boolean[] connected = new boolean[randomNumOfRooms + 1];
        coordinates[0][0] = xInitialCoord;
        coordinates[0][1] = yInitialCoord;
        for (int i = 0; i < randomNumOfRooms; i++) {
            boolean isFalse = false;
            int nextRoomSize = 0;
            int nextxInitialCoord = 0;
            int nextyInitialCoord = 0;
            while (!isFalse) {
                nextRoomSize = randomRoomSize(rand);
                nextxInitialCoord = randomRoomXCoordinate(rand, nextRoomSize);
                nextyInitialCoord = randomRoomYCoordinate(rand, nextRoomSize);
                isFalse = checkVertex(world, nextxInitialCoord, nextyInitialCoord, nextRoomSize);
            }
            coordinates[i + 1][0] = nextxInitialCoord;
            coordinates[i + 1][1] = nextyInitialCoord;
            if (i == specialChestIndex) {
                isSpecial = true;
            }
            chooseRoom(world, nextRoomSize, nextxInitialCoord, nextyInitialCoord, rand, isSpecial);
            isSpecial = false;
        }
        generateHallways(world, connected);

    }

    private void generateHallways(TETile[][] world, Integer xInitialCoord, Integer nextxInitialCoord,
                                  Integer yInitialCoord, Integer nextyInitialCoord) {

        for (int i = Math.min(xInitialCoord, nextxInitialCoord); i <= Math.max(xInitialCoord, nextxInitialCoord); i++) {
            if (!checkIfChest(world, i, yInitialCoord)) {
                if (!checkIfRoom(world, i, yInitialCoord) && !checkIfDoor(world, i, yInitialCoord)) {
                    if (checkIfRoom(world, i - 1, yInitialCoord) || checkIfRoom(world, i + 1, yInitialCoord)) {
                        world[i][yInitialCoord] = Tileset.LOCKED_DOOR;
                    } else {
                        world[i][yInitialCoord] = Tileset.FLOOR;
                    }
                }
            }
        }

        for (int j = Math.min(yInitialCoord, nextyInitialCoord); j <= Math.max(yInitialCoord, nextyInitialCoord); j++) {
            if (!checkIfChest(world, nextxInitialCoord, j)) {
                if (!checkIfRoom(world, nextxInitialCoord, j) && !checkIfDoor(world, nextxInitialCoord, j)) {
                    if (checkIfRoom(world, nextxInitialCoord, j - 1) || checkIfRoom(world, nextxInitialCoord, j + 1)) {
                        world[nextxInitialCoord][j] = Tileset.LOCKED_DOOR;
                    } else {
                        world[nextxInitialCoord][j] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    private void generateHallways(TETile[][] world, boolean[] connected) {
        for (int i = 0; i < coordinates.length - 1; i++) {
            List<Double> distance = dijkstra.getDistance(coordinates, i);
            int smallestIndex = dijkstra.createSmallestIndex(distance, connected);
            int smallestXCoordinate = coordinates[smallestIndex][0];
            int smallestYCoordinate = coordinates[smallestIndex][1];
            int currentXCoordinate = coordinates[i][0];
            int currentYCoordinate = coordinates[i][1];
            connected[i] = true;
            generateHallways(world, currentXCoordinate, smallestXCoordinate, currentYCoordinate, smallestYCoordinate);
        }
    }


    private void generateAvatar(TETile[][] finalWorldFrame, Random rand) {
        int xCoordinate = 0;
        int yCoordinate = 0;
        boolean foundFloorTile = false;
        while (!foundFloorTile) {
            xCoordinate = rand.nextInt(WIDTH);
            yCoordinate = rand.nextInt(HEIGHT);
            if (finalWorldFrame[xCoordinate][yCoordinate] == Tileset.ROOM) {
                foundFloorTile = true;
                finalWorldFrame[xCoordinate][yCoordinate] = Tileset.AVATAR;
            }
        }
        this.avatarXCoordinate = xCoordinate;
        this.avatarYCoordinate = yCoordinate;
        spawnMonster(finalWorldFrame, largestDifference(avatarXCoordinate, avatarYCoordinate));
    }

    private int[][] largestDifference(int x, int y) {
        int[][] res = new int[1][2];
        int largestX = 0;
        int largestY = 0;
        int largestDifference = Integer.MIN_VALUE;
        int totalCoordinate = x + y;
        for (int i = 0; i < coordinates.length; i++) {
            int currentDiff = Math.abs((coordinates[i][0] + coordinates[i][1]) - (totalCoordinate));
            if (currentDiff > largestDifference) {
                largestX = coordinates[i][0];
                largestY = coordinates[i][1];
                largestDifference = currentDiff;
            }
        }
        res[0][0] = largestX;
        res[0][1] = largestY;
        return res;
    }

    private void spawnMonster(TETile[][] world, int[][] res) {

        world[res[0][0]][res[0][1]] = Tileset.MONSTER;
        monsterXCoordinate = res[0][0];
        monsterYCoordinate = res[0][1];
        world[res[0][0] - 1][res[0][1]] = Tileset.LADDER;
    }

    /* private void chaseAvatar(TET)*/
    private boolean isFloor(TETile tile) {
        return tile.description().equals("floor");
    }

    private boolean isRoom(TETile tile) {
        return tile.description().equals("room");
    }

    private boolean isWall(TETile tile) {
        return tile.description().equals("wall");
    }

    private void setRoom(TETile[][] world, int x, int y) {
        world[x][y] = Tileset.ROOM;
    }

    private void setFloor(TETile[][] world, int x, int y) {
        world[x][y] = Tileset.FLOOR;
    }

    private void setDoor(TETile[][] world, int x, int y) {
        world[x][y] = Tileset.UNLOCKED_DOOR;
    }

    private void setAvatar(TETile[][] world, int x, int y, TETile avatar) {
        world[x][y] = avatar;
    }

    private TETile getTile(TETile[][] world, int x, int y) {
        return world[x][y];
    }

    private void removeDarkness(TETile[][] world) {
        ter.renderFrame(world);
    }

    private boolean isLockedDoor(TETile tile) {
        return tile.description().equals("locked door");

    }

    private boolean isLadder(TETile tile) {
        return tile.description().equals("ladder");
    }

    private boolean isUnlockedDoor(TETile tile) {
        return tile.description().equals("unlocked door");

    }

    private boolean isChest(TETile tile) {
        return tile.description().equals("chest closed");
    }

    private boolean isSpecialChest(TETile tile) {
        return tile.description().equals("special chest closed");
    }


    private boolean toggleDarkness(boolean isDarkInput, TETile[][] world) {
        if (!isDarkInput) {
            isDarkInput = true;
            removeDarkness(world);
        } else {
            isDarkInput = false;
            ter.withoutShowRenderFrame(world);
            generatePov(avatarXCoordinate, avatarYCoordinate);
        }
        return isDarkInput;
    }

    private void checkIfDark(boolean isDarkInput, TETile[][] world) {
        if (!isDarkInput) {
            ter.withoutShowRenderFrame(world);
            generatePov(avatarXCoordinate, avatarYCoordinate);
        } else {
            ter.renderFrame(world);
        }
    }


    private TETile playerMovement(String input, TETile[][] finalWorldFrame) {
        TETile nextTile;
        switch (input) {
            case "W":
                createGamestate("W");
                nextTile = getTile(finalWorldFrame, avatarXCoordinate, avatarYCoordinate + 1);
                if (!isWall(nextTile) && !isLockedDoor(nextTile) && !isChest(nextTile) && !isLadder(nextTile)) {
                    this.avatarYCoordinate += 1;
                }
                break;
            case "S":
                createGamestate("S");
                nextTile = getTile(finalWorldFrame, avatarXCoordinate, avatarYCoordinate - 1);
                if (!isWall(nextTile) && !isLockedDoor(nextTile) && !isChest(nextTile) && !isLadder(nextTile)) {
                    this.avatarYCoordinate -= 1;
                }
                break;
            case "A":
                createGamestate("A");
                nextTile = getTile(finalWorldFrame, avatarXCoordinate - 1, avatarYCoordinate);
                if (!isWall(nextTile) && !isLockedDoor(nextTile) && !isChest(nextTile) && !isLadder(nextTile)) {
                    this.avatarXCoordinate -= 1;
                    currentAvatar = Tileset.AVATAR_LEFT;
                }
                break;
            case "D":
                createGamestate("W");
                nextTile = getTile(finalWorldFrame, avatarXCoordinate + 1, avatarYCoordinate);
                if (!isWall(nextTile) && !isLockedDoor(nextTile) && !isChest(nextTile) && !isLadder(nextTile)) {
                    this.avatarXCoordinate += 1;
                    currentAvatar = Tileset.AVATAR;
                }
                break;
            default:
                nextTile = null;
                break;
        }
        return nextTile;
    }

    private TETile monsterMovement(int counter, TETile temp, TETile[][] finalWorldFrame) {
        int tempMonsterX = monsterXCoordinate;
        int tempMonsterY = monsterYCoordinate;
        MonsterAlgorithm.Node start = new MonsterAlgorithm.Node(monsterXCoordinate, monsterYCoordinate);
        MonsterAlgorithm.Node end = new MonsterAlgorithm.Node(avatarXCoordinate, avatarYCoordinate);
        int[][] nextMonsterCoordinate = MonsterAlgorithm.getVertex(start, end, maze);
        monsterXCoordinate = nextMonsterCoordinate[0][0];
        monsterYCoordinate = nextMonsterCoordinate[0][1];
        if (counter == 0) {
            temp = finalWorldFrame[monsterXCoordinate][monsterYCoordinate];
            if (monsterXCoordinate >= tempMonsterX) {
                finalWorldFrame[monsterXCoordinate][monsterYCoordinate] = Tileset.MONSTER_RIGHT;
            } else {
                finalWorldFrame[monsterXCoordinate][monsterYCoordinate] = Tileset.MONSTER;
            }
            finalWorldFrame[tempMonsterX][tempMonsterY] = Tileset.ROOM;
        } else {
            finalWorldFrame[tempMonsterX][tempMonsterY] = temp;
            temp = finalWorldFrame[monsterXCoordinate][monsterYCoordinate];
            if (monsterXCoordinate >= tempMonsterX) {
                finalWorldFrame[monsterXCoordinate][monsterYCoordinate] = Tileset.MONSTER_RIGHT;
            } else {
                finalWorldFrame[monsterXCoordinate][monsterYCoordinate] = Tileset.MONSTER;
            }
        }
        return temp;
    }

    private boolean hasChestCheck(boolean hasChestInput, boolean hasQuit) {
        if (hasChestInput) {
            hasQuit = true;
            won = true;
        }
        return hasQuit;
    }

    private boolean interactMovement(String lastInput, TETile[][] finalWorldFrame, boolean hasQuit) {
        if (lastInput.equals("W")) {
            if (finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1] == Tileset.LOCKED_DOOR) {
                finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1] = Tileset.UNLOCKED_DOOR;
            } else {
                if (isChest(finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1])) {
                    finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1] = Tileset.CHEST_OPEN;
                    checkIfDark(isDark, finalWorldFrame);
                    StdDraw.pause(TWOHUNDREDFIFTY);
                    finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1] = Tileset.ROOM;
                } else if (isSpecialChest(finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1])) {
                    finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1] = Tileset.CHEST_OPEN_FULL;
                    hasChest = true;
                    checkIfDark(isDark, finalWorldFrame);
                    StdDraw.pause(FIVEHUNDRED);
                    finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1] = Tileset.CHEST_OPEN;
                } else if (isLadder(finalWorldFrame[avatarXCoordinate][avatarYCoordinate + 1])) {
                    hasQuit = hasChestCheck(hasChest, hasQuit);
                }
            }
        } else if (lastInput.equals("S")) {
            if (finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1] == Tileset.LOCKED_DOOR) {
                finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1] = Tileset.UNLOCKED_DOOR;
            } else {
                if (isChest(finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1])) {
                    finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1] = Tileset.CHEST_OPEN;
                    checkIfDark(isDark, finalWorldFrame);
                    StdDraw.pause(TWOHUNDREDFIFTY);
                    finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1] = Tileset.ROOM;
                } else if (isSpecialChest(finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1])) {
                    finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1] = Tileset.CHEST_OPEN_FULL;
                    hasChest = true;
                    checkIfDark(isDark, finalWorldFrame);
                    StdDraw.pause(FIVEHUNDRED);
                    finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1] = Tileset.CHEST_OPEN;
                } else if (isLadder(finalWorldFrame[avatarXCoordinate][avatarYCoordinate - 1])) {
                    hasQuit = hasChestCheck(hasChest, hasQuit);
                }
            }
        } else if (lastInput.equals("A")) {
            if (finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate] == Tileset.LOCKED_DOOR) {
                finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate] = Tileset.UNLOCKED_DOOR;
            } else {
                if (isChest(finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate])) {
                    finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate] = Tileset.CHEST_OPEN;
                    checkIfDark(isDark, finalWorldFrame);
                    StdDraw.pause(TWOHUNDREDFIFTY);
                    finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate] = Tileset.ROOM;
                } else if (isSpecialChest(finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate])) {
                    finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate] = Tileset.CHEST_OPEN_FULL;
                    hasChest = true;
                    checkIfDark(isDark, finalWorldFrame);
                    StdDraw.pause(FIVEHUNDRED);
                    finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate] = Tileset.CHEST_OPEN;
                } else if (isLadder(finalWorldFrame[avatarXCoordinate - 1][avatarYCoordinate])) {
                    hasQuit = hasChestCheck(hasChest, hasQuit);
                }
            }
        } else if (lastInput.equals("D")) {
            if (finalWorldFrame[avatarXCoordinate + 1][avatarYCoordinate] == Tileset.LOCKED_DOOR) {
                finalWorldFrame[avatarXCoordinate + 1][avatarYCoordinate] = Tileset.UNLOCKED_DOOR;
            } else {
                if (isChest(finalWorldFrame[avatarXCoordinate + 1][avatarYCoordinate])) {
                    finalWorldFrame[avatarXCoordinate + 1][avatarYCoordinate] = Tileset.CHEST_OPEN;
                    checkIfDark(isDark, finalWorldFrame);
                    StdDraw.pause(TWOHUNDREDFIFTY);
                    finalWorldFrame[avatarXCoordinate + 1][avatarYCoordinate] = Tileset.ROOM;
                } else if (isSpecialChest(finalWorldFrame[avatarXCoordinate][avatarYCoordinate])) {
                    finalWorldFrame[avatarXCoordinate + 1][avatarYCoordinate] = Tileset.CHEST_OPEN_FULL;
                    hasChest = true;
                    checkIfDark(isDark, finalWorldFrame);
                    StdDraw.pause(FIVEHUNDRED);
                    finalWorldFrame[avatarXCoordinate + 1][avatarYCoordinate] = Tileset.CHEST_OPEN;
                } else if (isLadder(finalWorldFrame[avatarXCoordinate + 1][avatarYCoordinate])) {
                    hasQuit = hasChestCheck(hasChest, hasQuit);
                }
            }
        }
        return hasQuit;
    }

    private boolean checkMonster() {
        boolean quit = false;
        if (monsterXCoordinate == avatarXCoordinate && monsterYCoordinate == avatarYCoordinate) {
            quit = true;
        }
        return quit;
    }

    private void winningHUD() {
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("SansSerif", Font.BOLD, SMALLFONT);
        StdDraw.setFont(fontBig);
        if (won) {
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "You Win!");
        }
        StdDraw.show();
        StdDraw.pause(THOUSAND);
    }

    private void controlAvatar(TETile[][] finalWorldFrame) {
        StringBuilder sb = new StringBuilder();
        boolean hasQuit = false;
        boolean inRoom = true;
        String lastInput = "";
        TETile lastTile = null;
        TETile temp = null;
        checkIfDark(isDark, finalWorldFrame);
        int counter = 0;
        while (!hasQuit) {
            drawOnHUD(currentTile);
            if (StdDraw.hasNextKeyTyped()) {
                String input = String.valueOf(Character.toUpperCase(StdDraw.nextKeyTyped()));
                if (input.equals("W") || input.equals("S") || input.equals("A") || input.equals("D")) {
                    int tempX = avatarXCoordinate;
                    int tempY = avatarYCoordinate;
                    TETile nextTile = playerMovement(input, finalWorldFrame);
                    if (!isWall(nextTile) && !isLockedDoor(nextTile) && !isChest(nextTile) && !isLadder(nextTile)) {
                        if (isFloor(nextTile)) {
                            if (lastTile == Tileset.UNLOCKED_DOOR) {
                                setDoor(finalWorldFrame, tempX, tempY);
                            } else if (inRoom) {
                                setRoom(finalWorldFrame, tempX, tempY);
                            } else {
                                setFloor(finalWorldFrame, tempX, tempY);
                            }
                            inRoom = false;
                            setAvatar(finalWorldFrame, avatarXCoordinate, avatarYCoordinate, currentAvatar);
                        } else if (isRoom(nextTile)) {
                            if (lastTile == Tileset.UNLOCKED_DOOR) {
                                setDoor(finalWorldFrame, tempX, tempY);
                            } else if (inRoom) {
                                setRoom(finalWorldFrame, tempX, tempY);
                            } else {
                                setFloor(finalWorldFrame, tempX, tempY);
                            }
                            inRoom = true;
                            setAvatar(finalWorldFrame, avatarXCoordinate, avatarYCoordinate, currentAvatar);
                        } else {
                            if (inRoom) {
                                setRoom(finalWorldFrame, tempX, tempY);
                            } else {
                                setFloor(finalWorldFrame, tempX, tempY);
                            }
                        }
                        temp = monsterMovement(counter, temp, finalWorldFrame);
                        counter += 1;
                        if (!isDark) {
                            ter.withoutShowRenderFrame(finalWorldFrame);
                            generatePov(avatarXCoordinate, avatarYCoordinate);
                        } else {
                            ter.renderFrame(finalWorldFrame);
                        }
                        hasQuit = checkMonster();
                    }
                    lastTile = nextTile;
                } else if (input.equals(":")) {
                    sb.append(":");
                } else if (sb.toString().contains(":") && input.equals("Q")) {
                    sb.append("Q");
                    saveGame(finalWorldFrame);
                    hasQuit = true;
                } else if (input.equals("Z")) {
                    isDark = toggleDarkness(isDark, finalWorldFrame);
                } else if (input.equals("F")) {
                    hasQuit = interactMovement(lastInput, finalWorldFrame, hasQuit);
                    checkIfDark(isDark, finalWorldFrame);
                } else if (input.equals("E")) {
                    currentTile = updateHUD(finalWorldFrame);
                    checkIfDark(isDark, finalWorldFrame);
                    drawOnHUD(currentTile);
                }
                lastInput = input;
            }
        }
        winningHUD();
    }


    private TETile[][] controlAvatarInput(TETile[][] finalWorldFrame, String inputString) {
        StringBuilder sb = new StringBuilder();
        boolean hasQuit = false;
        boolean inRoom = true;
        TETile lastTile = null;
        checkIfDark(isDark, finalWorldFrame);
        int inputCounter = 0;
        while (!hasQuit) {
            drawOnHUD(currentTile);
            if (inputCounter < inputString.length()) {
                String input = String.valueOf(inputString.charAt(inputCounter)).toUpperCase();
                if (input.equals("W") || input.equals("S") || input.equals("A") || input.equals("D")) {
                    int tempX = avatarXCoordinate;
                    int tempY = avatarYCoordinate;
                    TETile nextTile = playerMovement(input, finalWorldFrame);
                    if (!isWall(nextTile) && !isLockedDoor(nextTile) && !isChest(nextTile) && !isLadder(nextTile)) {
                        if (isFloor(nextTile)) {
                            if (lastTile == Tileset.UNLOCKED_DOOR) {
                                setDoor(finalWorldFrame, tempX, tempY);
                            } else if (inRoom) {
                                setRoom(finalWorldFrame, tempX, tempY);
                            } else {
                                setFloor(finalWorldFrame, tempX, tempY);
                            }
                            inRoom = false;
                            setAvatar(finalWorldFrame, avatarXCoordinate, avatarYCoordinate, currentAvatar);
                        } else if (isRoom(nextTile)) {
                            if (lastTile == Tileset.UNLOCKED_DOOR) {
                                setDoor(finalWorldFrame, tempX, tempY);
                            } else if (inRoom) {
                                setRoom(finalWorldFrame, tempX, tempY);
                            } else {
                                setFloor(finalWorldFrame, tempX, tempY);
                            }
                            inRoom = true;
                            setAvatar(finalWorldFrame, avatarXCoordinate, avatarYCoordinate, currentAvatar);
                        } else {
                            if (inRoom) {
                                setRoom(finalWorldFrame, tempX, tempY);
                            } else {
                                setFloor(finalWorldFrame, tempX, tempY);
                            }
                        }
                        if (!isDark) {
                            ter.withoutShowRenderFrame(finalWorldFrame);
                            generatePov(avatarXCoordinate, avatarYCoordinate);
                        } else {
                            ter.renderFrame(finalWorldFrame);
                        }
                        hasQuit = checkMonster();
                    }
                    lastTile = nextTile;
                } else if (input.equals(":")) {
                    sb.append(":");
                } else if (sb.toString().contains(":") && input.equals("Q")) {
                    sb.append("Q");
                    saveGame(finalWorldFrame);
                    hasQuit = true;
                }
                inputCounter++;
            }
            if (inputCounter == inputString.length()) {
                hasQuit = true;
            }
        }
        return finalWorldFrame;
    }

    public void drawOnHUD(String text) {

        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("SansSerif", Font.BOLD, FONTBIGTWO);
        StdDraw.setFont(fontBig);
        StdDraw.textRight(this.WIDTH - 1, this.HEIGHT,
                "Player: " + avatarName.replace('-', ' '));
        StdDraw.textLeft(1, this.HEIGHT, "Tile: " + text);
        StdDraw.textLeft(WIDTH / 4 + 5, this.HEIGHT, "Treasure: " + hasChest);
        StdDraw.textRight(WIDTH - 1, 1, "Toggle Light (z)");
        StdDraw.textRight(WIDTH - FONTBIGTWO - 5, 1, "Hover Tile (e)");
        StdDraw.textRight(WIDTH - FONTBIGTWO + RAND, 1, "Interact (f)");
        StdDraw.show();
    }

    private String updateHUD(TETile[][] finalWorldFrame) {
        String tileText = "nothing";
        if ((int) Math.round(StdDraw.mouseX()) >= 0
                && (int) Math.round(StdDraw.mouseX()) < WIDTH
                && (int) Math.round(StdDraw.mouseY()) < HEIGHT
                && (int) Math.round(StdDraw.mouseY()) >= 0) {
            tileText = finalWorldFrame[(int) Math.round(StdDraw.mouseX())]
                    [(int) Math.round(StdDraw.mouseY())].description();
        }
        return tileText;
    }

    private int[][] makeMaze(TETile[][] world) {
        int[][] currentMaze = new int[world.length][world[0].length];
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                if (world[i][j] != Tileset.WALL) {
                    currentMaze[i][j] = 0;
                } else {
                    currentMaze[i][j] = 1;
                }
            }
        }
        return currentMaze;
    }
}

