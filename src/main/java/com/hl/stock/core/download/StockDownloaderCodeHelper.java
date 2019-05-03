package com.hl.stock.core.download;

import com.hl.stock.common.exception.AppException;
import com.hl.stock.common.util.FileUtils;
import com.hl.stock.core.exception.StockErrorCode;
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

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(StockDownloaderCodeHelper.class);

    /** 上海交易所A股编码 */
    private final static String ShanghaiCodeFile = FileUtils.getResourceFilePath("/code/stockcode_shanghai.txt");

    /** 深圳交易所A股编码 */
    private final static String ShenzhenCodeFile = FileUtils.getResourceFilePath("/code/stockcode_shenzhen.txt");

    private final static Pattern StockNameCodePattern = Pattern.compile("(.*)(\\((.*)\\))");

    private final static StockDownloaderCodeHelper instance = new StockDownloaderCodeHelper();

    static StockDownloaderCodeHelper getInstance() {
        return instance;
    }

    /**
     * 解析出股票编码
     * @param stockNameCode 股票名(编码) - 例如: 沙河股份(000014)
     * @return 股票编码
     */
    private String extractCode(String stockNameCode) throws AppException {
        Matcher matcher = StockNameCodePattern.matcher(stockNameCode);
        String code = null;
        if (matcher.find()) {
            String name = matcher.group(1);
            code = matcher.group(3);
        } else {
            StockErrorCode.ExtractStockCodeFail.error();
        }

        return code;
    }

    private List<String> loadShanghaiCodes() {
        return loadCodes(ShanghaiCodeFile);
    }

    private List<String> loadShenzhenCodes() {
        return loadCodes(ShenzhenCodeFile);
    }

    private List<String> loadCodes(String filename) {
        List<String> codes = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String stockNameCode = reader.readLine();
            try {
                String code = extractCode(stockNameCode);
                codes.add(code);
            } catch (AppException e) {
                // 解析失败, 忽略
                logger.warn("extract stock code fail,ignore.");
            }
        } catch (IOException e) {
            StockErrorCode.ExtractStockCodeFail.error();
        }

        return codes;
    }

    /**
     * 加载沪深股市代码
     *
     * @return 所有沪深股市代码
     */
    List<String> loadAllCodes() {
        List<String> codes = new ArrayList<>();
        codes.addAll(loadShanghaiCodes());
        codes.addAll(loadShenzhenCodes());
        return codes;
    }
}

