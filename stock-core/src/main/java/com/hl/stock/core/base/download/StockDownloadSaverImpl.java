package com.hl.stock.core.base.download;

import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StockDownloadSaverImpl implements StockDownloadSaver {
    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockDownloader stockDownloader;

    @Override
    public void downloadSaveHistory(String code, Date startDate, Date endDate) {
        List<StockData> data = stockDownloader.downloadHistory(code, startDate, endDate);
        for (StockData d : data) {
            stockDao.saveData(d);
        }
    }

    @Override
    public void downloadSaveMeta() {
        List<StockMeta> metas = stockDownloader.downloadMeta();
        for (StockMeta m : metas) {
            stockDao.saveMeta(m);
        }
    }
}
