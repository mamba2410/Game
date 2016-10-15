package callummcgregor.common.entity;

import java.awt.Color;
import java.awt.Rectangle;

import callummcgregor.common.terrain.World;
import callummcgregor.lib.References.Direction;
import callummcgregor.lib.References.EntityType;

public abstract class EntityProjectile extends EntityBase{

	public EntityProjectile(Rectangle hitbox, Direction d, int damage) {
		super(hitbox);
		this.isInvincible = true;
		setMaxHealth(1);
		overrideChangeDirection(d);
		setEntityAttackDamage(damage);
		entityType = EntityType.PROJECTILE;
		setEntityColor(Color.BLACK);
		setShouldDrawHealth(false);
		this.knockbackModifier = 0;
		this.ticksLeft = 60*10;
		this.canChangeDirection = false;
	}
	
	@Override
	public void tickEntity(World w) {
		super.tickEntity(w);
		moveEntity(getDirection(), 2);
		
	}
	
	@Override
	public void onCollide(EntityBase entity) {}
	
	@Override
	public void knockbackEntity(int pixels, Direction dir){}
	
	
	
	
	
	

}
