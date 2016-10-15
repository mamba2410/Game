package callummcgregor.lib;

import java.util.List;

import callummcgregor.common.Game;
import callummcgregor.common.terrain.World;

public class References {
	
	/* ENABLES LOTS OF DEBUG STUFF IN THE CONSOLE */
	public static boolean DEBUG = false;
	
	public static List<Integer> keysDown;
	public static DirectionKey movementKey = DirectionKey.NONE;
	public static boolean hasMovementButton = false;
	public static long g_tps, g_fps;
	public static int mouseX, mouseY;
	
	public static final String TITLE = "Frame Title";
	public static int WIDTH;
	public static int HEIGHT;
	
	public static final int ENTITYSEED = 423975;
	
	public static Game GAME;
	public static World WORLD;
	
	public static enum GAMESTATE {
		PLAYING, DEATH, TITLE_SCREEN
	}
	
	public static enum EntityType{
		UNDEFINED, PLAYER, ENEMY, BOSS, PROJECTILE, FRIENDLY_PROJECTILE
	}
	
	public static enum Direction{
		SOUTH, EAST, NORTH, WEST
	}
	
	public static enum DirectionKey{
		UP, LEFT, DOWN, RIGHT, NONE
	}

}
