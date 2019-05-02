package com.hl.stock.core.taobao;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.google.gson.FieldNamingPolicy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hl.stock.core.conf.InputSeedData;
import com.hl.stock.core.taobao.dao.TaobaoItemFileDao;
import com.hl.stock.core.taobao.model.TaobaoItem;
import com.hl.stock.core.taobao.model.TaobaoSku;
import com.hl.stock.core.taobao.model.TaobaoSkuMap;
import com.hl.stock.core.taobao.model.TaobaoSpec;
import com.hl.stock.common.util.JsonUtils;
import com.hl.stock.common.util.URLParser;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * taobao商品抓取
 * https://item.taobao.com/item.htm?id=574378898908
 * https://www.taobao.com/list/item-amp/574378898908.htm
 * 标题、价格、淘宝价、尺寸、分类、库存、宝贝详情、宝贝描述
 *
 * @author hu
 */
public class TaobaoCrawler extends BreadthCrawler {
    private static final Logger logger = LoggerFactory.getLogger(TaobaoCrawler.class);
    private static final String OUTPUT_DIR = "out";
    private static final String ITEM_PAGE="ITEM";//商品首页
    private static final String DETAIL_PAGE="DETAIL";//详情页
    private static final String AMP_PAGE="AMP";//商品lite,可以看到优惠价

    private TaobaoItemFileDao dao;
    private Map<String, TaobaoItem> items = new ConcurrentHashMap<>();
    private InputSeedData seedData = InputSeedData.getInstance();

    // javascript engine
    //private ScriptEngine jsEngine;

    /**
     * 加载种子url
     */
    private void loadSeeds(List<String> seeds) {
        this.addSeed(seeds, ITEM_PAGE);
    }

    /**
     * @param crawlPath 爬虫唯一名
     * @param autoParse 是否自动加入下一轮的URL
     */
    public TaobaoCrawler(String crawlPath, boolean autoParse, TaobaoItemFileDao dao) {
        super(crawlPath, autoParse);

        this.dao = dao;
        /*start page*/
        //this.addSeed("https://item.taobao.com/item.htm?id=566374531994", ITEM_PAGE);
        loadSeeds(seedData.getSeeds());

        /*fetch url like http://news.hfut.edu.cn/show-xxxxxxhtml*/
        //this.addRegex("https://www.taobao.com/list/item-amp/.*htm");
        /*do not fetch jpg|png|gif*/
        //this.addRegex("-.*\\.(jpg|png|gif).*");
        /*do not fetch url contains #*/
        //this.addRegex("-.*#.*");

        //ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        //jsEngine = engine;

    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String url = page.url();

        /*if page is item page*/
        try {
            if (page.matchType(ITEM_PAGE)) {
                visitItemPage(page, next, url);
            } else if (page.matchType(AMP_PAGE)) {
                visitAmpPage(page);
            } else if (page.matchType(DETAIL_PAGE)) {
                visitDetailPage(page);
            }
        } catch (Exception e) {
            logger.error("visit page error.", e);
        }
    }

    private void visitDetailPage(Page page) {
        // 详情页
        String id = page.meta("id");
        TaobaoItem item = items.get(id);
        if (item == null) {
            return;
        }

        // 详情的html内容
        String desc = page.html(); // var desc='.....'
        int start = desc.indexOf("'");
        int end = desc.lastIndexOf("'");
        desc = desc.substring(start + 1, end);
        item.setDetail(desc);

        //　解析里面的img src=的内容
        Document doc = Jsoup.parse(desc);
        Elements imgEles = doc.select("img");
        List<String> imgSrcs = imgEles.eachAttr("src");
        //移除不是以jpg结尾的图片,否则格式有?等，会导致上传失败
        imgSrcs.removeIf(s -> !s.endsWith(".jpg"));
        for (int i = 0; i < imgSrcs.size(); i++) {
            imgSrcs.set(i, processImgUrl(imgSrcs.get(i)));
        }
        if (imgSrcs == null || imgSrcs.isEmpty()) {
            logger.error("\n============= detail imgs is null. =================\n");
        }
        item.setDetailImgs(imgSrcs);
    }

