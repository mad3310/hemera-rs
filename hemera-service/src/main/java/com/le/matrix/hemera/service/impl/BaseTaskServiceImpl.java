package com.le.matrix.hemera.service.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.le.matrix.hemera.constant.Constant;
import com.le.matrix.hemera.facade.IBaseTaskService;
import com.le.matrix.hemera.model.RESTfulResult;
import com.le.matrix.hemera.model.TaskResult;
import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.email.bean.MailMessage;
import com.letv.common.exception.ValidateException;
import com.letv.common.result.ApiResultObject;

@Component("baseTaskService")
public class BaseTaskServiceImpl implements IBaseTaskService{

	@Value("${service.notice.email.to}")
	protected String SERVICE_NOTICE_MAIL_ADDRESS;
	@Autowired
	protected ITemplateMessageSender defaultEmailSender;
	@Autowired
	private SchedulingTaskExecutor threadPoolTaskExecutor;

	private final static Logger logger = LoggerFactory.getLogger(BaseTaskServiceImpl.class);
	
	@Override
	public TaskResult validator(Map<String, Object> params) throws Exception {
		logger.debug("Validate params:{}",params.toString());
		TaskResult tr = new TaskResult();
		if(CollectionUtils.isEmpty(params)) {
			tr.setSuccess(false);
			tr.setResult("params is empty");
		}
		tr.setParams(params);
		return tr;
	}
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		return this.validator(params);
	}
	
	@Override
	public TaskResult execute(Map<String, Object> params, String url, String result) throws Exception {
		return this.validator(params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public TaskResult analyzeRestServiceResult(ApiResultObject resultObject){
		logger.debug("Analyze apiResultObject:{}",resultObject.toString());
		TaskResult taskResult = new TaskResult();
		if(resultObject == null || StringUtils.isEmpty(resultObject.getUrl())){
			taskResult.setSuccess(false);
			taskResult.setResult("Analyze apiResultObject failed: apiResultObject is null");
			return taskResult;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			resultMap = fromJson(resultObject.getResult());
		}catch(Exception ex){
			taskResult.setSuccess(false);
			taskResult.setResult(MessageFormat.format("Analyze apiResultObject failed: {0}",ex.getMessage()));
			return taskResult;
		}
		if(CollectionUtils.isEmpty(resultMap)) {
			taskResult.setSuccess(false);
			taskResult.setResult(MessageFormat.format("Analyze apiResultObject from {0} failed: result is null",resultObject.getUrl()));
			return taskResult;
		}
		Map<String,Object> metaMap = (Map<String, Object>) resultMap.get("meta");
		if(CollectionUtils.isEmpty(metaMap)){
			taskResult.setSuccess(false);
			taskResult.setResult(MessageFormat.format("Analyze apiResultObject from {0} failed: meta property is not found,when json is {1}",resultObject.getUrl(),resultObject.getResult()));
			return taskResult;
		}
		String code = metaMap.get("code").toString();
		if(StringUtils.isEmpty(code)){
			taskResult.setSuccess(false);
			taskResult.setResult(MessageFormat.format("Analyze apiResultObject from {0} failed: code property is not found,when json is {1}",resultObject.getUrl(),resultObject.getResult()));
			return taskResult;
		}
		boolean isSucess = Constant.PYTHON_API_RESPONSE_SUCCESS.equals(code);
		taskResult.setSuccess(isSucess);
		if(isSucess){
			Map<String,Object> responseMap = (Map<String, Object>) resultMap.get("response");
			if(CollectionUtils.isEmpty(responseMap)){
				taskResult.setSuccess(false);
				taskResult.setResult(MessageFormat.format("Analyze apiResultObject from {0} failed: response property is not found,when json is {1}",resultObject.getUrl(),resultObject.getResult()));
				return taskResult;
			}else{
				String successMsg = (String) responseMap.get("message");
				taskResult.setParams(responseMap);
				taskResult.setResult(StringUtils.isEmpty(successMsg)?"opera successfully!":successMsg);
			}
		}else{
			taskResult.setResult(
					MessageFormat.format("When api url is {0}, The data on failure. The error message is as follows:{1}", 
							resultObject.getUrl(),resultObject.getResult()));
			/*Integer errorType =  (Integer) metaMap.get("errorType");
			String errorDetail = (String) metaMap.get("errorDetail");
			StringBuffer sb = new StringBuffer(MessageFormat.format("When api url is {0},The data on failure.", resultObject.getUrl()));
			if(errorType != null){
				sb.append(MessageFormat.format(" errorType:{0}.", errorType));
			}
			if(!StringUtils.isEmpty(errorDetail)){
				sb.append(MessageFormat.format(" errorDetail:{0}.", errorDetail));
			}
			taskResult.setResult(sb.toString());*/
		}
		return taskResult;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public TaskResult analyzeComplexRestServiceResult(ApiResultObject resultObject){
		TaskResult taskResult = this.analyzeRestServiceResult(resultObject);
		if(!taskResult.isSuccess())
			return taskResult;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = fromJson(resultObject.getResult());
		} catch (Exception ex) {
			//analyzeRestServiceResult方法已经判断过，所以不需要在判断
		}
		Map<String,Object> responseMap = (Map<String, Object>) resultMap.get("response");
		String resultCode = responseMap.get("code").toString();
		if(StringUtils.isEmpty(resultCode)){
			taskResult.setSuccess(false);
			taskResult.setResult(MessageFormat.format("Analyze apiResultObject from {0} failed: response's code property is not found,when json is {1}",resultObject.getUrl(),resultObject.getResult()));
			return taskResult;
		}
		boolean isSucess = Constant.PYTHON_API_RESULT_SUCCESS.equals(resultCode);
		taskResult.setSuccess(isSucess);
		if(isSucess) {
			String successMsg = (String) responseMap.get("message");
			taskResult.setParams(responseMap);
			taskResult.setResult(StringUtils.isEmpty(successMsg)?"opera successfully!":successMsg);
		} else {
			taskResult.setResult(
					MessageFormat.format("When api url is {0}, The data on failure. The error message is as follows:{1}", 
							resultObject.getUrl(),resultObject.getResult()));
		}
		return taskResult;
	}
	
	/**
	 * 发送通知邮件，通知管理员/指定人
	 * @param buildType	通知类型
	 * @param result	通知状态
	 * @param detail	通知内容
	 * @param to	通知给谁
	 * @author linzhanbo .
	 * @since 2016年7月28日, 下午1:50:11 .
	 * @version 1.0 .
	 */
	public void buildResultToMgr(String buildType, String result,
			String detail, String to) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buildType", buildType);
		map.put("buildResult", result);
		map.put("errorDetail", detail);
		MailMessage mailMessage = new MailMessage("乐视云平台Matrix系统",
				StringUtils.isEmpty(to) ? SERVICE_NOTICE_MAIL_ADDRESS : to,
				"乐视云平台Matrix系统通知", "buildForMgr.ftl", map);
		defaultEmailSender.sendMessage(mailMessage);
	}
	
	/**
	 * json串转为map
	 * @param paramsJsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @author linzhanbo .
	 * @since 2016年7月28日, 下午2:02:54 .
	 * @version 1.0 .
	 */
	public Map<String,Object> fromJson(String paramsJsonStr) throws JsonParseException, JsonMappingException, IOException{
		if(StringUtils.isEmpty(paramsJsonStr))
			return null;
		ObjectMapper resultMapper = new ObjectMapper();
		Map<String,Object> jsonResult = resultMapper.readValue(paramsJsonStr, Map.class);
		return jsonResult;
	}
	/**
	 * json串转为map<br/>
	 * 方法已经过时，强烈建议使用fromJson方法，虽然二者作用相同，后续升级流程时，会删掉该方法。
	 * @param params
	 * @return
	 * @author linzhanbo .
	 * @since 2016年7月28日, 下午2:04:05 .
	 * @version 1.0 .
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public Map<String,Object> transToMap(String params){
		if(StringUtils.isEmpty(params))
			return null;
		ObjectMapper resultMapper = new ObjectMapper();
		Map<String,Object> jsonResult = new HashMap<String,Object>();
		try {
			jsonResult = resultMapper.readValue(params, Map.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	/**
	 * 对象转为json串
	 * @param params
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @author linzhanbo .
	 * @since 2016年7月28日, 下午2:03:16 .
	 * @version 1.0 .
	 */
	public String toJson(Object params) throws JsonGenerationException, JsonMappingException, IOException{
		if(params == null)
			return null;
		ObjectMapper resultMapper = new ObjectMapper();
		String jsonResult =  resultMapper.writeValueAsString(params);
		return jsonResult;
	}
	
	/**
	 * 对象转为json串
	 * 方法已经过时，强烈建议使用toJson方法，虽然二者作用相同，后续升级流程时，会删掉该方法。
	 * @param params
	 * @return
	 * @author linzhanbo .
	 * @since 2016年7月28日, 下午2:05:06 .
	 * @version 1.0 .
	 */
	@Deprecated
	public String transToString(Object params){
		if(params == null)
			return null;
		ObjectMapper resultMapper = new ObjectMapper();
		String jsonResult = "";
		try {
			jsonResult = resultMapper.writeValueAsString(params);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	public Long getLongFromObject(Object o) {
		if(null == o)
			return null;
		Long value = null;
		if(o instanceof String)
			value = Long.parseLong((String) o);
		if(o instanceof Integer)
			value = Long.parseLong(((Integer)o).toString());
		if(o instanceof Long)
			value = (Long) o;
		
		return value;
	}

	@Override
	public void rollBack(TaskResult tr) {
	}

	@Override
	public void callBack(TaskResult tr) {
	}

	@Override
	public void beforExecute(Map<String, Object> params) {
	}
	
	@Override
	public void afterExecute(TaskResult tr) {
	}

	@Override
	public void beforeExecute(Map<String, Object> params) {
	}
	
	@Override
	public void finish(TaskResult tr) {
	}
	
	@Override
	public TaskResult polling(TaskResult tr, long interval, long timeout,Object... params) throws InterruptedException {
		// 返回结果
		ApiResultObject resultObject = null;
		long beginTime = System.currentTimeMillis();
		tr.setSuccess(false);
		while (!tr.isSuccess()) {
			// 循环的第一次肯定不会进该if，因为代码开头Assert.isTrue过，所以resultObject.url必然在轮训后有值
			if (System.currentTimeMillis() - beginTime > timeout) {
				tr.setSuccess(false);
				tr.setResult("check time over:" + resultObject.getUrl());
				break;
			} else {
				Object obj = pollingTask(params);
				if(obj instanceof ApiResultObject){
					resultObject = (ApiResultObject) obj;
					tr = analyzeComplexRestServiceResult(resultObject);
				}
				else if(obj instanceof TaskResult)
					tr = (TaskResult) obj;
				else
					throw new ValidateException("The pollingTask method's returnvalue must be ApiResultObject type or TaskResult type.");
			}
			Thread.sleep(interval);
		}
		return tr;
	}
	
	@Override
	public <T> T pollingTask(Object... params) throws InterruptedException {
		return null;
	}
	
	public TaskResult asynchroExecuteTasks(List<Task> tasks,TaskResult tr) {
		if(!tr.isSuccess())
			return tr;
		if(CollectionUtils.isEmpty(tasks)){
			tr.setSuccess(false);
			tr.setResult("there is no available tasks");
			return tr;
		}
		for(Task task:tasks){
			Object obj = task.onExec();
			if(obj == null){
				tr.setSuccess(false);
				tr.setResult("Gets the Task object's execute method returns a value is null");
				return tr;
			}
			ApiResultObject apiResult = null;
			if(obj instanceof ApiResultObject){
				apiResult = (ApiResultObject) obj;
				tr = analyzeRestServiceResult(apiResult);
			}else if(obj instanceof TaskResult){
				tr = (TaskResult) obj;
			}else{
				tr.setSuccess(false);
				tr.setResult("Gets the Task object's execute method returns a value is invalid");
				return tr;
			}
			if(!tr.isSuccess()) {
				tr.setSuccess(false);
				tr.setResult(MessageFormat.format("the {0} error:{1}",apiResult.getUrl(),tr.getResult()));
				return tr;
			}else{
				task.onSuccess(tr);
			}
		}
		return tr;
	}
	@Override
	public TaskResult synchroExecuteTasks(List<Task> tasks,TaskResult tr) {
		if(!tr.isSuccess())
			return tr;
		if(CollectionUtils.isEmpty(tasks)){
			tr.setSuccess(false);
			tr.setResult("there is no available tasks");
			return tr;
		}
		//是否继续
		boolean isContinue = true;
		Map<Future,Task> onions = new HashMap<Future, IBaseTaskService.Task>();
		for(Task task:tasks){
			//使用全局线程池
			Future future = threadPoolTaskExecutor.submit(task);
			onions.put(future, task);
		}
		OUT:
		while(isContinue){
			IUT:
			for(Future future:onions.keySet()){
				if(future.isDone()){
					Object obj = null;
					try {
						obj = future.get();
						//apiResult = (ApiResultObject) future.get();
					} catch (InterruptedException | ExecutionException e) {
						tr.setSuccess(false);
						tr.setResult("Gets the Task object's execute method returns a value failed:"+e.getMessage());
						isContinue = false;
						//终止后续任务工作
						break OUT;
					}
					if(obj == null){
						tr.setSuccess(false);
						tr.setResult("Gets the Task object's execute method returns a value is null");
						isContinue = false;
						//终止后续任务工作
						break OUT;
					}
					ApiResultObject apiResult = null;
					if(obj instanceof ApiResultObject){
						apiResult = (ApiResultObject) obj;
						tr = analyzeRestServiceResult(apiResult);
					}else if(obj instanceof TaskResult){
						tr = (TaskResult) obj;
					}else{
						tr.setSuccess(false);
						tr.setResult("Gets the Task object's execute method returns a value is invalid");
						isContinue = false;
						//终止后续任务工作
						break OUT;
					}
					if(!tr.isSuccess()) {
						tr.setSuccess(false);
						tr.setResult(MessageFormat.format("the {0} error:{1}",apiResult.getUrl(),tr.getResult()));
						isContinue = false;
						//终止后续任务工作
						break OUT;
					}else{
						//成功后执行回调
						onions.get(future).onSuccess(tr);
					}
					onions.remove(future);
					break IUT;
				}
			}
			if(onions.size()<=0){
				isContinue = false;
				break OUT;
			}
		}
		//如果有未完成任务，或者正在进行任务，直接终止其继续执行
		if(!CollectionUtils.isEmpty(onions)){
			for(Future future:onions.keySet()){
				future.cancel(true);
			}
		}
		return tr;
	}

	@Override
	public TaskResult analyzeHttpStatusRESTfulResult(RESTfulResult result) {
		logger.debug("Analyze RESTfulResult:{}", result);
		TaskResult taskResult = new TaskResult();
		
		if(result == null){
			taskResult.setSuccess(false);
			taskResult.setResult(MessageFormat.format("url:{0}, result:{1}", null, "RESTfulResult is null"));
		} else if(result.getStatus() < 200 || result.getStatus() >= 300) {
			taskResult.setSuccess(false);
			taskResult.setResult(MessageFormat.format("url:{0}, result:{1}", result.getUrl(), result.getBody()));
			return taskResult;
		} else {
			ApiResultObject apiResultObject = JSONObject.parseObject(result.getBody(), ApiResultObject.class);
			
			if(apiResultObject.getAnalyzeResult()) {
				taskResult.setSuccess(true);
			} else {//业务判断失败,修改结果为false
				taskResult.setSuccess(false);
			}
			taskResult.setResult(apiResultObject.getResult());
		}
		
		return taskResult;
	}
	
}
