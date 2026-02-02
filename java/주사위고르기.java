import java.util.*;

class Solution {
    int n;
    int maxWins = -1;
    int[][] DICE;
    List<Integer> temp = new ArrayList<>();
    
    public int[] solution(int[][] dice) {
        n = dice.length;
        DICE = dice;
        maxWins = -1; 
        
        doChecking(0, new ArrayList<>());
        
        int nn = temp.size();
        int[] answer = new int[nn];
        Collections.sort(temp); 
        for(int i=0; i<nn; i++) {
            answer[i] = temp.get(i) + 1;
        }
        return answer;
    }
    
    void doChecking(int idx, List<Integer> a) {
        if(a.size() == n/2) {
            List<Integer> b = new ArrayList<>();
            for(int i=0; i<n; i++) {
                if(!a.contains(i)) b.add(i);
            }
            
            List<Integer> sum1 = new ArrayList<>();
            List<Integer> sum2 = new ArrayList<>();
            makeSum(a, sum1, 0, 0);
            makeSum(b, sum2, 0, 0);
            
            check(sum1, sum2, a);
            return;
        }   
        
        if(idx == n) return;
        
        a.add(idx);
        doChecking(idx + 1, a);
        a.remove(a.size() - 1); 
        
        doChecking(idx + 1, a);
    }

    void makeSum(List<Integer> diceIndices, List<Integer> result, int level, int sum) {
        if(level == diceIndices.size()) {
            result.add(sum);
            return;
        }
        
        for(int v : DICE[diceIndices.get(level)]) {
            makeSum(diceIndices, result, level + 1, sum + v);
        }
    }
    
    void check(List<Integer> sumA, List<Integer> sumB, List<Integer> aaa) {
        int winCount = 0;
        
        Collections.sort(sumB);
        for(int aValue : sumA) {
            int left = 0, right = sumB.size();
            while(left < right) {
                int mid = (left + right) / 2;
                if(sumB.get(mid) < aValue) left = mid + 1;
                else right = mid;
            }
            winCount += left;
        }
        
        if(winCount > maxWins) {
            maxWins = winCount;
            temp = new ArrayList<>(aaa); 
        }
    }
}
