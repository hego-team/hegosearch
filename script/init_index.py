# encoding=utf-8
import jieba
import pandas as pd
import math
from collections import Counter
# 保存leveldb
import leveldb
import json
# Config logging
import logging


# 去掉文本中的空格
def process(our_data):
    m1 = map(lambda s: s.replace(' ', ''), our_data)
    return list(m1)


# 文本只保留汉字
def is_chinese(uchar):
    if uchar >= u'\u4e00' and uchar <= u'\u9fa5':
        return True
    else:
        return False


def format_str(content):
    content_str = ''
    for i in content:
        if is_chinese(i):
            content_str = content_str + i
    return content_str


def eliminate_punc(words):
    chinese_list = []
    for line in words:
        chinese_list.append(format_str(line))
    return chinese_list


# 中文分词
def tokenize(strs):
    tokenize_list = []
    for s in strs:
        seg_list = jieba.cut_for_search(s)  # 搜索引擎模式
        tokenize_list.append(list(seg_list))
    return tokenize_list


# 去停用词&&生成正排/倒排索引
def gen_index(contents, stopwords):
    inverted_index = {}
    positive_index = {}

    for i in range(len(contents)):
        doc = contents[i]
        positive_index[i] = []

        for word in doc:
            if word in stopwords:
                continue

            positive_index[i].append(word)
            if word in inverted_index:
                inverted_index[word].append(i)
            else:
                inverted_index[word] = [i]

    return inverted_index, positive_index


# 计算关键词的tf-idf，将文档id按score降序排列
# output: word:{idx1:score1, idx2:score2}
def compute_score(inverted_index, positive_index):
    # 总文档数
    D = len(positive_index)

    words_map = {}
    for word, idxs in inverted_index.items():
        words_map[word] = {}

        # idf: log(D/d)
        # 包含关键词的文档数
        doc_count = dict(Counter(idxs))
        d = len(doc_count)
        idf = math.log10(D / (d + 1))

        # tf: 词在文档中出现次数/文档中字词总数
        for doc in doc_count:
            word_count = doc_count[doc]
            sum_count = len(positive_index[doc])
            tf = word_count / sum_count
            score = tf * idf

            words_map[word][doc] = score

    # print(words_map)
    return words_map


def save_index(data, dbname):
    db = leveldb.LevelDB('../data/' + dbname)
    batch = leveldb.WriteBatch()
    for k, v in data.items():
        key_encode = bytes(str(k), encoding="utf-8")
        value_encode = bytes(json.dumps(v, ensure_ascii=False), encoding="utf-8")
        # print(value_encode.decode(encoding="utf-8"))
        batch.Put(key_encode, value_encode)

    db.Write(batch, sync=True)


def save_doc(words, images):
    db = leveldb.LevelDB('../data/doc')
    batch = leveldb.WriteBatch()
    for i in range(len(words)):
        key_encode = bytes(str(i), encoding="utf-8")
        value = {"content": words[i], "image": images[i]}
        value_encode = bytes(json.dumps(value, ensure_ascii=False), encoding="utf-8")
        # print(value_encode.decode(encoding="utf-8"))
        batch.Put(key_encode, value_encode)

    db.Write(batch, sync=True)


def log_config():
    logging.basicConfig(format="%(asctime)s %(levelname)s: %(message)s",
                        datefmt="%d-%m-%Y %H:%M:%S", level=logging.DEBUG)


if __name__ == '__main__':
    # config log
    log_config()

    # 读取停顿词列表
    stopwords = [k.strip() for k in open('stop_tokens.txt', encoding='utf8').readlines() if k.strip() != '']
    df = pd.read_csv("wukong_100m_62.csv", header=None, names=["url", "caption"])
    words = list(df["caption"][1:])
    images = list(df["url"][1:])
    logging.info("Data loading completed")

    chinese_list = eliminate_punc(words)
    tokenize_list = tokenize(chinese_list)
    logging.info("Data cleaning completed")

    inverted_index, positive_index = gen_index(tokenize_list, stopwords)
    inverted_index_score = compute_score(inverted_index, positive_index)
    logging.info("Index generation completed")

    save_index(inverted_index_score, "invertedIndex")
    save_index(positive_index, "positiveIndex")
    save_doc(words, images)
    logging.info("Leveldb initialization completed")
