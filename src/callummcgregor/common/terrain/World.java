package callummcgregor.common.terrain;

import static callummcgregor.lib.References.Direction.EAST;
import static callummcgregor.lib.References.Direction.NORTH;
import static callummcgregor.lib.References.Direction.SOUTH;
import static callummcgregor.lib.References.Direction.WEST;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import callummcgregor.common.Game.Difficulty;
import callummcgregor.common.entity.EnemyBlobs;
import callummcgregor.common.entity.EnemyHellBlob;
import callummcgregor.common.entity.EnemyHerogen;
import callummcgregor.common.entity.EntityBase;
import callummcgregor.common.entity.EntityEnemy;
import callummcgregor.common.entity.IJoinable;
import callummcgregor.common.entity.Player;
import callummcgregor.common.terrain.Tile.Material;
import callummcgregor.common.terrain.maps.DefaultMap;
import callummcgregor.common.terrain.maps.DefaultMap2;
import callummcgregor.common.terrain.maps.HellMap;
import callummcgregor.common.terrain.tile.HealingTile;
import callummcgregor.lib.References;
import callummcgregor.lib.References.Direction;
import callummcgregor.lib.Utilities;

public class World {
	
	public long ticksExisted;
	
	private Random random = new Random();
	private HashMap<String, TerrainMap> registeredMaps;
	
	public Player player;
	public List<EntityBase> activeEntities;
	public List<EntityEnemy> activeEnemies;
	public List<EntityBase> removingEntities;
	public List<EnemyBlobs> separatingBlobs;
	public List<EntityBase> spawningEntities;
	public static World instance;
	private boolean loaded = false;
	public List<Chunk> loadedChunks;
	public List<Tile> loadedTiles;
	private long spawnTimer = 0;
	private Difficulty difficulty = Difficulty.PEACEFUL;
	private TerrainMap curMap;
	private TerrainMap oldMap;
	private Biome biome;
	
	public World(Player player){
		loaded = false;
		instance = this;
		registeredMaps = new HashMap<String, TerrainMap>();
		activeEntities = new ArrayList<EntityBase>();
		activeEnemies = new ArrayList<EntityEnemy>();
		removingEntities = new ArrayList<EntityBase>();
		separatingBlobs = new ArrayList<EnemyBlobs>();
		spawningEntities = new ArrayList<EntityBase>();
		loadedChunks = new ArrayList<Chunk>();
		loadedTiles = new ArrayList<Tile>();
		
		registermap(new DefaultMap(instance));
		registermap(new DefaultMap2(instance));
		registermap(new HellMap(instance));
		
		setCurMap(getMap("DefaultMap"));
		worldStateChanged();
		
		this.player = player;
		spawnEntity(this.player, References.WIDTH/2, References.HEIGHT/2);
		loaded = true;
		if(References.DEBUG)
			Utilities.printf("Map list contains %s", registeredMaps.keySet());
	}
	
	public void worldStateChanged(){
		
		if(oldMap != null){
			activeEntities.remove(player);
			oldMap.entities.addAll(activeEntities);
			activeEntities.clear();
			activeEntities.add(player);
		}
		
		oldMap = curMap;
		activeEntities.addAll(curMap.entities);
		loadChunks();
	}
	
	public void loadChunks(){
		if(getCurMap() != null){
			loadedChunks = getCurMap().getChunks();
			loadedTiles = getCurMap().getTiles();
			setBiome(getCurMap().getBiome());
		} else {
			if(References.DEBUG)
				Utilities.printf("Loading backup chunks as current map is null");
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
			
				for(int i = 0; i < Chunk.TILE_AMOUNT_X*Chunk.TILE_AMOUNT_Y; i++)
					tempTiles.add(new Tile(Material.rock, "stone", chunk.getTilePos(i)[0], chunk.getTilePos(i)[1], instance));
			
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
		
			loadedTiles.set(278, new HealingTile(loadedTiles.get(278).getPosX(), loadedTiles.get(278).getPosY(), instance));
			loadedTiles.get(365).setWall(true);
		}
		
	}
	
