package callummcgregor.common;

import static callummcgregor.lib.References.GAME;
import static callummcgregor.lib.References.HEIGHT;
import static callummcgregor.lib.References.WIDTH;
import static callummcgregor.lib.References.WORLD;
import static callummcgregor.lib.References.g_fps;
import static callummcgregor.lib.References.g_tps;
import static callummcgregor.lib.References.hasMovementButton;
import static callummcgregor.lib.References.keysDown;
import static callummcgregor.lib.References.mouseX;
import static callummcgregor.lib.References.mouseY;
import static callummcgregor.lib.References.movementKey;
import static callummcgregor.lib.References.Direction.EAST;
import static callummcgregor.lib.References.Direction.NORTH;
import static callummcgregor.lib.References.Direction.SOUTH;
import static callummcgregor.lib.References.Direction.WEST;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import callummcgregor.client.gui.MenuButton;
import callummcgregor.common.entity.EnemyBlobs;
import callummcgregor.common.entity.EnemyHerogen;
import callummcgregor.common.entity.EntityBase;
import callummcgregor.common.entity.Player;
import callummcgregor.common.terrain.Chunk;
import callummcgregor.common.terrain.Tile;
import callummcgregor.common.terrain.World;
import callummcgregor.common.util.ButtonPosition;
import callummcgregor.lib.Listeners;
import callummcgregor.lib.References;
import callummcgregor.lib.References.DirectionKey;
import callummcgregor.lib.References.GAMESTATE;

public class Game {
	
	public long ticksExisted = 0;
	public boolean drawDebugGui = false;
	
	public Random entityRandom = new Random(References.ENTITYSEED);
	
	public Visuals visuals;
	public GameThread thread;
	
	public Player player;
	public World world;
	
	public GAMESTATE state;
	
	public MenuButton buttonStart;
	public SavesHandler saves;
	
	public Game(){
		GAME = this;
		//saves = new SavesHandler();
		state = GAMESTATE.PLAYING;
		player = new Player(20, 16, 16);
		
		world = new World(player);
		WORLD = world;
		
		buttonStart = new MenuButton("START", new Rectangle(100, 50), new ButtonPosition(500, 200));
		//saves.createSave("First");
		
		thread = new GameThread();
	}
	
	public void tick(){
		ticksExisted++;
		hasMovementButton = Listeners.dirKeysDown.size()>0;
		if(hasMovementButton){
			movementKey = Listeners.dirKeysDown.get(Listeners.dirKeysDown.size()-1);
			if(movementKey == DirectionKey.UP) player.setDirection(NORTH);
			if(movementKey == DirectionKey.LEFT) player.setDirection(WEST);
			if(movementKey == DirectionKey.DOWN) player.setDirection(SOUTH);
			if(movementKey == DirectionKey.RIGHT) player.setDirection(EAST);
			
			player.moveEntity(player.getDirection(), 2);
		}
		
		if(keysDown.contains(KeyEvent.VK_Q)) player.attemptAttack();
		if(keysDown.contains(KeyEvent.VK_F) && player.shootCooldown == 0) player.shootProjectile(world);
		if(keysDown.contains(KeyEvent.VK_E) && ticksExisted%15==0){ world.spawnEntity(new EnemyHerogen(16, 16), entityRandom.nextInt(WIDTH), entityRandom.nextInt(HEIGHT)); }
		if(keysDown.contains(KeyEvent.VK_R) && ticksExisted%15==0){ world.spawnEntity(new EnemyBlobs((byte)1), entityRandom.nextInt(WIDTH), entityRandom.nextInt(HEIGHT)); }

		if(state == GAMESTATE.PLAYING)
			world.worldTick();
		
		if(!(world.activeEntities.contains(player)))
			state = GAMESTATE.DEATH;
		
	}
	
	public class Visuals extends JPanel{
		
		private static final long serialVersionUID = -5553321555607785494L;
		public int spaceY = 15;

		public Visuals(){}
		
		public void paintComponent(Graphics g){
			g.setColor(Color.BLACK);
			render((Graphics2D)g);
		}
		
