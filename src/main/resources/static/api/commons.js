var CONSTANT = {
    // datatables常量
    DATA_TABLES: {
        DEFAULT_OPTION: { // DataTables初始化选项
            LANGUAGE: {
                sProcessing: "处理中...",
                sLengthMenu: "显示 _MENU_ 项结果",
                sZeroRecords: "没有匹配结果",
                sInfo: "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                sInfoEmpty: "显示第 0 至 0 项结果，共 0 项",
                sInfoFiltered: "(由 _MAX_ 项结果过滤)",
                sInfoPostFix: "",
                sSearch: "搜索:",
                searchPlaceholder: "关键字搜索",
                sUrl: "",
                sEmptyTable: "表中数据为空",
                sLoadingRecords: "载入中...",
                sInfoThousands: ",",
                oPaginate: {
                    sFirst: "首页",
                    sPrevious: "上页",
                    sNext: "下页",
                    sLast: "末页"
                },
                /*oAria: {
                    sSortAscending: ": 以升序排列此列",
                    sSortDescending: ": 以降序排列此列"
                }*/
            },
            pageLength: 50,
            // 禁用自动调整列宽
            lengthMenu:[[50,100,200,500],[50,100,200,500]],
            autoWidth: false,
            // 为奇偶行加上样式，兼容不支持CSS伪类的场合
            stripeClasses: ["odd", "even"],
            // 取消默认排序查询,否则复选框一列会出现小箭头
            //order: [],
            // 隐藏加载提示,自行处理
            processing: false,
            // 启用服务器端分页
            serverSide: true,
            // 禁用原生搜索
            searching: false,
            ordering: false,
            lengthChange: true
        },
        COLUMN: {
            // 复选框单元格
            CHECKBOX: {
                className: "td-checkbox",
                orderable: false,
                bSortable: false,
                data: "id",
                width: '30px',
                render: function (data, type, row, meta) {
                    var content = '<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">';
                    content += '    <input type="checkbox" class="group-checkable" value="' + data + '" />';
                    content += '    <span></span>';
                    content += '</label>';
                    return content;
                }
            }
        },
        // 回调
        CALLBACKS: {
            // 表格绘制前的回调函数
            PREDRAWCALLBACK: function (settings) {
                var nTableWrapper=$(settings.nTableWrapper);
                nTableWrapper.addClass('dataTables_audit');
                nTableWrapper.children().eq(0).css({right:0,top:'-30px',position:'absolute'});
                if (settings.oInit.scrollX == '100%') {
                    // 给表格添加css类，处理scrollX : true出现边框问题
                    $(settings.nTableWrapper).addClass('dataTables_DTS');
                }
            },
            INITCOMPLETE: function (settings, json) {
                if (settings.oInit.scrollX == '100%' && $(settings.nTable).parent().innerWidth() - $(settings.nTable).outerWidth() > 5) {
                    $(settings.nScrollHead).children().width('100%');
                    $(settings.nTHead).parent().width('100%');
                    $(settings.nTable).width('100%');
                }
            },
            // 表格每次重绘回调函数
            DRAWCALLBACK: function (settings) {
                if ($(settings.aoHeader[0][0].cell).find(':checkbox').length > 0) {
                    // 取消全选
                    $(settings.aoHeader[0][0].cell).find(':checkbox').prop('checked', false);
                }
                // 高亮显示当前行
                $(settings.nTable).find("tbody tr").click(function (e) {
                    $(e.target).parents('table').find('tr').removeClass('warning');
                    $(e.target).parents('tr').addClass('warning');
                });
            }
        },
        // 常用render可以抽取出来，如日期时间、头像等
        RENDER: {
            ELLIPSIS: function (data, type, row, meta) {
                data = data || "";
                return '<span title="' + data + '">' + data + '</span>';
            }
        }

    }

};

if ($.fn.dataTable) {
    $.extend($.fn.dataTable.defaults, {
        processing: true,
        //order: [],
        paging: true,
        searching: false,
        language: CONSTANT.DATA_TABLES.DEFAULT_OPTION.LANGUAGE,
        preDrawCallback: CONSTANT.DATA_TABLES.CALLBACKS.PREDRAWCALLBACK,
        initComplete: CONSTANT.DATA_TABLES.CALLBACKS.INITCOMPLETE,
        drawCallback: CONSTANT.DATA_TABLES.CALLBACKS.DRAWCALLBACK
    });
}

