import java.util.*;

class Solution {
    String[][] map = new String[51][51];
    int[][] map_int = new int[51][51];
    
    public String[] solution(String[] commands) {
        String[] answer;
        
        int v =1;
        for(int i=1;i<51;i++) {
            for(int j=1;j<51;j++) {
                map_int[i][j] = v++;
            }
        }    
        
        List<String> temp = new ArrayList<>();
        
        for(String cmd: commands) {
            String[] cmds = cmd.split(" ");
            String command = cmds[0];
            if(command.equals("UPDATE")) update(cmds);
            if(command.equals("MERGE")) merge(cmds);
            if(command.equals("UNMERGE")) unmerge(cmds);
            if(command.equals("PRINT")) print(cmds, temp);
        }
        
        answer = new String[temp.size()];
        for(int i=0;i<answer.length;i++) {
            answer[i] = temp.get(i);
        }
        
        return answer;
    }
    
    void update(String[] cmds) {        
        if(cmds.length == 4) {
            int r = Integer.parseInt(cmds[1]);  
            int c = Integer.parseInt(cmds[2]);  
            int target  = map_int[r][c];
            for(int i=1;i<51;i++) {
                for(int j=1;j<51;j++) {
                    if(map_int[i][j]==target) {
                        map[i][j] = cmds[3];
                    }
                }
            }
            return;
        } 
        String v1 = cmds[1];
        String v2 = cmds[2];
        
        for(int i=1;i<51;i++) {
            for(int j=1;j<51;j++) {
                if(map[i][j]!= null && map[i][j].equals(v1)) {
                    map[i][j] = v2;
                }
            }
        }
    }
    
    void merge(String[] cmds) {
        int r1 = Integer.parseInt(cmds[1]);
        int v1 = Integer.parseInt(cmds[2]);
        int r2 = Integer.parseInt(cmds[3]);
        int v2 = Integer.parseInt(cmds[4]);
        
        int value = map_int[r1][v1];
        int target = map_int[r2][v2];
        
        if(value == target) return;
        
        List<int[]> li = new ArrayList<>();

        for(int i=1;i<51;i++) {
            for(int j=1;j<51;j++) {
                if(map_int[i][j]==value) {
                    li.add(new int[]{i,j});
                }
                if(map_int[i][j]==target) {
                    map_int[i][j] = value;
                    li.add(new int[]{i,j});
                }
            }
        }
                
        String s1 = map[r1][v1];
        String s2 = map[r2][v2];
        
        if(s1==null && s2==null) return;
        String s = "";
        
        if(s1==null) {
            s = s2;
        } else {
            s = s1;
        }
        
        for(int[] cnt: li) {
            map[cnt[0]][cnt[1]] = s;
        }
    }
    
    void unmerge(String[] cmds) {
        int r = Integer.parseInt(cmds[1]);
        int v = Integer.parseInt(cmds[2]);
        int target = map_int[r][v];
        
        int value = 1;
        String s = map[r][v];
        for(int i=1;i<51;i++) {
            for(int j=1;j<51;j++) {
                if(map_int[i][j] == target) {
                    map_int[i][j] = value;
                    map[i][j] = null;
                }
                value++;
            }
        }
        
        map[r][v] = s;
    }
    
    void print(String[] cmds, List<String> temp) {
        int r = Integer.parseInt(cmds[1]);
        int v = Integer.parseInt(cmds[2]);
        String s = map[r][v];
        if(s==null) s = "EMPTY";
        temp.add(s);        
    }
}

