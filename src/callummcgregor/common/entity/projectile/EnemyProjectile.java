package callummcgregor.common.entity.projectile;

import java.awt.Color;
import java.awt.Rectangle;

import callummcgregor.common.entity.EntityProjectile;
import callummcgregor.lib.References.Direction;
import callummcgregor.lib.References.EntityType;

public class EnemyProjectile extends EntityProjectile{

	public EnemyProjectile(Direction d, int damage) {
		super(new Rectangle(4, 4), d, damage);
		setEntityType(EntityType.PROJECTILE);
	}
	
	@Override
	public Color getEntityColor() {
		return Color.BLACK;
	}

}
