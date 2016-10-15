package callummcgregor.common.util;

public class ButtonPosition {
	
	private int screenX, screenY;
	
	public ButtonPosition(int x, int y){
		setScreenX(x);
		setScreenY(y);
	}

	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

}
