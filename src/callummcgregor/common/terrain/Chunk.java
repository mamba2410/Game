package callummcgregor.common.terrain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import callummcgregor.common.entity.EntityBase;
import callummcgregor.lib.References;

public class Chunk {
	
	public static final int TILE_AMOUNT_X = 16;
	public static final int TILE_AMOUNT_Y = 16;
	
	private int chunkX, chunkY;
	private int posX, posY;
	private Color color;
	
	private List<Tile> tiles;
	
	public Chunk(int chunkX, int chunkY){
		tiles = new ArrayList<Tile>();
		setChunkX(chunkX);
		setChunkY(chunkY);
		posX = chunkX*TILE_AMOUNT_X*Tile.TILE_SIZE_X;
		posY = chunkY*TILE_AMOUNT_Y*Tile.TILE_SIZE_Y;
	}
	
	public boolean isInChunk(int posX, int posY){
		if(posX >= getMinX() && posX < getMaxX()){
			if(posY >= getMinY() && posY < getMaxY()){
				return true;
			}
		}
		return false;
	}
	
	public int[] getTilePos(int listPos){
		int[] pos = new int[2];
		pos[0] = posX + (listPos%TILE_AMOUNT_X)*Tile.TILE_SIZE_X;
		pos[1] = posY + (int)(Math.floor(listPos/Tile.TILE_SIZE_Y)*Tile.TILE_SIZE_Y);
		if(References.DEBUG)
			System.out.println(String.format("Returning %s, %s", pos[0], pos[1]));
		return pos;
	}
	
	public boolean isInChunk(EntityBase eb){
		if(eb.posX >= getMinX() && eb.posX < getMaxX()){
			if(eb.posY >= getMinY() && eb.posY < getMaxY()){
				return true;
			}
		}
		return false;
	}
	
	public int getMinX(){
		return chunkX*Chunk.TILE_AMOUNT_X*Tile.TILE_SIZE_X;
	}
	
	public int getMaxX(){
		return chunkX*Chunk.TILE_AMOUNT_X*Tile.TILE_SIZE_X + Chunk.TILE_AMOUNT_X*Tile.TILE_SIZE_X;
	}
	
	public int getMinY(){
		return chunkY*Chunk.TILE_AMOUNT_Y*Tile.TILE_SIZE_Y;
	}
	
	public int getMaxY(){
		return chunkY*Chunk.TILE_AMOUNT_Y*Tile.TILE_SIZE_Y + Chunk.TILE_AMOUNT_Y*Tile.TILE_SIZE_Y;
	}

	public List<Tile> getTiles() {
		return tiles;
	}
	
	public void setTileAt(int pos, Tile tile){
		if(pos == 0)
			tiles.add(tile);
		else
			tiles.set(pos, tile);
	}
	
	public Tile getTileAt(int pos){
		return tiles.get(pos);
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
