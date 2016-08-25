/*
* 当前版本：全部壹元
* 作   者：刘超（jichao）
*/
(function(window, undefined) {
	var document = window.document;
	var __C = function(spider, handle) {
			return __C.spidering(spider, handle)
		};
	__C.window = window;
	__C.document = document;
	var DEBUG = 0;
	if (DEBUG == 1) {
		__C.window.onerror = function() {
			var errorMsg = "参数个数：" + arguments.length + "个\n";
			errorMsg += "原因：" + arguments[0];
			errorMsg += "\nURL：" + arguments[1];
			errorMsg += "\nLINE：" + arguments[2];
			alert(errorMsg);
			window.onerror = null;
			return true
		};
		__C.timestamp = Math.ceil(new Date().getTime());
		__C.debug = function() {
			var now = Math.ceil(new Date().getTime());
			__C.timestamp = now
		};
		__C.showDebug = function() {
			var now = Math.ceil(new Date().getTime());
			__C.alert((now - __C.timestamp) / 1000)
		}
	}
	trimLeft = /^\s+/;
	trimRight = /\s+$/;
	mobileReg = /^1[34578]\d{9}$/;
	idcardReg = /^([1-9]{0,1})?(\d){1,16}((\d)|x)?$/i;
	tagReg = /^<([0-9A-Za-z]+)>$/i;
	idReg = /^[^\.<=>]+$/;
	nameReg = /^([0-9A-Za-z]+)\s*=\s*([\w\d]+)$/i;
	__C.spiderCache = new Array;
	__C.spidering = function(spider, handle) {
		if (!spider) {
			return false
		}
		this.getDOM = function() {
			var match, i;
			if (this.spider.match(idReg)) {
				return this.handle.getElementById(this.spider)
			} else {
				if (this.spider.match(tagReg)) {
					i = this.handle.getElementsByTagName(this.spider.replace(tagReg, "$1"));
					if (this.spider == "<body>") {
						var isXhtml = (document.documentElement.clientHeight <= document.body.clientHeight && document.documentElement.clientHeight != 0) ? true : false;
						var htmlbody = isXhtml ? document.documentElement : document.body;
						return htmlbody
					} else {
						if (this.spider == "<head>") {
							i = i[0]
						}
					}
					return i
				} else {
					if (match = this.spider.match(nameReg)) {
						var elements = new Array();
						if (document.all) {
							all = this.handle.all;
							for (i = 0; i < all.length; i++) {
								if (all[i][match[1]] == match[2]) {
									elements.push(all[i])
								}
							}
						} else {
							if (match[1] == "name") {
								elements = document.getElementsByName(match[2])
							} else {
								all = this.handle.getElementsByTagName("*");
								for (i = 0; i < all.length; i++) {
									if (all[i][match[1]] == match[2]) {
										elements.push(all[i])
									}
								}
							}
						}
						return elements
					}
				}
			}
		};
		this.init = function(spider, handle) {
			if (!spider) {
				return false
			}
			spider = spider.trim();
			if (spider === "<body>" || !handle) {
				handle = document
			}
			this.spider = spider;
			this.handle = handle
		};
		this.init(spider, handle);
		if (this.spider.length == 0) {
			return false
		}
		return this.getDOM()
	};
	__C.isIE = document.all ? true : false;
	__C.isFF = (navigator.userAgent.indexOf("Firefox") > -1) ? true : false;
	__C.isMobile = function(string) {
		return string.exec(mobileReg)
	};
	__C.isIdcard = function(string) {
		return idcardReg.test(string)
	};
	__C.isArray = function(o) {
		return Object.prototype.toString.call(o) === "[object Array]"
	};
	__C.extend = function(destination, source) {
		for (var property in source) {
			destination[property] = source[property]
		}
	};
	__C.String = window.String;
	__C.String.prototype.trim = function() {
		return this.replace(trimLeft, "").replace(trimRight, "")
	};
	__C.String.prototype.ltrim = function() {
		return this.replace(trimLeft, "")
	};
	__C.String.prototype.rtrim = function() {
		return this.replace(trimRight, "")
	};
	__C.String.prototype.JSONtoObj = function() {
		try {
			return eval("(" + this + ")")
		} catch (e) {
			return e.toString()
		}
	};
	__C.String.prototype.md5 = function() {
		return hex_md5(this)
	};
	__C.String.prototype.toInt = function() {
		return parseInt(this)
	};
	__C.in_array = function(arg, array) {
		for (var s in array) {
			if (array[s] == arg) {
				return true
			}
		}
		return false
	};
	__C.inArray = function(arg, array) {
		return __C.in_array(arg, array)
	};
	__C.arrayRepeat = function(array) {
		var key = new Array();
		var repeat = new Array();
		for (i in array) {
			if (!__C.inArray(array[i], key)) {
				key.push(array[i])
			} else {
				if (!__C.inArray(array[i], repeat)) {
					repeat.push(array[i])
				}
			}
		}
		if (array.length != key.length) {
			return repeat
		}
		return false
	};
	__C.isArray = function(arrayObj) {
		return arrayObj && (typeof arrayObj === "object") && (typeof arrayObj.length === "number") && (typeof arrayObj.splice === "function")
	};
	__C.isUndefined = function(variable) {
		return (typeof variable == "undefined") ? true : false
	};
	__C.threadInstances = new Array();
	__C.singletonThread = function(string) {
		var interval = parseInt(arguments[1]);
		if (__C.threadInstances[string]) {
			clearTimeout(__C.threadInstances[string])
		}
		__C.threadInstances[string] = setTimeout(string, interval)
	};
	__C.intervalHandel = new Array();
	__C.interval = function(string, interval) {
		if (__C.intervalHandel[string]) {
			clearInterval(__C.intervalHandel[string])
		}
		__C.intervalHandel[string] = setInterval(string, interval)
	};
	__C.onlyInt = function(id) {
		id = (typeof id == "string") ? __C(id) : id;
		if (!id) {
			return
		}
		if (typeof(id.onkeyup) == "function") {
			id.oldOnkeyup = id.onkeyup
		}
		id.onkeyup = id.onafterpaste = function() {
			this.value = this.value.replace(/\D/g, "");
			if (this.value == "") {
				this.value = 0
			}
			if (this["oldOnkeyup"]) {
				this.oldOnkeyup()
			}
			if (typeof(this.onchange) == "function") {
				this.onchange()
			}
		}
	};
	__C.onlyString = function(id) {
		if (typeof id != "string") {
			return
		}
		id = this(id);
		if (!id) {
			return
		}
		id.onkeyup = id.onafterpaste = function() {
			this.value = this.value.repalce(/\W/g, "")
		};
		if (typeof(id.onchange) == "function") {
			id.onchange()
		}
	};
	__C.createElement = function(tagName, attributes) {
		var element = document.createElement(tagName);
		if (attributes) {
			for (var name in attributes) {
				if (attributes.hasOwnProperty(name)) {
					if ((name === "class") || (name === "className")) {
						element.className = attributes[name]
					} else {
						if (name === "style") {
							element.style.cssText = attributes[name]
						} else {
							element.setAttribute(name, attributes[name])
						}
					}
				}
			}
		}
		return element
	};
	__C.LoadelementBox = function(e) {
		var ebox = $("LoadelementBox");
		if (!e) {
			return false
		}
		if (!ebox) {
			ebox = document.createElement("DIV");
			ebox.id = "LoadelementBox";
			ebox.style.cssText = "display:block;";
			this.insertBody(ebox)
		}
		ebox.appendChild(e);
		return true
	}, __C.bind = function(element, type, handle) {
		if (element.addEventListener) {
			element.addEventListener(type, handle, false)
		} else {
			if (element.attachEvent) {
				element.attachEvent("on" + type, handle)
			} else {
				element["on" + type] = handle
			}
		}
	};
	__C.unbind = function(element, type, handle) {
		if (element.removeEventListener) {
			element.removeEventListener(type, handle, false)
		} else {
			if (element.detachEvent) {
				element.detachEvent("on" + type, handle)
			} else {
				element["on" + type] = null
			}
		}
	};
	__C.getEvent = function() {
		if (document.all) {
			return window.event
		}
		func = getEvent.caller;
		while (func != null) {
			var arg0 = func.arguments[0];
			if (arg0) {
				if ((arg0.constructor == Event || arg0.constructor == MouseEvent) || (typeof(arg0) == "object" && arg0.preventDefault && arg0.stopPropagation)) {
					return arg0
				}
			}
			func = func.caller
		}
		return null
	};
	__C.getEventTarget = function(e) {
		return e.target || e.srcElement
	};
	__C.getClass = function(name) {
		var classElements = [];
		var allElements = document.getElementsByTagName("*") || document.all;
		for (var i = 0; i < allElements.length; i++) {
			if (allElements[i].className == name) {
				classElements[classElements.length] = allElements[i]
			}
		}
		return classElements
	};
	__C.ready = function(func) {
		var oldonload = window.onload;
		if (typeof window.onload != "function") {
			window.onload = func
		} else {
			window.onload = function() {
				oldonload();
				func()
			}
		}
	};
	__C.reload = function() {
		if (!arguments[1]) {
			if (!arguments[0]) {
				location.reload()
			} else {
				location.assign(arguments[0])
			}
		} else {
			setTimeout(' location.assign("' + arguments[0] + '")', parseInt(arguments[1]) * 1000)
		}
	};
	__C.cookieDomain = "";
	__C.setDomain = function(string) {
		if (!string) {
			return
		}
		__C.document.domain = string;
		__C.cookieDomain = string
	};
	__C.getNameValue = function(name) {
		var doNum = document.getElementsByName(name);
		if (!doNum) {
			return
		}
		var dos = 0;
		for (i = 0; i < doNum.length; i++) {
			if (doNum[i].checked) {
				dos = doNum[i].value;
				return dos
			}
		}
	};
	__C.insertHead = function(obj) {
		document.getElementsByTagName("head")[0].appendChild(obj)
	};
	__C.insertBody = function(obj) {
		document.body.insertBefore(obj, document.body.childNodes[0])
	};
	__C.load = function(url, callBack) {
		this.get(url, "", callBack)
	};
	__C.loadCss = function(url) {
		var css = __C.createElement("link", {
			"href": url,
			"rel": "stylesheet",
			"type": "text/css"
		});
		__C.insertHead(css)
	};
	__C.loadScript = function(url, callback) {
		setTimeout(function() {
			var script = __C.createElement("script", {
				"src": url,
				"type": "text/javascript"
			});
			if (script.readyState) {
				__C.bind(script, "readystatechange", function() {
					if ((script.readyState === "loaded") || (script.readyState === "complete")) {
						if (callback) {
							callback()
						}
						C.unbind(script, "readystatechange", arguments.callee)
					}
				})
			} else {
				__C.bind(script, "load", function() {
					if (callback) {
						callback()
					}
					__C.unbind(script, "load", arguments.callee)
				})
			}
			__C.insertHead(script)
		}, 0)
	};
	__C.setCookie = function() {
		if (arguments.length == 2) {
			var exp = new Date();
			exp.setTime(exp.getTime() + 24 * 60 * 60 * 1000 / 2);
			document.cookie = arguments[0] + "=" + escape(arguments[1]) + ";expires=" + exp.toGMTString() + ";path=/"
		} else {
			if (arguments.length == 3) {
				var exp = new Date();
				exp.setTime(exp.getTime() + arguments[2] * 24 * 60 * 60 * 1000 / 2);
				document.cookie = arguments[0] + "=" + escape(arguments[1]) + ";expires=" + exp.toGMTString() + ";path=/;domain=" + __C.cookieDomain
			} else {
				alert("操作错误！")
			}
		}
	};
	__C.getCookie = function(name) {
		var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
		return (arr != null) ? unescape(arr[2]) : null
	};
	__C.delCookie = function(name) {
		var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval = this.getCookie(name);
		if (cval != null) {
			document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ";path=/;domain=" + __C.cookieDomain
		}
	};
	__C.AJAXINIT = function() {
		this.xml = false;
		this.GetXmlHttp = function() {
			try {
				this.xml = new ActiveXObject("Msxml2.XMLHTTP")
			} catch (e) {
				try {
					this.xml = new ActiveXObject("Microsoft.XMLHTTP")
				} catch (e2) {
					this.xml = false
				}
			}
			if ((!this.xml) && (typeof XMLHttpRequest != "undefined")) {
				this.xml = new XMLHttpRequest()
			}
		};
		this.GetXmlHttp();
		var xmlHttp = this.xml;
		var post = this;
		var callBack = null;
		this.updatePage = function() {
			if (xmlHttp.readyState == 4) {
				var response = xmlHttp.responseText;
				if ((callBack != null) && (typeof callBack == "function")) {
					callBack(response)
				}
			}
		};
		this.toQueryString = function(json) {
			var query = "";
			if (json != null) {
				for (var param in json) {
					query += param + "=" + escape(json[param]) + "&"
				}
			}
			return query
		};
		this.invoke = function(url, params, pageCallBack, method) {
			if (xmlHttp) {
				var query = this.toQueryString(params);
				query = query.substring(0, query.length - 1);
				callBack = pageCallBack;
				if (!url) {
					return
				}
				if ((method != null) && (method.toUpperCase() == "GET")) {
					if (query) {
						if (url.indexOf("?") < 0) {
							url += "?"
						}
						url += "&" + query
					}
					xmlHttp.onreadystatechange = post.updatePage;
					xmlHttp.open("GET", url, true);
					xmlHttp.send(null)
				} else {
					xmlHttp.onreadystatechange = post.updatePage;
					xmlHttp.open("POST", url, true);
					xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
					xmlHttp.send(query)
				}
			}
		}
	};
	__C.getJSON = function(url, params, callBack) {
		var pageCallBack = function(str) {
				callBack(str.JSONtoObj())
			};
		new this.AJAXINIT().invoke(url, params, pageCallBack, "GET")
	};
	__C.ajax = function(url, params, pageCallBack, method) {
		new this.AJAXINIT().invoke(url, params, pageCallBack, method)
	};
	__C.get = function(url, params, callBack) {
		new this.AJAXINIT().invoke(url, params, callBack)
	};
	__C.post = function(url, params, callBack) {
		new this.AJAXINIT().invoke(url, params, callBack, "POST")
	};
	__C.show = function() {
		if (!arguments[0]) {
			return false
		}
		arguments[0].style.display = "block"
	};
	__C.hide = function() {
		var element = arguments[0];
		var timeout = arguments[1];
		switch (timeout) {
		case "fast":
			timeout = 0.5;
			break;
		case "normal":
			timeout = 2;
			break;
		case "slow":
			timeout = 5;
			break;
		default:
			timeout = parseInt(timeout);
			break
		}
		if (timeout == 0) {
			element.style.display = "none"
		} else {
			setTimeout("$('" + element.id + "').style.display='none'", timeout * 1000)
		}
	}, __C.door = function(c) {
		new slidingDoors(c)
	};
	__C.getWH = function() {
		var s;
		var r = {};
		if (!arguments[0]) {
			s = this("<body>")
		} else {
			s = arguments[0]
		}
		r.width = Math.max(s.scrollWidth, s.clientWidth);
		r.height = Math.max(s.scrollHeight, s.clientHeight);
		return r
	}, __C.fmoney = function(s, n, data) {
		var i, r, l, t;
		n = ((n > 0) && (n <= 20)) ? n : 2;
		s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
		l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
		t = "";
		for (i = 0; i < l.length; i++) {
			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "")
		}
		r = t.split("").reverse().join("") + "." + r;
		if (typeof data == "string") {
			r = data + r
		}
		return r
	};
	__C.showCover = function() {
		window.focus();
		coverDIV.show()
	};
	__C.hideCover = function() {
		coverDIV.hide()
	};
	__C.showDIV = function() {
		if ((!arguments[0]) || (!arguments[1])) {
			return
		}
		if (!doIframe[arguments[0]]) {
			var a = function() {
					var c = arguments.callee;
					doIframe.init(c.str, c.type, c.width, c.height)
				};
			a.self = arguments;
			a.str = arguments[1];
			a.type = arguments[2];
			a.width = arguments[3];
			a.height = arguments[4];
			doIframe[arguments[0]] = a
		}
		if (doIframe[arguments[0]]) {
			doIframe[arguments[0]]()
		}
	};
	__C.hideDIV = function() {
		doIframe.hide()
	};
	__C.alert = function(string) {
		__C.messageBox.show(string)
	};
	__C.copyLink = function(content) {
		if (window.clipboardData) {
			if (typeof(content) == "object") {
				content = content.value
			}
			if (window.clipboardData.setData("Text", content)) {
				__C.alert("你已复制成功!")
			}
		} else {
			if (typeof(content) == "object") {
				content.focus();
				content.select()
			}
		}
	};
	__C.getLocTime = function(nS) {
		return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日|六|七|一|二|三|四|五|星期/g, "")
	};
	__C.getNameValue = function(name) {
		var i;
		var dos = 0;
		var doNum = __C("name=" + name);
		if (!doNum) {
			return
		}
		for (i = 0; i < doNum.length; i++) {
			if (doNum[i].checked == true) {
				return doNum[i].value
			}
		}
	};
	var hexcase = 0;
	var b64pad = "";
	var chrsz = 8;
	hex_md5 = function(s) {
		return binl2hex(core_md5(str2binl(s), s.length * chrsz))
	};
	core_md5 = function(x, len) {
		x[len >> 5] |= 128 << ((len) % 32);
		x[(((len + 64) >>> 9) << 4) + 14] = len;
		var a = 1732584193;
		var b = -271733879;
		var c = -1732584194;
		var d = 271733878;
		for (var i = 0; i < x.length; i += 16) {
			var olda = a;
			var oldb = b;
			var oldc = c;
			var oldd = d;
			a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936);
			d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586);
			c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
			b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330);
			a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897);
			d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
			c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341);
			b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983);
			a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
			d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417);
			c = md5_ff(c, d, a, b, x[i + 10], 17, -42063);
			b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162);
			a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
			d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101);
			c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290);
			b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329);
			a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510);
			d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632);
			c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
			b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302);
			a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691);
			d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
			c = md5_gg(c, d, a, b, x[i + 15], 14, -660478335);
			b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848);
			a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
			d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690);
			c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961);
			b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
			a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467);
			d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784);
			c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
			b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734);
			a = md5_hh(a, b, c, d, x[i + 5], 4, -378558);
			d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463);
			c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
			b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556);
			a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060);
			d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
			c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632);
			b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640);
			a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
			d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222);
			c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979);
			b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
			a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487);
			d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835);
			c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520);
			b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651);
			a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844);
			d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
			c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905);
			b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055);
			a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
			d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606);
			c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523);
			b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799);
			a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
			d = md5_ii(d, a, b, c, x[i + 15], 10, -30611744);
			c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380);
			b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
			a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070);
			d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379);
			c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
			b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551);
			a = safe_add(a, olda);
			b = safe_add(b, oldb);
			c = safe_add(c, oldc);
			d = safe_add(d, oldd)
		}
		return Array(a, b, c, d)
	};
	md5_cmn = function(q, a, b, x, s, t) {
		return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b)
	};
	md5_ff = function(a, b, c, d, x, s, t) {
		return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t)
	};
	md5_gg = function(a, b, c, d, x, s, t) {
		return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t)
	};
	md5_hh = function(a, b, c, d, x, s, t) {
		return md5_cmn(b ^ c ^ d, a, b, x, s, t)
	};
	md5_ii = function(a, b, c, d, x, s, t) {
		return md5_cmn(c ^ (b | (~d)), a, b, x, s, t)
	};
	core_hmac_md5 = function(key, data) {
		var bkey = str2binl(key);
		if (bkey.length > 16) {
			bkey = core_md5(bkey, key.length * chrsz)
		}
		var ipad = Array(16),
			opad = Array(16);
		for (var i = 0; i < 16; i++) {
			ipad[i] = bkey[i] ^ 909522486;
			opad[i] = bkey[i] ^ 1549556828
		}
		var hash = core_md5(ipad.concat(str2binl(data)), 512 + data.length * chrsz);
		return core_md5(opad.concat(hash), 512 + 128)
	};
	safe_add = function(x, y) {
		var lsw = (x & 65535) + (y & 65535);
		var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
		return (msw << 16) | (lsw & 65535)
	};
	bit_rol = function(num, cnt) {
		return (num << cnt) | (num >>> (32 - cnt))
	};
	str2binl = function(str) {
		var bin = Array();
		var mask = (1 << chrsz) - 1;
		for (var i = 0; i < str.length * chrsz; i += chrsz) {
			bin[i >> 5] |= (str.charCodeAt(i / chrsz) & mask) << (i % 32)
		}
		return bin
	};
	binl2hex = function(binarray) {
		var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
		var str = "";
		for (var i = 0; i < binarray.length * 4; i++) {
			str += hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8 + 4)) & 15) + hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8)) & 15)
		}
		return str
	};
	var slidingDoors = function(c) {
			this.id = c["id"];
			this.on = " " + c["on"];
			this.off = " " + c["off"];
			this.mouse = c["mouse"];
			this.div = c["tagname"] ? c["tagname"] : "DIV";
			this.loop = c["loop"] ? c["loop"] : false;
			this.intval = c["intval"] ? parseInt(c["intval"]) : 2;
			this.j = $(this.id).getElementsByTagName(this.div);
			this.count = this.j.length;
			this.slidingDoor = function() {
				var e, i = 0;
				for (var k = 0; k < this.j.length; k++) {
					e = this.j[k];
					e.next = (k < this.j.length - 1) ? this.j[k + 1] : this.j[0];
					e.aid = this.id;
					e.id = this.id + "_door_" + k;
					e.parents = this.j;
					e.on = this.on;
					e.off = this.off;
					e.div = this.div;
					e.count = this.count - 1;
					e.intval = this.intval;
					e.loop = this.loop;
					e.brothers = $(e.aid).getElementsByTagName(e.div);
					e.brother = $(e.getAttribute("value"));
					e.oldClassName = (e.className.trim() && e.className.trim() != this.on.trim()) ? e.className : false;
					e.mouse = function() {
						for (var i = 0; i < this.parents.length; i++) {
							if (this.parents[i] == "length") {
								continue
							}
							if (this.parents[i] == this) {
								this.className = (this.oldClassName) ? (this.oldClassName + this.on) : this.on;
								this.parents[i].brother.style.display = ""
							} else {
								this.parents[i].className = (this.parents[i].oldClassName) ? (this.parents[i].oldClassName + this.parents[i].off) : this.parents[i].off;
								this.parents[i].brother.style.display = "none"
							}
						}
						if (arguments[0] == "loop" && this.loop) {
							setTimeout("$('" + this.next.id + "').mouse('loop');", this.intval * 1000)
						}
					};
					if (!this.mouse) {
						e.onmouseover = e.mouse
					} else {
						e.onclick = e.mouse
					}
				}
				if (this.loop) {
					setTimeout("$('" + this.id + "_door_" + "0" + "').mouse('loop');", this.intval * 1000)
				}
			};
			this.slidingDoor()
		};
	var doIframe = {
		fId: "Load_Iframe",
		dId: "Load_Iframe_Div",
		biId: "Load_Iframe_Main",
		bId: "Load_Iframe_Border",
		divs: new Array(),
		fwidth: 0,
		fheight: 0,
		position: null,
		top: 0,
		init: function() {
			if (this.divs.length == 0) {}
			var i;
			var param = arguments[0] ? arguments[0] : "";
			var showtype = arguments[1] ? arguments[1] : "DIV";
			var fwidth = arguments[2] ? arguments[2] : 700;
			var fheight = arguments[3] ? arguments[3] : 370;
			this.fwidth = fwidth;
			this.fheight = fheight;
			var bodyWidth = $.getWH();
			var bodyWidth = bodyWidth.width;
			var biWidth = fwidth;
			var biHeight = fheight;
			var biLeft = window.screen.width / 2 - fwidth / 2;
			this.top = __C("<body>").scrollTop;
			var biTop = this.top + window.screen.height / 2 - fheight / 2 - 50;
			var position = "absolute";
			if (this.top == 0 && !__C.isIE6()) {
				position = "fixed"
			}
			this.position = position;
			var bWidth = fwidth + 12;
			var bHeight = fheight + 14;
			var bLeft = biLeft - 5;
			var bTop = biTop - 5;
			var f = __C(this.fId);
			var d = __C(this.dId);
			var bi = __C(this.biId);
			var b = __C(this.bId);
			__C.showCover(1);
			if (!f) {
				b = document.createElement("DIV");
				bi = document.createElement("DIV");
				bi.id = this.biId;
				b.id = this.bId;
				bi.innerHTML = '<iframe style="overflow:hidden" scrolling="no" frameborder="0" id="Load_Iframe" ></iframe><div style="" id="Load_Iframe_Div" ></div>';
				bi.style.cssText = "position:" + position + ";width:0px;height:0px; background:#fff; z-index:100001; top:100px; left:100px;border-radius:5px; box-shadow:0px 0px 5px #999; ";
				b.style.cssText = "position:" + position + "; background:#DBF3FD; z-index:100000; top:100px; left:100px;opacity:0; -moz-opacity:0; filter:alpha(opacity=0);";
				__C.LoadelementBox(b);
				__C.LoadelementBox(bi);
				f = __C(this.fId);
				d = __C(this.dId);
				if (f.attachEvent) {
					f.attachEvent("onload", function() {
						__C.showCover(2)
					})
				} else {
					f.onload = function() {
						__C.showCover(2)
					}
				}
			}
			bi.style.width = biWidth + "px";
			bi.style.height = biHeight + "px";
			bi.style.top = biTop + "px";
			bi.style.left = biLeft + "px";
			b.style.width = bWidth + "px";
			b.style.height = bHeight + "px";
			b.style.left = bLeft + "px";
			b.style.top = bTop + "px";
			f.style.width = d.style.width = fwidth + "px";
			f.style.height = d.style.height = fheight + "px";
			if (showtype == "DIV") {
				var md5 = "Load_Iframe_Div_" + param.md5();
				var di;
				for (i = 0; i < this.divs.length; i++) {
					if (this.divs[i].id == md5) {
						di = this.divs[i]
					}
					this.divs[i].style.display = "none"
				}
				if (!di) {
					di = document.createElement("DIV");
					di.innerHTML = param;
					di.id = md5;
					if (d.childNodes[0]) {
						d.insertBefore(di, d.childNodes[0])
					} else {
						d.appendChild(di)
					}
					this.divs.push(di)
				}
				di.style.display = "";
				d.style.display = "";
				f.style.display = "none"
			} else {
				f.src = param;
				d.style.display = "none";
				f.style.display = ""
			}
			this.show();
			return
		},
		interval: null,
		show: function() {
			__C(this.biId).style.display = "";
			__C(this.bId).style.display = "";
			if (this.position == "absolute") {
				this.interval = setInterval(function() {
					if (doIframe.top == __C("<body>").scrollTop) {
						return
					}
					var bodyWidth = $.getWH();
					var bodyWidth = bodyWidth.width;
					var biWidth = doIframe.fwidth;
					var biHeight = doIframe.fheight;
					var biLeft = window.screen.width / 2 - doIframe.fwidth / 2;
					doIframe.top = __C("<body>").scrollTop;
					var biTop = doIframe.top + window.screen.height / 2 - doIframe.fheight / 2 - 50;
					var bWidth = doIframe.fwidth + 12;
					var bHeight = doIframe.fheight + 14;
					var bLeft = biLeft - 5;
					var bTop = biTop - 5;
					var f = __C(doIframe.fId);
					var d = __C(doIframe.dId);
					var bi = __C(doIframe.biId);
					var b = __C(doIframe.bId);
					bi.style.width = biWidth + "px";
					bi.style.height = biHeight + "px";
					bi.style.top = biTop + "px";
					bi.style.left = biLeft + "px";
					b.style.width = bWidth + "px";
					b.style.height = bHeight + "px";
					b.style.left = bLeft + "px";
					b.style.top = bTop + "px";
					f.style.width = d.style.width = doIframe.fwidth + "px";
					f.style.height = d.style.height = doIframe.fheight + "px"
				}, 1000)
			}
		},
		hide: function() {
			if (this.interval) {
				clearInterval(this.interval)
			}
			if (__C(this.biId)) {
				__C(this.biId).style.display = "none"
			}
			if (__C(this.bId)) {
				__C(this.bId).style.display = "none"
			}
			__C.hideCover()
		}
	};
	__C.isIE7 = function() {
		if (navigator.appName != "Microsoft Internet Explorer") {
			return false
		}
		if (navigator.appVersion.match(/7./i) == "7.") {
			return true
		}
		return false
	};
	__C.isIE6 = function() {
		if (navigator.appName != "Microsoft Internet Explorer") {
			return false
		}
		if (navigator.appVersion.match(/6./i) == "6.") {
			return true
		}
		return false
	};
	var coverDIV = {
		show: function() {
			var z = this.load();
			var r = __C.getWH();
			z.style.width = r.width + "px";
			z.style.height = r.height + "px";
			z.style.display = ""
		},
		hide: function() {
			this.load().style.display = "none"
		},
		load: function() {
			var z = __C("Load_Zhezhaoceng");
			if (!z) {
				z = document.createElement("DIV");
				z.id = "Load_Zhezhaoceng";
				z.style.cssText = "position:absolute; width:100%; height:100%; background:#333; z-index:99999; top:0; left:0; opacity:0.2; -moz-opacity:0.2; filter:alpha(opacity=20);";
				__C.LoadelementBox(z);
				z = __C("Load_Zhezhaoceng")
			}
			return z
		}
	};
	__C.alert = function(ar1, ar2, ar3, ar4) {
		__C.messageBox.arg1 = arguments[0];
		__C.messageBox.arg2 = arguments[1];
		__C.messageBox.arg3 = arguments[2];
		__C.messageBox.arg4 = arguments[3];
		__C.messageBox.show(ar1, ar2, ar3, ar4)
	};
	__C.messageBox = {
		show: function() {
			if ($("loginBoxWrap")) {
				$("loginBoxWrap").style.display = "none";
				$("loginBoxPublic").style.display = "none"
			}
			var message = arguments[0];
			var subject = arguments[1];
			if (!subject) {
				subject = "温馨提示"
			}
			if (!message) {
				message = ""
			}
			if (__C.messageBox.arg3 && __C.messageBox.arg3 == true) {
				var btnStr = '<input style="float:left;width:50%;height:50px; line-height:50px;font-size:18px; text-align:center; font-family:\'Microsoft YaHei\',\'宋体\';border:none; background:none;" type="button" id="msgSureBtn" class="cred" value="确定" onclick="$.messageBox.hideFun();" />' + '<input style="float:left;width:50%;height:50px; line-height:50px;font-size:18px; text-align:center; font-family:\'Microsoft YaHei\',\'宋体\';border:none; background:none;" type="button" id="msgClearBtn" class="cblue" value="取消" onclick="$.messageBox.hide();" />'
			} else {
				var btnStr = '<input style="float:left;width:100%;height:50px; line-height:50px;font-size:18px; text-align:center; font-family:\'Microsoft YaHei\',\'宋体\';border:none; background:none;" type="button" id="msgSureBtn" class="cblue" value="确定" onclick="$.messageBox.hideFun();" />'
			}
			if (!$("showMsgKeepout")) {
				var b = document.createElement("DIV");
				var bb = document.createElement("DIV");
				b.id = "showMsgKeepout";
				b.style.cssText = "position:fixed; top:0; left:0; width:100%; height:100%;z-index:99;background:#333; opacity:0.5;";
				bb.id = "showWinMsg";
				bb.style.cssText = "position:fixed; top:30%; left:50%;z-index:9999;width:280px;margin:0 0 0 -140px;background:#fff;border-radius:4%;overflow:hidden;";
				bb.innerHTML = '<dl><dt class="" style="color:#666; font-size:16px;text-align:center;height:60px; line-height:60px;"></dt>' + '<dd style="line-height:30px;color:#333; font-size:16px;padding:0 60px; text-align:center;"></dd></dl>' + '<div class="showMsgBtn" id="showMsgBtnBox" style="width:100%;height:50px; border-top:1px solid #EEEFEF;margin-top:10px;">' + btnStr + "</div>";
				$("<body>").appendChild(b);
				$("<body>").appendChild(bb)
			} else {
				$("showMsgKeepout").style.display = "";
				$("showWinMsg").style.display = "";
				$("showMsgBtnBox").innerHTML = btnStr
			}
			$("<dd>", $("showWinMsg"))[0].innerHTML = message;
			$("<dt>", $("showWinMsg"))[0].innerHTML = subject
		},
		hideFun: function() {
			if ($("showMsgKeepout")) {
				$("showMsgKeepout").style.display = "none";
				$("showWinMsg").style.display = "none"
			}
			if (__C.messageBox.arg4 != null) {
				if (typeof(__C.messageBox.arg4) == "function") {
					__C.messageBox.arg4()
				}
			}
		},
		hide: function() {
			if ($("showMsgKeepout")) {
				$("showMsgKeepout").style.display = "none";
				$("showWinMsg").style.display = "none"
			}
		}
	};
	__C.apply = function(object, fun) {
		return function() {
			return fun.apply(object, arguments)
		}
	};
	__C.each = function(list, fun) {
		for (var i = 0, len = list.length; i < len; i++) {
			fun(list[i], i)
		}
	};
	__C.upload = function() {
		return new fileUpload(arguments[0], arguments[1], arguments[2])
	};
	var fileUpload = function() {
			this.initialize.apply(this, arguments)
		};
	fileUpload.prototype = {
		initialize: function(form, folder, options) {
			this.Form = __C(form);
			this.Folder = __C(folder);
			this.Files = [];
			this.SetOptions(options);
			this.FileName = this.options.FileName;
			this._FrameName = this.options.FrameName;
			this.Limit = this.options.Limit;
			this.Distinct = !! this.options.Distinct;
			this.ExtIn = this.options.ExtIn;
			this.ExtOut = this.options.ExtOut;
			this.onIniFile = this.options.onIniFile;
			this.onEmpty = this.options.onEmpty;
			this.onNotExtIn = this.options.onNotExtIn;
			this.onExtOut = this.options.onExtOut;
			this.onLimite = this.options.onLimite;
			this.onSame = this.options.onSame;
			this.onFail = this.options.onFail;
			this.onIni = this.options.onIni;
			if (!this._FrameName) {
				this._FrameName = "uploadFrame_" + Math.floor(Math.random() * 1000);
				var oFrame = __C.isIE ? document.createElement('<iframe name="' + this._FrameName + '">') : document.createElement("iframe");
				oFrame.name = this._FrameName;
				oFrame.style.display = "none";
				document.body.insertBefore(oFrame, document.body.childNodes[0])
			}
			this.Form.target = this._FrameName;
			this.Form.method = "post";
			this.Form.encoding = "multipart/form-data";
			this.Ini()
		},
		SetOptions: function(options) {
			this.options = {
				FileName: "Files[]",
				FrameName: "",
				onIniFile: function() {},
				onEmpty: function() {},
				Limit: 10,
				onLimite: function() {},
				Distinct: true,
				onSame: function() {},
				ExtIn: ["gif", "jpg", "rar", "zip", "iso", "swf", "exe", "txt"],
				onNotExtIn: function() {},
				ExtOut: [],
				onExtOut: function() {},
				onFail: function() {},
				onIni: function() {}
			};
			__C.extend(this.options, options || {})
		},
		Ini: function() {
			this.Files = [];
			__C.each(this.Folder.getElementsByTagName("input"), __C.apply(this, function(o) {
				if (o.type == "file") {
					o.value && this.Files.push(o);
					this.onIniFile(o)
				}
			}));
			var file = document.createElement("input");
			file.name = this.FileName;
			file.type = "file";
			file.onchange = __C.apply(this, function() {
				if (this.options.isSingleton && this.Files.length > 0) {
					this.Clear();
					this.Files = []
				}
				var s = this.Check(file);
				this.Ini();
				if (this.options.isSingleton && s) {
					this.Form.submit()
				}
			});
			this.Folder.appendChild(file);
			this.onIni()
		},
		Check: function(file) {
			var bCheck = true;
			if (!file.value) {
				bCheck = false;
				this.onEmpty()
			} else {
				if (this.Limit && (this.Files.length >= this.Limit)) {
					bCheck = false;
					this.onLimite()
				} else {
					if (( !! this.ExtIn.length) && !RegExp(".(" + this.ExtIn.join("|") + ")$", "i").test(file.value)) {
						bCheck = false;
						this.onNotExtIn()
					} else {
						if ( !! this.ExtOut.length && RegExp(".(" + this.ExtOut.join("|") + ")$", "i").test(file.value)) {
							bCheck = false;
							this.onExtOut()
						} else {
							if ( !! this.Distinct) {
								__C.each(this.Files, function(o) {
									if (o.value == file.value) {
										bCheck = false
									}
								});
								if (!bCheck) {
									this.onSame()
								}
							}
						}
					}
				}
			}
			if (!bCheck) {
				this.onFail(file)
			}
			return bCheck
		},
		Delete: function(file) {
			this.Folder.removeChild(file);
			this.Ini()
		},
		Clear: function() {
			__C.each(this.Files, __C.apply(this, function(o) {
				this.Folder.removeChild(o)
			}));
			this.Ini()
		}
	};
	window.__C = window.$ = __C
})(window);

