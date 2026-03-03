import java.util.*;
import java.io.*;

public class Main {
    static int t;
    static String[] cmds;

    static List<int[]> reloads = new ArrayList<>(); // reload
    static TreeSet<Integer> powers = new TreeSet<>((a,b)-> b-a); // power 
    static Map<Integer, TreeSet<Integer>> powerToIds = new HashMap<>(); // power, ids 
    static Map<Integer, int[]> ships = new HashMap<>();
    static List<Integer> ids = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        t = Integer.parseInt(br.readLine());
        cmds = new String[t];

        for(int i=0;i<t;i++) {
            cmds[i] = br.readLine();
        }

        for(String cmd: cmds) {
            doLogic(cmd);
            doReload();
        }
    }

    static void doLogic(String command) {
        String[] commands = command.split(" ");
        int cmd = Integer.parseInt(commands[0]);

        if(cmd == 100) {
            int n = Integer.parseInt(commands[1]);
            for(int i=0;i<n;i++) {
                int start = 3*i + 2;
                int ii = Integer.parseInt(commands[start]);
                int p = Integer.parseInt(commands[start+1]);
                int r = Integer.parseInt(commands[start+2]);
                ships.put(ii, new int[]{p,r,0});
                ids.add(ii);
                powers.add(p);
                powerToIds.computeIfAbsent(p, key -> new TreeSet<>()).add(ii);
            }
        }

        if(cmd == 200) {
            int i = Integer.parseInt(commands[1]);
            int p = Integer.parseInt(commands[2]);
            int r = Integer.parseInt(commands[3]);
            ships.put(i, new int[]{p,r,0});
            ids.add(i);
            powers.add(p);
            powerToIds.computeIfAbsent(p, key -> new TreeSet<>()).add(i);
        }

        if(cmd == 300) {
            int i = Integer.parseInt(commands[1]);
            int p = Integer.parseInt(commands[2]);
            int[] shipInfo = ships.get(i);

            TreeSet<Integer> cnt_ts = powerToIds.get(shipInfo[0]);
            cnt_ts.remove(i);
            powerToIds.computeIfAbsent(p, key -> new TreeSet<>()).add(i);
            powers.add(p);
            shipInfo[0] = p;
        }
        
        if(cmd == 400) {
            int count = 0;
            int damage = 0;
            // power , shipId 
            PriorityQueue<int[]> result = new PriorityQueue<>((a,b) -> {
                if(a[0]==b[0]) {
                    return a[1] - b[1];
                }
                return b[0]-a[0];
            });

            for(int power: powers) {
                TreeSet<Integer> cnt_ts = powerToIds.get(power);
                if(cnt_ts.isEmpty()) continue;

                for(int id: cnt_ts) {
                    int[] shipInfo = ships.get(id);
                    if(shipInfo[2] != 0) continue;
                    reloads.add(new int[]{id, shipInfo[1]});
                    shipInfo[2] = 1; 
                    result.offer(new int[]{shipInfo[0], id});
                    damage += shipInfo[0];
                    count++;
                    if(count == 5) break;
                }

                if(count == 5) break; 
            }

            System.out.print(damage + " " + result.size());
            while(!result.isEmpty()) {
                System.out.print(" " + result.poll()[1]);
            }
            System.out.println();
        }
    }

    static void doReload() {
        for(int[] ship: reloads) {
            ship[1]--;
            if(ship[1] == 0) ships.get(ship[0])[2] = 0;
        }

        reloads.removeIf(a->a[1]==0); 
    }
}
