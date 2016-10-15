package callummcgregor.common.entity;

import java.awt.Color;
import java.awt.Rectangle;

import callummcgregor.common.entity.damage.DamageSource;
import callummcgregor.common.entity.damage.DamageSource.DamageType;
import callummcgregor.lib.References.EntityType;

public class Player extends EntityBase{

	public Player(Rectangle hitbox) {
		super(hitbox);
		setMaxHealth(20);
		setRange(40);
		setAttackRange(40);
		setEntityType(EntityType.PLAYER);
	}
	
	public Player(int health, int widthX, int lengthY){
		this(new Rectangle(widthX, lengthY));
	}
	
	public void onCollide(EntityBase entity){
		if(entity instanceof EntityEnemy){
			EntityEnemy e = (EntityEnemy)entity;
			damageEntity(new DamageSource(e.getEntityAttackDamage(), e.getDirection(), DamageType.Entity));
		}
	}
	
	public Color getEntityColor(){
		return Color.ORANGE;
	}
	
	

}