function updateCode(id) {
	var code = $(id);
	if (code) {
		code.src = "/?c=api_user&a=seccode&rand=" + Math.random()
	}
}
function pageShare(webSite, t) {
	var shareUrl = "";
	var d = document;
	var u = location.href;
	var des = d.selection ? (d.selection.type != "None" ? d.selection.createRange().text : "") : (d.getSelection ? d.getSelection() : "");
	var esTitle = escape(t);
	var esHref = escape(u);
	var esDes = escape(des);
	var ecTitle = encodeURIComponent(t);
	var ecHref = encodeURIComponent(u);
	var ecDes = encodeURIComponent(des);
	switch (webSite) {
	case "qq":
		shareUrl = "http://shuqian.qq.com/post?from=3&title=" + ecTitle + "&uri=" + ecHref + "&jumpback=2&noui=1";
		break;
	case "baidu":
		shareUrl = "http://cang.baidu.com/do/add?it=" + ecTitle + "&iu=" + ecHref + "&dc=" + ecDes + "&fr=ien#nw=1";
		break;
	case "sina":
		shareUrl = "http://v.t.sina.com.cn/share/share.php?title=" + ecTitle + "&url=" + ecHref + "&rcontent=" + ecDes;
		break;
	case "kaixin":
		shareUrl = "http://www.kaixin001.com/~repaste/repaste.php?&rurl=" + esHref + "&rtitle=" + esTitle + "&rcontent=" + esTitle;
		break;
	case "sohu":
		shareUrl = "http://bai.sohu.com/share/blank/add.do?link=" + esHref;
		break;
	case "douban":
		shareUrl = "http://www.douban.com/recommend/?url=" + ecHref + "&title=" + ecTitle + "&comment=" + ecDes;
		break;
	case "renren":
		shareUrl = "http://share.renren.com/share/buttonshare.do?link=" + ecHref + "&title=" + ecTitle;
		break
	}
	window.open(shareUrl, "favit", "")
}
var postData = {
	load: function(model, action, params, callback) {
		var url = "/?c=api_" + model + "&a=" + action + "&callType=JSON";
		$.post(url, params, callback)
	},
	checkUsername: function() {
		this.load("user", "checkUsername", {
			"username": arguments[0]
		}, arguments[1])
	},
	checkEmail: function() {
		this.load("user", "checkEmail", {
			"email": arguments[0]
		}, arguments[1])
	},
	checkSeccode: function() {
		this.load("user", "checkCode", {
			"code": arguments[0]
		}, arguments[1])
	},
	postReg: function() {
		this.load("user", "reg", {
			"username": arguments[0],
			"password": arguments[1],
			"sex": arguments[2],
			"channel": arguments[3],
			"d_id": arguments[4],
			"number": arguments[5]
		}, arguments[6])
	},
	postLogin: function() {
		this.load("user", "login", {
			"username": arguments[0],
			"password": arguments[1],
			"channel": arguments[2]
		}, arguments[3])
	},
	postLogout: function() {
		this.load("user", "logout", {}, arguments[0])
	},
	updateinfo: function() {
		this.load("user", "updateinfo", {
			"type": "updateinfo",
			"sex": arguments[0],
			"year": arguments[1],
			"month": arguments[2],
			"day": arguments[3],
			"province": arguments[4],
			"city": arguments[5],
			"address": arguments[6],
			"realname": arguments[7]
		}, arguments[8])
	},
	updatepassword: function() {
		this.load("user", "updateinfo", {
			"type": "updatepassword",
			"loginpassword": arguments[0],
			"newpassword": arguments[1],
			"querypassword": arguments[2]
		}, arguments[3])
	},
	updatemoneypassword: function() {
		this.load("user", "updateinfo", {
			"type": "updatemoneypassword",
			"password": arguments[0],
			"newpassword": arguments[1]
		}, arguments[2])
	},
	lastpwsubject: "",
	lastpwmessage: "",
	postPw: function() {
		if ((this.lastpwsubject == arguments[1]) && (this.lastpwmessage == arguments[2])) {} else {
			this.lastpwsubject = arguments[1];
			this.lastpwmessage = arguments[2];
			this.load("user", "postpw", {
				"subject": arguments[0],
				"message": arguments[1]
			}, arguments[2])
		}
	},
	checkLogin: function() {
		this.load("user", "checkLogin", {}, arguments[0])
	},
	rebatesTixian: function(model, action, params, callback) {
		var url = "/?c=" + model + "&a=" + action + "&callType=JSON";
		$.post(url, params, callback)
	},
	checkCommission: function(model, action, params, callback) {
		var url = "/?c=" + model + "&a=" + action + "&callType=JSON";
		$.post(url, params, callback)
	}
};
var pageData = {
	init: function(target) {
		$.extend(this, target);
		$.setDomain(this.rooturl)
	},
	rooturl: "",
	homeurl: "",
	spaceurl: "",
	sitename: "",
	staticurl: "",
	islogin: 0,
	uid: 0,
	pm: 0,
	username: null,
	lastlogin: 0,
	money: 0
};
$.showLogin = function() {
	publicLoginBox.show()
};
var Member = {
	loginReload: true,
	logoutReload: true,
	login: function() {
		var username = $("yygLogName").value;
		var password = $("yygLogPaw").value;
		if (username.trim() == "") {
			$.alert("手机号/用户名/邮箱 不能为空！");
			return
		}
		if (password.trim() == "") {
			$.alert("密码不能为空！");
			return
		}
		var loginuser = $("__usermsg");
		if (loginuser) {
			loginuser.innerHTML = "正在发送登陆数据...."
		}
		postData.postLogin(username, password, 0, this.backLogin);
		$.hideDIV()
	},
	backLogin: function(str) {
		if (str.indexOf("succeed") > -1) {
			if (Member.loginReload) {
				$.reload();
				return
			}
		} else {
			$.alert(str.JSONtoObj().msg);
			setTimeout("$.showLogin();", 1000)
		}
		Member.showMember()
	},
	logout: function() {
		postData.postLogout(this.backLogout);
		$.delCookie("__AUTHLOGIN")
	},
	backLogout: function() {
		$.reload();
		return;
		Member.showMember();
		Member._after()
	},
	noRefresh: function(fun) {
		this.logoutReload = false;
		this._after = fun
	},
	_after: function() {},
	showMember: function() {
		pageData.isLogined = 0;
		if ($.getCookie("__AUTHLOGIN")) {
			var loginuser = $("__usermsg");
			if (loginuser) {
				loginuser.innerHTML = "<span class='cfff fz14' style='display:block; height:100px; line-height:100px; margin-left:50px;'>正在检查登陆....</span>"
			}
			postData.checkLogin(this.backCheckLogin)
		} else {
			this.showMemeberMsg()
		}
		$.bind(document, "keypress", Member.isLoginSubmit)
	},
	isLoginSubmit: function(e) {
		if ((e.keyCode == 13) && $("yygLogName") && ($("yygLoginBox").style.display == "block")) {
			Member.login()
		}
	},
	showMemeberMsg: function() {
		var s = "";
		var mR = "";
		var loginuser = $("topBarInnerBox");
		if (!loginuser) {
			return
		}
		if (pageData.isLogined) {
			s = '<div class="tapBar_l_down" id="tapBar_l_down"><span></span><a href="#" class="">手机云购</a><img class="topBarEwm" id="topBarEwm" src="App/Views/Public/images/13.png" style="display:none;" /></div><div class="tapBar_r"><a href="/?c=user_main" class="login_username">' + pageData.username + '</a><a href="#" onclick="Member.logout();" style="margin-left:0;">[退出]</a><span class="tapBar_bor"></span><a href="/?c=user_main" class="">我的壹元购</a><span class="tapBar_bor"></span><a href="/?c=user_recharge" class="">充值</a><span class="tapBar_bor"></span><a href="/?c=help" class="">帮助</a><span class="tapBar_bor"></span><a href="/?c=main" class="cred">首页</a></div>'
		} else {
			s += '<span class="tapBar_l">欢迎来到全部壹元！</span><div class="tapBar_r"><a href="#" class="login cred" onclick="publicLoginBox.show()">请登录</a><a href="/?c=member&a=reg" class="reg">免费注册</a><span class="tapBar_bor">';
			s += '</span><a href="/?c=main&a=phone" class="download">下载客户端</a><span class="tapBar_bor"></span><a href="/?c=help" class="help">帮助中心</a></div>'
		}
		loginuser.innerHTML = s;
		if ($("userMailNum")) {
			$("userMailNum").onclick = function() {
				$("userMailTop").click()
			};
			pageData.newpmsnum <= 0 ? $("userMailNum").style.display = "none" : $("userMailNum").style.display = ""
		}
	},
	backCheckLogin: function(string) {
		var obj = string.JSONtoObj();
		pageData.isLogined = obj.islogin;
		if (obj.petname) {
			pageData.username = obj.petname
		} else {
			pageData.username = obj.username
		}
		pageData.lastlogin = obj.lastlogin;
		pageData.newpmsnum = obj.data.npwnum;
		pageData.money = obj.data.money;
		pageData.uid = obj.data.uid;
		pageData.img = obj.data.head;
		pageData.no_settlement = obj.data.no_settlement;
		pageData.tracking = obj.data.tracking;
		pageData.isTrader = obj.is_trader;
		Member.showMemeberMsg()
	}
};

