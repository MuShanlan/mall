app.service("addressService",function ($http) {

    this.findAddressByLoginUser=function () {
        return $http.get("../address/findAddressByLoginUser");
    }

    this.saveAddress = function (entity) {
        return $http.post("../address/saveAddress",entity);
    }
});