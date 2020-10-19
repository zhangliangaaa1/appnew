<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
  <head>
    <title>MyHtml.html</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<!-- 新 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="dist/css/bootstrap.min.css">
	
	<!-- 可选的Bootstrap主题文件（一般不用引入） -->
	<link rel="stylesheet" href="dist/css/bootstrap-theme.min.css">
	
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
	
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="dist/js/bootstrap.min.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
		
			doPrint();
			
		});
		//点击打印按钮，触发事件】
		function doPrint(){
			 setTimeout(" alert(1);$('#printIframe')[0].contentWindow.print();","6000");
		   
		}
	</script>
  </head>
  
  <body >
	<iframe style="display:block" id="printIframe" src="/FSSCWebAPP/dist/Lab02.pdf">
	<input type="button" value="AAAAAAA" onclick="javascript:doPrint()"></iframe>
	
  </body>
</html>
