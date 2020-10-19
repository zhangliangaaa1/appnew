function smartClaimControl(){
	//如果该字段有值，则表示是智能填单自动生成的明细，则不允许编辑金额字段
	if(!validateEleIsNull(claimLineEditService.dataObject.assAppCreateFlagId)){
		handEleDisavledAndRenewOfInput("foreignApplyAmount",true);
	}
}