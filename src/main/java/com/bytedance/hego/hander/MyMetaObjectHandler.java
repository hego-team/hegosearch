package com.bytedance.hego.hander;

/**
 * MP自动填充功能：
 * 元对象处理器接口实现
 * 填充日期字段时调用里面的方法
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        log.info("Insert 自动填充");
        this.strictInsertFill(metaObject, "createTime",LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject, "updadeTime",LocalDateTime.class,LocalDateTime.now());

        //2、自动填充的优化。【业务层赋值，不执行自动填充】
        //判断当前对象的自动填充属性是否进行了赋值
        Object age = this.getFieldValByName("age", metaObject);
        if (age==null){
            log.info("Insert age  自动填充。。。。。。。");
            this.strictInsertFill(metaObject, "age",Integer.class,3);
        }


        //1、自动填充的优化【多张表的不同字段做自动填充都是在这，所以可以先判断属性在不在对象信息里，提高执行效率】
        //判断当前对象的自动填充属性是否包含当前属性
        boolean hasAuthor = metaObject.hasSetter("author");  //metaObject：要操作对象的元数据信息
        if (hasAuthor){
            log.info("start insert fill author....");
            this.strictInsertFill(metaObject, "author", String.class, "Helen");
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Updade 自动填充");
        this.strictUpdateFill(metaObject, "updadeTime",LocalDateTime.class,LocalDateTime.now());
    }
}
