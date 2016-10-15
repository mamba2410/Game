package callummcgregor.common.entity.projectile;

import java.awt.Color;
import java.awt.Rectangle;

import callummcgregor.common.entity.EntityBase;
import callummcgregor.common.entity.EntityEnemy;
import callummcgregor.common.entity.EntityProjectile;
import callummcgregor.common.entity.damage.DamageSource;
import callummcgregor.common.entity.damage.DamageSource.DamageType;
import callummcgregor.lib.References.Direction;
import callummcgregor.lib.References.EntityType;

public class PlayerProjectile extends EntityProjectile {
	
	public PlayerProjectile(Direction d) {
		super(new Rectangle(4, 4), d, 2);
		setDirection(d);
		entityType = EntityType.FRIENDLY_PROJECTILE;
		setEntityColor(Color.ORANGE);
	}
	
	@Override
	public void onCollide(EntityBase entity) {
		System.out.println("Colliding with stuff");
		if(entity instanceof EntityEnemy){
			EntityEnemy e = (EntityEnemy)entity;
			e.damageEntity(new DamageSource(this.getEntityAttackDamage(), this.getDirection(), DamageType.Projectile));
			this.setDead();
		}
	}
	
	public Color getEntityColor(){
		return Color.ORANGE;
	}
}
