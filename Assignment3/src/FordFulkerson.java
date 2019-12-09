import java.io.*;
import java.util.*;




public class FordFulkerson {


    public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
        ArrayList<Integer> Stack = new ArrayList<Integer>();

        //
        //
        //
        //
        //
        ArrayList<Integer> Visited = new ArrayList<Integer>();
        Stack = dfshelper(source, destination, Visited, Stack, graph);

        //
        //

        return Stack;
    }

    private static ArrayList<Integer> dfshelper(Integer cur, Integer destination, ArrayList<Integer> visited, ArrayList<Integer> stk, WGraph graph) {
        visited.add(cur);
        stk.add(cur);
        if (cur.equals(destination))
            return stk;
        ArrayList<Integer> nexts = new ArrayList<Integer>();
        for (Edge e : graph.getEdges()) {
            if (e.nodes[0] == cur)
                if (!visited.contains(e.nodes[1])) {
                    ArrayList<Integer> tempstk=new ArrayList<>();
                    for (int i : stk) tempstk.add(i);
                    ArrayList<Integer> temp = dfshelper(e.nodes[1], destination, visited, tempstk, graph);
                    if (temp != null) {
                        return temp;
                    }
                }
        }
        return null;

    }

    public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath){
        String answer="";
        String myMcGillID = "260832483"; //Please initialize this variable with your McGill ID
        int maxFlow = 0;
        WGraph cur_flow = new WGraph(graph);
        WGraph residual;
        for (Edge e : cur_flow.getEdges())
            e.weight = 0;
        while (true) {
            residual = new WGraph();

            for (Edge e : graph.getEdges()) {
                Edge cur_e = cur_flow.getEdge(e.nodes[0], e.nodes[1]);
                if (cur_e.weight < e.weight) {
                    residual.addEdge(new Edge(e.nodes[0], e.nodes[1], e.weight - cur_e.weight));
                }
                if (cur_e.weight > 0) {
                    residual.addEdge(new Edge(e.nodes[1], e.nodes[0], cur_e.weight));
                }
            }//update residual
            ArrayList<Integer> path = pathDFS(source, destination, residual);
            if (path == null) break;
            int cur_max_flow = Integer.MAX_VALUE;
            for (int i = 0; i < path.size() - 1; i++) {
                Edge e = residual.getEdge(path.get(i), path.get(i + 1));
                cur_max_flow = Math.min(cur_max_flow, e.weight);
            }
            if (cur_max_flow == 0) break;//find current max flow
            maxFlow += cur_max_flow;
            for (int i = 0; i < path.size() - 1; i++) {
                Edge e = cur_flow.getEdge(path.get(i), path.get(i + 1));
                if (e == null) {
                    e = cur_flow.getEdge(path.get(i + 1), path.get(i));
                    cur_flow.setEdge(path.get(i + 1), path.get(i), e.weight - cur_max_flow);
                } else {
                    cur_flow.setEdge(path.get(i), path.get(i + 1), e.weight + cur_max_flow);
                }
            }
        }
        for (Edge e : graph.getEdges())
            graph.setEdge(e.nodes[0], e.nodes[1], 0);

        for (Edge e : cur_flow.getEdges())
            graph.setEdge(e.nodes[0], e.nodes[1], e.weight);
        //
        //

        answer += maxFlow + "\n" + graph.toString();
        writeAnswer(filePath+myMcGillID+".txt",answer);
        System.out.println(answer);
    }


    public static void writeAnswer(String path, String line){
        BufferedReader br = null;
        File file = new File(path);
        // if file doesnt exists, then create it

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(line+"\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        String file = args[0];
        File f = new File(file);
        WGraph g = new WGraph(file);
        fordfulkerson(g.getSource(),g.getDestination(),g,f.getAbsolutePath().replace(".txt",""));
    }
}
