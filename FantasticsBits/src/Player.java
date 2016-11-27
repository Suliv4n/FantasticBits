import java.util.*;
import java.io.*;
import java.math.*;


class Entity{
    protected int id;
    protected int x = 0;
    protected int y = 0;
    
    protected int vx = 0;
    protected int vy = 0;
    
    protected int radius = 0;
    
    public Entity(int id, int x, int y, int vx, int vy, int radius){
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
    }
    
    /**
     * Retourne l'entité la plus proche de l'entité courrants.
     * 
     * @param entities
     * 	Liste des entités à tester.
     * 
     * @return l'entité la plus proche de l'entité de l'entité courante parmi la liste d'entité.
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
    
    public int getY(){
    	return y;
    }
    
    public int getX(){
    	return x;
    }
}

class Snaffle extends Entity{
	public static final int DEFAULT_RADIUS = 150;
	
    public Snaffle(int id, int x, int y, int vx, int vy, int radius){
        super(id, x, y, vx, vy, radius);
    }
}

class Wizard extends Entity{
    private boolean hasSnaffle = false;
    
    public Wizard(int id, int x, int y, int vx, int vy, int radius, boolean hasSnaffle){
        super(id, x, y, vx, vy, radius);
        this.hasSnaffle = hasSnaffle;
    }
    
    /**
     * Génère l'action du sorcier.
     * 
     * @param snaffles
     * 	List des snaffles sur le terrains.
     */
    public void doAction(ArrayList<Snaffle> snaffles, Area area){
        if(!hasSnaffle){
            Snaffle closest = (Snaffle)getClosestEntity(snaffles);
            move(closest.x, closest.y, 100);
        }
        else{
        	Goal goal = area.getOpponentGoal();
        	int[] target = goal.getOptimalThrow(this);
            throwSnaffle(target[0], target[1], 500);
        }
    }
    
    /**
     * Génère l'action "MOVE" pour déplacer le sorcier.
     * 
     * @param x
     * 	Déplacement sur l'axe x.
     * @param y
     * 	Déplacement sur l'axe y.
     * @param power
     * 	Puissance du mouvement.
     */
    private void move(int x, int y, int power){
        System.out.println("MOVE " + x + " " + y + " " + power);
    }
    
    /**
     * Génère l'action "THROW" pour lancer le snaffle.
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

class Bludgers extends Entity{
	public Bludgers(int id, int x, int y, int vx, int vy, int radius){
		super(id, x, y, vx, vy, radius);
	}
}

class Area{
    
	public static final int AREA_WIDTH = 16001;
    public static final int AREA_HEIGHT = 7501;
    
    private Goal opponentGoal;
    private Goal myGoal;
    
    public Area(boolean reverse){
    	Goal goal1 = Goal.generate(0);
    	Goal goal2 = Goal.generate(1);
    	
    	if(!reverse){
    		myGoal = goal1;
    		opponentGoal = goal2;
    	}
    	else{
    		myGoal = goal2;
    		opponentGoal = goal1;
    	}
    }
    
    public Goal getOpponentGoal(){
    	return opponentGoal;
    }
    
}


class Goal{
	private int topX;
	private int topY;
	private int bottomX;
	private int bottomY;
	
	public static final int DEFAULT_Y = 3750;
	public static final int DEFAULT_HEIGHT = 4000;
	
	public Goal(int topX, int topY, int bottomX, int bottomY){
		this.topX = topX;
		this.topY = topY;
		this.bottomX = bottomX;
		this.bottomY = bottomY;
	}
	
	public int[] getOptimalThrow(Wizard wizard) {
		int[] coordinates = new int[2];
		coordinates[0] = topX;
		
		if(wizard.getY() < topY){
			coordinates[1] = topY + Snaffle.DEFAULT_RADIUS;
		}
		if(wizard.getY() > bottomY){
			coordinates[1] = bottomY - Snaffle.DEFAULT_RADIUS;
		}
		else{
			coordinates[1] = wizard.getY();
		}
		
		return coordinates;
	}

	public int getTopY(){
		return topY;
	}
	
	public int getBottomY(){
		return bottomY;
	}
	
	public static Goal generate(int id){
		switch(id){
			case(0):
				return new Goal(0,DEFAULT_Y - DEFAULT_Y/2, 0, DEFAULT_Y + DEFAULT_Y/2);
			case(1):
				return new Goal(Area.AREA_WIDTH-1,DEFAULT_Y - DEFAULT_Y/2, Area.AREA_WIDTH-1, DEFAULT_Y + DEFAULT_Y/2);
			default:
				return null;
		}
	}
	
}

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle and use your team id to determine where you need to throw it.
 **/
class Player {



    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int myTeamId = in.nextInt(); // if 0 you need to score on the right of the map, if 1 you need to score on the left
        
        Area area = new Area(myTeamId == 1);
        
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
            
                
                if(entityType.equals("WIZARD")){
                    wizards.add(new Wizard(entityId,x,y,vx,vy,400,state==1));
                }
                else if(entityType.equals("OPPONENT_WIZARD")){
                    opponents.add(new Wizard(entityId,x,y,vx,vy,400,state==1));
                }
                else if(entityType.equals("SNAFFLE")){
                    snaffles.add(new Snaffle(entityId,x,y,vx,vy,150));
                }
            
            }
            for (int i = 0; i < 2; i++) {
                wizards.get(i).doAction(snaffles, area);
                //System.out.println("MOVE 8000 3750 100");
            }
        }
    }
}