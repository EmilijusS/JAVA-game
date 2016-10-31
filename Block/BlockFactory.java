//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbą atliko: Emilijus Stankus
//
//=========================================================================

package Block;

import java.awt.Color;
import java.util.Random;

public class BlockFactory {
	private final int EVENT_PROBABILITY = 25;

	private int WIDTH;
	private int DIFF_COLORS;
	private Color[] colors;
	private Random random;

	public BlockFactory(int width, int diff_colors, Color[] colors) {
		WIDTH = width;
		DIFF_COLORS = diff_colors;
		this.colors = colors;
		random = new Random();
	}

	public Block getBlock() {
		int colorNumber, posX;

		colorNumber = random.nextInt(DIFF_COLORS);
		// Avoiding sides of screen
		posX = random.nextInt(WIDTH - 20) + 10;

		return new Block(posX, colors[colorNumber], colorNumber);
	}

	public Block specialBlock(Block orig) {

		Block copy = (Block)orig.clone();
		if(copy.getPosX() < WIDTH - (WIDTH / DIFF_COLORS) - 10) {
			copy.setPosX(copy.getPosX() + WIDTH / DIFF_COLORS - 30 + random.nextInt(60));
			copy.setColorNumber((copy.getColorNumber() + 1) % DIFF_COLORS);
			copy.setColor(colors[copy.getColorNumber()]);
		} else {
			copy.setPosX(copy.getPosX() - WIDTH / DIFF_COLORS + 30 - random.nextInt(60));
			copy.setColorNumber((copy.getColorNumber() - 1 + DIFF_COLORS) % DIFF_COLORS);
			copy.setColor(colors[copy.getColorNumber()]);
		}

		return copy;
	}

	public boolean specialEvent() {
		return random.nextInt(100) < EVENT_PROBABILITY;
	}
}