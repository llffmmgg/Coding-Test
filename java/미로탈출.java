import java.util.*;

class Solution {
    String[][] myMap;
    int[][] mvs = new int[][]{{1,0},{-1,0},{0,1},{0,-1}};
    int x;
    int y;
    public int solution(String[] maps) {
        int answer = 0;
        x = maps.length;
        y = maps[0].length();
        myMap = new String[x][y];
        int[] start = new int[2];
        int[] fin = new int[2];
        int[] l = new int[2];
        
        for(int i=0;i<x;i++){
            String[] cnt = maps[i].split("");
            for(int j=0;j<y;j++) {
                if(cnt[j].equals("S")) start = new int[]{i,j};        
                if(cnt[j].equals("E")) fin = new int[]{i,j};        
                if(cnt[j].equals("L")) l = new int[]{i,j};  
                myMap[i][j] = cnt[j];
            }
        }
        
        int a = bfs(start, l);
        if(a==-1) return -1;
        int b = bfs(l, fin);
        if(b==-1) return -1;
        
        return a+b;
    }
    
    int bfs(int[] start, int[] fin) {
        boolean[][] visited = new boolean[x][y];
        ArrayDeque<int[]> dq = new ArrayDeque<>();
        
        dq.add(new int[]{start[0], start[1], 0});
        visited[start[0]][start[1]] = true;
        
        while(!dq.isEmpty()) {
            int[] cnt = dq.poll();
            if(cnt[0] == fin[0] && cnt[1] == fin[1]) {
                return cnt[2];
            }
            for(int[] mv: mvs) {
                int nx = cnt[0] + mv[0];
                int ny = cnt[1] + mv[1];
                
                if(0<=nx && nx<x && 0<=ny && ny < y) {
                    if(!myMap[nx][ny].equals("X")) {
                        if(!visited[nx][ny]) {
                            visited[nx][ny] = true; 
                            dq.offer(new int[]{nx,ny,cnt[2]+1});
                        }
                    }
                }
            }
        }
        
        return -1;
    }
}
