var $j = jQuery.noConflict()
var CoopReg ={
	lastUserName:"q1",
  	isUserName:0,
  	isSecCode:0,
  	isRealname:0,
  	isPassWord:0,
	isPassWord2:0,
  	isMobile:0,
  	isEmail:0,
  	checkUserName:function(){
  		var userName = $('regName').value;
    	if(userName == this.lastUserName) {
	    	return ;
		} else {
			this.lastUserName = userName;
		}
		
		var unLen = userName.replace(/[^\x00-\xff]/g, "**").length;
		if(unLen < 2 || unLen > 12) {
			this.warning($('checkregName'), unLen < 3 ? '用户名小于3个字符' : '用户名超过 15 个字符');
			return;
		}
		postData.checkUsername(userName,this.backClickUserName);
	},
	backClickUserName:function (str){
   		if(str.indexOf('succeed') > -1){
    		$('checkregName').style.display = '';
			CoopReg.tipok($('checkregName'), '用户名可注册')
      		CoopReg.isUserName=1;	
    	}else{
   			var object = $j.parseJSON(str)
    		CoopReg.warning($('checkregName'), object.msg);
	    	CoopReg.isUserName=0;	
    	}
 	},
	checkMobile:function(){
		var mobile = $('mobile').value;
	    var patrn = /^1[358]\d{9}$/   
       	if(!patrn.exec(mobile)) {   
       		this.warning($('checkmobile'), '手机号码不符合规则');
			this.isMobile = 0;
      	}else{   
      	  $('checkmobile').style.display = '';
		  this.tipok($('checkmobile'), '手机号格式正确')
      	  this.isMobile = 1;
      	}
	},
	checkPassword:function (confirm) {
		var password = $('regPaw').value;
		var regPaw = $('regPaw').value;
		if ( regPaw.length < 6){
			this.warning($('checkregPaw'), '密码长度不能少于6位');
			return false;
		}else if ( regPaw.length > 12){
			this.warning($('checkregPaw'), '密码长度不能超过12位');
			return false;
		}else {
			$('checkregPaw').style.display = '';
			this.tipok($('checkregPaw'), '密码输入无误')
			if(!confirm) {
				this.checkPassword2(true);
			}
			return true;
		}
	},
	checkPassword2:function (confirm) {
		var password = $('regPaw').value;
		var password2 = $('regArginPaw').value;
			if(password2 != '') {
				this.checkPassword(true);
			}
			if(password == '' || (confirm && password2 == '')) {
				$('checkregArginPaw').style.display = 'none';
				return;
			}
			if(password != password2) {
				this.isPassWord=0;
				this.warning($('checkregArginPaw'), '两次输入的密码不一致');
			} else {
				$('checkregArginPaw').style.display = '';
				this.tipok($('checkregArginPaw'), '密码保持一致')
				this.isPassWord=1;
			}
	},
	checkSeccode:function () {
		var seccodeVerify = $('seccode').value;
		postData.checkSeccode(seccodeVerify,this.backCheckSeccode);
	},
	backCheckSeccode:function (str){
		if(str.indexOf('succeed') > -1){
			$('checkseccode').style.display = '';
			$('checkseccode').innerHTML = '验证码正确';
			$('checkseccode').className = "warning";
			CoopReg.isSecCode=1;
		}else{
			CoopReg.isSecCode=0;
			var obj	= strToJSON(str);
			CoopReg.warning($('checkseccode'),obj.msg);
		}
	},
	warning:function (obj, msg) {
		obj.style.display = '';
		obj.innerHTML = msg
		$j(obj).addClass('error').removeClass('ok')
	},
	tipok:function (obj, msg) {
		obj.style.display = '';
		obj.innerHTML = msg
		$j(obj).addClass('ok').removeClass('error')
	},
	checkPwd:function (pwd){

		if (pwd == "") {
			$("chkpswd").className = "psdiv0";
			$("chkpswdcnt").innerHTML = "";
		} else if (pwd.length < 3) {
			$("chkpswd").className = "psdiv1";
			$("chkpswdcnt").innerHTML = "太短";
		} else if(!this.isPassword(pwd) || !/^[^%&]*$/.test(pwd)) {
			$("chkpswd").className = "psdiv0";
			$("chkpswdcnt").innerHTML = "";
		} else {
			var csint = this.checkStrong(pwd);
			switch(csint) {
				case 1:
					$("chkpswdcnt").innerHTML = "很弱";
					$( "chkpswd" ).className = "psdiv"+(csint + 1);
					break;
				case 2:
					$("chkpswdcnt").innerHTML = "一般";
					$( "chkpswd" ).className = "psdiv"+(csint + 1);
					break;
				case 3:		
					$("chkpswdcnt").innerHTML = "很强";
					$("chkpswd").className = "psdiv"+(csint + 1);
					break;
			}
		}
	},
	isPassword:function (str){
		if (str.length < 3) return false;
		var len;
		var i;
		len = 0;
		for (i=0;i<str.length;i++){
			if (str.charCodeAt(i)>255) return false;
		}
		return true;
	},
	charMode:function (iN){ 
		if (iN>=48 && iN <=57) //数字 
		return 1; 
		if (iN>=65 && iN <=90) //大写字母 
		return 2; 
		if (iN>=97 && iN <=122) //小写 
		return 4; 
		else 
		return 8; //特殊字符 
	}, 
	//计算出当前密码当中一共有多少种模式 
	bitTotal:function (num){ 
		modes=0; 
		for (i=0;i<4;i++){ 
			if (num & 1) modes++; 
			num>>>=1; 
		} 
		return modes; 
	}, 

	//返回密码的强度级别 
	checkStrong:function (pwd){ 
		modes=0; 
		for (i=0;i<pwd.length;i++){ 
			//测试每一个字符的类别并统计一共有多少种模式. 
			modes|=this.charMode(pwd.charCodeAt(i)); 
		} 
		return this.bitTotal(modes);
	},
	postReg:function (){
		$('__registerform').innerHTML="";
		var username = $('regName').value;
		var password = $('regPaw').value;
		var code = $('reg_seccode').value;
		var mobile = $('mobile').value;
		var verify = $j('#verify').val()
		var d_id = $j('#d_id').val()
		if(!this.isUserName) {
	   		$('__registerform').innerHTML="用户名输入错误!";
        	return;	   	
		}
	    if(!this.isPassWord){
	   		$('__registerform').innerHTML="密码输入错误!";
	   		return;
	    }
	    if(!this.isMobile){
	   		$('__registerform').innerHTML="手机号码输入错误!";
	   		return;
	    }
		/*if(!this.isSecCode){
	   		$('__registerform').innerHTML="验证码输入错误!";
	   		return;
		}*/
		$j.ajax({
			type: "post",
			url:"/?c=api_user&a=coopReg&callType=JSON",
			data: "&mobile=" + mobile + "&verify=" + verify + "&code=" + code + "&username=" + username + "&password=" + password + "&d_id=" + d_id,
			contentType: "application/x-www-form-urlencoded",
			dataType: "json",
			success: function(data){
				console.log(data)
				if(data['result'] == 'succeed'){
					$('__registerform').innerHTML="<div style='color:blue'>注册成功</div>";
					location.href = "/?c=main&a=index";
				}else{
					$('__registerform').innerHTML = data['msg'];
				}
			}
		})
		return false;
	},
	showregmsg:function(o){
		s = $("showregmsgs");
		if(o&&s){
			if(o.checked == true){
			  s.style.display = "block";
			}else{
			s.style.display ="none";	
			}
		}
	}
} 
