import java.util.*;
import java.io.*;
import java.math.*;


class Entity{
    protected int id;
    protected int x = 0;
    protected int y = 0;
    
    protected int vx = 0;
    protected int vy = 0;
    
    public Entity(int id, int x, int y, int vx, int vy){
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }
    
    /**
     * Retourne l'entit� la plus proche de l'entit� courrants.
     * 
     * @param entities
     * 	Liste des entit�s � tester.
     * 
     * @return l'entit� la plus proche de l'entit� de l'entit� courante parmi la liste d'entit�.
     */
    public Entity getClosestEntity(ArrayList<? extends Entity> entities){
        boolean first = true;
        double min = 0;
        Entity res = null;
        for(Entity e : entities){
            double d = Math.sqrt((x-e.x)*(x-e.x) + (y-e.y)*(y-e.y));
            if(d<min || first){
                min = d;
                res = e;
            }
            first = false;
        }
        
        return res;
    }
}

class Snaffle extends Entity{
    public Snaffle(int id, int x, int y, int vx, int vy){
        super(id,x,y,vx,vy);
    }
}

class Wizard extends Entity{
    private boolean hasSnaffle = false;
    
    public Wizard(int id, int x, int y, int vx, int vy, boolean hasSnaffle){
        super(id,x,y,vx,vy);
        this.hasSnaffle = hasSnaffle;
    }
    
    /**
     * G�n�re l'action du sorcier.
     * 
     * @param snaffles
     * 	List des snaffles sur le terrains.
     */
    public void doAction(ArrayList<Snaffle> snaffles){
        if(!hasSnaffle){
            Snaffle closest = (Snaffle)getClosestEntity(snaffles);
            move(closest.x, closest.y, 100);
        }
        else{
            throwSnaffle(Player.AREA_WIDTH, y, 500);
        }
    }
    
    /**
     * G�n�re l'action "MOVE" pour d�placer le sorcier.
     * 
     * @param x
     * 	D�placement sur l'axe x.
     * @param y
     * 	D�placement sur l'axe y.
     * @param power
     * 	Puissance du mouvement.
     */
    private void move(int x, int y, int power){
        System.out.println("MOVE " + x + " " + y + " " + power);
    }
    
    /**
     * G�n�re l'action "THROW" pour lancer le snaffle.
     * 
     * @param x
     * 	Ciblage sur l'axe x.
     * @param y
     * 	Ciblage sur l'axe y.
     * @param power
     * 	Puissance du lancer.
     */
    private void throwSnaffle(int x, int y, int power){
        System.out.println("THROW " + x + " " + y + " " + power);
    }

}

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle and use your team id to determine where you need to throw it.
 **/
class Player {

    public static final int AREA_WIDTH = 16001;
    public static final int AREA_HEIGHT = 7501;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int myTeamId = in.nextInt(); // if 0 you need to score on the right of the map, if 1 you need to score on the left

        
        // game loop
        while (true) {
            ArrayList<Wizard> wizards = new ArrayList<Wizard>();
            ArrayList<Wizard> opponents = new ArrayList<Wizard>();
            ArrayList<Snaffle> snaffles = new ArrayList<Snaffle>();
            
            int entities = in.nextInt(); // number of entities still in game
            for (int i = 0; i < entities; i++) {
                int entityId = in.nextInt(); // entity identifier
                String entityType = in.next(); // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" (or "BLUDGER" after first league)
                int x = in.nextInt(); // position
                int y = in.nextInt(); // position
                int vx = in.nextInt(); // velocity
                int vy = in.nextInt(); // velocity
                int state = in.nextInt(); // 1 if the wizard is holding a Snaffle, 0 otherwise
            
                
                if(entityType.equals("WIZARDS")){
                    wizards.add(new Wizard(entityId,x,y,vx,vy,state==1));
                }
                else if(entityType.equals("OPPONENT_WIZARD")){
                    opponents.add(new Wizard(entityId,x,y,vx,vy,state==1));
                }
                else if(entityType.equals("SNAFFLE")){
                    snaffles.add(new Snaffle(entityId,x,y,vx,vy));
                }
            
            }
            for (int i = 0; i < 2; i++) {
                wizards.get(i).doAction(snaffles);
                //System.out.println("MOVE 8000 3750 100");
            }
        }
    }
}