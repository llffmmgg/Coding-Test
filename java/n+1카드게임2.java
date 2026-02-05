import java.util.*;

class Solution {
    int n;
    public int solution(int coin, int[] cards) {
        int answer = 1;
        n = cards.length;
        int start = (int)n/3;
        
        boolean[] exists = new boolean[n+1]; // 1~n 
        Map<Integer, Integer> myCards = new HashMap<>();
        for(int i=0; i<start; i++) {
            int v = cards[i];
            exists[v] = true;
            myCards.put(v, v);
        }
        
        int cntIdx = start;
        List<Integer> left = new ArrayList<>();
        
        while(true){
            // fin
            if(cntIdx >= n) break;
            
            // check my deck
            int a = 0;
            int b = 0;
            boolean isOk = false;
            for (int key : myCards.keySet()) {
                int remain = n+1-key;
                int counter = myCards.getOrDefault(remain, 0);
                if(counter != 0) {
                    a = key;
                    b = counter;
                    isOk = true;
                    break;
                }
            }
            
            // remove in my deck
            if(isOk) {
                myCards.remove(a);
                myCards.remove(b);
                exists[a] = false;
                exists[b] = false;
                addRemains(left, exists, cards,cntIdx);
                cntIdx+=2;
                answer++;
                continue;
            } 
            
            if(coin == 0) break;
            
            // check next and myDeck
            int neededCoin = 0;
            addRemains(left, exists, cards,cntIdx);
            
            for(int card: left) {
                if(!exists[card]) continue;
                
                int cardOp = n+1-card;
                
                if(exists[cardOp]) {
                    if(myCards.containsKey(cardOp)) {
                        neededCoin = 1;
                        a = card;
                        b = cardOp;
                        break;
                    } else {
                        neededCoin = 2;
                        a = card;
                        b = cardOp;
                    }
                }
            }
            
            if(coin<neededCoin) break;
            if(neededCoin == 0) break;
            
            if(neededCoin == 1) {
                myCards.remove(b);
            }
            exists[a] = false;
            exists[b] = false;
            coin -= neededCoin;
            cntIdx += 2;
            answer++;
        }
        System.out.println(cntIdx);
        return answer;
    }
    
    void addRemains(List<Integer> left, boolean[] exists ,int[] cards,int cnt) {
        for(int i=0;i<2;i++) {
            int next = cnt+i;
            if(next < n) {
                left.add(cards[next]);
                exists[cards[next]] = true;
            }
        }
    }
}
