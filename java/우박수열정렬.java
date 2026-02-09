import java.util.*;

class Solution {
    List<Double>  li = new ArrayList<>();
    
    public double[] solution(int k, int[][] ranges) {
        int len = ranges.length;
        int nn = 0;
        double[] answer = new double[len];
         
        double before = 0;
        li.add(0.0);
        while(k!=1) {
            int next;
            nn++;
            if(k%2==0) {
                next = (int)k/2;
            } else {
                next = k*3 + 1;
            }
            
            double n = (double)(next + k) / 2;
            
            li.add(n+before);
            before = n+before;
            k = next;
        }
        
        for(int i=0;i<len;i++) {
            int a = ranges[i][0];
            int b = ranges[i][1];
            
            b = nn+b;
            if(a > b) answer[i] = -1;
            else {
                answer[i] = li.get(b) - li.get(a);
            }
        }
        
        return answer;
    }
}
