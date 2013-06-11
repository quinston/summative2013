package summative2013;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JFrame;
import javax.swing.JPanel;
import summative2013.lifeform.Lifeform;

public class Summative extends JPanel implements KeyListener{

    private static GraphicsEnvironment ge;
    private static GraphicsDevice gd;
    private static JFrame frame;
    private HashMap<Point,Lifeform> locToLife;
    private HashMap<Point,Terrain> locToTerrain;
    private final Object lock = new Object();
    private Rectangle screen;
    
    /**
     * Default constr
     */
    public Summative() {
        Lifeform.summative = this;
        locToLife = new HashMap<Point,Lifeform>();
        locToTerrain = new HashMap<Point,Terrain>();
        setSize(gd.getFullScreenWindow().getSize());
        screen = new Rectangle(-1*getWidth()/2,-1*getHeight()/2,getWidth(),getHeight());
        for(int i = -1*getWidth()/20;i<=getWidth()/20;i++){
            for(int j = -1*getHeight()/20;j<=getHeight()/20;j++){
                mapGen(new Point(i,j));
            }
        }
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }
    //Terrain making
    public enum Terrain {

        LAND, SEA
    };
    /**
     * 
     * @param args The command line parameters
     */
    public static void main(String[] args) {
        frame = new JFrame();

        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
        Summative s = new Summative();
        frame.add(s);
        s.requestFocusInWindow();
        s.repaint();
        frame.setVisible(true);
    }
    public void doMapGen(Point p,int i){
        if(i>=0){
            for(int x = p.x-1;x<=p.x+1;x++){
                for(int y = p.y-1;y<=p.y+1;y++){
                    Point genPoint = new Point(x,y);
                    if((x!=p.x||y!=p.y)&&!locToTerrain.containsKey(new Point(x,y)))
                        mapGen(genPoint);
                    doMapGen(genPoint,i-1);
                }
            }
        }
    }
    //Map generator
    public void mapGen(Point point) {
        int lands = 0, seas = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                synchronized(lock){
                    if (locToTerrain.get(new Point(point.x + x, point.y + y)) == Terrain.LAND) {
                        lands++;
                    } else if (locToTerrain.get(new Point(point.x + x, point.y + y)) == Terrain.SEA) {
                        seas++;
                    } else {
                    }
                }
            }
        }
        //Only land, have a small chance to generate a sea block
        if (seas == 0) {
            if (Math.random() < .95) {
                locToTerrain.put(point, Terrain.LAND);
            } else {
                locToTerrain.put(point, Terrain.SEA);
            }
            //Mostly land, but some sea, generate more sea
        } else if (lands > seas && seas > 0) {
            if (Math.random() < .95) {
                locToTerrain.put(point, Terrain.SEA);
            } else {
                locToTerrain.put(point, Terrain.LAND);
            }
            //Mostly sea, but some land, generate more land
        } else if (seas > lands && lands > 0) {
            if (Math.random() < 0.95) {
                locToTerrain.put(point, Terrain.LAND);
            } else {
                locToTerrain.put(point, Terrain.SEA);
            }
        } else {
            if (Math.random() < 0.5) {
                locToTerrain.put(point, Terrain.LAND);
            } else {
                locToTerrain.put(point, Terrain.SEA);
            }
        }
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawTerrain(g);
    }
    public  void drawTerrain(Graphics g){
        synchronized(lock){
            for(Entry<Point, Terrain> e:locToTerrain.entrySet()){
                Point p = e.getKey();
                if(e.getValue() ==Terrain.LAND)
                    g.setColor(Color.MAGENTA);
                else if(e.getValue() == Terrain.SEA)
                    g.setColor(Color.BLUE);
                if(screen.contains(new Point(p.x*10,p.y*10)))
                    g.fillRect(p.x*10-screen.x,p.y*10-screen.y, 10, 10);
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed");
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_RIGHT)
            moveRight();
        else if(keyCode == KeyEvent.VK_LEFT)
            moveLeft();
        else if(keyCode == KeyEvent.VK_UP)
            moveUp();
        else if(keyCode == KeyEvent.VK_DOWN)
            moveDown();
        repaint();
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        e.consume();
    }
    public void moveRight(){
        screen.translate(10,0);//moves the perspective
        if(!locToTerrain.containsKey(new Point((screen.x+screen.width)/10,(screen.y+screen.height)/10)))//do we have terrain there?
            for(int i = 0; i < screen.height/10;i++)//loop through that visible column
                mapGen(new Point((screen.x+screen.width)/10,screen.y/10+i));//create terrain at those points
    }
    public void moveLeft(){
        screen.translate(-10,0);//moves "camera"
        if(!locToTerrain.containsKey(new Point((screen.x+screen.width)/10,(screen.y+screen.height)/10)))//do we have terrain in the newly visible area?
            for(int i = 0; i < screen.height/10;i++)//loops through column
                mapGen(new Point(screen.x/10,screen.y/10+i));//create terrain there
    }
    public void moveUp(){
        screen.translate(0,-10);//moves viewpoint
        if(!locToTerrain.containsKey(new Point((screen.x)/10,(screen.y)/10)))//check topleft
            for(int i = 0; i < getWidth()/10;i++)//loop through row
                mapGen(new Point(screen.x/10+i,screen.y/10));//generate terrain in that row
    }
    public void moveDown(){
        screen.translate(0,10);//moves centre of field of view
        if(!locToTerrain.containsKey(new Point((screen.x+screen.width)/10,(screen.y+screen.height)/10)))//check bottomright
            for(int i = 0; i < screen.width/10;i++)//loop through points that are newly visible
                mapGen(new Point(screen.x/10+i,(screen.y+screen.height)/10));//generate new row
    }
}