// js程序入口
import jQuery from "jquery";
import {StockHistory} from './stock-history';
import {StockData} from './stock-data';


var main = function() {
    let stockHistory = new StockHistory();
    let stockData = new StockData();
    
    jQuery(document).ready(function () {
        stockHistory.init(document);
        stockData.init(document);
    });
};

main();