# HEGO-SEARCH
search engine ，java implemention

### 项目架构模块
1. 推荐系统：实现搜索引擎，其抽象接口和ElasticSearch类似，可以认为是实现一个搜索引擎基础服务。
2. 用户处理：用户处理程序，主要实现建立、销毁、修改收藏夹，登录，注册等功能，和抖音项目的用户功能相似。
3. 用户Server端处理：通过Client端发送来的数据，根据详情需要来调用搜索引擎和用户功能的基础服务。
4. 数据处理：主要工作是清洗数据集，并将数据集进行整理。
5. 前端：实现一个简易搜索引擎页面。

#### 数据处理
##### Dependencies
如果leveldb安装失败可以试试: https://github.com/happynear/py-leveldb-windows
```
pip install -r requirements.txt
```
##### Usage
将数据集(wukong_100m_62.csv)放进script文件夹下
```
cd script
python init_index.py
```
