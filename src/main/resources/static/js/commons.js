var bashPath = $("#basePath").val();
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


$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};


$.fn.serializeJson = function () {
    var serializeObj = {};
    //disabled的元素获取不到vualue，使用readonly即可.
    var array = this.serializeArray();
    var str = this.serialize();
    $(array).each(function () {
        if (serializeObj[this.name]) {
            if ($.isArray(serializeObj[this.name])) {
                serializeObj[this.name].push(this.value);
            } else {
                serializeObj[this.name] = [serializeObj[this.name], this.value];
            }
        } else {
            serializeObj[this.name] = this.value;
        }
    });
    return serializeObj;
};

/**
  * 将josn对象赋值给form
  * @param {dom} 指定的选择器
  * @param {obj} 需要给form赋值的json对象
  * @method serializeJson
  * */
$.fn.setForm = function (jsonValue) {
    var obj = this;
    $.each(jsonValue, function (name, ival) {
        var $oinput = obj.find("input[name=" + name + "]");
        if ($oinput.attr("type") == "checkbox") {
            if (ival !== null) {
                var checkboxObj = $("[name=" + name + "]");
                var checkArray = ival.split(";");
                for (var i = 0; i < checkboxObj.length; i++) {
                    for (var j = 0; j < checkArray.length; j++) {
                        if (checkboxObj[i].value == checkArray[j]) {
                            checkboxObj[i].click();
                        }
                    }
                }
            }
        }
        else if ($oinput.attr("type") == "radio") {
            $oinput.each(function () {
                var radioObj = $("[name=" + name + "]");
                for (var i = 0; i < radioObj.length; i++) {
                    if (radioObj[i].value == ival) {
                        radioObj[i].click();
                    }
                }
            });
        }
        else if ($oinput.attr("type") == "textarea") {
            obj.find("[name=" + name + "]").html(ival);
        }
        else {
            obj.find("[name=" + name + "]").val(ival);
        }
    })
}

$.fn.modal.Constructor.prototype.enforceFocus = function () {
};