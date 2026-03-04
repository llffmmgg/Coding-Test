import java.util.*;
import java.io.*;

public class Main {
    static int n;
    static int m;
    static int[][] visited; 
    static class Box{
        int x; // 좌측 좌표
        int y; // 세로 좌표 
        int k; // 번호 
        int h; // 세로 크기
        int w; // 가로 크기 

        Box(int k, int h, int w, int x, int[][] visited) {
            this.x = x-1;
            this.w = w;
            this.k = k;
            this.h = h;
            this.y = 0;

            for(int i=0;i<w;i++) {
                for(int j=0;j<h;j++) {
                    visited[j][this.x+i] = k;
                }
            }
        }
        
        boolean move(int[][] visited) {
            if(!canMove()) return false;

            for(int i=0;i<w;i++) {
                visited[y][x+i] = 0;
                visited[y+h][x+i] = k;
            }
            y++;

            return true;
        }

        private boolean canMove() {
            int next = y+h;
            if(next >= n) return false;
            for(int i=0;i<w;i++) {
                if(visited[next][x+i] > 0) return false;
            }
            return true;
        }
        
        boolean canOut(int[][] visited, int rl) {
            
            if(rl%2 == 0) {
                for(int j=y;j<y+h;j++) {
                    for(int i=0;i<x;i++) {
                        if(visited[j][i]>0) return false;
                    }
                }
            
                return true;
            } else {
                for(int j=y;j<y+h;j++) {
                    for(int i=x+w;i<n;i++) {
                        if(visited[j][i]>0) return false;
                    }
                }
            
                return true;
            }
        }

        void pop(int[][] visited) {
            for(int i=0;i<w;i++) {
                for(int j=0;j<h;j++) {
                    visited[y+j][this.x+i] = 0;
                }
            }   
        }
        int getX() {
            return x+w-1;
        }

        int getYmin() {
            return y;
        } 

        int getY() {
            return y+h-1;
        }
        int getId() {
            return k;
        }
    }

    public static void main(String[] args) throws IOException{
        // Please write your code here.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        visited = new int[n][n];
        Map<Integer, Box> idToBox = new HashMap<>();
        int[] result = new int[m];
        int[] ids = new int[m];
        int idx = 0;
        for(int i=0;i<m;i++) {
            st = new StringTokenizer(br.readLine());
            Box box = new Box(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),
            Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), visited);
            idToBox.put(box.getId(), box);
            ids[i] = box.getId();
            while(box.move(visited)) {} 
        }

        Set<Integer> set = new HashSet<>();
        
        int index =0;
        int rl = 2;
        
        while(true) {
            int outId = Integer.MAX_VALUE;
            for(int i: ids) {
                if(set.contains(i)) continue;
                Box box = idToBox.get(i);
                if(box.canOut(visited, rl)) {
                    outId = Math.min(outId, i); 
                }
            }

            if(outId != Integer.MAX_VALUE) {
                Box outBox = idToBox.get(outId);
                result[index++] = outId;
                outBox.pop(visited);
                idToBox.remove(outId);
                set.add(outId);
                while(true) {
                    boolean fin = true;
                    for(Box remains: idToBox.values()) {
                        if(remains.move(visited)) {
                            fin = false;
                        }
                    }
                    if(fin) break;
                }
            }
                
            rl++;
            if(idToBox.isEmpty()) break;
        }


        for(int r: result) {
            System.out.println(r);
        }
    }
}
