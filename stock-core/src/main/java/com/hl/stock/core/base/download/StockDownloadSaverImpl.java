package com.hl.stock.core.base.download;

import com.hl.stock.core.base.data.StockDao;
import com.hl.stock.core.base.model.StockData;
import com.hl.stock.core.base.model.StockMeta;
import com.hl.stock.core.base.model.StockZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StockDownloadSaverImpl implements StockDownloadSaver {

    private static final Logger logger = LoggerFactory.getLogger(StockDownloadSaverImpl.class);

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockDownloader stockDownloader;

    @Override
    public void downloadSaveHistory(StockZone zone, String code, Date startDate, Date endDate) {
        List<StockData> data = stockDownloader.downloadHistory(zone, code, startDate, endDate);
        if (data == null || data.isEmpty()) {
            // do nothing
            logger.warn("download stock data empty. code:{}", code);
            return;
        }
        stockDao.saveDataBatch(data);
    }

    @Override
    public void downloadSaveMeta() {
        List<StockMeta> metas = stockDownloader.downloadMeta();
        if (metas == null || metas.isEmpty()) {
            // do nothing
            logger.warn("download all stock meta empty.");
            return;
        }
        stockDao.saveMetaBatch(metas);
    }
}
