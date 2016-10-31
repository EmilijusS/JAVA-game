//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbą atliko: Emilijus Stankus
//
//=========================================================================

package GameException;

public class GameException extends Exception {
	public String getErrorMessage() {
		return "The game cannot continue";
	}
}