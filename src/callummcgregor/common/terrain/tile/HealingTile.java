package callummcgregor.common.terrain.tile;

import callummcgregor.common.entity.EntityBase;
import callummcgregor.common.terrain.Tile;
import callummcgregor.common.terrain.World;

public class HealingTile extends Tile{

	public HealingTile(int posX, int posY, World world) {
		super(Material.healing, "healing_tile", posX, posY, world);
	}
	
	public HealingTile(Material mat, String name, boolean isWall){
		super(mat, name, isWall);
	}
	
	@Override
	public void tickTile(World world) {
		super.tickTile(world);
		if(world.ticksExisted%60 == 0){
			for(EntityBase eb : world.activeEntities){
				if(eb.intersects(hitbox)){
					eb.healEntity(1);
				}
			}
		}
	}

}
