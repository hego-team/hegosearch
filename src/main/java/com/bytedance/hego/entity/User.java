//package com.bytedance.hego.entity;
////lombok简化实体类的开发
//import com.baomidou.mybatisplus.annotation.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Data//将实体的类名映射到一个形式不一样的数据库的表名。进行反射时，不会直接根据实体的类名来进行sql语句的生成，会首先参考TableName注解。如果可没有注解，则根据实体类名生成
//@TableName(value = "User")
//public class User {
//    //分别对应映射数据库表中的四个字段
//
//    @TableId(value = "uid",type = IdType.AUTO)   //实体类的属性名是 id，数据库的列名是 uid，此时使用 value 属性将属性名映射到列名
//    private long id;
//    private String name;
//    private Integer age;
//    private String email;
//
//    //符合MP映射规律
//    //@TableField(value = "create_time")
//    @TableField(fill = FieldFill.INSERT)
//    private LocalDateTime createTime;
//
//    //如果数据库表字段为username，则不符合映射规律，必须手工做映射。
//    @TableField(value = "data_time",fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updadeTime;
//
//    @TableLogic
//    @TableField(value = "is_deleted")
//    private boolean deleted;
//}
package com.bytedance.hego.entity;
//lombok简化实体类的开发
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;


@Data//将实体的类名映射到一个形式不一样的数据库的表名。进行反射时，不会直接根据实体的类名来进行sql语句的生成，会首先参考TableName注解。如果可没有注解，则根据实体类名生成
@TableName(value = "user")
public class User {
    //分别对应映射数据库表中的四个字段
    private Integer id;
    private String name;
    private String password;
    private Integer role; // 默认是0, 普通用户，可登陆、注册、查看和修改个人信息； 1表示管理员，可登陆、查看和修改自己及其他人的信息
    private String email;
    private Date lastLoginTime;


}