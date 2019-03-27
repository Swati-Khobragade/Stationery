package com.gl.adminportal.stationery.enums;

import com.gl.adminportal.stationery.model.Request;

/** 
 * This Enum is used for representing the state of an {@link Request}<br>
 * A {@link Request} can have three state's <br>
 * Either <strong>CREATED,PENDING APPROVAL,DELIVERED</strong>
 * 
 * @author anand.kadhi
 *
 */
public enum REQUESTSTATE {

	/**
	 * Request is in CREATED state.
	 */
	CREATED("CREATED"),
	
	/**
	 * Request is PENDING_APPROVAL..
	 */
	PENDING_APPROVAL("PENDING_APPROVAL"),
	
	/**
	 * Request has been DELIVERED and completed.
	 */
	DELIVERED("DELIVERED"),
	
	/**
	 * Request has been REJECTED.
	 */
	REJECTED("REJECTED"),
	
	/**
	 * Request has been REJECTED.
	 */
	APPROVED("APPROVED");
	
	/** Variable for holding the state **/
	private String state;
	
	private REQUESTSTATE(String state){
		this.state=state;	
	}
	
	/**
	 * Return the equivalent {@link REQUESTSTATE} the request in providing the state.
	 * <br><b>Note : </b> The item operation provided can be case insensetive. 
	 * 
	 * @param operation
	 * @return
	 */
	public REQUESTSTATE getRequestState(String state){
		for(REQUESTSTATE requestState:REQUESTSTATE.values()){
			if(state.equalsIgnoreCase(requestState.getRequestState())){
				return requestState;
			}			
		}
		return null;
	}
	
	/**
	 * The item operation which is holded by the 
	 * current object.
	 * 
	 * @return
	 */
	private String getRequestState(){
		return this.state;
	}
	
	
}
