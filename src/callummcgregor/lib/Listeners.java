package callummcgregor.lib;

import static callummcgregor.lib.References.DEBUG;
import static callummcgregor.lib.References.GAME;
import static callummcgregor.lib.References.WORLD;
import static callummcgregor.lib.References.keysDown;
import static callummcgregor.lib.References.mouseX;
import static callummcgregor.lib.References.mouseY;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import callummcgregor.common.Game.Difficulty;
import callummcgregor.lib.References.DirectionKey;
import callummcgregor.lib.References.GAMESTATE;

public class Listeners implements KeyListener, MouseMotionListener, MouseListener {
	
	public static List<DirectionKey> dirKeysDown = new ArrayList<DirectionKey>();

	@Override
	public void mouseClicked(MouseEvent e) {
		References.GAME.state = GAMESTATE.PLAYING;
		if(DEBUG)
			System.out.println("Mouse Clicked");
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_W){if(!dirKeysDown.contains(DirectionKey.UP))dirKeysDown.add(DirectionKey.UP); }
		if(code == KeyEvent.VK_A){if(!dirKeysDown.contains(DirectionKey.LEFT))dirKeysDown.add(DirectionKey.LEFT); }
		if(code == KeyEvent.VK_S){if(!dirKeysDown.contains(DirectionKey.DOWN))dirKeysDown.add(DirectionKey.DOWN); }
		if(code == KeyEvent.VK_D){if(!dirKeysDown.contains(DirectionKey.RIGHT))dirKeysDown.add(DirectionKey.RIGHT); }
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		else{
			if(!(keysDown.contains(e.getKeyCode()))){
					keysDown.add(e.getKeyCode());
					if(References.DEBUG)
						System.out.println("Adding " + e.getKeyCode());
			}
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_W){
			if(dirKeysDown.contains(DirectionKey.UP))
				dirKeysDown.remove((DirectionKey)DirectionKey.UP);
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			if(dirKeysDown.contains(DirectionKey.LEFT))
				dirKeysDown.remove((DirectionKey)DirectionKey.LEFT);
		}
		if(e.getKeyCode() == KeyEvent.VK_S){
			if(dirKeysDown.contains(DirectionKey.DOWN))
				dirKeysDown.remove((DirectionKey)DirectionKey.DOWN);
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			if(dirKeysDown.contains(DirectionKey.RIGHT))
				dirKeysDown.remove((DirectionKey)DirectionKey.RIGHT);
		}
		
		if(keysDown.contains((Integer)e.getKeyCode())){
			keysDown.remove((Integer)e.getKeyCode());
			if(References.DEBUG)
				System.out.println("Removing " + e.getKeyCode());
		}
		
		
		
		if(e.getKeyCode() == KeyEvent.VK_F3)
			GAME.drawDebugGui = !GAME.drawDebugGui;
		if(e.getKeyCode() == KeyEvent.VK_F2){
			if(WORLD.getDifficulty() == Difficulty.NORMAL)
				WORLD.setDifficulty(Difficulty.PEACEFUL);
			else if(WORLD.getDifficulty() == Difficulty.PEACEFUL)
				WORLD.setDifficulty(Difficulty.NORMAL);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent paramKeyEvent) {}

}
