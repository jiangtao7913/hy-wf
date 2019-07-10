$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'order/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '订单价格', name: 'orderPrice', width: 90 },
            {label: '订单状态', name: 'orderStatus', width: 100, formatter: function (value, options, row) {
                var html = null;
                if (value == 0) {
                    html = '<span >已确认</span>';
                } else if(value == 1){
                    html = '<span >已完成</span>';
                }else {
                    html = '<span >已取消</span>';
                }
                return html;
            }},
            { label: '支付状态', name: 'payStatus', width: 100,formatter: function (value, options, row) {
                var html = null;
                if (value == 0) {
                    html = '<span >未支付</span>';
                } else if(value == 1){
                    html = '<span >已支付</span>';
                }else if(value == 2){
                    html = '<span >部分退款</span>';
                }else {
                    html = '<span >已退款</span>';
                }
                return html;
            }},
            { label: '订单类型', name: 'type', width: 100,formatter: function (value, options, row) {
                var html = null;
                if (value == 0) {
                    html = '<span >购买单个功能</span>';
                } else if(value == 1){
                    html = '<span >购买vip</span>';
                }
                return html;
            }},
            { label: '订单编号', name: 'orderNumber', width: 100 },
            { label: '业务id', name: 'businessId', width: 100 },
            { label: '摘要', name: 'summary', width: 100 },
            { label: '用户id', name: 'userId', width: 100 },
            { label: '支付方式', name: 'payType', width: 100 },
            { label: '付款人', name: 'payer', width: 100 },
            { label: '付款日期', name: 'paymentDate', width: 100 },
            { label: '收款单号', name: 'payNumber', width: 100 },
            { label: '支付方法', name: 'paymentName', width: 100 },
            { label: '插件名称', name: 'pluginName', width: 100 },
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
    //vm.getTodayCredit();
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

        // todayCredit:null,
        // todayCount:null,
        // alipayCount:null,
        // weixinpayCount:null,
        // balanceCount:null,
        // deductCount:null
    },
    methods: {
        query: function () {
            vm.reload();
        },
        // getTodayCredit: function(){
        //     $.get(baseURL + "order/findTodayCredit", function(r){
        //         console.log(r);
        //         vm.todayCredit = r.resMap.todayCredit == null?0:r.resMap.todayCredit+" 元";
        //         var detail =  r.resMap.detail;
        //         detail.map(function(value,index){
        //             console.log(value);
        //             if(value.AMOUNT ==1){
        //                 vm.detailOne = value.count;
        //             }else if(value.AMOUNT ==6){
        //                 vm.detailTwo = value.count;
        //             }else if(value.AMOUNT ==18){
        //                 vm.detailThree = value.count;
        //             }else if(value.AMOUNT ==30){
        //                 vm.detailFour = value.count;
        //             }else if(value.AMOUNT ==68){
        //                 vm.detailFive = value.count;
        //             }else if(value.AMOUNT ==128){
        //                 vm.detailSix = value.count;
        //             }else if(value.AMOUNT ==328){
        //                 vm.detailSeven = value.count;
        //             }
        //         });
        //     });
        // },
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