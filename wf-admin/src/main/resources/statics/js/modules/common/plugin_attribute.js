$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'pluginAttribute/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '插件id', name: 'pluginConfigId', width: 90 },
            { label: 'key', name: 'name', width: 100 },
            { label: 'value', name: 'value', width: 100 },
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
    vm.getPluginName();
});

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            title: null,
            hardwareKey:null,
            id:null,
            uid:null,
            type:null
        },
        showList: true,
        title:null,
        ff:{
            pluginConfigId:null,
            name:null,
            value:null,
        },
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.ff = {
                pluginConfigId:null,
                name:null,
                value:null,
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
            $.get(baseURL + "pluginAttribute/info/"+id, function(r){
                vm.ff = r.pluginConfigAttribute;
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
                    url: baseURL + "pluginAttribute/delete",
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
            var url = vm.ff.id == null ? "pluginAttribute/save" : "pluginAttribute/update";
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
        getPluginName:function(){
            $.get(baseURL + "pluginAttribute/getPluginName/", function(r){
                pluginNameMap = r.mapList;
            });
        },
        reload: function () {
            console.log(vm.q.name);
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'title': vm.q.title,
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