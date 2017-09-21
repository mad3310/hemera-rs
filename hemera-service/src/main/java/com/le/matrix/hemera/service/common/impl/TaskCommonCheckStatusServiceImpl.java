package com.le.matrix.hemera.service.common.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.matrix.hemera.constant.Constant;
import com.le.matrix.hemera.facade.IBaseTaskService;
import com.le.matrix.hemera.model.RESTfulResult;
import com.le.matrix.hemera.model.TaskResult;
import com.le.matrix.hemera.util.StatusHttpClient;
import com.letv.common.util.RetryUtil;
import com.letv.common.util.function.IRetry;

@Service("taskCommonCheckStatusService")
public class TaskCommonCheckStatusServiceImpl extends BaseTaskCommonServiceImpl implements IBaseTaskService{

	private final static Logger logger = LoggerFactory.getLogger(TaskCommonCheckStatusServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params, String url, String result) throws Exception{
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;
		
		//查询创建状态(返回正确结果结束或超时结束)
		Map<String, Object> obj = retry(url, result);
		
		//分析查询结果并保存
		if((Boolean)obj.get("judgeAnalyzeResult")) {//分析结果正常
			tr = (TaskResult) obj.get("analyzeResult");
			logger.debug("查询结果正常");
		} else {
			tr.setResult("check time over:"+url);
			tr.setSuccess(false);
		}
		tr.setParams(params);
		tr.setResult(result);
		return tr;
	}
	
	private Map<String, Object> retry(final String url, final String result) {
		IRetry<Object, Boolean> iRetry = new IRetry<Object, Boolean>() {
			@Override
			public Object execute() {
				return StatusHttpClient.get(url.replace("{id}", result), Constant.CONNECTION_TIMEOUT, Constant.SO_TIMEOUT);
			}
			
			@Override
			public Object analyzeResult(Object r) {
				return analyzeHttpStatusRESTfulResult((RESTfulResult) r);
			}
			
			@Override
			public Boolean judgeAnalyzeResult(Object o) {
				return ((TaskResult)o).isSuccess();
			}
		};
		
		Map<String, Object> obj = RetryUtil.retryByTime(iRetry, Constant.CHECK_TOTAL_TIME, Constant.CHECK_INTERVAL_TIME);
		
		return obj;
	}
	
}
