import java.io.*;
import java.util.*;
public class Polygon {

	public static ArrayList<Node> vertices = new ArrayList<Node>();
	public static ArrayList<Edge> edges = new ArrayList<Edge>(); 
	
	public static int numNodes = 0;
	//ArrayList to hold third point in triangle
	public static int[][] chords = new int[numNodes][numNodes]; 
	//ArrayList to hold the triangle sum given the third point in the triangle
	//Note: this is a parallel matrix to chords
	public static double[][] triSum = new double[numNodes][numNodes];

	public static double minTriSum = 0.0;
	public static double polygonPerimeter = 0.0;
	public static double chordSum = 0.0;
	
	public static void main(String[] args) {
		try{
			buildPolygon();
		} catch (FileNotFoundException e){
			System.out.println("File not found");
			System.exit(1);
		}
		/*for(int n = 0; n < vertices.size(); n++){
			System.out.println("Node " + n + ":");
			for(int i = 0; i < vertices.size(); i++){
				if(i != n){
					System.out.println(i + " " + vertices.get(n).getDistance(i));
				}
			}
		}*/
		
		minTriSum = findTriangles();
		/*for(int n = 0; n < numNodes; n++){
			System.out.println();
			for(int i = 0; i < numNodes; i++){
				System.out.print(" " + triSum[n][i] + " ");
			}
		}*/
		/*for(int n = 0; n < numNodes; n++){
			System.out.println();
			for(int i = 0; i < numNodes; i++){
				System.out.print(" " + chords[n][i] + " ");
			}
		}*/
		//System.out.println(numNodes);
		System.out.println("Minimal sum of triangle perimeters = " + minTriSum);
		printChords(0, numNodes - 2);
		System.out.println("Check: twice(sum of chords) + polygon perimeter = " + (2*chordSum + polygonPerimeter));
	}
	
	/*This function builds the polygon based on the given vertices from the file*/
	public static void buildPolygon() throws FileNotFoundException{
		int nodeID = 0; //counter to id the nodes
		System.out.println("Please enter the graph you would like to read.");
		Scanner in = new Scanner(System.in);
		String filename = in.nextLine();
		in.close();
		Scanner sc = new Scanner(new File(filename));
		//For every line in the file, read in the doubles attributed to the node's
		//point value. Add every vertex to the polygon's HashMap
		while(sc.hasNextLine()){
			vertices.add(nodeID, new Node(sc.nextDouble(), sc.nextDouble(), nodeID));
			//System.out.println(nodeID);
			nodeID ++;
		}
		sc.close();
		
		//Build each node's ArrayList of distances to other nodes
		for(Node n : vertices){ //for every node of the polygon
			for(Node i : vertices){
				if(i != n){
					vertices.get(n.getID()).addNode(vertices.get(i.getID()));
				}
				
			}
		}
		//at the end of the while loop, nodeID + 1 = number of nodes of the polygon
		numNodes = nodeID + 1;
		chords = new int[numNodes][numNodes];
		triSum = new double[numNodes][numNodes];
		//System.out.println("NumNodes = " + numNodes);
		//find perimeter
		polygonPerimeter = vertices.get(0).getDistance(numNodes - 2);
		for(int i = 0; i < numNodes - 2; i++){
			polygonPerimeter += vertices.get(i).getDistance(i+1);
		}
		//fill in chords and edges with appropriate values
		for(int c = 0; c < numNodes - 1; c++){
			//if on the diagonal, no need to calculate a triangle sum or third point
			chords[c][c] = -1;
			triSum[c][c] = 0.0;
			//if on the edge of the triangle, no need to calc triSum or third point
			chords[c][c+1] = -1;
			triSum[c][c+1] = 0.0;
		}
	}
	
	/*Function fills the parallel matrices, holding the values of the third point of triangles
	 * and the resulting triangle sums from the triangles*/
	public static double findTriangles(){
		for(int i = 0; i < numNodes - 3; i++){
			int t = (i + i + 2)/2;
			//System.out.println("i: " + i + " t: " + t);
			chords[i][i+2] = t;
			triSum[i][i+2] = vertices.get(i).getDistance(t) + vertices.get(i).getDistance(i+2) +
							vertices.get(i+2).getDistance(t);
		}
		for(int j = 3; j < numNodes; j++){
			//fill the diagonals only
			for(int i = 0; i < numNodes - j-1; i++){
				//find min subproblem
				double min = Double.MAX_VALUE;
				double option = 0.0;
				int A = -1; //a value to hold the resulting chord between i and i+j
				for(int a = i+1; a < i+j; a++){
					option = triSum[i][a] + triSum[a][i+j] + vertices.get(i).getDistance(a)
															+ vertices.get(i).getDistance(i+j)
															+ vertices.get(a).getDistance(i+j);
					if(option < min){
						//if the given option is the most minimum, save it
						min = option;
						A = a;
					}
				}
				//save the most minimum option in the matrices
				chords[i][i+j] = A;
				triSum[i][i+j] = min;
			}
		}
		return triSum[0][numNodes-2];
	}
	public static void printChords(int n1, int n2){
		if(n1 == 0 && n2 == numNodes - 2){
			printChords(n1, chords[n1][n2]);
			printChords(chords[n1][n2], n2);
		}
		else if (Math.abs(n1-n2) > 1){
			System.out.println(" " + n1 + " " + n2);
			printChords(n1, chords[n1][n2]);
			printChords(chords[n1][n2], n2);
			chordSum += vertices.get(n1).getDistance(n2);
		}
	}
}