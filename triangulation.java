package offline;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Stack;

public class triangulation {

    ArrayList<Pair<point, point >> diagonal = new ArrayList<>();
    ArrayList<Pair<point,point>> doTriangulation(polygon p){

        p.sortvertex(p);
        for (vertex v:
             p.sortedlist) {
            System.out.println(v.data.index);
        }
        System.out.println("begin ..........");
        Stack<point> st = new Stack<>();
        st.push(p.sortedlist.get(0).data);
        st.push(p.sortedlist.get(1).data);
        for (point point:
                st) {
            System.out.println(point.index);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        for(int i = 2; i < p.sortedlist.size() - 1; i++){
            point t = st.peek();
            point cur = p.sortedlist.get(i).data;
            point last ;
            if(p.findchain(p,cur) == p.findchain(p,t)){
                last = st.peek();
                st.pop();
                while(!st.empty()){
                    t = st.peek();
                    boolean is_right = p.findchain(p,cur);
                    double val = (last.X - cur.X) * (t.Y - cur.Y) - (last.Y - cur.Y) * (t.X - cur.X);
                    boolean turn = (val>0) ? true : false;
                    if(is_right && turn || !is_right && !turn){
                        diagonal.add(new Pair(cur,t));
                        st.pop();
                        last = t;
                    }
                    else break;
                }

                st.push(last);
                st.push(cur);
                for (point point:
                        st) {
                    System.out.println(point.index);
                }
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
            }
            else{
                while(!st.empty()){
                    t = st.peek();
                    st.pop();
                    diagonal.add(new Pair<>(cur,t));
                }

                st.push(p.sortedlist.get(i-1).data);
                st.push(p.sortedlist.get(i).data);
                for (point point:
                        st) {
                    System.out.println(point.index);
                }
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
            }

        }
        if(!st.empty()) st.pop();
        return diagonal;
    }
}
