import java.util.*;

class Solution {
    public int solution(String[][] relation) {
        int n = relation[0].length;
        int rowCount = relation.length;
        List<Integer> candidateKeys = new ArrayList<>();

        for (int i = 1; i < (1 << n); i++) {
            
            boolean isMinimal = true;
            for (int key : candidateKeys) {
                if ((i & key) == key) {
                    isMinimal = false;
                    break;
                }
            }
            if (!isMinimal) continue; // 최소성 탈락하면 다음 조합으로

            Set<String> set = new HashSet<>();
            for (String[] rt : relation) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < n; j++) {
                    if ((i & (1 << j)) != 0) {
                        sb.append(rt[j]).append("/")
                    }
                }
                set.add(sb.toString());
            }

            if (set.size() == rowCount) {
                candidateKeys.add(i);
            }
        }

        return candidateKeys.size();
    }
}
