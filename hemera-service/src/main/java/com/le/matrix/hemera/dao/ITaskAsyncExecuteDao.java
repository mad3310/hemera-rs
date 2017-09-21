package com.le.matrix.hemera.dao;

import com.le.matrix.hemera.model.TaskAsyncExecute;
import com.letv.common.dao.IBaseDao;

public interface ITaskAsyncExecuteDao extends IBaseDao<TaskAsyncExecute> {
	TaskAsyncExecute selectByTaskChainId(Long taskChainId);
	
	void deleteByMclusterName(String mclusterName);
}
