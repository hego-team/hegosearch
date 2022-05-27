# HEGO-SEARCH
search engine ，java implemention

## 项目架构模块
1. 推荐系统：实现搜索引擎，其抽象接口和ElasticSearch类似，可以认为是实现一个搜索引擎基础服务。
2. 用户处理：用户处理程序，主要实现建立、销毁、修改收藏夹，登录，注册等功能，和抖音项目的用户功能相似。
3. 用户Server端处理：通过Client端发送来的数据，根据详情需要来调用搜索引擎和用户功能的基础服务。
4. 数据处理：主要工作是清洗数据集，并将数据集进行整理。
5. 前端：实现一个简易搜索引擎页面。

## 数据处理模块

#### 运行步骤
1. 安装python3.6
2. 根据操作系统修改init_index.py里的路径名
    ```
    db = leveldb.LevelDB('../data/doc')
    db = leveldb.LevelDB('../data/' + dbname)
    ```
3. 安装依赖
    如果leveldb安装失败可以试试: [https://github.com/happynear/py-leveldb-windows](https://github.com/happynear/py-leveldb-windows)
    
    ```
    cd script
    pip install -r requirements.txt
    ```
4. 把数据集放到script目录下然后运行脚本
    
    数据集：https://pan.baidu.com/s/1nxGKhtbX--QRl0wCCGe7Ng 
        提取码：7mi1 

    ```
   python init_index.py
    ```
#### 运行结果
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
    ...
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

1. 搜索文本/图片信息
2. 搜索结果分页，每页最多10条结果
3. 关键词高亮

    接口示例：GET /hego/result?query=医院患者&page=10
    
    ```
    {
        "time": 26.0     // 响应时间ms
        "total": 2134    // 查询结果总数
        "documents": [   // 查询结果
          {
            "docId":99374,
            "content":"西京<em>医院</em>脊柱外科专家团队为<em>患者</em>进行手术."，
            "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Ftranslate%2F386%2Fw729h457%2F20180712%2F-e3y-hfefkqr1069131.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630786327&t=95892981d9a434b2106fa3c07f55212d"
          },
          {
            "docId": 68063,
            "content": "[转载]颈椎病<em>患者</em>全身运动-青岛洪强骨科<em>医院</em>",
            "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fs8.sinaimg.cn%2Fmiddle%2F78eb8059hbbf4ac2408a0%26690&refer=http%3A%2F%2Fs8.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630659895&t=186f73a556e74867dcf7874e3891521f"
          },
          ...
        ],
        "page":         // 分页信息
          {"current":10,"limit":10,"rows":2134,"start":90,"end":100,"total":214,"from":5,"to":15}
    }
    ```
3. 关键词过滤

    接口示例：GET /hego/result?query=医院患者&filter=医院&page=10
     ```
        {
            "time": 28.0        // 响应时间ms
            "total": 514        // 查询结果总数
            "documents": [...]  // 查询结果
            "page": {...}       // 分页信息
              
        }
     ```     
4. 以图搜图功能

    调用百度通用物体和场景识别接口实现图片转query，再通过query查询
    
    百度API: https://ai.baidu.com/ai-doc/IMAGERECOGNITION/Xk3bcxe21
    
    接口：POST /hego/search/image
    
    示例parameters: file = "本地图片", page = 1
    
    ```
    {
        "time": 0.0,
        "total": 2401,
        "documents": [
            {
                "docId": 246154,
                "content": "我爱你 电影<em>截图</em>",
                "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201702%2F01%2F20170201120550_eV3kn.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630826106&t=b29b0a9b466df10005fc51979d8af552"
            },
        ...
        ],
        "page": {...}
    ```
    
    
    
    
### 待完成

相关搜索功能


### 优化

#### 搜索功能优化

参考ES的功能
https://www.elastic.co/cn/elasticsearch/features#asynchronous-search
1. 搜索提示词功能：例如用户输入字节可以提示字节跳动，用所有词频大于某个阈值的关键词构建前缀树实现
    https://developer.aliyun.com/article/765914
2. 提示器\拼写检查功能：向搜索体验中加入 did-you-mean（您指的是 XXX 吗）功能，让用户能够选择改正后的整个短语。例如用户输入学的校可以搜到学校的结果。
3. 跨语言搜索功能：输入query不是中文时，将query翻译成中文查询，可以调百度翻译API
    https://fanyi-api.baidu.com/api/trans/product/apidoc#appendix
4. 同义词搜索功能

#### 搜索性能优化
1. 查询速度提升 
    1. redis缓存doc
    2. 多线程搜索
    3. leveldb优化？
    4. jvm调优？
    
2. 数据量扩充


## 用户模块
### 待完成
1. 用户注册、登录、注销功能
2. 用户收藏夹功能
3. 用户搜索历史记录功能(option)
4. 用户最常搜索功能(option)
