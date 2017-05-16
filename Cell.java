package game;

public class Cell{

    private boolean alive;
    private int near;
    private int x;
    private int y;
    
    public void setAlive( boolean alive ){ this.alive = alive; }
    public void setNear( int near ){ this.near = near; }
    public void setX( int x ){ this.x = x; }
    public void setY( int y ){ this.y = y; }

    public int getNear(){ return this.near; }
    public boolean getAlive(){ return this.alive; }
    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    
}
