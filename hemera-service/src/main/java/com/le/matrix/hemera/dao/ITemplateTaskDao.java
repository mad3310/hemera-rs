package com.le.matrix.hemera.dao;

import com.le.matrix.hemera.model.TemplateTask;
import com.letv.common.dao.IBaseDao;

public interface ITemplateTaskDao extends IBaseDao<TemplateTask> {

	TemplateTask selectByName(String name);

}
