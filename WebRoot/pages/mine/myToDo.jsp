<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>我的待办</title>
    <%@include file="/newPages/efinance/common/common-head.jsp" %>
    <%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/framework/util/other-ajax.js"></script>
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
<form id="queryForm" name="queryForm" method="post" action="commonbizajax2report.do?requestId=FindTodoList">
    <input type="hidden" id="IsAdvancedSerach" name="IsAdvancedSerach" value="1">
    <div id="wrapper" class="gray-bg">
        <%--<div id="page-wrapper" class="gray-bg">--%>
        <div class="">
            <div class="p-w-md m-t-sm">
                <div class="row">
                    <div class="row" style="margin: 10px 0px"><!--面包屑开始-->
                        <ol class="breadcrumb">
                            <li class="active"><a style="color: #232323;">我的待办</a></li>
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
                                                   placeholder="请填写报账单编号" class="form-control" c-model="myTodo.claimNo"
                                                   style="width: 100%;height: auto">
                                        </div>
                                        <div class="display-none">
                                            <div class="form-group">
                                                <label class="font-noraml">起草人</label>
                                                <input type="text" id="applyUsername" name="applyUsername"
                                                       placeholder="请填写起草人姓名" class="form-control" c-model="myTodo.applyUsername"
                                                       style="width: 100%;height: auto">
                                            </div>
                                            <div class="form-group">
                                                <label class="font-noraml">业务类型</label>
                                                <select name="businessType" id="businessType" class="form-control" c-model="myTodo.businessType"></select>
                                            </div>
                                            <div class="form-group">
                                                <label class="font-noraml">是否超出预算</label>
                                                <div style="letter-spacing: 20px">
                                                    <input type="radio" name="hasExceedBudget" c-model="myTodo.hasExceedBudget"
                                                           style="border-radius: 2px;width: 15px;height: 15px;color: #B4D8FF; margin-right: 10px"
                                                           value="1"/>是
                                                    <input type="radio" name="hasExceedBudget" c-model="myTodo.hasExceedBudget"
                                                           style="border-radius: 2px;width: 15px;height: 15px;color: #B4D8FF; margin-right: 10px"
                                                           value="0"/>否
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label class="font-noraml">报账单模板</label>
                                            <select name="itemId1" id="itemId1" class="form-control" c-model="myTodo.itemId1"
                                                    onchange="initItem2List()"></select>
                                        </div>
                                        <div class="display-none">
                                            <div class="form-group">
                                                <label class="font-noraml">起草部门</label>
                                                <input type="text" id="applyUserDepname" name="applyUserDepname" c-model="myTodo.applyUserDepname"
                                                       placeholder="请填写起草部门" class="form-control"
                                                       style="width: 100%;height: auto">
                                            </div>
                                            <div class="form-group">
                                                <label class="font-noraml">合同名称</label>
                                                <input type="text" id="contractName" name="contractName"
                                                       placeholder="请填写合同名称" class="form-control" c-model="myTodo.contractName"
                                                       style="width: 100%;height: auto">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label class="font-noraml">业务大类</label>
                                            <select class="form-control" id="item2Id1" name="item2Id1" c-model="myTodo.item2Id1"
                                                    style="width: 100%;height: auto"></select>
                                        </div>
                                        <div class="display-none">
                                        <div class="form-group">
                                            <label class="font-noraml">报账金额范围</label>
                                            <div class="input-group">
                                                <input type="text" id="currencyMin" name="currencyMin"
                                                       class="input-sm form-control" placeholder="最低金额" c-model="myTodo.currencyMin"
                                                       style=";height: auto"/>
                                                <span class="input-group-addon"
                                                      style="padding: 4px 5px;background-color:#eeeeee">-</span>
                                                <input type="text" id="currencyMax" name="currencyMax"
                                                       class="input-sm form-control" placeholder="最高金额" c-model="myTodo.currencyMax"
                                                       style=";height: auto"/>
                                            </div>
                                        </div>
                                            <div class="form-group">
                                                <label class="font-noraml">合同编号</label>
                                                <input id="contractNo" name="contractNo" class="form-control pd-right20" c-model="myTodo.contractNo"
                                                       placeholder="请填写合同编号" style="width: 100%;height: auto"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label class="font-noraml">供应商名称</label>
                                            <input type="hidden" id="vendorNo" name="vendorNo">
                                            <input id="vendorName" name="vendorName" class="form-control" c-model="myTodo.vendorName"
                                                   placeholder="请填写供应商名称" style="width: 100%;height: auto" readonly="readonly"/>
                                            <i class="glyphicon glyphicon-remove input-search ivendordel" onclick="onResetVendor();"  controlKey="unit"></i>
                                            <i class="glyphicon glyphicon-search input-search ivendorName" onclick="openVendor();"  controlKey="unit"></i>
                                        </div>
                                        <div class="display-none">
                                            <div class="form-group">
                                                <label class="font-noraml">付款金额范围</label>
                                                <div class="input-group">
                                                    <input type="text" id="payAmountMin" name="payAmountMin" c-model="myTodo.payAmountMin"
                                                           class="input-sm form-control" placeholder="最低金额"
                                                           style="height: auto"/>
                                                    <span class="input-group-addon"
                                                          style="padding: 4px 5px;background-color:#eeeeee">-</span>
                                                    <input type="text" id="payAmountMax" name="payAmountMax" c-model="myTodo.payAmountMax"
                                                           class="input-sm form-control" placeholder="最高金额"
                                                           style="height: auto"/>
                                                </div>
                                            </div>
                                            <div class="form-group" id="data_5">
                                                <label class="font-noraml">接收时间</label>
                                                <div class="input-daterange input-group">
                                                    <input type="text" class="form-control Wdate" id="startDate" c-model="myTodo.startDate" autocomplete="off"
                                                           name="startDate" value="" style="height: auto"
                                                           onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'});"/>
                                                    <span class="input-group-addon">-</span>
                                                    <input type="text" class="form-control Wdate" id="endDate" c-model="myTodo.endDate" autocomplete="off"
                                                           name="endDate" value="" style="height: auto"
                                                           onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'});"/>
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
                                                    onclick="$('#dtClaimTodo').bootstrapTable('refresh'); ">查询
                                            </button>
                                            <button type="button" onclick="onReset()"
                                                    style="background-color: #B7B9BB;height: 28px;width: 100px;border: 0px;border-radius: 2px;color: #FFFFFF;font-size: 14px;">
                                                重置
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
                                <table id="dtClaimTodo"
                                       class="table table-striped table-hover q_table table_td_left"
                                       data-classes="table-no-bordered"
                                       data-id-field="id" data-click-to-select="true" data-striped="true"
                                       data-ajax="claimTodoListSearchAjax"
                                       data-side-pagination="server" data-pagination="true" data-page-size="50"
                                       data-height="360" data-show-jumpto="true"data-resizable="true">
                                    <thead>
                                    <tr>
                                        <th data-checkbox="true"></th>
                                        <th data-width="120px" data-field="claimNo" data-formatter="render4TdLink">
                                            报账单号
                                        </th>
                                        <th data-width="90px" data-field="itemName" data-formatter="render4TdLink">
                                            报账单模板
                                        </th>
                                        <th data-width="90px" data-field="item2Id" data-formatter="render4Item2Id">业务大类
                                        </th>
                                        <th data-width="130px" data-field="title" data-formatter="render4TdLink">申请事项</th>
                                        <th data-width="70px" data-field="applyUsername" data-formatter="render4TdLink">
                                            上一处理人
                                        </th>
                                        <th data-width="100px" data-field="applyAmount"
                                            data-formatter="formatterMoney4BootTable" data-class="amountThCss">报账金额
                                        </th>
                                        <th data-width="100px" data-field="payAmount"
                                            data-formatter="formatterMoney4BootTable" data-class="amountThCss">付款金额
                                        </th>
                                        <th data-width="60px" data-field="isHookImage" data-formatter="render4IsIma">影像挂接
                                        </th>
                                        <th data-width="110px" data-field="id" data-formatter="render4open">
                                        </th>
                                        <th data-width="50px" data-field="hasExceedBudget" data-formatter="render4Budget">
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
        <br/><br/><br/>
        <div class="navbar-fixed-bottom"
             style="height: 80px;width:100%;background: #FFFFFF;box-shadow: 0 -2px 10px 0 #C0DFFF;padding: 20px 60px;z-index: 10000;">
            <button type="button"
                    style="background: #48A0FF;border-radius: 2px; border-width: 0px;width: 150px;height: 40px;font-family: MicrosoftYaHei;font-size: 24px;color: #FFFFFF;float: right;" onclick="batchApprove()">
                批量审批
            </button>
        </div>
        <%-- </div>--%>
    </div>
