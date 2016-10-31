//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbą atliko: Emilijus Stankus
//
//=========================================================================

package Block;

import BlockInterfaces.*;
import java.awt.Color;
import java.awt.Graphics;

public class Block extends GameObject implements Positionable, Cloneable{
	final int HEIGHT = 10;
	final int WIDTH = 10;

	private int colorNumber;

	public Block(int posX, Color color, int colorNumber) {
		super();
		this.posX = posX;
		this.posY = 50;
		this.color = color;
		this.colorNumber = colorNumber;
	}

	@Override
	public Object clone() {
		Block b = null;
		try {
			b = (Block)super.clone();
		} catch(Exception e){}
		b.color = new Color(b.color.getRGB());
		return b;
	}

	public void paint(Graphics g) {
		g.setColor(color);
        g.fillRect(posX, posY, WIDTH, HEIGHT);
	}

	public void println() {
			System.out.print("height: " + HEIGHT + ", width: " + WIDTH + ", ");
			super.println();
	}

	public void updatePosY(double latency, int speed) {
		posY += speed * latency;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getColorNumber() {
		return colorNumber;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setColorNumber(int colorNumber) {
		this.colorNumber = colorNumber;
	}


}



