package com.le.matrix.hemera.model;

import com.letv.common.model.BaseModel;

public class TaskResult extends BaseModel {

	private static final long serialVersionUID = 6232740793479016919L;

	private boolean isSuccess = true;
	
	private Object params; //json params
	
	private String result; //failed or success message.

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
