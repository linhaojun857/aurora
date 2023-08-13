package com.aurora;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import static com.aurora.constant.RedisConstant.BLOOM_FILTER;

@SpringBootApplication
@MapperScan("com.aurora.mapper")
public class AuroraSpringbootApplication implements CommandLineRunner {
    @Autowired
    private RedissonClient redissonClient;

    public static void main(String[] args) {
        SpringApplication.run(AuroraSpringbootApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
        //初始化布隆过滤器
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER);
        //设置数据规模 误判率 预计统计元素数量为100000，期望误差率为0.01
        bloomFilter.tryInit(100000, 0.01);
    }

}
