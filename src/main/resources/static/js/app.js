/**
 * 
 */
var app = function(){



	var bdResponseHandler = function(resp){
		var data = {
		  "current": 1,
		  "rowCount": 10,
		  "rows": [],
		  "total": 0
		};
		if(resp.data != null) {
			if(resp.data.pageNumber){
				data.current = resp.data.pageNumber;
				data.rowCount = resp.data.pageSize;
				data.rows = resp.data.results;
				data.total = resp.data.total;
				return data;
			} else if(resp.data.pageNumber){
				data.rowCount = resp.data.length;
				data.rows = resp.data;
				data.total = resp.data.length;
				return data;
			}
		}
		
		return data;
	};
	
	var doResult = function (data, url, failureCallBack) {
        var message = "";
        var result = true;
        
        if (data.code != 0) {
            if (data.code == 2) {
            	message += data.message + ":";
                for (var i in data.errMsg) {
                    message += data.errMsg[i];
                }
            } else if (data.code == 1) {
                location.href = "/login.html?from=" + encodeURIComponent(location.href);
            } else if (data.code == 4) {
                location.href = "/403";
            } else {
                message = data.message;
            }
            result = false;
            console.debug(JSON.stringify({request : url, response : data}));
        } else {
            result = true;
            message = data.message;
        }
        if (!result) { if (failureCallBack) failureCallBack(data); }

        return { result: result, message: message, code: data.code }
    };

    var alertErr = function(msg){
    	$.growl({
            message: msg
        },{
            type: 'danger',
            allow_dismiss: true,
            placement: {
                    align: 'center'
            },
            offset: {
                x: 20,
                y: 85
            },
            z_index: 9999999
        });
    };
    
    var alertOk = function(msg){
    	$.growl({
            message: msg
        },{
            type: 'success',
            allow_dismiss: true,
            placement: {
                    align: 'center'
            },
            offset: {
                x: 20,
                y: 85
            },
            z_index: 9999999
        });
    };
    
    var confirm = function(param, funOk, funCancel){
    	var defaults = {
	       title: "操作提示",
	       text: "确定删除吗？",
	       type: "warning",
	       showCancelButton: true,
	       confirmButtonColor: "#DD6B55",
	       cancelButtonText: "取消",
	       confirmButtonText: "确定",
	       closeOnConfirm: true,
	       allowOutsideClick: true
	     };
    	if(typeof(param) === 'string')
    		param = {text:param};
    	var p = $.extend(defaults, param);
    	swal(p, function (confirm) {
	          if(confirm){
	        	  if(funOk)funOk();
	          }else{
	        	  if(funCancel)funCancel();
	          }
	     });
    };
    
    var validateRequire = function(form, els){
    	var vR = true;
    	var f = $(form);
    	for(var i=0,len=els.length;i<len;i++){
    		f.find('input[name='+els[i]+']').each(function(){
    			var me = $(this);
    			var v = me.val();
    			me.parent().parent().find('.help-block').remove();
				me.parent().parent().parent().removeClass('has-error');
    			if($.trim(v) === ''){
    				var err_msg = me.attr('require-msg');
    				if(!err_msg)err_msg='此项必填';
    				me.parent().after('<small class="help-block">'+err_msg+'</small>');
    				me.parent().parent().parent().addClass('has-error');
    				vR = false;
    			}
    			me.unbind('keyup');
    			me.bind('keyup', function(){
    				var e = [];
    				e.push(me.attr('name'));
    				validateRequire(form,e);
    			});
    		});
    	}
    	return vR;
    };

    var loaddingFlag = 0;
	var showPreloader = function(){
		var preloader = $('.page-loader');
		if(preloader.length === 0){
		}
		preloader.show();
		loaddingFlag = 1;
	};
	var hidePreloader = function(){
		loaddingFlag = 0;
		var preloader = $('.page-loader');
		setTimeout(function(){
			if(loaddingFlag === 0)
				preloader.hide();
		},500);
	};
	return {
		showPreloader : function(){
			showPreloader();
		},
		hidePreloader : function(){
			hidePreloader();
		},
		getAbsoluteImageUrl : function(relativeUrl) {
			if (relativeUrl.startsWith("http")) {
				return relativeUrl;
			}
			var host = imageHost.replace("upload/images/.jpg", "");
			if (!relativeUrl.startsWith("/")) {
				relativeUrl = "/" + relativeUrl;
			}
			return host + relativeUrl;
		},
		bootgrid : function(selector, options) {

			var isPaged = true;
			if ('isPaged' in options)
				isPaged = options.isPaged;

			var defaults = {
				navigation: isPaged ? 2 : 0,
				rowCount: 10,
				sorting: isPaged ? true : false,
				ajax: true,
				requestHandler: function (request) {
					var condition = {
						"pageNumber": request.current,
						"pageSize": request.rowCount
					};
					if ('filters' in options)
						$.each(options.filters(), function (name, value) {
							condition[name] = value;
						});


					var orderBy = null;
					var orderSort = null;
					$.each(request.sort, function (name, value) {
						// 排序字段定义，支持别名
						var sn = $(selector).data('sort-'+name.toLowerCase());
						orderBy = sn ? sn : name;
						orderSort = value;
					});

					if (orderBy == null || orderSort == null) {
						return condition;
					}
					condition["orderBy"] = orderBy;
					condition["orderSort"] = orderSort;
					return condition;
				},
				labels: {
					loading: '数据加载中...',
					infos: '',
					noResults: "没有可展示的数据"
				}
			};


			var p = $.extend(defaults, options);

			return $(selector).bootgrid(p);
		},
		summernote : function(selector, options) {
			var defaults = {
				height: 150,
				lang: 'zh-CN',
				toolbar : [
					['style', ['bold', 'italic', 'underline']],
					['fontsize', ['fontsize']],
					['color', ['color']],
					['insert', ['picture', 'link', 'table']],
					['para', ['ul', 'ol', 'paragraph']],
					['Misc', ['codeview']]
				],
				dialogsInBody: true
			};


			var p = $.extend(defaults, options);

			return $(selector).summernote(p);
		},
		ajax : function(option){
			showPreloader();
			$.ajax({
	            url: option.url + (option.url.indexOf("?") == -1 ? "?" : "&") + "_temp=" + Math.random(),
	            type: option.type,
	            data: option.data,
	            contentType: option.contentType,
	            async: option.async == undefined ? true : option.async,
	            success: function (data) {
	            	hidePreloader();
	                var result = doResult(data, option.url, option.failureCallBack || function () { });
	                if (result.result) {
	                    option.success(data);
	                } else {
	                	alertErr(result.message);
	                	if(option.error)
	                		option.error(result);
	                }
	            }, error: function (a, b, c) {
	            	hidePreloader();
	            	//----处理shiro框架的 401 权限验证----
	            	var errJson = $.parseJSON(a.responseText);
	            	var statusCode = errJson.status;
	            	if(statusCode != null && statusCode == 401){
	            		location.href = "/403";
	            	}else{
	            		alertErr("抱歉，数据请求失败！<br/>url:" + option.url);
	            	}
	            	//----处理shiro框架的 401 权限验证   END----
	            }
	        });
		},
		ajaxUploadFile : function(option){
			showPreloader();
			$.ajax({
	            url: option.url + (option.url.indexOf("?") == -1 ? "?" : "&") + "_temp=" + Math.random(),
	            type: option.type,
	            data: option.data,
	            dataType: option.dataType,
	            contentType : false,
				processData : false,
	            async: option.async == undefined ? true : option.async,
	            success: function (data) {
	            	hidePreloader();
	                var result = doResult(data, option.url, option.failureCallBack || function () { });
	                if (result.result) {
	                    option.success(data);
	                } else {
	                	alertErr(result.message);
	                	if(option.error)
	                		option.error(result);
	                }
	            }, error: function (a, b, c) {
	            	hidePreloader();
	            	alertErr("抱歉，数据请求失败！<br/>url:" + option.url);
	            }
	        });
		},
		alertOk : function(msg){
			alertOk(msg);
		},
		alertErr : function(msg){
			alertErr(msg);
		},
		confirm : function(param, funOk, funCancel){
			confirm(param, funOk, funCancel);
		},
		ready : function(){},
		showAjaxModal : function(url, data, callback, closeCallback){
			showPreloader();
			var me = this;
			$.ajax({
	            url: url + (url.indexOf("?") == -1 ? "?" : "&") + "_temp=" + Math.random(),
	            type: 'GET',
	            success: function (html) {
	            	hidePreloader();
	            	if (html.indexOf('没有操作权限') > 0) {
	            	 	alertErr("没有操作权限");
	            	 	return;
	            	 }
	            	 var m = $(html).modal({ backdrop: "static", show: false });
	            	 m.returnValue = "";
	            	 var clientHeight = $(window).height();
	                 m.find(".modal-body").css("max-height", clientHeight - 180);
	                 m.find(".modal-body").css("overflow-y", 'auto');
	                 m.modal("show");
	                 m.on('shown.bs.modal', function () {
	                	 if(callback)callback();
	                     me.ready.call(m, data);
	                 });
	                 m.on('hidden.bs.modal', function () {
	                     if (m.returnValue) {
	                        if (closeCallback){
	                        	closeCallback(m.returnValue);
	                        }
	                     }
	                     m.remove();
	                 });
	            }, error: function (a, b, c) {
	            	hidePreloader();
	            	if(a.responseText){
	            		if(a.responseText.indexOf('登录 好老板业务管理云') > 0){
	            			location.href = '/forward/login.html?from=' + encodeURIComponent(location.href);
	            			return;
	            		}
	            		if(a.responseText.indexOf('没有操作权限') > 0){
	            			alertErr('没有操作权限！');
	            			return;
	            		}
	            	}
	            	alertErr("抱歉，数据请求失败！<br/>url:" + url);
	            }
           });
		},
		showModal : function(selector, options){
			var defaults = {
				show : true
			};
			var p = $.extend(defaults, options);
			var clientHeight = $(window).height();
			$(selector).find('.modal-body').css("max-height", clientHeight - 180);
			$(selector).find('.modal-body').css("overflow-y", 'auto');
			var m = $(selector).modal(p);
			m.on('shown.bs.modal', function () {
				if(p.callback)p.callback();
            })
            m.on('hidden.bs.modal', function () {
               if (p.closeCallback){
               	p.closeCallback(m.returnValue);
               }
            });
		},
		validateRequire : function(f, e){
			return validateRequire(f,e);
		}
	};
}();

