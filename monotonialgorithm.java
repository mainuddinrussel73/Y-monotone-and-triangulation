package offline;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class monotonialgorithm {

    TreeMap<Integer, edge> binarytree = new TreeMap<>();
    ArrayList<Pair<point, point>> diagonal = new ArrayList<>();
    public ArrayList<Pair<point, point>> makeMonotone(polygon p) {

        p.makecounterclock();
        PriorityQueue<vertex> Q = new PriorityQueue<vertex>((v1, v2) -> {
            if (v1.data.Y > v2.data.Y || v1.data.Y == v2.data.Y && v1.data.X > v2.data.X)
                return -1;
            else return 1;
        });

        for (int i = 0; i < p.verteces.size(); i++) {
            Q.add(p.verteces.get(i));
        }

        while (! Q.isEmpty()) {
            try {
               handle(p,Q.poll());
            } catch(Exception e) {
               System.out.print(e.getMessage());
            }
        }
        return diagonal;
    }
    void handle(polygon p, vertex v_i) {

        int i = p.verteces.indexOf(v_i);
        int i_1 = 0;
..  ...  if(i == 0) i_1= p.verteces.size()-1
.......  else i_1= i-1;
        edge e_i, e_j, e_i_1;
        vertex helper_e_i_1, helper_e_j;

        if(v_i.getvertextype(p,p.verteces.indexOf(v_i)).equals( "start") ){
            e_i = p.edges.get(i);
            e_i.helper = v_i;
            binarytree.put(i, e_i);
        }
        else if(v_i.getvertextype(p,p.verteces.indexOf(v_i)).equals(  "end")){
            e_i_1 = binarytree.get(i_1);
            helper_e_i_1 = e_i_1.helper;
            if (helper_e_i_1.getvertextype(p,p.verteces.indexOf(helper_e_i_1)).equals("merge")) {
                diagonal.add(new Pair<>(v_i.data, helper_e_i_1.data));
            }
            binarytree.remove(i_1);
        }
        else if(v_i.getvertextype(p,p.verteces.indexOf(v_i)).equals( "split")) {
            e_j = find_e_j(p,v_i);
            diagonal.add(new Pair<>(v_i.data, e_j.helper.data));
            e_j.helper = v_i;
            e_i = p.edges.get(i);
            binarytree.put(i, e_i);
            e_i.helper = v_i;
        }
        else if(v_i.getvertextype(p,p.verteces.indexOf(v_i)).equals( "merge")) {
            e_i_1 = binarytree.get(i_1);
            helper_e_i_1 = e_i_1.helper;
            if (helper_e_i_1.getvertextype(p, p.verteces.indexOf(helper_e_i_1)).equals("merge")) {
                diagonal.add(new Pair<>(v_i.data, helper_e_i_1.data));
            }
            binarytree.remove(i_1);
            e_j = find_e_j(p,v_i);
            helper_e_j = e_j.helper;
            if (helper_e_j.getvertextype(p, p.verteces.indexOf(helper_e_j)).equals("merge")) {
                diagonal.add(new Pair<>(v_i.data, helper_e_j.data));
            }
            e_j.helper = v_i;

        }

        else if(v_i.getvertextype(p,p.verteces.indexOf(v_i)).equals("regular")) {
            if (v_i.ispolygonright(p, p.verteces.indexOf(v_i))) {
                e_i_1 = binarytree.get(i_1);
                helper_e_i_1 = e_i_1.helper;
                if (helper_e_i_1.getvertextype(p, p.verteces.indexOf(helper_e_i_1)).equals("merge")) {
                    diagonal.add(new Pair<>(v_i.data, helper_e_i_1.data));
                }
                binarytree.remove(i_1);
                e_i = p.edges.get(i);
                binarytree.put(i, e_i);
                e_i.helper = v_i;

            } else {
                e_j = find_e_j(p,v_i);
                helper_e_j = e_j.helper;
                if (helper_e_j.getvertextype(p, p.verteces.indexOf(helper_e_j)).equals("merge")) {
                    diagonal.add(new Pair<>(v_i.data, helper_e_j.data));
                }
                e_j.helper = v_i;
            }


        }
    }

}