package aps2.tsp;

import java.lang.reflect.Array;
import java.util.*;

public class TravellingSalesman {
    public ArrayList<Node> nodes = new ArrayList<>();

    class Node {
        int x;
        int y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    /**
     * To solve the travelling salesman problem (TSP) you need to find a shortest
     * tour over all nodes in the graph where each node must be visited exactly
     * once. You need to finish at the origin node.
     *
     * In this task we will consider complete undirected graphs only with
     * Euclidean distances between the nodes.
     */

    /**
     * Adds a node to a graph with coordinates x and y. Assume the nodes are
     * named by the order of insertion.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     */
    public void addNode(int x, int y) {
        nodes.add(new Node(x, y));
    }

    /**
     * Returns the distance between nodes v1 and v2.
     *
     * @param v1 Identifier of the first node
     * @param v2 Identifier of the second node
     * @return Euclidean distance between the nodes
     */
    public double getDistance(int v1, int v2) {
        Node n1 = nodes.get(v1);
        Node n2 = nodes.get(v2);
        double xs = Math.pow(n1.x - n2.x, 2);
        double ys = Math.pow(n1.y - n2.y, 2);
        return Math.sqrt(xs + ys);
    }

    /**
     * Finds and returns an optimal shortest tour from the origin node and
     * returns the order of nodes to visit.
     * <p>
     * Implementation note: Avoid exploring paths which are obviously longer
     * than the existing shortest tour.
     *
     * @param start Index of the origin node
     * @return List of nodes to visit in specific order
     */
    public int[] calculateExactShortestTour(int start) {
        int[] route = nearestNeighborGreedy(start);//calculateApproximateShortestTour(start);
        //route = new int[]{0,1,2,3};
        System.out.println(calculateDistanceTravelled(route));
        ArrayList<Integer> stack = new ArrayList<>();
        for (int i = route.length - 1; i >= 0; i--) {
            stack.add(route[i]);
            }
        return backTracking(route, new ArrayList<>(), stack, calculateDistanceTravelled(route), 0, start);
    }

    public int[] backTracking(int[] current, ArrayList<Integer> path, ArrayList<Integer> queue, double currentDistance, int depth, int start) {
        if (queue.isEmpty()){
            double dist = calcDist(path);
            if (calculateDistanceTravelled(current) > dist){
                for (int i = 0; i < path.size(); i++){
                    current[i] = path.get(i);
                }
            }
            //System.out.println(Arrays.toString(current));
            return current;
        }
        int check = 1;
        int a = queue.get(0);
        while (!queue.isEmpty() && (a != queue.get(0) || check == 1)){
            int node = queue.remove(queue.size()-1);
            if (depth == 0 && node != start) {
                return current;
            }
            path.add(node);
            if (calcDist(path) <= currentDistance){
                ArrayList<Integer> cl = (ArrayList<Integer>) queue.clone();
                ArrayList<Integer> cl2 = (ArrayList<Integer>) path.clone();
                current =  backTracking(current, cl2, cl, currentDistance,depth + 1, start);
                currentDistance = calculateDistanceTravelled(current);
                int tmp = path.remove(path.size()-1);
                queue.add(0, tmp);
            }else{
                int tmp = path.remove(path.size()-1);
                queue.add(0, tmp);
            }
            check++;
        }
        return current;
    }



    public double calcDist(ArrayList<Integer> path){
        if (path.size() == 0 || path.size()==1) return 0;
        int[] r = new int[path.size()];
        for (int i = 0; i < r.length; i++){
            r[i] = path.get(i);
        }
        return calculateDistanceTravelled(r);
    }


    /**
     * Returns an optimal shortest tour and returns its distance given the
     * origin node.
     *
     * @param start Index of the origin node
     * @return Distance of the optimal shortest TSP tour
     */
    public double calculateExactShortestTourDistance(int start) {
        int r[] = calculateExactShortestTour(start);
        return calculateDistanceTravelled(r);
    }

