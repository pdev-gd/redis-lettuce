import java.util.Scanner;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;

public class LettuceConnPool {

    Scanner scanner = new Scanner(System.in);
    Util util = new Util();

    protected RedisClient redisClient = null;

    protected StatefulRedisConnection<String, String> connection = null;
    protected RedisCommands<String, String> commands = null;
    protected RedisAsyncCommands<String, String> cmdsAsync = null;
    protected GenericObjectPool<StatefulRedisConnection<String, String>> pool = null;

    protected int idx = 0;

    public LettuceConnPool() {

        connPoolSync();

        while(true) {
            System.out.println("1:SET   2:GET  3:SET-loop\n");
            System.out.println("선택(exit:99)> ");
            int i = scanner.nextInt();
            scanner.nextLine();
            switch (i) {
                case 1: set(); break;
                case 2: get(); break;
                case 3: setLoop(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }

        closePool();

    }

    /* -----------------------------------------------
     * 레디스 서버에 실제로 connect하지 않는다.
     * 처음 명령을 실행할 때 connect한다.
     * 이후로 이미 연결된 connection을 사용하고 부족하면 추가로 연결한다.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void connPoolSync() {
        GenericObjectPoolConfig conConf  = new GenericObjectPoolConfig();
        pool = ConnectionPoolSupport.createGenericObjectPool(() -> redisConnectSync(), conConf);
        System.out.println("Create Connection Pool: getMaxTotal -> "+pool.getMaxTotal());
    }

    public StatefulRedisConnection<String, String> redisConnectSync() {
        System.out.println("Redis Connection -> Start");

        try {
            redisClient = RedisClient.create(Main.conn);
            connection = redisClient.connect();
            System.out.println("Redis Connection -> OK");
            return connection;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void set() {
        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisCommands<String, String> commands = connection.sync();
            idx++;
            String ret = commands.set("keyPool-"+idx, "valuePool-"+idx);
            System.out.println("SET -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void get() {
        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisCommands<String, String> commands = connection.sync();
            String ret = commands.get("keyPool-"+idx);
            System.out.println("GET -> "+ret);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void setLoop() {
        for (int i=0; i<1000; i++) {
            set();
        }
    }

    public void closePool() {
        pool.close();
//        redisClient.shutdown();
    }
}