package callummcgregor.common.terrain.maps;

import callummcgregor.common.terrain.TerrainMap;
import callummcgregor.common.terrain.Tile;
import callummcgregor.common.terrain.World;
import callummcgregor.common.terrain.World.Biome;
import callummcgregor.common.terrain.tile.TileDoor;
import callummcgregor.lib.References;

public class HellMap extends TerrainMap {
	
	public HellMap(World world) {
		super(world, "HellMap", Biome.HELL);
		
		int i = -1;
		for(Tile tile : loadedTiles){
			i++;
			Tile t0 = Tile.getHellRock();
			t0.setPosition(tile.getPosX(), tile.getPosY(), this);
			loadedTiles.set(i, t0);
		}
		
		setTileAt(0, 200, new TileDoor("tile_door", new int[] {References.WIDTH-16, 200}, "DefaultMap"));
	}

}