    /**
     * Returns an approximate shortest tour and returns the order of nodes to
     * visit given the origin node.
     * <p>
     * Implementation note: Use a greedy nearest neighbor apporach to construct
     * an initial tour. Then use iterative 2-opt method to improve the
     * solution.
     *
     * @param start Index of the origin node
     * @return List of nodes to visit in specific order
     */
    public int[] calculateApproximateShortestTour(int start) {
        int[] route = nearestNeighborGreedy(start);
        double len =  calculateDistanceTravelled(route);
        while(true){
            int[] tmp2 = route.clone();
            int[] tmp = twoOptExchange(tmp2);
            double len2 = calculateDistanceTravelled(tmp);
            if (len2 < len){
                route = tmp.clone();
                len = len2;
            }else{
                break;
            }
        }
        return route;
    }

    /**
     * Returns an approximate shortest tour and returns its distance given the
     * origin node.
     *
     * @param start Index of the origin node
     * @return Distance of the approximate shortest TSP tour
     */
    public double calculateApproximateShortestTourDistance(int start) {
        int r[] = calculateApproximateShortestTour(start);
        return calculateDistanceTravelled(r);
    }

    /**
     * Constructs a Hamiltonian cycle by starting at the origin node and each
     * time adding the closest neighbor to the path.
     * <p>
     * Implementation note: If multiple neighbors share the same distance,
     * select the one with the smallest id.
     *
     * @param start Origin node
     * @return List of nodes to visit in specific order
     */
    public int[] nearestNeighborGreedy(int start) {
        int[] path = new int[nodes.size()];
        ArrayList<Integer> already = new ArrayList<>();
        int k = 0;
        path[k] = start;
        already.add(start);
        for (int i = 0; i < nodes.size() - 1; i++) {
            double min = Integer.MAX_VALUE;
            int pos = -1;
            for (int j = 0; j < nodes.size(); j++) {
                if (already.contains(j)) continue;
                double dis = getDistance(start, j);
                if (min > dis) {
                    min = dis;
                    pos = j;
                }
            }
            start = pos;
            k++;
            path[k] = pos;
            already.add(pos);
        }


        return path;
    }

    /**
     * Improves the given route using 2-opt exchange.
     * <p>
     * Implementation note: Repeat the procedure until the route is not
     * improved in the next iteration anymore.
     *
     * @param route Original route
     * @return Improved route using 2-opt exchange
     */
    private int[] twoOptExchange(int[] route) {
        double min = calculateDistanceTravelled(route);
        int[] toret = route.clone();
        for (int i = 0; i < route.length; i++) {
            for (int j = i + 1; j < route.length; j++) {
                int r1[] = twoOptSwap(route.clone(), i, j);
                double len = calculateDistanceTravelled(r1);
                if (len < min) {
                    toret = r1;
                    min = len;
                }// else {
                   // return toret;
                //}
            }
        }
        return toret;
    }

    /**
     * Swaps the nodes i and k of the tour and adjusts the tour accordingly.
     * <p>
     * Implementation note: Consider the new order of some nodes due to the
     * swap!
     *
     * @param route Original tour
     * @param i     Name of the first node
     * @param k     Name of the second node
     * @return The newly adjusted tour
     */
    public int[] twoOptSwap(int[] route, int i, int k) {
        int[] r = route.clone();
        while (i < k) {
            int tmp = r[k];
            r[k] = r[i];
            r[i] = tmp;
            i++;
            k--;
        }
        return r;
    }

    /**
     * Returns the total distance of the given tour i.e. the sum of distances
     * of the given path add the distance between the final and initial node.
     *
     * @param tour List of nodes in the given order
     * @return Travelled distance of the tour
     */
    public double calculateDistanceTravelled(int[] tour) {
        double path = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            path += getDistance(tour[i], tour[i + 1]);
        }
        return path + getDistance(tour[0], tour[tour.length - 1]);
    }

}
