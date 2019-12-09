import sun.java2d.opengl.WGLGraphicsConfig;

import java.util.*;

public class Kruskal {

    public static WGraph kruskal(WGraph g) {

        /* Fill this method (The statement return null is here only to compile) */

        int nb = g.getNbNodes();

        WGraph res=new WGraph();

        DisjointSets d = new DisjointSets(nb);
        int cur = 0;
        ArrayList<Edge> l = g.listOfEdgesSorted();
        for (int i = 0; i < nb - 1; i++) {
            while (!IsSafe(d, l.get(cur)))
                cur += 1;
            d.union(l.get(cur).nodes[0], l.get(cur).nodes[1]);
            res.addEdge(l.get(cur));
            cur += 1;
        }
        return res;
    }

    public static Boolean IsSafe(DisjointSets p, Edge e) {

        return !(p.find(e.nodes[0]) == p.find(e.nodes[1]));
        //already made sure this is a light edge in kruskal(), so here it only checks if the vertices connected by the edge is in the same set

    }

    public static void main(String[] args) {

        String file = args[0];
        WGraph g = new WGraph(file);
        WGraph t = kruskal(g);
        System.out.println(t);

    }
}
