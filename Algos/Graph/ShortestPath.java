/*
最短路算法

1.Dijkstra
原理是贪心算法 保证每一步都是最短 不能处理带有负权值的图 O(E*lgV) using TreeSet
http://www.wayne.ink/2018/02/06/Algorithm/Dijkstra/
http://threezj.com/2016/05/02/%E6%9C%80%E7%9F%AD%E8%B7%AF%E5%BE%84%E7%AE%97%E6%B3%95%E6%80%BB%E7%BB%93/
https://blog.csdn.net/v_JULY_v/article/details/6096981

2.Bellman-Ford
原理是动态规划 适应一般的情况（即存在负权边的情况） O(V*E)
http://threezj.com/2016/05/02/%E6%9C%80%E7%9F%AD%E8%B7%AF%E5%BE%84%E7%AE%97%E6%B3%95%E6%80%BB%E7%BB%93/
http://www.cnblogs.com/gaochundong/p/bellman_ford_algorithm.html
https://zhuanlan.zhihu.com/p/36295603

3.Floyd-Warshall
解决任意两点间的最短路径的一种算法 可以正确处理有向图或负权的最短路径问题
https://www.cnblogs.com/skywang12345/p/3711532.html
https://blog.csdn.net/ljhandlwt/article/details/52096932
http://www.it610.com/article/2136313.htm


几个最短路径算法的比较
https://blog.csdn.net/v_july_v/article/details/6181485
 */

import java.util.*;

public class ShortestPath {

  //start Dijkstra fields
  private int[] prev;
  private int[] dist;
  private TreeSet<Integer> pq;
  private Map<Integer, Map<Integer, Integer>> weightedG;
  //end Dijkstra fields

  //start Bellman-Ford fields
  private int[] prevBF;
  private int[] distBF;
  private Map<Integer, Map<Integer, Integer>> weightedGBF;

  //start Floyd fields
  private int[][] distFloyd;
  private int[][] path;
  //end Floyd fields


  // start Dijkstra methods
  public void initDijkstra(int n, Map<Integer, Map<Integer, Integer>> weightedG) {
    prev = new int[n];
    dist = new int[n];
    this.weightedG = weightedG;
    pq = new TreeSet<>(new Comparator<Integer>(){
      public int compare(Integer idxA, Integer idxB) {
        if (dist[idxA] < dist[idxB]) return -1;
        if (dist[idxA] > dist[idxB]) return 1;
        return idxA - idxB; //到源点距离一样的时候 按照idx大小顺序排序
      }
    });
    //初始化
    for (int i = 1; i < n; i++) {
      dist[i] = Integer.MAX_VALUE;
    }
  }

  public void findPathsDijkstra() {
    pq.add(0); //0 is the src point
    while (!pq.isEmpty()) {
      int u = pq.pollFirst();
      for (Map.Entry entry : weightedG.get(u).entrySet()) {
        int v = (int)entry.getKey();
        int weight = (int)entry.getValue();
        if (dist[v] == Integer.MAX_VALUE) {
          dist[v] = dist[u] + weight;
          pq.add(v);
          prev[v] = u;
        } else {
          if (dist[v] > dist[u] + weight) {
            pq.remove(v);
            dist[v] = dist[u] + weight;
            pq.add(v);
            prev[v] = u;
          }
        }
      }
    }
  }
  //end Dijkstra methods

  //start Bellman-Ford methods
  public void initBF(int n, Map<Integer, Map<Integer, Integer>> weightedGBF) {
    prevBF = new int[n];
    distBF = new int[n];
    this.weightedGBF = weightedGBF;
    for (int i = 1; i < n; i++) {
      distBF[i] = Integer.MAX_VALUE;
    }
  }

  /*
   Relax all edges |V| - 1 times.
   A simple shortest path from source to any other vertex can have at most |V| - 1
   */
  public void findPathsBF() {
    int vCnt = weightedGBF.size();
    for (int i = 0; i < vCnt - 1; i++) {
      for (int u = 0; u < vCnt; u++) {
        for (Map.Entry entry : weightedGBF.get(u).entrySet()) {
          int v = (int)entry.getKey();
          int w = (int)entry.getValue();
          if (distBF[u] != Integer.MAX_VALUE && distBF[v] > distBF[u] + w) {
            distBF[v] = distBF[u]+ w;
            prevBF[v] = u;
          }
        }
      }
    }
  }

  /*
  check for negative-weight cycles.
  method findPathsBF() guarantees shortest distances
  if graph doesn't contain negative weight cycle.
  If we get a shorter path, then there is a cycle.
   */
  public boolean hasNegativeCycle() {
    for (int u = 0; u < weightedGBF.size(); u++) {
      for (Map.Entry entry : weightedGBF.get(u).entrySet()) {
        int v = (int)entry.getKey();
        int w = (int)entry.getValue();
        if (distBF[u] != Integer.MAX_VALUE && distBF[v] > distBF[u] + w) {
          return true;
        }
      }
    }
    return false;
  }
  //end Bellman-Ford methods

  //start Floyd methods
  public void initFloyd(int[][] distFloyd) {
    this.distFloyd = distFloyd;
    int num = distFloyd.length;
    path = new int[num][num];
    for (int i = 0; i < num; i++) {
      for (int j = 0; j < num; j++) {
        path[i][j] = j;
      }
    }
  }

  public void findPathsFloyd() {
    int vNum = distFloyd.length;
    //注意三重循环的嵌套顺序.一定是k在最外层,(i,j)在内层
    for (int k = 0; k < vNum; k++) {
      for (int i = 0; i < vNum; i++) {
        for (int j = 0; j < vNum; j++) {
          if (distFloyd[i][k] == Integer.MAX_VALUE || distFloyd[i][k] == Integer.MAX_VALUE) {
            continue;
          }
          int tmpDist = distFloyd[i][k] + distFloyd[k][j];
          if (tmpDist < distFloyd[i][j]) {
            distFloyd[i][j] = tmpDist;
            //i->j最短路径经过k
            path[i][j] = path[i][k];
          }
        }
      }
    }
  }
  //end Floyd methods


  public static void main(String[] args) {
    ShortestPath sp = new ShortestPath();
    Map<Integer, Map<Integer, Integer>> wg = new HashMap<>();
    for (int i = 0; i < 6; i++) {
      wg.put(i, new HashMap<>());
    }
    wg.get(0).put(1, 1);
    wg.get(0).put(5, 3);
    wg.get(1).put(2, 2);
    wg.get(1).put(5, -3);
    wg.get(2).put(3, 1);
    wg.get(2).put(4, 3);
    sp.initBF(6, wg);
    sp.findPathsBF();
    boolean negCycle = sp.hasNegativeCycle();
    System.out.println(sp.distBF[5] +  " " + negCycle);
  }
}