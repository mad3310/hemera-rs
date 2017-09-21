package com.le.matrix.hemera.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.le.matrix.hemera.dao.ITaskAsyncExecuteDao;
import com.le.matrix.hemera.facade.ITaskAsyncExecuteService;
import com.le.matrix.hemera.facade.ITaskEngine;
import com.le.matrix.hemera.model.TaskAsyncExecute;
import com.letv.common.dao.IBaseDao;
import com.letv.common.exception.ValidateException;

@Service("taskAsyncExecuteService")
public class TaskAsyncExecuteServiceImpl extends BaseServiceImpl<TaskAsyncExecute> implements ITaskAsyncExecuteService{
	
	private final static Logger logger = LoggerFactory.getLogger(TaskAsyncExecuteServiceImpl.class);
	
	@Resource
	private ITaskAsyncExecuteDao asyncExecuteDao;
	@Resource
	private ITaskEngine taskEngine;
	
	public TaskAsyncExecuteServiceImpl() {
		super(TaskAsyncExecute.class);
	}

	@Override
	public IBaseDao<TaskAsyncExecute> getDao() {
		return this.asyncExecuteDao;
	}
	
	@Override
	public void insert(TaskAsyncExecute t) {
		if(t == null) {
			throw new ValidateException("参数不合法");
		}
		super.insert(t);
	}

	@Override
	public TaskAsyncExecute selectByTaskChainId(Long taskChainId) {
		return this.asyncExecuteDao.selectByTaskChainId(taskChainId);
	}

	@Override
	public void taskAsyncRetry() {
		List<TaskAsyncExecute> executes = this.asyncExecuteDao.selectByMap(null);
		for (TaskAsyncExecute taskAsyncExecute : executes) {
			execute(taskAsyncExecute);
		}
	}
	
	@Async
	private void execute(TaskAsyncExecute taskAsyncExecute) {
		if(taskAsyncExecute.getCount()>0) {
			this.taskEngine.proceed(taskAsyncExecute.getTaskChainId());
			//调用数减1
			taskAsyncExecute.setCount(taskAsyncExecute.getCount()-1);
			super.updateBySelective(taskAsyncExecute);
		}
	}

	@Override
	public void deleteByMclusterName(String mclusterName) {
		this.asyncExecuteDao.deleteByMclusterName(mclusterName);
	}

}
