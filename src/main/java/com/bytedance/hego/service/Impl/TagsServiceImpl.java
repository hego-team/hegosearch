package com.bytedance.hego.service.Impl;

import com.bytedance.hego.entity.Tags;

import com.bytedance.hego.dao.TagsDAO;
import com.bytedance.hego.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class TagsServiceImpl implements TagsService {

    @Autowired
    private TagsDAO tagsDAO;

    @Override
    public List<Tags> findAll(String owner) {
        return tagsDAO.findAll(owner);
    }


    @Override
    public Tags save(Tags tags) {
        if (tags.getId() != null) {
            Tags oldTags = tagsDAO.findById(tags.getId());
            if (oldTags == null) {
                return null;
            }
            return tagsDAO.update(tags) ? tagsDAO.findById(tags.getId()) : null; // 更新成功则返回tags，失败则返回null
        } return null;
    }

    public Tags add(Tags tags) {
        if (tags.getId() == null) {
            tagsDAO.insert(tags);
            return tags.getId() != null ? tagsDAO.findById(tags.getId()) : null; // 保存成功则返回tags，失败则返回null
        }
        else {return null;}
    }

    @Override
    public Tags findById(int id) {
        return tagsDAO.findById(id);
    }

    @Override
    public Tags findByName(String owner,String name) {
        return tagsDAO.findByName(owner,name);
    }

    @Override
    public boolean delete(int id) {
        Tags tags = tagsDAO.findById(id);
        if (tags == null) {
            return false;
        }

        return tagsDAO.deleteById(id);
    }
}