    private void visitAmpPage(Page page) {
        // lite page, 包含优惠价
        //String content = page.html();//page.select("div#artibody", 0).text();
        String pricePromote = page.selectText("div.price-contianer>div.price");
        pricePromote = pricePromote.replace("¥", "");

        String id = page.meta("id");
        TaobaoItem item = items.get(id);
        if (item == null) {
            return;
        }
        logger.debug("price promote:\n" + pricePromote);

        //TODO: 价格可能是 4130 - 8133的形式，取第一个值即低值
        Double dPricePromote = parsePrice(pricePromote);
        item.setPricePromote(dPricePromote);
    }

    private void visitItemPage(Page page, CrawlDatums next, String url) {
        String id = URLParser.fromURL(url).getParameter("id");
        //String id = page.url().replace("https://item.taobao.com/item.htm?id=","");
        logger.debug("visit item id=" + id);

        // 宝贝一般信息
        String title = page.selectText("div#J_Title>h3");
        Long stock = page.selectLong("span#J_SpanStock");

        //TODO: 价格可能是 4130 - 8133的形式，先暂不处理
        String strPrice = page.selectText("strong#J_StrPrice>em.tb-rmb-num");
        Double price = parsePrice(strPrice);

        Elements basicInfoEles = page.select("div#attributes>ul>li");

        // 宝贝详情
        // 是通过javascript加载的，不能直接从html获取到，因此需要先得到详情URL，然后发请求再获取
        String descUrlJson = page.regex(".*\\bdescUrl\\s*:.*,");
        int startDescHttpUrl = descUrlJson.lastIndexOf("?");
        int endDescHttpsUrl = descUrlJson.lastIndexOf(":");
        String descHttpUri = descUrlJson.substring(startDescHttpUrl, endDescHttpsUrl);
        startDescHttpUrl = descHttpUri.indexOf("'");
        endDescHttpsUrl = descHttpUri.lastIndexOf("'");
        descHttpUri = descHttpUri.substring(startDescHttpUrl + 1, endDescHttpsUrl);
        descHttpUri = "http:" + descHttpUri;
        String detail = descHttpUri;

        // 组装宝贝对象
        TaobaoItem taobaoItem = new TaobaoItem();
        taobaoItem.setId(id);
        taobaoItem.setTaobaoUrl(url);
        taobaoItem.setTitle(title);
        taobaoItem.setPrice(price);
        //taobaoItem.setPricePromote();
        taobaoItem.setStock(stock);

        Map<String, String> basicInfoMap = new HashMap<>();
        // 基本信息
        StringBuffer buf = new StringBuffer();
        for (String basicInfo : basicInfoEles.eachText()) {
            String[] keyValue = basicInfo.split(":", 2);
            if (keyValue != null) {
                buf.append(basicInfo + "\n");
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                basicInfoMap.put(key, value);
            }
        }
        taobaoItem.setBasicInfo(buf.toString());
        taobaoItem.setBasicInfoMap(basicInfoMap);
        taobaoItem.setDetail(detail);
        items.put(id, taobaoItem);

        // 爬取滚动播放图片
        Elements picEles = page.select("div.tb-pic>a>img");
        List<String> pics = picEles.eachAttr("data-src");
        pics.removeIf(s -> !s.endsWith(".jpg"));
        for (int i = 0; i < pics.size(); i++) {
            // 读取的是缩略图路径，去掉最后的_50x50.jpg就是原图
            String pic = pics.get(i).replace("_50x50.jpg", "");
            pic = processImgUrl(pic);
            pics.set(i, pic);
        }
        taobaoItem.setPics(pics);

        // 爬取规格
        Elements specEles = page.select("#J_isku ul.J_TSaleProp");
        List<TaobaoSpec> taobaoSpecs = new ArrayList<>();
        TaobaoSpec mainSpec = null;
        for (Element ele : specEles) {
            String specName = ele.attr("data-property");
            TaobaoSpec tbSpec = new TaobaoSpec();
            tbSpec.setName(specName);

            //有图片的就是主规格
            boolean isMainSpec = false;
            if (ele.hasClass("tb-img")) {
                isMainSpec = true;
                mainSpec = tbSpec;
            }

            Elements childSpecEles = ele.select("li");
            for (Element childEle : childSpecEles) {
                TaobaoSpec childSpec = new TaobaoSpec();
                String childSpecId = childEle.attr("data-value");
                String childName = childEle.selectFirst("span").text();
                childSpec.setId(childSpecId);
                childSpec.setName(childName);

                String style = childEle.selectFirst("a").attr("style");
                if (style != null && !style.isEmpty()) {
                    int start = style.indexOf("url(//") + "url(//".length();
                    int end = style.indexOf("_30x30.jpg)");
                    String specPic = style.substring(start, end);
                    String img = processImgUrl(specPic);
                    childSpec.setImg(img);
                }
                tbSpec.getChildSpecs().add(childSpec);
            }
            taobaoSpecs.add(tbSpec);
        }
        taobaoItem.setMainSpec(mainSpec);
        taobaoItem.setSpecs(taobaoSpecs);

        // 爬sku信息
        try {
            String skuJson = page.regex("skuMap\\s*:.*");
            int start = skuJson.indexOf("{");
            int end = skuJson.lastIndexOf("}");
            skuJson = skuJson.substring(start,end+1);
            try {
                TaobaoSkuMap skuMap = JsonUtils.fromJson(skuJson, TaobaoSkuMap.class, FieldNamingPolicy.IDENTITY);
                for (TaobaoSkuMap.Entry<String, TaobaoSku> entry : skuMap.entrySet()) {
                    entry.getValue().setSpecIds(entry.getKey());
                }
                taobaoItem.setSkuMap(skuMap);
            } catch (Exception e) {
                logger.error("parse skuMap fail.",e);
            }
        } catch (Exception e) {
            logger.warn(MessageFormat.format("=======item {0}: skuMap not found, create default sku========", taobaoItem.getId()));
        }

        //next.add("http://xxxxxx.com");
        //next为下次迭代

        // 下次爬amp页，获取优惠价
        String ampUrl = MessageFormat.format("https://www.taobao.com/list/item-amp/{0}.html", id);
        CrawlDatum descDatum = new CrawlDatum();
        descDatum.url(ampUrl);
        descDatum.type(AMP_PAGE);
        descDatum.meta("id", id);
        next.add(descDatum);

        // 下次爬详情URL
        descDatum = new CrawlDatum();
        descDatum.url(descHttpUri);
        descDatum.type(DETAIL_PAGE);
        descDatum.meta("id", id);
        next.add(descDatum);
        // 如果开启了autoParse, 则这里不用手动添加url到next, Crawler会根据设置的规则，自动提取page中的url加入next.
        // 在此基础上，也可以手动加入url,但是必须符合之前设置的规则，否则会被过滤。
    }

