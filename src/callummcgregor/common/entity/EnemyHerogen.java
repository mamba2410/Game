package callummcgregor.common.entity;

import java.awt.Color;
import java.awt.Rectangle;

public class EnemyHerogen extends EntityEnemy{

	public EnemyHerogen(int widthX, int lengthY) {
		this(new Rectangle(widthX, lengthY));
		setEntityAttackDamage(2);
		setMaxHealth(10);
		setRange(200);
	}
	
	public EnemyHerogen(Rectangle hitbox){ super(hitbox); }
	
	public Color getEntityColor(){
		return Color.GREEN;
	}
	
	

}
