package callummcgregor.common;

import callummcgregor.lib.References;

public class GameThread extends Thread implements Runnable{
	
	boolean running = true;
	
	@Override
	public void run(){
		long SUSPECTEDTPS = 60, SUSPECTEDFPS = 60;
		long time = System.nanoTime();
		long TPSWAIT = 1000000000/SUSPECTEDTPS;
		long FPSWAIT = 1000000000/SUSPECTEDFPS;
		long nextTick = time+TPSWAIT;
		long nextFrame = time+FPSWAIT;
		
		int MAXFRAMESKIPS = 5;
		int loops = 0;
		
		long tps = 0, fps = 0;
		
		if(References.DEBUG)
			System.out.println(String.format("Suspected Tps: %s, Tps Wait: %s, Next Tick: %s", SUSPECTEDTPS, TPSWAIT, nextTick));
		
		while(running){
			
			while(System.nanoTime() >= nextTick && loops < MAXFRAMESKIPS){
				nextTick += TPSWAIT;
				tps++;
				References.GAME.tick();
				loops++;
			}
			
			if(System.nanoTime() >= nextFrame){
				nextFrame += FPSWAIT;
				fps++;
				References.GAME.visuals.repaint();
				loops = 0;
			}
			
			if(System.nanoTime() >= (time+1000000000)){
				time += 1000000000;
				if(References.DEBUG)
					System.out.println(String.format("Tps: %s, FPS: %s", tps, fps));
				References.g_tps = tps;
				References.g_fps = fps;
				tps = fps = 0;
				loops = 0;
				if(References.DEBUG)
					System.out.println(String.format("Width: %s, Height: %s", References.WIDTH, References.HEIGHT));
			}
			
			
		}
		
		
	}

}
