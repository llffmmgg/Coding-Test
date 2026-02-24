class Solution {
    int rows, cols;
    
    public int solution(int[][] beginning, int[][] target) {
        rows = beginning.length;
        cols = beginning[0].length;
        int answer = Integer.MAX_VALUE;

        // 행을 먼저 선택 
        for (int bit = 0; bit < (1 << rows); bit++) {
            answer = Math.min(answer, check(bit, beginning, target));
        }

        return (answer == Integer.MAX_VALUE) ? -1 : answer;
    }

    private int check(int bit, int[][] beginning, int[][] target) {
        int flipCount = 0;
        int[][] temp = new int[rows][cols];

        // 초기 상태 복사 및 선택된 행 뒤집기
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = beginning[i][j];
            }
            
            if ((bit & (1 << i)) != 0) {
                flipCount++;
                for (int j = 0; j < cols; j++) {
                    temp[i][j] = 1 - temp[i][j];
                }
            }
        }

        // 각 열을 확인
        for (int j = 0; j < cols; j++) {
            int diff = 0;
            for (int i = 0; i < rows; i++) {
                if (temp[i][j] != target[i][j]) diff++;
            }

            if (diff == rows) { // 열 전체가 다르면 뒤집기
                flipCount++;
            } else if (diff != 0) { // 일부만 다르면 이 조합은 불가능
                return Integer.MAX_VALUE;
            }
        }

        return flipCount;
    }
}
