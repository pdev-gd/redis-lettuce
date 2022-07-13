import java.util.Scanner;
import java.util.Set;

public class LettuceSetCmd {

    Scanner scanner = new Scanner(System.in);
    Util util = new Util();

    public LettuceSetCmd() {

        while(true) {
            System.out.println("1:SADD   2:SREM   3:SMEMBERS   4:SCARD   5:SUNION  6:SPOP\n");
            System.out.println("선택(exit:99)> ");
            String input = scanner.nextLine();
            int i = util.checkInt(input);
            switch (i) {
                case 1: sadd(); break;
                case 2: srem(); break;
                case 3: smembers(); break;
                case 4: scard(); break;
                case 5: sunion(); break;
                case 6: spop(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }
    }

    // SADD key member1 [member2 ...]
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    // 버전 2.4 이후부터 member를 여러 개 지정할 수 있게 되었다.
    public void sadd() {
        System.out.println("SADD key member1 [member2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("SADD") == false) return;
        if (cmd.length < 3) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+cmd[1].length()+2));

        try {
            Long ret = Main.syncCmd.sadd(cmd[1], cmd2);
            System.out.println("SADD -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // SREM key member1 [member1 ...]
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    // 버전 2.4 이후 부터 여러 개의 member를 받을 수 있다.
    public void srem() {
        System.out.println("SREM key member1 [member1 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("SREM") == false) return;
        if (cmd.length < 3) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+cmd[1].length()+2));

        try {
            Long ret = Main.syncCmd.srem(cmd[1], cmd2);
            System.out.println("SREM -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // SMEMBERS key
    // 이 명령은 version 1.0.0 부터 사용할 수 있습니다.
    public void smembers() {
        System.out.println("SMEMBERS key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("SMEMBERS") == false) return;
        if (cmd.length != 2) return;

        try {
            Set<String> ret = Main.syncCmd.smembers(cmd[1]);
            System.out.println("SMEMBERS -> "+ret.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // SCARD key
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    public void scard() {
        System.out.println("SCARD key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("SCARD") == false) return;
        if (cmd.length != 2) return;

        try {
            Long ret = Main.syncCmd.scard(cmd[1]);
            System.out.println("SCARD -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // SUNION key1 [key2 ...]
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    public void sunion() {
        System.out.println("SUNION key1 [key2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("SUNION") == false) return;
        if (cmd.length < 2) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+1));

        try {
            Set<String> ret = Main.syncCmd.sunion(cmd2);
            System.out.println("SUNION -> "+ret.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // SPOP key [count]
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    // 버전 3.2부터 count 사용 가능하다.
    public void spop() {
        System.out.println("SPOP key [count]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("SPOP") == false) return;

        try {
            if (cmd.length == 2) {
                String ret = Main.syncCmd.spop(cmd[1]);
                System.out.println("SPOP -> "+ret);
            } else {
                long count = util.checkLong(cmd[2]);
                Set<String> ret = Main.syncCmd.spop(cmd[1], count);
                System.out.println("SPOP -> "+ret.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }
}