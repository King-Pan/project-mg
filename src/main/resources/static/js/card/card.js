$(function () {
    $("#search_date").datetimepicker({
        format: "yyyy-mm-dd",
        minView: 2,
        language: 'zh-CN',
        autoclose: true,
        todayBtn: 1,
        todayHighlight: 1,
        clearBtn: true
    });


    $("#date").datetimepicker({
        format: "yyyy-mm-dd",
        minView: 2,
        language: 'zh-CN',
        autoclose: true,
        todayBtn: 1,
        todayHighlight: 1,
        clearBtn: true
    });


    initTable();
    initEvent();

});

/**
 * 初始化绑定事件
 */
function initEvent() {
    //导入按钮
    $("#importCardInfo").click(function () {
        $("#importModal").modal('show');
        $("#cardInfoExcelFile").fileinput({
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
        validate();
    });
    $("#btnAllot").click(function () {
        $("#allotModal").modal('show');
    });

    $("#btnModify").click(function () {
        var recards = $('#cardInfoTable').bootstrapTable('getSelections');
        if (recards.length === 1) {
            initModalData(recards[0]);
            $("#infoModal").modal('show');
            validate();
        } else {
            layer.msg('请选择一条需求进行修改');
            return;
        }

    });

    //删除打卡记录
    $("#btn_delete").click(function () {
        var recards = $('#cardInfoTable').bootstrapTable('getSelections');
        if (recards.length > 0) {
            var ids = new Array();
            recards.forEach(function (item) {
                ids.push(item.id);
            });
            layer.confirm('是否确定要删除该数据？', {
                btn: ['是', '否'] //按钮
            }, function () {
                $.ajax({
                    url: bashPath + "card/",
                    type: "post",
                    dataType: "json",
                    data: {
                        _method: 'DELETE',
                        ids: ids
                    },
                    success: function (data) {
                        layer.alert(data.msg, {icon: 6});
                        if (data.status === 200) {
                            $("#cardInfoTable").bootstrapTable('refresh');
                        }
                    },
                    error: function () {
                        layer.alert('批量删除数据失败', {icon: 6});
                    }
                })
            }, function () {

            });
        } else {
            layer.msg('请选择一条需求进行修改');
            return;
        }
    });
}

function initModalData(card) {
    $("#infoForm")[0].reset();
    console.log(card);
    $("#infoForm").setForm(card);
}

function infoSave() {
    var jsonData = $("#infoForm").serializeJson();
    $("#infoForm").bootstrapValidator('validate');//提交验证
    if ($("#infoForm").data('bootstrapValidator').isValid()) {
        $.ajax({
            type: 'post',
            url: bashPath + "card/save",
            data: jsonData,
            success: function (data) {
                if (data && data.status === 200) {
                    $("#infoModal").modal('hide');
                    doQuery();
                }
                layer.msg(data.msg);
            },
            error: function (data) {
                layer.msg('请求失败: \n' + data);
            }

        })
        return;
    }
}

function doQuery() {
    $('#cardInfoTable').bootstrapTable('refresh');
}

//验证表单输入
function validate() {
    $('#infoForm').bootstrapValidator({
        live: 'enabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
        excluded: [':disabled', ':hidden', ':not(:visible)'],//排除无需验证的控件，比如被禁用的或者被隐藏的
        //submitButtons: '#btn_sub1',//指定提交按钮，如果验证失败则变成disabled，但我没试成功，反而加了这句话非submit按钮也会提交到action指定页面
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            userName: {
                message: '员工姓名不能为空',
                validators: {
                    notEmpty: {
                        message: '员工姓名不能为空'
                    }
                }
            },
            date: {
                trigger: 'change',
                validators: {
                    notEmpty: {
                        message: '打卡日期不能为空'
                    }
                }
            },
            hours: {
                validators: {
                    notEmpty: {
                        message: '打卡工时(时)不能为空'
                    },
                    regexp: {
                        regexp: /^\d+(\.\d{1,2})?$/,
                        message: '请输入正确的正数'
                    }
                }
            },
            surHours: {
                validators: {
                    notEmpty: {
                        message: '剩报工工时不能为空'
                    },
                    regexp: {
                        regexp: /^\d+(\.\d{1,2})?$/,
                        message: '请输入正确的正数'
                    }
                }
            }
        }
    });
}

function initTable() {
    $('#cardInfoTable').bootstrapTable({
        url: 'list',         //请求后台的URL（*）
        method: 'get',                     //请求方式（*）
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
                title: '打卡日期',
                formatter: formatterUtils.getDayTime
            }, {
                field: 'hours',
                title: '打卡工时(时)'
            }, {
                field: 'surHours',
                title: '剩报未报工时'
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

function queryParams(params) {
    var param = {
        surHours: $("*[name='surHours']:checked").val(),
        userName: $("#search_userName").val() || '',
        date: $("#search_date").val() || '',
        size: params.limit,
        page: params.offset / params.limit
    };
    return param;
}

function queryCard() {
    $("#cardInfoTable").bootstrapTable('refresh');
}

/**
 * 导出excel
 */
function exportAllotInfo() {

}