package callummcgregor;

import static callummcgregor.lib.References.GAME;
import static callummcgregor.lib.References.HEIGHT;
import static callummcgregor.lib.References.WIDTH;
import static callummcgregor.lib.References.keysDown;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import callummcgregor.common.Game;
import callummcgregor.lib.Listeners;
import callummcgregor.lib.References;

public class GameEntry {
	
	public static void main(String[] args){
		
		for(String string : args){
			if(string == "-debug") References.DEBUG = true;
		}
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				new GameEntry();
			}
		});
		
		//new GameEntry();
	}
	
	public GameEntry(){
		
		Listeners listener = new Listeners();
		keysDown = new ArrayList<Integer>();
		
		Dimension FULLSCREEN = Toolkit.getDefaultToolkit().getScreenSize();
		WIDTH = FULLSCREEN.width;
		HEIGHT = FULLSCREEN.height;
		
		GAME = new Game();
		GAME.visuals = GAME.new Visuals();
		GAME.thread.start();
		
		JFrame frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setSize(FULLSCREEN);
		frame.setResizable(false);
		frame.addKeyListener(listener);
		frame.addMouseListener(listener);
		frame.addMouseMotionListener(listener);
		
		frame.add(GAME.visuals);
		
		frame.setVisible(true);
		
		frame.requestFocus();
		frame.setLocationRelativeTo(null);
	}

}
