package com.aurora.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 花未眠
 * redis操作
 **/
@SuppressWarnings("all")
public interface RedisService {
    /**
     * 保存属性
     *
     * @param key   key值
     * @param value value值
     * @param time  时间戳
     */
    void set(String key, Object value, long time);

    /**
     * 保存属性
     *
     * @param key   key值
     * @param value value值
     */
    void set(String key, Object value);

    /**
     * 获取属性
     *
     * @param key key值
     * @return 返回对象
     */
    Object get(String key);

    /**
     * 删除属性
     *
     * @param key key值
     * @return 返回成功
     */
    Boolean del(String key);

    /**
     * 批量删除属性
     *
     * @param keys key值集合
     * @return 返回删除数量
     */
    Long del(List<String> keys);

    /**
     * 设置过期时间
     *
     * @param key  key值
     * @param time 时间戳
     * @return 返回成功
     */
    Boolean expire(String key, long time);

    /**
     * 获取过期时间
     *
     * @param key key值
     * @return 返回时间戳
     */
    Long getExpire(String key);

    /**
     * 判断key是否存在
     *
     * @param key key值
     * @return 返回
     */
    Boolean hasKey(String key);

    /**
     * 按delta递增
     *
     * @param key   key值
     * @param delta delta值
     * @return 返回递增后结果
     */
    Long incr(String key, long delta);

    /**
     * 获取自增1后的 值
     *
     * @param key  key值
     * @param time 过期时间
     * @return 返回递增后结果
     */
    Long incrExpire(String key, long time);

    /**
     * 按delta递减
     *
     * @param key   key值
     * @param delta delta值
     * @return 返回递减后结果
     */
    Long decr(String key, long delta);

    /**
     * 获取Hash结构中的属性
     *
     * @param key     外部key值
     * @param hashKey 内部key值
     * @return 返回内部key的value
     */
    Object hGet(String key, String hashKey);

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @param value   内部key的value
     * @param time    过期时间
     * @return 返回是否成功
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @param value   内部key的value
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 直接获取整个Hash结构
     *
     * @param key 外部key值
     * @return 返回hashMap
     */
    Map<String, Object> hGetAll(String key);

    /**
     * 直接设置整个Hash结构
     *
     * @param key  外部key
     * @param map  hashMap值
     * @param time 过期时间
     * @return 返回是否成功
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * 直接设置整个Hash结构
     *
     * @param key 外部key
     * @param map hashMap值
     */
    void hSetAll(String key, Map<String, ?> map);

    /**
     * 删除Hash结构中的属性
     *
     * @param key     外部key值
     * @param hashKey 内部key值
     */
    void hDel(String key, Object... hashKey);

    /**
     * 判断Hash结构中是否有该属性
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @return 返回是否存在
     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * Hash结构中属性递增
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @param delta   递增条件
     * @return 返回递增后的数据
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Hash结构中属性递减
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @param delta   递增条件
     * @return 返回递减后的数据
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * zset添加分数
     *
     * @param key   关键
     * @param value 价值
     * @param score 分数
     * @return {@link Double}
     */
    Double zIncr(String key, Object value, Double score);

    /**
     * zset减少分数
     *
     * @param key   关键
     * @param value 价值
     * @param score 分数
     * @return {@link Double}
     */
    Double zDecr(String key, Object value, Double score);

    /**
     * zset根据分数排名获取指定元素信息
     *
     * @param key   关键
     * @param start 开始
     * @param end   结束
     * @return {@link Map<Object, Double>}
     */
    Map<Object, Double> zReverseRangeWithScore(String key, long start, long end);

    /**
     * 获取zset指定元素分数
     *
     * @param key   关键
     * @param value 价值
     * @return {@link Double}
     */
    Double zScore(String key, Object value);

    /**
     * 获取zset所有分数
     *
     * @param key 关键
     * @return {@link Map}
     */
    Map<Object, Double> zAllScore(String key);

    /**
     * 获取Set结构
     *
     * @param key key
     * @return 返回set集合
     */
    Set<Object> sMembers(String key);

    /**
     * 向Set结构中添加属性
     *
     * @param key    key
     * @param values value集
     * @return 返回增加数量
     */
    Long sAdd(String key, Object... values);

    /**
     * 向Set结构中添加属性
     *
     * @param key    key
     * @param time   过期时间
     * @param values 值集合
     * @return 返回添加的数量
     */
    Long sAddExpire(String key, long time, Object... values);

