package callummcgregor.common.entity;

import java.awt.Rectangle;

import callummcgregor.common.terrain.World;
import callummcgregor.lib.References.EntityType;

public class EntityEnemy extends EntityBase{

	public EntityEnemy(int widthX, int lengthY) {
		this(new Rectangle(widthX, lengthY));
	}
	
	public EntityEnemy(Rectangle hitbox){
		super(hitbox);
		entityType = EntityType.ENEMY;
	}
	
	public void updateAI(){
		if(!( world.scanForEntities(this) .isEmpty())){
			if(world.scanForEntities(this).contains(world.player))
				setTrackingEntity(world.player);
			else setTrackingEntity(null);
		}
		
		if(getTrackingEntity() != null){
			moveEntity(world.getPreferredDirection(this, getTrackingEntity()), 1);
		}
	}
	
	public void tickEntity(World w) {
		super.tickEntity(w);
		updateAI();
	}

}
