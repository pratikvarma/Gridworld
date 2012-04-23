package org.thimm.gridworlds;

public class Gridcell implements Comparable<Gridcell>{
	public static String break_tie="";
    private int g_value = Integer.MAX_VALUE;
    private int h_value = Integer.MAX_VALUE;
    private int f_value = Integer.MAX_VALUE;
    
    public boolean isStart = false;
    public boolean isGoal = false;
    public boolean isBlocked = false;
    public boolean isInRange = false;
    
    public Gridcell north;
    public Gridcell east;
    public Gridcell west;
    public Gridcell south;
    
    public Gridcell parent;
    
    public int xPos;
    public int yPos;
    
    public char display;
    
    public Gridcell() {    	
    }
    
    public Gridcell(char ch) {
    	switch(ch) {
	    	case 'x': 
	    		this.isBlocked = true;
                this.display = 'x';
	    		break;
	    	case 's':
	    		this.isStart = true;
                this.display = 's';
	    		break;
	    	case 'g':
	    		this.isGoal = true;
                this.display = 'g';
	    		break;
	    	default:
	    		this.isBlocked = false;
                this.display = '_';
	    		break;
    	}
    }
    
    public Gridcell(int g, int h, int f) {
        this.g_value = g;
        this.h_value = h;
        this.f_value = f;
    }
    
    public int compareTo(Gridcell other) {
        if (this.f_value < other.f_value) {
            return -1;
        } else if (this.f_value > other.f_value) {
            return 1;
        } else {
        	if ("s".equals(Gridcell.break_tie)) {
        		if (this.g_value < other.g_value) {
                    return -1;
                } else if (this.g_value > other.g_value) {
                    return 1;
                } else return 0;
        	} else if ("l".equals(Gridcell.break_tie)) {
        		if (this.g_value < other.g_value) {
                    return 1;
                } else if (this.g_value > other.g_value) {
                    return -1;
                } else return 0;
        	} else return 0;
        }
    }

    public int getF_value() {
        return f_value;
    }

    public void setF_value(int f_value) {
        this.f_value = f_value;
    }

    public int getG_value() {
        return g_value;
    }

    public void setG_value(int g_value) {
        this.g_value = g_value;
    }

    public int getH_value() {
        return h_value;
    }

    public void setH_value(int h_value) {
        this.h_value = h_value;
    }
    
}
