package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
    //este es nuestro canvas sobre el cual hacemos todo el trabajo.
    
    //INTERFAZ
    private JButton boton = new JButton("Play"); 
    private JTextField newFPS = new JTextField(3); //casilla de texto, esto es inncesesario
    private ListenButtons lButton = new ListenButtons(); 
    private ListenMouse lMouse = new ListenMouse();
    private TextHandler lText = new TextHandler();
    private GameOfLife game = new GameOfLife();
    private Cell matriz[][] = game.getMatriz(); //auxiliar para contener a la matriz
    private Cell aux[][] = new Cell[40][25]; //auxiliar
    private DrawMatriz drawMatriz = new DrawMatriz( matriz );
    //-------------
    
    //TICKER 
    // se usa para manejar el tiempo del juego
    private boolean running = false; //controla el play/pause
    private int fps = 60; //frames per second
	private long targetTime = 1000 / fps; //milisegundos entre fps
    //-------------
    
    private int aliveCondition = 0; // se necesitaran 6 vivas para iniciar
    //regla innecesaria creada arbitrariamente
     	
	public void iterate(){
         // esta funcion se ejecutara cada frame
         // para revisar que reglas cumple una celda
         // y asignarle un nuevo estado de vida o muerte
        for( int i = 0; i < 40; i++){
	        for( int j = 0 ; j < 25;j++){	 
	        	 near(i,j);	            
	        }
	    }
	}
	
    private void near( int x, int y ){
        //los self representan una posicion relativa de la matriz
        //toman los valores de las casillas adyacentes a mis cordenads x,y
        //de la siguiente manera:
        /* 
           (SELFX,SELFY)|(SELFX,SELFY)|(SELFX,SELFY)
           (SELFX,SELFY)|    (x,y)    |(SELFX,SELFY)
           (SELFX,SELFY)|(SELFX,SELFY)|(SELFX,SELFY)
         */
        int selfX = 0;
        int selfY = 0;

        //BOOLEANOS( Control de iteraciones en la matriz )
        boolean minX, minY, maxX, maxY, onArea, alive, notMySelf;
        minX = selfX >= 0;
        minY = selfY >= 0;
        maxX = selfX < 40;
        maxY = selfY < 25;        
        onArea = minX && minY  && maxX  && maxY;        
        //------------------------------------------------
        
        //a mis celda en la casilla x,y se la setean la cantidad de celulas vivas que
        //tiene alrededor
        int surrounding = 0;
        
        for( int i = 0; i < 3; i++){
            for( int j = 0; j < 3; j++){
                // recibimos las cordenadas de la posicioin que evaluamos y les restamos 1 para obtener
                selfX = ( x - 1 ) + i;  //nuestra coordenada relativa y evaluar a nuestro alrededor
                selfY = ( y - 1 ) + j;
                               
                selfX = selfX < 0 ? 0 : selfX; // es mi posicion menor que 0? si: 0 ; no = actual
                selfY = selfY < 0 ? 0 : selfY;
                selfX = selfX >= 40 ? 39 : selfX; // es mi posicion mayor que el limite? si: limite-1 ; no: actual
                selfY = selfY >= 25 ? 24 : selfY;
                
                notMySelf = selfX != x || selfY != y; // no tenemos que iterar sobre nuestra propia posicion
                alive = matriz[selfX][selfY].getAlive();
				
                if( alive && onArea && notMySelf )
                    surrounding++;    
            }//for
        } //for 
        matriz[x][y].setNear( surrounding );
        setState(x,y);
    }//near

     private void setState( int x, int y ){
        //REGLAS DEL JUEGO
    	if( matriz[x][y].getAlive() == false && matriz[x][y].getNear()  == 3 ){
             aux[x][y].setAlive(true);
        } else if( matriz[x][y].getAlive() == true && ( matriz[x][y].getNear()  == 3 || matriz[x][y].getNear()  == 2 ) ){
            aux[x][y].setAlive(true);
        } else if( matriz[x][y].getAlive() == true && ( matriz[x][y].getNear()  < 2 || matriz[x][y].getNear()  > 3 ) ){
            aux[x][y].setAlive(false);
        }
     }

    public void start(){
        //obligatoriamente el redibujado tuve que hacerlo en un hilo
		Thread thread = new Thread() {
			public void run(){
                //ticker -- se usa para manejar el tiempo del juego
				long start;
				long elapsed;
				long wait;
				while( running ){
					start = System.nanoTime();//obtenemos tiempo inicial
					elapsed = System.nanoTime() - start; //final - inicial == tiempo transcurrido
					wait = targetTime - elapsed / 1000000; // le restamos a los fps el tiempo transcurrido y lo dividimos
		            try {						                        
		            	iterate();				        
		            	copyMatrix();
		                drawMatriz.updateMatriz( matriz );
		                drawMatriz.repaint();		                
			            Thread.sleep(wait);
					}catch(Exception e) {
						e.printStackTrace();
					}
		        }//while
				// game loop
			}
		};
		thread.start();
    }

    private void fillAux(){
    	 int x = 0;
         int y = 0;
         for( int i = 0; i < 40; i++){
         	for( int j = 0 ; j < 25;j++){
         		aux[i][j] = new Cell();
         		aux[i][j].setX(x);
         		aux[i][j].setY(y);
         		aux[i][j].setAlive(false);
         		y+=20;
         	}
             y=0;
         	x+=20;
         }
    }
    
    private void copyMatrix(){
         for( int i = 0; i < 40; i++){
         	for( int j = 0 ; j < 25;j++){
                matriz[i][j].setAlive( aux[i][j].getAlive() );
         	}
         }
    }


    public GamePanel(){
        super();
        game.game(); //inicializa la matriz
        boton.setBounds(0,0,60,20); //posicionador del boton
        boton.addActionListener(lButton);
        newFPS.addActionListener(lText);
        fillAux();
        this.addMouseListener(lMouse);
        this.setBounds(0,0,800,600); //posicionador
        this.add(boton);
        this.add(new JLabel("FPS: "));
        this.add(newFPS);
        this.add(drawMatriz);
    }

    private class ListenButtons implements ActionListener{
        private int state = 0; //maneja si el boton ha sido presionado
        // 0 = pausado ; 1 = jugando
        public void actionPerformed( ActionEvent e ){
            if( e.getSource() == boton){
                if( state == 0  && aliveCondition >= 4 ){
                    boton.setText("Pause");
                    running = true;
                    state = 1;
                    start();
                }else{
                    boton.setText("Play");
                    running = false;
                    state = 0;
                }
            }//if
        }//func

    }//class

    private class ListenMouse implements MouseListener{
        private int x;
        private int y;

        public boolean validPoint( int x, int y){
            return x >= 0 && x < 800 && y >= 40 && y <= 540 ;
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            x = mouseEvent.getX();
            y = mouseEvent.getY();
            if (validPoint( x, y )){
                x /= 20; //se debe dividir la coordenada del clic entre el tamaño de los cuadrados para obtener su
                y /= 20; //equivalencia en la posicion de la matriz
                y-=2; //compenza el desplazamiento hacia abajo de la matriz
                aliveCondition++;
                matriz[x][y].setAlive(true);
                drawMatriz.drawCell( matriz[x][y].getX(), matriz[x][y].getY(), drawMatriz.getGraphics(),true );                         
            }
            if(SwingUtilities.isRightMouseButton(mouseEvent)){
            	matriz[x][y].setAlive(false);
            	drawMatriz.drawCell( matriz[x][y].getX(), matriz[x][y].getY(), drawMatriz.getGraphics(),false );
            }
        }//func

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
    }

    private class TextHandler implements ActionListener {
        
		public void actionPerformed(ActionEvent e) {
				fps = Integer.parseInt( newFPS.getText() );
	            targetTime = 1000 / fps; //milisegundos entre fps
		}
	}
    
}
