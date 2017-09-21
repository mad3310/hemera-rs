package com.le.matrix.hemera.dao;

import java.util.List;
import java.util.Map;

import com.le.matrix.hemera.model.TaskChain;
import com.letv.common.dao.IBaseDao;

public interface ITaskChainDao extends IBaseDao<TaskChain> {

	TaskChain selectNextChainByIndexAndOrder(TaskChain tc);

	TaskChain selectFailedChainByIndex(TaskChain tc);

	List<TaskChain> selectAllChainByIndexId(Long chainIndexId);

	void updateAfterDoingChainStatus(Map<String, Object> params);
	
	public void insertBatch(List<TaskChain> taskChains);
}
