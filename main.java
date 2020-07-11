package offline;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        ArrayList<vertex> vertices = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<vertex> ff = new ArrayList<>();
        File file;
        Scanner in =new Scanner(System.in);
        int type;
        type = in.nextInt();
        if(type==1){
            file = new File("/Users/macbookair/IdeaProjects/monotone/src/offline/in");
        }else {
            file = new File("/Users/macbookair/IdeaProjects/monotone/src/offline/in2");
        }

        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int N = sc.nextInt();
        for (int i = 0; i <N ; i++) {
            point p1 = new point(sc.nextDouble(),sc.nextDouble());
            vertex v1  = new vertex(p1);
            vertices.add(v1);
        }
        for (int i = 1; i <N ; i++) {
            edge e1 = new edge(vertices.get(i-1),vertices.get(i));
            edges.add(e1);
        }
        edges.add(new edge(vertices.get(N-1),vertices.get(0)));

        polygon p = new polygon(vertices, edges);
        if(type==1) {
            monotonialgorithm monotonialgorithm = new monotonialgorithm();
            ArrayList<Pair<point, point>> result = monotonialgorithm.makeMonotone(p);
            parse(p, result, ff);
        }else {
            ff = new ArrayList<>();
            triangulation triangulation = new triangulation();
            ArrayList<Pair<point, point>> result1 = triangulation.doTriangulation(p);
            parse(p,result1,ff);
        }

    }
    static void parse(polygon p, ArrayList<Pair<point, point>> result, ArrayList<vertex> ff)  {

        for (Pair d:
                result) {
            ff.add(new vertex((point) d.getKey()));
            ff.add(new vertex((point) d.getValue()));
        }
        PrintStream fileOut = null;
        try {
            fileOut = new PrintStream("/Users/macbookair/IdeaProjects/monotone/src/offline/out");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(ff.size());
        fileOut.println(p.verteces.size());
        for (vertex v:
             p.verteces) {
            System.out.println(v.data.X+" ");
            System.out.println(v.data.Y);
            fileOut.print(v.data.X+" ");
            fileOut.println(v.data.Y);
        }
        fileOut.println(ff.size());
        System.out.println(ff.size());
        for (vertex e: ff) {
            System.out.println(e.data.X+" ");
            System.out.println(e.data.Y);
            fileOut.print(e.data.X+" ");
            fileOut.println(e.data.Y);
        }
        fileOut.close();
    }
}
