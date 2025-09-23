package com.experimentcode.algo;

import java.util.*;

public class CheapestRouteWithKStops {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        int[][] routes = new int[m][3];
        for (int i = 0; i < m; i++) {
            routes[i][0] = sc.nextInt();
            routes[i][1] = sc.nextInt();
            routes[i][2] = sc.nextInt();
        }
        int src = sc.nextInt(), dst = sc.nextInt(), k = sc.nextInt();
        System.out.println(findCheapestRoute(n, routes, src, dst, k));
    }

    private static int findCheapestRoute(int n, int[][] routes, int src, int dst, int k) {
        Map<Integer, List<int[]>> routesMap = new HashMap<>();
        for(int[] arr: routes){
            routesMap.computeIfAbsent(arr[0], integer -> new ArrayList<>()).add(new int[]{arr[1], arr[2]});
        }
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        int[] start = new int[]{src, k+1, 0};
        heap.add(start);
        while(!heap.isEmpty()){
            int[] rec = heap.poll();
            if(rec[0]==dst){
                return rec[2];
            }
            List<int[]> routesList = routesMap.get(rec[0]);
            routesList.forEach(r -> heap.add(new int[]{r[0], rec[1]-1, r[1]+rec[2]}));
        }
        return -1;
    }
}
