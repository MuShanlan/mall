app.controller("indexController",function($scope,indexService){

    //因为你查询的时候不一定只查首页广告轮播图的，有可能会查询多个，所以咱们放在数组中
    $scope.contentList=[];

    $scope.findContentByCategoryId=function(categoryId){
        indexService.findContentByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId] = response;
        })
    }

    //点击搜索按钮去搜索工程查询
    $scope.search=function () {
        //页面传递参数可以直接在后面追加：#?
        location.href="http://search.pinyougou.com/search.html#?keywords="+$scope.keywords;
    }

});