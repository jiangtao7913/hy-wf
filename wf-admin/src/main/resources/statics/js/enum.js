var logTypeMap ={
		all:{key:100,value:"全部"},
		recharge:{key:0,value:"正常"},
		vip:{key:1,value:"错误"}
};

var orderStatusMap ={
    all:{key:100,value:"全部"},
    confirmed:{key:0,value:"已确认"},
    completed:{key:1,value:"已完成"},
    cancelled:{key:2,value:"已取消"}
};

var payStatusMap ={
    all:{key:100,value:"全部"},
    unpaid:{key:0,value:"未支付"},
    paid:{key:1,value:"已支付"},
    partialRefunds:{key:2,value:"部分退款"},
    refunded:{key:3,value:"已退款"}
};

var typeMap = {
    all:{key:100,value:"全部"},
    unpaid:{key:0,value:"购买单个功能"},
    paid:{key:1,value:"购买vip"},
};


var pluginNameMap=[];

var moneyRecordStatusMap = {
    all:{key:100,value:"全部"},
    unpaid:{key:0,value:"审核中"},
    paid:{key:1,value:"成功"},
    fail:{key:2,value:"失败"},
};

var moneyRecordTypeMap = {
    all:{key:100,value:"全部"},
    consume:{key:0,value:"消费"},
    withdraw:{key:1,value:"提现"},
    income:{key:2,value:"收入"},
};

var imgBaseUrl = "http://admin.wf.hongyunapp.com";