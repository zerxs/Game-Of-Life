package game;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

    public GameFrame(){
        //esta es nuestra ventana
        super("Game of Life");
        GamePanel panel = new GamePanel();
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);
        this.setVisible(true);
    }

}
