// js程序入口
import jQuery from "jquery";
import {StockHistory} from './stock-history';
import {StockData} from './stock-data';
import {StockStat} from './stock-stat';


var main = function() {
    let stockStat = new StockStat();
    let stockHistory = new StockHistory();
    let stockData = new StockData();
    
    jQuery(document).ready(function () {
        stockHistory.init(document);
        stockStat.init(document);
        stockData.init(document);
    });
};

main();