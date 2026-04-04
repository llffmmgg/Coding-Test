import java.util.*;

class Solution {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    int[] myInfo;
    int answer = 0;
    public int solution(int[] info, int[][] edges) {
        myInfo = info;
        for(int[] edge: edges) {
            int p = edge[0];
            int c = edge[1];
            
            tree.computeIfAbsent(p, k -> new ArrayList<>()).add(c);
            tree.computeIfAbsent(c, k -> new ArrayList<>());
        }
        boolean[] visited = new boolean[20];        
        visited[0] = true;
        Set<Integer> nexts = new HashSet<>();

        dfs(0, 0, 0, visited, nexts);
        
        return answer;
    }
    
    void dfs(int node, int ship, int wolf, boolean[] visited, Set<Integer> nexts) {        
        if(myInfo[node] == 0) ship++;
        else wolf++;
        
        if((ship != 0) && (ship <= wolf)) {
            return;
        }

        answer = Math.max(ship, answer);

        Set<Integer> newNexts = new HashSet<>(nexts);
        newNexts.addAll(tree.get(node));
        newNexts.remove(node); // 현재 노드는 제거

        for(int next : newNexts) {
            if(!visited[next]) {
                visited[next] = true;
                dfs(next, ship, wolf, visited, newNexts);
                visited[next] = false;
            }
        }
    }
}
