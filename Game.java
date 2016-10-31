//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbà atliko: Emilijus Stankus
//
//=========================================================================

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.LinkedList;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import Block.*;
import GameMusic.*;
import GameException.*;

public class Game extends JPanel implements ActionListener, KeyListener{

	// This must be divisible by DIFF_COLORS
	private final int WINDOW_WIDTH = 4 * 100;
	private final int WINDOW_HEIGHT = 600;
	private final int MENU_BUTTON_WIDTH = 300;
	private final int MENU_BUTTON_HEIGHT = 80;
	private final int MAIN_MENU = 0;
	private final int GAME = 1;
	private final int ESC_MENU = 2;
	private final int END_MENU = 3;
	private final int LOADING_GAME = 4;
	private final int SAVING_GAME = 5;
	private final int TIME_PER_FRAME = 1000 / 60;
	private final int DIFF_COLORS = 4;
	private final int BLOCK_SPEED = 5;
	private final int START_BLOCK_INTEVAL = 1000;
	private final Color[] COLORS = {new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255),
								new Color(255, 128, 0)};

	private final String STR_FILE_NAME = "savedGame.ser";
	private final String STR_PLAY = "Þaisti";
	private final String STR_CONTINUE = "Tæsti pradëtà ";
	private final String STR_LOADING_ERROR = "Klaida ákraunant duomenis";
	private final String STR_SAVE = "Iðsaugoti";
	private final String STR_SUCCESSFUL_SAVING = "Sëkmingai iðsaugota";
	private final String STR_SAVING_ERROR = "Klaida bandant iðsaugoti";
	private final String STR_PLAY_AGAIN = "Þaisti vël";

	private int gameState;
	
	private long time;
	private int elapsedTime;

	private boolean leftArrowPressed = false;
	private boolean rightArrowPressed = false;
	private boolean mouseInsideButton1 = false;
	private boolean mouseInsideButton2 = false;

	private boolean unsuccessfulLoading = false;
	private boolean unsuccessfulSaving = false;
	private boolean successfulSaving = false;

	private int menuButtonPosX;
	private int menuButton1PosY;
	private int menuButton2PosY;

	private int timeSinceLastBlock = 0;
	private int blockInterval;

	private int score = 0;

	private boolean repainted = true;

	private Timer timer;
	private Color[] colors;
	private BlockFactory blockFactory;
    private LinkedList <Block> blocks = new LinkedList <Block>();
    private Catcher catcher;
    private Font scoreFont = new Font("Helvetica", Font.BOLD, 50);
    private Font menuFont = new Font("Helvetica", Font.BOLD, 40);
    private Font errorFont = new Font("Helvetica", Font.PLAIN, 30);
    private FontMetrics scoreFontMetrics;
    private FontMetrics menuFontMetrics;
    private FontMetrics errorFontMetrics;

    // Initializes game window
    public Game() {

    	GameMouseAdapter g = new GameMouseAdapter();

        setBorder(BorderFactory.createLineBorder(Color.black));

        addKeyListener(this);
        addMouseListener(g);
        addMouseMotionListener(g);
        setBackground(Color.white);
        setFocusable(true);
        requestFocusInWindow();
    }

    // Initializes the game
    public void start() {
    	gameState = MAIN_MENU;
    	time = System.currentTimeMillis() - TIME_PER_FRAME;
    	blockFactory = new BlockFactory(WINDOW_WIDTH, DIFF_COLORS, COLORS);
    	try {
    		catcher = Catcher.getCatcher(WINDOW_WIDTH, WINDOW_HEIGHT, DIFF_COLORS, COLORS);
    	} catch(CatcherAlreadyExistsException e) {
    		System.out.println(e.getErrorMessage());
    		return;
    	}
    	
    	elapsedTime = 1000 / 60;
    	menuButtonPosX = (WINDOW_WIDTH - MENU_BUTTON_WIDTH) / 2;
    	menuButton1PosY = 100;
    	menuButton2PosY = 100 + menuButton1PosY + MENU_BUTTON_HEIGHT;
    	scoreFontMetrics = getFontMetrics(scoreFont);
    	menuFontMetrics = getFontMetrics(menuFont);
    	errorFontMetrics = getFontMetrics(errorFont);
    	new Thread(new GameMusic()).start();


    	timer = new Timer(TIME_PER_FRAME, this);
    	timer.start();
    }

    private void playGame() {
    	blocks.clear();
    	score = 0;
    	blockInterval = START_BLOCK_INTEVAL;
    	unsuccessfulLoading = false;
    	catcher.setPosX(0);
    	gameState = GAME;
    }

    private void loadGame() {

    	try {
    		int blockQTY;

	    	FileInputStream fis = new FileInputStream(STR_FILE_NAME);
	    	ObjectInputStream ois = new ObjectInputStream(fis);

	    	catcher = (Catcher)ois.readObject();
	    	blockQTY = ois.readInt();

	    	for(int i = 0; i < blockQTY; ++i)
	    		blocks.add((Block)ois.readObject());

	    	score = ois.readInt();
	    	blockInterval = ois.readInt();

	    	ois.close();
	    	fis.close();

	    	gameState = GAME;
    	} catch(Exception e) {
    		unsuccessfulLoading = true;
    	}

    }

    private void saveGame() {
    	try {
	    	FileOutputStream fos = new FileOutputStream(STR_FILE_NAME);
	    	ObjectOutputStream oos = new ObjectOutputStream(fos);

	    	oos.writeObject(catcher);
	    	oos.writeInt(blocks.size());
	    	for(int i = 0; i < blocks.size(); ++i)
	    		oos.writeObject(blocks.get(i));
	    	oos.writeInt(score);
	    	oos.writeInt(blockInterval);

	    	oos.close();
	    	fos.close();
	    	unsuccessfulSaving = false;
	    	successfulSaving = true;
    	} catch(Exception e) {
    		successfulSaving = false;
    		unsuccessfulSaving = true;
    	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    	if(gameState == GAME)
    		update();

    	repaint();
    }

    // Updates positions and states of all game objects
    private void update() {
    	double latency = elapsedTime / TIME_PER_FRAME;
    	timeSinceLastBlock += elapsedTime;
    	Block lastBlock;

    	if(blocks.size() > 0) {
    		lastBlock = blocks.getFirst();
    		// Check, if the last block has fallen down
    		if(lastBlock.getPosY() + lastBlock.getHeight() >= WINDOW_HEIGHT - catcher.getHeight()) {
	    		// Did it fall to the same color
	    		if(lastBlock.getColorNumber() == catcher.getColorNumberAtX(lastBlock.getPosX()) &&
	    		lastBlock.getColorNumber() == catcher.getColorNumberAtX(lastBlock.getPosX() + lastBlock.getWidth())) {
	    			++score;
	    			blocks.removeFirst();
	    			blockInterval -= 5;	
	    		} else {
	    		// The game is lost
	    		gameState = END_MENU;
	    		return;
	    		}
    		}
    	} 
    	
    	// Update positions of all the blocks
    	// Maybe change to for each later
    	for(int i = 0; i < blocks.size(); ++i) {
    		blocks.get(i).updatePosY(latency, BLOCK_SPEED);
    	}

    	// Add a new block if necessary
       	if(timeSinceLastBlock >= blockInterval) {
    		blocks.add(blockFactory.getBlock());

    		if(blockFactory.specialEvent())
    			blocks.add(blockFactory.specialBlock(blocks.getLast()));

    		timeSinceLastBlock = 0;
    	}

    	// Update position of catcher
    	if(leftArrowPressed)
    		catcher.moveLeft(latency);

    	if(rightArrowPressed)
    		catcher.moveRight(latency);
    		
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Determine what to paint
        switch(gameState) {
        	case MAIN_MENU:
        		paintMainMenu(g);
        		break;
        	case GAME:
        		paintGame(g);
        		break;
        	case ESC_MENU:
        		paintEscMenu(g);
        		break;
        	case END_MENU:
        		paintEndMenu(g);
        		break;
        }

        repainted = true;
    }

    private void paintGame(Graphics g) {
    	catcher.paint(g);

    	for(int i = 0; i < blocks.size(); ++i) {
    		blocks.get(i).paint(g);
    	}

    	String scoreSTR = String.valueOf(score);

        g.setColor(Color.black);
        g.setFont(scoreFont);
        g.drawString(scoreSTR, (WINDOW_WIDTH - scoreFontMetrics.stringWidth(scoreSTR)) / 2, 40);
    }

    private void paintMainMenu(Graphics g) {

    	paintMenuButton1(g, STR_PLAY);
    	paintMenuButton2(g, STR_CONTINUE);

    	if(unsuccessfulLoading) {
    		paintInfo(g, STR_LOADING_ERROR, false);
    	}

    }

    private void paintEscMenu(Graphics g) {

    	paintMenuButton1(g, STR_SAVE);

    	if(successfulSaving) {
    		paintInfo(g, STR_SUCCESSFUL_SAVING, true);
    	}
    	
    	if(unsuccessfulSaving) {
    		paintInfo(g, STR_SAVING_ERROR, false);
    	}
    }

    private void paintEndMenu(Graphics g) {
    	String scoreSTR = String.valueOf(score);

    	g.setColor(Color.black);
        g.setFont(scoreFont);
        g.drawString(scoreSTR, (WINDOW_WIDTH - scoreFontMetrics.stringWidth(scoreSTR)) / 2, 100);

        paintMenuButton2(g, STR_PLAY_AGAIN);
    	
    }

    private void paintMenuButton1(Graphics g, String text) {

    	if(mouseInsideButton1) {
    		g.setColor(Color.black);
    		g.fillRect(menuButtonPosX, menuButton1PosY, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
    		g.setColor(Color.white);
    		g.setFont(menuFont);
    		g.drawString(text, (WINDOW_WIDTH - menuFontMetrics.stringWidth(text)) / 2, menuButton1PosY + MENU_BUTTON_HEIGHT / 2 + menuFontMetrics.getHeight() / 2);
    	} else {
    		g.setColor(Color.black);
    		g.drawRect(menuButtonPosX, menuButton1PosY, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
    		g.setFont(menuFont);
    		g.drawString(text, (WINDOW_WIDTH - menuFontMetrics.stringWidth(text)) / 2, menuButton1PosY + MENU_BUTTON_HEIGHT / 2 + menuFontMetrics.getHeight() / 2);
    	}
    }

    private void paintMenuButton2(Graphics g, String text) {
    	if(mouseInsideButton2) {
    		g.setColor(Color.black);
    		g.fillRect(menuButtonPosX, menuButton2PosY, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
    		g.setColor(Color.white);
    		g.setFont(menuFont);
    		g.drawString(text, (WINDOW_WIDTH - menuFontMetrics.stringWidth(text)) / 2, menuButton2PosY + MENU_BUTTON_HEIGHT / 2 + menuFontMetrics.getHeight() / 2);
    	} else {
    		g.setColor(Color.black);
    		g.drawRect(menuButtonPosX, menuButton2PosY, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
    		g.setFont(menuFont);
    		g.drawString(text, (WINDOW_WIDTH - menuFontMetrics.stringWidth(text)) / 2, menuButton2PosY + MENU_BUTTON_HEIGHT / 2 + menuFontMetrics.getHeight() / 2);
    	}
    }

    private void paintInfo(Graphics g, String text, boolean type) {
    	if(type == true) {
    		g.setColor(Color.green);
    	} else {
    		g.setColor(Color.red);
    	}

    	g.setFont(errorFont);
    	g.drawString(text, (WINDOW_WIDTH - errorFontMetrics.stringWidth(text)) / 2, WINDOW_HEIGHT - 50);
    }

    // Set window size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

   	public void keyPressed(KeyEvent e) {
   		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
   			rightArrowPressed = true;

    	if(e.getKeyCode() == KeyEvent.VK_LEFT)
   			leftArrowPressed = true;

   		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
    		if(gameState == GAME)
    			gameState = ESC_MENU;
    		else if(gameState == ESC_MENU) {
    			gameState = GAME;
    			successfulSaving = false;
    			unsuccessfulSaving = false;	
    		}
    	}
   	}

   	public void keyReleased(KeyEvent e) {
   		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
   			rightArrowPressed = false;

   		if(e.getKeyCode() == KeyEvent.VK_LEFT)
   			leftArrowPressed = false;
   	}

    public void keyTyped(KeyEvent e) {}

   	private class GameMouseAdapter extends MouseAdapter {

   		@Override
   		public void mouseClicked(MouseEvent e) {

		   	if(mouseInsideButton1) {

		   		switch(gameState) {
			   		case MAIN_MENU:
			   			playGame();
			   			break;
			   		case ESC_MENU:
			   			saveGame();
			   			break;
			   	}

		   	} else if(mouseInsideButton2) {

		   		switch(gameState) {
		   		case MAIN_MENU:
		   			loadGame();
		   			break;
		   		case END_MENU:
		   			playGame();
		   			break;
	   			}

		   	}
	
	   	}

	   	@Override
	   	public void mouseMoved(MouseEvent e) {
	   		if(e.getPoint().x >= menuButtonPosX && e.getPoint().x < menuButtonPosX + MENU_BUTTON_WIDTH) {
	   			if(e.getPoint().y >= menuButton1PosY && e.getPoint().y < menuButton1PosY + MENU_BUTTON_HEIGHT) {	
	   				mouseInsideButton1 = true;
	   			} else if(e.getPoint().y >= menuButton2PosY && e.getPoint().y < menuButton2PosY + MENU_BUTTON_HEIGHT) {
	   				mouseInsideButton2 = true;
	   			} else {
	   				mouseInsideButton1 = false;
	   				mouseInsideButton2 = false;
	   			}
	   		} else {
	   			mouseInsideButton1 = false;
	   			mouseInsideButton2 = false;
	   		}
	   	}
   	}
}