package callummcgregor.common.entity;

import java.awt.Color;
import java.awt.Rectangle;

import callummcgregor.common.terrain.World;

public class EnemyBlobs extends EntityEnemy implements IJoinable{
	
	private byte blobJoins = 1;
	private int joinCooldown = 0;
	
	private static Rectangle hitbox_ = new Rectangle(8, 8);

	public EnemyBlobs(byte size) {
		super(new Rectangle((int)(hitbox_.width*Math.pow(2, size)), (int)(hitbox_.height*Math.pow(2, size))));
		setBlobJoins(size);
		setEntityColor(Color.BLUE);
		setMaxHealth(10);
		setEntityAttackDamage(1);
		setRange(200);
	}
	
	@Override
	public void tickEntity(World w) {
		if(joinCooldown > 0) joinCooldown--;
		super.tickEntity(w);
	}
	
	@Override
	public void onCollide(EntityBase entity) {
		if(entity instanceof EnemyBlobs){
			EnemyBlobs e = (EnemyBlobs)entity;
			if(getJoinCooldown() == 0){
				if(e.getBlobJoins() == this.blobJoins && e.getHealth() != 0){
					world.combineEntities(this, e);
				} else super.onCollide(entity);
			} else super.onCollide(entity);
		} else super.onCollide(entity);
	}

	public byte getBlobJoins() {
		return blobJoins;
	}

	public void setBlobJoins(byte blobJoins) {
		this.blobJoins = blobJoins;
		setHitbox(new Rectangle((int)(hitbox_.width*Math.pow(2, blobJoins)), (int)(hitbox_.height*Math.pow(2, blobJoins))));
	}
	
	public void separateBlobs(){
		setBlobJoins((byte)(getBlobJoins()-1));
		EnemyBlobs b = new EnemyBlobs((byte)(getBlobJoins()));
		b.setHealth(b.getMaxHealth());
		world.spawnEntity(b,
				(int)(posX + this.getHitbox().getWidth() + 10), 
				(int)(posY + this.getHitbox().getHeight() + 10));
		setHealth(getMaxHealth());
		setJoinCooldown(600);
		b.setJoinCooldown(600);
	}

	@Override
	public int getJoinCooldown(){
		return joinCooldown;
	}

	@Override
	public void setJoinCooldown(int joinCooldown){
		this.joinCooldown = joinCooldown;
		
	}

}
