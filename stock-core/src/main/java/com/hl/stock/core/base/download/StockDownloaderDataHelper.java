package com.hl.stock.core.base.download;

import com.hl.stock.core.base.download.model.SouhuHistoryHq;
import com.hl.stock.core.base.download.model.SouhuHistoryHqHq;
import com.hl.stock.core.base.download.model.SouhuHistoryHqs;
import com.hl.stock.core.base.exception.StockErrorCode;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.common.util.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 下载器辅助类
 * 隐藏下载器内部实现
 */
class StockDownloaderDataHelper {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StockDownloaderDataHelper.class);

    private static final StockDownloaderDataHelper instance = new StockDownloaderDataHelper();

    // http://q.stock.sohu.com/hisHq?code=zs_000001&start=20000504&end=20151215&stat=1&order=D&period=d
    private static final String stockDataUrlTemplate = "http://q.stock.sohu.com/hisHq?code={0}&start={1}&end={2}&stat=1&order=D&period=d";

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private OkHttpClient okHttpClient = new OkHttpClient();

    static StockDownloaderDataHelper getInstance() {
        return instance;
    }


    private String buildStockDataUrl(String code, Date start, Date end) {
        String startStr = dateFormat.format(start);
        String endStr = dateFormat.format(end);
        return MessageFormat.format(stockDataUrlTemplate, code, startStr, endStr);
    }

    private StockData convertSouhuHq(@NonNull String code, @NonNull SouhuHistoryHqHq hq) {
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
     * 下载搜狐股票的历史数据
     *
     * @return 历史股票数据
     */
    List<StockData> downSouhuHistoryStockData(String code, Date start, Date end) {
        final List<StockData> emptyData = new ArrayList<>();

        String url = buildStockDataUrl(code, start, end);
        final Request request = new Request.Builder().url(url)
                .get().build();

        try {
            Response resp = okHttpClient.newCall(request).execute();
            if (resp == null) {
                return emptyData;
            }
            String rstJson = resp.body().string();
            SouhuHistoryHqs hqs = JsonUtils.fromJson(rstJson, SouhuHistoryHqs.class);
            if (hqs == null || hqs.size() <= 0) {
                return emptyData;
            }
            SouhuHistoryHq hq = hqs.get(0);
            List<SouhuHistoryHqHq> hqEveryDay = hq.getHq();
            List<StockData> stockDatum = hqEveryDay.stream().map(hqhq -> convertSouhuHq(code, hqhq)).collect(Collectors.toList());
            return stockDatum;

        } catch (IOException e) {
            StockErrorCode.DownloadStockDataFail.error();
        }

        return emptyData;
    }
}

