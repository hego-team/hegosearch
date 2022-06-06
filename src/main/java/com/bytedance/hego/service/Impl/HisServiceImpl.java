package com.bytedance.hego.service.Impl;

import com.bytedance.hego.entity.His;

import com.bytedance.hego.dao.HisDAO;
import com.bytedance.hego.service.HisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;



@Service
public class HisServiceImpl implements HisService {

    @Autowired
    private HisDAO hisDAO;

    @Override
    public List<His> findAll(String owner) {
        return hisDAO.findAll(owner);
    }

    @Override
    public List<His> sortHis(List<His> hislist) {
        hislist.sort(new Comparator<His>() {
            @Override
            public int compare(His o1, His o2) {
                return o2.getTimes()-o1.getTimes();
            }
        });
        return hislist;
    }

    @Override
    public His save(His his) {
            return hisDAO.update(his) ? hisDAO.findById(his.getId()) : null; // 更新成功则返回tags，失败则返回null

    }
    @Override
    public His add(His his) {
        if (his.getId() == null) {
            hisDAO.insert(his);
            return his.getId() != null ? hisDAO.findById(his.getId()) : null; // 保存成功则返回his，失败则返回null
        }
        else {return null;}
    }

    @Override
    public His findById(int id) {
        return hisDAO.findById(id);
    }

    @Override
    public His findByContent(String owner,String name) {
        return hisDAO.findByContent(owner,name);
    }

    @Override
    public boolean delete(int id) {
        His his = hisDAO.findById(id);
        if (his == null) {
            return false;
        }

        return hisDAO.deleteById(id);
    }
}
