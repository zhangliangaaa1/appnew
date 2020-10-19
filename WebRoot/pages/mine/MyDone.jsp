<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<title>我的已办</title>
	<%@include file="/newPages/efinance/common/common-head.jsp" %>
	<%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
	<style type="text/css">
		.image{
			width: 26px;
			height: 26px;
			background: #FFFFFF;
			border: 1px solid #48A0FF;
			border-radius: 4px;
			display: inline-block;
			padding-top: 2px;
			padding-left: 4px;
		}
		.warning{
			margin-left: 10px;
		}

	</style>
</head>

<body>
<form id="queryForm" name="queryForm" method="post" action="commonbizajax2report.do?requestId=FindDoneList">
	<input type="hidden" id="IsAdvancedSerach" name="IsAdvancedSerach" value="1">
	<div id="wrapper" class="gray-bg">
		<%--<div id="page-wrapper" class="gray-bg">--%>
		<div class="">
			<div class="p-w-md m-t-sm">
				<div class="row">
					<div class="row" style="margin: 10px 0px"><!--面包屑开始-->
						<ol class="breadcrumb">
							<li class="active"><a style="color: #232323;">我的已办</a></li>
						</ol>
					</div><!--面包屑结束-->
					<div class="ibox">
						<div class="ibox-content">
							<div id="tt" style="display: block;">
								<div class="row">
									<div class="col-sm-3">

										<div class="form-group">
											<label class="font-noraml">报账单编号</label>
											<input type="text" id="claimNo" name="claimNo" value=""
												   placeholder="请填写报账单编号" class="form-control" c-model="myDone.claimNo"
												   style="width: 100%;height: auto">
										</div>
										<div class="display-none">
											<div class="form-group">
												<label class="font-noraml">起草人</label>
												<input type="text" id="applyUsername1" name="applyUsername1"
													   placeholder="请填写起草人姓名" class="form-control" c-model="myDone.applyUsername1"
													   style="width: 100%;height: auto">
											</div>
											<div class="form-group">
												<label class="font-noraml">合同名称</label>
												<input type="text" id="contractName" name="contractName"
													   placeholder="请填写合同名称" class="form-control" c-model="myDone.contractName"
													   style="width: 100%;height: auto">
											</div>

											<div class="form-group" id="data_5">
												<label class="font-noraml">处理时间</label>
												<div class="input-daterange input-group">
													<input type="text" class="form-control Wdate" id="startDate" c-model="myDone.startDate"
														   name="startDate" value="" style="height: auto" autocomplete="off"
														   onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'});"/>
													<span class="input-group-addon">-</span>
													<input type="text" class="form-control Wdate" id="endDate" c-model="myDone.endDate"
														   name="endDate" value="" style="height: auto"autocomplete="off"
														   onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'});"/>
												</div>
											</div>

										</div>
									</div>
									<div class="col-sm-3">
										<div class="form-group">
											<label class="font-noraml">报账单模板</label>
											<select name="itemId1" id="itemId1" class="form-control" c-model="myDone.itemId1"
													onchange="initItem2List()"></select>
										</div>
										<div class="display-none">
											<div class="form-group">
												<label class="font-noraml">起草部门</label>
												<input type="text" id="applyUserDepname1" name="applyUserDepname1" c-model="myDone.applyUserDepname1"
													   placeholder="请填写起草部门" class="form-control"
													   style="width: 100%;height: auto">
											</div>
											<div class="form-group">
												<label class="font-noraml">合同编号</label>
												<input id="contractNo" name="contractNo" class="form-control pd-right20" c-model="myDone.contractNo"
													   placeholder="请填写合同编号" style="width: 100%;height: auto"/>
											</div>

											<div class="form-group">
												<label class="font-noraml">付款批</label>
												<input type="text" id="applyUsername" name="applyUsername"
													   placeholder="请填写付款批" class="form-control" c-model="myDone.applyUsername"
													   style="width: 100%;height: auto">
											</div>

										</div>
									</div>
									<div class="col-sm-3">
										<div class="form-group">
											<label class="font-noraml">业务大类</label>
											<select class="form-control" id="item2Id1" name="item2Id1" c-model="myDone.item2Id1"
													style="width: 100%;height: auto"></select>
										</div>



										<div class="display-none">

										<div class="form-group">
											<label class="font-noraml">供应商名称</label>
											<input type="hidden" id="vendorNo" name="vendorNo">
											<input id="vendorName" name="vendorName" class="form-control" c-model="myDone.vendorName"
												   placeholder="请填写供应商名称" style="width: 100%;height: auto" readonly="readonly"/>
											<i class="glyphicon glyphicon-remove input-search ivendordel" onclick="onResetVendor();"  controlKey="unit"></i>
											<i class="glyphicon glyphicon-search input-search ivendorName" onclick="openVendor();"  controlKey="unit"></i>
										</div>


											<div class="form-group">
												<label class="font-noraml">报账金额范围</label>
												<div class="input-group">
													<input type="text" id="applyAmountMin" name="applyAmountMin"
														   class="input-sm form-control" placeholder="最低金额" c-model="myDone.applyAmountMin"
														   style=";height: auto"/>
													<span class="input-group-addon"
														  style="padding: 4px 5px;background-color:#eeeeee">-</span>
													<input type="text" id="applyAmountMax" name="applyAmountMax"
														   class="input-sm form-control" placeholder="最高金额" c-model="myDone.applyAmountMax"
														   style=";height: auto"/>
												</div>
											</div>

											<div class="form-group">
												<label class="font-noraml">凭证号</label>
												<input type="text" id="settlementNum" name="settlementNum"
													   placeholder="请填写凭证号" class="form-control" c-model="myDone.settlementNum"
													   style="width: 100%;height: auto">
											</div>

										</div>
									</div>
									<div class="col-sm-3">

										<div class="form-group">
											<label class="font-noraml">公司OU</label>
											<select id="orgId" name="orgId" class="form-control required" style="width: 100%;height: auto"></select>
										</div>


										<div class="display-none">

										<div class="form-group">
											<label class="font-noraml">业务类型</label>
											<select  class="form-control" id="businessType"
													 name="businessType"></select>
										</div>

										<div class="form-group">
												<label class="font-noraml">付款金额范围</label>
												<div class="input-group">
													<input type="text" id="payAmountMin" name="payAmountMin" c-model="myDone.payAmountMin"
														   class="input-sm form-control" placeholder="最低金额"
														   style="height: auto"/>
													<span class="input-group-addon"
														  style="padding: 4px 5px;background-color:#eeeeee">-</span>
													<input type="text" id="payAmountMax" name="payAmountMax" c-model="myDone.payAmountMax"
														   class="input-sm form-control" placeholder="最高金额"
														   style="height: auto"/>
												</div>
											</div>
											<div class="form-group">
												<label class="font-noraml">是否超出预算</label>

													<select  class="form-control" id="hasExceedBudget" name="hasExceedBudget" c-model="myDone.hasExceedBudget">
														<option value="">请选择</option>
														<option value="1">是</option>
														<option value="0">否</option>
													</select>

											</div>
										</div>
									</div>
									<div class="col-sm-3">
										<div class="display-none">
											<div class="form-group">
												<label class="font-noraml">项目名称</label>
												<input type="text" id="projectName" name="projectName"
													   placeholder="请填写项目名称" class="form-control" c-model="myDone.projectName"
													   style="width: 100%;height: auto">
											</div>
										</div>
									</div>
									<div class="col-sm-3">
										<div class="display-none">
											<div class="form-group">
												<label class="font-noraml">项目编号</label>
												<input type="text" id="projectNum" name="projectNum"
													   placeholder="请填写项目编号" class="form-control"
													   c-model="myDone.projectNum"
													   style="width: 100%;height: auto">
											</div>
										</div>
									</div>
								</div>
								</div>
								<div class="row">
									<div class="col-sm-12">
										<div class="text-center"
											 style="border-bottom: 1px solid rgba(183,212,246,0.50); padding-bottom: 20px">
											<button id="showAndHide" type="button"
													style="background-color: #48A0FF;height: 28px;width: 28px;border: 0px;  background: url(${pageContext.request.contextPath}/newstatic/img/icon/group_12.png) 50% 50%;  border-radius: 2px;color: #FFFFFF;font-size: 14px;">
												&nbsp;</button>
											<button style="background-color: #48A0FF;height: 28px;width: 100px;border: 0px;border-radius: 2px;color: #FFFFFF;font-size: 14px;"
													type="button"
													onclick="$('#dtClaimDone').bootstrapTable('refresh'); ">查询
											</button>
											<button type="button" onclick="onReset()"
													style="background-color: #B7B9BB;height: 28px;width: 100px;border: 0px;border-radius: 2px;color: #FFFFFF;font-size: 14px;">
												重置
											</button>
											<button style="background-color: #48A0FF;height: 28px;width: 100px;border: 0px;border-radius: 2px;color: #FFFFFF;font-size: 14px;"
													type="button"
													onclick="doExport()">导出
											</button>
										</div>
									</div>
								</div>

							</div>
							<div style="margin: 10px 0px 20px 0px; padding-top: 10px">
								<span style="border: 1px solid #48A0FF;margin-left: -20px;"></span>
								<span style="font-family: MicrosoftYaHei;font-size: 15px;color: #0B112A;margin-left: 13px;">查询结果</span>
							</div>
							<div class="checkListDiv q_mpsdc_body" style="margin-top: -20px;">
								<table id="dtClaimDone"
									   class="table table-striped table-hover q_table table_td_left"
									   data-classes="table-no-bordered"
									   data-id-field="id" data-click-to-select="true" data-striped="true"
									   data-ajax="claimTodoListSearchAjax"
									   data-side-pagination="server" data-pagination="true" data-page-size="50"
									   data-height="360" data-show-jumpto="true"data-resizable="true">
									<thead>
									<tr>
										<th data-checkbox="true"></th>

										<th data-width="200px" data-field="claimNo" data-formatter="render4TdLink">
											报账单号
										</th>

										<th data-width="80px" data-field="hasExceedBudget" data-formatter="render4Budget">
											预算超支
										</th>

										<th data-width="400px" data-field="title" data-formatter="render4TdLink">
											摘要
										</th>

										<th data-width="80px" data-field="applyUsername" data-formatter="render4TdLink">
											起草人
										</th>


										<th data-width="110px" data-field="handlerName" data-formatter="render4TdLink">
											当前环节处理人
										</th>

										<th data-width="180px" data-field="endTime" data-formatter="render4Date">
											处理时间
										</th>
										<th data-width="110px" data-field="contractNo" data-formatter="render4TdLink">
											合同编号
										</th>
										<th data-width="120px" data-field="contractName" data-formatter="render4TdLink">
											合同名称
										</th>
										<th data-width="110px" data-field="projectNum" data-formatter="render4TdLink">
											项目编号
										</th>
										<th data-width="120px" data-field="projectName" data-formatter="render4TdLink">
											项目名称
										</th>
										<th data-width="120px" data-field="applyAmount"  data-formatter="formatterMoney4BootTable" data-class="amountThCss">
											报账金额
										</th>

										<th data-width="120px" data-field="payAmount"  data-formatter="formatterMoney4BootTable" data-class="amountThCss">
											付款金额
										</th>

										<th data-width="120px" data-field="settlementNum" data-formatter="render4TdLink">
											凭证号
										</th>

										<th data-width="150px" data-field="summary" data-formatter="render4TdLink">
											备注
										</th>

										<th data-width="50px" data-field="id" data-formatter="render4Opt">
											操作
										</th>
									</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
				</div>

			</div>


		</div>

	</div>
