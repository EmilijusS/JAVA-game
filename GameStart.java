//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbà atliko: Emilijus Stankus
//
//=========================================================================


import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class GameStart {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }

    private static void createAndShowGUI() {
        Game game = new Game();
        JFrame frame = new JFrame("Þaidimas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        // Open window in centre of the screen
        frame.setLocationRelativeTo(null);

        game.start();
    }
}