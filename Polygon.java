package offline;

import javafx.util.Pair;
import java.util.ArrayList;

class point {
    double X;double Y;
    point(double x,double y){ this.Y = y;this.X = x; }
    @Override public String toString() {
        return X+" "+Y;
    }
}
class vertex{
    point data;
    double a, b, c;

    vertex(point p1){ data = p1; }
	public double calculatedistance(polygon p){
		
		double distance = 0;
        for (int i = 0; i < verteces.size(); i++) {
            distance  += (verteces.get((i+1)%verteces.size()).data.X - verteces.get(i).data.X)* (verteces.get((i+1)%verteces.size()).data.Y + verteces.get(i).data.Y);
        }
        return distance ;
    }
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
    public void  sweepline(vertex p1, vertex p2) {
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
    public boolean intersectswap(double sweeep,polygon p,int index) {
        if(sweeep >= p.verteces.get((index+1)%p.verteces.size()).data.Y){
        	 if(sweepY <= p.verteces.get(index).data.Y)return true;
        }return false;
    }
    public boolean rightside(polygon p ,int index, edge e, double Y) {
        p.verteces.get(index).sweepline(p.verteces.get(index),p.verteces.get((index+1)%p.verteces.size()));
        return p.verteces.get(index).xvalue(Y) > p.edges.get(p.edges.indexOf(e)).v_i.xvalue(Y);
    }
    public boolean leftside(polygon p ,int index,vertex v) {
        p.verteces.get(index).sweepline(p.verteces.get(index),p.verteces.get((index+1)%p.verteces.size()));
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
       
		if(calculatedistance(this) <0) return ;
        vertex temp;
        for (int i = 1; i <= verteces.size() / 2; i++) {
        	temp = verteces.get(i);
            verteces.set(i, verteces.get(verteces.size()-i));
            verteces.set(verteces.size()-i, temp);
        }
        
    }

    public boolean isconvex(polygon p,int i) {
        int i_1 = i - 1;
        if (i_1< 0) i_1+= p.verteces.size();
        int i_i_1= (i + 1) % p.verteces.size();
        boolean b=false;
		 if((p.verteces.get(i).data.X - .p.verteces.get(i_1).data.X) * (p.verteces.get(i_i_1).data.Y - p.verteces.get(i_1).data.Y) - (p.verteces.get(i_i_1).data.X - p.verteces.get(i_1).data.X) * (p.verteces.get(i).data.Y -p.verteces.get(i_1).data .Y)>0) b=true;
		 else b=false;
        return b;
    }

    Pair<Integer,Integer> findtoplow(polygon p){
    	point top;
        point bottom  =  top = p.verteces.get(0).data;
        int k =0;
        int j = 0;
        for (int i = 0; i < p.verteces.size(); i++) {
            point temp = p.verteces.get(i).data;
            if (bottom .Y < temp.Y) {
                bottom = temp;
                k = i;
            }
            else if (top.Y > temp.Y) {
                top = temp;
                j = i;
            }
        }
        return new Pair<>(new Integer(k),new Integer(j));
    }
    void partition(polygon p,int k,int j){
        int i = j;
        for (i=0;i <= j;i++) {
            leftchain.add(p.verteces.get(i));
            if(i>=p.verteces.size()) i %= p.verteces.size();
        }
       
        i = k - 1 + p.verteces.size();
        if(i>=p.verteces.size())i %= p.verteces.size();

        for (i=0;i<= k;i=-1+p.verteces.get(i)){
            rightchain.add(p.verteces.get(i));
            if(i>=p.verteces.size())i %= p.verteces.size();
        }
    }

    void sortvertex(polygon p) {


        Pair<Integer,Integer> pair = findtoplow(p);
        int top = pair.getKey().intValue();
        int bottom  = pair.getValue().intValue();
        partition(p,top,bottom );
        int i = 0;
        int j = 0;

        while (i < leftchain.size() || j < rightchain.size()) {

                if(i < leftchain.size() && j < rightchain.size()){
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
                else if (j >= rightchain.size()) {
                    sortedlist.add(leftchain.get(i));
                    i++;
                }
                else if (i >= leftchain.size()) {
                    sortedlist.add(rightchain.get(j));
                    j++;
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
