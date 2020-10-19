<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
  <head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>伊利集团财务共享平台 | 移动审批</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.6 -->
  <link rel="stylesheet" href="${ctx}/bootstrap/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="${ctx}/dist/css/AdminLTE.min.css">
  
  <!-- jQuery 2.2.3 -->
<script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- 
<script src="${ctx}/plugins/jQuery/jquery.messager.js"></script>
<script src="${ctx}/plugins/jQuery/jquery-1.2.6.pack.js"></script>
 -->
<!-- AdminLTE App -->
<script src="${ctx}/dist/js/app.min.js"></script>

<!-- Bootstrap 3.3.6 -->
<script src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
  <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
        page. However, you can choose any other skin. Make sure you
        apply the skin class to the body tag so the changes take effect.
  -->
  <link rel="stylesheet" href="${ctx}/dist/css/skins/skin-black.min.css">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
  <!--<style>
	.form-group {
		margin-left: 0px !important;
		margin-right: 0px !important;
	}
	.form-group label.col-sm-2 {
	  padding-top: 7px !important;
	  padding-left: 0 !important;
	  padding-right: 0 !important;
	  margin-bottom: 0 !important;
	  text-align: right !important;
	  width: 20% !important;
	  float: left !important;
	}
	.form-group div.col-sm-10 {
	  width: 80% !important;
	  float: left !important;
	}
  </style>-->
  
  <script type="text/javascript">
  	function sendBack(){
  		/*var str=document.getElementById("comment").value;
  		var len=strByteLength(str);
  		if (len<10){
  			alert("请至少输入5个汉字或10个字符！！！");
  			return;
  		}*/
  		$("#doSubmitBut").attr({"disabled":"disabled"});
  		$("#doSendbackBut").attr({"disabled":"disabled"});
  		$("#submitForm").attr("action", "${ctx}/ajax/sendback_order.do");
  		$("#submitForm").submit();
  	}
  	
  	function doSubmit(){
  		$("#doSubmitBut").attr({"disabled":"disabled"});
  		$("#doSendbackBut").attr({"disabled":"disabled"});
  		$("#submitForm").attr("action", "${ctx}/ajax/getNextSelect_order.do");
  		$("#submitForm").submit();
  	}
  	
	function strByteLength(str) {
		var i, sum;
		sum = 0;
		for (i = 0; i < str.length; i++) {
			if ((str.charCodeAt(i) >= 0) & (str.charCodeAt(i) <= 255))
				sum = sum + 1;
			else
				sum = sum + 2;
		}
		return sum;
	}
	function suggestionChange() {
		var sel = document.getElementById("sele");
		if (sel.options[sel.selectedIndex].text == "\u8bf7\u9009\u62e9") {
			document.getElementById("comment").value = "";
			return;
		}
		var opt = sel.options[sel.selectedIndex].text;
		opt = opt.replace(/\\n/g, "\n");
		document.getElementById("comment").value = opt;
	}
		</script>
  </head>
  
