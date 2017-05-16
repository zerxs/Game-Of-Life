package game;

public class GameOfLife {

    Cell matriz[][] = new Cell[40][25];
    //la matriz que usaremos

    public void fillMatriz(){
        //inicializamos la matriz con puras celdas muertas.
        //y a cada celda le asignamos unas cordenadas en X y Y
        int x = 0;
        int y = 0;
        for( int i = 0; i < 40; i++){
        	for( int j = 0 ; j < 25;j++){
        		matriz[i][j] = new Cell();
        		matriz[i][j].setX(x);
        		matriz[i][j].setY(y);
        		matriz[i][j].setAlive(false);
        		y+=20;
        	}
            y=0;
        	x+=20;
        }
    }
    
    public void game(){
        //constructor.
        fillMatriz();
        /*debugger
        for( int i = 0; i < 40; i++){
        	for( int j = 0 ; j < 25	;j++){
        		System.out.println( matriz[i][j].getX() + " " + matriz[i][j].getY() );
        	}
        } */
    }

    public Cell[][] getMatriz(){ return this.matriz; }

}