$.fn.serializeObject = function(suffix) {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (suffix) {
			this.name = this.name.replace(suffix, '');
		}
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value == 'null'? null: this.value || null);
		} else {
			o[this.name] = this.value == 'null'? null: this.value || null;
		}
	});
	return o;
};

$.urlPath = function(args) {
	var result = this;
	var a = document.createElement('a'); 
	a.href = args;
	return a.pathname.replace(/^([^\/])/,'/$1');
};

$.moneyFormat = function(s, n) {
   n = n > 0 && n <= 20 ? n : 2;
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
   var l = s.split(".")[0].split("").reverse(),
   r = s.split(".")[1];
   t = "";
   for(i = 0; i < l.length; i ++ )
   {
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
   }
   return t.split("").reverse().join("") + "." + r;
}

String.prototype.format = function(args) {
	var result = this;
	if (arguments.length > 0) {
		if (arguments.length == 1 && typeof (args) == "object") {
			for (var key in args) {
				if(args[key]!=undefined){
					var reg = new RegExp("({" + key + "})", "g");
					result = result.replace(reg, args[key]);
				}
			}
		}
		else {
			for (var i = 0; i < arguments.length; i++) {
				if (arguments[i] != undefined) {
					var reg = new RegExp("({[" + i + "]})", "g");
					result = result.replace(reg, arguments[i]);
				}
			}
		}
	}
	return result;
};

