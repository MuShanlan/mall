app.service("brandService",function($http){
    this.findAll=function(){
        return $http.get("../brand/findAll");
    }
    this.findPage=function(pageNum,pageSize){
        return $http.get("../brand/findPage?pageNum="+pageNum+"&pageSize="+pageSize);
    }
    this.update=function(entity){
        return $http.post("../brand/update",entity);
    }
    this.add=function(entity){
        return $http.post("../brand/add",entity);
    }
    this.dele=function(ids){
        return $http.get("../brand/delete?ids="+ids);
    }
    this.findOne=function (id) {
        return $http.get("../brand/findOne?id="+id);
    }
    this.search=function(pageNum,pageSize,entity){
        return $http.post("../brand/search?pageNum="+pageNum+"&pageSize="+pageSize,entity);
    }
    this.findMap=function(){
        return $http.get("../brand/findMap");
    }
});