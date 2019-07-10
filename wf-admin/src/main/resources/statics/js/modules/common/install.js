$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'common/install/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '修改时间', name: 'modifyDate', width: 90 },
            { label: '硬件Key', name: 'hardwareKey', width: 100 },
            { label: 'ip', name: 'ip', width: 100 },
            { label: 'clientType', name: 'clientType', width: 100 },
            { label: 'apk版本', name: 'clientVersion', width: 100 },
            { label: '备注', name: 'memo', width: 100 },
            { label: '来源', name: 'source', width: 100 },
            { label: '包名', name: 'appType', width: 100 },
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
            console.log(vm.q.name);
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'hardware_key': vm.q.hardwareKey,
                },
                page:page
            }).trigger("reloadGrid");
        }
    }
});