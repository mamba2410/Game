package callummcgregor.common.terrain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import callummcgregor.common.entity.EntityBase;
import callummcgregor.common.terrain.World.Biome;
import callummcgregor.common.terrain.tile.HealingTile;
import callummcgregor.lib.References;
import callummcgregor.lib.Utilities;

public abstract class TerrainMap {
	
	protected World world;
	protected List<Tile> loadedTiles;
	private List<Chunk> loadedChunks;
	public List<EntityBase> entities;
	private Biome biome;
	// OVERRIDE ME!!!
	public String mapName;
	
	public TerrainMap(World world, String mapName, Biome biome){
		this.world = world;
		this.mapName = mapName;
		this.biome = biome;
		if(this.mapName != null){
		loadedChunks = new ArrayList<Chunk>();
		loadedTiles = new ArrayList<Tile>();
		entities = new ArrayList<EntityBase>();
		
		List<Tile> tempTiles = new ArrayList<Tile>();
		int chunkX = 0, chunkY = 0;
		while(chunkX-1 < (References.WIDTH/(Chunk.TILE_AMOUNT_X*Tile.TILE_SIZE_X))){
		
			while(chunkY-1 < (References.HEIGHT/(Chunk.TILE_AMOUNT_Y*Tile.TILE_SIZE_Y))){
				loadedChunks.add(new Chunk(chunkX, chunkY));
				chunkY++;
			}
			chunkY = 0;
			chunkX++;
		}
	
	if(References.DEBUG)
		System.out.println("Loaded Chunks contains " + loadedChunks.size());
		
		for(Chunk chunk : loadedChunks){
		
			for(int i = 0; i < Chunk.TILE_AMOUNT_X*Chunk.TILE_AMOUNT_Y; i++){
				Tile tile = Tile.getTileGrass();
				tile.setPosition((i%Tile.TILE_SIZE_X)*Tile.TILE_SIZE_X + Chunk.TILE_AMOUNT_X*Tile.TILE_SIZE_X*chunk.getChunkX(),
						(int)(Math.floor(i/Tile.TILE_SIZE_Y)*Tile.TILE_SIZE_Y) + Chunk.TILE_AMOUNT_Y*Tile.TILE_SIZE_Y*chunk.getChunkY(), this);
				tempTiles.add(tile);
			}
			if(References.DEBUG)
				Utilities.printf("Tile amount is %s", tempTiles.size());
		
			chunk.setTiles(tempTiles);
		
			if(chunk.getTiles() != null)
				loadedTiles.addAll(chunk.getTiles());
		
			if(chunk.getChunkX() %2 == 0 && chunk.getChunkY() %2 != 0){
				chunk.setColor(Color.GRAY);
			} else if(chunk.getChunkX() %2 != 0 && chunk.getChunkY() %2 == 0 ){
				chunk.setColor(Color.GRAY);
			} else chunk.setColor(Color.WHITE);
			
			tempTiles.clear();
		}
	}
	}
	
	public void setTileAt(int at, Tile tile){
		getTiles().set(at, new HealingTile(getTiles().get(at).getPosX(), getTiles().get(at).getPosY(), world));
	}

	public List<Tile> getTiles() {
		return loadedTiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.loadedTiles = tiles;
	}

	public List<Chunk> getChunks() {
		return loadedChunks;
	}

	public void setChunks(List<Chunk> chunks) {
		this.loadedChunks = chunks;
	}
	
	public Chunk getContainingChunk(int posX, int posY){
		for(Chunk chunk : loadedChunks){
			if(chunk.isInChunk(posX, posY))
				return chunk;
		}
		System.out.println("Returning null");
		return null;
	}
	
	public void setTileAt(int posX, int posY, Tile t1){
		int i = -1;
		Tile tile = t1;
		
		for(Tile t0 : loadedTiles){
			i++;
			if(t0.isInTile(posX, posY)){
				tile.setPosition(t0.getPosX(), t0.getPosY(), this);
				if(References.DEBUG)
					Utilities.printf("Setting position of tile to %s, %s", tile.getPosX(), tile.getPosY());
				break;
			}
		}
		
		loadedTiles.set(i, tile);
	}
	
	public Tile getTileAt(int posX, int posY){
		for(Tile tile : loadedTiles){
			if(tile.isInTile(posX, posY))
				return tile;
		}
		return null;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public void registerMap(){
		world.registermap(this);
	}

	public Biome getBiome() {
		return biome;
	}

	public void setBiome(Biome biome) {
		this.biome = biome;
	}

}
