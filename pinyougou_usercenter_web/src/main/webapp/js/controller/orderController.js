 //控制层 
app.controller('orderController' ,function($scope,$controller   ,orderService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		orderService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		orderService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		orderService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=orderService.update( $scope.entity ); //修改  
		}else{
			serviceObject=orderService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		orderService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}

	$scope.loginName = "";
	$scope.getLoginName = function () {
		orderService.getLoginName().success(function (resopnse) {
            $scope.loginName = resopnse;
        })
    }

    $scope.searchMap = {
        "status" : "",
        "page" : 1,
        "pageSize" : 5
    };

    $scope.search=function () {
        orderService.search($scope.searchMap).success(function (response) {
            //返回的数据建议用Map返回，因为后期你不确定是不是只返回一种数据，用Map来封装比较好！
            $scope.resultMap = response;
            $scope.list = $scope.resultMap.rows;

            //分页
            buildPageLabel();
        })
    }

    //初始化方法，获取首页传递过来的参数
    $scope.loadSearch=function (status) {
        $scope.searchMap.status = status;
        //执行查询
        $scope.search();
    }

    //构建分页标签(totalPages为总页数)
    buildPageLabel=function(){
        $scope.pageLabel=[];//新增分页栏属性
        var maxPageNo= $scope.resultMap.totalPages;//得到最后页码 13
        var firstPage=1;//开始页码
        var lastPage=maxPageNo;//截止页码
        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后边有点
        if($scope.resultMap.totalPages > 5){  //如果总页数大于5页,显示部分页码
            if($scope.searchMap.page<=3){//如果当前页小于等于3
                lastPage=5; //前5页
                $scope.firstDot=false;
            }else if( $scope.searchMap.page>=lastPage-2  ){//如果当前页大于等于最大页码-2
                firstPage= maxPageNo-4;		 //后5页
                $scope.lastDot= false;
            }else{ //显示当前页为中心的5页
                firstPage=$scope.searchMap.page-2;
                lastPage=$scope.searchMap.page+2;
                $scope.firstDot=true;//前面有点
                $scope.lastDot=true;//后边有点
            }
        }
        //循环产生页码标签
        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    }
    
});	
