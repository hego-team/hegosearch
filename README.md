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
#### 文本搜索功能
1. 搜索文本/图片信息
2. 搜索结果分页，每页最多10条结果
3. 关键词高亮

    接口示例：GET http://localhost:8443/hego/search/text?query=医院患者&page=6&limit=10
    
4. 关键词过滤

    接口示例：GET http://localhost:8443/hego/search/text?query=学校&filter=学生&page=6&limit=10
 
5. 提示器\拼写检查功能：
 
    基于困惑集实现常见错误的纠错功能，困惑集存储在src\main\resources\data\word_checker_zh.txt下
      
    接口实例：GET http://localhost:8443/hego/search/text?query=学的校&page=6&limit=10

6. 跨语言搜索功能：输入query不是中文时，将query翻译成中文查询，调百度翻译API
    https://fanyi-api.baidu.com/api/trans/product/apidoc#appendix
    
    接口实例：GET http://localhost:8443/hego/search/text?query=school&page=6&limit=10

返回JSON示例
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
    "check": []     // 查询结果为空时开启拼写检查
    "page":         // 分页信息
      {"current":10,"limit":10,"rows":2134,"start":90,"end":100,"total":214,"from":5,"to":15}
}
```   
#### 图片搜索功能

调用百度通用物体和场景识别接口实现图片转query，再通过query查询

百度API: https://ai.baidu.com/ai-doc/IMAGERECOGNITION/Xk3bcxe21

接口：POST http://localhost:8443/hego/search/image

示例parameters: Multipartfile = "本地图片", page = 1

#### 搜索提示词功能
 
将数据集中词频大于5的关键词构建前缀树，返回前十个高频提示词

接口示例：GET http://localhost:8443/hego/search/prompt?query=中国 

```
["中国馆","中国文联","中国美术学院","中国美术馆","中国美术家协会","中国戏曲","中国戏剧出版社","中国式","中国林业","中国香港"]
```
    
### 待完成

相关搜索功能


### 优化

#### 搜索功能优化

参考ES的功能
https://www.elastic.co/cn/elasticsearch/features#asynchronous-search


4. 同义词搜索功能

    暂时未找到对应的同义词词库，找到了一个开源的python同义词转换工具：https://github.com/chatopera/Synonyms

#### 搜索性能优化
1. 查询速度提升 
    1. redis缓存doc 
    2. 多线程搜索
    3. leveldb优化？
    4. jvm调优？
    
2. 数据量扩充


## 用户模块
### 功能清单
1. 用户注册、登录、注销功能
2. 用户收藏夹功能
3. 用户搜索历史记录功能(option)
4. 用户最常搜索功能(option)


当前：收藏夹目录未实现。

### 初始化

1. 运行script/init_sql.sql文件初始化数据库数据。
1. 改数据库的端口，用户名，密码配置。

### 功能：

### 用户相关请求：

1. 使用session控制用户登录状态。
2. 通过对用户表操作实现登录，登出，注册，注销（删号），改密，改信息。

##### 登录

POST http://localhost:8443/hego/login?name=Default&password=Default

- 参数：用户名、密码。

返回的json示例：（登录用户的全部的用户信息，role表示用户权限等级，目前没有用；lastLoginTime是上次登录时间，目前没有用；）

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": {
        "user": {
            "id": 1,
            "name": "Default",
            "password": "Default",
            "role": 1,
            "email": "1111111111@qq.com",
            "lastLoginTime": 1653955582967
        }
    }
}
```

其他示例：

http://localhost:8443/hego/login?name=John&password=111111



##### 登出

GET http://localhost:8443/hego/logout

- 无参数。
- 退出当前账户。

json示例：返回成功或错误信息。

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": null
}
```





##### 注册

POST http://localhost:8443/hego/register?name=John&password=111111&rePassword=111111&email=john@163.com

- 参数：用户名、密码、重复密码、邮箱信息（可选）

json示例：

（目前不能检查email格式；目前密码只有位数限制；用户名有非重复限制；成功返回用户信息；失败返回错误类型提示；通过注册只能添加0级普通账户；）

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": {
        "user": {
            "id": 2,
            "name": "John",
            "password": "111111",
            "role": 0,
            "email": "john@163.com",
            "lastLoginTime": 1653956412000
        }
    }
}
```



##### 注销（删除）账户

DELETE http://localhost:8443/hego/user/delete

- /

- 无参数，注销当前用户。

