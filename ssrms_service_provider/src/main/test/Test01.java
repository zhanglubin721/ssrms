import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Test01 {

    @Autowired
    private JedisPool jedisPool;
    @Test
    public void test01() {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String key = jedis.get("key");
            if (null == key) {
                jedis.set("key", "password");
                jedis.expire("key", 20);
                System.out.println("已存储key");
            } else {
                System.out.println("缓存中已有数据");
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }  finally {
            jedis.close();
        }
    }
}
