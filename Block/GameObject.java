//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbą atliko: Emilijus Stankus
//
//=========================================================================

package Block;

import java.awt.Color;
import java.io.Serializable;
import BlockInterfaces.*;

abstract class GameObject implements Paintable, Serializable{
	public static int OBJECT_COUNT = 0;
	int posX;
	int posY;
	Color color;

	public GameObject() {
		++OBJECT_COUNT;
	}

	public final String toString() {
		return super.toString() + "\nNumber of objects: " + OBJECT_COUNT;
	}

	public void println() {
		System.out.println("posX: " + posX + ", posY: " + posY + ", color: " + color.toString());
	}
}