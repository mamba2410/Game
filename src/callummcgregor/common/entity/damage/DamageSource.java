package callummcgregor.common.entity.damage;

import callummcgregor.lib.References.Direction;

public class DamageSource {
	
	public int damage;
	public Direction directionFrom;
	public DamageType type;
	
	public DamageSource(int damage, Direction directionRelativeToDamage, DamageType type){
		this.damage = damage;
		this.directionFrom = directionRelativeToDamage;
		this.type = type;
	}
	
	public enum DamageType{
		Entity, Projectile, Fire
	}
	
	public int getKnockbackAmount(){
		switch(type){
		case Entity: return 30;
		case Projectile: return 15;
		case Fire: return 0;
		default: return 0;
		}
	}

}
