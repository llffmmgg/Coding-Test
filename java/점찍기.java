import java.util.*;

class Solution {
    public long solution(int k, int d) {
        long answer = 0;
        
        int x = d/k;
        
        for(int i=0;i<=x;i++) {
            int y=  calY(k*i, d);
            int yy = y / k +1;
            answer += yy;
        }
        return answer;
    }
    
    int calY(int x, int d) {
        long dd = (long)d*d;
        long xx = (long)x*x;
        
        return (int)Math.sqrt(dd-xx);
    }
}
