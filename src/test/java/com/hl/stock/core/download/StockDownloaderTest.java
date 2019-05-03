package com.hl.stock.core.download;

import com.hl.stock.common.exception.AppException;
import com.hl.stock.common.util.JsonUtils;
import com.hl.stock.core.model.StockData;
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
        String code = "zs_000001";
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(2000, 1, 1, 0, 0, 0);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(2019, 1, 1, 0, 0, 0);

        Date startDate = calendarStart.getTime();
        Date endDate = calendarEnd.getTime();

        List<StockData> stockDatum = stockDownloader.downloadHistory(code, startDate, endDate);
        String str = JsonUtils.toPrettyJson(stockDatum);
        //System.out.println(str);
        try (FileWriter writer = new FileWriter("out/testStockData.json")) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downCodeList() throws AppException {
        List<String> codeList = stockDownloader.downCodeList();
        System.out.println(codeList);
    }
}