</form>

<script>

    var curPortalUserGroupId = "";
    var user;
    var item2List;
    var item2List1;
    var commonUiDataBindService4QueryList = new CommonUiDataBindService4QueryList("myDone");

    function initOU() {//初始化公司
        var url = contextPath + "/org/businessunit/currentuser.do?allOrg=true";
        baseService.generateSelectElement({
            selectId: "orgId"
            ,
        }, url, "orgId", "orgName");
    }

    //初始化业务类型下拉框
    function initBussinessList() {
        var url = contextPath + "/dictListAjax.do?&dictId=" + "businessType";
        baseService.generateSelectElement({
            selectId: "businessType"
        }, url, "code", "value");
    }

    $(function () {

        initOU();

        initBussinessList();

        $('#hasExceedBudget').chosen();
        $("#showAndHide").click(function () {
            $(".display-none").slideToggle(function () {
            });
            $(this).toggleClass('showAndHide-active');

        });

        $('#hh').click(function () {
            $('#tt').slideToggle();
        });

        //初始化查询条件中的报账单模板
        initClaimTemplate();
        //业务类型下拉框
        initBussinessList();

        $("#dtClaimDone").bootstrapTable({ pageList: [10, 20, 30, 50,100,200,500]});//初始化表格
    });

    //初始化报账单模板下拉框
    function initClaimTemplate() {
        var url = contextPath + "/dictListAjax.do?&dictId=" + "claimTemplate";
        baseService.generateSelectElement({
            selectId: "itemId1"
        }, url, "code", "value");
    }

    //初始化业务大类下拉框
    function initItem2List() {
        var parentId = $("#itemId1").val();
        if (parentId == null || parentId == '') {
            $("#item2Id1").val("");
            $("#item2Id1").trigger("chosen:updated");
            return false;
        }
        var url = contextPath + "/org/businessunit/cateLevel2.do?" + "&orgId=0&parentId=" + parentId;
        baseService.generateSelectElement({
            selectId: "item2Id1"
        }, url, "code", "value");
    }


    //重置
    function onReset() {
        //input类型的标签
        $("#queryForm input").val("");
        $("#queryForm input[name= 'IsAdvancedSerach']").val("1");

        $("#queryForm input[type=radio][name='hasExceedBudget']:checked").attr("checked", false);
        $("#queryForm input[type=radio][name='hasExceedBudget']:eq(0)").val("1");
        $("#queryForm input[type=radio][name='hasExceedBudget']:eq(1)").val("0");
        //select类型的标签
        $("#queryForm select").val("");
        $("#queryForm select").trigger("chosen:updated");
    }

    function claimTodoListSearchAjax(params) {//数据查询ajax

        var paramUrl = contextPath + "/commonbizajax2report.do?requestId=FindDoneList";
        var inputMapModel="item2List,item2List1";
        var data={inputMapModel : inputMapModel};

        data = generateSearchConditionAndUpd2Ui(commonUiDataBindService4QueryList,this,"queryForm",data,function (urlParam) {//设置联动方法，如果没有联动无需写
            if(typeof urlParam.itemId1 !='undefined'&& urlParam.itemId1 !=''){//设置报账单模板和报账单大类的联动
                updateUiById("itemId1",urlParam.itemId1);
                $('#itemId1').trigger("change");
            }
        });

        loadBootstrapTableData(this, paramUrl, params, $('#queryForm'),data,setParam);//ajax加载表格
    }


    function setParam(data) {
        curPortalUserGroupId = data.user.curGroupId;
        user = data.user;
        var inputMap = data.inputMap;
        item2List = inputMap.item2List;
        item2List1 = inputMap.item2List1;
        if(data.outItem!=''){
            var outItem = JSON.parse(data.outItem);
            if($.trim($("#claimNo").val())==''){
                if(typeof outItem !='undefined'&& typeof outItem.startDate!='undefined'){
                    $("#startDate").val(outItem.startDate);
                }
                if(typeof outItem !='undefined'&& typeof outItem.endDate!='undefined'){
                    $("#endDate").val(outItem.endDate);
                }
            }
        }
    }

    /**
     * 生成查询条件并更新到页面
     * @param commonUiDataBindService4QueryList	ui绑定对象
     * @param btObj	bootstrap 的ajax 获取数据对象
     * @param formid
     * @param beforeObj2Ui	在更新查询条件到页面之前执行的函数
     * @param beforeReturn	在返回查询条件前执行的函数
     */
    function generateSearchConditionAndUpd2Ui(commonUiDataBindService4QueryList,btObj,formId,data,beforeObj2Ui,beforeReturn){
        var queryFormObj =$('#'+formId);
        if(serachListFlag=="fromUrl"){
            serachListFlag="fromForm";//更新标记位，不从
            queryFormObj=null;
            var urlParam = getSearchParamObjFromUrl();
            if($.isEmptyObject(urlParam)){
                urlParam.endDate =formatDate(new Date());
                urlParam.startDate = subMonth4Query(new Date(),-3);

            }
            commonUiDataBindService4QueryList.obj2Ui(urlParam);//更新查询条件到页面上
            if(typeof urlParam.curPage !="undefined"){//设置页码
                btObj.options.pageNumber =parseInt(urlParam.curPage);
            }
            if(beforeReturn != null) {
                beforeReturn(urlParam);
            }
            data =$.extend(urlParam,data);
        }
        return data;
    }

    function render4TdLink(value, row, index, field) {
        var oldUrl = row.url+"&enterClaimStatus=yiban";
        return createTdLinkCommon("onQuery( '" + oldUrl + "')", value);
    }
    function onQuery(url){
        skipUrl(url);
    }
    function doNewUrl(url) {
        var itemId = getParam(url,"itemId");
        var lowerItem = itemId.toLowerCase();
        var newUrl = contextPath+"/newPages/efinance/claim/"+itemId+"/"+lowerItem+"_draft.jsp?"+url.substr(url.indexOf("?")+1)+generateSearchUrl($("#queryForm").serializeObject(),"dtClaimDone");
        window.location.href = doProcessUrl(newUrl);
    }

    function doOldUrl(url) {
        var urlData = url;
        urlData = urlData.replace(/&/g,"|");
        url = url + "&url=" + urlData;
        url = url + "&doneFlag=true";
        window.location.href = contextPath+doProcessUrl(url)+generateSearchUrl($("#queryForm").serializeObject(),"dtClaimDone");
    }
    function render4Opt(value, row, index, field) {

        return  '<i class="iconfont icon-filesearch" onclick="onQuery(\'' + row.url + '\');" title="查看详情"></i>&nbsp;&nbsp;';

    }

    //打开供应商弹框
    function openVendor() {
        var userJson = JSON.parse(user);
        var compId = userJson.curCompId;
        var orgId = userJson.orgId;
        var pageUrl = "/newPages/efinance/myReimbursement/searchVendorList.jsp";
        dialogService.openDialog(function (dataReturn) {//回调方法
            if (typeof(dataReturn) != "undefined") {
                updateVendor(dataReturn);
            }
        }, {//参数
            compId : compId,
            orgId : orgId,
            hasSite : 'yes'
        }, pageUrl, "选择供应商");
    }

    function updateVendor(dataReturn) {
        if (typeof(dataReturn) != "undefined") {
            updateUiById("vendorNo", dataReturn.vendorNumber);
            updateUiById("vendorName", dataReturn.vendorName);
        }
    }

    function onResetVendor(){
        updateUiById("vendorNo", "");
        updateUiById("vendorName", "");
    }

    function render4Budget(value, row, index, field) {
        if(value == "是"){
            //var imgUrl =contextPath+"/newstatic/img/icon/shape.png";
            return '<img class="warning" src= "'+contextPath+'/newstatic/img/icon/shape.png"/><div><font size="1" color="red">预算超支</font></div>';
        }else{
            return "";
        }
    }

	function doExport() {
		var form = document.getElementById("queryForm");
		form.action = contextPath + "/claimVoucherReportExportAction.do?requestId=doExportMyDone";
		form.submit();
	}
</script>


</body>
</html>
