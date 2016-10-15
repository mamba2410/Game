package callummcgregor.common.entity;

import static callummcgregor.lib.References.Direction.EAST;
import static callummcgregor.lib.References.Direction.NORTH;
import static callummcgregor.lib.References.Direction.SOUTH;
import static callummcgregor.lib.References.Direction.WEST;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import callummcgregor.common.Game;
import callummcgregor.common.entity.damage.DamageSource;
import callummcgregor.common.entity.damage.DamageSource.DamageType;
import callummcgregor.common.entity.projectile.EnemyProjectile;
import callummcgregor.common.entity.projectile.PlayerProjectile;
import callummcgregor.common.terrain.Chunk;
import callummcgregor.common.terrain.Tile;
import callummcgregor.common.terrain.World;
import callummcgregor.common.terrain.World.Biome;
import callummcgregor.lib.References;
import callummcgregor.lib.References.Direction;
import callummcgregor.lib.References.EntityType;

public abstract class EntityBase {
	
	protected Game game = References.GAME;
		
	protected int maxHealth;
	private int health;
	private Rectangle hitbox;
	private int attackDamage = 1;
	private Color entityColor = Color.BLACK;
	private int damageWaitTime = 30;
	private int damageCooldown = 1;
	protected boolean isInvincible = false;
	protected boolean canChangeDirection = true;

	protected World world;
	protected EntityType entityType;
	public float knockbackModifier = 1f;
	private EntityBase trackingEntity;
	private Tile tileOn;
	private List<Biome> spawnableBiomes;
	private int teleportDelay = 0;
	private boolean shouldDrawHealth = true;
	protected int ticksLeft = -1;
	public int shootCooldown = 0;
	
	private Direction direction = SOUTH;
	private int range = 30;
	private int attackRange = 30;

	public int posX = 0;
	public int posY = 0;
	public int tileX = 0;
	public int tileY = 0;
	public int chunkX = 0;
	public int chunkY = 0;
	
	public EntityBase(Rectangle hitbox){
		spawnableBiomes = new ArrayList<Biome>();
		setHitbox(hitbox);
		this.health = maxHealth;
		entityType = EntityType.UNDEFINED;
		addSpawnableBiomes(Biome.EARTH);
	}
	
	public EntityBase(int widthX, int lengthY){
		this(new Rectangle(widthX, lengthY));
	}
	
	public void shootProjectile(World world){
		shootCooldown = 60;
		System.out.println("Direction is " + direction);
		if(this.getEntityType() == EntityType.PLAYER){
			world.spawnEntity(new PlayerProjectile(direction), (direction == EAST)?getMaxX():getMinX()+5, (direction == SOUTH)?getMaxY():getMinY()+5);
		} else {
			world.spawnEntity(new EnemyProjectile(direction, this.getEntityAttackDamage()), (direction == EAST)?getMaxX():getMinX()+5, (direction == SOUTH)?getMaxY():getMinY()+5);
		}
	}
	
	public int getOutsideBounds(Direction d){
		
		switch(d){
		case EAST: return (int) (this.posX + this.getHitbox().getWidth());
		case WEST: return this.posX;
		case NORTH: return this.posY;
		case SOUTH: return (int) (this.posY + this.getHitbox().getHeight());
		}
		
		return 0;
	}
	
	public Chunk getChunk(){
		for(Chunk chunk : world.loadedChunks){
			if(world.isInChunk(chunk, posX, posY)){
				return chunk;
			}
		}
		return null;
	}
	
	public void setTrackingEntity(EntityBase eb){
		trackingEntity = eb;
	}
	
	public EntityBase getTrackingEntity(){
		return trackingEntity;
	}
	
	public void tickEntity(World w){
		this.world = w;
		if(posX <= 0) knockbackEntity(2, EAST);
		if(posY <= 0) knockbackEntity(2, SOUTH);
		
		if(shootCooldown > 0)shootCooldown--;
		
		if(ticksLeft > 0) ticksLeft--;
		else if(ticksLeft == 0) this.setDead();
		
		if(damageCooldown > 0 && !isInvincible) damageCooldown--;
		if(teleportDelay > 0) teleportDelay--;
		
		if(posX > 0 && posY > 0){
			tileX = world.getTileAt(posX, posY).getPosX();
			tileY = world.getTileAt(posX, posY).getPosY();
		}
		setTileOn(world.getTileAt(posX, posY));
	}
	
	public void onTileCollide(Tile tile){}
	
	public int getMaxHealth(){ return maxHealth; }
	
	public int getHealth(){ return health; }
	
	public Rectangle getHitbox(){
		return new Rectangle(posX, posY, hitbox.width, hitbox.height);
	}
	
	public void setMaxHealth(int health){ this.maxHealth = health; this.health = health;}
	
	public void damageEntity(DamageSource source){
		if(damageCooldown == 0){
			health -= source.damage;
			damageCooldown = damageWaitTime;
			switch(source.directionFrom){
			
			case SOUTH:{
				knockbackEntity((int)(knockbackModifier*source.getKnockbackAmount()), SOUTH);
				if(References.DEBUG) System.out.println("Knocking back NORTH");
				break;
			}
			case EAST:{
				knockbackEntity((int)(knockbackModifier*source.getKnockbackAmount()), EAST); 
				if(References.DEBUG) System.out.println("Knocking back WEST");
				break;
			}
				
			case NORTH:{
				knockbackEntity((int)(knockbackModifier*source.getKnockbackAmount()), NORTH);
				if(References.DEBUG) System.out.println("Knocking back SOUTH");
				break;
			}
				
			case WEST:{
				knockbackEntity((int)(knockbackModifier*source.getKnockbackAmount()), WEST);
				if(References.DEBUG) System.out.println("Knocking back EAST");
				break;
			}
				
			}
		}
	}
	
