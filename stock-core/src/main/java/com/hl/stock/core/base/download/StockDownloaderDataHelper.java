package com.hl.stock.core.base.download;

import com.google.gson.JsonSyntaxException;
import com.hl.stock.core.base.download.model.SouhuHistoryHq;
import com.hl.stock.core.base.download.model.SouhuHistoryHqHq;
import com.hl.stock.core.base.download.model.SouhuHistoryHqStatus;
import com.hl.stock.core.base.download.model.SouhuHistoryHqs;
import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockZone;
import com.hl.stock.core.common.util.DateTimeUtils;
import com.hl.stock.core.common.util.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 下载器辅助类
 * 隐藏下载器内部实现
 * 因为是非公开类，因此不支持spring自动注入, 必须使用instance()获取单例
 */
class StockDownloaderDataHelper {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StockDownloaderDataHelper.class);

    private static final StockDownloaderDataHelper instance = new StockDownloaderDataHelper();

    /**
     * 请求url的格式
     * e.g. http://q.stock.sohu.com/hisHq?code=zs_000001&start=20000504&end=20151215&stat=1&order=D&period=d
     */
    private static final String stockDataUrlTemplate = "http://q.stock.sohu.com/hisHq?code={0}&start={1}&end={2}&stat=1&order=D&period=d";

    private static final DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.yyyyMMdd);

    private OkHttpClient okHttpClient = new OkHttpClient();

    static StockDownloaderDataHelper getInstance() {
        return instance;
    }

    /**
     * 通过交易所构造url中的代码字符串
     * 上海: zh
     * 深圳: zs
     */
    private static String buildStockFullCode(StockZone zone, String code) {
        String prefix = "cn_"; //搜狐新接口沪深股市一律以cn为前缀
        return prefix + code;
    }

    private static String buildStockDataUrl(StockZone zone, String code, Date start, Date end) {
        String startStr = dateFormat.format(start);
        String endStr = dateFormat.format(end);
        return MessageFormat.format(stockDataUrlTemplate, buildStockFullCode(zone, code), startStr, endStr);
    }

    private static StockData convertSouhuHq(@NonNull String code, @NonNull SouhuHistoryHqHq hq) {
        StockData data = new StockData();
        data.setCode(code);

        data.setDate(hq.getDate());
        data.setOpenPrice(hq.getOpen());
        data.setClosePrice(hq.getClose());
        data.setGrowPrice(hq.getGrow());
        data.setGrowPercent(hq.getGrowPercent());
        data.setLowPrice(hq.getLow());
        data.setHighPrice(hq.getHigh());
        data.setAmount(hq.getAmount());
        data.setAmountMoney(hq.getAmountMoney());
        data.setExchangePercent(hq.getExchange());
        return data;
    }

    /**
     * 如果超时，重试。
     * 总共重试3次。间隔5,10,15秒
     *
     * @param request 请求
     * @return
     */
    private String tryHttpCall(Request request) throws InterruptedException {
        final int sleepTimeBaseSeconds = 5; // 首次重试等待时间
        final int sleepTimeStepSeconds = 5; //每次重试等待时间的增量，重试等待时间越来越久
        final int nRetry = 3; // 重试次数
        final int MILLISECOND = 1000; // s -> ms

        for (int i = 0; i < nRetry; i++) {
            try {
                Response resp = okHttpClient.newCall(request).execute();
                if (resp == null) {
                    return null;
                }
                String rstJson = resp.body().string();
                return rstJson;
            } catch (SocketTimeoutException e) {
                // 请求超时，重试
                StockErrorCode.DownloadStockDataTimeout.warn(e);
                int sleepTimeSeconds = sleepTimeBaseSeconds + i * sleepTimeStepSeconds;
                logger.warn("request Timeout. waiting {} seconds to retry...", sleepTimeSeconds);
                Thread.sleep(sleepTimeSeconds * MILLISECOND);
                logger.warn("retry now. {} {}", request.method(), request.url());
            } catch (IOException e) {
                logger.error("io exception. {} {}", request.method(), request.url());
                StockErrorCode.DownloadStockDataFail.error(e);
            }
        }
        StockErrorCode.DownloadStockDataTimeout.error();
        return null;
    }


    /**
     * 下载搜狐股票的历史数据
     *
     * @return 历史股票数据
     */
    List<StockData> downSouhuHistoryStockData(StockZone zone, String code, Date start, Date end) {
        final List<StockData> emptyData = new ArrayList<>();

        String url = buildStockDataUrl(zone, code, start, end);
        final Request request = new Request.Builder().url(url)
                .get().build();

        String rstJson = null;
        try {
            rstJson = tryHttpCall(request);

            if (null == rstJson || "".equals(rstJson) || rstJson.startsWith("{}")) {
                // 无数据的情况，直接返回。
                logger.warn("response stock data is empty, ignore. requestUrl: {} , reply: {}", url, rstJson);
                return emptyData;
            }

            // 成功获取到数据，返回值为数组
            SouhuHistoryHqs hqs = JsonUtils.fromJson(rstJson, SouhuHistoryHqs.class);
            if (hqs == null || hqs.isEmpty()) {
                // 无数据的情况，直接返回。
                logger.warn("response stock data is empty, ignore. requestUrl: {} , reply: {}", url, rstJson);
                return emptyData;
            }

            // 取第一条数据为有效数据
            SouhuHistoryHq hq = hqs.get(0);

            // 获取数据失败，返回值为错误码
            if (hq.getStatus() == SouhuHistoryHqStatus.NonExistent.getCode()) {
                // 如果股票不存在，忽略
                logger.warn("down stock code not exists. requestUrl: {} , reply: {}", url, rstJson);
                StockErrorCode.DownloadStockCodeNotExists.warn();
                return emptyData;
            } else if (hq.getStatus() != SouhuHistoryHqStatus.OK.getCode()) {
                // 其他错误，异常
                logger.error("get stock history data fail. requestUrl: {} , reply: {}", url, rstJson);
                StockErrorCode.DownloadStockDataFail.error();
            }

            List<SouhuHistoryHqHq> hqEveryDay = hq.getHq();
            List<StockData> stockDatas = new ArrayList<>(hqEveryDay.size());
            for (SouhuHistoryHqHq hqhq : hqEveryDay) {
                StockData stockData = convertSouhuHq(code, hqhq);
                stockDatas.add(stockData);
            }
            return stockDatas;
        } catch (JsonSyntaxException e) {
            // 服务端返回失败，当传入的参数非法，例如code不存在时，就会返回不是一个数组{"status":2,"msg":"stock code non-existent"}
            // 获取数据失败，返回值为错误码
            int errStatus = -1;
            try {
                // 如果错误码为2-stock code不存在，则忽略该错误
                SouhuHistoryHq errData = JsonUtils.fromJson(rstJson, SouhuHistoryHq.class);
                errStatus = errData.getStatus();
                if (errStatus == SouhuHistoryHqStatus.NonExistent.getCode()) {
                    // 如果股票不存在，忽略
                    logger.warn("down stock code not exists. requestUrl: {} , reply: {}", url, rstJson);
                    StockErrorCode.DownloadStockCodeNotExists.warn();
                } else {
                    // 其他错误，终止
                    logger.error("get stock history data fail. requestUrl: {} , reply: {}", url, rstJson);
                    StockErrorCode.DownloadStockDataFail.error(e);
                }
            } catch (Exception e1) {
                // do nothing
                // 其他错误，终止
                logger.error("get stock history data fail. requestUrl: {} , reply: {}", url, rstJson);
                StockErrorCode.DownloadStockDataFail.error(e1);
            }
        } catch (InterruptedException e) {
            logger.error("download stock data interrupt. requestUrl: {} , reply: {}", url, rstJson);
            StockErrorCode.DownloadStockDataFail.error(e);
        }

        return emptyData;
    }
}

