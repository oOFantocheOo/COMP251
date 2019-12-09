package a3solutions;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.*;

public class FlowNetwork {

	//   The data structures follow what I presented in class.  Use three graphs which 
	//   represent the capacities, the flow, and the residual capacities.
	
	Graph capacities;      		// weights are capacities   (G)
	Graph flow;            		// weights are flows        (f)
	Graph residualCapacities;   // weights are determined by capacities (graph) and flow (G_f)
	
	//   Constructor.   The input is a graph that defines the edge capacities.
	
	public FlowNetwork(Graph capacities){
				
		this.capacities    = capacities;
		
		//  The flow and residual capacity graphs have the same vertices as the original graph.
		
		flow               = new Graph( capacities.getVertices() );
		residualCapacities = new Graph( capacities.getVertices() );
		
		//  Initialize the flow and residualCapacity graphs.   The flow is initialized to 0.  
		//  The residual capacity graph has only forward edges, with weights identical to the capacities. 

		for (String u : flow.getVertices()){
			for (String v : capacities.getEdgesFrom(u).keySet() ){
				
				//  Initialize the flow to 0 on each edge
				
				flow.addEdge(u, v, new Double(0.0));
				
				//	Initialize the residual capacity graph G_f to have the same edges and capacities as the original graph G (capacities).
				
				residualCapacities.addEdge(u, v, new Double( capacities.getEdgesFrom(u).get(v) ));
			}
		}
	}

	/*
	 * Here we find the maximum flow in the graph.    There is a while loop, and in each pass
	 * we find an augmenting path from s to t, and then augment the flow using this path.
	 * The beta value is computed in the augment method. 
	 */
	
	public void  maxFlow(String s,  String t){
		
		LinkedList<String> path;
		double beta;
		while (true){
			path = this.findAugmentingPath(s, t);
			if (path == null)
				break;
			else{
			    System.out.println("Path1:" + path.toString());
				beta = computeBottleneck(path);
				System.out.println("Path2:" + path.toString());
				augment(path, beta);				
			}
		}	
	}
	
	/*
	 *   Use breadth first search (bfs) to find an s-t path in the residual graph.    
	 *   If such a path exists, return the path as a linked list of vertices (s,...,t).   
	 *   If no path from s to t in the residual graph exists, then return null.  
	 */
	
	public LinkedList<String>  findAugmentingPath(String s, String t){

		//  ADD YOUR CODE HERE.
	
		LinkedList<String> augmentingPath = new LinkedList<String>();
		residualCapacities.bfs(s);
		if(residualCapacities.getParent(t) != null){
			String newT = t;
			while( ! newT.equals(s)){
				augmentingPath.addFirst(newT);
				newT = residualCapacities.getParent(newT);
			}
			augmentingPath.addFirst(s);
			return augmentingPath;		
		}
		else
			return null;   //   stub.  delete this line.
	}

	
	/*
	 *   Given an augmenting path that was computed by findAugmentingPath(), 
	 *   find the bottleneck value (beta) of that path, and return it.
	 */
	
	public double computeBottleneck(LinkedList<String>  path){

		double beta = Double.MAX_VALUE;

		//  Check all edges in the path and find the one with the smallest weight in the
		//  residual graph.   This will be the new value of beta.

		//   ADD YOUR CODE HERE.
		
		Iterator<String> iter = path.iterator();
		String previous, next;
		previous = iter.next();
		while(iter.hasNext()){
			next = iter.next();
			
			HashMap<String, Double> VerticesFromSTR = residualCapacities.getEdgesFrom(previous);
			Double weight = VerticesFromSTR.get(next);
			if(beta > weight){
				beta = weight;
			}
			previous = next;
		}
		return beta;
	}
	
	//  Once we know beta for a path, we recompute the flow and update the residual capacity graph.

	public void augment(LinkedList<String>  path,  double beta){

		//   ADD YOUR CODE HERE.
		
		//Compute the new flow
		LinkedList<String> p = path;
		String terminal = p.getLast();
		while(!p.getFirst().equals(terminal)){
			String previousVertex = p.removeFirst();
			String nextVertex = p.getFirst();
			HashMap <String, Double> edgesFromPrevious = flow.getEdgesFrom(previousVertex);
			HashMap <String, Double> edgesFromNext = flow.getEdgesFrom(nextVertex);
			//Check if there is an edge in the flow from previousVertex to nextVertex
			Boolean edgeInFlow = false;
			if(edgesFromPrevious.containsKey(nextVertex)){
				edgeInFlow = true;
				//If the edge exist, then change its weight in the flow
				Double oldWeight = edgesFromPrevious.get(nextVertex);
				Double newWeight = beta + oldWeight;
				flow.addEdge(previousVertex, nextVertex, newWeight);
			}
			//If the edge doesn't exist, check whether there is a backward edge
			else if(edgesFromNext.containsKey(previousVertex)){
				edgeInFlow = false;
				//If backward edge exists, change its weight in the flow
				Double oldWeight = edgesFromNext.get(previousVertex);
				Double newWeight = oldWeight - beta;
				flow.addEdge(nextVertex, previousVertex, newWeight);
				
			}
			else{
				System.out.println(" There isn't any path between" + previousVertex + "and" + nextVertex);
			}
		
		//Compute the new residual capacities
		String previousVertex2, nextVertex2;
		if(edgeInFlow == true){
			previousVertex2 = previousVertex;
			nextVertex2 = nextVertex;
		}
		else{
			previousVertex2 = nextVertex;
			nextVertex2 = previousVertex;
		}
		
		Double edgeWeightInFlow = flow.getEdgesFrom(previousVertex2).get(nextVertex2);
		Double totalCapacity = capacities.getEdgesFrom(previousVertex2).get(nextVertex2);
		Double newCapacity = totalCapacity - edgeWeightInFlow;
		Double newCapacityBackwards = edgeWeightInFlow;
		
		
		HashMap<String, Double> residualEdgeFrom = residualCapacities.getEdgesFrom(previousVertex2);
			if(residualEdgeFrom.containsKey(nextVertex2)){
				if(newCapacity.equals(0.0)){
					//Remove edge
					residualEdgeFrom.remove(nextVertex2);	
				}
				else{
					residualEdgeFrom.put(nextVertex2, newCapacity);
				}
			}
			else{
				//Do nothing
				if(newCapacity.equals(0.0));
				else{
					residualEdgeFrom.put(nextVertex2, newCapacity);
				}
			}
		
			HashMap<String, Double> residualEdgeFromOppo = residualCapacities.getEdgesFrom(nextVertex2);
			if(residualEdgeFromOppo.containsKey(previousVertex2)){
				if(newCapacityBackwards.equals(0.0)){
					residualEdgeFromOppo.remove(previousVertex2);
				}
				else{
					residualEdgeFromOppo.put(previousVertex2, newCapacityBackwards);
				}
			}
			else{
				if (newCapacityBackwards.equals(0.0));
				else{
					residualEdgeFromOppo.put(previousVertex2, newCapacityBackwards);
				}
			}
		}		
	}
	

	//  This just dumps out the adjacency lists of the three graphs (original with capacities,  flow,  residual graph).
	
	public String toString(){
		return "capacities\n" + capacities + "\n\n" + "flow\n" + flow + "\n\n" + "residualCapacities\n" + residualCapacities;
	}
	
}
