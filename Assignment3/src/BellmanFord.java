public class BellmanFord{


    /**
     * Utility class. Don't use.
     */
    public class BellmanFordException extends Exception{
        private static final long serialVersionUID = -4302041380938489291L;
        public BellmanFordException() {super();}
        public BellmanFordException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception class for BellmanFord algorithm
     *
     * Use this to specify a negative cycle has been found
     */
    public class NegativeWeightException extends BellmanFordException{
        private static final long serialVersionUID = -7144618211100573822L;
        public NegativeWeightException() {super();}
        public NegativeWeightException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception class for BellmanFord algorithm
     *
     * Use this to specify that a path does not exist
     */
    public class PathDoesNotExistException extends BellmanFordException{
        private static final long serialVersionUID = 547323414762935276L;
        public PathDoesNotExistException() { super();}
        public PathDoesNotExistException(String message) {
            super(message);
        }
    }

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    BellmanFord(WGraph g, int source) throws BellmanFordException {
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         *
         *  When throwing an exception, choose an appropriate one from the ones given above
         */

        distances = new int[g.getNbNodes()];

        double[] distances_in_double = new double[g.getNbNodes()];
        predecessors = new int[g.getNbNodes()];
        for (int i = 0; i < g.getNbNodes(); i++) {
            distances_in_double[i] = Double.POSITIVE_INFINITY;
            predecessors[i] = -1;
        }
        distances[source] = 0;
        distances_in_double[source] = 0;
        predecessors[source] = source;
        this.source = source;


        for (int i = 0; i <= g.getNbNodes(); i++)
            for (Edge e : g.getEdges())
                relax(e.nodes[0], e.nodes[1], g, distances_in_double);
        for (Edge e : g.getEdges())
            if (distances_in_double[e.nodes[1]] > distances_in_double[e.nodes[0]] + e.weight )
                throw new NegativeWeightException("Negative cycle detected");

        for (int i = 0; i < g.getNbNodes(); i++) {
            if (distances_in_double[i] == Double.POSITIVE_INFINITY)
                distances[i] = Integer.MAX_VALUE;
            else distances[i] = (int) distances_in_double[i];
        }
        /* YOUR CODE GOES HERE */
    }

    private void relax(int u, int v, WGraph g, double[] d) {
        Edge e = g.getEdge(u, v);

        if (d[v] > d[u] + e.weight) {
            d[v] = d[u] + e.weight;
            predecessors[v] = u;
        }

    }

    public int[] shortestPath(int destination) throws BellmanFordException {
        /*Returns the list of nodes along the shortest path from
         * the object source to the input destination
         * If not path exists an Exception is thrown
         * Choose appropriate Exception from the ones given
         */

        /* YOUR CODE GOES HERE (update the return statement as well!) */
        int size = 1;
        int cur = destination;
        int[] res = new int[size];
        res[0] = cur;
        while (predecessors[cur] != -1 && cur != source) {
            cur = predecessors[cur];
            int[] tmp = new int[size];
            for (int i = 0; i < size; i++)
                tmp[i] = res[i];
            size++;
            res = new int[size];
            for (int i = 0; i < size - 1; i++)
                res[i + 1] = tmp[i];
            res[0] = cur;
        }
        if (predecessors[cur] == -1)
            throw new PathDoesNotExistException("Path does not exist");

        return res;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }

    }
}
