$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'common/appLog/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '客户端IP', name: 'clientIp', width: 90 },
            { label: '请求路径', name: 'uri', width: 100 },
            { label: '请求方式', name: 'requireMethod', width: 100 },
            { label: '请求参数', name: 'paramData', width: 100 },
            { label: '请求时间', name: 'requireTime', width: 100 },
            { label: '结束时间(ms)', name: 'endTime', width: 100 },
            { label: '响应时长', name: 'responseTime', width: 100 },
            { label: '响应信息', name: 'response', width: 100 },
            { label: '堆栈信息', name: 'stackMessage', width: 100 },
            { label: 'uid', name: 'uid', width: 100 },
            { label: '类型', name: 'type', width: 100 ,formatter: function(value, options, row){
                    var html=null;
                    if(value ==0){
                        html = '<span >正常日志</span>';
                    }else {
                        html = '<span >错误日志</span>';
                    }
                    return html;
            }},
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            type: null,
        },
        showList: true,
        title:null,
    },
    methods: {
        query: function () {
            vm.reload();
        },
        del: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "common/log/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        reload: function () {
            console.log(vm.q.name);
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'type': vm.q.type==null?null:logTypeMap[vm.q.type].key,
                },
                page:page
            }).trigger("reloadGrid");
        }
    }
});