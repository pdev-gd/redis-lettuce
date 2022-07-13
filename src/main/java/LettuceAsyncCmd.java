import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.lettuce.core.RedisFuture;

public class LettuceAsyncCmd {

    Scanner scanner = new Scanner(System.in);
    Util util = new Util();
    int loop = 10000;

    public LettuceAsyncCmd() {

        while(true) {
            System.out.println("1:SET   2:GET   3:SET(Sync)  4:SET(Async)  \n");
            System.out.println("선택(exit:99)> ");
            String input = scanner.nextLine();
            int i = util.checkInt(input);
            switch (i) {
                case 1: set(); break;
                case 2: get(); break;
                case 3: setSync(); break;
                case 4: setAsync(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }
    }

    public void set() {
        System.out.println("SET key value");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("SET") == false) return;
        if (cmd.length != 3) return;

        try {
            RedisFuture<String> ret = Main.asyncCmd.set(cmd[1], cmd[2]);
            ret.thenAccept(r -> {
                System.out.println("SET -> "+r.toString());
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void get() {
        System.out.println("GET key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("GET") == false) return;
        if (cmd.length != 2) return;

        try {
            RedisFuture<String> ret = Main.asyncCmd.get(cmd[1]);
            ret.thenAccept(r -> {
                System.out.println("GET -> "+r.toString());
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void setSync() {
        System.out.println("SET Sync Test "+loop+" times --------------");
        long startTime = System.currentTimeMillis();
        System.out.println("Start time: "+new Timestamp(startTime));
        for (int i=0; i<loop; i++) {
            Main.syncCmd.set("keyTestSync"+i, "valueTestSync"+i);
        }
        System.out.println("SET Sync elapsed time(ms) -> "+(System.currentTimeMillis() - startTime));
    }

    public void setAsync() {
        System.out.println("SET Async Test "+loop+" times --------------");
        long startTime = System.currentTimeMillis();
        System.out.println("Start time: "+new Timestamp(startTime));
        for (int i=0; i<loop; i++) {
            Main.asyncCmd.set("keyTestAsync"+i, "valueTestAsync"+i);
        }
        System.out.println("SET Async elapsed time(ms) -> "+(System.currentTimeMillis() - startTime));
    }

    protected void setPipeline() {
        System.out.println("SET Pipeline & Async Test "+loop+" times --------------");
        long startTime = System.currentTimeMillis();
        System.out.println("Start time: "+new Timestamp(startTime));
        List<RedisFuture<?>> futures = new ArrayList<RedisFuture<?>>();
        for (int i=0; i<loop; i++) {
            futures.add(Main.asyncCmd.set("keyTestPipe-" + i, "valueTestPipe-" + i));
        }
        System.out.println("SET Pipeline elapsed time(ms) -> "+(System.currentTimeMillis() - startTime));
    }
}