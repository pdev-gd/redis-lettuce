import java.util.Scanner;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

public class Main {

    public static RedisClient redisClient = null;
    public static StatefulRedisConnection<String, String> connection = null;
    public static RedisCommands<String, String> syncCmd = null;		// Sync용 command
    public static RedisAsyncCommands<String, String> asyncCmd = null;		// Async용 command

    public static String conn = "redis://root1234@127.0.0.1:6379/0?timeout=1m";

    public static void main(String[] args) {

        System.out.println("Hello Lettuce World");
        System.out.println("Redis-7.0.2, Lettuce-6.1.7");
        Scanner scanner = new Scanner(System.in);
        Util util = new Util();

        redisConnect();
        ping();

        while(true) {
            System.out.println("1:STRING   2:LIST   3:SET   4:ZSET   5:HASH  6:COMMON KEYS\n"+
                    "7:ASYNC   8:Connection Pool   9:Pubsub");
            System.out.println("선택(exit:99)> ");
            String input = scanner.nextLine();
            int i = util.checkInt(input);
            switch (i) {
                case 1: new LettuceStringCmd(); break;
                case 2: new LettuceListCmd(); break;
                case 3: new LettuceSetCmd(); break;
                case 4: new LettuceZsetCmd(); break;
                case 5: new LettuceHashCmd(); break;
                case 6: new LettuceCommonCmd(); break;
                case 7: new LettuceAsyncCmd(); break;
                case 8: new LettuceConnPool(); break;
                case 9: new LettucePubsubCmd(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }

        scanner.close();
        redisClose();
    }

    public static void redisConnect() {
        try {
            redisClient = RedisClient.create(conn);
            connection = redisClient.connect();
            syncCmd = connection.sync();
            asyncCmd = connection.async();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Redis Connection -> OK");
    }

    public static void ping() {
        String ret = syncCmd.ping();
        System.out.println(ret);
    }

    public static void redisClose() {
        connection.close();
        redisClient.shutdown();
        System.out.println("Redis Close -> OK");
    }
}