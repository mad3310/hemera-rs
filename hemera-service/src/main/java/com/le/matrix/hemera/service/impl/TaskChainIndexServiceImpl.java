package com.le.matrix.hemera.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.matrix.hemera.dao.ITaskChainIndexDao;
import com.le.matrix.hemera.facade.ITaskChainIndexService;
import com.le.matrix.hemera.model.TaskChainIndex;
import com.letv.common.dao.IBaseDao;

@Service("taskChainIndexService")
public class TaskChainIndexServiceImpl extends BaseServiceImpl<TaskChainIndex> implements ITaskChainIndexService{
	
	private final static Logger logger = LoggerFactory.getLogger(TaskChainIndexServiceImpl.class);

	@Resource
	private ITaskChainIndexDao taskChainIndexDao;
	
	public TaskChainIndexServiceImpl() {
		super(TaskChainIndex.class);
	}
	
	@Override
	public IBaseDao<TaskChainIndex> getDao() {
		return taskChainIndexDao;
	}

	@Override
	public TaskChainIndex selectByServiceAndClusterName(String serviceName,String clusterName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("serviceName", serviceName);
		map.put("clusterName", clusterName);
		List<TaskChainIndex> buildSteps = this.taskChainIndexDao.selectByMap(map);
		if(buildSteps.isEmpty())
			return null;
		return buildSteps.get(0);
	}

	@Override
	public List<Map<String, Object>> selectFailedChainIndex() {
		return this.taskChainIndexDao.selectFailedChainIndex();
	}
	
}
