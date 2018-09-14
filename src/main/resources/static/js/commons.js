var formatterUtils = {};
formatterUtils.getDayTime = function (value, row, index) {
    var time;
    if (value) {
        var t = new Date(value);
        var y = t.getFullYear();    // 年
        var m = t.getMonth() + 1;   // 月
        var d = t.getDate();        // 日

        time = [y, m, d].map(formatterUtils.formatNumber).join('-');
    }
    return time;
};
formatterUtils.formatNumber = function (n) {
    n = n.toString();
    return n[1] ? n : '0' + n;
}
formatterUtils.getFullTime = function (value, row, index) {
    var time;
    if (value) {
        var t = new Date(value);
        var y = t.getFullYear();    // 年
        var m = t.getMonth() + 1;   // 月
        var d = t.getDate();        // 日

        var h = t.getHours();       // 时
        var i = t.getMinutes();     // 分
        var s = t.getSeconds();     // 秒
        time = [y, m, d].map(formatterUtils.formatNumber).join('-') + ' ' + [h, i, s].map(formatterUtils.formatNumber).join(':');
    }
    return time;
};

formatterUtils.title = function (value, row, index) {
    return '<span data-toggle="tooltip" data-placement="left" title="' + value + '">' + value + '</span>'
};