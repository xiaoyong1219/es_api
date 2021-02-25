package com.xiaoyong.es_api.util;

import com.xiaoyong.es_api.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Create By dongxiaoyong on /2021/2/24
 * description: 爬取京东搜索页面信息
 *
 * @author dongxiaoyong
 */
public class HtmlParseUtil {

    /**
     * 爬取京东搜索页面信息封装成实体列表
     *
     * @param keyword
     * @Author :dongxiaoyong
     * @Date : 2021/2/25 15:21
     * @return: java.util.ArrayList<com.xiaoyong.es_api.pojo.Content>
     */

    public static ArrayList<Content> parseJdHtml(String keyword) throws IOException {
        ArrayList<Content> contentArrayList = new ArrayList<>();
        //获取请求
        String url = "https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8&wq=" + keyword + "&pvid=6c2f8c8298cf43baad017524ca06e528";
        //解析网页(Jsoup返回Document就是浏览器Document对象)
        Document document = Jsoup.parse(new URL(url), 30000);
        //所有你在js中可以使用的方法，这里都可以使用
        Element element = document.getElementById("J_goodsList");
        if (element != null) {
            //获取所有的li元素
            Elements elements = element.getElementsByTag("li");
            if (elements != null) {
                for (Element ele : elements) {
                    //关于这种图片特别多的网站，所有的图片都是延迟加载的，图片属性初始访问时候一般src值是空，京东的是先放在data-lazy-img属性上
                    String img = ele.getElementsByTag("img").eq(0).attr("data-lazy-img");
                    String price = ele.getElementsByClass("p-price").eq(0).text();
                    String title = ele.getElementsByClass("p-name").eq(0).text();
                    String shopName = ele.getElementsByClass("p-shopnum").eq(0).text();
                    Content content = new Content(title, price, img, shopName);
                    contentArrayList.add(content);
                }
            }
        }
        return contentArrayList;
    }

    public static void main(String[] args) throws IOException {
        parseJdHtml("vue").forEach(System.out::println);
    }
}
