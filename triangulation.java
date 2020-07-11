package offline;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Stack;

public class triangulation {

    ArrayList<Pair<point, point >> diagonal = new ArrayList<>();
    ArrayList<Pair<point,point>> doTriangulation(polygon p){

        p.sortvertex(p);
        Stack<point> s= new Stack<>();
        s.push(p.sortedlist.get(0).data);
        s.push(p.sortedlist.get(1).data);
        for(int i = 2; i < p.sortedlist.size() - 1; i++){
            point t = s.peek();
            point cur = p.sortedlist.get(i).data;
            point top ;
            if(p.findchain(p,cur) == p.findchain(p,t)){
                top = s.peek();
                s.pop();
                while(!s.empty()){
                    t = s.peek();
                    boolean is_right = p.findchain(p,cur);
                    boolean turn ;
                    if((top.X - cur.X) * (t.Y - cur.Y) - (top.Y - cur.Y) * (t.X - cur.X)>0) )
                        turn=true 
                    else turn=false;
                    
                    if(!is_right && turn) {
                    	break;
                    }
                    if(is_right && !turn){
                        break;
                    }
                    diagonal.add(new Pair(cur,t));
                    s.pop();
                    top = t;
                    
                }
                s.push(top);
                s.push(cur);
            }
            else{
                while(!s.empty()){
                    t = st.peek();
                    s.pop();
                    diagonal.add(new Pair<>(cur,t));
                }
                s.push(p.sortedlist.get(i-1).data);
                s.push(p.sortedlist.get(i).data);
            }
        }
        if(!s.empty()) s.pop();
        return diagonal;
    }
}
