import java.util.*;

class Solution {
    int n;
    int m;
    int[][] mvs = new int[][]{{1,0},{-1,0},{0,1},{0,-1}};
    boolean[][] visited;
    List<int[]> points = new ArrayList<>();
    Map<Integer, Integer> map = new HashMap<>();
    
    public int solution(int[][] land) {
        int answer = 0;
        n = land.length;
        m = land[0].length;
        visited = new boolean[n][m];
        int index = 1;
        for(int i=0;i<n;i++) {
            for(int j=0;j<m;j++) {
                if(land[i][j]==1 && !visited[i][j]) {
                    int value = dfs(land, i, j, 1);
                    fill(land, points, index);
                    map.put(index++, value);
                    points= new ArrayList<>();
                }
            }
        }
        
        for(int i=0;i<m;i++) {
            boolean[] sichued = new boolean[index];
            answer = Math.max(answer, check(land, sichued ,i));
        }
        return answer;
    }   
    
    int dfs(int[][] land, int x, int y, int count) {
        visited[x][y] = true;
        points.add(new int[]{x,y});
        for(int[] mv: mvs) {
            int nx = mv[0] + x;
            int ny = mv[1] + y;
            
            if(0<=nx&&nx<n&&0<=ny&&ny<m&&land[nx][ny]!=0) {
                if(visited[nx][ny]) continue;
                count = dfs(land, nx, ny, count+1);
            }
        }
        
        return count;
    }
    
    void fill(int[][] land, List<int[]> points, int value) {
        for(int[] point: points) {
            land[point[0]][point[1]] = value;
        }
    }
    
    int check(int[][] land, boolean[] sichued ,int y) {
        int result = 0;
        for(int i=0; i<n; i++) {
            int cnt = land[i][y];
            if(cnt != 0 && !sichued[cnt]) {
                result += map.get(cnt);
                sichued[cnt] = true;
            }
            
        }
        return result;
    }
}
