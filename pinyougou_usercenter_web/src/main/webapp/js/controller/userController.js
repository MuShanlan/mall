//控制层
app.controller('userController', function ($scope, $controller, userService, seckillOrderService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        userService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        userService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        userService.findAddressOne(id).success(function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {

        //判断密码
        if ($scope.entity.password != $scope.password) {
            alert("密码不一致，请重新输入...");
            return;
        }

        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = userService.update($scope.entity); //修改
        } else {
            serviceObject = userService.add($scope.entity, $scope.smsCode);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //注册成功后，跳转到登录页面(修改：跳转到个人中心的首页)
                    location.href = "/home-index.html";
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function (id) {
        userService.dele(id).success(
            function (response) {
                if (response.success) {
                    $scope.findUserAddressAll();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        userService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //发送短信的方法
    $scope.sendSms = function () {
        userService.sendSms($scope.entity.phone).success(function (response) {
            if (response.success) {
                alert("验证码已发送");
            } else {
                alert(response.message);
            }
        })
    }


    //查询用户信息
    /*$scope.birthday = [];
    $scope.birYear = "";
    $scope.birMonth = "";
    $scope.birDay = "";*/
    $scope.findUser = function () {
        userService.findOne().success(function (response) {
                $scope.entity = response;
               /* $scope.birthday = $scope.entity.birthday.split("-");
                $scope.birYear = $scope.birthday[0];
                $scope.birMonth = $scope.birthday[1];
                $scope.birDay = $scope.birthday[2];*/
            }
        );
    };

    /**
     * 保存个人信息
     */
    $scope.saveInformation = function () {
        $scope.entity.sex = $scope.sexNum;

        userService.update($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert(response.message);
                    $scope.findUser();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }

    $scope.sexNum = "";
    $scope.sex = function(num){
        $scope.sexNum = num;
    }
    /**
     * 页面初始化查询当前登录用户的所有地址
     */
    $scope.findUserAddressAll = function () {
        userService.findUserAddressAll().success(
            function (response) {
                $scope.addressList = response;
            }
        );
    }

    /**
     * 新增地址
     */
    $scope.addAddress = function () {
        userService.addAddress($scope.entity).success(function (response) {
                if (response.success) {
                    alert(response.message);
                    $scope.findUserAddressAll();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }

    /**
     * 密码设置
     */
    $scope.savSubmit = function () {
        //修改
        userService.update($scope.entity).success(function (response) {
            if (response.success) {
                alert(response.message);
                location.href="/logout/cas";
            } else {
                alert(response.message);
            }
        });
    }


});
