import java.util.List;
import java.util.Scanner;

import io.lettuce.core.Limit;
import io.lettuce.core.Range;
import io.lettuce.core.Range.Boundary;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.ZAddArgs;

public class LettuceZsetCmd {

    Scanner scanner = new Scanner(System.in);
    Util util = new Util();

    public LettuceZsetCmd() {

        while(true) {
            System.out.println("1:ZADD   2:ZREM   3:ZRANGE   4:ZCARD   5:ZRANGEBYSCORE  6:ZRANGEBYLEX\n");
            System.out.println("선택(exit:99)> ");
            String input = scanner.nextLine();
            int i = util.checkInt(input);
            switch (i) {
                case 1: zadd(); break;
                case 2: zrem(); break;
                case 3: zrange(); break;
                case 4: zcard(); break;
                case 5: zrangebyscore(); break;
                case 6: zrangebylex(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }
    }

    // ZADD key [NX|XX] [GT|LT] [CH] [INCR] score member [score member ...]
    // 이 명령은 version 1.2.0 부터 사용할 수 있습니다.
    // 버전 2.4 이후 부터 member를 여러 개 입력할 수 있습니다.
    // 버전 3.0.2 [NX|XX] [CH] [INCR] 추가
    // 		NX: 멤버가 이미 있으면 업데이트하지 않고 없을 경우에만 추가한다.
    // 		XX: 이미 존재하는 멤버에 스코어를 업데이트한다.
    // 		CH: 스코어를 업데이트한 경우에만 업데이트한 멤버수를 리턴한다.
    //			스코어가 같으면 0을 리턴하고, 다르면 업데이트하고 1을 리턴한다. 리턴 값에만 영향을 미치는 옵션이다.
    // 		INCR: 스코어를 주어진 값만큼 증가시킨다.
    // 버전 6.2  [GT|LT] 추가
    // 		LT: 새 스코어가 현재 스크오 보다 적은 경우에만 기존 요소를 업데이트 합니다.
    // 		GT: 새 스코어가 현재 스크오 보다 큰 경우에만 기존 요소를 업데이트 합니다.
    @SuppressWarnings("unchecked")
    public void zadd() {
        System.out.println("ZADD key [NX|XX] [GT|LT] [CH] [INCR] score1 member1 [score2 member2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("ZADD") == false) return;
        if (cmd.length < 4) return;
        ZAddArgs args = new ZAddArgs();
        // option이 없을 경우
        if (cmd.length % 2 == 0) {
            ScoredValue<String>[] list = new ScoredValue[(cmd.length-2)/2];
            for (int i=2, j=0; i<cmd.length; i++, j++) {
                Double d = util.checkDouble(cmd[i]);
                list[j] = ScoredValue.just(d, cmd[++i]);
            }
            try {
                Long ret = Main.syncCmd.zadd(cmd[1], list);
                System.out.println("ZADD -> "+ret);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        } else {	// option이 있을 경우
            switch (cmd[2].toUpperCase()) {
                case "NX": args.nx(); break;
                case "XX": args.xx(); break;
                case "GT": args.gt(); break;
                case "LT": args.lt(); break;
                case "CH": args.ch(); break;
            }
            ScoredValue<String>[] list = new ScoredValue[(cmd.length-3)/2];
            for (int i=3, j=0; i<cmd.length; i++, j++) {
                Double d = util.checkDouble(cmd[i]);
                list[j] = ScoredValue.just(d, cmd[++i]);
            }
            try {
                Long ret = Main.syncCmd.zadd(cmd[1], args, list);
                System.out.println("ZADD -> "+ret);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    // ZREM key member1 [member2 ...]
    // 이 명령은 version 1.2.0 부터 사용할 수 있다.
    // 버전 2.4 이후 부터 member를 여러개 지정할 수 있다.
    public void zrem() {
        System.out.println("ZREM key member1 [member2 ...]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("ZREM") == false) return;
        if (cmd.length < 3) return;
        String cmd2[] = util.split(input.substring(cmd[0].length()+cmd[1].length()+2));

        try {
            Long ret = Main.syncCmd.zrem(cmd[1], cmd2);
            System.out.println("ZREM -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // ZRANGE key start stop [REV] [[BYSCORE|BYLEX] [LIMIT offset count]] [withscores]
    // 이 명령은 version 1.2.0 부터 사용할 수 있습니다.
    // REV, BYSCORE|BYLEX, LIMIT offset count 옵션은 버전 6.2부터 사용할 수 있습니다.
    public void zrange() {
        System.out.println("ZRANGE key start stop [REV] [[BYSCORE|BYLEX] [LIMIT offset count]] [withscores]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("ZRANGE") == false) return;
        if (cmd.length < 4) return;

        if (cmd.length == 4) {
            long start = util.checkLong(cmd[2]);
            long stop = util.checkLong(cmd[3]);
            try {
                List<String> ret = Main.syncCmd.zrange(cmd[1], start, stop);
                System.out.println("ZRANGE -> "+ret.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        } else {	// cmd.length > 4
            if (cmd[4].equalsIgnoreCase("WITHSCORES")) {
                long start = util.checkLong(cmd[2]);
                long stop = util.checkLong(cmd[3]);
                try {
                    List<ScoredValue<String>> ret = Main.syncCmd.zrangeWithScores(cmd[1], start, stop);
                    System.out.println("ZRANGE -> "+ret.toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
            } else
            if (cmd[4].equalsIgnoreCase("BYSCORE")) {
                try {
                    Range<Number> range = makeRangeByScore(cmd[2], cmd[3]);
                    List<String> ret = Main.syncCmd.zrangebyscore(cmd[1], range);
                    System.out.println("ZRANGE -> "+ret.toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
            } else
            if (cmd[4].equalsIgnoreCase("BYLEX")) {
                try {
                    Range<String> range = makeRangeByLex(cmd[2], cmd[3]);
                    List<String> ret = Main.syncCmd.zrangebylex(cmd[1], range);
                    System.out.println("ZRANGE -> "+ret.toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
        }
    }

    // ZCARD key
    // 이 명령은 version 1.2.0 부터 사용할 수 있다.
    public void zcard() {
        System.out.println("ZCARD key");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("ZCARD") == false) return;
        if (cmd.length != 2) return;

        try {
            Long ret = Main.syncCmd.zcard(cmd[1]);
            System.out.println("ZCARD -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // ZRANGEBYSCORE key min max [withscores] [limit offset count]
    // 이 명령은 version 1.2.0 부터 사용할 수 있습니다.
    // 버전 2.0.0 부터 withscores 옵션을 사용할 수 있습니다.
    public void zrangebyscore() {
        System.out.println("ZRANGEBYSCORE key min max [withscores] [limit offset count]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("ZRANGEBYSCORE") == false) return;
        if (cmd.length < 4) return;
        Range<Number> range = makeRangeByScore(cmd[2], cmd[3]);

        if (cmd.length == 4) {
            try {
                List<String> ret = Main.syncCmd.zrangebyscore(cmd[1], range);
                System.out.println("ZRANGEBYSCORE -> "+ret.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        } else {
            if (cmd[4].equalsIgnoreCase("LIMIT")) {
                try {
                    long offset = util.checkLong(cmd[5]);
                    long count = util.checkLong(cmd[6]);
                    Limit limit = Limit.create(offset, count);
                    List<String> ret = Main.syncCmd.zrangebyscore(cmd[1], range, limit);
                    System.out.println("ZRANGEBYSCORE -> "+ret.toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
        }
    }

    // ZRANGEBYLEX key min max [limit offset count]
    // 이 명령은 version 2.8.9 부터 사용할 수 있습니다.
    public void zrangebylex() {
        System.out.println("ZRANGEBYLEX key min max [limit offset count]");
        String input = scanner.nextLine();
        String[] cmd = util.split(input);
        if (cmd[0].equalsIgnoreCase("ZRANGEBYLEX") == false) return;
        if (cmd.length < 4) return;
        Range<String> range = makeRangeByLex(cmd[2], cmd[3]);

        try {
            List<String> ret = Main.syncCmd.zrangebylex(cmd[1], range);
            System.out.println("ZRANGEBYLEX -> "+ret.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    //-------------------------------------------------------------------------
    // by score: -inf, +inf, ( 제외, 입력하지 않으면 포함.
    public Range<Number> makeRangeByScore(String smin, String smax) {
        Range<Number> range = null;
        if (smin.equalsIgnoreCase("-INF")) {
            if (smax.equalsIgnoreCase("+INF")) {
                range = Range.unbounded();
            } else {
                if (smax.substring(0, 1).equals("(")) {	// excluding
                    double max = util.checkDouble(smax.substring(1));
                    if (max == Double.MIN_VALUE) return range;
                    range = Range.from(Boundary.unbounded(), Boundary.excluding(max));
                } else {	// including
                    double max = util.checkDouble(smax);
                    if (max == Double.MIN_VALUE) return range;
                    range = Range.from(Boundary.unbounded(), Boundary.including(max));
                }
            }
        } else {
            if (smin.substring(0, 1).equals("(")) {	// excluding
                double min = util.checkDouble(smin.substring(1));
                if (min == Double.MIN_VALUE) return range;
                if (smax.equalsIgnoreCase("+INF")) {	// unbounded
                    range = Range.from(Boundary.excluding(min), Boundary.unbounded());
                } else {
                    if (smax.substring(0, 1).equals("(")) {	// excluding
                        double max = util.checkDouble(smax.substring(1));
                        if (max == Double.MIN_VALUE) return range;
                        range = Range.from(Boundary.excluding(min), Boundary.excluding(max));
                    } else {	// including
                        double max = util.checkDouble(smax);
                        if (max == Double.MIN_VALUE) return range;
                        range = Range.from(Boundary.excluding(min), Boundary.including(max));
                    }
                }
            } else {	// including
                double min = util.checkDouble(smin);
                if (min == Double.MIN_VALUE) return range;
                if (smax.equalsIgnoreCase("+INF")) {	// unbounded
                    range = Range.from(Boundary.including(min), Boundary.unbounded());
                } else {
                    if (smax.substring(0, 1).equals("(")) {	// excluding
                        double max = util.checkDouble(smax.substring(1));
                        if (max == Double.MIN_VALUE) return range;
                        range = Range.from(Boundary.including(min), Boundary.excluding(max));
                    } else {	// including
                        double max = util.checkDouble(smax);
                        if (max == Double.MIN_VALUE) return range;
                        range = Range.from(Boundary.including(min), Boundary.including(max));
                    }
                }
            }
        }
        return range;
    }

    // by lex
    public Range<String> makeRangeByLex(String smin, String smax) {
        Range<String> range = null;
        if (smin.equalsIgnoreCase("-")) {
            if (smax.equalsIgnoreCase("+")) {
                range = Range.unbounded();
            } else {
                if (smax.substring(0, 1).equals("(")) {	// excluding
                    range = Range.from(Boundary.unbounded(), Boundary.excluding(smax.substring(1)));
                } else
                if (smax.substring(0, 1).equals("[")) {	// including
                    range = Range.from(Boundary.unbounded(), Boundary.including(smax.substring(1)));
                } else {
                    return range;
                }
            }
        } else {
            if (smin.substring(0, 1).equals("(")) {	// excluding
                if (smax.equalsIgnoreCase("+")) {	// unbounded
                    range = Range.from(Boundary.excluding(smin.substring(1)), Boundary.unbounded());
                } else {
                    if (smax.substring(0, 1).equals("(")) {	// excluding
                        range = Range.from(Boundary.excluding(smin.substring(1)), Boundary.excluding(smax.substring(1)));
                    } else
                    if (smax.substring(0, 1).equals("[")) {	// including
                        range = Range.from(Boundary.excluding(smin.substring(1)), Boundary.including(smax.substring(1)));
                    } else {
                        return range;
                    }
                }
            } else {	// including
                if (smax.equalsIgnoreCase("+")) {	// unbounded
                    range = Range.from(Boundary.including(smin.substring(1)), Boundary.unbounded());
                } else {
                    if (smax.substring(0, 1).equals("(")) {	// excluding
                        range = Range.from(Boundary.including(smin.substring(1)), Boundary.excluding(smax.substring(1)));
                    } else
                    if (smax.substring(0, 1).equals("[")) {	// including
                        range = Range.from(Boundary.including(smin.substring(1)), Boundary.including(smax.substring(1)));
                    } else {
                        return range;
                    }
                }
            }
        }
        return range;
    }
}