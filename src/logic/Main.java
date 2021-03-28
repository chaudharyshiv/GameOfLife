package logic;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myObj = new Scanner(System.in);
	    System.out.println("Enter Universe size in M x N format");
		System.out.println("Enter M - ");
		int rows = myObj.nextInt();  // Read user input N
		System.out.println("Enter N - ");
		int columns = myObj.nextInt();	// Read user input N
		int count = 2;
		while(rows == 0 || columns == 0 || count <= 0)
		{
			System.out.println("You have " + count + " attempts left");
			System.out.println("You have entered invalid Universe size.Please re-enter Universe size");
			System.out.println("Enter M - ");
			rows = myObj.nextInt();  // Read user input N
			System.out.println("Enter N - ");
			columns = myObj.nextInt();  // Read user input N
			count--;
		}
		if(count==0)
		{
			System.out.println("You have exhausted all attempts. Try restarting the game.");
			myObj.close();
			return;
		}
		ArrayList<Cordinate> populationCordinates = new ArrayList<Cordinate>();
		String cord = "y";
		System.out.println("Enter Universe Cordinates where Population is present starting position is 0,0");
		System.out.println("Enter X,Y cordinate - Enter n to Stop entering cordinates");
		
		while(cord.charAt(0)!='n') {
			cord = getStartingCordinates(myObj, rows, columns, populationCordinates);	
		}
		System.out.println("You want to see population states of Universe after how many generations");
		int generationsPassed = myObj.nextInt();
		
		playGame(rows,columns,populationCordinates,generationsPassed);
	}

	/**
	 * @param myObj
	 * @param rows
	 * @param columns
	 * @param populationCordinates
	 * get valid cordinates from User Input and save them in populationCordinates
	 */
	private static String getStartingCordinates(Scanner myObj, int rows, int columns,
			ArrayList<Cordinate> populationCordinates) {
		 String cord = myObj.next();
		if((cord.charAt(0) == 'n'))
		{
			return cord;
		}
		try {
			int x = Integer.parseInt(cord.split(",")[0]);
			int y = Integer.parseInt(cord.split(",")[1]);
			if(x>=rows || y>=columns || x<0 || y<0) //check for valid entry
			{
				System.out.println("Cordinates you entered are out of this Universe, so will not be considered populated");
			}
			else {
				Cordinate cordinate = new Cordinate(x, y);
				populationCordinates.add(cordinate);
			}
		} catch (Exception e) {
			System.out.println("Cordinates you entered are out of this Universe, so will not be considered populated");
		}
		return "y";
	}
	//Main logic of playing the game lies here
	private static void playGame(int rows, int columns, ArrayList<Cordinate> populationCordinates, int generationsPassed) {
		// TODO Auto-generated method stub
		int[][] universe = new int[rows][columns];
		for(Cordinate cordinate : populationCordinates)
		{
			universe[cordinate.getX()][cordinate.getY()] = 1;
		}
		System.out.println("Initial Universe Looks like as below (0 is unpopulated and 1 is populated)");
		showCurrentUniverseState(rows, columns, universe);
		
		for(int generation = 1;generation<=generationsPassed;generation++) {
		
			calculateCellNeighbours(rows, columns, universe);
		
			convertUniverseToNextgeneration(rows, columns, universe);
			
			System.out.println("After " + generation + " generation(s)");
			showCurrentUniverseState(rows, columns, universe);
		}
		
		
	}

	/**
	 * @param rows
	 * @param columns
	 * @param universe
	 * based on the +ve and -ve values in universe we define the rules for next generation
	 */
	private static void convertUniverseToNextgeneration(int rows, int columns, int[][] universe) {
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				if(universe[i][j]>0) // for Live Population
				{
					if(universe[i][j]<3) //Condition 1 (underpopulation for <2 neighbours) -> we checked with 3 as we incremented starting from 1
					{
						universe[i][j]=0;
					}
					else if(universe[i][j]<=4) // Condition 2 -> lives with 2or 3 neighbours
					{
						universe[i][j]=1;
					}
					else if(universe[i][j]>4) // Condition 3 (Overpopulation) dies for neighbour>3
					{
						universe[i][j]=0;
					}
				}
				else
				{
					if(universe[i][j] == -3) //condition 4 (make dead cell live) when neighbour equals 3
					{
						universe[i][j]=1;
					}
					else					//when dead cell remains dead
						universe[i][j]=0;
				}
			}
		}
	}

	/**
	 * @param rows
	 * @param columns
	 * @param universe
	 * We calculate live neighbours and put the values in place
	 * Increment +ve Values when Live and decrement -ve values for dead cells
	 */
	private static void calculateCellNeighbours(int rows, int columns, int[][] universe) {
		int adjacentX[] = {-1,1,0,-1,1,0,-1,1};
		int adjacentY[] = {1,1,1,-1,-1,-1,0,0};
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				for(int k=0;k<8;k++)
				{
					if(checkinUniverse(rows,columns,i+adjacentX[k],j+adjacentY[k]))
					{
						if(universe[i+adjacentX[k]][j+adjacentY[k]] > 0)
						{
							if(universe[i][j] > 0)
								universe[i][j]++;
							else
								universe[i][j]--;
						}
					}
				}
			}
		}
	}
	
    //check if the cordinates lie in universe or out of universe
	private static boolean checkinUniverse(int rows, int columns, int x, int y) {
		// TODO Auto-generated method stub
		return rows>x && columns>y && x>=0 && y>=0;
	}
    // shows current state of Universe
	private static void showCurrentUniverseState(int rows, int columns, int[][] universe) {
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				System.out.print(universe[i][j]);
			}
			System.out.println();
		}
	}

}
