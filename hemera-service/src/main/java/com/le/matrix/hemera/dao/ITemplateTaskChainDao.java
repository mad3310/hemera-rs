package com.le.matrix.hemera.dao;

import java.util.List;

import com.le.matrix.hemera.model.TemplateTaskChain;
import com.letv.common.dao.IBaseDao;

public interface ITemplateTaskChainDao extends IBaseDao<TemplateTaskChain> {

	public List<TemplateTaskChain> selectByTemplateTaskId(Long taskId);
}