    /**
     * 解析价格
     *
     * @param strPrice 价格字符串，可能是123，也可能是123 - 456
     * @return 价格（元），取价格字符串中的最低值（第一个值）
     */
    private Double parsePrice(String strPrice) {
        Double price = null;
        // a - b形式
        if (strPrice.matches(".*-.*")) {
            String[] prices = strPrice.split("-", 2);
            if (prices != null && prices.length >= 1) {
                strPrice = prices[0].trim();
            }
        }

        try {
            price = Double.valueOf(strPrice.trim());
        } catch (Exception e) {
            logger.error("parse price fail.", e);
            price = null;
        }
        return price;
    }

    /**
     * 处理图片URL
     *
     * @param imgUrl 原url,可能为a.jpg, //a.jpg, http://a.jpg
     * @return 如果不是http://a.jpg, 一律改为https://a.jpg
     */
    private String processImgUrl(String imgUrl) {
        String img;
        if (imgUrl.startsWith("https://") || imgUrl.startsWith("http://")) {
            img = imgUrl;
        } else if (imgUrl.startsWith("//")) {
            img = imgUrl.replaceFirst("//", "https://");
        } else {
            img = "https://" + imgUrl;
        }
        return img;
    }

    /**
     * 输出结果
     */
    public void outputResults() {
        dao.writeHtmlPages(this.items.values(), OUTPUT_DIR);
    }

    public List<TaobaoItem> getItemList() {
        List<TaobaoItem> taobaoItemList = new ArrayList<>();
        taobaoItemList.addAll(items.values());
        return taobaoItemList;
    }

    public void run() throws Exception {
        TaobaoItemFileDao dao = new TaobaoItemFileDao();
        TaobaoCrawler crawler = new TaobaoCrawler("taobaoCraw", false, dao);
        crawler.setThreads(10);//线程数=CPU内核数
        crawler.getConf().setExecuteInterval(1000);//下次爬取前的等待时间ms
        //crawler.getConf().setTopN(100);
        //crawler.setResumable(true);
        /*start crawl with depth of 4*/
        crawler.start(2);
        crawler.outputResults();
    }

}