    /**
     * 是否为Set中的属性
     *
     * @param key   key
     * @param value value
     * @return 返回是否存在
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set结构的长度
     *
     * @param key key
     * @return 返回长度
     */
    Long sSize(String key);

    /**
     * 删除Set结构中的属性
     *
     * @param key    key
     * @param values value集合
     * @return 删除掉的数据量
     */
    Long sRemove(String key, Object... values);

    /**
     * 获取List结构中的属性
     *
     * @param key   key
     * @param start 开始
     * @param end   结束
     * @return 返回查询的集合
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List结构的长度
     *
     * @param key key
     * @return 长度
     */
    Long lSize(String key);

    /**
     * 根据索引获取List中的属性
     *
     * @param key   key
     * @param index 索引
     * @return 对象
     */
    Object lIndex(String key, long index);

    /**
     * 向List结构中添加属性
     *
     * @param key   key
     * @param value value
     * @return 增加后的长度
     */
    Long lPush(String key, Object value);

    /**
     * 向List结构中添加属性
     *
     * @param key   key
     * @param value value
     * @param time  过期时间
     * @return 增加后的长度
     */
    Long lPush(String key, Object value, long time);

    /**
     * 向List结构中批量添加属性
     *
     * @param key    key
     * @param values value 集合
     * @return 增加后的长度
     */
    Long lPushAll(String key, Object... values);

    /**
     * 向List结构中批量添加属性
     *
     * @param key    key
     * @param time   过期时间
     * @param values value集合
     * @return 增加后的长度
     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * 从List结构中移除属性
     *
     * @param key   key
     * @param count 总量
     * @param value value
     * @return 返回删除后的长度
     */
    Long lRemove(String key, long count, Object value);

    /**
     * 向bitmap中新增值
     *
     * @param key    key
     * @param offset 偏移量
     * @param b      状态
     * @return 结果
     */
    Boolean bitAdd(String key, int offset, boolean b);

    /**
     * 从bitmap中获取偏移量的值
     *
     * @param key    key
     * @param offset 偏移量
     * @return 结果
     */
    Boolean bitGet(String key, int offset);

    /**
     * 获取bitmap的key值总和
     *
     * @param key key
     * @return 总和
     */
    Long bitCount(String key);

    /**
     * 获取bitmap范围值
     *
     * @param key    key
     * @param limit  范围
     * @param offset 开始偏移量
     * @return long类型集合
     */
    List<Long> bitField(String key, int limit, int offset);

    /**
     * 获取所有bitmap
     *
     * @param key key
     * @return 以二进制字节数组返回
     */
    byte[] bitGetAll(String key);

    /**
     * 向hyperlog中添加数据
     *
     * @param key   key
     * @param value 值
     * @return {@link Long}
     */
    Long hyperAdd(String key, Object... value);

    /**
     * 获取hyperlog元素数量
     *
     * @param key key
     * @return {@link Long} 元素数量
     */
    Long hyperGet(String... key);

    /**
     * 删除hyperlog数据
     *
     * @param key key
     */
    void hyperDel(String key);

    /**
     * 增加坐标
     *
     * @param key  key
     * @param x    x
     * @param y    y
     * @param name 地点名称
     * @return 返回结果
     */
    Long geoAdd(String key, Double x, Double y, String name);

    /**
     * 根据城市名称获取坐标集合
     *
     * @param key   key
     * @param place 地点
     * @return 坐标集合
     */
    List<Point> geoGetPointList(String key, Object... place);

    /**
     * 计算两个城市之间的距离
     *
     * @param key      key
     * @param placeOne 地点1
     * @param placeTow 地点2
     * @return 返回距离
     */
    Distance geoCalculationDistance(String key, String placeOne, String placeTow);

    /**
     * 获取附该地点附近的其他地点
     *
     * @param key      key
     * @param place    地点
     * @param distance 附近的范围
     * @param limit    查几条
     * @param sort     排序规则
     * @return 返回附近的地点集合
     */
    GeoResults<RedisGeoCommands.GeoLocation<Object>> geoNearByPlace(String key, String place, Distance distance, long limit, Sort.Direction sort);

    /**
     * 获取地点的hash
     *
     * @param key   key
     * @param place 地点
     * @return 返回集合
     */
    List<String> geoGetHash(String key, String... place);
}

