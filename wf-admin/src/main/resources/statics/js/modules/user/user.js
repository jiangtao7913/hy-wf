$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/list',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'id', index: "id", width: 40, key: true,sortorder:"desc" },
			{ label: 'UID', name: 'uid', width: 50 },
            { label: '创建时间', name: 'createDate',index:'createDate', sortable: true, width: 75 },
			{ label: '名称', name: 'name', width: 90 },
			{ label: '头像', name: 'icon', width: 100 , formatter: function (value, options, row) {
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
            { label: '性别', name: 'sex', width: 100 },
            { label: '设备登录', name: 'hardwareKey', width: 100 },
            { label: '账号类型', name: 'accountType',index:'accountType', width: 60, formatter: function(value, options, row){
                   var html=null;
                   if(value ==0){
                       html = '<span >微信</span>';
                   }else if(value ==1){
                       html = '<span >QQ</span>';
                   }else if(value ==2){
                       html = '<span >手机</span>';
                   }else {
                       html = '<span >虚拟用户</span>';
                   }
                    return html;
            }},
            { label: '来源', name: 'source', index: "source", width: 85},
			{ label: '关键字', name: 'sign', index: "sign", width: 85},
            { label: '包名', name: 'appType', index: "appType", width: 85},
            { label: '充值金额', name: 'rechargeTotal', index: "rechargeTotal", width: 85},
            { label: '余额', name: 'balance', index: "balance", width: 85},
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
            name: null,
            hardwareKey:null,
            id:null,
            uid:null,
            sign:null
        },
        showList: true,
        title:null,
        user:{
            name:null,
            icon:null,
            sex:null,
            hardwareKey:null,
            accountType:3,
            sign:null,
            rechargeTotal:null,
            balance:null,
            source:'admin',
            appType:'admin',
            password:'admin'
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.user = {
                name:null,
                icon:null,
                sex:null,
                hardwareKey:null,
                accountType:3,
                sign:null,
                rechargeTotal:null,
                balance:null,
                source:'admin',
                appType:'admin',
                password:'admin'
            };
        },
        update: function () {
            var userId = getSelectedRow();
            if(userId == null){
                return ;
            }

            vm.showList = false;
            vm.title = "修改";
            vm.getUser(userId);
        },
        getUser: function(userId){
            $.get(baseURL + "user/info/"+userId, function(r){
                vm.user = r.user;
            });
        },
        del: function () {
            var userIds = getSelectedRows();
            if(userIds == null){
                return ;
            }
            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
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
            var url = vm.user.id == null ? "user/save" : "user/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
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
                    'name': vm.q.name,
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