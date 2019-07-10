$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'common/functionUse/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '修改时间', name: 'modifyDate', width: 90 },
            { label: '用户ID', name: 'userId', width: 100 },
            { label: '功能ID', name: 'functionId', width: 100 },
            { label: '硬件key', name: 'hardwareKey', width: 100 },
            { label: '使用次数', name: 'useCount', width: 100 },
            { label: '类型', name: 'type', width: 100 ,formatter: function(value, options, row){
                var html=null;
                if(value ==0){
                    html = '<span >免费使用</span>';
                }else {
                    html = '<span >付费使用</span>';
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
            hardwareKey: null,
        },
        showList: true,
        title:null,
    },
    methods: {
        query: function () {
            vm.reload();
        },
        reload: function () {
            console.log(vm.q.hardwareKey);
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'hardwareKey': vm.q.hardwareKey,
                },
                page:page
            }).trigger("reloadGrid");
        }
    }
});