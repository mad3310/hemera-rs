package com.le.matrix.hemera.service.common.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.matrix.hemera.constant.Constant;
import com.le.matrix.hemera.facade.IBaseTaskService;
import com.le.matrix.hemera.model.RESTfulResult;
import com.le.matrix.hemera.model.TaskResult;
import com.le.matrix.hemera.util.StatusHttpClient;

@Service("taskCommonSendUserEmailService")
public class TaskCommonSendUserEmailServiceImpl extends BaseTaskCommonServiceImpl implements IBaseTaskService{
	
	private final static Logger logger = LoggerFactory.getLogger(TaskCommonSendUserEmailServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params, String url, String result) throws Exception{
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;
			
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("id", String.valueOf(params.get("id")));
		
		//http调用
		RESTfulResult invokeResult = StatusHttpClient.get(url, postParams, Constant.CONNECTION_TIMEOUT, Constant.SO_TIMEOUT);
		
		//分析执行结果
		tr = analyzeHttpStatusRESTfulResult(invokeResult);
		
		if (tr.isSuccess()) {
			logger.debug("发送用户邮件成功");
		}
		tr.setParams(params);
		tr.setResult(result);
		return tr;
	}
	
	@Override
	public void callBack(TaskResult tr) {
		super.finish(tr);
	}
	
}
