/*Audrey Clark March 1, 2017
 * Node class to hold information on vertices of the given polygon
 * */
import java.util.*;

public class Node {
	private int id;
	private double x;
	private double y;
	private HashMap<Integer, Double> connectingNodes = new HashMap<Integer, Double>();
	
	public Node(double x, double y, int id){
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public int getID(){
		return this.id;
	}
	
	public double getX(){
		return this.x;
	}
	public double getY(){
		return this.y;
	}
	public void addNode(Node node){
		this.connectingNodes.put(node.getID(), Math.sqrt(Math.pow(this.x - node.getX(), 2.0) + Math.pow(this.y - node.getY(), 2.0)));
	}
	
	public double getDistance(int n){
		return connectingNodes.get(n);
	}
}
