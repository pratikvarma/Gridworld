package org.thimm.gridworlds;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



public class Astar {
    
    protected BufferedReader reader = null;
    protected int ROWS = 0;
    protected int COLUMNS = 0;
    protected Gridcell[][] gridworld;
    protected Gridcell start;
    protected Gridcell goal;
    protected ArrayList<Gridcell> masterPath;
    protected String searchDirection = "f";
    protected boolean useAdaptive = false;
    
    protected int currentPathCost=0;
    protected Set<Gridcell> currentClosedSet = null; 
    protected int pathsUntilGoal=0;
    protected int cellExpansions=0;
    
    public void initGridworld(String fileName) {
        try {
            reader = new BufferedReader(new FileReader(fileName));
            //1. First Line is numbers of rows
            String currentLine = reader.readLine();
            ROWS = Integer.parseInt(currentLine);
            //2. Second line is number of columns
            currentLine = reader.readLine();
            COLUMNS = Integer.parseInt(currentLine);
            
            //Intialize a new gridworld
            gridworld = new Gridcell[ROWS][COLUMNS];
            
            //3. Remaining lines are the Grid itself.
            //for each row            
            for (int row=0;row < ROWS; row++) {
                currentLine = reader.readLine();  
                currentLine = currentLine.replaceAll(" ", "");
                //for each column
                for (int col=0;col < COLUMNS; col++) {
                //initialize a gridcell
                    char ch = currentLine.charAt(col);
                    Gridcell cell= new Gridcell(ch);
                    cell.xPos = col;
                    cell.yPos = row;
                    gridworld[row][col] = cell;
                    if (ch == 's') {
                        start = cell;
                    } else if (ch == 'g') {
                        goal = cell;
                    }
                }
            }
            
            masterPath = new ArrayList<Gridcell>(); 
            currentPathCost = 0;
            pathsUntilGoal=0;
            cellExpansions=0;
            
        } catch(FileNotFoundException fe) {
            System.out.println("Could not find the file:" + fileName);
        } catch(IOException ioe) {
            System.out.println("IOException:Error reading from file:" + fileName);
        } catch(Exception e) {
            System.out.println("Error initializing Gridworld from file:" + fileName);
            System.out.println(e.getMessage());
        } 
    }
    
    public void initFValues() {
        //for each row            
        for (int row=0;row < ROWS; row++) {                 
            //for each column
            for (int col=0;col < COLUMNS; col++) {
            //initialize a gridcell
                Gridcell cell = gridworld[row][col];                
                cell.setG_value(Math.abs(start.xPos - cell.xPos) + 
                        Math.abs(start.yPos - cell.yPos));
                //Use Manhattan distance for the heuristic
                cell.setH_value(Math.abs(goal.xPos - cell.xPos) + 
                        Math.abs(goal.yPos - cell.yPos));
                cell.setF_value(cell.getG_value() + cell.getH_value());
            }
        }
    }
    
    public void initUsingAdaptiveAstar() {
        if (currentClosedSet == null) {
            return;
        }
        System.out.println("A* with path cost " + currentPathCost);
        for (Gridcell cell: currentClosedSet) {
        	//System.out.println("Old H Value:"+cell.getH_value());
        	//System.out.println("Old F Value:"+cell.getF_value());
            
        	//Use Adaptive A* for the heuristic            
            //cell.setH_value(currentPathCost - (Math.abs(start.xPos - cell.xPos) + 
              //      Math.abs(start.yPos - cell.yPos)));
        	cell.setH_value(currentPathCost - cell.getG_value());
            cell.setF_value(cell.getG_value() + cell.getH_value());
            
            //System.out.println("New H Value:"+cell.getH_value());
        	//System.out.println("New F Value:"+cell.getF_value());
            
        }
    }
    
