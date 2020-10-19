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
	<link rel="stylesheet" href="${ctx}/dist/css/bootstrap.min.css">
	
	<!-- 可选的Bootstrap主题文件（一般不用引入） -->
	<link rel="stylesheet" href="${ctx}/dist/css/bootstrap-theme.min.css">
	
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
	
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="${ctx}/dist/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	
	</script>
  </head>
  
  <body>
 <form id="submitForm" action="${ctx}/ajax/getNextSelect_order.do">
 <input type="hidden" id="claimId" name="claimId" value="${mobileClaimInfoVo.claimId}">
 <input type="hidden" id="pendingId" name="pendingId" value="${mobileClaimInfoVo.pendingId}">
 <input type="hidden" id="hrNo" name="hrNo" value="${hrNo}">
<fieldset id="inputs" style="border: 1px solid #000;border-radius: 6px;">
    <legend>移动审批</legend>
	<table >
		<tbody>
			<tr>
				<td><label for="name">单据类型：</label>
				</td>
				<td><input id="name" name="name" type="text" 
					autofocus  value="${mobileClaimInfoVo.applyGroupName}"/>
				</td>
			</tr>
			<tr>
				<td><label for="name">经办人：</label>
				</td>
				<td><input id="name" name="name" type="text" 
          			placeholder="经办人"  value="${mobileClaimInfoVo.applyUserName}"/>
				</td>
			</tr>
			<tr>
				<td><label for="email">单位名称：</label>
				</td>
				<td><input id="email" name="email" type="text"           
          			placeholder="单位名称"  value="${mobileClaimInfoVo.itemName}"/>
				</td>
			</tr>
			<tr>
				<td><label for="email">部门名称：</label>
				</td>
				<td><input id="email" name="email" type="text"           
          			placeholder="部门名称"   value="${mobileClaimInfoVo.orgName}"/>
				</td>
			</tr>
			<tr>
				<td><label for="email">单据编号：</label>
				</td>
				<td><input id="email" name="email" type="text"           
          			placeholder="单据编号"  value="${mobileClaimInfoVo.claimNo}"/>
				</td>
			</tr>
			<tr>
				<td><label for="email">报账金额：</label>
				</td>
				<td><input id="email" name="email" type="text"           
          			placeholder="报账金额"   value="${mobileClaimInfoVo.applyAmount}"/>
				</td>
			</tr>
			<tr>
				<td><label for="email">支付金额：</label>
				</td>
				<td><input id="email" name="email" type="text"           
          			placeholder="支付金额"  value="${mobileClaimInfoVo.payAmount}"/>
				</td>
			</tr>
			<tr>
				<td><label for="email">报账日期：</label>
				</td>
				<td><input id="email" name="email" type="text"           
          			placeholder="报账日期"  value="${mobileClaimInfoVo.applyDate}"/>
				</td>
			</tr>
		</tbody>
	</table>
    <button class="btn btn-default" type="submit">同意</button>&nbsp;
    <button class="btn btn-default" type="submit">退回</button>
</fieldset>
</form>
  </body>
</html>

<script type="text/javascript">
	/*
    jQuery(function ($) {
        $('#settings').submit(function () {
            $.ajax({
                type: 'post',
                url: '${ctx}/ajax/getNextSelect_order.do',
                data: $('#settings').serialize(),
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
	*/
</script>
