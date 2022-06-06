package com.bytedance.hego.service;

import com.bytedance.hego.entity.User;
import com.bytedance.hego.entity.Tags;

import java.util.List;

public interface TagsService {

    /**
     * 查询该用户所有记录
     * @return
     */
    List<Tags> findAll(String owner);

    /**
     * 更新或保存条目
     * @param tags
     * @return
     */
    Tags save(Tags tags);

    Tags findByName(String owner,String name);
    /**
     * 新增条目
     * @param tags
     * @return
     */
    Tags add(Tags tags);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    Tags findById(int id);



    /**
     * 根据ID删除条目
     * @param id
     * @return
     */
    boolean delete(int id);
}
