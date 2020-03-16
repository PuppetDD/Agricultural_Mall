package com.gold.mall.common.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <redis 工具类>
 */
public class RedisUtil {

    private static JedisPool jedisPool = null;

    static {
        jedisPool = (JedisPool) SpringBeanHolder.getBean("jedisPool");
    }

    /**
     * @param [key, value]
     *              Description <保存数据到 redis 到redis 缓存中>
     * @author GOLD
     * @date 2020/2/24 14:15
     */
    public static void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @param [key, value, expireTIme]
     *              Description <保存数据到 redis 到redis 缓存中带过期时间>
     * @author GOLD
     * @date 2020/2/24 14:15
     */
    public static void set(String key, String value, int expireTIme) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, expireTIme); //设置过期时间
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @param [key]
     * @return {@link String}
     * Description <获取redis缓存中的内容>
     * @author GOLD
     * @date 2020/2/24 14:15
     */
    public static String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

}
