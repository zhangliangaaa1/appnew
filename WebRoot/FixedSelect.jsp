<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<c:forEach items="${nextActivityVoList}" var="nextActivity">
	<c:if test="${fn:indexOf(nextActivity.nextActivityName,'灵活审批') != -1}">
		<c:set var="claimId" value="${nextActivity.claimId}" />
		<c:set var="pendingId" value="${nextActivity.pendingId}" />
		<c:set var="nextActivityIdSync" value="${nextActivity.nextActivityId}" />
	</c:if>
	<c:if test="${fn:indexOf(nextActivity.nextActivityName,'灵活审批') == -1}">
		<c:set var="nextActivityIdFixed" value="${nextActivity.nextActivityId}" />
	</c:if>
</c:forEach>

<!DOCTYPE html>
<html>
  <head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>伊利集团财务共享平台 | 移动审批</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.6 -->
  <link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <!-- Select2 -->
  <link rel="stylesheet" href="../plugins/select2/select2.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="../dist/css/AdminLTE.min.css">
  <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
        page. However, you can choose any other skin. Make sure you
        apply the skin class to the body tag so the changes take effect.
  -->
  <link rel="stylesheet" href="../dist/css/skins/skin-black.min.css">

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
	  width: 30% !important;
	  float: left !important;
	}
	.form-group div.col-sm-10 {
	  width: 70% !important;
	  float: left !important;
	}
  </style>-->
  <script type="text/javascript">
  	var selectedIndex;
  	function changeType(){
  		var type = $("input[name='selectType']:checked").val();
  		$("#syncSelect").hide();
  		$("#fixedSelect" + selectedIndex).hide();
  		if( type == "-1" ){
  			$("#syncSelect").show();
  			$("#nextActivityId").val("${nextActivityIdSync }");
  		}else{
  			$("#fixedSelect" + type).show();
  			$("#nextActivityId").val($("#nextActivityIdFixed" +type).val());
  		}
  		
  		selectedIndex = type;
  	}
  	
  	function searchUser(){
  		var keyword =  $('#keyword').val();
          $.ajax({
              type: 'get',
              url: '${ctx}/ajax/getUser_order.do?keyword='+encodeURIComponent(keyword),
              data: {},
              dataType: "json",
              success: function (data) {
                  data = eval(data);
                  $("#syncUser").show();
                  var htmlStr = "<ul class='list-group'>";

                  for(var i=0; i<data.length ;i++){
                  	htmlStr = htmlStr + "<li class='list-group-item'><input type='radio' name='userId' value='" +
                  	data[i].userId + "-" + data[i].groupId +
                  	"'>" + data[i].fullname +"-"+ data[i].username +"-"+  data[i].groupName +"</li>";
                  }
				  htmlStr = htmlStr + "</ul>";
				  
				  $("#syncUser").html(htmlStr);
              },
              error: function (data) {
              	
              }
          });
		return false;
  	}
  	
  	function doSubmit(){
  		$("#doSubmitBut").attr({"disabled":"disabled"});
  		var type = $("input[name='selectType']:checked").val();
  		var userId ;
  		if( type == '-1' ){
  			userId = $("input[name='userId']:checked").val();
  			if(userId == undefined || userId == ''){
  				alert("请选择下一环节审批人");
  				$("#doSubmitBut").removeAttr("disabled");
  				return ;
  			}
  		}else{
  			userId = $("#userId"+type).val();
  			if(userId == undefined || userId == ''){
  				alert("请选择下一环节审批人");
  				$("#doSubmitBut").removeAttr("disabled");
  				return ;
  			}
  		}
  		$("#userGroupId").val(userId);
  		$("#submitForm").submit();
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
              <img src="../img/Black and White.jpg" class="user-image" alt="User Image">
              <!-- hidden-xs hides the username on small devices so only the image appears. -->
              <span class="hidden-xs">李涛</span>
            </a>
            <ul class="dropdown-menu">
              <!-- The user image in the menu -->
              <li class="user-header">
                <img src="../img/Black and White.jpg" class="img-circle" alt="User Image">

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
          <img src="../img/Black and White.jpg" class="img-circle" alt="User Image">
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
      <h1>
        选择审批下一环节候选人
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
		<form id="submitForm" class="form-horizontal" action="${ctx}/ajax/approve_order.do">
		 <input type="hidden" id="claimId" name="claimId" value="${claimId }">
 		 <input type="hidden" id="pendingId" name="pendingId" value="${pendingId }">
 		 <input type="hidden" id="nextActivityId" name="nextActivityId">
 		 <input type="hidden" id="userGroupId" name="userGroupId">
 		 <input type="hidden" id="comment" name="comment" value="${comment }">
 		 <input type="hidden" id="lockId" name="lockId" value="${lockId}">
 		 <input type="hidden" id="lockTime" name="lockTime" value="${lockTime}">
 
		<div class="box-body">
		  <!--<div class="col-md-6">-->
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">审批类型</label>
            <div class="col-sm-10">
            	<c:forEach items="${nextActivityVoList}" var="nextActivity" varStatus="status">
					<c:if test="${fn:indexOf(nextActivity.nextActivityName,'灵活审批') != -1}">
						<input type="radio" id="selectType" name="selectType" value="-1" onclick="javascript:changeType();">${nextActivity.nextActivityName }
					</c:if>
					<c:if test="${fn:indexOf(nextActivity.nextActivityName,'灵活审批') == -1}">
						<input type="radio" id="selectType" name="selectType" value="${status.index }" onclick="javascript:changeType();">${nextActivity.nextActivityName }
					</c:if>
				</c:forEach>
            </div>
		  </div>
		  <div id="syncSelect" class="form-group" style="display: none">
            <label for="" class="col-sm-2 control-label">下环节审批人</label>
            <div class="col-sm-10">
			  <div class="input-group">
			      <input type="text" class="form-control" id="keyword" name="keyword">
			      <span class="input-group-btn">
			        <button class="btn btn-default" type="button" onclick="javascript:searchUser();">搜索</button>
			      </span>
			    </div>
            </div>
            <label for="" class="col-sm-2 control-label"></label>
             <div id="syncUser" class="col-sm-10" style="display: none">
			 
			</div>
		  </div>
		  <c:forEach items="${nextActivityVoList}" var="nextActivity" varStatus="status">
			<c:if test="${fn:indexOf(nextActivity.nextActivityName,'灵活审批') == -1}">
				<div id="fixedSelect${status.index }" class="form-group" style="display: none">
					<input type="hidden" id="nextActivityIdFixed${status.index }" name="nextActivityIdFixed${status.index }" value="${nextActivity.nextActivityId }">
		            <label for="" class="col-sm-2 control-label">下环节审批人</label>
		            <div class="col-sm-10">
					  <select class="form-control" name="userId${status.index }" id="userId${status.index }">
			  	 		<c:forEach items="${nextActivity.nextParticipant}" var="participant">
			  	 			<option value="${participant.userId }-${participant.groupId }">${participant.fullname }</option>
			  	 		</c:forEach>
		              </select>
		            </div>
				  </div>
			</c:if>
		</c:forEach>
		  <!-- 
		  <div class="form-group">
            <label for="" class="col-sm-2 control-label">审批意见</label>
            <div class="col-sm-10">
			  <textarea id="commment" name="comment" class="form-control" rows="3" placeholder="Enter ...">同意</textarea>
            </div>
		  </div>
		   -->
		  <!--</div>-->
	    </div>
		<div class="box-footer">
          <button id="doSubmitBut" type="button" class="btn btn-default" onclick="javascript:doSubmit();">提交</button>
          <button class="btn btn-default pull-right" onclick="history.go(-1);">返回</button>
        </div>
	    </form>
	  </div>

    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

</div>
<!-- ./wrapper -->

<!-- REQUIRED JS SCRIPTS -->

<!-- jQuery 2.2.3 -->
<script src="../plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="../bootstrap/js/bootstrap.min.js"></script>
<!-- Select2 -->
<script src="../plugins/select2/select2.full.min.js"></script>
<!-- AdminLTE App -->
<script src="../dist/js/app.min.js"></script>

<!-- Optionally, you can add Slimscroll and FastClick plugins.
     Both of these plugins are recommended to enhance the
     user experience. Slimscroll is required when using the
     fixed layout. -->
<script>
  $(function () {
    //Initialize Select2 Elements
    $(".select2").select2();
  });
</script>
</body>
</html>
