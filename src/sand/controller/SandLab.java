package sand.controller;

import java.awt.*;

public class SandLab
{
  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;
  public static final int SAND = 2;
  public static final int WATER = 3;
  public static final int DIRT = 4;
  public static final int GRASS = 5;
  public static final int FIRE = 6;
  public static final int BURNING_GRASS = 7;
  public static final int ASH = 8;
  
  //Constants for random directions
  public static final int DOWN = 0;
  public static final int LEFT = 1;
  public static final int RIGHT = 2;
  
  //do not add any more fields below
  private int[][] grid;
  private SandDisplay display;
  private long[][] timerGrid;
  
  
  /**
   * Constructor for SandLab
   * @param numRows The number of rows to start with
   * @param numCols The number of columns to start with;
   */
  public SandLab(int numRows, int numCols)
  {
    String[] names;
    // Change this value to add more buttons
    names = new String[9];
    names[EMPTY] = "Empty";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[DIRT] = "Dirt";
    names[GRASS] = "Grass";
    names[FIRE] = "Fire";
    names[BURNING_GRASS] = "Burning_Grass";
    names[ASH] = "Ash";
    
    // Initialize the data member grid with same dimensions
    this.grid = new int[numRows][numCols];
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
    this.timerGrid = new long[numRows][numCols];
    
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
    // Assign the values associated with the parameters to the grid
	  grid[row][col] = tool;
  }

  //copies each element of grid into display
  public void updateDisplay()
  {
	  for(int row = 0; row < grid.length; row++)
	  {
		  for(int col = 0; col < grid[0].length; col++)
		  {
			  if(grid[row][col] == EMPTY)
			  {
				  display.setColor(row, col, Color.BLACK);
			  }
			  if(grid[row][col] == METAL)
			  {
				  display.setColor(row, col, new Color(122, 127, 128));
			  }
			  if(grid[row][col] == SAND)
			  {
				  display.setColor(row, col, new Color(242, 210, 169));
			  }
			  if(grid[row][col] == WATER)
			  {
				  display.setColor(row,  col,  new Color(117, 180, 238));
			  }
			  if(grid[row][col] == DIRT)
			  {
				  display.setColor(row, col, new Color(155, 118, 83));
			  }
			  if(grid[row][col] == GRASS)
			  {
				  display.setColor(row, col, new Color(126, 200, 80));
			  }
			  if(grid[row][col] == FIRE)
			  {
				  display.setColor(row, col, new Color(204, 63, 24));
			  }
			  if(grid[row][col] == BURNING_GRASS)
			  {
				  display.setColor(row, col, new Color(219, 100, 0));
			  }
			  if(grid[row][col] == ASH)
			  {
				  display.setColor(row,  col,  new Color(178, 190, 181));
			  }
		  }
	  }
  }

  //called repeatedly.
  //causes one random particle in grid to maybe do something.
  public void step()
  {
    //The scalar refers to how big the value could be
    //int someRandom = (int) (Math.random() * scalar)
	  int row = (int)(Math.random() * grid.length - 1);
	  int col = (int)(Math.random() * grid[0].length);
	  
	  // Sand and Ash will fall if there's empty space below it
	  if(grid[row][col] == SAND && grid[row + 1][col] == EMPTY)
	  {
		  grid[row][col] = EMPTY;
		  grid[row + 1][col] = SAND;
	  }
	  if(grid[row][col] == ASH && grid[row + 1][col] == EMPTY)
	  {
		  grid[row][col] = EMPTY;
		  grid[row + 1][col] = ASH;
	  }
	  
	  
	  if(grid[row][col] == WATER)
	  {
		  int randDirection = (int)(Math.random() * 3);
		  
		  // Water will fall if it moves down and there's nothing below it
		  if(randDirection == DOWN && grid[row + 1][col] == EMPTY)
		  {
			  grid[row][col] = EMPTY;
			  grid[row + 1][col] = WATER;
		  }
		  
		  // Water could randomly go right to empty space
		  if(randDirection == LEFT && col > 0 && grid[row][col - 1] == EMPTY)
		  {
			  grid[row][col] = EMPTY;
			  grid[row][col - 1] = WATER;
		  }
		  
		  // Water could randomly go left to empty space
		  if(randDirection == RIGHT && col < grid[0].length - 1 
				  && grid[row][col + 1] == EMPTY)
		  {
			  grid[row][col] = EMPTY;
			  grid[row][col + 1] = WATER;
		  }
	  }
	  
	  // Sand or ash will displace water below it
	  if(grid[row][col] == SAND && grid[row + 1][col] == WATER)
	  {
		  grid[row][col] = WATER;
		  grid[row + 1][col] = SAND;
	  }
	  if(grid[row][col] == ASH && grid[row + 1][col] == WATER)
	  {
		  grid[row][col] = WATER;
		  grid[row + 1][col] = ASH;
	  }
	  
	  // Water above dirt will produce grass
	  if(grid[row][col] == DIRT && grid[row - 1][col] == WATER)
	  {
		  grid[row][col] = GRASS;
		  grid[row - 1][col] = EMPTY;
	  }
	  
	  if(grid[row][col] == GRASS)
	  {
		  if(row > 0 && row < grid.length)
		  {
			  
			  // Fire or burning grass on top of or beneath grass will set it on fire
			  if(grid[row+1][col] == FIRE || grid[row-1][col] == FIRE
					  || grid[row+1][col] == BURNING_GRASS || grid[row-1][col] == BURNING_GRASS)
			  {
				  grid[row][col] = BURNING_GRASS;
			  }
		  }
		  
		  if(col > 0 && col < grid[0].length - 1)
		  {
			  
			  // Fire or burning grass on either side of grass will set it on fire
			  if(grid[row][col+1] == FIRE || grid[row][col-1] == FIRE
					  || grid[row][col+1] == BURNING_GRASS || grid[row][col-1] == BURNING_GRASS)
			  {
				  grid[row][col] = BURNING_GRASS;
			  }
		  }
	  }
	  
	  // Sets a timer on fire. After 1 second, it disappears
	  if(grid[row][col] == FIRE)
	  {
		  if(timerGrid[row][col] == 0)
		  {
			  timerGrid[row][col] = System.currentTimeMillis();
		  }
		  else
		  {
			  if(System.currentTimeMillis() - timerGrid[row][col] >= 1000)
			  {
				  timerGrid[row][col] = 0;
				  grid[row][col] = EMPTY;
			  }
		  }
	  }
	  
	  // Sets a timer on burning grass. After 5 seconds, it turns to ash
	  if(grid[row][col] == BURNING_GRASS)
	  {
		  if(timerGrid[row][col] == 0)
		  {
			  timerGrid[row][col] = System.currentTimeMillis();
		  }
		  else
		  {
			  if(System.currentTimeMillis() - timerGrid[row][col] >= 5000)
			  {
				  timerGrid[row][col] = 0;
				  grid[row][col] = ASH;
			  }
		  }
	  }
  }
  
  //do not modify this method
  public void run()
  {
    while (true)
    {
      for (int i = 0; i < display.getSpeed(); i++)
      {
        step();
      }
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
      {
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
      }
    }
  }
}
