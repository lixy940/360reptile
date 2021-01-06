package com.lixy.service.impl;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.lixy.service.ReptileService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ReptileServiceImpl implements ReptileService {
    private static String BASE_URL = "https://www.5guanjianci.com/bidding/search?type=3";
    private static String LOGIN_URL = "https://www.5guanjianci.com/user/login/action";



    @Override
    public String climber(String condition) throws Exception {
        String result = "";
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

          //登录操作
//        Map<String, String> additionalHeaders = new HashMap<String, String>();
//        additionalHeaders.put("User-Agent",
//                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
//        additionalHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
//        additionalHeaders.put("Accept", "*/*");
//        WebRequest request = new WebRequest(new URL(LOGIN_URL), HttpMethod.POST);
//        request.setRequestParameters(new ArrayList<NameValuePair>());
//        request.getRequestParameters().add(new NameValuePair("username","china"));
//        request.getRequestParameters().add(new NameValuePair("password","feifei"));
//        request.setAdditionalHeaders(additionalHeaders);
       //执行登录得到cookie
//        webClient.getPage(request);


        List<String> keywords = new ArrayList<>();

        String[] searchArr = condition.split(",");
        for (String key : searchArr) {
            String search = URLEncoder.encode(key, "utf-8");
            //遍历关键词
            String searchUrl = BASE_URL + "&query=" + search + "&page=1";
            System.out.println("searchUrl:"+searchUrl);

            Map<String, String> additionalHeaders = new HashMap<String, String>();
            additionalHeaders.put("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
            additionalHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
            additionalHeaders.put("Accept", "*/*");
            WebRequest request = new WebRequest(new URL(searchUrl), HttpMethod.GET);
            request.setAdditionalHeaders(additionalHeaders);
            HtmlPage page = null;
            try {
                page = webClient.getPage(request);//尝试加载上面图片例子给出的网页
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                webClient.close();
            }

            webClient.waitForBackgroundJavaScript(30000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束

            //总记录数
            Document document = Jsoup.parse(page.asXml());

            Elements uElements = document.getElementsByClass("u-res-table");

            Element divE = uElements.first();

            Elements tbodyE = divE.getElementsByTag("tbody");
            Elements trEls = tbodyE.first().getElementsByTag("tr");
            int len = trEls.size();
            for (int j = 1; j < len; j++) {
                Element tr = trEls.get(j);
                Elements tdEls = tr.getElementsByTag("td");
                if (tdEls.size() == 3) {
                    keywords.add(tdEls.get(2).text());
                }

            }
            //间隔1s
            TimeUnit.MILLISECONDS.sleep(300);
        }

        if(!keywords.isEmpty())
            result = keywords.stream().collect(Collectors.joining("\r\n"));
        return result;
    }
}
