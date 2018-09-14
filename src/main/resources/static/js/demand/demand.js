//列表控件初始化
$(function () {
    initTable();
    initEvent();
});

/**
 * 初始化绑定事件
 */
function initEvent() {
    //导入按钮
    $("#importDemand").click(function () {
        $("#importModal").modal('show');
        $("#demandExcelFile").fileinput({
            language: 'zh_CN', //设置语言
            uploadUrl: "fileUpload", //上传的地址
            allowedFileExtensions: ['xls', 'xlsx'],//接收的文件后缀
            uploadAsync: true, //默认异步上传
            showUpload: true, //是否显示上传按钮
            showRemove: true, //显示移除按钮
            showPreview: false, //是否显示预览
            showCaption: true,//是否显示标题
            enctype: 'multipart/form-data',
            validateInitialCount: true,
            uploadExtraData: function (previewId, index) {   //额外参数的关键点
                var obj = {};
                return obj;
            }
        }).on("fileuploaded", function (event, data, previewId, index) {
            var status = data.response.status;
            if (status == 200) {
                layer.msg("导入成功");
                $("#importModal").modal('hide');
            } else {
                layer.msg("导入失败");
            }
        });
    });

    $("#btnAdd").click(function () {
        $("#infoModal").modal('show');
    });
    $("#btnAllot").click(function () {
        var recards = $("#demandTable").bootstrapTable('getSelections');
        if (recards.length === 1) {
            $("#allotModal").modal('show');
            initCardInfoTable();
        } else {
            layer.msg('请选择一条需求分配工时');
        }
    });

    //分配工时确定按钮
    $("#btnAllotHours").click(function () {
        //1. 验证值是否填写正确

        var records = $("#cardInfoTable").bootstrapTable('getSelections');
        if (records.length === 0) {
            layer.msg('请选择员工打开记录');
            return;
        }
        console.log(records);
        var type = $("*[name='type']:checked").val();
        var demand = $("#demandTable").bootstrapTable('getSelections')[0];
        //该需求剩余工时
        var surHours = parseFloat(demand.surHours);

        //定额分配工时数量

        var hour = $("#hour").val();
        if (type === '0') {
            var reg = new RegExp("^[0-9]+(.[0-9]{1})?$");
            if (!hour || !reg.test(hour)) {
                layer.msg('请输入正确的正实时，可以保留一位小数');
                return;
            }
            hour = parseFloat(hour);

            var flag = true;

            // 验证员工改日剩余工时是否满足条件  剩余工时>=分配工时
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (parseFloat(record.surHours) < hour) {
                    layer.msg('员工:' + record.userName + ",打开日期:" + record.date + ",剩余工时不足.请重新选择");
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                return;
            }
        }

        // 获取分配的打卡记录ID
        var sumHours = 0;
        var cardIds = new Array();
        for (var i = 0; i < records.length; i++) {
            var record = records[i];
            cardIds.push(record.id);
            sumHours += parseFloat(record.surHours);
        }

        // 需要分配的工时总量是否小于等于该需求剩余工时
        if (sumHours > surHours) {
            layer.msg('需求:[' + demand.name + "]剩余工时不足. 剩余工时[" + surHours + "],分配工时[" + sumHours + "]");
            return;
        }
        //4. 开始分配工时，生成分配记录，扣减员工剩余工时和需求剩余工时
        var obj = {
            type: type,          //分配类型: 定额分配，余量分配
            hour: hour,          // 定额分配时，分配的工时
            demandId: demand.id, //需求ID
            ids: cardIds      //打卡记录ID
        }
        console.log(obj);
        $.ajax({
            type: 'post',
            data: obj,
            url: 'allotHours',
            success: function (data) {
                console.log(data);
            },
            error: function () {

            }
        })
    });
}

function initTable() {
    $('#demandTable').bootstrapTable({
        url: 'list',         //请求后台的URL（*）
        method: 'post',                     //请求方式（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                    //是否启用排序
        sortOrder: "asc",                   //排序方式
        queryParams: queryParams,			//传递参数（*）
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 20, 50],        		//可供选择的每页的行数（*）
        search: false,                     //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        //clickToSelect: true,                //是否启用点击选中行
        showColumns: true,                  //是否显示所有的列
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        height: 520,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "userId",                     //每一行的唯一标识，一般为主键列
        idField: "userId",
        showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        //singleSelect: true,
        formatLoadingMessage: function () {
            return "数据加载中，请稍候......";
        },
        onLoadError: function (status) {
            layer.alert("查询数据失败，错误码：" + status, {icon: 5, title: '提示'});
        },
        onLoadSuccess: function (data) {
            if (data.flag == '-1') {
                layer.alert("查询数据失败，错误信息：<br>" + data.msg, {icon: 5, title: '提示'});
                return;
            }
            $('[data-toggle="tooltip"]').tooltip();
        },
        columns: [{
            align: 'center',
            checkbox: true,
        }
            , {
                field: 'id',
                title: 'ID',
                visible: false
            }
            , {
                field: 'code',
                title: '需求编码'
            }
            , {
                field: 'name',
                title: '需求名称',
                formatter: formatterUtils.title
            }
            , {
                field: 'personDay',
                title: '预估工作量(天)'
            }, {
                field: 'preHours',
                title: '预估工作量(时)'
            }, {
                field: 'alHours',
                title: '已报工工时'
            }, {
                field: 'surHours',
                title: '剩报工工时'
            }, {
                field: 'persons',
                title: '可报工人员'
            }, {
                field: 'createTime',
                title: '创建时间',
                formatter: formatterUtils.getFullTime
            }, {
                field: 'updateTime',
                title: '更新时间',
                formatter: formatterUtils.getFullTime
            }
        ]
    });
}

function queryParams() {

}

function initCardInfoTable() {
    $('#cardInfoTable').bootstrapTable({
        url: '../card/list',         //请求后台的URL（*）
        method: 'post',                     //请求方式（*）
        //toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                    //是否启用排序
        sortOrder: "asc",                   //排序方式
        queryParams: queryParams,			//传递参数（*）
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 20, 50],        		//可供选择的每页的行数（*）
        search: false,                     //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        //clickToSelect: true,                //是否启用点击选中行
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        height: 200,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "userId",                     //每一行的唯一标识，一般为主键列
        idField: "userId",
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        //singleSelect: true,
        formatLoadingMessage: function () {
            return "数据加载中，请稍候......";
        },
        onLoadError: function (status) {
            layer.alert("查询数据失败，错误码：" + status, {icon: 5, title: '提示'});
        },
        onLoadSuccess: function (data) {
            if (data.flag == '-1') {
                layer.alert("查询数据失败，错误信息：<br>" + data.msg, {icon: 5, title: '提示'});
                return;
            }
        },
        columns: [{
            align: 'center',
            checkbox: true,
        }
            , {
                field: 'id',
                title: 'ID',
                visible: false
            }
            , {
                field: 'userId',
                title: '员工ID',
                visible: false
            }
            , {
                field: 'userName',
                title: '员工姓名'
            }
            , {
                field: 'date',
                title: '打卡日期'
            }, {
                field: 'hours',
                title: '打卡工时(时)'
            }, {
                field: 'surHours',
                title: '剩报未报工时'
            }
        ]
    });
}