	public void worldTick(){
		if(loaded){
			ticksExisted++;
			if(difficulty == Difficulty.NORMAL && activeEntities.size() <= 50)
				spawnTimer++;
			if(!activeEntities.contains(player)){}
			
			for(EntityBase e : activeEntities){
				e.tickEntity(instance);
				if(e.getHealth() <= 0){
					if(e instanceof EnemyBlobs){
						EnemyBlobs b = (EnemyBlobs)e;
						if(b.getBlobJoins() > 1){
							separatingBlobs.add(b);
						} else {
							removingEntities.add(e);
						}
					} else removingEntities.add(e);
				}
			}
			
			checkForCollisions();
			
			for(Tile tile : loadedTiles) tile.tickTile(instance);
			
			if(spawnTimer >= 600){
				int x, y;
				x = random.nextInt(References.WIDTH);
				y = random.nextInt(References.HEIGHT);
				
				if(!getTileAt(x, y).isWall()) spawnEntity(new EnemyBlobs((byte)1), x, y);
				x = random.nextInt(References.WIDTH);
				y = random.nextInt(References.HEIGHT);
				if(!getTileAt(x, y).isWall()) spawnEntity(new EnemyHerogen(16, 16), x, y);
				x = random.nextInt(References.WIDTH);
				y = random.nextInt(References.HEIGHT);
				if(!getTileAt(x, y).isWall()) spawnEntity(new EnemyHellBlob((byte)1), x, y);
				spawnTimer = 0;
			}
			
			for(EnemyBlobs b : separatingBlobs){
				b.separateBlobs();
			} separatingBlobs.clear();
			
			for(EntityBase eb : removingEntities){
				activeEntities.remove(eb);
				if(eb instanceof EntityEnemy)
					activeEnemies.remove(eb);
			} removingEntities.clear();
			
			for(EntityBase eb : spawningEntities){
				spawnEntity(eb);
			} spawningEntities.clear();
			
			for(Chunk chunk : loadedChunks){
				for(EntityBase eb : activeEntities){
					if(chunk.isInChunk(eb)){
						eb.chunkX = chunk.getChunkX();
						eb.chunkY = chunk.getChunkY();
					}
				}
			}
			
			if(oldMap != curMap){
				worldStateChanged();
			}
			
		}
	}
	
	public int[] clampToWorld(int x, int y){
		int[] ret = {0, 0};
		if(x >= References.WIDTH) ret[0] = References.WIDTH-1;
		else if( x <= 0) ret[0] = 0;
		else ret[0] = x;
		
		if(y >= References.HEIGHT) ret[1] = References.HEIGHT-1;
		else if(y <= 0) ret[1] = 0;
		else ret[1] = y;
		
		return ret;
	}
	
	public Direction getPreferredDirection(EntityBase from, EntityBase to){
		if(Utilities.getPositiveDifference(from.posX, to.posX) > Utilities.getPositiveDifference(from.posY, to.posY)){
			if(from.posX > to.posX){
				if(References.DEBUG)
					Utilities.printf("Returning WEST");
				return WEST;
			}
			else {
				if(References.DEBUG)
					Utilities.printf("Returning EAST");
				return EAST;
			}
		} else if(Utilities.getPositiveDifference(from.posX, to.posX) < Utilities.getPositiveDifference(from.posY, to.posY)) {
			if(from.posY > to.posY){
				if(References.DEBUG)
					Utilities.printf("Returning NORTH");
				return NORTH;
			}
			else {
				if(References.DEBUG)
					Utilities.printf("Returning SOUTH");
				return SOUTH;
			}
		} else {
			if(from.posX > to.posX){
				if(References.DEBUG)
					Utilities.printf("Returning WEST");
				return WEST;
			}
			else {
				if(References.DEBUG)
					Utilities.printf("Returning EAST");
				return EAST;
			}
		}
	}
	
