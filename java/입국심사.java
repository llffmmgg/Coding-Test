import java.util.*;

class Solution {
    public long solution(int n, int[] times) {
        long answer = 0;

        int max = times[0];
        for (int i = 1; i < times.length; i++) {
            if (times[i] > max) {
                max = times[i];
            }
        }

        long left = 1;
        long right = (long) max * n;

        while (left <= right) {
            long mid = left + (right - left) / 2; // 오버플로우 방지 

            long count = 0;
            for (int t : times) { // 시간 당 처리 가능한 최대 인원
                count += mid / t;
            }
                
            // 처리 가능 인원으로 해당 시간 가능한지 체크 
            if (count >= n) {  // 더 많이 처리 가능하면 왼쪽범위 탐색 
                answer = mid; 
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return answer;
    }
}
