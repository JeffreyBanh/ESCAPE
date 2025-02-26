package byow.TileEngine;

import java.awt.Color;
import java.nio.file.Paths;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {

    // Define the base directory and filename
    static String baseDir = "/path/to/proj3/byow";
    static String filename = "byow/images/monster.png";

    // Join the base directory and filename using Paths
    static String fullPath = Paths.get(baseDir, filename).toString();


    public static final TETile AVATAR = new TETile('@', Color.white, new Color(0, 0, 0), "you", "byow/images/hero_basic.png");
    public static final TETile AVATAR_LEFT = new TETile('@', Color.white, new Color(0, 0, 0), "you left", "byow/images/hero_basic_left.png");

    public static final TETile WALL = new TETile('❐', new Color(0, 0, 0), Color.darkGray, "wall", "byow/images/wall_center.png");
    public static final TETile FLOOR = new TETile('.', new Color(72, 255, 2), Color.black, "floor", "byow/images/floor_light.png");

    public static final TETile ROOM = new TETile('□', new Color(221, 36, 36), Color.black, "room", "byow/images/floor_stain_1.png");

    public static final TETile MONSTER = new TETile('K', Color.white, Color.black, "monster", "byow/images/monster_demon.png");
    public static final TETile MONSTER_RIGHT = new TETile('K', Color.white, Color.black, "monster right", "byow/images/monster_demon_right.png");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door", "byow/images/door_closed.png");
    public static final TETile LADDER = new TETile('.', Color.blue, Color.BLUE, "ladder","byow/images/Floor_ladder.png");

    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door", "byow/images/door_open.png");


    public static final TETile CHEST_CLOSED = new TETile('▢', Color.black, Color.black, "chest closed", "byow/images/chest_golden_closed.png");
    public static final TETile SPECIAL_CHEST_CLOSED = new TETile('▢', Color.black, Color.black, "special chest closed", "byow/images/chest_golden_closed.png");
    public static final TETile CHEST_OPEN = new TETile('▢', Color.black, Color.black, "chest open", "byow/images/chest_golden_open_empty.png");
    public static final TETile CHEST_OPEN_FULL = new TETile('▢', Color.black, Color.black, "chest open full", "byow/images/chest_golden_open_full.png");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    //    public static final TETile NOTHING = new TETile(' ', new Color(255, 255, 255), new Color(29, 29, 47), "nothing");

    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");

}