	public Chunk getContainingChunk(int posX, int posY){
		for(Chunk chunk : loadedChunks){
			if(chunk.isInChunk(posX, posY))
				return chunk;
		}
		System.out.println("Returning null");
		return null;
	}
	
	public boolean isInChunk(Chunk chunk, int posX, int posY){
		if(posX > chunk.getMinX() && posX < chunk.getMaxX()){
			if(posY > chunk.getMinY() && posY < chunk.getMaxY()){
				return true;
			}
		}
		return false;
	}
	
	public Chunk getChunkAt(int posX, int posY){
		for(Chunk chunk : loadedChunks){
			if(chunk.getChunkX() == posX && chunk.getChunkY() == posY)
				return chunk;
		}
		return null;
	}
	
	public void removeEntity(EntityBase entity){
		removingEntities.add(entity);
	}
	
	private void spawnEntity(EntityBase entity){
		if(entity instanceof EntityEnemy){
			activeEnemies.add((EntityEnemy)entity);
			activeEntities.add((EntityEnemy)entity);
		} else activeEntities.add(entity);
	}
	
	public void spawnEntity(EntityBase entity, int x, int y){
		entity.moveEntity(x, y);
		if(entity.getSpawnableBiomes().contains(this.curMap.getBiome()))
		spawningEntities.add(entity);
	}
	
	public List<EntityBase> getEntitiesInRange(EntityBase eb, int range, Direction direction){
		List<EntityBase> entities = scanForEntities(eb);
		List<EntityBase> inRange = new ArrayList<EntityBase>();
		for(EntityBase e : entities){
			if(e != eb){
				Rectangle checker;
				switch(direction){
				
				case SOUTH:{
					checker = new Rectangle(eb.posX-range, eb.posY, (range*2)+eb.getHitbox().width, range+eb.getHitbox().height); break;
				}
				case EAST:{
					checker = new Rectangle(eb.posX+eb.getHitbox().width, eb.posY-range, range, (range*2)+eb.getHitbox().height); break;
				}
				case NORTH:{
					checker = new Rectangle(eb.posX-range, eb.posY-range, (range*2)+eb.getHitbox().width, range); break;
				}
				case WEST:{
					checker = new Rectangle(eb.posX-range, eb.posY-range, range, (range*2)+eb.getHitbox().height); break;
				}
				
				default: checker = null; break;
				}
				
				if(e.intersects(checker)) inRange.add(e);
				
			}
		}
		return inRange;
	}
	
	public List<EntityBase> scanForEntities(EntityBase eb){
		List<EntityBase> entities = new ArrayList<EntityBase>();
		Rectangle rect = new Rectangle(eb.posX-eb.getRange(), eb.posY-eb.getRange(),
				eb.getRange()*2+eb.getHitbox().width, eb.getRange()*2+eb.getHitbox().height);
		for(EntityBase e : activeEntities){
			if(e.intersects(rect) && e != eb)
				entities.add(e);
		}
		
		return entities;
	}
	
