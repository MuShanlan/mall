//定义业务服务
app.service("seckillOrderService",function ($http) {
    //加载列表数据
    this.findAll = function(){
        return $http.get("../seckillOrder/searchSecKillOrderList");
    };

    this.findPage = function (page, rows) {
        return $http.get("../seckillOrder/searchSecKillOrderList?page=" + page + "&rows=" + rows);
    };

    this.add = function (entity) {
        return $http.post("../seckillOrder/add.do",entity);
    };

    this.update = function (entity) {
        return $http.post("../seckillOrder/update.do",entity);
    };

    this.findOne = function (id) {
        return $http.get("../seckillOrder/findOne.do?id=" + id);
    };

    this.delete = function (orderId) {
        return $http.get("../seckillOrder/delete?orderId=" + orderId);
    };

    this.search = function (page, rows, searchEntity) {
        return $http.post("../seckillOrder/searchSecKillOrderList?page=" + page + "&rows=" + rows, searchEntity);
    };

    this.checkLoginUser = function(){
        return $http.get("../seckillOrder/checkLoginUser");
    }
    this.findSeckillOrderDetail = function (orderId) {
        return $http.get("../seckillOrder/findSeckillOrderDetail?orderId="+orderId);
    }
});