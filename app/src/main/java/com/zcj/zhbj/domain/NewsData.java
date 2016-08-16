package com.zcj.zhbj.domain;

import java.util.ArrayList;

/**
 * 网络分类信息的封装类
 * 字段名字必须和服务器返回的一样，方便son解析
 */
public class NewsData {

    public int retcode;//返回码
    public ArrayList<NewsMenuData> data;

    //侧边栏数据
    public class NewsMenuData{
        public String id;
        public String title;
        public int type;
        public String url;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    //新闻页面下的11个字页签的数据
    public class NewsTabData{
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "retcode=" + retcode +
                ", data=" + data +
                '}';
    }
}
