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
 
<fieldset id="inputs" style="border: 1px solid #000;border-radius: 6px;">
    <legend>选择审批下一环节候选人：</legend>
    <div class="row">
	<div class="col-lg-6">
    <div class="input-group">
      <input type="text" class="form-control" id="keyword" name="keyword">
      <span class="input-group-btn">
        <button class="btn btn-default" type="submit">搜索</button>
      </span>
    </div><!-- /input-group -->
  </div><!-- /.col-lg-6 -->
  </div>
 <div class="row">
 <div class="col-lg-6">
  <ul class="list-group">
	  <li class="list-group-item"><input type="radio">00101234	财务部	张三</li>
	  <li class="list-group-item"><input type="radio">00101234	集团总部	李四</li>
	  <li class="list-group-item"><input type="radio">00101234	冷饮事业部	张三</li>
	  <li class="list-group-item"><input type="radio">00101234	财务部	王五</li>
	  <li class="list-group-item"><input type="radio">00101234	报销部	张三</li>
	</ul>
	</div>
	</div>
	 <label for="email">审批意见：</label>
	 <textarea rows="4" cols="40%">
	 </textarea>
  <br> <br>
  <div class="btn-group">
  <p>
  	<button class="btn btn-default" type="submit">提交</button>&nbsp;
    <button type="button" class="btn btn-success">返回</button>
    </p>
    </div>
</fieldset>
</form>
  </body>
</html>

<script type="text/javascript">
    jQuery(function ($) {
        $('#settings').submit(function () {
        	var keyword =  $('#keyword').val();
        	alert(keyword);
            $.ajax({
                type: 'get',
                url: '${ctx}/ajax/getUser_order.do?keyword='+keyword,
                data: {},
                dataType: "json",
                success: function (data) {
                	alert(data);
                    data = eval(data);
                    
                },
                error: function (data) {
                	alert(1);
                    $('#alert').empty();
                    $('#alert').append("<div class=\"alert alert-danger\">" + data + "<a href=\"#\" data-dismiss=\"alert\" class=\"close\">×</a></div>");
                }
            });
            return false;
        });
    });
</script>