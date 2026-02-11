import java.util.*;

class Solution {
    int X, Y;
    // 전역에서 제거하거나 solution 내에서 초기화 필수
    // int answer = 0; 
    // int sum = 0;
    int[][][] isChecked;
    
    int[][] mvs = new int[][]{{1,0}, {-1,0}, {0,1}, {0,-1}};
    int[][] near1 = new int[][]{{1,3},{0,2}};
    int[][] near2 = new int[][]{{1,2},{0,3}};

    public int solution(int[][] grid) {
        int answer = 0;
        int sum = 0;
        
        X = grid.length;
        Y = grid[0].length;
        isChecked = new int[2][X][Y];
        int total = X * Y * 2;
        int index = 1;
        
        for(int i=0; i<X; i++) {
            for(int j=0; j<Y; j++) {
                for(int tri=0; tri<2; tri++) {
                    // if(answer >= total - sum) return answer; tlqkf!!!
                    if(isChecked[tri][i][j] == 0) {
                        int temp = bfs(tri, i, j, grid, index++);
                        answer = Math.max(answer, temp);
                    }
                }
            }
        }
        return answer;
    }
    
    int bfs(int startTri, int startX, int startY, int[][] grid, int index) {
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{startTri, startX, startY});
        isChecked[startTri][startX][startY] = index;
        int count = 0;
        
        while(!q.isEmpty()) {
            int[] curr = q.poll();
            count++;
            
            int tri = curr[0];
            int x = curr[1];
            int y = curr[2];
            
            int[][] cnt = (grid[x][y] == -1) ? near2 : near1;
            
            for(int next : cnt[tri]) {
                int nx = x + mvs[next][0];
                int ny = y + mvs[next][1];
                
                if(nx >= 0 && nx < X && ny >= 0 && ny < Y) {
                    int nextTri = getNextTriangle(next, grid[nx][ny]);
                    int otherTri = 1 - nextTri;
                    
                    if(isChecked[nextTri][nx][ny] != index && isChecked[otherTri][nx][ny] != index) {
                        isChecked[nextTri][nx][ny] = index;
                        q.add(new int[]{nextTri, nx, ny});
                    }
                }
            }
        }
        return count;
    }

    int getNextTriangle(int moveDir, int gridShape) {
        if(moveDir == 0) return 0; 
        if(moveDir == 1) return 1; 
        if(moveDir == 2) return (gridShape == 1) ? 0 : 1;
        if(moveDir == 3) return (gridShape == 1) ? 1 : 0;
        return 0;
    }
}
