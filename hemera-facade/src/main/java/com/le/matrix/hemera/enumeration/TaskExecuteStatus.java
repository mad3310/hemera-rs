package com.le.matrix.hemera.enumeration;

import com.le.matrix.redis.enumeration.IntEnum;


public enum TaskExecuteStatus implements IntEnum {
	FAILED(0),
	SUCCESS(1),
	DOING(2),
	WAITTING(3),
	UNDO(4);
	
	private final Integer value;
	
	private TaskExecuteStatus(Integer value) {
		this.value = value;
	}
	
	@Override
	public Integer getValue() {
		return this.value;
	}

}
