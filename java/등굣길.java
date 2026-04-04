import java.util.*;

class Solution {
    public int solution(int m, int n, int[][] puddles) {
        int[][] dp = new int[n][m];
        boolean[][] pool = new boolean[n][m];
        int MOD = 1000000007;

        for (int[] p : puddles) pool[p[1]-1][p[0]-1] = true;

        dp[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (pool[i][j] || (i == 0 && j == 0)) continue;
                if (i > 0) dp[i][j] += dp[i-1][j];
                if (j > 0) dp[i][j] += dp[i][j-1];
                dp[i][j] %= MOD;
            }
        }
        return dp[n-1][m-1];
    }
}
