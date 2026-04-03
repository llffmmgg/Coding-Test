import java.util.*;

class Solution {
    public int solution(int[] stones, int k) {
        int answer = 0;
        // int max = 0;
        int n = stones.length;
        // for(int st: stones) {
        //     if(max < st) max = st;
        // }
        
        int left = 1;
        int rigth = Integer.MAX_VALUE;
        
        while(left <= rigth) {
            int mid = left + (rigth - left) / 2;
            
            if(can(mid, stones, k)) {
                answer = mid;
                left = mid + 1;
            } else {
                rigth = mid - 1;
            }
        }
        
        
        return answer;
    }
    
    boolean can(int mid, int[] stones, int k) {
        int jump = 0;
        for(int st: stones) {
            int now = st - mid;
            if(now >= 0) {
                jump = 0;
                continue;
            } else {
                jump++;
                if(jump >= k) return false;
            }
        }
        return true;
    }
}
