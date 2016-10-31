//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbą atliko: Emilijus Stankus
//
//=========================================================================

package Block;

import java.awt.Color;
import java.awt.Graphics;

import GameException.*;

public class Catcher extends GameObject{

	final int HEIGHT = 30;
	final int SPEED = 5;
	private static boolean catcherExists = false;
	private int SECTIONS;
	private int WIDTH;
	private int posY;
	// Which section gets split
	private int split;
	private double posX;

	private Color[] colors;

	private Catcher(int width, int height, int sections, Color[] colors) {
		WIDTH = width;
		posY = height - HEIGHT;
		posX = 0;
		SECTIONS = sections;
		this.colors = colors;
	}

	public static Catcher getCatcher(int width, int height, int sections, Color[] colors) throws CatcherAlreadyExistsException{
		if(!catcherExists) {
			return new Catcher(width, height, sections, colors);
		} else
			throw new CatcherAlreadyExistsException();
	}

	public void paint(Graphics g) {
		int currentPos = (int)posX;

		// Paints all the sections separately
		for(int i = 0; i < SECTIONS; ++i) {
			g.setColor(colors[i]);

			if(currentPos + WIDTH / SECTIONS <= WIDTH) {
				g.fillRect(currentPos, posY, WIDTH / SECTIONS, HEIGHT);
			} else {
				// This section got split
				g.fillRect(currentPos, posY, WIDTH - currentPos, HEIGHT);
				g.fillRect(0, posY, WIDTH / SECTIONS - (WIDTH - currentPos), HEIGHT);
				split = i;
			}

			currentPos += WIDTH / SECTIONS;

			if(currentPos > WIDTH) {
				currentPos -= WIDTH;
			}
		}
	}

	public void moveRight(double latency) {
		posX += SPEED * latency;

		if(posX >= WIDTH) {
			posX -= WIDTH;
		}
	}

	public void moveLeft(double latency) {
		posX -= SPEED * latency;

		if(posX < 0) {
			posX += WIDTH;
		}
	}

	public int getPosX() {
		return (int)posX;
	}

	public void setPosX(int posX) {
		this.posX = (double)posX;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public int getSplit() {
		return split;
	}

	public int getColorNumberAtX(int posX) {
		if(posX < (int)this.posX) {
			posX += WIDTH;
		}

		return (posX - (int)this.posX) / (WIDTH / SECTIONS);
	}
}