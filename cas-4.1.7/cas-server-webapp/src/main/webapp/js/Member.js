/*
* 当前版本：Immotor
* 作   者：Bear
*/
postData.masg = function() { this.load("user", "checkMobileNumber", {"mobile":arguments[0]}, arguments[1]); };
var REG ={
	lastUserName:"",
	lastRealname:"",
	lastRealname:"",
	lastPassword:"",
	lastEmail:"",
	lastMobile:"",
	lastSeccode:"",
	isUserName:0,
	isSecCode:0,
	isRealname:0,
	isPassWord:0,
	isMobile:0,
	isEmail:0,
	postOpen:true,
	warningBox:[],
	init:function(){
		this.warningBox = $.getClass('warning c999');
		$('getYzm').onclick = function(){
			if($('regName').value == ""){ $.alert('请输入手机号码'); return;}
			if(this.className == "msgXym off"){ return; }
			postData.masg($('regName').value, REG.yxmCallback);
		}
	},
	checkUserName:function() {
		var mobile = $('regName').value;
		this.lastMobile = mobile;
		postData.checkUsername(this.lastMobile,this.backClickUserName);
	},
	backClickUserName:function (str){

		if (str.indexOf('succeed') > -1)
		{
			var patrn = /^1[358]\d{9}$/
			var mobile = REG.lastMobile;
			if (!patrn.exec(mobile) || mobile.length!=11) 
			{
				this.warningBox[0].className = "warning cred";
				this.warningBox[0].innerHTML = "请输入正确的手机号码";
				this.isMobile = 0;
			}else{
				REG.warningBox[0].className = "warning cgreen";
				REG.warningBox[0].innerHTML = "手机号码可以注册";
				REG.isUserName=1;
			}
		}
		else
		{
			var object = str.JSONtoObj(str);
			REG.warningBox[0].className = "warning cred";
			REG.warningBox[0].innerHTML = object.msg;
			REG.isUserName=0;
		}
	},
	checkPassword:function () {
		var patrn=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im; 
		var regPaw = $('regPaw').value;
		if ( regPaw.length < 6) 
		{
			this.warningBox[2].className = "warning cred";
			this.warningBox[2].innerHTML = "密码长度不能少于6位";
			this.isPassWord=0;
			this.checkPassword2();
		}else if ( regPaw.length > 12){
			this.warningBox[2].className = "warning cred";
			this.warningBox[2].innerHTML = "密码长度不能超过12位";
			this.isPassWord=0;
			this.checkPassword2();
		}else if ((regPaw == '') || (patrn.test(regPaw))){
			this.warningBox[2].className = "warning cred";
			this.warningBox[2].innerHTML = "密码空或包含非法字符";
			this.isPassWord=0;
			this.checkPassword2();
		}else{
			this.warningBox[2].className = "warning cgreen";
			this.warningBox[2].innerHTML = "密码可用";
			this.isPassWord=1;
			this.checkPassword2();
		}
		

	},
	checkPassword2:function () {
		var regPaw = $('regPaw').value;
		var regArginPaw = $('regArginPaw').value;
		if (regPaw != regArginPaw) 
		{
			this.warningBox[3].className = "warning cred";
			this.warningBox[3].innerHTML = "两次输入的密码不一致";
			return false;
		}
		else
		{
				this.warningBox[3].className = "warning cgreen";
				this.warningBox[3].innerHTML = "密码可用";
				return true;
		}
	},
	updateCode:function(obj)
	{
		//getcode(obj);
		this.isSecCode=0;
	},
	postReg:function () {
			var userName = $('regName').value;
			var passWord = $('regPaw').value;
			var passWord2 = $('regArginPaw').value;
			var yzm = $('inpXym').value;
			var sex = 1;
			var channel = 0;
			//var did = $('d_id').value;
			var did = '';
			if(!this.isUserName) {
				$.alert("用户名输入错误!");
				return;
			}else if(passWord=='' || passWord2==''){
				$.alert("密码不能为空!")
				return;
			}else if(!this.isPassWord || !this.checkPassword2()){
				$.alert("密码输入错误!")
				return;
			}else if(yzm == ""){
				$.alert("校证码不能为空!")
				return;
			}
			postData.postReg(userName,passWord,sex,channel,did,yzm,REG.backPostReg);
	},
	
	backPostReg:function(str){												
		if(str.indexOf('succeed') > -1){
			$.alert('<span class="cgreen fb">注册成功！</span>');
			setTimeout(function(){ location.href = "/?c=main" },1800);
		}else{
			$.alert('<span class="cred">'+str.JSONtoObj().msg+'</span>');
		}
	},
	yxmCallback:function(str){
		var obj = str.JSONtoObj();
		if(str.indexOf('succeed') > -1){
			var dtime = 60;
			setInterval(function(){
				$('getYzm').className = "msgXym off";
				$('getYzm').innerHTML = dtime;
				dtime--;
				if(dtime<=0){ $('getYzm').className = "msgXym"; $('getYzm').innerHTML = "免费获取效验码"; return;}
			},1000);
		}else{
			$.alert("发送失败！");
			return;
		}
	}
}


// JavaScript Document