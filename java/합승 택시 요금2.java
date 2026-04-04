import java.util.*;

class Solution {
    Map<Integer, List<int[]>> map = new HashMap<>();
    
    public int solution(int n, int s, int a, int b, int[][] fares) {
        int answer = Integer.MAX_VALUE;
        int[] dj1 = new int[n+1];
        int[] dj2 = new int[n+1];
        int[] dj3 = new int[n+1];
        Arrays.fill(dj1, Integer.MAX_VALUE);
        Arrays.fill(dj2, Integer.MAX_VALUE);
        Arrays.fill(dj3, Integer.MAX_VALUE);
        
        for(int[] fr: fares) {
            int f = fr[0];
            int ss = fr[1];
            int v = fr[2];
            
            map.computeIfAbsent(f, k->new ArrayList<>()).add(new int[]{ss, v});
            map.computeIfAbsent(ss, k->new ArrayList<>()).add(new int[]{f, v});
        }    
        
        djk(dj1, s);
        djk(dj2, a);
        djk(dj3, b);
        
        for(int i = 1; i<=n; i++) {
            answer = Math.min(answer, dj1[i] + dj2[i] + dj3[i]);
        }
        
        return answer;
    }
    
    void djk(int[] dj, int s) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a,b)->a[1]-b[1]);
        pq.add(new int[]{s, 0});
        boolean[] visited = new boolean[dj.length];
        
        while(!pq.isEmpty()) {
            int[] cnt = pq.poll();
            int node = cnt[0];
            int value = cnt[1];
            if(visited[node]) continue;
            
            visited[node] = true;
            dj[node] = Math.min(dj[node], value);
            
            for(int[] nx: map.get(node)) { 
                int near = nx[0];
                int pay = nx[1];
                if(!visited[near]) pq.offer(new int[]{near, value + pay});
            }
        }
    }
}