function addfavorite(sURL, sTitle) {
	try {
		window.external.addFavorite("" + sURL + "", "" + sTitle + "")
	} catch (e) {
		try {
			if (window.sidebar) {
				window.sidebar.addPanel("" + sTitle + "", "" + sURL + "", "")
			} else {
				alert("加入收藏失败，该浏览器不支持自动加入收藏功能，请使用Ctrl+D进行添加。")
			}
		} catch (e) {
			alert("加入收藏失败，该浏览器不支持自动加入收藏功能，请使用Ctrl+D进行添加。")
		}
	}
}
var $j = jQuery.noConflict();
var showTime = {
	StartTime: 0,
	EndTime: 0,
	objArr: null,
	timeArr: null,
	fun: "",
	nM: "",
	nS: "",
	nMS: "",
	type: 1,
	isDjs : false,
	init: function(end, obj, type) {
		this.objArr = obj;
		this.timeArr = end;
		this.StartTime = new Date();
		if (type) {
			this.type = type
		} else {
			this.type = 1
		}
	},
	show: function() {
		if (this.type == 1) {
			for (var kk = 0; kk < this.objArr.length; kk++) {
				var tag = this.objArr[kk];
				var nMS = this.timeArr[kk];
				var isZore = nMS;
				var nH = this.addZore(parseInt(nMS / 3600));
				nMS = nMS % 3600;
				var nM = this.addZore(parseInt(nMS / 60));
				nMS = nMS % 60;
				var nS = this.addZore(nMS);
				if (Number(isZore) <= 0) {
					tag.innerHTML = "00:00:00"
				} else {
					tag.innerHTML = nH + ":" + nM + ":" + nS;
					this.timeArr[kk]--
				}
			}
			setTimeout(function() {
				showTime.show()
			}, 1000)
		} else {
			if (this.type == 2) {
				var tag = this.objArr;
				var nMS = this.timeArr;
				var isZore = nMS;
				var nH = this.addZore(parseInt(nMS / 3600));
				nMS = nMS % 3600;
				var nM = this.addZore(parseInt(nMS / 60));
				nMS = nMS % 60;
				var nS = this.addZore(nMS);
				if (Number(isZore) <= 0) {
					tag.innerHTML = "00:00:00";
					return
				} else {
					tag.innerHTML = nH + ":" + nM + ":" + nS;
					this.timeArr--
				}
				setTimeout(function() {
					showTime.show()
				}, 1000)
			} else {
				if (this.type == 3) {
					for (var kk = 0; kk < this.objArr.length; kk++) {
						var tag = this.objArr[kk];
						var nMS = this.timeArr[kk];
						var isZore = nMS;
						var nH = this.addZore(parseInt(nMS / 3600));
						nMS = nMS % 3600;
						var nM = this.addZore(parseInt(nMS / 60));
						nMS = nMS % 60;
						var nS = this.addZore(nMS);
						if (Number(isZore) <= 0) {
							var aSpan = $("<span>", tag);
							aSpan[0].innerHTML = "00";
							aSpan[1].innerHTML = "00";
							aSpan[2].innerHTML = "00";
							location.reload();
							return
						} else {
							var aSpan = $("<span>", tag);
							aSpan[0].innerHTML = nH;
							aSpan[1].innerHTML = nM;
							aSpan[2].innerHTML = nS;
							//this.timeArr[kk]--
						}
					}
					if(this.isDjs == false){
						this.isDjs = true;
						setInterval(function() {
							var djs_obj = $j('.daojishi');
							jQuery.each(djs_obj, function(i){
								var tag = djs_obj[i].parentNode;
								var nMS = djs_obj[i].value;
								nMS--;
								var isZore = nMS;
								var nH = showTime.addZore(parseInt(nMS / 3600));
								nMS = nMS % 3600;
								var nM = showTime.addZore(parseInt(nMS / 60));
								nMS = nMS % 60;
								var nS = showTime.addZore(nMS);
								if (Number(isZore) <= 0) {
									var aSpan = $("<span>", tag);
									aSpan[0].innerHTML = "00";
									aSpan[1].innerHTML = "00";
									aSpan[2].innerHTML = "00";
									location.reload();
									return
								} else {
									var aSpan = $("<span>", tag);
									aSpan[0].innerHTML = nH;
									aSpan[1].innerHTML = nM;
									aSpan[2].innerHTML = nS;
									//this.timeArr[kk]--
									djs_obj[i].value = isZore;
								}
							});
						}, 1000)
					}
				}
			}
		}
	},
	addZore: function(number) {
		if (number < 10) {
			number = "0" + number
		}
		number = number.toString();
		return number
	}
};
var $get = function(id) {
		return "string" == typeof id ? document.getElementById(id) : id
	};
var Class = {
	create: function() {
		return function() {
			this.initialize.apply(this, arguments)
		}
	}
};
Object.extend = function(destination, source) {
	for (var property in source) {
		destination[property] = source[property]
	}
	return destination
};

function addEventHandle(obj, eventname, handle) {
	if (document.attachEvent) {
		obj.attachEvent("on" + eventname, handle)
	} else {
		if (document.addEventListener) {
			obj.addEventListener(eventname, handle, false)
		}
	}
};
var replaceNumber = function(str){
		var rex = /\d{11}/;
		if(rex.test(str) && str.length==11){
			var newstr = str.substr(0,7);
			return newstr+"****";
		}else{
			return str;
		}
}
