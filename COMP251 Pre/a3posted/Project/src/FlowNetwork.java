
import sun.util.resources.cldr.en.TimeZoneNames_en_ZA;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class FlowNetwork {

    //   The data structures follow what I presented in class.  Use three graphs which
    //   represent the capacities, the flow, and the residual capacities.

    Graph capacities;            // weights are capacities   (G)
    Graph flow;                    // weights are flows        (f)
    Graph residualCapacities;   // weights are determined by capacities (graph) and flow (G_f)

    //   Constructor.   The input is a graph that defines the edge capacities.

    public FlowNetwork(Graph capacities) {

        this.capacities = capacities;

        //  The flow and residual capacity graphs have the same vertices as the original graph.

        flow = new Graph(capacities.getVertices());
        residualCapacities = new Graph(capacities.getVertices());

        //  Initialize the flow and residualCapacity graphs.   The flow is initialized to 0.
        //  The residual capacity graph has only forward edges, with weights identical to the capacities.

        for (String u : flow.getVertices()) {
            for (String v : capacities.getEdgesFrom(u).keySet()) {

                //  Initialize the flow to 0 on each edge

                flow.addEdge(u, v, new Double(0.0));

                //	Initialize the residual capacity graph G_f to have the same edges and capacities as the original graph G (capacities).

                residualCapacities.addEdge(u, v, new Double(capacities.getEdgesFrom(u).get(v)));
            }
        }
    }

	/*
     * Here we find the maximum flow in the graph.    There is a while loop, and in each pass
	 * we find an augmenting path from s to t, and then augment the flow using this path.
	 * The beta value is computed in the augment method. 
	 */

    public void maxFlow(String s, String t) {

        LinkedList<String> path;
        double beta;
        while (true) {
            path = this.findAugmentingPath(s, t);
            if (path == null)
                break;
            else {
                beta = computeBottleneck(path);
                augment(path, beta);
            }
        }
    }

	/*
     *   Use breadth first search (bfs) to find an s-t path in the residual graph.
	 *   If such a path exists, return the path as a linked list of vertices (s,...,t).   
	 *   If no path from s to t in the residual graph exists, then return null.  
	 */

    public LinkedList<String> findAugmentingPath(String s, String t) {

        //  ADD YOUR CODE HERE.
        residualCapacities.bfs(s);
        if (residualCapacities.getParent(t) == null)
            return null;
        else {
            LinkedList<String> res = new LinkedList<>();
            String cur = t;
            while (!cur.equals(s)) {
                res.addFirst(cur);
                cur = residualCapacities.getParent(cur);
            }
            res.addFirst(s);
            return res;
        }

    }

	/*
     *   Given an augmenting path that was computed by findAugmentingPath(),
	 *   find the bottleneck value (beta) of that path, and return it.
	 */

    public double computeBottleneck(LinkedList<String> path) {

        double beta = Double.MAX_VALUE;
        //  Check all edges in the path and find the one with the smallest weight in the
        //  residual graph.   This will be the new value of beta.
        String now, next;
        for (int i = 0; i < path.size() - 1; i++) {
            now = path.get(i);
            next = path.get(i + 1);
            double val = residualCapacities.getEdgesFrom(now).get(next);
            if (val < beta) beta = val;
        }

        //   ADD YOUR CODE HERE.

        return beta;
    }

    //  Once we know beta for a path, we recompute the flow and update the residual capacity graph.

    public void augment(LinkedList<String> path, double beta) {
        Graph tmp = new Graph(flow.getVertices());
        String now, next;
        for (int i = 0; i < path.size() - 1; i++) {
            now = path.get(i);
            next = path.get(i + 1);
            if (flow.getEdgesFrom(next).containsKey(now)) {
                flow.addEdge(now, next, flow.getEdgesFrom(now).getOrDefault(next, 0.0) - beta);
            } else {
                flow.addEdge(now, next, flow.getEdgesFrom(now).getOrDefault(next, 0.0) + beta);
            }
        }
        for (String u : flow.getVertices()) {
            for (String v : capacities.getEdgesFrom(u).keySet()) {
                double f = flow.getEdgesFrom(u).get(v);
                double c = capacities.getEdgesFrom(u).get(v);
                if (f < c) {
                    tmp.addEdge(u, v, c - f);
                }
                if (f > 0) {
                    tmp.addEdge(v, u, f);
                }
            }
        }
        residualCapacities = tmp;

        //   ADD YOUR CODE HERE.

    }

    //  This just dumps out the adjacency lists of the three graphs (original with capacities,  flow,  residual graph).

    public String toString() {
        return "capacities\n" + capacities + "\n\n" + "flow\n" + flow + "\n\n" + "residualCapacities\n" + residualCapacities;
    }

}
