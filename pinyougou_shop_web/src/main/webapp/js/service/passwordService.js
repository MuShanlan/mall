app.service('passwordService',function ($http) {

    this.update=function (password,newpassword) {
        return $http.get('../passWord/update?password='+password+'&newpassword='+newpassword);
    }
});