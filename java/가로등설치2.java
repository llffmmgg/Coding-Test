import java.util.*;
import java.io.*;

public class Main {
    static int n = 0;
    static int m = 0;
    static int index = 1; 
    static TreeSet<Integer> ts = new TreeSet<>();
    static Map<Integer, Integer> map = new HashMap<>();
    static PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> {
        if(a[1]==b[1]) return Integer.compare(a[0], b[0]);
        return Integer.compare(b[1],a[1]);
    });

    public static void main(String[] args) throws IOException{
        // Please write your code here.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());
        String[] cmds = new String[Q];
        for(int i=0;i<Q;i++) {
            cmds[i] = br.readLine();
        }
        
        for(String cmd: cmds) {
            String[] cnt_cmds = cmd.split(" ");
            doLogic(cnt_cmds);
        }
    }

    static void doLogic(String[] cmds) {
        int command = Integer.parseInt(cmds[0]);

        if(command == 100) {
            n = Integer.parseInt(cmds[1]);
            m = Integer.parseInt(cmds[2]);

            for(int i=3; i<3+m;i++) {
                int cnt = Integer.parseInt(cmds[i]);
                ts.add(cnt);
                map.put(index++, cnt);
                if(i!=2+m) {
                    pq.offer(new int[]{cnt, Integer.parseInt(cmds[i+1])- cnt});
                }
            }
        }
        
        if(command == 200) {
            int cnt_v = 0;
            int[] cnt = new int[2];
            while(true) {
                cnt = pq.poll();
                cnt_v = cnt[0];
                if(cnt_v == ts.last()) continue;
                if(!ts.contains(cnt_v)) continue;
                if(ts.higher(cnt_v) != cnt_v + cnt[1]) continue;
                break;
            }
            int next_v = cnt[1];
            int next = cnt_v * 2 + next_v;
            if(next % 2 == 0) next /= 2;
            else {
                next /= 2;
                next += 1;
            }

            ts.add(next);
            map.put(index++, next);
            pq.offer(new int[]{cnt_v, next-cnt_v});
            pq.offer(new int[]{next, next_v + cnt_v - next});
        }
        
        if(command == 300) {
            int idx = Integer.parseInt(cmds[1]);
            int value = map.get(idx);
            if(value != ts.first() && value != ts.last()) {
                int f = ts.lower(value);
                int s = ts.higher(value);
                pq.offer(new int[]{f, s-f});
            }
            ts.remove(value);
        }

        if(command == 400) {
            int min = ts.first();
            int max = ts.last();
            int result = 0;
            int[] cnt = new int[2];
            while(true) {
                cnt = pq.poll();
                int cnt_v = cnt[0];
                if(cnt_v == ts.last()) continue;
                if(!ts.contains(cnt_v)) continue;
                if(ts.higher(cnt_v) != cnt_v + cnt[1]) continue;
                break;
            }
            pq.offer(cnt);
            // for(int v: ts) {
            //     System.out.print(v + " ");
            // }
            // System.out.println("==" +cnt[0] + " " + cnt[1]);
            result = Math.max(min-1, n-max);
            result = Math.max(result*2, cnt[1]);

            System.out.println(result);
        }
    }
}

