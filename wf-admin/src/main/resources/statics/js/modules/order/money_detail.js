$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'order/moneyRecord/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '用户id', name: 'userId', width: 90 },
            {label: '描述', name: 'meno', width: 100},
            { label: '订单状态', name: 'status', width: 100,formatter: function (value, options, row) {
                    var html = null;
                    if (value == 0) {
                        html = '<span >审核中</span>';
                    } else if(value == 1){
                        html = '<span >成功</span>';
                    }else if(value == 2){
                        html = '<span >取消</span>';
                    }
                    return html;
                }},
            { label: '订单类型', name: 'type', width: 100,formatter: function (value, options, row) {
                var html = null;
                if (value == 0) {
                    html = '<span >消费</span>';
                } else if(value == 1){
                    html = '<span >提现</span>';
                }else if(value == 2){
                    html = '<span >收入</span>';
                }
                return html;
            }},
            { label: '编号', name: 'number', width: 100},
            { label: '徒弟名称', name: 'otherUserName', width: 100 },
            { label: '徒弟id', name: 'otherUserId', width: 100 },
            { label: '提现账号', name: 'account', width: 100 },
            { label: '提现人姓名', name: 'name', width: 100 },
            { label: '提现金额', name: 'amount', width: 100 },
            { label: '操作', name: '', width: 80, formatter:function(cellvalue,options,rowObject){
                    if(rowObject.type=='1'){
                        if(rowObject.status=='0'){
                            return "<a class='btn btn-primary' href='#' onclick=\"withdrawAudit("+ rowObject.id+")\">审核</a>";
                        }
                    }
                    return "";
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

/**
 * 提现审核
 */
var withdrawAudit = function(id){
    vm.withdrawAudit(id);
}

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            userId: null,
            status:null,
            type:null,
        },
        showList: true,
        title:null,
        status:null,
        reason:null
    },
    methods: {
        query: function () {
            vm.reload();
        },
        //审核订单
        withdrawAudit: function(id){
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "审核",
                area: ['550px', '450px'],
                shadeClose: false,
                content:jQuery("#auditOrder"),
                btn: ['确认','取消'],
                btn1: function (index) {
                    var data=null;
                    if(vm.status=='received'){
                        data = "id="+id+"&reason="+vm.reason+"&status="+vm.status;
                    }else{
                        data = "id="+id+"&reason="+vm.reason+"&status="+vm.status;
                    }
                    if(vm.status == null || vm.status ==''){
                        alert("请选择是否通过");
                        return ;
                    }
                    var msg = "审核成功";
                    var url = baseURL + 'order/withdrawAudit';
                    $.ajax({
                        type: "POST",
                        url: url,
                        dataType: "json",
                        data: data,
                        success: function(r){
                            if(r.code === 0){
                                layer.close(index);
                                layer.alert(msg, function(index){
                                    layer.close(index);
                                    vm.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        // withdrawAudit:function(id){
        //     var url = baseURL + 'order/withdrawAudit';
        //     var data = "id="+id;
        //     $.ajax({
        //         type: "POST",
        //         url: url,
        //         dataType: "json",
        //         data: data,
        //         success: function(r){
        //             if(r.code === 0){
        //                 alert("提现成功",function(){
        //                     vm.reload();
        //                 });
        //             }else{
        //                 alert("提现失败");
        //             }
        //         }
        //     });
        // },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'userId': vm.q.userId,
                    'status': vm.q.status==null?null:moneyRecordStatusMap[vm.q.status].key,
                    'type': vm.q.type==null?null:moneyRecordTypeMap[vm.q.type].key,
                },
                page:page
            }).trigger("reloadGrid");
        }
    }
});