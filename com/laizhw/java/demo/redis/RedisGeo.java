package demo.redis;

import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * redis geo基本覆盖了主流的点位相关场景，比如附近的人，周边的店铺等等，
 * 我们熟悉这些api就可以解决这些需求了，没此类需求的也可当一个知识储备,
 * 如果你有更复杂的地理信息存储需求，可以参考我的MySQL地理信息处理文章-
 * MySQL空间数据存储及函数 - 掘金
 * <p>
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@Component
public class RedisGeo {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CITY = "city";

    /**
     * 添加点位
     *
     * @param name 名称
     * @param x    经度
     * @param y    纬度
     */
    public void add(String name, double x, double y) {
        redisTemplate.opsForGeo().add(CITY, new Point(x, y), name);
    }


    /**
     * 距离（km）
     *
     * @param city1
     * @param city2
     * @return
     */
    public double distance(String city1, String city2) {
        Distance distance = redisTemplate.opsForGeo().distance(CITY, city1, city2, RedisGeoCommands.DistanceUnit.KILOMETERS);
        return distance.getValue();
    }

    /**
     * 周边城市
     *
     * @param city
     * @param distance
     * @return
     */
    public List<Map<String, Object>> circum(String city, double distance) {
        // 获取中心城市坐标
        List<Point> positions = redisTemplate.opsForGeo().position(CITY, city);
        List<Map<String, Object>> cityList = new ArrayList<>();
        if (CollectionUtils.isEmpty(positions)) {
            return cityList;
        }
        Point point = positions.stream().findFirst().get();
        Circle circle = new Circle(point, new Distance(distance, Metrics.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(5);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.opsForGeo()
                .radius(CITY, circle, args);
        for (GeoResult<RedisGeoCommands.GeoLocation<Object>> result : results) {
            RedisGeoCommands.GeoLocation<Object> content = result.getContent();
            String name = (String) content.getName();
            Point cityPoint = content.getPoint();
            Distance cityDistance = result.getDistance();
            // 为了展示这些api的使用，我将返回值包装成map
            Map<String, Object> cityMap = new HashMap<>();
            cityMap.put("name", name);
            cityMap.put("lng", cityPoint.getX());
            cityMap.put("lat", cityPoint.getY());
            cityMap.put("distance", cityDistance.getValue());
            cityList.add(cityMap);
        }
        return cityList;
    }


}
