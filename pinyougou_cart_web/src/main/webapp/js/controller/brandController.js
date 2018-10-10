app.controller("brandController",function($scope,brandService){

    $scope.findAll=function () {
        brandService.findAll().success(function (response) {
            $scope.list=response;
        });
    }

    $scope.add=function(){
        var objectName = "";
        if($scope.entity.id != null){
            objectName = brandService.update($scope.entity);
        } else{
            objectName = brandService.add($scope.entity);
        }
        objectName.success(function (response) {
            if(response.success){
                //成功了
                $scope.entity  = null;
                $scope.findAll();
            } else{
                alert(response.message);
            }
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

} );