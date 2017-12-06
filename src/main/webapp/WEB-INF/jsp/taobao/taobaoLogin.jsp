<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../common/tag.jsp"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<title>淘宝二维码登录</title>
	    <%@include file="../common/head.jsp"%>
	    <style>
	    	.main-state-info {
	    		font-size: 20px;
	    		color: #444;
	    	}
	    </style>
	</head>
  	<body>
  		<div class="container">
  			<div class="row text-center">
  				<br />
  				<br />
  				<br />
	      		<img id="qrCode" src="${empty imageCode ? '${pageContext.request.contextPath}/resources/image/wall.jpg' : imageCode}" alt="二维码" class="img-responsive img-thumbnail" /> 
  				<br />
  				<br />
	        	<h3 class="main-state-info">
	        	</h3>
	        	<p class="text-muted small">
	        		淘宝主播机器人
	        		<br />
	        		再次访问此页面可退出机器人
	        	</p>
			</div>
  		</div>
	    <%@include file="../common/foot.jsp"%>
	    <script type="text/javascript">
	    $(function() {
	    	var intervalQr = 3000;
	    	var getCodeStrUrl = "./getCode";
	    	var checkCodeStrUrl = "./checkCode";
	    	$.getJSON(getCodeStrUrl).done(callQrcode);
	    	var ref = setInterval(function(){
	    		$.getJSON(getCodeStrUrl).done(callQrcode);
	    	},1000*60);
	    	function callQrcode(rr){
	    		console.log(rr);
	    		if(rr.success==true)
		        {
		    		$("#qrCode").attr("src","https:"+rr.data.url);
		    		//设置定时刷新获取二维码状态的方法
		        }
	    	}
	    	var checkQrcode = $.getJSON(checkCodeStrUrl);
	    	var checkQr = setInterval(function(){
	    		$.getJSON(checkCodeStrUrl).done(callcheckQrcode);
	    	},3000);
	    	
	    	function callcheckQrcode(rr){
	    		console.log("--------checkQrcode----------");
	    		console.log(rr);
	    		if(rr.data.code=='10001')
	    		{
	    			$(".main-state-info").text("用户手机淘宝已经扫描!");
	    			console.log("用户手机淘宝已经扫描。");
	    		}
	    		if(rr.data.code=='10006')
	    		{
	    			$(".main-state-info").text("用户手机淘宝已经确认登录!");
	    			console.log("用户手机淘宝已经确认登录。");
	    		}
	    	}
	    	//qrget.done(function(rr){
	    		//console.log(rr);
	    	//});
	    });
	    </script>
  	</body>
</html>
