// js程序入口
import jQuery from "jquery";
import {StockHistory} from './stock-history';
import {StockData} from './stock-data';
import {StockAnalysis} from "./stock-analysis";


var main = function() {
    let stockHistory = new StockHistory();
    let stockData = new StockData();
    let stockAnalysis = new StockAnalysis();
    
    jQuery(document).ready(function () {
        stockHistory.init(document);
        stockData.init(document);
        stockAnalysis.init(document);
    });
};

main();