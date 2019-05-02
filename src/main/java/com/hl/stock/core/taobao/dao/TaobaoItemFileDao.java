package com.hl.stock.core.taobao.dao;

import com.hl.stock.core.taobao.model.TaobaoItem;
import com.hl.stock.core.taobao.model.TaobaoSpec;
import com.hl.stock.common.util.CharsetConstant;

import java.io.*;
import java.util.Collection;
import java.util.List;

public class TaobaoItemFileDao {
    private static final String TEMPLATE_FILENAME = "/template/taobao-item.txt";
    private OutputStreamWriter writer;

    //输出html模板的内容
    private String htmlTemplate;

    public TaobaoItemFileDao() {
        readTemplate();
    }

    public void open(String filename) throws Exception {
        writer = new OutputStreamWriter(new FileOutputStream(filename), CharsetConstant.UTF8);
    }
    public void close(){
        try {
            if (writer!=null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write(TaobaoItem item){
        try {
            writer.write("taobaoUrl: "+item.getTaobaoUrl()+"\n");
            writer.write("id: "+item.getId()+"\n");
            writer.write("标题: "+item.getTitle()+"\n");
            writer.write("价格: "+item.getPrice()+"\n");
            writer.write("优惠价: "+item.getPricePromote()+"\n");
            writer.write("库存: "+item.getStock()+"\n");
            writer.write("基本信息: "+item.getBasicInfo()+"\n");
            writer.write("详情: "+item.getDetail() +"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将淘宝商品输出为网页文件
     * @param item 商品
     * @param filename 文件名
     */
    public void writeHtmlPage(TaobaoItem item, String filename) {
        OutputStreamWriter fw = null;
        try {
            // 拼接详情图片字符串
            StringBuffer sb = new StringBuffer();
            List<String> itemDetailImgs = item.getDetailImgs();
            String detailImgs = null;
            if (itemDetailImgs != null) {
                for (String img : item.getDetailImgs()) {
                    sb.append(img + ",");
                }
                detailImgs = sb.substring(0, Math.max(0, sb.length() - 1));
            }
            if (detailImgs == null) {
                detailImgs = "";
            }

            // 拼接主规格图片字符串
            TaobaoSpec mainSpec = item.getMainSpec();
            String mainSpecPics = "";
            if (mainSpec != null) {
                List<TaobaoSpec> childSpecs = mainSpec.getChildSpecs();
                sb = new StringBuffer();
                for (TaobaoSpec childSpec : childSpecs) {
                    sb.append(childSpec.getImg() + ",");
                }
                sb.deleteCharAt(sb.length() - 1);
                mainSpecPics = sb.toString();
            }

            String temp = this.htmlTemplate;
            String html = temp.replace("###taobaoUrl###", item.getTaobaoUrl())
                    .replace("###id###", item.getId())
                    .replace("###title###", item.getTitle())
                    .replace("###price###", String.valueOf(item.getPrice()))
                    .replace("###pricePromote###", String.valueOf(item.getPricePromote()))
                    .replace("###stock###", String.valueOf(item.getStock()))
                    .replace("###basicInfo###", item.getBasicInfo())
                    .replace("###detailImgs###", detailImgs)
                    .replace("###mainSpecPics###", mainSpecPics);

            File outfile = new File(filename);
            fw = new OutputStreamWriter(new FileOutputStream(outfile), CharsetConstant.UTF8);
            fw.write(html);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将淘宝商品输出为网页文件，指定输出路径，文件名为 id.html, id为商品编号
     * @param items 商品
     * @param outDir 输出路径
     */
    public void writeHtmlPages(Collection<TaobaoItem> items, String outDir) {
        File outDirFile = new File(outDir);
        if (!outDirFile.exists()) {
            outDirFile.mkdir();
        }

        for (TaobaoItem item: items) {
            String filename = outDir + "/" + item.getId() + ".txt";
            this.writeHtmlPage(item, filename);
        }
    }

    /**
     * 读取模板内容
     */
    private void readTemplate() {
        InputStreamReader ir = null;
        BufferedReader br = null;
        StringBuffer buf = new StringBuffer();
        try {
            InputStream ins = this.getClass().getResourceAsStream(TEMPLATE_FILENAME);
            ir = new InputStreamReader(ins, CharsetConstant.UTF8);
            br = new BufferedReader(ir);

            String line = null;
            while ((line=br.readLine())!=null){
                buf.append(line+"\r\n");
            }

            this.htmlTemplate = buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
