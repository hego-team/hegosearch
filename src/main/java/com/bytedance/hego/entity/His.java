package com.bytedance.hego.entity;
//lombok简化实体类的开发

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data//将实体的类名映射到一个形式不一样的数据库的表名。进行反射时，不会直接根据实体的类名来进行sql语句的生成，会首先参考TableName注解。如果可没有注解，则根据实体类名生成
@TableName(value = "his")
public class His {

    private Integer id;
    private String owner;
    private String content;
    private Integer times;
}
