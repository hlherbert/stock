package com.hl.stock.core.download;

import com.hl.stock.common.exception.AppException;
import com.hl.stock.core.model.StockData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockDownloaderTest {

    @Autowired
    private StockDownloader stockDownloader;

    @Test
    public void download() throws AppException {
        String code = "sz_10001";
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(2018,1,1);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(2019,1,1);

        Date startDate = calendarStart.getTime();
        Date endDate = calendarEnd.getTime();

        StockData[] stockDatum = stockDownloader.download(code, startDate, endDate);
        
    }

    @Test
    public void downCodeList() throws AppException {
        String[] codeList = stockDownloader.downCodeList();
    }
}