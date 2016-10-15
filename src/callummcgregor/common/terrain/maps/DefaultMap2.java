package callummcgregor.common.terrain.maps;

import callummcgregor.common.terrain.TerrainMap;
import callummcgregor.common.terrain.Tile;
import callummcgregor.common.terrain.World;
import callummcgregor.common.terrain.World.Biome;
import callummcgregor.common.terrain.tile.TileDoor;
import callummcgregor.lib.References;

public class DefaultMap2 extends TerrainMap{
	
	public DefaultMap2(World world) {
		super(world, "DefaultMap2", Biome.EARTH);
		
		setTileAt(300, 300, Tile.getTileHealing());
		setTileAt(300, 400, Tile.getTileHealing());
		for(int i = 0; i < References.WIDTH; i+= 16){
			setTileAt(i, 100, Tile.getTileDirt());
			setTileAt(i, 116, Tile.getTileDirt());
			setTileAt(i, 132, Tile.getTileDirt());
		}
		
		setTileAt(0, 200, new TileDoor("tile_door", new int[] {References.WIDTH-16, 200}, "DefaultMap"));
		setTileAt(365, 420, Tile.getTileRock());
	}
	
	public static String getName() {
		return "DefaultMap2";
	}

}
