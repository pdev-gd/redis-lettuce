import java.util.List;
import java.util.Scanner;

public class LettuceListCmd {

    Scanner scanner = new Scanner(System.in);
    Util util = new Util();

    public LettuceListCmd() {

        while(true) {
            System.out.println("1:LPUSH   2:LPOP   3:LRANGE   4:LLEN   5:LREM\n");
            System.out.println("선택(exit:99)> ");
            String input = scanner.nextLine();
            int i = util.checkInt(input);
            switch (i) {
                case 1: lpush(); break;
                case 2: lpop(); break;
                case 3: lrange(); break;
                case 4: llen(); break;
                case 5: lrem(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }
    }

    // LPUSH key value1 [value2 ...]
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    public void lpush() {
        System.out.println("LPUSH key value1 [value2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("LPUSH") == false) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+cmd[2].length()+2));

        try {
            Long ret = Main.syncCmd.lpush(cmd[1], cmd2);
            System.out.println("LPUSH -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // LPOP key [count]
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    // COUNT 옵션은 버전 6.2에서 추가되었습니다.
    public void lpop() {
        System.out.println("LPOP key [count]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("LPOP") == false) return;

        try {
            if (cmd.length == 2) {
                String ret = Main.syncCmd.lpop(cmd[1]);
                System.out.println("LPOP -> "+ret);
            } else {
                long count = util.checkLong(cmd[2]);
                List<String> ret = Main.syncCmd.lpop(cmd[1], count);
                System.out.println("LPOP -> "+ret.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // LRANGE key start stop
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    public void lrange() {
        System.out.println("LRANGE key start stop");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("LRANGE") == false) return;
        if (cmd.length != 4) return;
        long start = util.checkLong(cmd[2]);
        long stop = util.checkLong(cmd[3]);

        try {
            List<String> ret = Main.syncCmd.lrange(cmd[1], start, stop);
            System.out.println("LRANGE -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // LLEN key
    // 이 명령은 version 1.1.0 부터 사용할 수 있다.
    public void llen() {
        System.out.println("LLEN key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("LLEN") == false) return;
        if (cmd.length != 2) return;
        try {
            Long ret = Main.syncCmd.llen(cmd[1]);
            System.out.println("LLEN -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // LREM key count value
    // count
    // 	양수: 왼쪽부터 삭제
    // 	음수: 오른쪽부터 삭제
    // 	0: 모두 삭제
    public void lrem() {
        System.out.println("LREM key count value");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("LREM") == false) return;
        if (cmd.length != 4) return;
        long count = util.checkLong(cmd[2]);
        try {
            Long ret = Main.syncCmd.lrem(cmd[1], count, cmd[3]);
            System.out.println("LREM -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }
}