	public double getDistance(int x1, int y1, int x2, int y2){
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	public void checkForCollisions(){
		for(EntityBase e : activeEntities){
			for(EntityBase e1 : activeEntities){
				if(e != e1){
					//if(e.getEntityType() != EntityType.PROJECTILE && e.getEntityType() != EntityType.FRIENDLY_PROJECTILE
							//&& e1.getEntityType() != EntityType.PROJECTILE && e1.getEntityType() != EntityType.FRIENDLY_PROJECTILE)
					if(e.getHitbox().intersects(e1.getHitbox())){
						e.onCollide(e1);
						e1.onCollide(e);
					}
				}
			}
		}
	}
	
	public Direction getDirectionForEntity(EntityBase from, EntityBase to){
		if(to.posX > from.posX) return EAST;
		else if(to.posX < from.posX) return WEST;
		else if(to.posY > from.posY) return SOUTH;
		else return NORTH;
	}
	
	public Direction[] getRelativeDirections(EntityBase from, EntityBase to){
		Direction[] da = new Direction[2];
		if(to.getMiddleOfEntity()[0] > from.getMiddleOfEntity()[0]) da[0] = EAST;
		else if(to.getMiddleOfEntity()[0] < from.getMiddleOfEntity()[0]) da[0] = WEST;
		else da[0] = null;
		
		if(to.getMiddleOfEntity()[1] > from.getMiddleOfEntity()[1]) da[1] = SOUTH;
		else if(to.getMiddleOfEntity()[1] < from.getMiddleOfEntity()[1]) da[1] = NORTH;
		else da[1] = null;
		
		return da;
	}
	
	public Direction getOppositeDirection(Direction d){
		switch(d){
		case SOUTH: return NORTH;
		case EAST: return WEST;
		case NORTH: return SOUTH;
		case WEST: return EAST;
		default: return null;
		}
	}
	
	public EntityBase getClosestEntity(EntityBase from){
		int shortestDistance = References.WIDTH;
		EntityBase closestEntity = from;
		List<EntityBase> entities = scanForEntities(from);
		for(EntityBase e : entities){
			if(getDistance(from.posX, from.posY, e.posX, e.posY) < shortestDistance){
				closestEntity = e;
				shortestDistance = (int)getDistance(from.posX, from.posY, e.posX, e.posY);
			}
		}
		
		return closestEntity;
	}
	
	public void combineEntities(IJoinable e1, IJoinable e2){
		if(e1.getJoinCooldown() == 0 && e2.getJoinCooldown() == 0){
			if(e1 instanceof EnemyBlobs && e2 instanceof EnemyBlobs){
				EnemyBlobs eb1 = (EnemyBlobs)e1;
				EnemyBlobs eb2 = (EnemyBlobs)e2;
				if(eb1.getBlobJoins() == eb2.getBlobJoins()){
					eb2.destroyEntity();
					eb1.setBlobJoins((byte)(eb1.getBlobJoins()+1));
					eb1.setHealth(eb1.getHealth()+eb2.getHealth());
					eb1.setJoinCooldown(600);
				}
			}
		}
	}
	
	public int getDistanceX(EntityBase e1, EntityBase e2){
		return (int)(Math.sqrt((e1.posX*e2.posX)));
	}
	
	public int getDistanceY(EntityBase e1, EntityBase e2){
		return (int)(Math.sqrt((e1.posY*e2.posY)));
	}
	
	public Chunk getContainingChunk(int listPos){
		return loadedChunks.get(listPos/(Chunk.TILE_AMOUNT_X*Chunk.TILE_AMOUNT_Y) + 1);
	}
	
	public Tile getTileAt(int posX, int posY){
		for(Tile tile : loadedTiles){
			if(tile.isInTile(posX, posY))
				return tile;
		}
		return null;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public TerrainMap getCurMap() {
		return curMap;
	}

	public void setCurMap(TerrainMap curMap) {
		if(curMap != null)
		this.curMap = curMap;
	}
	
	public void setCurMap(String name){
		if(registeredMaps.containsKey(name)){
			this.curMap = registeredMaps.get(name);
		}
	}
	
	public void registermap(TerrainMap map){
		if(!registeredMaps.containsKey(map.mapName))
			registeredMaps.put(map.mapName, map);
	}
	
	public TerrainMap getMap(String name){
		if(registeredMaps.containsKey(name))
			return registeredMaps.get(name);
		else return null;
	}
	
	public Biome getBiome() {
		return biome;
	}

	public void setBiome(Biome biome) {
		this.biome = biome;
	}

	public static enum Biome{
		EARTH, HELL, SNOW
	}

}
