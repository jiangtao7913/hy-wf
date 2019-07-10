$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'common/config/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: 'name', name: 'name', width: 90 },
            { label: 'value', name: 'value', width: 100 },
            { label: 'model', name: 'model', width: 100 },
            { label: '描述', name: 'remark', width: 100 },
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
            model: null,
            appType:null,
        },
        showList: true,
        title:null,
        ff:{
            name:null,
            value:null,
            model:null,
            remark:null,
            appType:null
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
                name:null,
                value:null,
                model:null,
                remark:null,
                appType:null
            };
        },
        update: function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }

            vm.showList = false;
            vm.title = "修改";
            vm.getAd(id);
        },
        getAd: function(id){
            $.get(baseURL + "common/config/info/"+id, function(r){
                vm.ff = r.config;
            });
        },
        del: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            if(ids.length>1){
                alert('只能选中一个', function(){
                    vm.reload();
                });
                return ;
            }
            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "common/config/delete",
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
            var url = vm.ff.id == null ? "common/config/save" : "common/config/update";
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
                    'model': vm.q.model,
                    'appType': vm.q.appType
                },
                page:page
            }).trigger("reloadGrid");
        }
    }
});