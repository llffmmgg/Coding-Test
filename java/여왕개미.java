import java.io.*;
import java.util.*;

public class Main {
    static final int INF = 1_000_000_000;
    static int Q;

    static int[] house;        
    static boolean[] broken;   
    static int hcnt;         

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        house = new int[40000 + 5];
        broken = new boolean[40000 + 5];

        for (int qi = 0; qi < Q; qi++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == 100) {
                int n = Integer.parseInt(st.nextToken());
                Arrays.fill(broken, false);

                house[0] = 0;
                hcnt = 1;
                for (int i = 0; i < n; i++) {
                    house[hcnt++] = Integer.parseInt(st.nextToken());
                }
            } else if (cmd == 200) {
                int p = Integer.parseInt(st.nextToken());
                house[hcnt++] = p;
            } else if (cmd == 300) {
                int idx = Integer.parseInt(st.nextToken());
                broken[idx] = true; 
            } else if (cmd == 400) {
                int r = Integer.parseInt(st.nextToken());
                System.out.println(binarySearch(r));
            }
        }
    }

    static int binarySearch(int r) {
        int left = 0, right = INF, ans = INF;

        while (left <= right) {
            int mid = (left + right) >>> 1;
            if (can(mid, r)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    static boolean can(int mid, int r) {
        int prev = -1;
        int ants = 0;

        for (int i = 1; i < hcnt; i++) {
            if (!broken[i]) {
                prev = house[i];
                ants = 1;
                // 이후 집들 스캔
                for (int j = i + 1; j < hcnt; j++) {
                    if (broken[j]) continue;
                    int cur = house[j];
                    if (cur - prev > mid) {
                        ants++;
                        prev = cur;
                        if (ants > r) return false;
                    }
                }
                return ants <= r;
            }
        }

        return true;
    }
}
