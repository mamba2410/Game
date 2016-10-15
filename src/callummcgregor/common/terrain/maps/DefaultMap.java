package callummcgregor.common.terrain.maps;

import callummcgregor.common.terrain.TerrainMap;
import callummcgregor.common.terrain.Tile;
import callummcgregor.common.terrain.World;
import callummcgregor.common.terrain.World.Biome;
import callummcgregor.common.terrain.tile.TileDoor;
import callummcgregor.lib.References;

public class DefaultMap extends TerrainMap {

	public DefaultMap(World world) {
		super(world, "DefaultMap", Biome.EARTH);
		
		setTileAt(300, 300, Tile.getTileHealing());
		setTileAt(300, 400, Tile.getTileHealing());
		
		for(int i = 0; i < References.WIDTH; i+= 16){
			setTileAt(i, 100, Tile.getTileDirt());
			setTileAt(i, 116, Tile.getTileDirt());
			setTileAt(i, 132, Tile.getTileDirt());
		}
		
		for(int x = 64; x < 196; x += 16){
			for(int y = 224; y < 544; y += 16){
				setTileAt(x, y, Tile.getTileRock());
				getTileAt(x, y).setWall(true);
			}
		}
		
		setTileAt(20, 200, new TileDoor("tile_door", new int[] {References.WIDTH-16, 200}, "DefaultMap2"));
		setTileAt(300, 64, new TileDoor("tile_door", new int[] {References.WIDTH/2, References.HEIGHT/2}, "HellMap"));
	}

	public static String getName() {
		return "DefaultMap";
	}
	
	

}