	public void knockbackEntity(int pixels, Direction dir){
		
		//System.out.println(String.format("Knocking back entity at %d, %d", posX, posY));
		
		switch(dir){
		case SOUTH: posY += pixels; break;
		case EAST: posX += pixels; break;
		case NORTH: posY -= pixels; break;
		case WEST: posX -= pixels; break;
		}
		
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public void setDirection(Direction direction){
		//System.out.println("Setting direction to " + String.valueOf(direction));
		if(canChangeDirection)
			this.direction = direction;
	}
	
	public Direction getDirection(){ return direction; }
	
	public void moveEntity(int posX, int posY){
				if(posX > this.posX) setDirection(EAST);
		else 	if(posX < this.posX) setDirection(WEST);
		else 	if(posY > this.posY) setDirection(SOUTH);
		else 	if(posY < this.posY) setDirection(NORTH);
				
		if(world != null){
			int[] pos = world.clampToWorld(posX, posY);
			this.posX = pos[0];
			this.posY = pos[1];
		} else {
			this.posX = posX;
			this.posY = posY;
		}
		
	}
	
	public void moveEntity(Direction direction, int pixels){
		switch(direction){
		case SOUTH: posY += pixels; break;
		case EAST: posX += pixels; break;
		case NORTH: posY -= pixels; break;
		case WEST: posX -= pixels; break;
		}
		
		int[] pos = world.clampToWorld(posX,  posY);
		posX = pos[0];
		posY = pos[1];
		
		setDirection(direction);
	}
	
	public void moveEntity(Direction d1, int pixels1, Direction d2, int pixels2){
		if(d1 != null){
			moveEntity(d1, pixels1);
			setDirection(d1);
		} else setDirection(d2);
		
		if(d2 != null)
			moveEntity(d2, pixels2);
		
		int[] pos = world.clampToWorld(posX,  posY);
		posX = pos[0];
		posY = pos[1];
	}
	
	public boolean attemptAttack(){
		if(!world.getEntitiesInRange(this, getAttackRange(), direction).isEmpty()){;
			for(EntityBase e : world.getEntitiesInRange(this, getAttackRange(), direction)){
				if(e != this)
					e.damageEntity(new DamageSource(getEntityAttackDamage(), getDirection(), DamageType.Entity));
			}
			return !world.getEntitiesInRange(this, getAttackRange(), getDirection()).isEmpty();
		} else {
			return false;
		}
		
	}

	public Color getEntityColor() { return entityColor; }

	public void setEntityColor(Color entityColor) { this.entityColor = entityColor; }
	
	public int getEntityAttackDamage(){ return attackDamage; }
	
	public void setEntityAttackDamage(int attackDamage){ this.attackDamage = attackDamage; }
	
	public void onCollide(EntityBase entity){
		if(References.DEBUG)
			System.out.println(String.format("Collision detected at %s, %s", posX, posY));
		moveEntity(world.getOppositeDirection(world.getDirectionForEntity(this, entity)), 2);
	}
	
	public int getRange(){ return this.range; }
	
	public EntityType getEntityType(){ return entityType; }
	
	public void setEntityType(EntityType type){ this.entityType = type; }

	public int getDamageCooldown() {
		return damageCooldown;
	}

	public void setDamageCooldown(int damageCooldown) {
		this.damageCooldown = damageCooldown;
	}
	
	public void setDead(){
		this.health = 0;
	}
	
	public void destroyEntity(){
		world.removeEntity(this);
	}
	
	public void setHitbox(Rectangle rect){
		this.hitbox = rect;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getMaxX(){
		return posX+hitbox.width;
	}
	
	public int getMinX(){
		return posX;
	}
	
	public int getMaxY(){
		return posY+hitbox.height;
	}
	
	public int getMinY(){
		return posY;
	}
	
	public boolean intersects(int x, int y){
		if(x >= getMinX() && x <= getMaxX()){
			if(y >= getMinY() && y <= getMaxY()){
				return true;
			}
		}
		return false;
	}
	
	public boolean intersects(Rectangle rect){
		return getHitbox().intersects(rect);
	}
	
	public int[] getMiddleOfEntity(){
		int[] middle = new int[2];
		middle[0] = posX+(getHitbox().width/2);
		middle[1] = posY+(getHitbox().height/2);
		return middle;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}
	
	public void healEntity(int health){
		this.health += health;
		if(this.health > getMaxHealth())
			setHealth(getMaxHealth());
	}

	public Tile getTileOn() {
		return tileOn;
	}

	public void setTileOn(Tile tileOn) {
		this.tileOn = tileOn;
	}

	public List<Biome> getSpawnableBiomes() {
		return spawnableBiomes;
	}

	public void setSpawnableBiomes(List<Biome> spawnableBiomes) {
		this.spawnableBiomes = spawnableBiomes;
	}
	
	public void addSpawnableBiomes(Biome biome){
		spawnableBiomes.add(biome);
	}
	
	public void removeSpawnableBiomes(Biome biome){
		spawnableBiomes.remove(biome);
	}

	public int getTeleportDelay() {
		return teleportDelay;
	}

	public void setTeleportDelay(int teleportDelay) {
		this.teleportDelay = teleportDelay;
	}

	public boolean shouldDrawHealth() {
		return shouldDrawHealth;
	}

	public void setShouldDrawHealth(boolean shouldDrawHealth) {
		this.shouldDrawHealth = shouldDrawHealth;
	}
	
	public boolean isInvincible() {
		return isInvincible;
	}

	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}
	
	protected void overrideChangeDirection(Direction d){
		this.direction = d;
	}

}
