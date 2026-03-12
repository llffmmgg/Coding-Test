import java.io.*;
import java.util.*;

public class Main {
    static int n, q;
    static int[][] board;
    static Map<Integer, Microbe> microbes = new HashMap<>();
    static int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static class Microbe {
        int id;
        int area;
        int baseX; // 현재 배치된 위치의 기준 x
        int baseY; // 현재 배치된 위치의 기준 y
        List<int[]> cells; // 현재 모양의 상대좌표 목록

        Microbe(int id, int baseX, int baseY, List<int[]> cells) {
            this.id = id;
            this.baseX = baseX;
            this.baseY = baseY;
            this.cells = cells;
            this.area = cells.size();
        }

        List<int[]> getAbsoluteCells() {
            List<int[]> result = new ArrayList<>(cells.size());
            for (int[] cell : cells) {
                result.add(new int[]{baseX + cell[0], baseY + cell[1]});
            }
            return result;
        }

        void placeOnBoard(int[][] targetBoard, int x, int y) {
            for (int[] cell : cells) {
                targetBoard[x + cell[0]][y + cell[1]] = id;
            }
            this.baseX = x;
            this.baseY = y;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        board = new int[n][n];

        int nextId = 1;

        for (int turn = 0; turn < q; turn++) {
            st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken()) - 1; // inclusive
            int c2 = Integer.parseInt(st.nextToken()) - 1; // inclusive

            // 1) 새 미생물 삽입 + 겹친 기존 미생물 정보 수집
            Map<Integer, List<int[]>> overlappedById = new HashMap<>();

            for (int x = r1; x <= r2; x++) {
                for (int y = c1; y <= c2; y++) {
                    int oldId = board[x][y];
                    if (oldId != 0) {
                        overlappedById.computeIfAbsent(oldId, k -> new ArrayList<>()).add(new int[]{x, y});
                    }
                    board[x][y] = nextId;
                }
            }

            // 2) 겹친 기존 미생물 생존 판정 및 모양 갱신
            for (Map.Entry<Integer, List<int[]>> entry : overlappedById.entrySet()) {
                int id = entry.getKey();
                Microbe microbe = microbes.get(id);
                if (microbe == null) continue;

                Microbe updated = rebuildAfterCut(microbe);
                if (updated == null) {
                    microbes.remove(id);
                } else {
                    microbes.put(id, updated);
                }
            }

            // 3) 새 미생물 등록
            List<int[]> newCells = new ArrayList<>();
            for (int x = r1; x <= r2; x++) {
                for (int y = c1; y <= c2; y++) {
                    newCells.add(new int[]{x - r1, y - c1});
                }
            }
            microbes.put(nextId, new Microbe(nextId, r1, c1, newCells));
            nextId++;

            // 4) 모든 살아있는 미생물 이동
            relocateAll();

            // 5) 정답 계산
            System.out.println(calculateAnswer());
        }
    }

    static Microbe rebuildAfterCut(Microbe microbe) {
        List<int[]> absoluteCells = microbe.getAbsoluteCells();
        List<int[]> aliveCells = new ArrayList<>();

        for (int[] cell : absoluteCells) {
            int x = cell[0];
            int y = cell[1];
            if (board[x][y] == microbe.id) {
                aliveCells.add(cell);
            }
        }

        if (aliveCells.isEmpty()) {
            return null;
        }

        if (!isConnected(aliveCells)) {
            return null;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (int[] cell : aliveCells) {
            minX = Math.min(minX, cell[0]);
            minY = Math.min(minY, cell[1]);
        }

        List<int[]> normalized = new ArrayList<>(aliveCells.size());
        for (int[] cell : aliveCells) {
            normalized.add(new int[]{cell[0] - minX, cell[1] - minY});
        }

        return new Microbe(microbe.id, minX, minY, normalized);
    }

    static boolean isConnected(List<int[]> cells) {
        Set<Long> cellSet = new HashSet<>();
        for (int[] cell : cells) {
            cellSet.add(encode(cell[0], cell[1]));
        }

        ArrayDeque<int[]> dq = new ArrayDeque<>();
        Set<Long> visited = new HashSet<>();

        int[] start = cells.get(0);
        dq.offer(start);
        visited.add(encode(start[0], start[1]));

        int count = 0;
        while (!dq.isEmpty()) {
            int[] cur = dq.poll();
            count++;

            for (int[] d : DIRS) {
                int nx = cur[0] + d[0];
                int ny = cur[1] + d[1];
                long key = encode(nx, ny);

                if (cellSet.contains(key) && visited.add(key)) {
                    dq.offer(new int[]{nx, ny});
                }
            }
        }

        return count == cells.size();
    }

    static void relocateAll() {
        List<Microbe> list = new ArrayList<>(microbes.values());
        list.sort((a, b) -> {
            if (a.area == b.area) return a.id - b.id;
            return b.area - a.area;
        });

        int[][] newBoard = new int[n][n];
        Map<Integer, Microbe> nextMap = new HashMap<>();

        for (Microbe microbe : list) {
            int[] pos = findPosition(microbe, newBoard);
            if (pos == null) {
                continue; // 이동 불가 -> 사라짐
            }

            microbe.placeOnBoard(newBoard, pos[0], pos[1]);
            nextMap.put(microbe.id, microbe);
        }

        board = newBoard;
        microbes = nextMap;
    }

    static int[] findPosition(Microbe microbe, int[][] targetBoard) {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (canPlace(microbe, x, y, targetBoard)) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    static boolean canPlace(Microbe microbe, int x, int y, int[][] targetBoard) {
        for (int[] cell : microbe.cells) {
            int nx = x + cell[0];
            int ny = y + cell[1];

            if (nx < 0 || nx >= n || ny < 0 || ny >= n) return false;
            if (targetBoard[nx][ny] != 0) return false;
        }
        return true;
    }

    static long calculateAnswer() {
        long answer = 0;
        Set<Long> seenPairs = new HashSet<>();

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                int a = board[x][y];
                if (a == 0) continue;

                if (y + 1 < n) {
                    int b = board[x][y + 1];
                    if (b != 0 && a != b) {
                        answer += addPairOnce(a, b, seenPairs);
                    }
                }

                if (x + 1 < n) {
                    int b = board[x + 1][y];
                    if (b != 0 && a != b) {
                        answer += addPairOnce(a, b, seenPairs);
                    }
                }
            }
        }

        return answer;
    }

    static long addPairOnce(int a, int b, Set<Long> seenPairs) {
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }

        long key = (((long) a) << 32) | (b & 0xffffffffL);
        if (!seenPairs.add(key)) return 0L;

        Microbe ma = microbes.get(a);
        Microbe mb = microbes.get(b);
        if (ma == null || mb == null) return 0L;

        return (long) ma.area * mb.area;
    }

    static long encode(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }
}
