$(function () {
    // $('#beginDate').datepicker({
    //     autoclose: true,
    //     language: 'zh-CN',
    // }).on('changeDate',function(e){
    //     var startTime = e.date;
    //     $('#endDate').datepicker('setStartDate',startTime);
    // });
    //
    // $('#endDate').datepicker({
    //     autoclose: true,
    //     language: 'zh-CN',
    // }).on('changeDate',function(e){
    //     var endTime = e.date;
    //     $('#beginDate').datepicker('setEndDate',endTime);
    // });

    $("#jqGrid").jqGrid({
        url: baseURL + 'common/ad/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 40, key: true },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
            { label: '标题', name: 'title', width: 90 },
            { label: '类型', name: 'type', width: 100 ,formatter: function(value, options, row){
                    var html=null;
                    if(value ==0){
                        html = '<span >banner</span>';
                    }else {
                        html = '<span >其他</span>';
                    }
                    return html;
                }},
            { label: '内容', name: 'content', width: 100 },
            { label: '位置', name: 'position', width: 100 },
            { label: '开始时间', name: 'beginDate', width: 100 },
            { label: '结束时间', name: 'endDate', width: 100 },
            { label: 'path', name: 'path', width: 100,formatter: function (value, options, row) {
                if (value != null && value != "") {
                    var index = value.indexOf("http");
                    if (index == -1){
                        value = imgBaseUrl + value;
                    }
                    return '<img src="'+ value + '" width="30" height="30">';
                } else {
                    return value;
                }
            }},
            { label: 'url', name: 'url', width: 100 },
            { label: '排序', name: 'orders', width: 100 },
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
            title: null,
            hardwareKey:null,
            id:null,
            uid:null,
            sign:null
        },
        showList: true,
        title:null,
        ff:{
            orders:null,
            title:null,
            type:null,
            content:null,
            path:null,
            beginDate:null,
            endDate:null,
            url:null,
            position:null,
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
                orders:null,
                title:null,
                type:null,
                content:null,
                path:null,
                beginDate:null,
                endDate:null,
                url:null,
                position:null,
                appType:null
            };
            new AjaxUpload('#upload', {
                action: baseURL + "common/upload/ad",
                name: 'file',
                autoSubmit:true,
                responseType:"json",
                onSubmit:function(file, extension){
                    if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
                        alert('只支持jpg、png、gif格式的图片！');
                        return false;
                    }
                },
                onComplete : function(file, r){
                    if(r.code == 0){
                        vm.ff.path = r.url;
                    }else{
                        alert(r.msg);
                    }
                }
            });
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
            $.get(baseURL + "common/ad/info/"+id, function(r){
                vm.ff = r.ad;
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
                    url: baseURL + "common/ad/delete",
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
            // vm.ff.beginDate = $('#beginDate').val();
            // vm.ff.endDate = $('#endDate').val();
            var url = vm.ff.id == null ? "common/ad/save" : "common/ad/update";
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