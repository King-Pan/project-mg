$(function () {
    initTable();
    $("#search_start_date").datetimepicker({
        format: "yyyy-mm-dd",
        minView: 2,
        language: 'zh-CN',
        autoclose: true,
        todayBtn: 1,
        todayHighlight: 1,
        clearBtn: true
    });
    $("#search_end_date").datetimepicker({
        format: "yyyy-mm-dd",
        minView: 2,
        language: 'zh-CN',
        autoclose: true,
        todayBtn: 1,
        todayHighlight: 1,
        clearBtn: true
    });

    //删除分配记录
    $("#btn_delete").click(function () {
        var recards = $('#allotInfoTable').bootstrapTable('getSelections');
        if (recards.length > 0) {
            var ids = new Array();
            recards.forEach(function (item) {
                ids.push(item.id);
            });
            layer.confirm('是否确定要删除该数据？', {
                btn: ['是', '否'] //按钮
            }, function () {
                $.ajax({
                    url: bashPath + "allot/",
                    type: "post",
                    dataType: "json",
                    data: {
                        _method: 'DELETE',
                        ids: ids
                    },
                    success: function (data) {
                        layer.alert(data.msg, {icon: 6});
                        if (data.status === 200) {
                            $("#allotInfoTable").bootstrapTable('refresh');
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
});

function initTable() {
    $('#allotInfoTable').bootstrapTable({
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
                field: 'userName',
                title: '员工姓名'
            }
            , {
                field: 'date',
                title: '打卡日期',
                formatter: formatterUtils.getDayTime
            },  {
                field: 'demandCode',
                title: '需求编号'
            },{
                field: 'demandName',
                title: '需求名称'
            }, {
                field: 'hour',
                title: '分配时长'
            }
        ]
    });
}

function doQuery() {
    $("#allotInfoTable").bootstrapTable('refresh');
}

function queryParams(params) {
    var param = {
        userName: $("#search_userName").val() || '',
        date: $("#search_start_date").val() || '',
        endDate: $("#search_end_date").val() || '',
        demandCode: $("#search_code").val()||'',
        demandName: $("#search_name").val() || '',
        size: params.limit,
        page: params.offset / params.limit
    };
    return param;
}

function download() {

    var url = $("#baseUrlA").attr("href") + "allot/download?1=1&";
    var name = $("#search_name").val() || '';
    if (name) {
        url += "name=" + name + "&";
    }
    var userName = $("#search_userName").val() || ''
    if (userName) {
        url += "userName=" + userName + "&";
    }
    var date = $("#search_start_date").val() || '';
    if (date) {
        url += "date=" + date + "&";
    }

    var endDate = $("#search_end_date").val() || '';
    if (endDate) {
        url += "endDate=" + endDate + "&";
    }
    url += "2=2";
    window.open(url);
}