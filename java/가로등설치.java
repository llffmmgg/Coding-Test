import java.util.*;
import java.io.*;

public class Main {
    static int N = 0;
    static int M = 0;
    
    static TreeSet<Integer> points = new TreeSet<>();
    
    // 고유 ID(생성 순서) -> 점의 좌표(Value)
    static Map<Integer, Integer> idToPoint = new HashMap<>(); 
    static int idCounter = 1;

    static PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
        if (a[1] == b[1]) {
            return Integer.compare(a[0], b[0]); // 오름차순
        }
        return Integer.compare(b[1], a[1]);     // 내림차순
    });

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int n = Integer.parseInt(br.readLine());
        String[] cmds = new String[n];

        for(int i = 0; i < n; i++) {
             cmds[i] = br.readLine();
        }

        for(String cmd : cmds) {
            String[] cnts = cmd.split(" ");
            doLogic(cnts);
        }
    }

    static void doLogic(String[] cmd) {
        int command = Integer.parseInt(cmd[0]);

        if(command == 100) {
            N = Integer.parseInt(cmd[1]);
            M = Integer.parseInt(cmd[2]);
            
            int prev = -1; // 이전 점을 기억하기 위한 변수
            for(int i = 3; i < 3 + M; i++) {
                int now = Integer.parseInt(cmd[i]);
                points.add(now); // TreeSet에 추가 (O(log N))
                idToPoint.put(idCounter++, now); 
                
                if(prev != -1) {
                    // 이전 점과 현재 점 사이의 구간을 PQ에 추가
                    pq.offer(new int[]{prev, now - prev});
                }
                prev = now;
            }
        }
        else if(command == 200) {
            int[] cnts;
            while(true) {
                cnts = pq.poll(); 
                int start = cnts[0];
                int len = cnts[1];
                
                // 검증 로직 (TreeSet 활용)
                if(points.contains(start)) {
                    Integer nextPoint = points.higher(start); 
                    if(nextPoint != null && (nextPoint - start) == len) {
                        break; // 유효한 구간 확인 완료!
                    }
                }
            }
            
            // 회원님이 작성하신 완벽한 중앙값 계산 로직 그대로 사용
            int next = cnts[0] * 2 + cnts[1];
            if(next % 2 == 0) next /= 2;
            else {
                next /= 2;
                next += 1; 
            } 

            // 새로운 구간 2개 추가
            pq.offer(new int[]{cnts[0], next - cnts[0]});
            pq.offer(new int[]{next, cnts[0] + cnts[1] - next});
            
            // TreeSet과 ID Map에 새 점 추가 (Index 업데이트 for문이 통째로 사라짐!)
            points.add(next);
            idToPoint.put(idCounter++, next);
        }
        else if(command == 300) {
            int id = Integer.parseInt(cmd[1]);
            if(!idToPoint.containsKey(id)) return; // 안전장치
            
            int pointToRemove = idToPoint.get(id);

            // 지울 점의 바로 앞 점과 바로 뒤 점을 찾음
            Integer lower = points.lower(pointToRemove);
            Integer higher = points.higher(pointToRemove);
            
            // 앞, 뒤 점이 모두 존재한다면, 지워지면서 하나로 합쳐질 거대한 새 구간을 PQ에 넣음
            if(lower != null && higher != null) {
                pq.offer(new int[]{lower, higher - lower});
            }
            
            // 점 삭제
            points.remove(pointToRemove);
            idToPoint.remove(id);
        }
        else if(command == 400) {
            int[] cnts = null;
            while(!pq.isEmpty()) {
                cnts = pq.peek();
                int start = cnts[0];
                int len = cnts[1];
                
                if(points.contains(start)) {
                    Integer nextPoint = points.higher(start);
                    if(nextPoint != null && (nextPoint - start) == len) {
                        break; // 유효한 구간 확인
                    }
                }
                pq.poll(); // 유령 구간 버림
            }
            
            int min = points.first();
            int max = points.last();
            
            int result = Math.max(min - 1, N - max) * 2;
            if(cnts != null) {
                result = Math.max(result, cnts[1]);
            }
            System.out.println(result);
        }
    }
}