json示例：返回成功或失败信息

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": null
}
```





##### 改信息

POST http://localhost:8443/hego/user/save?name=John&email=11111@qq.com

- 根据当前session中用户id查找，修改名称、邮箱个人信息。

json示例：返回成功或失败信息

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": null
}
```





##### 改密

PUT http://localhost:8443/hego/user/change_password?oldPass=111111&newPass=222222&reNewPass=222222

- 参数：旧密、新密、重复新密。

json示例：返回成功或失败信息

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": null
}
```





### 收藏夹相关请求。 TAGS:

1. 对数据表操作实现了收藏夹的增删改查。
2. 条目有name不能重复限制，方便不根据id（主键）而根据name修改删除。
3. 无用户登录状态不提供收藏功能。（如果意外发出收藏请求，返回信息”请先登录”）。

##### 添加条目

POST http://localhost:8443/hego/tags/add?newname=哇咔咔&newdocid=344

- 参数：新条目名称、新docID。

返回json示例：返回成功或失败信息

```json
{
    "code": null,
    "success": true,
    "msg": "已添加到收藏夹",
    "data": null
}
```



##### 获得当前用户的收藏夹

GET http://localhost:8443/hego/tags/list

- 无参数。

返回json示例：返回当前用户（owner）的所有收藏条目。

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": {
        "docList": [
            {
                "docId": 34,
                "content": "南,北掸邦军战事胶着 掸北昔卜镇难民人数已攀升至1600余人",
                "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181229%2F9f5a4a178f1c44aeabd4ee40fa1f27e1.jpeg&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630764501&t=7c6620ffa5c774d2d0d6d91547ab048e"
            },
            {
                "docId": 233,
                "content": "外婆去世后,孙子在她的车库里发现了一个巨大的保险柜,经过翻找他在",
                "image": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180714%2F154e8fc560d449109b629f9de629c308.jpeg&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630585735&t=b60100d8875513553caf2ae44d3bb931"
            }
        ]
    }
}
```



##### 删除条目

DELETE http://localhost:8443/hego/tags/delete?name=1111
//按名称删除收藏夹项。

返回json示例：返回成功或失败信息

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": null
}
```



##### 编辑收藏夹项

POST http://localhost:8443/hego/tags/save?oldname=百度一下，也不知道&newname=百度一下，你就知道

- 编辑收藏夹项名称。

- 此示例的数据是由sql脚本生成的初始数据，在Default用户的收藏夹内。

返回json示例：返回成功或失败信息

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": null
}
```





### 历史记录相关请求。HIS：

1. 通过操作数据库实现历史记录的增删查（两种）。
2. 条目添加无限制。
3. 无登录状态不保存历史记录。

##### 搜索时自动触发添加

POST http://localhost:8443/hego/his/add?newquery=1111

- 可多次添加相同项通过times++计频数。
- 添加条目到当前用户历史记录中。

返回json示例：返回成功或失败（当前无用户登录），均无提示信息。

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": null
}
```

##### 删除

DELETE http://localhost:8443/hego/his/delete?content=1111

- 通过content项查询当前用户历史并删除。

返回json示例：返回成功或失败提示信息。

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": null
}
```



##### 时间排序查看

GET http://localhost:8443/hego/his/list/default

- 返回按加入顺序（从早到晚，前端展示可能要逆序）排列的搜索历史记录。
- 无参数，default表示默认顺序。
- 只能查看当前用户的历史记录。

返回json示例：

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": {
        "hisList": [
            {
                "id": 1,
                "owner": "Default",
                "content": "1111",
                "times": 2
            },
            {
                "id": 3,
                "owner": "Default",
                "content": "2222",
                "times": 1
            },
            {
                "id": 4,
                "owner": "Default",
                "content": "3333",
                "times": 3
            }
        ]
    }
}
```



##### 频度排序查看

GET http://localhost:8443/hego/his/list/sort

- 返回按频度顺序（从高频到低频）排列的搜索历史记录。
- 无参数，sort表示频度顺序。
- 只能查看当前用户的历史记录。

返回json示例：

```json
{
    "code": null,
    "success": true,
    "msg": null,
    "data": {
        "hisList": [
            {
                "id": 4,
                "owner": "Default",
                "content": "3333",
                "times": 3
            },
            {
                "id": 1,
                "owner": "Default",
                "content": "1111",
                "times": 2
            },
            {
                "id": 3,
                "owner": "Default",
                "content": "2222",
                "times": 1
            }
        ]
    }
}
```






