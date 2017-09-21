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

@Service("taskCommonSaveInfoService")
public class TaskCommonSaveInfoServiceImpl extends BaseTaskCommonServiceImpl implements IBaseTaskService{
	
	private final static Logger logger = LoggerFactory.getLogger(TaskCommonSaveInfoServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params, String url, String result) throws Exception{
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;
			
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("id", String.valueOf(params.get("id")));
		postParams.put("serviceId", result);
		
		//http调用
		RESTfulResult invokeResult = StatusHttpClient.get(url, postParams, Constant.CONNECTION_TIMEOUT, Constant.SO_TIMEOUT);
		
		//分析执行结果
		tr = analyzeHttpStatusRESTfulResult(invokeResult);
		
		if (tr.isSuccess()) {
			logger.debug("保存创建服务信息成功");
		}
		tr.setParams(params);
		tr.setResult(result);
		return tr;
	}
	
}
