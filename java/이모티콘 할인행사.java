import java.util.*;

// 1. 가입자 2. 금액 

class Solution {
    int n;
    int[] answer;
    int temp = 0;
    int[] halins = new int[]{10, 20, 30, 40};
    public int[] solution(int[][] users, int[] emoticons) {
        answer = new int[2];
        n = emoticons.length;
               
        int[] mns = new int[users.length];
        dfs(0, mns, users, emoticons);
        
        return answer;
    }
    
    void dfs(int level, int[] mns, int[][] users, int[] emojis) {
        if(level == n) {
            int cnt = check(mns, users);
            // System.out.println(cnt + " , "  + temp);
            if(cnt >= answer[0]) {
                if(cnt>answer[0] || temp > answer[1]) {
                    answer[0] = cnt;
                    answer[1] = temp;
                }
            }
            temp = 0;
            return;
        }
        
        for(int i=0;i<4;i++) {
            int halin = halins[i];
            int value = emojis[level] / 100 * (100-halin);
            buy(users, mns, halin, value, 1);
            dfs(level+1, mns, users, emojis);
            buy(users, mns, halin, value, -1);
        }
    }
    
    void buy(int[][] users, int[] mns, int v, int halin, int mode) {
        int idx = 0;
        if(mode==-1) halin *= -1;
        
        for(int[] us: users) {
            if(us[0] <= v) {
                mns[idx] += halin;
            }
            
            idx++;
        }
    }
    
    int check(int[] mns, int[][] v) {
        int n = mns.length;
        int result = 0;
        
        for(int i=0;i<n;i++) {
            if(mns[i] >= v[i][1]) {
                result++;
            } else {
             temp += mns[i]; 
            }
        }
        
        return result;
    }
}
