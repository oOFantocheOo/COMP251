import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Grader {

	public static void main(String[] args) {
		
		System.out.println("This is the COMP251 Assignment 2 Grader");
		
		System.out.println("Question 1 - Disjoint Sets");
		GraderManager ds_grade = new GraderManager();
		
		
		// Test non-action state
		ds_grade.addGradings(
			linker->{
				linker.info("Testing non-action state; find does not change states");
				DisjointSets ds = new DisjointSets(10);
				boolean nochange = IntStream.range(0, 10)
					.mapToObj(i->i==ds.find(i))
					.reduce((acc,x)-> acc&x ).get();
				return nochange ? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
			},
			
			linker->{
				linker.info("Testing simple union");
				DisjointSets ds = new DisjointSets(2);
				ds.union(0, 1);
				return ds.find(0)==ds.find(1)? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
			},
			
			linker->{
				linker.info("Testing repeated union");
				DisjointSets ds = new DisjointSets(3);
				ds.union(0, 1);
				int f0_0 = ds.find(0);
				int f1_0 = ds.find(1);
				ds.union(0, 1);
				int f0_1 = ds.find(0);
				int f1_1 = ds.find(1);
				return f0_0==f0_1 
						&& f0_1==f1_0 
						&& f1_0==f1_1? new GraderManager.Mark(1, 1): new GraderManager.Mark(0, 1);
			},
			
			linker->{
				linker.info("Testing preservation of representation");
				DisjointSets ds = new DisjointSets(1337);
				int[] representatives = new int[1336];
				for (int i = 0; i < representatives.length; i++) {
					ds.union(i+1, 0);
					representatives[i] = ds.find(0);
				}
				boolean allSame = IntStream.range(1, 1336).allMatch(i->representatives[i]==representatives[i-1]);
				return allSame? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
			},
			linker->{
				linker.info("Testing union by rank");
				DisjointSets ds = new DisjointSets(8);
				// generate rank 2
				ds.union(2, 3);
				ds.union(2, 1);
				ds.union(4, 5);
				ds.union(3, 1);
				ds.union(2, 4);
				int represent2 = ds.find(2);
				// generate rank 1
				ds.union(6, 7);
				// union the 2
				ds.union(2, 6);
				
				return ds.find(2) == represent2? new GraderManager.Mark(2, 2): new GraderManager.Mark(0, 1);
			}
		);
		
		
		ds_grade.grade();
		ds_grade.displayGrades();
		System.out.println("Disjoint sets: "+ds_grade.grade);
		
		System.out.println("Question 2 - MST");
		
		GraderManager mst_grade = new GraderManager();
		
		mst_grade.addGradings(
			linker ->{
				linker.info("Testing isSafe");
				DisjointSets ds0 = new DisjointSets(2);
				DisjointSets ds1 = new DisjointSets(2);
				ds0.union(0, 1);
				boolean isSafe0 = Kruskal.IsSafe(ds0, new Edge(0, 1, 1));
				boolean isSafe1 = Kruskal.IsSafe(ds1, new Edge(0, 1, 1));
				
				return !isSafe0 && isSafe1? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
			},
			linker->{
				linker.info("Testing kruskal; (case: 3-star)");
				WGraph g = new WGraph();
				Edge e0 = new Edge(0, 1, 1);
				Edge e1 = new Edge(0, 2, 1);
				Edge e2 = new Edge(0, 3, 1);
				Edge[] es = new Edge[] {e0, e1, e2};
				Arrays.stream(es).forEach(e->g.addEdge(e));
				WGraph kruskal = Kruskal.kruskal(g);
				
				return kruskal.listOfEdgesSorted().size() == 3
						&& kruskal.listOfEdgesSorted().contains(e0)
						&& kruskal.listOfEdgesSorted().contains(e1)
						&& kruskal.listOfEdgesSorted().contains(e2)? 
								new GraderManager.Mark(1, 1):
								new GraderManager.Mark(0, 1);
			},
			linker->{
				linker.info("Testing kruskal; (case: 4 pan graph)");
				WGraph g = new WGraph();
				Edge[] es = new Edge[] {
						new Edge(0, 1, 1),
						new Edge(0, 2, 1),
						new Edge(1, 3, 1),
						new Edge(2, 3, 1),
						new Edge(3, 4, 1),
				};
				Arrays.stream(es).forEach(e->g.addEdge(e));
				WGraph kruskal = Kruskal.kruskal(g);
				
				List<Edge> edges = kruskal.listOfEdgesSorted();
				return edges.size() == 4
						&& edges.contains(es[4])? 
								new GraderManager.Mark(1, 1):
									new GraderManager.Mark(0, 1);
			},
			linker->{
				linker.info("Testing kruskal; (case: disjoint)");
				WGraph g = new WGraph();
				Edge[] es = new Edge[] {
						new Edge(0, 1, 1),
						new Edge(0, 2, 1),
						new Edge(1, 3, 1),
						new Edge(2, 3, 1),
						new Edge(3, 4, 1),
						
						new Edge(5, 6, 1),
						new Edge(5, 7, 1),
						new Edge(5, 8, 1),
				};
				Arrays.stream(es).forEach(e->g.addEdge(e));
				WGraph kruskal = Kruskal.kruskal(g);
				
				List<Edge> edges = kruskal.listOfEdgesSorted();
				return edges.size() == es.length-1
						&& edges.contains(es[4])
						&& edges.contains(es[5])
						&& edges.contains(es[6])
						&& edges.contains(es[7])? 
								new GraderManager.Mark(1, 1):
									new GraderManager.Mark(0, 1);
			},
			linker->{
				linker.info("Testing kruskal; (case: dumbbell)");
				WGraph g = new WGraph();
				Edge[] es = new Edge[] {
						new Edge(0, 1, 1),
						new Edge(0, 2, 1),
						new Edge(1, 3, 1),
						new Edge(2, 3, 1),
						
						new Edge(3, 4, 1),
						
						new Edge(4, 5, 1),
						new Edge(4, 6, 1),
						new Edge(5, 7, 1),
						new Edge(6, 7, 1),
				};
				Arrays.stream(es).forEach(e->g.addEdge(e));
				WGraph kruskal = Kruskal.kruskal(g);
				
				List<Edge> edges = kruskal.listOfEdgesSorted();
				return edges.size() == es.length-2
						&& edges.contains(es[4])? 
								new GraderManager.Mark(1, 1):
									new GraderManager.Mark(0, 1);
			},
			linker->{
				linker.info("Testing kruskal; (case: dumbbell with distractors)");
				WGraph g = new WGraph();
				Edge[] es = new Edge[] {
						new Edge(0, 1, 1),
						new Edge(0, 2, 1),
						// horizontal distractor
						new Edge(0, 3, 1),
						new Edge(1, 3, 1),
						new Edge(2, 3, 1),
						
						new Edge(3, 4, 1),
						
						new Edge(4, 5, 1),
						new Edge(4, 6, 1),
						//vertical distractor
						new Edge(5, 6, 1),
						new Edge(5, 7, 1),
						new Edge(6, 7, 1),
				};
				Arrays.stream(es).forEach(e->g.addEdge(e));
				WGraph kruskal = Kruskal.kruskal(g);
				
				List<Edge> edges = kruskal.listOfEdgesSorted();
				return edges.size() == es.length-4
						&& edges.contains(es[5])? 
								new GraderManager.Mark(1, 1):
									new GraderManager.Mark(0, 1);
			}
		);
		
		mst_grade.grade();
		mst_grade.displayGrades();
		System.out.println("MST: "+ mst_grade.grade);
		System.out.println("Question 3");
		
		GraderManager greedy_grade = new GraderManager();
		int[] weights = new int[] {23, 60, 14, 25, 7}; 
		int[] deadlines = new int[] {3, 1, 2, 1, 3};
		int m = weights.length;
		
		
		
		greedy_grade.addGradings(
			linker->{
				linker.info("Testing correct length of output");
				HW_Sched schedule =  new HW_Sched(weights, deadlines, m);
				int[] res = schedule.SelectAssignments();
				
				return res.length==schedule.lastDeadline? new GraderManager.Mark(1, 1): new GraderManager.Mark(0, 1);
			},
			linker->{
				linker.info("Testing for completion before deadline");
				HW_Sched schedule =  new HW_Sched(weights, deadlines, m);
				int[] res = schedule.SelectAssignments();
				boolean onTime = true;
				for (int i = 0; i < schedule.Assignments.size(); ++i) {
					Assignment asg = schedule.Assignments.get(i);
					for (int time = 0; time < res.length; ++time) {
						if (res[time] == asg.number) {
							onTime = time < asg.deadline;
						}
					}
				}
				return onTime? new GraderManager.Mark(2, 2): new GraderManager.Mark(0, 2);
			},
			linker->{
				linker.info("Testing scheduling; (case: no conflicts)");
				// should be no conflicts
				int[] w = new int[] {3,2,1};
				int[] t = new int[] {3,2,1};
				HW_Sched schedule = new HW_Sched(w, t, w.length);
				int[] res = schedule.SelectAssignments();
				int[] target = new int[] {2,1,0};
				boolean matchTarget = IntStream.range(0, res.length).allMatch(i-> res[i]==target[i]);
				return matchTarget? new GraderManager.Mark(2,2): new GraderManager.Mark(0,2);
			},
			linker->{
				linker.info("Testing scheduling; (case: weight advantage)");
				// conflict for first slot
				int[] w = new int[] {3,2,9};
				int[] t = new int[] {2,1,1};
				HW_Sched schedule = new HW_Sched(w, t, w.length);
				int[] res = schedule.SelectAssignments();
				int score = get_total_score(schedule, res);
				return score == 12? new GraderManager.Mark(2,2): new GraderManager.Mark(0,2);
			},
			linker->{
				linker.info("Testing scheduling; (case: not all important things are urgent)");
				int[] w = new int[] {1,1,100};
				int[] t = new int[] {1,2,3};
				HW_Sched schedule = new HW_Sched(w, t, w.length);
				int[] res = schedule.SelectAssignments();
				int score = get_total_score(schedule, res);
				return score == 102? new GraderManager.Mark(1,1): new GraderManager.Mark(0,1);
			},
			linker->{
				linker.info("Testing scheduling; (case: not all urgent things are important)");
				int[] w = new int[] {1,100,100};
				int[] t = new int[] {1,2,2};
				HW_Sched schedule = new HW_Sched(w, t, w.length);
				int[] res = schedule.SelectAssignments();
				int score = get_total_score(schedule, res);
				return score == 200? new GraderManager.Mark(1,1): new GraderManager.Mark(0,1);
			},
			linker->{
				linker.info("Testing scheduling; (case: homework overwhelming)");
				int[] w = new int[] {9,19,9,9,19,9};
				int[] t = new int[] {2,2,2,2,2,2};
				HW_Sched schedule = new HW_Sched(w, t, w.length);
				int[] res = schedule.SelectAssignments();
				int score = get_total_score(schedule, res);
				return score==38? new GraderManager.Mark(1,1): new GraderManager.Mark(0,1);
			},
			linker->{
				linker.info("Testing scheduling; (case: slacker's paradise)");
				int[] w = new int[] {10,10};
				int[] t = new int[] {3,100};
				HW_Sched schedule = new HW_Sched(w, t, w.length);
				int[] res = schedule.SelectAssignments();
				int score = get_total_score(schedule, res);
				return score==20? new GraderManager.Mark(1,1): new GraderManager.Mark(0,1);
			},
			linker->{
				linker.info("Testing scheduling; (case: overlapping conflicts)");
				int[] w = new int[] {1,2,3,4};
				int[] t = new int[] {1,1,2,2};
				HW_Sched schedule = new HW_Sched(w, t, w.length);
				int[] res = schedule.SelectAssignments();
				int score = get_total_score(schedule, res);
				return score==6? new GraderManager.Mark(1,2):
					   score==7? new GraderManager.Mark(2,2):
						   new GraderManager.Mark(0,2);
			}
		);
		
		greedy_grade.grade();
		greedy_grade.displayGrades();
		System.out.println("Greedy: "+greedy_grade.grade);
		
		
		System.out.println("Total Score: "+ 
				(ds_grade.reweightedGrade(15).combineWith(mst_grade.reweightedGrade(25), greedy_grade.reweightedGrade(20))));
	}

	static int get_total_score (HW_Sched schedule, int[] res) {
		int score = 0;
		for (int time = 0; time < res.length; ++time) {
			for (int i = 0; i < schedule.Assignments.size(); ++i) {
				Assignment asg = schedule.Assignments.get(i);
				if (asg.number == res[time]) {
					score += asg.weight;
				}
			}
		}
		return score;
	}
}
