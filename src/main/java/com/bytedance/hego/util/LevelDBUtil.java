package com.bytedance.hego.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bytedance.hego.entity.Document;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class LevelDBUtil {

    private DB docStorages;
    private DB invertedIndexStorages;
    private DB positiveIndexStorages;

    @Value("${levelDB.dir.path}")
    private String DB_DIR_PATH;

    private static final String CHARSET = "utf-8";

    //spring启动时初始化
    @PostConstruct
    public void initLevelDB(){
        this.docStorages = newStorage("doc");
        this.invertedIndexStorages = newStorage("invertedIndex");
        this.positiveIndexStorages = newStorage("positiveIndex");
    }

    private DB newStorage(String dbName) {
        // 初始化docStorage
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        options.cacheSize(100 * 1024 * 1024); // 100MB cache
        options.createIfMissing(true);
        options.logger(System.out::println);  // 打印日志
        DB db = null;
        try {

            db = factory.open(new File(DB_DIR_PATH + dbName), options);
        } catch (IOException e) {
            log.error("levelDB启动异常", e);
        }
        return db;
    }

    //基于fastjson的对象序列化
    private byte[] serializer(Object obj) {
        return JSON.toJSONBytes(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 存放数据
     * @param key 键
     * @param val 值
     */
    private void put(String key, Object val, DB db) {
        try {
            db.put(key.getBytes(CHARSET), this.serializer(val));
        } catch (UnsupportedEncodingException e) {
            log.error("编码转化异常", e);
        }
    }


    /**
     * 根据document_id获取document
     * @param key document_id
     * @return value document对象
     */
    public Document getDoc(String key) {
        String value = null;
        try {
            byte[] keyByte = key.getBytes(CHARSET);
            value = new String(this.docStorages.get(keyByte), CHARSET);
        } catch (Exception e) {
            log.error("levelDB get error", e);
        }

        if(value != null)
            return JSON.parseObject(value, new TypeReference<Document>() {});
        else
            return null;
    }


    /**
     * 根据keyword获取document_id list
     * @param key keyword
     * @return value doc_ids: {id: score}
     */
    public Map<Integer, Float> getDocIds(String key)  {
        String value = null;
        try {
            byte[] keyByte = key.getBytes(CHARSET);
            if (this.invertedIndexStorages.get(keyByte) != null) {
                value = new String(this.invertedIndexStorages.get(keyByte), CHARSET);
            } else {
                value = "";
            }

        } catch (Exception e) {
            log.error("levelDB get error or the keyword does not exist", e);
        }

        if(!value.equals(""))
            return JSON.parseObject(value, new TypeReference<HashMap<Integer, Float>>() {});
        else
            return new HashMap<Integer, Float>();
    }

    /**
     * 根据key删除数据
     * @param key 键
     */
    private void delete(String key, DB db) {
        try {
            db.delete(key.getBytes(CHARSET));
        } catch (Exception e) {
            log.error("levelDB delete error", e);
        }

    }

    /**
     * 获取所有key
     * @return 所有的keys
     */
    private List<String> getKeys(DB db) {

        List<String> list = new ArrayList<>();
        DBIterator iterator = null;
        try {
            iterator = db.iterator();
            while (iterator.hasNext()) {
                Map.Entry<byte[], byte[]> item = iterator.next();
                String key = new String(item.getKey(), CHARSET);
                list.add(key);
            }
        } catch (Exception e) {
            log.error("遍历发生异常", e);
        } finally {
            if (iterator != null) {
                try {
                    iterator.close();
                } catch (IOException e) {
                    log.error("遍历发生异常", e);
                }

            }
        }
        return list;
    }

    //spring销毁对象前关闭
    @PreDestroy
    public void closeAllDB() {
        this.closeDB(this.docStorages);
        this.closeDB(this.invertedIndexStorages);
        this.closeDB(this.positiveIndexStorages);
    }

    private void closeDB(DB db) {
        if (db != null) {
            try {
                db.close();
            } catch (IOException e) {
                log.error("levelDB 关闭异常", e);
            }
        }
    }

}

