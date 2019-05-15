// 股票统计功能

(function () {
    jQuery(document).ready(function () {
        let tableStat = document.querySelector('#table-stat');
        let selIndex = document.querySelector('#sel-index');
        selIndex.addEventListener('change', onIndexChange);
    });

    // 统计指标改变
    function onIndexChange(e) {
        // 更新股票名称
        let index = selIndex.value; //指标
        let code = inputCode.value; //当前股票编码
        updateStatTable(code, index, startdate, enddate);
    }

    // 更新统计表
    function updateStatTable(code, index, startdate, enddate) {

    }

})();