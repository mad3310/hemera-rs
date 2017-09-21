package com.le.matrix.hemera.service.common.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.le.matrix.hemera.constant.Constant;
import com.le.matrix.hemera.facade.IBaseTaskService;
import com.le.matrix.hemera.model.RESTfulResult;
import com.le.matrix.hemera.model.TaskResult;
import com.le.matrix.hemera.util.StatusHttpClient;

@Service("taskCommonCreateService")
public class TaskCommonCreateServiceImpl extends BaseTaskCommonServiceImpl implements IBaseTaskService{
	
	private final static Logger logger = LoggerFactory.getLogger(TaskCommonCreateServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params, String url, String result) throws Exception{
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;
			
		//拼接调用参数
		String postJsonParams = JSONObject.toJSONString(params);
		
		//http调用
		RESTfulResult invokeResult = StatusHttpClient.postObject(url, postJsonParams, Constant.CONNECTION_TIMEOUT, Constant.SO_TIMEOUT, null);
		
		//分析执行结果
		tr = analyzeHttpStatusRESTfulResult(invokeResult);
		if (tr.isSuccess()) {
			logger.debug("请求创建服务成功");
		}
		tr.setParams(params);
		return tr;
	}
	
	
}
