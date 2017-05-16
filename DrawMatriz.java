package game;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class DrawMatriz extends JComponent {
	//clase que se usa para dibujar la matriz
    //creamos una variablque contenga un arreglo de matriz
    //cada "Matriz" en si misma es mas bien una celda
	private Cell[][] matriz;

    //esta metodo se usa para la accion de dibujar en pantalla
    public void paintComponent(Graphics g) {
        //DIBUJANDO MATRIZ.
        //el auxAlive se usa solo por A E S T H E T I C S
    	boolean auxAlive;
        int x = 0, y = 0;
        for( int i = 0; i < 40; i++){
        	for( int j = 0 ; j < 25;j++){
        		x = matriz[i][j].getX();
        		y = matriz[i][j].getY();
        		auxAlive = matriz[i][j].getAlive();
                drawCell( x, y, g, auxAlive);
        	}
        }
        //------------------

        //DIBUJANDO LINEAS
        x = 0;
        y = 0;
        g.setColor(Color.black);
        for (int i = 0; i < 26; i++) {
            g.drawLine(0, x, 800, x);
            x += 20;
        }

        for (int i = 0; i < 41; i++) {
            g.drawLine( y, 0, y, 500);
            y += 20;
        }
        //---------------------
    }

    public void updateMatriz( Cell[][] matriz ){
        // "setMatriz" seria un nombre tambien para esta funcion, 
        // en este modo recibimos una matriz "actualizada" sobre la cual
        // trbajaremos
        this.matriz = matriz; 
    }

    public void drawCell( int x, int y , Graphics g, boolean alive ){
        //recibimos las coordenadas de una celda y dibujamos el
        //color del cuadro correspondiente a su estado de vida
    	if(alive){
    		g.setColor( Color.black );
    		g.fillRect(x, y, 20, 20);
    	}else{
    		g.setColor( Color.white );
            g.fillRect(x, y, 20, 20);
    	}
    }

    public DrawMatriz( Cell[][] matriz ) {
        //constructor.
        super();
        this.matriz = matriz;
        this.setBounds(0, 40, 801, 540);
        this.setSize(801, 540);
        this.setPreferredSize(new Dimension(800, 540));
    }
}
