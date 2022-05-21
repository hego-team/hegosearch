package com.bytedance.hego.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 针对持久层的配置，可以把所有关于持久层的配置都放到这。进行集中的管理。
 */

/**
 * 配置MP的分页插件
 */
@Configuration  //添加注解，证明当前的类是一个配置文件，程序启动时，会自动被Spring容器以配置的形式读取
@MapperScan("com.bytedance.hego.mapper") //mapper扫描。针对持久层的扫描配置。
public class MybatisPlusConfig {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration
     * #useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        //创建一个Interceptor插件管理器（或者说拦截器的一个管理器）
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        //将分页插件的拦截器对象创建出来配置到interceptor对象里面，然后作为bean对象返回

        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); //添加分页插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//添加乐观锁插件

        return interceptor;
    }
}
