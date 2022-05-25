# HEGO-SEARCH
search engine ，java implemention

## 项目架构模块
1. 推荐系统：实现搜索引擎，其抽象接口和ElasticSearch类似，可以认为是实现一个搜索引擎基础服务。
2. 用户处理：用户处理程序，主要实现建立、销毁、修改收藏夹，登录，注册等功能，和抖音项目的用户功能相似。
3. 用户Server端处理：通过Client端发送来的数据，根据详情需要来调用搜索引擎和用户功能的基础服务。
4. 数据处理：主要工作是清洗数据集，并将数据集进行整理。
5. 前端：实现一个简易搜索引擎页面。

## 数据处理模块

如果leveldb安装失败可以试试: [https://github.com/happynear/py-leveldb-windows](https://github.com/happynear/py-leveldb-windows)

```
cd script
pip install -r requirements.txt
python init_index.py
```

运行脚本后在data目录下生成doc, invertedIndex, positiveIndex

doc: 保存csv中所有信息

```
{
    "0": [
	    "content": "预计四季度或明年的出口增速可能转负,但整体仍保持着比较高的规模."
	    "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F0819%252F14f73805j00qy27bs000xc000hs009hg.jpg%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1632609419&t=fb64f531652a851f8c25a1c1eabc141b"
    ],
    "1": [
	    "content": "当然从医院患者人满为患的情况看,也同样缺少医生"
	    "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwx4.sinaimg.cn%2Fcrop.0.11.1786.993%2F0033ImPzly1gkp0ee8jbrj61dm0rwu0x02.jpg&refer=http%3A%2F%2Fwx4.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630658390&t=29c486e6298b9fcb52df8088e676fd87"
	],
''''''
}
```

invertedIndex: 保存倒排索引以及word-doc对应的tf-idf分数

```
{
	"男孩": {
		"25": 0.1962202932604879, 
		"291": 0.7358260997268297,
		...
	},
	"医院": {
		"1": 0.3402687707199622, 
		"64": 0.14011067029645502,
		...
	},
	...
}
```

positiveIndex：保存正排索引，之后实现插入、删除索引时可用于计算tf-idf

```
{
	"0": ["预计", "四季", "季度", "四季度", ...],
	"1": ["医院", "患者", "人满为患", "情况", ...],
	...
}
```

## 搜索模块

### 已完成

1. 搜索文本/图片信息，并将搜索结果分页，每页最多10条结果
    
    接口示例：GET /hego/result?query=医院患者&page=10
    
    ```
    {
        "time": 26.0     // 响应时间ms
        "total": 2134    // 查询结果总数
        "documents": [   // 查询结果
          {
            "docId":99374,
            "content":"西京医院脊柱外科专家团队为患者进行手术."
            "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Ftranslate%2F386%2Fw729h457%2F20180712%2F-e3y-hfefkqr1069131.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630786327&t=95892981d9a434b2106fa3c07f55212d"
          },
          {
            "docId": 68063,
            "content": "[转载]颈椎病患者全身运动-青岛洪强骨科医院",
            "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fs8.sinaimg.cn%2Fmiddle%2F78eb8059hbbf4ac2408a0%26690&refer=http%3A%2F%2Fs8.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630659895&t=186f73a556e74867dcf7874e3891521f"
          },
          ...
        ],
        "page":         // 分页信息
          {"current":10,"limit":10,"rows":2134,"start":90,"end":100,"total":214,"from":5,"to":15}
    }
    ```
        

### 待完成

1. 关键词高亮
2. 支持模糊查询
3. 查询速度优化
