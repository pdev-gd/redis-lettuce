import java.util.List;
import java.util.Scanner;

import io.lettuce.core.CopyArgs;
import io.lettuce.core.KeyScanCursor;

public class LettuceCommonCmd {

    Scanner scanner = new Scanner(System.in);
    Util util = new Util();

    public LettuceCommonCmd() {

        while(true) {
            System.out.println("1:EXISTS   2:DEL   3:KEYS   4:SCAN  5:RENAME\n"+
                    "6:EXPIRE  7:TTL  8:PERSIST   9:COPY");
            System.out.println("선택(exit:99)> ");
            String input = scanner.nextLine();
            int i = util.checkInt(input);
            switch (i) {
                case 1: exists(); break;
                case 2: del(); break;
                case 3: keys(); break;
                case 4: scan(); break;
                case 5: rename(); break;
                case 6: expire(); break;
                case 7: ttl(); break;
                case 8: persist(); break;
                case 9: copy(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }
    }

    // EXISTS key1 [key2 ...]  version 1.0.0
    // 버전 3.0.3부터 key를 여러 개 사용할 수 있습니다.
    public void exists() {
        System.out.println("EXISTS key1 [key2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("EXISTS") == false) return;
        if (cmd.length < 2) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+1));

        try {
            Long ret = Main.syncCmd.exists(cmd2);
            System.out.println("EXISTS -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // DEL key1 [key2 ...]
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    public void del() {
        System.out.println("DEL key1 [key2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("DEL") == false) return;
        if (cmd.length < 2) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+1));

        try {
            Long ret = Main.syncCmd.del(cmd2);
            System.out.println("DEL -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // KEYS pattern
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    public void keys() {
        System.out.println("KEYS pattern");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("KEYS") == false) return;
        if (cmd.length != 2) return;

        try {
            List<String> ret = Main.syncCmd.keys(cmd[1]);
            System.out.println("KEYS -> "+ret.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // SCAN cursor [MATCH pattern] [COUNT count]
    // 이 명령은 version 2.8.0 부터 사용할 수 있다.
    public void scan() {
        try {
            KeyScanCursor<String> keys = new KeyScanCursor<String>();
            keys.setCursor("0");
            do {
                keys = Main.syncCmd.scan(keys);
                System.out.println("scan -> size: "+keys.getKeys().size()+", "+keys.getKeys().toString()+", nextCursor: "+keys.getCursor()+", Finish: "+keys.isFinished());
                System.out.println("Continue: yes/no");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("yes")) {
                    keys.setFinished(false);
                } else {
                    keys.setFinished(true);
                }
            } while (keys.isFinished() == false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // RENAME key new_key
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    public void rename() {
        System.out.println("RENAME key new_key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("RENAME") == false) return;
        if (cmd.length != 3) return;

        try {
            String ret = Main.syncCmd.rename(cmd[1], cmd[2]);
            System.out.println("RENAME -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // EXPIRE key seconds
    // 이 명령은 version 1.0.0 부터 사용할 수 있습니다.
    public void expire() {
        System.out.println("EXPIRE key seconds");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("EXPIRE") == false) return;
        if (cmd.length != 3) return;
        long sec = util.checkLong(cmd[2]);

        try {
            Boolean ret = Main.syncCmd.expire(cmd[1], sec);
            System.out.println("EXPIRE -> "+ret.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // TTL key
    // 이 명령은 version 1.0.0 부터 사용할 수 있다.
    public void ttl() {
        System.out.println("TTL key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("TTL") == false) return;
        if (cmd.length != 2) return;

        try {
            Long ret = Main.syncCmd.ttl(cmd[1]);
            System.out.println("TTL -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // PERSIST key
    // 이 명령은 version 2.2.0 부터 사용할 수 있다.
    public void persist() {
        System.out.println("PERSIST key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("PERSIST") == false) return;
        if (cmd.length != 2) return;

        try {
            Boolean ret = Main.syncCmd.persist(cmd[1]);
            System.out.println("PERSIST -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // COPY source destination [DB destination-db] [REPLACE]
    // 이 명령은 version 6.2.0 부터 사용할 수 있다.
    public void copy() {
        System.out.println("COPY source destination [DB destination-db] [REPLACE]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("COPY") == false) return;
        if (cmd.length < 3) return;
        CopyArgs args = new CopyArgs();
        if (cmd.length > 3) {
            for (int i=3; i<cmd.length; i++) {
                switch(cmd[i].toUpperCase()) {
                    case "DB":
                        int dbno = util.checkInt(cmd[++i]);
                        args.destinationDb(dbno);
                        break;
                    case "REPLASE":
                        args.replace(true);
                        break;
                    default:
                        System.out.println("옵션이 잘못되었습니다.");
                }
            }
        }

        try {
            Boolean ret = Main.syncCmd.copy(cmd[1], cmd[2], args);
            System.out.println("COPY -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }
}