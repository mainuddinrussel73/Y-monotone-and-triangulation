package offline;

import javafx.util.Pair;
import java.util.ArrayList;

class point {
    int index;
    double X;double Y;
    point(double x,double y,int index){ this.Y = y;this.X = x;this.index = index; }
    @Override public String toString() {
        return X+" "+Y;
    }
}
class vertex{
    point data;
    double a, b, c;

    vertex(point p1){ data = p1; }

    public String getvertextype(polygon p,int index) {
        point p_cur = p.verteces.get(index).data;
        point p_prev;
        if(index==0) p_prev = p.verteces.get(p.verteces.size()-1 ).data;
        else p_prev = p.verteces.get( index-1 ).data;
        point p_next = p.verteces.get((index+1) % p.verteces.size()).data;
        if (p_prev.Y < p_cur.Y && p_next.Y < p_cur.Y) {
            if (p.isconvex(p,index)) return "start";
            else return "split";
        } else if (p_prev.Y > p_cur.Y && p_next.Y > p_cur.Y) {
            if (p.isconvex(p,index)) return "end";
            else return "merge";
        } else return "regular";
    }


    public boolean ispolygonright(polygon p, int index) {

        point p_cur = p.verteces.get(index).data;
        point p_prev;
        if(index==0) p_prev = p.verteces.get(p.verteces.size()-1 ).data;
        else p_prev = p.verteces.get( index-1 ).data;
        point p_next = p.verteces.get((index+1) % p.verteces.size()).data;
        if (p_prev.Y > p_cur.Y && p_next.Y < p_cur.Y) return true;
        else return false;

    }
    public void  line(vertex p1, vertex p2) {
        this.a = p1.data.Y- p2.data.Y;
        this.b = p2.data.X - p1.data.X;
        this.c = p1.data.X*p2.data.Y - p2.data.X*p1.data.Y;
    }
    public double xvalue(double y) {
        return (-c - b*y)/a;
    }

}
class edge{
    vertex v_i;
    vertex v_i_1;
    vertex helper;
    edge(vertex v1,vertex v2){
        v_i = v1;
        v_i_1 = v2;
    }
    public boolean intersectswap(double sweepY,polygon p,int index) {
        return (sweepY >= p.verteces.get(index).data.Y && sweepY <= p.verteces.get((index+1)%p.verteces.size()).data.Y) ||
                (sweepY >= p.verteces.get((index+1)%p.verteces.size()).data.Y && sweepY <= p.verteces.get(index).data.Y);
    }
    public boolean rightside(polygon p ,int index, edge e, double Y) {
        p.verteces.get(index).line(p.verteces.get(index),p.verteces.get((index+1)%p.verteces.size()));
        return p.verteces.get(index).xvalue(Y) > p.edges.get(p.edges.indexOf(e)).v_i.xvalue(Y);
    }
    public boolean leftside(polygon p ,int index,vertex v) {
        p.verteces.get(index).line(p.verteces.get(index),p.verteces.get((index+1)%p.verteces.size()));
        return p.verteces.get(index).xvalue(v.data.Y) < v.data.X;
    }
}
class polygon {
    ArrayList<vertex> verteces = new ArrayList<>();
    ArrayList<edge> edges = new ArrayList<>();
    ArrayList<vertex> leftchain = new ArrayList<>();
    ArrayList<vertex> rightchain = new ArrayList<>();
    ArrayList<vertex> sortedlist = new ArrayList<>();

    polygon(ArrayList<vertex> vertices ,ArrayList<edge>edges){
        for (vertex v: vertices) {
            this.verteces.add(v);
        }
        for (edge e: edges) {
            this.edges.add(e);
        }
    }

    public void makecounterclockwise() {

        double sum = 0;
        for (int i = 0; i < verteces.size(); i++) {
            sum += (verteces.get((i+1)%verteces.size()).data.X - verteces.get(i).data.X)*
                    (verteces.get((i+1)%verteces.size()).data.Y + verteces.get(i).data.Y);
        }

        if (sum>0) {
            vertex temp;
            for (int i = 1; i <= verteces.size() / 2; i++) {
                temp = verteces.get(i);
                verteces.set(i, verteces.get(verteces.size()-i));
                verteces.set(verteces.size()-i, temp);
            }
        }
    }

    public boolean isconvex(polygon p,int i) {
        int prev = i - 1;
        if (prev < 0) prev += p.verteces.size();
        int next = (i + 1) % p.verteces.size();

        if (leftturn(p.verteces.get(prev).data, p.verteces.get(i).data, p.verteces.get(next).data)){
            return true;
        }return false;
    }

    boolean leftturn(point p0, point p1, point p2) {
        if((p1.X - p0.X) * (p2.Y - p0.Y) - (p2.X - p0.X) * (p1.Y - p0.Y)>0)return true;
        else return false;
    }

    Pair<Integer,Integer> findtoplow(polygon p){
        point Bottom  = p.verteces.get(0).data;
        point Top = p.verteces.get(0).data;
        int top_idx =0;
        int bot_idx = 0;
        for (int i = 0; i < p.verteces.size(); i++) {
            point temp = p.verteces.get(i).data;
            if (Top.Y < temp.Y) {
                Top = temp;
                top_idx = i;
            }
            if (Bottom.Y > temp.Y) {
                Bottom = temp;
                bot_idx = i;
            }
        }
        return new Pair<>(new Integer(top_idx),new Integer(bot_idx));
    }
    void partition(polygon p,int top_idx,int bot_idx){
        int i = top_idx;
        while (i != bot_idx) {
            leftchain.add(p.verteces.get(i));
            i++;
            if(i>=p.verteces.size()) i %= p.verteces.size();
        }
        leftchain.add(p.verteces.get(i));
        i = top_idx - 1 + p.verteces.size();
        if(i>=p.verteces.size())i %= p.verteces.size();

        while (i != bot_idx) {
            rightchain.add(p.verteces.get(i));
            i = i - 1 + p.verteces.size();
            if(i>=p.verteces.size())i %= p.verteces.size();
        }
    }

    void sortvertex(polygon p) {


        Pair<Integer,Integer> pair = findtoplow(p);
        int top_idx = pair.getKey().intValue();
        int bot_idx = pair.getValue().intValue();
        partition(p,top_idx,bot_idx);
        int i = 0;
        int j = 0;

        while (i < leftchain.size() || j < rightchain.size()) {
            if (i >= leftchain.size()) {
                sortedlist.add(rightchain.get(j));
                j++;
            } else if (j >= rightchain.size()) {
                sortedlist.add(leftchain.get(i));
                i++;
            } else {
                point a, b;
                a = leftchain.get(i).data;
                b = rightchain.get(j).data;

                if (a.Y > b.Y) {
                    sortedlist.add(leftchain.get(i));
                    i++;
                } else if (b.Y > a.Y) {
                    sortedlist.add(rightchain.get(j));
                    j++;
                } else {
                    if (a.X < b.X) {
                        sortedlist.add(leftchain.get(i));
                        i++;
                    } else {
                        sortedlist.add(rightchain.get(j));
                        j++;
                    }
                }
            }
        }

    }

    boolean findchain(polygon p,point x) {
        for (int i = 0; i < p.rightchain.size(); i++) {
            if (x.X == p.rightchain.get(i).data.X && x.Y == p.rightchain.get(i).data.Y  ) return true;
        }
        return false;
    }
}