		public void render(Graphics2D g){
				g.clearRect(0, 0, References.WIDTH, References.HEIGHT);
				
				if(state == GAMESTATE.PLAYING){
					for(Chunk chunk : WORLD.loadedChunks){
						g.setColor(chunk.getColor());
						g.fillRect(chunk.getChunkX()*Chunk.TILE_AMOUNT_X*Tile.TILE_SIZE_X,
								chunk.getChunkY()*Chunk.TILE_AMOUNT_Y*Tile.TILE_SIZE_Y,
								Chunk.TILE_AMOUNT_X*Tile.TILE_SIZE_X,
								Chunk.TILE_AMOUNT_Y*Tile.TILE_SIZE_Y);
					}
					
					for(Tile tile : WORLD.loadedTiles){
						g.setColor(tile.getColor());
						g.fillRect(tile.getPosX(), tile.getPosY(), Tile.TILE_SIZE_X, Tile.TILE_SIZE_Y);
						if(tile.isWall()){
							g.setColor(tile.material.getWallColor());
							
							g.setStroke(new BasicStroke(2));
							g.drawRect(tile.getPosX(), tile.getPosY(), Tile.TILE_SIZE_X-1, Tile.TILE_SIZE_Y-1);
							g.setStroke(new BasicStroke(1));
						}
					}
					
				for(EntityBase e : world.activeEntities){
					g.setColor(e.getEntityColor());
					if(e.shouldDrawHealth())
						g.drawString(String.valueOf(e.getHealth()), e.posX, e.posY);
					if(GAME.drawDebugGui)
						drawDirection(g, e);
					g.fillRect(e.posX, e.posY, e.getHitbox().width, e.getHitbox().height);
					g.setColor(Color.RED);
					if(e.getDamageCooldown() > 0 && !e.isInvincible())
						g.drawRect(e.posX, e.posY, e.getHitbox().width, e.getHitbox().height);
					
					}
					g.setColor(Color.WHITE);
					g.fillRect(126, 19, player.getHealth()*17+2, 18);
					int heartSpaceX = 128;
					for(int i = 0; i < player.getHealth(); i++){
						drawHeart(g, heartSpaceX, 20, Color.RED);
						heartSpaceX += 17;
					
				}
				
				g.setColor(Color.BLACK);
				
				if(GAME.drawDebugGui)
					drawDebugGui(g);
				
				}
				
				if(state == GAMESTATE.TITLE_SCREEN){
					buttonStart.render(g);
				}
				
				if(state == GAMESTATE.DEATH){
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, WIDTH, HEIGHT);
					g.setColor(Color.BLACK);
					g.drawString("YOU DIED", 
							(int)(WIDTH/2),
							(int)(HEIGHT/2));
				}
				
		}
		
		public void drawHeart(Graphics2D g, int x, int y, Color color){
			g.setColor(color);
			g.fillRect(x+1, y, 5, 12);
			g.fillRect(x+10, y, 5, 12);
			g.fillRect(x+6, y+4, 4, 12);
			g.fillRect(x, y+2, 1, 6);
			g.fillRect(x+15, y+2, 1, 6);
			g.fillRect(x+3, y+12, 10, 2);
			g.fillRect(x+6, y+2, 1, 2);
			g.fillRect(x+9, y+2, 1, 2);
			g.setColor(Color.BLACK);
		}
		
		public int getLinePoint(EntityBase e){
			switch(e.getDirection()){
			case SOUTH: return e.posY + 10;
			case EAST: return e.posX + 10;
			case NORTH: return e.posY - 10;
			case WEST: return e.posX - 10;
			}
			return e.posX;
		}
		
		public void drawDirection(Graphics2D g, EntityBase e){
			if(e.getDirection() != null){
				switch(e.getDirection()){
				case SOUTH: g.drawLine(e.posX, e.posY, e.posX, e.posY+e.getHitbox().height+10); break;
				case EAST: g.drawLine(e.posX, e.posY, e.posX+e.getHitbox().width+10, e.posY); break;
				case NORTH: g.drawLine(e.posX, e.posY, e.posX, e.posY-10); break;
				case WEST: g.drawLine(e.posX, e.posY, e.posX-10, e.posY); break;
				}
			}
		}
		
		public void drawDebugGui(Graphics2D g){
			spaceY = 15;
			drawString(g, "FPS: " + g_fps);
			drawString(g, "TPS: " + g_tps);
			spaceY+=15;
			drawString(g, "Difficulty: " + WORLD.getDifficulty());
			drawString(g, "Player X: " + player.posX);
			drawString(g, "Player Y: " + player.posY);
			drawString(g, "Player Tile X: " + player.tileX);
			drawString(g, "Player Tile Y: " + player.tileY);
			drawString(g, "Player Chunk X: " + player.chunkX);
			drawString(g, "Player Chunk Y: " + player.chunkY);
			drawString(g, "Direction: " + player.getDirection());
			drawString(g, "Health: " + player.getHealth());
			spaceY+=15;
			drawString(g, "Mouse X: " + mouseX);
			drawString(g, "Mouse Y: " + mouseY);
			drawString(g, "MovementKey: " + movementKey);
			spaceY+=15;
			drawString(g, "Keys Down:");
			drawList(g, "Key: ", keysDown);
			drawString(g, "DirKeysDown: ");
			drawList(g, "DirKeys: ", Listeners.dirKeysDown);
		}
		
		public void drawString(Graphics2D g, String string){
			g.setColor(Color.WHITE);
			g.fillRect(9, spaceY-12, (int) (g.getFontMetrics().getStringBounds(string, g).getWidth()+2),  15);
			g.setColor(Color.BLACK);
			g.drawString(string, 10, spaceY);
			spaceY += 15;
		}
		
		public void drawList(Graphics2D g, String string, List<?> list){
			for(Object i : list){
				drawString(g, string + i);
				if(References.DEBUG)
					System.out.println("Contains key: " + i);
			}
			
		}
		
	}
	
	public enum Difficulty {
		PEACEFUL, NORMAL
	}
	
	

}
