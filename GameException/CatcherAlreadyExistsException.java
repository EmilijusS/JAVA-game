//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbą atliko: Emilijus Stankus
//
//=========================================================================

package GameException;

public class CatcherAlreadyExistsException extends GameException {
	@Override
	public String getErrorMessage() {
		return "Catcher already exists, thus a new one cannot be created.\n" + super.getErrorMessage();
	}
}