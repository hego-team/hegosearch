package com.bytedance.hego.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bytedance.hego.util.MD5Util;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class BtService {
    /**
     * 自动检测语言
     */
    public static final String AUTO = "auto";
    /**
     * 中文
     */
    public static final String ZH = "zh";
    /**
     * 英语
     */
    public static final String EN = "en";
    /**
     * 繁体中文
     */
    public static final String CHT = "cht";

    /**
     * 要翻译的词条
     */
    private String q;
    private String from;
    private String to;
    /**
     * 百度翻译开发者信息的APP ID
     * 上面图片上的APP ID
     */
    private final String APPID = "20220601001236249";
    private String salt = new Random().nextInt(99999)+"";
    /**
     * 百度翻译开发者信息的密钥：上面图片上的密钥
     */
    private final String KEY = "Ytslk2vxjAbz_R6VzRUc";
    //    private String sign = MD5Util.md5(APPID + q + salt + KEY);
    private String sign;

    private String url;

    private static BtService bt = new BtService();

    private BtService(){}

    /**
     *
     * @return BT,获取本类实例
     */
    public static BtService getInstance(){
        return bt;
    }

    /**
     *
     * @param word：要翻译的词条
     * @return 获取翻译的结果
     */
    public  String translate(String word){
        this.q = word;
        this.from = "en";
        this.to = "zh";
        this.sign = MD5Util.md5(APPID + this.q + salt + KEY);
        this.url = "http://fanyi-api.baidu.com/api/trans/vip/translate?q="+this.q+"&from="+this.from+"&to="+this.to+"&appid="+APPID+"&salt="+this.salt+"&sign="+this.sign;
        String result = null;
        try {
            result = doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("发送doGet请求时，出现错误");
        }
        return result;
    }

    /**
     *
     * @param url 调用百度翻译openapi的url
     * @return 返回的字符串结果
     * @throws IOException
     */
    private String doGet(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
//        System.out.println("StatusLine:"+response.getStatusLine());
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity);
//        System.out.println(str);
        JSONObject jsonObject = (JSONObject) JSON.parse(str);
        JSONArray jsonArray = (JSONArray) jsonObject.get("trans_result");
        JSONObject result = (JSONObject)jsonArray.get(0);
        return result.get("dst").toString();
    }

}
