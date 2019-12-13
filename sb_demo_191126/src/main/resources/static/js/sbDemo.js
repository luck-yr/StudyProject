$(document).ready(function(){
	
	
	
	
	
	
	
	
	
	
	$("#registerpassword").bind("blur",function(){
		var password = $(this).val();
		if(password == ""){
			$("#passwordnull").show();
		}
		if(password != ""){
			$("#passwordnull").hide();
		}
	});
	
	$("#registerusername").bind("blur",function(){
		var userName = $(this).val();
		if(userName == ""){
			$("#usernamenull").show();
			$("#usernameexist").hide();
		}
		if(userName != ""){
			$("#usernamenull").hide();
		}
		$.ajax({
			type:"post",
			url:"userNameIsExist",
			data:userName,
			contentType:"application/json",
			success:function(data){
				if(data.status == 500){
					$("#usernameexist").show();
				};
				if(data.status == 200){
					$("#usernameexist").hide();
				};
			}
		});
		
	});
	
	/*
	 * 点击注册按钮
	 */
	$("#registerButton").bind("click",function(){
		var user = {};
		user.userName = $("[name=userName]").val();
		user.password = $("[name=password]").val();
	
		
		$.ajax({
			type:"post",
			url:"doRegister",
			data:JSON.stringify(user),
			contentType:"application/json",
			success:function(data){
				if(data.status == 200){
					alert(data.message);
					location.href = "login";
				};
			},
			error:function(data){
				alert(data.responseText);
			}
		});
		
	});

	
	/*
	 * 点击登录按钮
	 */
	$("#loginButton").bind("click",function(){
		var user = {};
		user.userName = $("[name=userName]").val();
		user.password = $("[name=password]").val();
		user.rememberMe = $("#rememberMe").prop('checked');
		console.log(user);
		$.ajax({
			type:"post",
			url:"doLogin",
			data:JSON.stringify(user),
			contentType:"application/json",
			success:function(data){
				if(data.status == 200){
					alert(data.message);
					location.href = "dashboard";
				};
			},
			error:function(data){
				alert("用户名或密码错误");
			}
		});
		
	});
	
	
	/*
	 * users页面中点击edit按钮刷新到用户编辑界面,
	 * 展示出userName和所有的role
	*/
	$("[name=userEdit]").bind("click",function(){
		
		var userName = $(this).parents("tr").find("[name=userName]").text();
		var userId = $(this).parents("tr").find("[name=userId ]").text();
		$("#userEdit").css("display","block");
		$("#userList").css("display","none");
		$("#userName").val(userName);
		$("#userId").val(userId); 
		
		$.ajax({
			type:"get",
			url:"/account/roles/user/"+userId,
			success:function(data){
				$.each(data, function(i,item){
					$("input[name='roleCheckbox'][value=" + item.roleId + "]")
    				.attr("checked","checked");
				});
			},
			error:function(data){
				console.log("ajax failed");
			}
		});
	
		
	});
	
	/*
	点击提交按钮，ajax把数据提交到后台，插入中间表
	*/
	$("#userSubmit").bind("click",function(){
		var user = {};
		user.userId = $("#userId").val();
		var roles = new Array();
		$.each($("input[name='roleCheckbox']"), function() {
			if(this.checked){
				var role ={};
				role.roleId=$(this).val();
				roles.push(role);
			}
		});
		user.roles = roles;
		console.log(user);
		$.ajax({
			type:"post",
			url : "editUser",
			contentType:"application/json",
			data:JSON.stringify(user),
			success:function(data){
				if(data.status == 200){
					location.href="users"
				}else{
					$("[name=messageDiv]").show();
					$("[name=message]").html(data.message);
				}
			},
			error:function(data){
				$("[name=messageDiv]").show();
				$("[name=message]").html(data.message);
				console.log("ajax failed");
			}
		});
		
	});
	
	/*
     *roles界面中点击新增按钮，刷新到role编辑界面
     *隐藏 id="roleList" 的界面，展示 id="roleEdit"界面
	 */
	$("#addRole").bind("click",function(){
		$("#roleList").css("display","none");
		$("#roleEdit").css("display","block");
	});
	
	/*
	 *点击roles界面中的edit按钮，刷新到role编辑界面
	 *隐藏 id="roleList" 的界面，展示 id="roleEdit"界面
	 */
	$("[name=editRole]").bind("click",function(){
		var roleId = $(this).parents("tr").find("[name=roleId]").text();
		var roleName = $(this).parents("tr").find("[name=roleName]").text();
		
		$("#roleList").css("display","none");
		$("#roleEdit").css("display","block");
		$("#roleId").val(roleId);
		$("#roleName").val(roleName);
	});
	
	/*
	 * 点击role编辑界面中的提交
	 * 发送ajax请求到 editRole
 	 */
	$("#roleSubmit").bind("click",function(){
		var role = {};
		role.roleId = $("#roleId").val();
		role.roleName = $("#roleName").val();
		$.ajax({
			type:"post",
			url:"editRole",
			contentType:"application/json",
			data: JSON.stringify(role),
			
			success:function(data){
				if(data.status == 200){
					location.href="roles"
				}else{
					$("[name=messageDiv]").show();
					$("[name=message]").html(data.message);
				}
			},
			
			error:function(data){
				$("#messageDiv").show();
				$("[name=message]").html(data.message);
				console.log("ajax failed");
			}
		});
	});
	
	/*
	 * 点击增加按钮，刷新到resource编辑界面
	 * 隐藏id="resourceList"  显示id="resourceEdit"
	 */
	$("#addResource").bind("click",function(){
		$("#resourceList").hide();
		$("#resourceEdit").show();
	});
	
	/*
	 * 点击edit，刷新到resource编辑界面
	 * 隐藏id="resourceList"  显示id="resourceEdit"
	 */
	$("[name=editResource]").bind("click",function(){
		var resourceId = $(this).parents("tr").find("[name=resourceId]").text();
		var resourceName = $(this).parents("tr").find("[name=resourceName]").text();
		var resourceUri = $(this).parents("tr").find("[name=resourceUri]").text();
		var permission = $(this).parents("tr").find("[name=permission]").text();
		
		$.ajax({
			type:"post",
			url:"roles/resource/"+resourceId,
			success:function(data){
				$.each(data,function(i,item){
					$("input[name='roleCheckbox'][value=" + item.roleId + "]").attr("checked","checked");
					console.log("ajax success");
				});
			},
			error:function(data){
				console.log("ajax failed");
			}
		});
		
		$("#resourceId").val(resourceId);
		$("#resourceName").val(resourceName);
		$("#resourceUri").val(resourceUri);
		$("#permission").val(permission);
		$("#resourceList").hide();
		$("#resourceEdit").show();
	});
	
	$("#resourceSubmit").bind("click",function(){
		var resource = {};
		resource.resourceId = $("#resourceId").val();
		resource.resourceName =  $("#resourceName").val();
		resource.resourceUri = $("#resourceUri").val();
		resource.permission = $("#permission").val();
		var roles = new Array();
		$.each($("input[name='roleCheckbox']"),function(){
			if(this.checked){
				var role = {};
				role.roleId = $(this).val();
				roles.push(role);
			}
		});
		resource.roles = roles;
		$.ajax({
			type:"post",
			url:"editResource",
			contentType:"application/json",
			data:JSON.stringify(resource),
			success:function(data){
				if(data.status == 200){
					location.href = "resources";
				}else{
					$("[name=messageDiv]").show();
					$("[name=message]").html(data.message);
				}
			},
			error:function(data){
				$("[name=messageDiv]").show();
				$("[name=message]").html(data.message);
				console.log("ajax failed");
			}
		});
		
	});
	
})