</form>

<script>

    var lang_type = "";
    var curPortalUserGroupId = "<%=currentUser.getCurGroupId()%>";
    var user;
    var item2List;
    var item2List1;
    var item2NameList;
    var commonUiDataBindService4QueryList = new CommonUiDataBindService4QueryList("myTodo");

    $(function () {
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

        $("#dtClaimTodo").bootstrapTable();//初始化表格
    });

    //初始化报账单模板下拉框
    function initClaimTemplate() {
        var url = contextPath + "/dictListAjax.do?&dictId=" + "claimTemplate";
        baseService.generateSelectElement({
            selectId: "itemId1"
        }, url, "code", "value");
    }
    //初始化业务类型下拉框
    function initBussinessList() {
        var url = contextPath + "/dictListAjax.do?&dictId=" + "businessType";
        baseService.generateSelectElement({
            selectId: "businessType"
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
    //报账单号跳转
    function openClaim(url) {
        skipUrl(url);
    }
    function doNewUrl(url){
        var itemId = getParam(url,"itemId");
        var LowerItemId = itemId.toLowerCase();
        var newUrl = contextPath + "/newPages/efinance/claim/" + itemId + "/" + LowerItemId + "_audit.jsp?" + url+generateSearchUrl($("#queryForm").serializeObject(),"dtClaimTodo");
        newUrl = newUrl + "&claimNowStatus=daiban"; //追加本环节为代办
        window.location.href = newUrl;
    }
    function doOldUrl(url){
        var urlData = url;
        urlData = urlData.replace(/&/g,"|");
        url = url + "&url=" + urlData;
        url = url + "&fromMenu=1"+generateSearchUrl($("#queryForm").serializeObject(),"dtClaimTodo");
        url = url + "&claimNowStatus=daiban"; //追加本环节为代办
        UiBlockService.blockUI4Load();
        window.location.href = contextPath+doProcessUrl(url);
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

        var paramUrl = contextPath + "/commonbizajax2report.do?requestId=FindTodoList";
        var inputMapModel="item2List,item2List1,item2NameList";
        var data={inputMapModel : inputMapModel };

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
        item2NameList = inputMap.item2NameList;
        item2List1 = inputMap.item2List1;
    }

    function render4Item2Id(value, row, index, field) {
        var itemId = "T" + row.item2Id.substr(0, 3);
        var item2Name = getCatelevel2Value(value, itemId);
        var newUrl = getNewUrl(value, row);
        var oldUrl = getOldUrl(value, row);
        var createTime = row.createTime;
        return createTdLinkCommon("openClaim('" + oldUrl + "')", item2Name);
    }
    //获取新的跳转url
    function getNewUrl(value, row) {
        var url = row.url + '&nextIsSingle=' + row.isSingle + '&businessSubType=' + row.businessSubType;
        url = url.substr(url.indexOf("item2Id"));
        return url;
    }
    //获取旧的跳转url
    function getOldUrl(value, row) {
        return row.url;
    }

    function render4TdLink(value, row, index, field) {
        var itemId = "T" + row.item2Id.substr(0, 3);
        var newUrl = getNewUrl(value, row);
        var oldUrl = getOldUrl(value, row);
        var createTime = row.createTime;
        return createTdLinkCommon("openClaim( '" + oldUrl + "')", value);
    }

    function render4IsIma(value, row, index, field) {
        var result = "";
        if(value == "1"){
            result ="是";
        }else{
            result ="否";
        }
        return result;
    }
    function render4Budget(value, row, index, field) {
        if(value == "是"){
            //var imgUrl =contextPath+"/newstatic/img/icon/shape.png";
            return '<img class="warning" src= "'+contextPath+'/newstatic/img/icon/shape.png"/><div><font size="1" color="red">预算超支</font></div>';
        }else{
            return "";
        }
    }
    function render4open(value, row, index, field){
        var activityDefID = row.activityDefID;
        var batchApproveOpt = row.batchApproveOpt;
        var itemId = "T" + row.item2Id.substr(0, 3);
        var newUrl = getNewUrl(value, row);
        var oldUrl = getOldUrl(value, row);
        var createTime = row.createTime;
        var result = '<a href="javascript:void(0)" onclick="openClaim( \''+ oldUrl +'\')" title="详情"><span class="image"><img src="'+contextPath+'/newstatic/img/icon/q/fdjss.png"/></span></a>';
        if(activityDefID != "drafterActivity"){
            if(activityDefID.indexOf("drafterOperateActivity") == -1){
                if(batchApproveOpt != "PackBA"){
                    var claimId = row.claimId;
                    var claimNo = row.claimNo;
                    var processInstID = row.processInstID;
                    var activityInstID = row.activityInstID;
                    var taskInstID = row.taskInstID;
                    var activityDefID = row.activityDefID;
                    var item2Id = row.item2Id;
                    var isSingle = row.isSingle;
                    var id = row.id;
                    var businessObjectID = row.businessObjectID;
                    var businessType = row.businessType;
                    var businessSubType = row.businessSubType;

                    var activityInstID = row.activityInstID;
                    var processDefName = row.processDefName;
                    result += '<a href="javascript:void(0)" onclick="engineValidate(\''+claimId+'\',\''+processInstID+'\',\''+activityInstID+'\',\''+taskInstID+'\',\''+activityDefID+'\',\''+item2Id+'\',\''+isSingle+'\',\''+id+'\',\''+businessObjectID+'\',\''+businessType+'\',\''+businessSubType+'\')" title="同意"><span class="image"><img src="'+contextPath+'/newstatic/img/icon/q/ok.png"/></span></a>';
                    result += '<a href="javascript:void(0)" onclick="tuihui(\''+claimId+'\',\''+item2Id+'\',\''+processInstID+'\',\''+activityInstID+'\',\''+taskInstID+'\',\''+processDefName+'\',\''+activityDefID+'\',\''+isSingle+'\',\''+id+'\',\''+businessObjectID+'\',\''+businessType+'\',\''+businessSubType+'\')" title="退回"><span class="image"><img src="'+contextPath+'/newstatic/img/icon/q/fdjss2.png"/></span></a>';
                }
            }
        }
        return result;
    }
    
	var category1;
	var category2;
	var category3;
	var category4; 
	var submitBlockFlag = false;
	var validateStatus = "1";//ok  
    
    function loadEngineData(claimId) {
    	submitBlockFlag = false;
    	//对账完成
        var url = contextPath + "/commonbizajax.do?requestId=engineRuleResultView";
        var data = {claimId:claimId};
    	
		$.ajax({
	        url: url,
	        type: "post",
	        data:data ,
	        async: false,
	        success : function(result){
	            if (result.status == AjaxResultFlag.OK) {
	                
	                var validateSummaryMap = JSON.parse(result.response).output;
	                console.log(validateSummaryMap);
	                
	                //初始化表格
			  		category1 = validateSummaryMap["1"];
			  		category2 = validateSummaryMap["2"];
			  		category3 = validateSummaryMap["3"];
			  		category4 = validateSummaryMap["4"];
	                
			  		validateEngineResultCall();

	            }else{
	            	try{
	            		MessageBox.error(JSON.parse(result.errors)[0].message);
	            	}catch(e){
	            		
	            	}
	            	UiBlockService.unblockUI();
	            }
	        }
	    });
    }	
    
    function validateEngineResultCall(){
		
    	processSubmitBlockFlag(category1);
    	processSubmitBlockFlag(category2);
    	processSubmitBlockFlag(category3);
    	processSubmitBlockFlag(category4);
    	
    }
    
    //处理报警级别
    function processSubmitBlockFlag(category){
    	if(typeof category != "undefined"){
    		if(category.submitBlockFlag){
    			submitBlockFlag = true;
    		}
    		if(category.validateStatus == '3'){
    			validateStatus = '3';
    		}else if(category.validateStatus == '2' && validateStatus == '1'){
    			validateStatus = '2';
    		}
    	}    	
    }      
    
    //规则引擎做拦截
    function engineValidate(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType){
    	UiBlockService.blockUI4Load();
    	//TODO 检查规则引擎
		loadEngineData(claimId);
		UiBlockService.unblockUI();
		//检查是否可以提交
  		if(submitBlockFlag){
  			MessageBox.error("规则引擎校验不通过请检查。");
  		}else if(validateStatus == '2'){
            MessageBox.confirm({
                message: "规则引擎校验结果存在弱控，请确认是否继续提交？",
                action4Yes: function action4Yes() {
                	wfsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType);
                }
            },"确认","返回");
  		}else{
  			wfsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType);
  		}
    	    	
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

    $.page =  $.page||{};

    //-- T058 流程提交
    $.page.nextProcess =function ( claimId) {
        UiBlockService.blockUI({message: "<h4>正在发送数据，请稍等...</h4>"});
        var url = contextPath + "/newPages/efinance/claim/T058/nextProcess.jsp?";
        url += "&claimId=" +claimId;
        showSimpleDivBox2(700,200,"流程提交",url,5,"");
        $("#popupButtonDiv").remove();
        $("#closeBtn").hide();
    };

    //-- 驳回
    $.page.rejectProcess =function (claimId ) {

        MessageBox.confirm({
            message: "是否确认 退回起草人?",
            action4Yes: reject,
            action4No: function action4No() {}
        });

        function reject(){

            UiBlockService.blockUI({message: "<h4>正在发送数据，请稍等...</h4>"});

            showSimpleDivBox2(600,433,"\u63d0\u4ea4","",5,"");

            $("#popupButtonDiv").remove();
            $("#closeBtn").hide();

            UiBlockService.unblockUI();

            var url = contextPath + "/commonbizajax2report.do";

            var params = {};
            params.requestId = "T058$reject";
            params.claimId = claimId;
            params.approvaltext = "驳回";
            Ajax.post().url(url).data(params).success(success).do();
            function success(res, isOk) {
                res= res||{};
                $("#closeBtn").click();
                isOk ||  MessageBox.error( res.message);//--提交失败 : 则关闭窗口
                isOk &&  callBack();//--成功刷新列表
                function callBack(){
                    MessageBox.notice( res.message);
                    $('#dtClaimTodo').bootstrapTable('refresh');
                }
            }
        }

    };

    function wfsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType) {

        if( 0 === item2Id.indexOf( "T058") ){
            return  $.page.nextProcess(claimId);
        }

        var jsonUser = $.parseJSON(user);
        var url = "";
        if (businessType == "claim") {
            url = contextPath +"/service.do?requestId=taskInstQueryNextActivityDefFacadeService&claimId=" + claimId + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=" + item2Id + "&pendingID=" + pendingID + "&cType=auto" + "&taskInstID=" + taskInstID;

        } else if (businessType == "prvMessage") {
            if (businessSubType == "head") {
                url = "审批", contextPath+"/service.do?requestId=oTHTaskInstQueryNextActivityDefFacadeService&businessObjectID=" + businessObjectID + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=" + item2Id + "&pendingID=" + pendingID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType;
            } else if (businessSubType == "line") {
                url = contextPath+"/service.do?requestId=oTHTaskInstQueryNextActivityDefFacadeService&businessObjectID=" + businessObjectID + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=505001&pendingID=" + pendingID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType;
            }
        }
        //增加列表查询条件
        url = combination(url, "claimNo");
        url = combination(url, "itemId");
        url = combination(url, "targetSelect");
        url = combination(url, "vendorName");
        url = combination(url, "contractName");
        url = combination(url, "contractNo");
        url = combination(url, "applyUsername");
        url = combination(url, "moneyFrom");
        url = combination(url, "moneyTo");
        url = combination(url, "processState");
        url = combination(url, "startDate");
        url = combination(url, "endDate");
        url = combination(url, "orgId");
        url = combination(url, "segment1");
        url = combination(url, "itemId1");
        url = combination(url, "item2Id1");
        url = combination(url, "targetSelect");
        url = combination(url, "currencyMin");
        url = combination(url, "currencyMax");
        url = combination(url, "vendorNo");
        url = combination(url, "payAmountMin");
        url = combination(url, "applyUserDepname");
        url = combination(url, "payAmountMax");
        url = combination(url, "businessType");
        url = combination(url, "IsAdvancedSerach");
        url = url + "&queryItem2Id1=" + document.getElementById("item2Id1").value;


        if (businessType == "claim") {

            XMLHttp.sendReq("post", contextPath+"/ajax.do?serviceBean=taskInstQueryNextActivityDefFacadeService&methodName=queryNextActivity&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&claimId=" + claimId + "&activityDefID=" + activityDefID, "", function (http_request) {
                var rspText = http_request.responseText;
                var isTrue = false;
                if (rspText != "") {
                    var results = http_request.responseXML;
                    var rsflag = results.getElementsByTagName("rsflag")[0].childNodes;
                    var flag = getNodeValue(rsflag[0]);

                    var rsnextid = results.getElementsByTagName("rsnextid")[0].childNodes;
                    var nextid = getNodeValue(rsnextid[0]);

                    var rsnextname = results.getElementsByTagName("rsnextname")[0].childNodes;
                    var nextname = getNodeValue(rsnextname[0]);

                    var rsviewuser = results.getElementsByTagName("rsviewuser")[0].childNodes;
                    var viewuser = getNodeValue(rsviewuser[0]);

                    var rsnextuser = results.getElementsByTagName("rsnextuser")[0].childNodes;
                    var nextuser = getNodeValue(rsnextuser[0]);

                    var currid = activityDefID;

                    if (flag == "single") {
                        //显示确认信息页面
                        isTrue = true;
                        confirmSubmitUrl = url;
                        var url_confirm = contextPath+"/pages/workflow/confirmSubmit.jsp?claimId=" + claimId + "&item2Id=" + item2Id + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&pendingID=" + pendingID + "&nextname=" + encodeURI(nextname) + "&nextuser=" + encodeURI(nextuser) + "&viewuser=" + encodeURI(viewuser);
                        showSimpleDivBox41(500, 390, "审批", url_confirm, 5, "doConfirmWfSubmit", 0, 0);

                    } else if (flag == "singleNotConfirm") {
                        //增加提交时差不能小于30秒限制
                        if (!checkAndSetCookie(claimId, jsonUser.userid, jsonUser.curGroupId, activityDefID, pendingID)) {
                            MessageBox.notice("此操作已经提交，请稍后查看。");
                            return;
                        }
                        isTrue = false;
                        wfRealsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType);

                    } else if (flag == "notsingle") {
                        isTrue = false;
                        wfRealsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType);

                    } else if (flag == "error") {
                        isTrue = false;
                        MessageBox.error(nextuser);

                    } else {
                        isTrue = false;
                        MessageBox.error("\u51FA\u73B0\u5F02\u5E38\uFF0C\u67E5\u8BE2\u4E0B\u4E00\u73AF\u8282\u5931\u8D25\uFF0C\u672A\u5F97\u5230\u73AF\u8282\u540D\uFF0C\u8BF7\u7A0D\u540E\u91CD\u8BD5\uFF01");

                    }
                } else {
                    isTrue = false;
                    //查询后续环节错误
                    MessageBox.error("\u51FA\u73B0\u5F02\u5E38\uFF0C\u67E5\u8BE2\u4E0B\u4E00\u73AF\u8282\u5931\u8D25\uFF0C\u672A\u5F97\u5230\u73AF\u8282\u540D\uFF0C\u8BF7\u7A0D\u540E\u91CD\u8BD5\uFF01");

                }

            });

        } else {

            showSimpleDivBox3(550, 390, "审批", url, 5, "");
        }

    }

    function wfRealsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType) {
        var url = "";
        var jsonUser = $.parseJSON(user);
        if (businessType == "claim") {
            url = contextPath+"/service.do?requestId=taskInstQueryNextActivityDefFacadeService&claimId=" + claimId + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=" + item2Id + "&pendingID=" + pendingID + "&cType=auto";
        } else if (businessType == "prvMessage") {
            if (businessSubType == "head") {
                url = "审批", contextPath+"/service.do?requestId=oTHTaskInstQueryNextActivityDefFacadeService&businessObjectID=" + businessObjectID + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=" + item2Id + "&pendingID=" + pendingID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType;
            } else if (businessSubType == "line") {
                url = contextPath+"/service.do?requestId=oTHTaskInstQueryNextActivityDefFacadeService&businessObjectID=" + businessObjectID + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=505001&pendingID=" + pendingID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType;
            }
        }
        //增加列表查询条件
        url = combination(url, "claimNo");
        url = combination(url, "itemId");
        url = combination(url, "targetSelect");
        url = combination(url, "vendorName");
        url = combination(url, "contractName");
        url = combination(url, "contractNo");
        url = combination(url, "applyUsername");
        url = combination(url, "moneyFrom");
        url = combination(url, "moneyTo");
        url = combination(url, "processState");
        url = combination(url, "startDate");
        url = combination(url, "endDate");
        url = combination(url, "orgId");
        url = combination(url, "segment1");
        url = combination(url, "itemId1");
        url = combination(url, "item2Id1");
        url = combination(url, "targetSelect");
        url = combination(url, "currencyMin");
        url = combination(url, "currencyMax");
        url = combination(url, "vendorNo");
        url = combination(url, "payAmountMin");
        url = combination(url, "applyUserDepname");
        url = combination(url, "payAmountMax");
        url = combination(url, "businessType");
        url = combination(url, "IsAdvancedSerach");
        url = url + "&queryItem2Id1=" + document.getElementById("item2Id1").value;
        showSimpleDivBox3(550, 390, "审批", url, 5, "");
    }

    function tuihui(claimId, item2Id, processInstID, activityInstID, taskInstID, processDefName, activityDefID, isSingle, pendingID, businessObjectID, businessType, businessSubType) {

        if( 0 === item2Id.indexOf( "T058") ){
            return  $.page.rejectProcess(claimId);
        }

        var url = "";
        if (businessType == "claim") {
            url = contextPath+"/service.do?requestId=processBackQueryFacadeService&taskInstID=" + taskInstID + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&processDefName=" + processDefName + "&activityDefID=" + activityDefID + "&pendingID=" + pendingID + "&claimId=" + claimId + "&cType=auto&item2Id=" + item2Id;
        } else if (businessType == "prvMessage") {
            if (businessSubType == "head") {
                url = contextPath+"/service.do?requestId=oTHProcessBackQueryFacadeService&taskInstID=" + taskInstID + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&processDefName=" + processDefName + "&activityDefID=" + activityDefID + "&pendingID=" + pendingID + "&businessObjectID=" + businessObjectID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType + "&item2Id=" + item2Id;
            } else if (businessSubType == "line") {
                url = contextPath+"/service.do?requestId=oTHProcessBackQueryFacadeService&taskInstID=" + taskInstID + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&processDefName=" + processDefName + "&activityDefID=" + activityDefID + "&pendingID=" + pendingID + "&businessObjectID=" + businessObjectID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType + "&item2Id=" + item2Id;
            }
        }
        //增加列表查询条件
        url = combination(url, "claimNo");
        url = combination(url, "itemId");
        url = combination(url, "targetSelect");
        url = combination(url, "vendorName");
        url = combination(url, "contractName");
        url = combination(url, "contractNo");
        url = combination(url, "applyUsername");
        url = combination(url, "moneyFrom");
        url = combination(url, "moneyTo");
        url = combination(url, "processState");
        url = combination(url, "startDate");
        url = combination(url, "endDate");
        url = combination(url, "orgId");
        url = combination(url, "segment1");
        url = combination(url, "itemId1");
        url = combination(url, "item2Id1");
        url = combination(url, "targetSelect");
        url = combination(url, "currencyMin");
        url = combination(url, "currencyMax");
        url = combination(url, "vendorNo");
        url = combination(url, "payAmountMin");
        url = combination(url, "payAmountMax");
        url = combination(url, "businessType");
        url = combination(url, "IsAdvancedSerach");
        url = url + "&queryItem2Id1=" + document.getElementById("item2Id1").value;
        showSimpleDivBox3(500, 390, "退回", url, 5, "getVendorData");
    }
    //批量审批
    function batchApprove(){
        var claimList = $.map($('#dtClaimTodo').bootstrapTable('getSelections'), function (row) {
            return row;
        });
        var claimIds="";
        var count=0;
        var selectCount=0;
        for(i=0;i<claimList.length;i++){
                selectCount=selectCount+1;
                var claim =claimList[i];
                var item2Id=claim.item2Id;
                var claimId=claim.claimId;
                var processStateEng=claim.activityDefID;

                if(item2List.indexOf(item2Id)>=0 &&processStateEng=="departmentManagerActivity"){
                    count=count+1;
                    if(claimIds==""){
                        claimIds= claimId;
                    }else{
                        claimIds=claimIds+","+claimId;
                    }
                }
                if(item2List1.indexOf(item2Id)>=0&&processStateEng=="directorActivity"){
                    count=count+1;
                    if(claimIds==""){
                        claimIds= claimId;
                    }else{
                        claimIds=claimIds+","+claimId;
                    }
                }

        }
        if(count==0){
            MessageBox.notice("只有业务大类名称为"+item2NameList+"并且处于‘部门业务负责人审批’,‘分管领导’状态的报账单才能进行批量审批！");
            return false;
        }
        $("#batch").prop("disabled", "disabled");
        UiBlockService.blockUI({message:"<h4>请求处理中.请稍候...</h4>"});
        var url=contextPath+"/service.do?requestId=batchApproval&claimIds="+claimIds+"&selectCount="+selectCount+"&count="+count;
        window.location.href =doProcessUrl(url);
        $("#batch").removeProp("disabled");

    }
</script>
</body>
</html>
