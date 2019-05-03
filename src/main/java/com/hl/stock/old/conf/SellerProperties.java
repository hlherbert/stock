package com.hl.stock.old.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * 卖家属性，从配置文件seller.properties中读取
 */
public class SellerProperties {
    private static Logger logger = LoggerFactory.getLogger(SellerProperties.class);

    private String appkey;
    private String appsecret;
    private String brand;
    private String mobile;
    private String recommendRemark;
    private List<String> recommendRemarks;
    private String extra;
    private String stock;

    public static SellerProperties getInstance() {
        InputStream in = null;
        try {
            in = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + "conf" + File.separator + "seller.properties"));
        } catch (FileNotFoundException e) {
            logger.error("请在当前目录下创建并配置conf/seller.properties文件.", e);
            return null;
        }
        Properties props = new Properties();
        SellerProperties sellerProperties = new SellerProperties();
        try {
            props.load(in);
            sellerProperties.appkey = props.getProperty("appkey");
            sellerProperties.appsecret = props.getProperty("appsecret");
            sellerProperties.brand = props.getProperty("brand");
            sellerProperties.mobile = props.getProperty("mobile");

            sellerProperties.recommendRemark = props.getProperty("recommendRemark");
            String remarkStr = sellerProperties.getRecommendRemark();
            if (remarkStr == null) {
                return null;
            }
            String[] remarks = remarkStr.split("\\|");
            if (remarks == null || remarks.length == 0) {
                return null;
            }
            sellerProperties.recommendRemarks = new ArrayList<>();
            Collections.addAll(sellerProperties.recommendRemarks, remarks);

            sellerProperties.extra = props.getProperty("extra");
            sellerProperties.stock = props.getProperty("stock");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sellerProperties;
    }

    protected SellerProperties() {

    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRecommendRemark() {
        return recommendRemark;
    }

    public void setRecommendRemark(String recommendRemark) {
        this.recommendRemark = recommendRemark;
    }

    public List<String> getRecommendRemarks() {
        return recommendRemarks;
    }

    public void setRecommendRemarks(List<String> recommendRemarks) {
        this.recommendRemarks = recommendRemarks;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