jQuery.extend(jQuery.validator.messages, {
	required: "必选字段",
	remote: "请修正该字段",
	email: "请输入正确格式的电子邮件",
	url: "请输入合法的网址",
	date: "请输入合法的日期",
	dateISO: "请输入合法的日期 (ISO).",
	number: "请输入合法的数字",
	digits: "只能输入整数",
	creditcard: "请输入合法的信用卡号",
	equalTo: "请再次输入相同的值",
	accept: "请输入拥有合法后缀名的字符串",
	maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
	minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
	rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
	range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max: jQuery.validator.format("请输入一个最大为{0} 的值"),
	min: jQuery.validator.format("请输入一个最小为{0} 的值")
});

$.validator.setDefaults({
	errorPlacement: function(error, element) {
		if ( element.is(":radio") )
			error.appendTo( element.parent().parent().next() );
		else if ( element.is(":checkbox") )
			error.appendTo ( element.parent() );
		else
			error.appendTo(element.parent());
	}
});

//对Date的扩展，将 Date 转化为指定格式的String   
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
//例子：   
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.format = function(fmt)
{
var o = {   
  "M+" : this.getMonth()+1,                 //月份   
  "d+" : this.getDate(),                    //日   
  "h+" : this.getHours(),                   //小时   
  "m+" : this.getMinutes(),                 //分   
  "s+" : this.getSeconds(),                 //秒   
  "q+" : Math.floor((this.getMonth()+3)/3), //季度   
  "S"  : this.getMilliseconds()             //毫秒   
};   
if(/(y+)/.test(fmt))   
  fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
for(var k in o)   
  if(new RegExp("("+ k +")").test(fmt))   
fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
return fmt;   
};

function addDays(value, n){
	if(value == null) return;
	var time = new Date(value);
	if (time == "Invalid Date") {
        alert("参数非日期");
        return;
    }
	//累加的天数 ： n
	var date = time.getTime() + n * 24 * 60 * 60 * 1000;
	var result = new Date(date);
	//拼接成需要的格式返回
	return (result.getFullYear() + "-" + (result.getMonth() + 1) + "-" + result.getDate());
}
