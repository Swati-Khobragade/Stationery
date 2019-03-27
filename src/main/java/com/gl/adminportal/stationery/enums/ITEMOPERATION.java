package com.gl.adminportal.stationery.enums;

/**
 * This Enum tells us the operation on the item
 * being performed. That is whether the item is being 
 * ADDED or UPDATED or DELETED.
 * 
 * @author anand.kadhi
 *
 */
public enum ITEMOPERATION {

	/**
	 * ADD the item to inventory.
	 */
	ADD("ADD"),
	
	/**
	 * UPDATE the item existing in inventory.
	 */
	UPDATE("UPDATE"),
	
	/**
	 * DELETE the item from inventory.
	 */
	DELETE("DELETE");
	
	private String operation;
	
	private ITEMOPERATION(String operation){
		this.operation=operation;
	}
	
	/**
	 * Return the equivalent {@link ITEMOPERATION} being performed providing the operation
	 * being performed.
	 * <br><b>Note : </b> The item operation provided can be case insensetive. 
	 * 
	 * @param operation
	 * @return
	 */
	public ITEMOPERATION getItemOpeation(String operation){
		for(ITEMOPERATION itemoperation:ITEMOPERATION.values()){
			if(operation.equalsIgnoreCase(itemoperation.getOperation())){
				return itemoperation;
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
	private String getOperation(){
		return this.operation;
	}
}
