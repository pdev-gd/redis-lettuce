# redis-lettuce
07.13 redis 교육 실습 예제에 문제가 많아 추가 및 수정하였습니다

# Run 
java -jar lettuce2.jar

# Example 
Hello Lettuce World
Redis-7.0.2, Lettuce-6.1.7
Redis Connection -> OK
PONG
1:STRING   2:LIST   3:SET   4:ZSET   5:HASH  6:COMMON KEYS
7:ASYNC   8:Connection Pool   9:Pubsub
선택(exit:99)>
2
1:LPUSH   2:LPOP   3:LRANGE   4:LLEN   5:LREM

선택(exit:99)>
3
LRANGE key start stop
lrange mylist 0 -1
LRANGE -> [new value, value2, value3, Hello, Redis]
