app.controller("payController", function ($scope, $location, payService) {

    //生成支付二维码
    $scope.createNative = function () {
        payService.createNative().success(function (response) {
            if("SUCCESS" == response.result_code){
                //微信那边下单成功
                $scope.out_trade_no = response.out_trade_no;
                //本笔订单花的总金额；精确到两位小数
                $scope.money = (response.total_fee/100).toFixed(2);

                //生成二维码
                var qr = new QRious({
                    element:document.getElementById("qrious"),
                    size:250,
                    level:"Q",
                    value:response.code_url
                });

                //查询支付状态
                queryPayStatus(response.out_trade_no);
            } else {
                alert("生成支付二维码失败");
            }

        });

    };

    //根据订单号查询订单支付状态
    queryPayStatus = function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(function (response) {
            if (response.success) {
                //跳转到支付成功的页面
                location.href="paysuccess.html#?money=" + $scope.money;
            } else {
                if("支付二维码超时"== response.message){
                    $scope.createNative();
                } else {
                    location.href = "payfail.html";
                }
            }

        });

    };

    //获取请求地址栏中的参数
    $scope.getMoney = function () {
       return $location.search()["money"];
    };
});