$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'invitationDetail/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '邀请id', name: 'invitationId', width: 90 },
            { label: '徒弟id', name: 'preticeId', width: 100 },
            { label: '徒弟uid', name: 'preticeUid', width: 100 },
            { label: '徒弟名称', name: 'name', width: 100 },
            { label: '消费描述', name: 'meno', width: 100 },
            { label: '徒弟带来的收益', name: 'incomeTotal', width: 100 },
            { label: '类型', name: 'type', width: 100,formatter: function(value, options, row){
                var html=null;
                if(value ==0){
                    html = '<span >一级徒弟</span>';
                }else if(value ==1){
                    html = '<span >二级徒弟</span>';
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
            invitationId: null,
            hardwareKey:null,
            id:null,
            uid:null,
            sign:null
        },
        showList: true,
        title:null,
        ff:{
            invitationId:null,
            preticeId:null,
            preticeUid:null,
            name:null,
            price:null,
            meno:null,
            incomeTotal:null,
            type:null
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.ff = {
                invitationId:null,
                preticeId:null,
                preticeUid:null,
                name:null,
                meno:null,
                incomeTotal:null,
                type:null
            };
        },
        update: function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }

            vm.showList = false;
            vm.title = "修改";
            vm.getFunction(id);
        },
        getFunction: function(id){
            $.get(baseURL + "invitationDetail/info/"+id, function(r){
                vm.ff = r.invitationDetail;
            });
        },
        del: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "invitationDetail/delete",
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
        saveOrUpdate: function () {
            var url = vm.ff.id == null ? "invitationDetail/save" : "invitationDetail/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.ff),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },

        reload: function () {
            console.log(vm.q.name);
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'invitationId': vm.q.invitationId,
                    'id': vm.q.id,
                    'uid': vm.q.uid,
                    'hardwareKey': vm.q.hardwareKey,
                    'sign': vm.q.sign,
                },
                page:page
            }).trigger("reloadGrid");
        }
    }
});