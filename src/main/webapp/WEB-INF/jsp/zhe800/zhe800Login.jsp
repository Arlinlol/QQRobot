<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../common/tag.jsp"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<title>折800签到机器人</title>
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
  				<div id="qrcode"></div>
	      		<!-- <img src="${empty imageCode ? '${pageContext.request.contextPath}/resources/image/wall.jpg' : imageCode}" alt="二维码" class="img-responsive img-thumbnail" /> -->
  				<br />
  				<br />
	        	<h3 class="main-state-info">
	        	</h3>
	        	<p class="text-muted small">
	        		登录后QQ机器人将自动回复
	        		<br />
	        		再次访问此页面可退出机器人
	        	</p>
			</div>
  		</div>
	    <%@include file="../common/foot.jsp"%>
	    <script src="${pageContext.request.contextPath}/resources/js/jquery.qrcode.min.js?${resVer}"></script>
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
	    		if(rr.code==0)
		        {
	    			$('#qrcode').children().remove(); 
		    		$('#qrcode').qrcode(rr.data.qrStr);
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
	    	}
	    	//qrget.done(function(rr){
	    		//console.log(rr);
	    	//});
	    });
	    </script>
  	</body>
</html>
