package com.bytedance.hego.dao;

import com.bytedance.hego.entity.Tags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagsDAO {

    /**
     * findall user
     * @param owner
     * @return
     */
    public List<Tags> findAll(@Param("owner") String owner);


    /**
     * findById user
     * @param id
     * @return
     */
    public Tags findById(int id);


    /**
     * findById user
     * @param tags
     * @return
     */
    public boolean update(Tags tags);

    /**
     * findByName user
     * @param owner
     * @param name
     * @return
     */
    public Tags findByName(@Param("owner") String owner,@Param("name") String name);
    /**
     * insert tags
     * @param tags
     * @return
     */

    public boolean insert(Tags tags);

    /**
     * delete tags by id
     * @param id
     * @return
     */
    public boolean deleteById(int id);

}
