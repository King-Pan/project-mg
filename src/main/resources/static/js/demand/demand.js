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
            uploadUrl:  "fileUpload", //上传的地址
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
        $("#allotModal").modal('show');
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
                title: '需求名称'
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
                title: '创建时间'
            }, {
                field: 'updateTime',
                title: '更新时间'
            }, {
                field: 'ID',
                title: '操作',
                width: 120,
                align: 'center',
                valign: 'middle',
                formatter: actionFormatter
            }
        ]
    });

    function actionFormatter(value, row, index) {
        var id = row.userId;
        var result = "";
        // if(id==0)
        // {
        //     result += "<a href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"infoModal('" + id + "')\" title='修改'><span class='glyphicon glyphicon-edit'></span></a>";
        //     result += "<a href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"deleteUser('" + id + "')\" title='删除'><span class='glyphicon glyphicon-trash'></span></a>";
        //     //result += "<a href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"DeleteByIds('" + id + "')\" title='修改可视地市'><span class='glyphicon glyphicon-list'></span></a>";
        //     result += "<a href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"showUserRole('" + id + "')\" title='编辑用户角色'><span class='glyphicon glyphicon-user'></span></a>";
        //     result += "<a href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"showUserGroup('" + id + "')\" title='编辑用户组'><span class='glyphicon glyphicon-user'></span><span class='glyphicon glyphicon-user'></span></a>";
        // }
        // else
        // {
        //     result += "<a disabled='true' href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"infoModal('" + id + "')\" title='修改'><span class='glyphicon glyphicon-edit'></span></a>";
        //     result += "<a disabled='true' href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"deleteUser('" + id + "')\" title='删除'><span class='glyphicon glyphicon-trash'></span></a>";
        //     //result += "<a href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"DeleteByIds('" + id + "')\" title='修改可视地市'><span class='glyphicon glyphicon-list'></span></a>";
        //     result += "<a disabled='true' href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"showUserRole('" + id + "')\" title='编辑用户角色'><span class='glyphicon glyphicon-user'></span></a>";
        //     result += "<a disabled='true' href='javascript:;' class='btn btn-sm btn-info size-S' onclick=\"showUserGroup('" + id + "')\" title='编辑用户组'><span class='glyphicon glyphicon-user'></span><span class='glyphicon glyphicon-user'></span></a>";
        //
        // }
        return result;
    }

    function queryParams() {

    }
}