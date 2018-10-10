app.controller('passWordController' ,function ($scope,passwordService) {



    $scope.update=function () {
        if ($scope.newpassword != $scope.newpassword2){
            alert("密码不一致!");
            return;
        }
        passwordService.update($scope.password, $scope.newpassword).success(
                function (responce) {
                    if(responce.success){
                        alert("修改成功,请重新登录");

                        window.parent.location.href="/shoplogin.html";
                    }else{
                        alert(responce.message)
                    }

                }
            )
    }
})

