package callummcgregor.common.terrain;

import java.awt.Color;
import java.awt.Rectangle;

import callummcgregor.common.entity.EntityBase;
import callummcgregor.common.terrain.tile.HealingTile;

public class Tile {

	public static final int TILE_SIZE_X = 16;
	public static final int TILE_SIZE_Y = 16;

	private int posX, posY;
	private int chunkX, chunkY;
	public Material material;
	public float resistance;
	public String name;
	private String textureName;
	protected World world;
	protected TerrainMap map;
	protected Rectangle hitbox;
	private Chunk chunkIn;
	private boolean isWall = false;

	public Tile(Material mat, String name, int posX, int posY, World world) {
		this.material = mat;
		this.name = name;
		this.world = world;
		this.setTextureName("/res/tiles/" + name + ".png");
		this.posX = posX;
		this.posY = posY;
		hitbox = new Rectangle(posX, posY, TILE_SIZE_X, TILE_SIZE_Y);
		chunkX = world.getContainingChunk(posX, posY).getChunkX();
		chunkY = world.getContainingChunk(posX, posY).getChunkY();
		setChunkIn(world.getContainingChunk(posX, posY));
	}

	public Tile(Material mat, String name, int posX, int posY, World world,
			boolean isWall) {
		this(mat, name, posX, posY, world);
		this.isWall = isWall;
	}

	public Tile(Material mat, String name, boolean isWall) {
		this.material = mat;
		this.isWall = isWall;
		this.setTextureName("/res/tiles/" + name + ".png");
		this.name = name;
	}

	public void setPosition(int x, int y, TerrainMap map) {
		this.map = map;
		hitbox = new Rectangle(x, y, TILE_SIZE_X, TILE_SIZE_Y);
		setPosX(x);
		setPosY(y);
		chunkX = map.getContainingChunk(posX, posY).getChunkX();
		chunkY = map.getContainingChunk(posX, posY).getChunkY();
		setChunkIn(map.getContainingChunk(posX, posY));
	}

	public void setPosition(int x, int y, World world) {
		this.world = world;
		hitbox = new Rectangle(x, y, TILE_SIZE_X, TILE_SIZE_Y);
		setPosX(x);
		setPosY(y);
		chunkX = world.getContainingChunk(posX, posY).getChunkX();
		chunkY = world.getContainingChunk(posX, posY).getChunkY();
		setChunkIn(world.getContainingChunk(posX, posY));
	}

	public void tickTile(World world) {
		this.world = world;
		if (isWall()) {
			for (EntityBase e : world.activeEntities) {
				if (e.intersects(getHitbox())) {
					e.knockbackEntity(2,
							world.getOppositeDirection(e.getDirection()));
				}
			}
		}
	}

	public Color getColor() {
		return material.getColor();
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getChunkX() {
		return chunkX;
	}

	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public void setChunkY(int chunkY) {
		this.chunkY = chunkY;
	}

	public String getTextureName() {
		return textureName;
	}

	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}

	public Chunk getChunkIn() {
		return chunkIn;
	}

	public void setChunkIn(Chunk chunkIn) {
		this.chunkIn = chunkIn;
	}

	public boolean isInTile(int posX, int posY) {
		if (posX >= this.posX && posX < this.posX + TILE_SIZE_X) {
			return posY >= this.posY && posY < this.posY + TILE_SIZE_Y;
		} else
			return false;
	}

	public boolean isWall() {
		return isWall;
	}

	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}

	public static int convertToTilePlace(int place) {
		return (place % TILE_SIZE_X) + 1;
	}

	public static class Material {

		public Color color;
		public Color wallColor = Color.BLACK;

		public static final Material rock = new Material(Color.GRAY, Color.DARK_GRAY);
		public static final Material healing = new Material(Color.PINK);
		public static final Material grass = new Material(new Color(0.05f, 0.75f, 0.15f));
		public static final Material dirt = new Material(new Color(0.7f, 0.6f, 0.0f));
		public static final Material hellRock = new Material(new Color(0.8f, 0.1f, 0.1f));

		public Material(Color color) {
			this.color = color;
		}
		
		public Material(Color color, Color wall){
			this(color);
			setWallColor(wall);
		}

		public Color getColor() {
			return color;
		}
		
		public Color getWallColor() {
			return wallColor;
		}

		public void setWallColor(Color wallColor) {
			this.wallColor = wallColor;
		}

	}

	public static final Tile getTileRock() {
		return new Tile(Material.rock, "tile_rock", false);
	}

	public static final Tile getTileHealing() {
		return new HealingTile(Material.healing, "tile_healing", false);
	}

	public static final Tile getTileGrass() {
		return new Tile(Material.grass, "tile_grass", false);
	}
	
	public static final Tile getTileDirt(){
		return new Tile(Material.dirt, "tile_dirt", false);
	}
	
	public static final Tile getHellRock(){
		return new Tile(Material.hellRock, "tile_hell_rock", false);
	}

}