// 屏蔽数据缺失警告
$.fn.dataTable.ext.errMode = 'none';

var utils={
    //分转元
    fenConvertYuan:function (money) {
        if(null==money||''==money){
            return '';
        }
        var res=money/100.0
        return res.toFixed(2);
    },
    //元转分
    yuanConventFen:function (money) {
        if(null==money||''==money){
            return '';
        }
        return money*100;
    },
    tmpUrl:function (url) {
        var tmp="?";
        if(url.indexOf(tmp)!=-1){
            tmp="&";
        }
        return tmp;
    },
    /**
     * 金额校验
     * @param money  金额
     * @returns {boolean} 是否为正确格式的金额，正确返回true，否则返回false
     */
    matchMoney:function (money) {
        var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
        if (reg.test(money)) {
            return true;
        }else{
            return false;
        }
    },
    getNowDate:function () {
        var now = new Date();
        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
        var clock = year + "";

        if(month < 10)
            clock += "0";
        clock += month + "";
        if(day < 10)
            clock += "0";
        clock += day;
        return clock;
    },
    /**
     * 比较2个数字的大小
     * @param n1
     * @param n2
     */
    compare:function (n1,n2) {
        return n1 < n2;
    },
    /**
     * 打开新窗口
     * @param url
     * @param name
     */
    openWin:function(url,name) {
        //检查URL是否能访问
        $.ajax({
            type: 'get',
            url: url,
            cache: false,
            dataType: "jsonp", //跨域采用jsonp方式
            processData: false,
            timeout:10000, //超时时间，毫秒
            complete: function (data) {
                if (data.status==200) {
                    var iWidth=900; //弹出窗口的宽度;
                    var iHeight=600;//弹出窗口的高度;
                    //获得窗口的垂直位置
                    var iTop = (window.screen.availHeight - 30 - iHeight) / 2;
                    //获得窗口的水平位置
                    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
                    window.open(url, name, 'height=' + iHeight + ',,innerHeight=' + iHeight + ',width=' + iWidth + ',innerWidth=' + iWidth + ',top=' + iTop + ',left=' + iLeft + ',status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no,titlebar=no');
                }else{
                    window.open("http://emis.tj.cmcc");
                }
            }
        });
    },
    /**
     * 勾选初始化
     * @param parentDom
     * @param childrenDom
     * @param checkedList
     * @param callback
     */
    checkedInit:function (parentDom,childrenDom,checkedList,callback) {
        //回显选中状态
        if (null != checkedList) {
            //遍历表格
            var checkboxArr = $(childrenDom);
            //判断是否有未勾选的列
            var unChecked=false;
            $.each(checkboxArr, function (i, t) {
                checkedList.forEach(function (t2) {
                    var cid=typeof t2=='object'?t2.id:t2;
                    if (t.value == cid) {
                        //选中
                        checkboxArr.eq(i).prop("checked", true);
                        return false;
                    }
                });
                var checked=checkboxArr.eq(i).prop("checked");
                if(!checked){
                    unChecked=true;
                }
            });
            if(checkboxArr && checkboxArr.length>0){
                $(parentDom).prop("checked",unChecked ? false:true);
            }
        }else {
            //全选按钮取消勾选
            $(parentDom).prop("checked",false);
        }
        if(callback){
            callback(checkedList);
        }
    },

    /**
     * 单选
     * @param parentDom
     * @param childrenDom
     * @param checkedList
     * @param callback
     * @Deprecated
     */
    checkedSingleton:function (parentDom,childrenDom,checkedList,callback) {
        $("body").on("click",childrenDom,function() {
            var id=$(this).attr('value');
            //勾选
            var isCheck=$(this).prop('checked');
            var checkboxList=$(childrenDom);
            if(isCheck){
                //判断是否全部选中
                var unChecked=false;
                $.each(checkboxList, function (i, t) {
                    var checked=checkboxList.eq(i).prop("checked");
                    if(!checked){
                        unChecked=true;
                        return false;
                    }
                });
                if(unChecked){
                    $(parentDom).prop("checked",false);
                }else{
                    $(parentDom).prop("checked",true);
                }
            }else{
                //取消勾选
                checkedList.forEach(function (t,i) {
                    var cid=typeof t=='object'?t.id:t;
                    if(cid==id){
                        checkedList.splice(i, 1);
                        return false;
                    }
                });
                $(parentDom).prop("checked",false);
            }
            if(callback){
                var _this=$(this);
                callback(checkedList,_this,isCheck);
            }
        });
    },

    /**
     * 单选功能处理
     * @param parentDom
     * @param childrenDom
     * @param checkedList
     * @param callback
     */
    checkedSingletonHandle:function (_this,parentDom,childrenDom,checkedList,callback) {
        var id=_this.attr('value');
        //勾选
        var isCheck=_this.prop('checked');
        var checkboxList=$(childrenDom);
        if(isCheck){
            //判断是否全部选中
            var unChecked=false;
            $.each(checkboxList, function (i, t) {
                var checked=checkboxList.eq(i).prop("checked");
                if(!checked){
                    unChecked=true;
                    return false;
                }
            });
            if(unChecked){
                $(parentDom).prop("checked",false);
            }else{
                $(parentDom).prop("checked",true);
            }
        }else{
            //取消勾选
            checkedList.forEach(function (t,i) {
                var cid=typeof t=='object'?t.id:t;
                if(cid==id){
                    checkedList.splice(i, 1);
                    return false;
                }
            });
            $(parentDom).prop("checked",false);
        }
        if(callback){
            callback(checkedList,_this,isCheck);
        }
    },

    /**
     * 全选功能
     * @param parentDom
     * @param childrenDom
     * @param checkedList
     * @param callback
     * @Deprecated
     */
    checkedAll:function(parentDom,childrenDom,checkedList,callback) {
        $(parentDom).bind("click",function(){
            var isCheck=$(this).prop('checked');
            var checkboxList=$(childrenDom);
            if(!isCheck){
                //取消勾选
                for(var i=0;i<checkboxList.length;i++){
                    checkboxList.eq(i).prop('checked',false);
                    var id=checkboxList.eq(i).attr('value');
                    //删除当前全中值
                    checkedList.forEach(function (t,i) {
                        var cid=typeof t=='object'?t.id:t;
                        if(cid==id){
                            checkedList.splice(i, 1);
                            return;
                        }
                    });
                }
            }
            if(callback){
                var _this=$(this);
                callback(checkedList,_this,isCheck);
            }
        });
    },

    /**
     * 全选功能处理
     * @param _this
     * @param childrenDom
     * @param checkedList
     * @param callback
     */
    checkedAllHandle:function(_this,childrenDom,checkedList,callback) {
        var isCheck=_this.prop('checked');
        var checkboxList=$(childrenDom);
        if(!isCheck){
            //取消勾选
            for(var i=0;i<checkboxList.length;i++){
                checkboxList.eq(i).prop('checked',false);
                var id=checkboxList.eq(i).attr('value');
                //删除当前全中值
                checkedList.forEach(function (t,i) {
                    var cid=typeof t=='object'?t.id:t;
                    if(cid==id){
                        checkedList.splice(i, 1);
                        return;
                    }
                });
            }
        }
        if(callback){
            callback(checkedList,_this,isCheck);
        }
    },
    /**
     * 全选功能
     * @param parentDom
     * @param childrenDom
     * @param checkedList
     * @param callback
     */
    checkedAllFunc:function(parentDom,childrenDom) {
        $(parentDom).bind("click",function(){
            var checkboxList=$(childrenDom);
            for(var i=0;i<checkboxList.length;i++){
                checkboxList.eq(i).trigger('click');
            }
        });
    },

    /**
     * 每页多少条改变事件
     * @param dom
     * @param callback
     */
    lengthMenuChangeFun:function (dom,callback) {
        $('body').on('change',dom,function () {
            var _this=$(this);
            if(callback){
                callback(_this);
            }
        })
    }
};
$(document).ready(function() {
    $(document).ajaxError(function(event,xhr,options,exc){
        errorHandle();
    });
});

function errorHandle() {
    //跳转到登录页面
    $.post(basePath+"userInstance/userConfig",{},function (res) {
        if(!res.success){
            alert(res.message);
            return;
        }
        if(!res.data.loginUrl){
            alert("参数错误");
            return;
        }
        var loginUrl=res.data.loginUrl;
        if(''==loginUrl || null==loginUrl){
            alert("跳转地址配置错误");
            return;
        }
        window.location.href=loginUrl;
    });
}