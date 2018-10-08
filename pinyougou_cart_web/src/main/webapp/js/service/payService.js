app.service("payService", function ($http) {
    this.createNative = function () {
        //r表示避开浏览器缓存；每次都到服务器执行请求
        return $http.get("pay/createNative.do?r=" + Math.random());

    };

    this.queryPayStatus = function (out_trade_no) {
        return $http.get("pay/queryPayStatus.do?out_trade_no=" + out_trade_no);

    };
});