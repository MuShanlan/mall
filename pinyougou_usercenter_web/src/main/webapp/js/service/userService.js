//服务层
app.service('userService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../user/findAll');
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../user/findPage?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(){
		return $http.get('../user/findOne');
	}
	//查询地址
	this.findAddressOne=function(){
		return $http.get('../user/findAddressOne');
	}
	//增加 
	this.add=function(entity,smsCode){
		return  $http.post('../user/add?smsCode='+smsCode,entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../user/update',entity);
	}
	//删除
	this.dele=function(id){
		return $http.get('../user/deleteAddress?id='+id);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../user/search?page='+page+"&rows="+rows, searchEntity);
	}
	this.sendSms=function(phone){
		return $http.get("../user/sendSms?phone="+phone);
	}
	this.showName=function(){
		return $http.get("../user/showName");
	}
    this.findUserAddressAll=function(){
        return $http.get("../user/findUserAddressAll");
    }
    this.addAddress=function(entity){
        return $http.post("../user/addAddress",entity);
    }

});
