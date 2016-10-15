package callummcgregor.common.terrain.tile;

import java.awt.Color;
import java.awt.Rectangle;

import callummcgregor.common.entity.EntityBase;
import callummcgregor.common.terrain.TerrainMap;
import callummcgregor.common.terrain.Tile;
import callummcgregor.common.terrain.World;

public class TileDoor extends Tile {
	
	private int[] linkedTo = {0, 0};
	private TerrainMap nextMap;
	private String mapName;

	public TileDoor(String name) {
		super(Material.rock, name, false);
		hitbox = new Rectangle(getPosX(), getPosY(), TILE_SIZE_X, TILE_SIZE_Y);
	}
	
	public TileDoor(String name, int[] linkedTo, String mapName){
		this(name);
		setLinkedTo(linkedTo);
		setMapName(mapName);
	}
	
	@Override
	public void tickTile(World world) {
		super.tickTile(world);
		if(world.ticksExisted%60 == 0){
			for(EntityBase eb : world.activeEntities){
				if(eb.intersects(hitbox) && eb.equals(world.player)){
					if(eb.getTeleportDelay() == 0){
						world.setCurMap(mapName);
						world.player.moveEntity(linkedTo[0], linkedTo[1]);
						world.player.setTeleportDelay(900);
					}
				}
			}
		}
	}
	
	@Override
	public Color getColor() {
		return new Color(0.3f, 0.2f, 0.1f);
	}

	public int[] getLinkedTo() {
		return linkedTo;
	}

	public void setLinkedTo(int[] linkedTo) {
		this.linkedTo = linkedTo;
	}

	public TerrainMap getNextMap() {
		return nextMap;
	}

	public void setNextMap(TerrainMap nextMap) {
		this.nextMap = nextMap;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

}
