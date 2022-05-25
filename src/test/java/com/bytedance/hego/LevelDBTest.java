package com.bytedance.hego;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bytedance.hego.entity.Document;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class LevelDBTest {

    private static final Charset CHARSET = Charset.forName("utf-8");

    @Test
    public void getDocTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        File file = new File("data/doc");
        DB db = null;
        try {
            db = factory.open(file, options);
            byte[] keyByte1 = "1".getBytes(CHARSET);
            byte[] keyByte2 = "2".getBytes(CHARSET);

            String value1 = new String(db.get(keyByte1), CHARSET);
            Document doc = JSON.parseObject(value1, new TypeReference<Document>() {});
            System.out.println(doc);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void getInvertedIndexTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        File file = new File("data/invertedIndex");
        DB db = null;
        try {
            db = factory.open(file, options);
            byte[] keyByte1 = "男孩".getBytes(CHARSET);
            // 会写入磁盘中

            String value1 = new String(db.get(keyByte1), CHARSET);
            System.out.println(value1);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void getPositiveIndexTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        File file = new File("data/positiveIndex");
        DB db = null;
        try {
            db = factory.open(file, options);
            byte[] keyByte1 = "1".getBytes(CHARSET);
            // 会写入磁盘中

            String value1 = new String(db.get(keyByte1), CHARSET);
            System.out.println(value1);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