<body class="hold-transition skin-black sidebar-mini">
<div class="wrapper">

  <!-- Main Header -->
  <header class="main-header">

    <!-- Header Navbar -->
    <nav class="navbar navbar-static-top" role="navigation" style="display:none">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>
      <!-- Navbar Right Menu -->
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <!-- User Account Menu -->
          <li class="dropdown user user-menu">
            <!-- Menu Toggle Button -->
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <!-- The user image in the navbar-->
              <img src="img/Black and White.jpg" class="user-image" alt="User Image">
              <!-- hidden-xs hides the username on small devices so only the image appears. -->
              <span class="hidden-xs">李涛</span>
            </a>
            <ul class="dropdown-menu">
              <!-- The user image in the menu -->
              <li class="user-header">
                <img src="img/Black and White.jpg" class="img-circle" alt="User Image">

                <p>
                  李涛 - 工程师
                  <small>XX 部门</small>
                </p>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </nav>
  </header>
  <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar" style="display:none">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
	
      <!-- Sidebar user panel (optional) -->
      <div class="user-panel">
        <div class="pull-left image">
          <img src="img/Black and White.jpg" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p>李涛</p>
          <!-- Status -->
          <a href="#"><i class="fa fa-circle text-success"></i> 在线</a>
        </div>
      </div>

      <!-- Sidebar Menu -->
      <ul class="sidebar-menu">
        <!-- Optionally, you can add icons to the links -->
        <li class="active"><a href="#"><i class="fa fa-tasks"></i> <span>我的代办</span></a></li>
        <li><a href="#"><i class="fa fa-archive"></i> <span>我的记录</span></a></li>
		<li><a href="#"><i class="fa fa-cog"></i> <span>设置</span></a></li>
		<li><a href="#"><i class="fa fa-sign-out"></i> <span>退出登录</span></a></li>
      </ul>
      <!-- /.sidebar-menu -->
    </section>
    <!-- /.sidebar -->
  </aside>
 
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1 style="display:none">
        移动审批
        <small></small>
      </h1>
      <ol class="breadcrumb" style="display:none">
        <li><a href="#"><i class="fa fa-dashboard"></i> 财务共享平台</a></li>
        <li class="active">我的代办</li>
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">

      <!-- Your Page Content Here -->
	  <div class="box box-default">
		<form class="form-horizontal" id="submitForm" action="${ctx}/ajax/getNextSelect_order.do">
		<input type="hidden" id="claimId" name="claimId" value="${mobileClaimInfoVo.claimId}">
 		<input type="hidden" id="pendingId" name="pendingId" value="${mobileClaimInfoVo.pendingId}">
 		<input type="hidden" id="hrNo" name="hrNo" value="${hrNo}">
 		<input type="hidden" id="lockId" name="lockId" value="${mobileClaimInfoVo.id}">
 		<input type="hidden" id="lockTime" name="lockTime" value="${mobileClaimInfoVo.submitDate}">
 		
		<div class="box-body">
		  <!--<div class="col-md-6">-->
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">经办人</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder=""  value="${mobileClaimInfoVo.applyUserName}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">报销人</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder="" value="${mobileClaimInfoVo.expenseIssuerName}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">业务类别</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder=""  value="${mobileClaimInfoVo.item2Name}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">单据类型</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder="" value="${mobileClaimInfoVo.itemName}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">单位名称</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder=""  value="${mobileClaimInfoVo.orgName}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">部门名称</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder=""  value="${mobileClaimInfoVo.applyGroupName}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">单据编号</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder=""  value="${mobileClaimInfoVo.claimNo}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">报账金额</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder=""  value="${mobileClaimInfoVo.applyAmount}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">支付金额</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder=""  value="${mobileClaimInfoVo.payAmount}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">报账日期</label>
            <div class="col-sm-10">
			  <input type="text"  class="form-control" placeholder=""  value="${mobileClaimInfoVo.applyDate}" disabled>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">申请事由</label>
            <div class="col-sm-10">
			  <textarea id="" class="form-control" rows="3" placeholder="" disabled>${mobileClaimInfoVo.summary}</textarea>
            </div>
		  </div>
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">常用意见</label>
            <div class="col-sm-10">
			  <select id="sele" size="1" class="select" onchange="suggestionChange()" style="height: 25px">
					<option>请选择</option>
					<option>同意</option>
					<option>请重新办理</option>
					<option>请尽快办理</option>
					<option>请阅知</option>
					<option>请审核</option>
				</select>
            </div>
		  </div>
		  <c:if test="${wfstate == '10'}">
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">审批意见</label>
            <div class="col-sm-10">
			  <textarea id="comment" name="comment" class="form-control" rows="3" placeholder="请输入审批意见 ..."  ></textarea>
            </div>
		  </div>
		  </c:if>
		 <div id="div1" style="width:80px;height:80px;display:none;background-color:red;"></div><br>

		  <!--</div>-->
	    </div>
	     <div id="div"></div>
	    <c:if test="${wfstate == '10'}">
			<div class="box-footer">
	          <button id="doSubmitBut" class="btn btn-default" type="button" onclick="javascript:doSubmit();">同意</button>
	          <button id="doSendbackBut" class="btn btn-default pull-right" type="button" onclick="javascript:sendBack();">退回</button>
	        </div>
        </c:if>
        
        <div class="form-group">
            <label for="" class="col-sm-2 control-label">审批历史</label>
            <div class="col-sm-10">
			  <div class="widget-content nopadding"  style="overflow-x:auto;overflow-y:hidden;" >
				<div id="DataTables_Table_0_wrapper" class="dataTables_wrapper" role="grid">
					<table class="table table-bordered table-striped table-hover data-table dataTable" id="DataTables_Table_0">
						<thead>
							<tr role="row">
								<th style="WORD-BREAK: keep-all; white-space:nowrap;">审批人</th>
								<th style="width:300px;word-wrap:break-word;">意见</th>
								<th style="width:180px;word-wrap:break-word;">审批时间</th>
								<th style="WORD-BREAK: keep-all; white-space:nowrap;">状态</th>
							</tr>
						</thead>
						<tbody>
						  <c:forEach items="${mobileClaimInfoVo.processHistoryList}" var="data" varStatus="status">
								<tr>
									<td style="WORD-BREAK: keep-all; white-space:nowrap;">${data.user}</td>
									<td style="width:300px;word-wrap:break-word;">${data.comment} </td>
									<td style="width:180px;word-wrap:break-word;">${data.endDate}</td>
									<td style="WORD-BREAK: keep-all; white-space:nowrap;">${data.currentState}</td>		
								</tr>
							</c:forEach>
						</tbody>
					</table>							
				</div>
				</div>
            </div>
		  </div>
	    </form>
	  </div>

    </section>
    <!-- /.content -->
  </div>
  
</div>
<!-- ./wrapper -->

<!-- Optionally, you can add Slimscroll and FastClick plugins.
     Both of these plugins are recommended to enhance the
     user experience. Slimscroll is required when using the
     fixed layout. -->
</body>
</html>
