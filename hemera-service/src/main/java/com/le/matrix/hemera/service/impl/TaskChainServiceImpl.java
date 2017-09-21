package com.le.matrix.hemera.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.le.matrix.hemera.dao.ITaskChainDao;
import com.le.matrix.hemera.enumeration.TaskExecuteStatus;
import com.le.matrix.hemera.facade.ITaskChainIndexService;
import com.le.matrix.hemera.facade.ITaskChainService;
import com.le.matrix.hemera.model.TaskChain;
import com.letv.common.dao.IBaseDao;

@Service("taskChainService")
public class TaskChainServiceImpl extends BaseServiceImpl<TaskChain> implements
		ITaskChainService {

	private final static Logger logger = LoggerFactory
			.getLogger(TaskChainServiceImpl.class);

	@Resource
	private ITaskChainDao taskChainDao;
	@Autowired
	private ITaskChainIndexService taskChainIndexService;
	
	public TaskChainServiceImpl() {
		super(TaskChain.class);
	}

	@Override
	public IBaseDao<TaskChain> getDao() {
		return taskChainDao;
	}

	@Override
	public TaskChain selectNextChainByIndexAndOrder(Long chainIndexId,
			int executeOrder) {
		TaskChain tc = new TaskChain();
		tc.setChainIndexId(chainIndexId);
		tc.setExecuteOrder(executeOrder);
		return this.taskChainDao.selectNextChainByIndexAndOrder(tc);
	}

	@Override
	public TaskChain selectFailedChainByIndex(long chainIndexId) {
		TaskChain tc = new TaskChain();
		tc.setChainIndexId(chainIndexId);
		tc.setStatus(TaskExecuteStatus.FAILED);
		return this.taskChainDao.selectFailedChainByIndex(tc);
	}

	@Override
	public List<TaskChain> selectAllChainByIndexId(Long chainIndexId) {
		return this.taskChainDao.selectAllChainByIndexId(chainIndexId);
	}

	@Override
	public void updateAfterDoingChainStatus(Map<String, Object> params) {
		this.taskChainDao.updateAfterDoingChainStatus(params);

	}

	private int getStepByTaskChainIndexId(Long taskChainIndexId) {
		List<TaskChain> taskChains = this
				.selectAllChainByIndexId(taskChainIndexId);

		if (taskChains.get(taskChains.size() - 1).getStatus() == TaskExecuteStatus.SUCCESS) {
			return 0;// 返回创建成功
		}
		for (TaskChain taskChain : taskChains) {
			if (taskChain.getStatus() == TaskExecuteStatus.FAILED) {
				return -1;// 返回创建失败
			} else if (taskChain.getStatus() == TaskExecuteStatus.DOING) {
				return taskChain.getExecuteOrder();// 返回此步所在任务中的顺序
			}
		}
		return 1;// 都没有，则认为正在执行第一步
	}

	@Override
	public void insertBatch(List<TaskChain> taskChains) {
		taskChainDao.insertBatch(taskChains);
	}
}