    public Gridcell[] getNeighbors(Gridcell cell){
        //A cell can have at most 4 neighbours.
        ArrayList<Gridcell> neighbors = new ArrayList<Gridcell>();
        
        int cellXPos = cell.xPos;
        int cellYPos = cell.yPos;
        
        //north neighbor
        int northXPos = cellXPos;
        int northYPos = cellYPos - 1;
        
        //Only add iff there is a Northern neighbor
        if (northYPos >= 0) {
            //watch closely
            Gridcell north = gridworld[northYPos][northXPos];
            cell.north = north;
            neighbors.add(north);
        }
        
        //East neighbor
        int eastXPos = cellXPos + 1;
        int eastYPos = cellYPos;
        
        //Only add iff there is a eastern neighbor
        if (eastXPos < COLUMNS) {
            //watch closely
            Gridcell east = gridworld[eastYPos][eastXPos];
            cell.east = east;
            neighbors.add(east);
        }
        
        //West neighbor
        int westXPos = cellXPos - 1;
        int westYPos = cellYPos;
        
        //Only add iff there is a western neighbor
        if (westXPos >= 0) {
            //watch closely
            Gridcell west = gridworld[westYPos][westXPos];
            cell.west = west;
            neighbors.add(west);
        }

        //South neighbor
        int southXPos = cellXPos;
        int southYPos = cellYPos + 1;
        
        //Only add iff there is a southern neighbor
        if (southYPos < ROWS) {
            //watch closely
            Gridcell south = gridworld[southYPos][southXPos];
            cell.south = south;
            neighbors.add(south);
        }
        
        Collections.sort(neighbors);
        //Gridcell[] gc = new Gridcell[1];
        return neighbors.toArray(new Gridcell[0]);
    }
    
    /**
     * This is the main A* algorithm
     * @param s The root of the search
     * @param g The leaf we are looking for
     * @param path An empty parameter that is returned.
     * @return The path taken by the algorithm.
     */
    public ArrayList<Gridcell> doAstar(Gridcell s, Gridcell g, ArrayList<Gridcell> path) {
        pathsUntilGoal++;
        Set<Gridcell> closed = new HashSet<Gridcell>();
        PriorityHeap<Gridcell> heap = new PriorityHeap<Gridcell>(ROWS*COLUMNS);
        heap.insert(s);
        while (heap.getCount() > 0) {
            Gridcell currentCell = (Gridcell) heap.extractMin(); 
            path.add(currentCell);
            //get last element from path
            Gridcell cell = path.get(path.size() - 1);
            if (closed.contains(cell)) {
                continue;
            }
            
            if (cell.isGoal) {              
                break;
            	//return path;
            } 
            closed.add(cell);
            Gridcell[] neighbors = getNeighbors(cell);
            for (int i=0;i< neighbors.length;i++) {
                if (!closed.contains(neighbors[i]) && !(neighbors[i].isInRange && neighbors[i].isBlocked)) {
                    neighbors[i].parent = currentCell;
                    heap.insert(neighbors[i]);
                }               
            }
            //heap.outputTree();            
        }
        //TODO
        if (useAdaptive) {
	        currentClosedSet = new HashSet<Gridcell>(closed);	        
        }
        return path;
    }
    
    /**
     * This is the A* launcher. It prepares the start state and the goal states.
     * It receives the expanded cells from A*, and extracts the actual path
     * from target to start.
     */
    public void beginAstar() {        
        //Lets begin. Get neighbours of start
        Gridcell[] sNeighbors = getNeighbors(start);
        
        for (int i=0;i<sNeighbors.length;i++) {
            sNeighbors[i].isInRange = true;
        }
        
        //Make a path Array and add the start cell to it.
        ArrayList<Gridcell> path = new ArrayList<Gridcell>();        
        //Call our A star algorithm.
        //according to the search direction
        if ("b".equals(searchDirection)) {
            start.isStart = false;
            start.isGoal = true;
            goal.isStart = true;
            goal.isGoal = false;
            //path.add(goal);
            path = doAstar(goal, start, path);
        } else if ("f".equals(searchDirection)) {
            //path.add(start);
            path = doAstar(start, goal, path);
        } else {
            System.out.println("No search direction specified");
            return;
        }
        
        //Output this path. Lets pause over here and test.
        //Check if goal was reached. Extract the last element and check if it is a goal.
        Gridcell nextInPath = path.get(path.size() - 1);
        ArrayList<Gridcell> list = new ArrayList<Gridcell>();
        cellExpansions = cellExpansions + path.size();
        currentPathCost=0;
        if (!nextInPath.isGoal) {
            System.out.println("No Path was found");
        } else {
            while (nextInPath != null) {
            	currentPathCost++;
                list.add(nextInPath);
                nextInPath.display = '0';
                if (nextInPath.isStart) {
                    break;
                }
                nextInPath = nextInPath.parent;
            }             
        }        
        start.display = 's';
        goal.display = 'g';
        
        if ("f".equals(searchDirection)) {
            Collections.reverse(list);
        }
        for (Gridcell pathCell: list) {
            //beware there might be duplicates.
            masterPath.add(pathCell);
        }
        
        //reset start and goal states.
        start.isStart = true;
        start.isGoal = false;
        goal.isStart = false;
        goal.isGoal = true;
        //move the game character to the next location
        //Output the gridworld
        outputMasterpath();
        outputGridworld();
        
        moveGameCharacter();
    }
    
