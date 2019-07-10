$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'plugin/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '插件名称', name: 'pluginName', width: 100 },
            { label: '排序', name: 'orders', width: 100 },
            { label: '是否启用', name: 'isEnabled', width: 100 ,formatter: function(value, options, row){
                    var html=null;
                    if(value ==0){
                        html = '<span >未启用</span>';
                    }else {
                        html = '<span >启用</span>';
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
            title: null,
            hardwareKey:null,
            id:null,
            uid:null,
            sign:null
        },
        showList: true,
        title:null,
        ff:{
            pluginName:null,
            orders:null,
            isEnabled:null,
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
                pluginName:null,
                orders:null,
                isEnabled:null,
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
            $.get(baseURL + "plugin/info/"+id, function(r){
                vm.ff = r.pluginConfig;
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
                    url: baseURL + "plugin/delete",
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
            var url = vm.ff.id == null ? "plugin/save" : "plugin/update";
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