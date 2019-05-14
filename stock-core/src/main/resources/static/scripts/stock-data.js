// 补录按钮
const maxProgress = 100; //进度100%

let labelProgressComplement = document.querySelector('#progress-complement-val');
let progressComplement = document.querySelector('#progress-complement');
let btnComplement = document.querySelector('#btn-complement');
btnComplement.addEventListener('click', onBtnComplementClick);


// 补录按钮按下
function onBtnComplementClick(e) {
    // 阻止默认提交行为
    e.preventDefault();
    complement();
}

// 补录
function complement() {
    // 提交任务
    let url = "/stock/download/complement";
    jQuery.post(url).done(function (taskId) {
        let intervalId = setInterval(function () {
            let urlTaskProgress = stringutil.stringFormat("/stock/task/progress?taskId={0}", taskId);
            jQuery.get(urlTaskProgress)
                .done(function (progress) {
                    updateComplementProgress(progress);
                    if (progress === maxProgress) {
                        clearInterval(intervalId);
                    }
                });
        }, 1000);
    });
}

// 更新补录进度条
function updateComplementProgress(progress) {
    progressComplement.value = progress;
    labelProgressComplement.textContent = progress + "%";
    if (progress === maxProgress) {
        labelProgressComplement.textContent = "完成";
    }
}