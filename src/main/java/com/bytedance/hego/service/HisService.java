package com.bytedance.hego.service;


import com.bytedance.hego.entity.His;


import java.util.List;

public interface HisService {

    /**
     * 查询该用户所有记录
     * @return
     */
    List<His> findAll(String owner);
    /**
     * 查询该用户所有记录
     * @return
     */
    List<His> sortHis(List<His> hisList);



    His findByContent(String owner,String content);
    /**
     * 新增条目
     * @param his
     * @return
     */
    His add(His his);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    His findById(int id);


    /**
     * 更新或保存条目
     * @param his
     * @return
     */
    His save(His his);
    /**
     * 根据ID删除条目
     * @param id
     * @return
     */
    boolean delete(int id);
}
