import java.util.*;
import java.io.*;

public class Main {
    static int n;
    static int k;
    static int l;
    static int[][] board;
    static int[][] robots;
    static int[][] mvs = new int[][]{{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
    static Map<String, Integer> map = new HashMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        l = Integer.parseInt(st.nextToken());

        board = new int[n + 1][n + 1];
        robots = new int[k][2];

        for (int i = 1; i <= n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= n; j++) {
                int value = Integer.parseInt(st.nextToken());
                board[i][j] = value;
                if (value != 0 && value != -1) {
                    map.put(i + "," + j, value);
                }
            }
        }

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            robots[i] = new int[]{x, y};
        }

        for (int i = 0; i < l; i++) {
            doLogic();
        }
    }

    static void doLogic() {
        // 1. 청소기 이동
        for (int[] rb : robots) {
            moveRobo(rb);
        }
        // 2. 청소
        for (int[] rb : robots) {
            clean(rb);
        }
        // 3. 먼지 축적
        dust();
        // 4. 먼지 확산
        spread();
        
        print();
    }

    // 청소기 이동 BFS
    static void moveRobo(int[] cnt) {
        if (board[cnt[0]][cnt[1]] > 0) return;

        ArrayDeque<int[]> dq = new ArrayDeque<>();
        boolean[][] visited = new boolean[n + 1][n + 1];
        List<int[]> candidates = new ArrayList<>();

        visited[cnt[0]][cnt[1]] = true;
        dq.offer(new int[]{cnt[0], cnt[1], 0});

        int targetDist = Integer.MAX_VALUE;

        while (!dq.isEmpty()) {
            int[] now = dq.poll();
            int curX = now[0];
            int curY = now[1];
            int curDist = now[2];

            if (curDist > targetDist) break;

            if (board[curX][curY] > 0) {
                candidates.add(new int[]{curX, curY});
                targetDist = curDist;
                continue;
            }

            for (int[] mv : mvs) {
                int nx = curX + mv[0];
                int ny = curY + mv[1];

                if (nx >= 1 && nx <= n && ny >= 1 && ny <= n) {
                    if (board[nx][ny] >= 0 && !visited[nx][ny]) {
                        boolean existRobo = false;
                        for (int[] rb : robots) {
                            if (nx == rb[0] && ny == rb[1]) {
                                existRobo = true;
                                break;
                            }
                        }
                        if (!existRobo) {
                            visited[nx][ny] = true;
                            dq.offer(new int[]{nx, ny, curDist + 1});
                        }
                    }
                }
            }
        }

        if (!candidates.isEmpty()) {
            fixNextXY(candidates, cnt);
        }
    }

    private static void fixNextXY(List<int[]> li, int[] cnt) {
        if (li.isEmpty()) return;

        int tx = Integer.MAX_VALUE;
        int ty = Integer.MAX_VALUE;

        for (int[] next : li) {
            int nx = next[0];
            int ny = next[1];

            if (nx < tx) {
                tx = nx;
                ty = ny;
            } else if (nx == tx) {
                if (ny < ty) {
                    ty = ny;
                }
            }
        }
        cnt[0] = tx;
        cnt[1] = ty;
    }

    static void clean(int[] cnt) {
        int x = cnt[0];
        int y = cnt[1];
        int bestExclude = -1;
        int bestSum = -1;

        // 4방향 중 한 곳을 제외하고 가장 많이 청소할 수 있는 방향 탐색
        for (int ex = 0; ex < 4; ex++) {
            int sum = 0;
            if (board[x][y] > 0) {
                sum += Math.min(board[x][y], 20);
            }
            for (int d = 0; d < 4; d++) {
                if (d == ex) continue;
                int nx = x + mvs[d][0];
                int ny = y + mvs[d][1];

                if (nx < 1 || nx > n || ny < 1 || ny > n) continue;
                if (board[nx][ny] == -1) continue;
                if (board[nx][ny] > 0) {
                    sum += Math.min(board[nx][ny], 20);
                }
            }

            if (sum > bestSum) {
                bestSum = sum;
                bestExclude = ex;
            }
        }

        // 실제 청소 적용
        if (board[x][y] > 0) {
            board[x][y] = Math.max(board[x][y] - 20, 0);
        }
        for (int d = 0; d < 4; d++) {
            if (d == bestExclude) continue;
            int nx = x + mvs[d][0];
            int ny = y + mvs[d][1];

            if (nx >= 1 && nx <= n && ny >= 1 && ny <= n) {
                if (board[nx][ny] > 0) {
                    board[nx][ny] = Math.max(board[nx][ny] - 20, 0);
                }
            }
        }
    }

    static void dust() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (board[i][j] > 0) {
                    board[i][j] += 5;
                }
            }
        }
    }

    static void spread() {
        int[][] tempBoard = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (board[i][j] == 0) {
                    int sumAround = 0;
                    for (int[] mv : mvs) {
                        int nx = i + mv[0];
                        int ny = j + mv[1];
                        if (nx >= 1 && nx <= n && ny >= 1 && ny <= n) {
                            if (board[nx][ny] > 0) {
                                sumAround += board[nx][ny];
                            }
                        }
                    }
                    if (sumAround > 0) {
                        tempBoard[i][j] = sumAround / 10;
                    }
                }
            }
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (board[i][j] == 0 && tempBoard[i][j] > 0) {
                    board[i][j] = tempBoard[i][j];
                }
            }
        }
    }

    static void print() {
        long totalDust = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (board[i][j] > 0) {
                    totalDust += board[i][j];
                }
            }
        }
        System.out.println(totalDust);
    }
}
