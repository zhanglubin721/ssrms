import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    @Test
    public void test02() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,1);
        String date2= sdf.format(calendar.getTime());
        System.out.println(date2);

    }

    @Test
    public void test03() {
        StringBuffer stringBuffer = new StringBuffer("9点-12点");
        int i = stringBuffer.lastIndexOf("-");
        int i1 = stringBuffer.lastIndexOf("点");
        String substring = stringBuffer.substring(i + 1, i1);
        int i2 = stringBuffer.indexOf("点", 1);
        String substring1 = stringBuffer.substring(0, i2);
        if (substring1.length() == 1) {
            substring1 = "0" + substring1;
        }
        System.out.println(substring1 + substring);
    }
}
