package com.yili.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ANDY on 2016/12/3.
 */
public class MobileNextActivityVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String claimId;
	private String pendingId;
	private String nextActivityId;
	private String nextActivityName;
	private List<MobileParticipantVo> nextParticipant;

	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public String getPendingId() {
		return pendingId;
	}
	public void setPendingId(String pendingId) {
		this.pendingId = pendingId;
	}

	public String getNextActivityId() {
		return nextActivityId;
	}
	public void setNextActivityId(String nextActivityId) {
		this.nextActivityId = nextActivityId;
	}

	public String getNextActivityName() {
		return nextActivityName;
	}
	public void setNextActivityName(String nextActivityName) {
		this.nextActivityName = nextActivityName;
	}

	public List<MobileParticipantVo> getNextParticipant() {
		return nextParticipant;
	}
	public void setNextParticipant(List<MobileParticipantVo> nextParticipant) {
		this.nextParticipant = nextParticipant;
	}
}
