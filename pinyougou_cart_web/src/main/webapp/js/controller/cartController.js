app.controller("cartController",function ($scope,cartService,addressService,orderService) {

    //初始化页面：获取购物车集合数据
    $scope.findCartList=function () {
        cartService.findCartList().success(function (response) {
            $scope.cartList = response;
            //计算合计数
            $scope.totalValue = cartService.sum($scope.cartList);
        })
    }

    //点击商品数量添加/减少
    $scope.addGoodsToCartList=function (itemId, num) {
        cartService.addGoodsToCartList(itemId,num).success(function (response) {
            if(response.success){
                //刷新页面
                $scope.findCartList();
            } else{
                alert(response.message);
            }
        })
    }

    //结算页初始化根据当前登录者用户信息查询收货人列表
    $scope.findAddressByLoginUser=function () {
        addressService.findAddressByLoginUser().success(function (response) {
            $scope.addressList = response;
            //遍历这个集合把默认状态：1 赋值给$scope.address
            $scope.address = $scope.addressList[0];
            /*for(var i = 0; i < $scope.addressList.length; i++){
                if($scope.addressList[i].isDefault == 1){
                    $scope.address = $scope.addressList[i];
                }
            }*/
        })
    }

    //點擊选中收货人地址
    $scope.selectAddress=function (address) {
        $scope.address = address;
    }
    //判断是否点击选中收货人地址
    $scope.isSelectAddress=function (address) {
        if(address == $scope.address){
            return true;
        }
        return false;
    }

    //初始化一个Order对象，为了方便点击提交订单到后台
    //初始化支付类型：在线支付
    $scope.order={paymentType:1};

    //点击选中支付类型
    $scope.selectPayType=function (type) {
        $scope.order.paymentType = type;
    }


    //提交订单了
    $scope.saveOrder=function () {

        //提交什么数据？
        //收货人地址数据
        $scope.order.receiverAreaName = $scope.address.address;
        $scope.order.receiverMobile = $scope.address.mobile;
        $scope.order.receiver = $scope.address.contact;

        orderService.save($scope.order).success(function (response) {
            if(response.success){
                //跳转到支付页面
                location.href="/pay.html";
            }
        })
    }
    
});