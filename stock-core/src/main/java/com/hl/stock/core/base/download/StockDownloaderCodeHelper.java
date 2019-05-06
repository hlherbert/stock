package com.hl.stock.core.base.download;

import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
import com.hl.stock.core.common.exception.AppException;
import com.hl.stock.core.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 下载器辅助类
 * 隐藏下载器内部实现
 */
class StockDownloaderCodeHelper {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StockDownloaderCodeHelper.class);

    /**
     * 上海交易所A股编码
     */
    private final static String ShanghaiCodeFile = FileUtils.getResourceFilePath("/code/stockcode_shanghai.txt");

    /**
     * 深圳交易所A股编码
     */
    private final static String ShenzhenCodeFile = FileUtils.getResourceFilePath("/code/stockcode_shenzhen.txt");

    private final static Pattern StockNameCodePattern = Pattern.compile("(.*)(\\((.*)\\))");

    private final static StockDownloaderCodeHelper instance = new StockDownloaderCodeHelper();

    static StockDownloaderCodeHelper getInstance() {
        return instance;
    }

    /**
     * 解析出股票元数据
     *
     * @param stockNameCode 股票名(编码) - 例如: 沙河股份(000014)
     * @return 股票元数据
     */
    private StockMeta extractMeta(String stockNameCode, StockZone zone) throws AppException {
        Matcher matcher = StockNameCodePattern.matcher(stockNameCode);
        StockMeta meta = null;
        if (matcher.find()) {
            String name = matcher.group(1);
            String code = matcher.group(3);
            meta = new StockMeta();
            meta.setCode(code);
            meta.setName(name);
            meta.setZone(zone);
        } else {
            StockErrorCode.ExtractStockMetaFail.error();
        }

        return meta;
    }

    private List<StockMeta> loadShanghaiCodes() {
        return loadCodes(ShanghaiCodeFile, StockZone.SHANGHAI);
    }

    private List<StockMeta> loadShenzhenCodes() {
        return loadCodes(ShenzhenCodeFile, StockZone.SHENZHEN);
    }

    private List<StockMeta> loadCodes(String filename, StockZone zone) {
        List<StockMeta> metas = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String stockNameCode = reader.readLine();
            try {
                StockMeta meta = extractMeta(stockNameCode, zone);
                metas.add(meta);
            } catch (AppException e) {
                // 解析失败, 忽略
                logger.warn("extract stock code fail,ignore.");
            }
        } catch (IOException e) {
            StockErrorCode.ExtractStockMetaFail.error();
        }

        return metas;
    }

    /**
     * 加载沪深股市代码
     *
     * @return 所有沪深股市代码
     */
    List<StockMeta> loadAllMeta() {
        List<StockMeta> metas = new ArrayList<>();
        metas.addAll(loadShanghaiCodes());
        metas.addAll(loadShenzhenCodes());
        return metas;
    }
}

