package com.bytedance.hego.dao;

import com.bytedance.hego.entity.His;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HisDAO {

    /**
     * findall user
     * @param owner
     * @return
     */
    public List<His> findAll(@Param("owner") String owner);


    /**
     * findById user
     * @param id
     * @return
     */
    public His findById(int id);


    /**
     * findById user
     * @param his
     * @return
     */
    public boolean update(His his);

    /**
     * findByName user
     * @param owner
     * @param content
     * @return
     */
    public His findByContent(@Param("owner") String owner,@Param("content") String content);
    /**
     * insert his
     * @param his
     * @return
     */

    public boolean insert(His his);

    /**
     * delete his by id
     * @param id
     * @return
     */
    public boolean deleteById(int id);

}
