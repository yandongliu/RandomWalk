import java.io.*;
import java.util.*;

class Pair {
  String s;
  double w;
  public Pair(String s, int w) {
    this.s = s;
    this.w = w;
  }
  public Pair(String s, double w) {
    this.s = s;
    this.w = w;
  }
}

public class walk_rooted {
  double scale = 10000.0;
  double epsilon = 0.01;
  HashMap<String, Double> mapProb = new HashMap<String, Double>();
  HashMap<String, ArrayList<Pair>> mapLink = new HashMap<String, ArrayList<Pair>>();
  HashSet<String> setStart = new HashSet<String>();

  public walk_rooted(String log_file, String start_file, int MAX, int min_conn, double root_prob) {
    try {
      System.err.println("===read startpoints file===:"+start_file);
      read_startpoints(start_file);
      System.err.println("===read log file===:"+log_file);
      read_log(log_file, min_conn);
      System.err.println("===modify root probs===");
      modifyRootProb(root_prob);
      System.err.println("===walk===");
      walk(MAX);
      output();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  void read_startpoints(String fn) throws IOException {
    try {
      BufferedReader br = new BufferedReader(new FileReader(fn));
      String s;
      //double total = Double.parseDouble(br.readLine());
      double total=0;
      while((s = br.readLine())!=null) {
        String[] a = s.split("\t");
        mapProb.put(a[0], Double.parseDouble(a[1]));
        total+=Double.parseDouble(a[1]);
        setStart.add(a[0]);
      } 
      for(String node:mapProb.keySet()) {
        mapProb.put(node, mapProb.get(node)/total*scale);
      }
      br.close();
    } catch (IOException ex) {
      throw ex;
    }
  } 

  void read_log(String fn_log, int min_conn) throws IOException {
    try {
      BufferedReader br = new BufferedReader(new FileReader(fn_log));
      String s;
      int cnt=0;
      while((s = br.readLine())!=null) {
        //System.out.println("--"+s);
        s = s.toLowerCase();
        String[] a = s.split("\t");
        int w = Integer.parseInt(a[2]);
        if( w < min_conn) continue;
        String q = a[0], u = a[1]; 
        if(u.endsWith("/")) u = u.substring(0,u.length()-1);
        q = "q_"+q;
        u = "u_"+u;
        //System.out.println(q);
        //System.out.println(u);
        if (!mapLink.containsKey(q)) {
          mapLink.put(q, new ArrayList<Pair>());
        }
        if (!mapLink.containsKey(u)) {
          mapLink.put(u, new ArrayList<Pair>());
        }
        mapLink.get(q).add(new Pair(u,w));
        mapLink.get(u).add(new Pair(q,w));
        cnt++;
        if(cnt%100000==0) {
          System.err.println("read "+cnt+" lines.");
        }
      }
      br.close();
    } catch (IOException ex) {
      throw ex;
    }
  }

  void modifyRootProb(double root_prob) {
    int cnt=0;
    System.err.println("#nodes:"+mapLink.size());
    for(String node: mapLink.keySet()) {
      ArrayList<Pair> links = mapLink.get(node);
      double total_w = getTotalWeight(links);
      ArrayList<Pair> links_new = new ArrayList<Pair>();
      List<String> listStart = new ArrayList<String>(setStart);
      HashMap<String, Double> tmpStartW = new HashMap<String, Double>();
      for(String s: setStart) {
      }
      for(Pair link: links) {
        if(!setStart.contains(link.s)) {
          links_new.add(new Pair(link.s, link.w*(1.0-root_prob)));
        } else {
          tmpStartW.put(link.s, link.w*(1.0-root_prob));
        }
      } 

      for(String s: setStart) {
        double w=0.0;
        if(tmpStartW.containsKey(s)) w+=tmpStartW.get(s);
        links_new.add(new Pair(s, w+total_w*root_prob/setStart.size()));
      }
      mapLink.put(node, links_new);
      cnt++;
      if(cnt%100000==0) {
        System.err.println("modified "+cnt+" lines.");
      }
    }
  }

  double getTotalWeight(ArrayList<Pair> links) {
    double t=0;
    for(Pair p:links) t+=p.w;
    return t;
  }
  void output() {
    for(String node: mapProb.keySet()) {
      System.out.println(node+"\t"+mapProb.get(node));
    }
  }
  void walk(int iterMAX) {
    int iter = 0;
    while(iter<iterMAX) {
      System.err.println("iteration "+iter);
      HashMap<String, Double> mapProbnew = new HashMap<String, Double>();
      for(String node: mapLink.keySet()) {
        if(!mapProb.containsKey(node)) continue;
        double prob = mapProb.get(node);
        if(prob<epsilon) continue;
        ArrayList<Pair> links = mapLink.get(node);
        double total_w = getTotalWeight(links);
        for(Pair link: links) {
          if(!mapProbnew.containsKey(link.s)) {
            mapProbnew.put(link.s, 0.0);
          }
          mapProbnew.put(link.s, mapProbnew.get(link.s)+prob/total_w*link.w);
        }
      }
      mapProb = mapProbnew;
      iter++;
    }
  }
  public static void main(String[] args) {
    if(args.length<5) {
      System.out.println("Usage: java walk_rooted log_file start_file max_iteration min_conn root_prob");
      System.exit(1);
    }
    String log_file = args[0];
    String fn_start = args[1];
    int MAX = Integer.parseInt(args[2]);
    int min_conn = Integer.parseInt(args[3]);
    double root_prob = Double.parseDouble(args[4]);
    new walk_rooted(log_file, fn_start, MAX, min_conn, root_prob);
  }
}
