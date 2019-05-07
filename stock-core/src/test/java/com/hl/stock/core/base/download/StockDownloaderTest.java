package com.hl.stock.core.base.download;

import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
import com.hl.stock.core.common.exception.AppException;
import com.hl.stock.core.common.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockDownloaderTest {

    @Autowired
    private StockDownloader stockDownloader;

    @Test
    public void download() throws AppException {
        String code = "000001";
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(2000, 1, 1, 0, 0, 0);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(2019, 1, 1, 0, 0, 0);

        Date startDate = calendarStart.getTime();
        Date endDate = calendarEnd.getTime();

        List<StockData> stockDatas = stockDownloader.downloadHistory(StockZone.SHENZHEN, code, startDate, endDate);
        String str = JsonUtils.toPrettyJson(stockDatas);
        //System.out.println(str);
        try (FileWriter writer = new FileWriter("out/testStockData.json")) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadMeta() throws AppException {
        List<StockMeta> codeList = stockDownloader.downloadMeta();
        System.out.println(JsonUtils.toPrettyJson(codeList));
    }
}