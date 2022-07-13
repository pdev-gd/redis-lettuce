/*-------------------------------------------------------
 * 시작하면 ch01로 subscribe한다.
 * 1번 publish를 선택하면 ch01로 Hello 메시지를 보낸다.
 * cli에서 ch01로 subscribe해서 받아보든지, ch01로 publish해본다.
 */

import java.util.Scanner;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;

public class LettucePubsubCmd {

    Scanner scanner = new Scanner(System.in);
    Util util = new Util();
    int idx = 1;
    String channel = "ch01";

    public LettucePubsubCmd() {

        // Subscribe channel
        RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @Override
            public void message(String channel, String message) {
                System.out.println(String.format("subscribe -> Channel: %s, Message: %s", channel, message));
            }
        };

        StatefulRedisPubSubConnection<String, String> pubsubConn = Main.redisClient.connectPubSub();
        pubsubConn.addListener(listener);
        RedisPubSubAsyncCommands<String, String> async = pubsubConn.async();
        async.subscribe(channel);


        while(true) {
            System.out.println("1:PUBLISH\n");
            System.out.println("선택(exit:99)> ");
            String input = scanner.nextLine();
            int i = util.checkInt(input);
            switch (i) {
                case 1: publish(); break;
                case 99: break;
                default: System.out.println("잘못 선택했습니다. 다시 선택하세요. ");
            }
            if (i==99) break;
        }
    }

    // PUBLISH channel message
    public void publish() {
        try {
            Main.syncCmd.publish(channel, "Hello"+idx);
            System.out.println("publish "+channel+" Hello"+idx);
            idx++;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}