package com.le.matrix.hemera.service.common.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.le.matrix.hemera.facade.IBaseTaskService;
import com.le.matrix.hemera.model.TaskResult;
import com.le.matrix.hemera.service.impl.BaseTaskServiceImpl;

@Component("baseTaskCommonService")
public class BaseTaskCommonServiceImpl extends BaseTaskServiceImpl implements IBaseTaskService{

	private final static Logger logger = LoggerFactory.getLogger(BaseTaskCommonServiceImpl.class);
	
	@Override
	public void beforeExecute(Map<String, Object> params) {
		
	}
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		return validator(params);
	}

	@Override
	public void rollBack(TaskResult tr) {
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public void finish(TaskResult tr) {
		Map<String,Object> params = (Map<String, Object>) tr.getParams();
		//给管理员发送邮件
		this.buildResultToMgr(params.get("username")==null? "" : String.valueOf(params.get("username"))+"申请服务", tr.isSuccess()?"创建成功":"创建失败", String.valueOf(params.get("name")), SERVICE_NOTICE_MAIL_ADDRESS);
	}
	
}
