$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'statistics/pay/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '时间', name: 'date',index:'date', sortable: true, width: 75 },
            { label: '总收入', name: 'totalIncome', width: 90 },
            { label: '微信收入', name: 'wxIncome', width: 100 },
            { label: '支付宝收入', name: 'zfbIncome', width: 100 },
            { label: '余额收入', name: 'balanceIncome', width: 100 },
            { label: '总提成', name: 'totalDeduct', width: 100 },
            { label: '安装人数', name: 'totalInstall', width: 100 },
            { label: 'DAC', name: 'dayActiveCount', width: 100 },
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
        postData:{
            'orderStatus':vm.q.orderStatus==null?null:$.trim(orderStatusMap[vm.q.orderStatus].key)
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
            userId: null,
            orderStatus:'completed',
            payStatus:null,
            type:null,
            sign:null
        },
        showList: true,
        title:null,
    },
    methods: {
        query: function () {
            vm.reload();
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'userId': vm.q.userId,
                    'orderStatus': vm.q.orderStatus==null?null:orderStatusMap[vm.q.orderStatus].key,
                    'payStatus': vm.q.payStatus==null?null:payStatusMap[vm.q.payStatus].key,
                    'type': vm.q.type==null?null:typeMap[vm.q.type].key,
                },
                page:page
            }).trigger("reloadGrid");
        }
    }
});