import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import io.lettuce.core.KeyValue;

public class LettuceHashCmd {

    Scanner scanner = new Scanner(System.in);
    Util util = new Util();

    public LettuceHashCmd() {

        while(true) {
            System.out.println("1:HSET   2:HGET   3:HDEL   4:HLEN   5:HMGET   6:HGETALL\n");
            System.out.println("선택(exit:99)> ");
            String input = scanner.nextLine();
            int i = util.checkInt(input);
            switch (i) {
                case 1: hset(); break;
                case 2: hget(); break;
                case 3: hdel(); break;
                case 4: hlen(); break;
                case 5: hmget(); break;
                case 6: hgetall(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }
    }

    // HSET key field1 value1 [field2 value2 ...]
    // 이 명령은 version 2.0.0 부터 사용할 수 있습니다.
    // 버전 4.0부터 field와 value를 여러 개 입력할 수 있다.
    public void hset() {
        System.out.println("HSET key field1 value1 [field2 value2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("HSET") == false) return;
        if (cmd.length < 4 || (cmd.length % 2) == 1) return;
        HashMap<String,String> map = new HashMap<String,String>();
        for (int i=2; i<cmd.length; i++) map.put(cmd[i], cmd[++i]);

        try {
            Long ret = Main.syncCmd.hset(cmd[1], map);
            System.out.println("HSET -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // HGET key field
    // 이 명령은 version 2.0.0 부터 사용할 수 있다.
    public void hget() {
        System.out.println("HGET key field");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("HGET") == false) return;
        if (cmd.length != 3) return;

        try {
            String ret = Main.syncCmd.hget(cmd[1], cmd[2]);
            System.out.println("HGET -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // HDEL key field1 [field2 ...]
    // 이 명령은 version 2.0.0 부터 사용할 수 있다.
    public void hdel() {
        System.out.println("HDEL key field1 [field2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("HDEL") == false) return;
        if (cmd.length < 3) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+cmd[1].length()+2));

        try {
            Long ret = Main.syncCmd.hdel(cmd[1], cmd2);
            System.out.println("HDEL -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // HLEN key
    // 이 명령은 version 2.0.0 부터 사용할 수 있다.
    public void hlen() {
        System.out.println("HLEN key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("HLEN") == false) return;
        if (cmd.length != 2) return;

        try {
            Long ret = Main.syncCmd.hlen(cmd[1]);
            System.out.println("HLEN -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // HMGET key field1 [field2 ...]
    // 이 명령은 version 2.0.0 부터 사용할 수 있다.
    public void hmget() {
        System.out.println("HMGET key field1 [field2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("HMGET") == false) return;
        if (cmd.length < 3) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+cmd[1].length()+2));

        try {
            List<KeyValue<String, String>> ret = Main.syncCmd.hmget(cmd[1], cmd2);
            System.out.println("HMGET -> "+ret.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // HGETALL key
    // 이 명령은 version 2.0.0 부터 사용할 수 있습니다.
    public void hgetall() {
        System.out.println("HGETALL key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("HGETALL") == false) return;
        if (cmd.length != 2) return;

        try {
            Map<String, String> ret = Main.syncCmd.hgetall(cmd[1]);
            System.out.println("HGETALL -> "+ret.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }
}