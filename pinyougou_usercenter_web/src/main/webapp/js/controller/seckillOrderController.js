app.controller("seckillOrderController", function ($scope,$controller, $location, $interval, seckillOrderService) {

    $controller('baseController',{$scope:$scope});//继承

    //获取秒杀商品列表
    $scope.searchSecKillOrderList = function () {
        seckillOrderService.findAll().success(function (response) {
            $scope.list = response;
        });
    };

    // 立即支付
    $scope.payNow = function(orderId){
        location.href = "pay.html#?orderId="+orderId;
    };

    // 删除订单
    $scope.delete = function (orderId) {
        seckillOrderService.delete(orderId).success(function (response) {
            if(response.success){
                // 刷新页面
                $scope.searchSecKillOrderList();
            }else{
                alert(response.message);
            }
        });
    };


    // 订单详情
    $scope.orderDetail = function(orderId){
        location.href = "home-seckillOrderDetail.html#?orderId="+orderId;
    };

    // 查询订单详情
    $scope.seckillGoodsOrder = {};
    $scope.seckillGoodsOrderStatus ={};
    $scope.progressBar = 0;
    $scope.findSeckillOrderDetail = function () {
        seckillOrderService.findSeckillOrderDetail($location.search()["orderId"]).success(function (response) {
            $scope.seckillGoodsOrder = response;
            var status = $scope.seckillGoodsOrder.seckillOrder.status;
            if(status == "0"){
                $scope.seckillGoodsOrderStatus = "未付款";
                $scope.progressBar = 1;
            }else if(status == "1"){
                $scope.seckillGoodsOrderStatus = "已付款";
                $scope.progressBar = 2;
            }else if(status == "2"){
                $scope.seckillGoodsOrderStatus = "未发货";
                $scope.progressBar = 2;
            }else if(status == "3"){
                $scope.seckillGoodsOrderStatus = "已发货";
                $scope.progressBar = 4;
            }else if(status == "4"){
                $scope.seckillGoodsOrderStatus = "交易成功";
                $scope.progressBar = 5;
            }else if(status == "5"){
                $scope.seckillGoodsOrderStatus = "交易关闭";
                $scope.progressBar = 0;
            }else if(status == "6"){
                $scope.seckillGoodsOrderStatus = "待评价";
                $scope.progressBar = 6;
            }
        });
    };


    // 判断状态
    //$scope.seckillGoodsOrderStatus = ["未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];
    // var status = $scope.seckillGoodsOrder


    /*$scope.findOne = function () {
        seckillOrderService.findOne($location.search()["id"]).success(function (response) {
            $scope.entity = response;

            //倒计时总秒数
            var allSeconds = Math.floor((new Date(response.endTime).getTime() - new Date().getTime()) / 1000);

            //$interval是angularJS的内置服务，参数1：要执行的内容，参数2：执行频率（毫秒）
            var task = $interval(function () {
                if (allSeconds > 0) {
                    allSeconds = allSeconds - 1;
                    //转换倒计时总秒数为 **天**:**:** 的格式并在页面展示
                    $scope.timestring = convertTimeString(allSeconds);
                } else {
                    //取消定时任务
                    $interval.cancel(task);
                    alert("秒杀活动已结束。");
                }
            }, 1000);
        });
    };

    convertTimeString = function (allSeconds) {
        //天数
        var days = Math.floor(allSeconds / (60 * 60 * 24));
        //时
        var hours = Math.floor((allSeconds - days * 60 * 60 * 24) / (60 * 60));
        //分
        var minutes = Math.floor((allSeconds - days * 60 * 60 * 24 - hours * 60 * 60) / 60);
        //秒
        var seconds = allSeconds - days * 60 * 60 * 24 - hours * 60 * 60 - minutes * 60;

        var str = "";
        if (days > 0) {
            str = days + "天";
        }
        return str + hours + ":" + minutes + ":" + seconds;
    };
	
    //提交秒杀订单
    $scope.submitOrder = function () {
        seckillGoodsService.submitOrder($scope.entity.id).success(function (response) {
            if(response.success){
                alert("提交订单成功；请在1分钟内完成支付");
                location.href = "pay.html#?outTradeNo=" + response.message;
            } else {
                alert(response.message);
            }
        });
    };*/
});