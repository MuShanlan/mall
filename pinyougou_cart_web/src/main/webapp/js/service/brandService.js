app.service("brandService", function ($http) {
    this.findAll = function () {
        return $http.get("../address/findAll");
    }

    this.add = function (entity) {
        return $http.post("../address/add", entity);
    }
    this.dele = function (ids) {
        return $http.get("../brand/delete?ids=" + ids);
    }
    this.findOne = function (id) {
        return $http.get("../brand/findOne?id=" + id);
    }
    this.search = function (pageNum, pageSize, entity) {
        return $http.post("../brand/search?pageNum=" + pageNum + "&pageSize=" + pageSize, entity);
    }
});