    /**
     * outputs the path we've travelled so far.
     *
     */
    public void outputMasterpath() {
        for (int i=0;i<masterPath.size();i++) {
            Gridcell cell = masterPath.get(i);
            // display purposes only
            System.out.print(cell.yPos);
            System.out.print(":");
            System.out.print(cell.xPos);
            System.out.print(" ");
        }
        System.out.println();

    }
    
    /**
     * Takes the master path and starts
     * travelling through it until it hits a blocked cell. It then retraces
     * one step and calls beginAstar again.
     *
     */
    public void moveGameCharacter() {
        boolean repeat = false;
        
        int i=0;
        for ( i=0; i<masterPath.size(); i++) {
            Gridcell current = masterPath.get(i);
            if (current.isGoal) {
                System.out.println("Goal Reached!!");
                printDiagnostics();
                break;
            }
            if (!current.isBlocked) {
                continue;
            } else {
                //reset current start
                start.isStart = false;                
                //assign a new start. The cell just before the blocked cell.
                start = masterPath.get(i-1);
                start.isStart = true;
                repeat = true;
                break;
            }
        }
        if (repeat) {
            for (int j=i;j<masterPath.size();) {
                Gridcell tmp = masterPath.get(j);
                if (tmp.isBlocked) {
                    tmp.display = 'x';
                } else {
                    tmp.display = '_';
                }
                //System.out.println("Removing..." + j + "-" + tmp.yPos + ":" + tmp.xPos);
                masterPath.remove(j);
            }
            //initFValues();
            if (useAdaptive) {
                initUsingAdaptiveAstar();
            }
            beginAstar();
        }
        
    }
    
    /**
     * Outputs the grid.
     * The grid is 0 based rows and columns starting at TOP LEFT
     *
     */
    public void outputGridworld() {
        for (int row=0;row < ROWS; row++) {
            //for each column           
            for (int col=0;col < COLUMNS; col++) {
                Gridcell cell = gridworld[row][col];
                System.out.print(cell.display);
                System.out.print(" ");
            }
            System.out.println();            
        }
        System.out.println();
        for (int i=COLUMNS;i>0; i--) {
            System.out.print("--");
        }
        System.out.println();
        System.out.println();
    }
    
    public void printDiagnostics() {
        System.out.println("-----------------------STATS--------------------------------------------------");
        System.out.println("Number of cells visited in Path (maybe more than once):" + masterPath.size());
        System.out.println("Number of A*s performed until goal was reached :" + pathsUntilGoal);
        System.out.println("Number of cell expansions :" + cellExpansions);
    }
    
    public static void main(String[] args) {
        String ipFileName = null;
        String srchDirection = null;
        String tieBreaking = null;
        boolean useAdaptiveAstar = false;
        //Start reading in command line
        for (int i=0;i<args.length;i++) {
            String s = args[i];
            if ("-i".equals(s)) {
                ipFileName = args[++i];
            } else if ("-d".equals(s)) {
                srchDirection = args[++i];
                if (!("f".equals(srchDirection) || "b".equals(srchDirection))) {
                    System.out.println("Invalid option for search direction: -d");
                    System.exit(0);
                }
            } else if ("-t".equals(s)) {
                tieBreaking = args[++i];
                if (!("s".equals(tieBreaking) || "l".equals(tieBreaking))) {
                    System.out.println("Invalid option for tie breaking direction: -t");
                    System.exit(0);
                }
            } else if ("-A".equals(s)) {
                useAdaptiveAstar = true;
            } 
        }
        //Initialize Astar.
        Gridcell.break_tie = tieBreaking;
        Astar astar = new Astar();
        astar.searchDirection= srchDirection;
        astar.useAdaptive = useAdaptiveAstar;
        astar.initGridworld(ipFileName);
        //The grid as in file
        astar.outputGridworld();
        //Initailizing all FValues
        astar.initFValues();
        //Get the ball rolling.
        astar.beginAstar();
    }
}
