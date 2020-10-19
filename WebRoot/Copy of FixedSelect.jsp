<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
  </head>
  
  <body>
 <form id="settings">
   <input type="hidden" id="claimId" name="claimId" value="${claimId}">
 <input type="hidden" id="pendingId" name="pendingId" value="${pendingId}">
 <div class="container-fluid">
<fieldset id="inputs" style="border: 1px solid #000;border-radius: 6px;">
    <legend>移动审批</legend>
	<div class="row">
	  <div class="col-lg-6">
	    <div class="input-group">
	      <span class="input-group-addon">
	        <input type="radio">
	      </span>
	      <input type="text" class="form-control" disabled="disabled" value="00102039 财务部 李四">
	    </div><!-- /input-group -->
	    <div class="input-group">
	      <span class="input-group-addon">
	        <input type="radio">
	      </span>
	      <input type="text" class="form-control" disabled="disabled" value="00102039 集团总部 王五">
	    </div><!-- /input-group -->
	    <div class="input-group">
	      <span class="input-group-addon">
	        <input type="radio">
	      </span>
	      <input type="text" class="form-control" disabled="disabled" value="00102039 冷饮事业部 赵六">
	    </div><!-- /input-group -->
	    <div class="input-group">
	      <span class="input-group-addon">
	        <input type="radio">
	      </span>
	      <input type="text" class="form-control" disabled="disabled" value="00102039 财务部 张三">
	    </div><!-- /input-group -->
	  </div><!-- /.col-lg-6 -->
	 </div>
	 <br>
	 <div class="row">
	  <div class="col-lg-6">
	 <label for="email">审批意见：</label>
	 <textarea rows="4" cols="40%">
	 </textarea>
	 </div></div>
	  <br>
	  <p>
    <button type="button" class="btn btn-primary">提交</button>&nbsp;
    <button type="button" class="btn btn-success">提交</button>
    </p>
</fieldset>

</div>
</form>
  </body>
</html>
