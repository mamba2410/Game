package callummcgregor.client.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import callummcgregor.common.util.ButtonPosition;

public class MenuButton {
	
	String text;
	Rectangle bounds;
	ButtonPosition pos;
	
	public MenuButton(String text, Rectangle bounds, ButtonPosition pos){
		this.text = text;
		this.bounds = bounds;
		this.pos = pos;
	}
	
	public void render(Graphics2D g){
		g.fillRect(pos.getScreenX(), pos.getScreenY(), bounds.width, bounds.height);
		g.setColor(Color.WHITE);
		g.drawString(text, pos.getScreenX(), pos.getScreenY()+bounds.height);
	}
	
	

}
