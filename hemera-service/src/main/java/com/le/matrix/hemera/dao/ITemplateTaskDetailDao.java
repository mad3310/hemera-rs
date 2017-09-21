package com.le.matrix.hemera.dao;

import java.util.List;

import com.le.matrix.hemera.model.TemplateTaskDetail;
import com.letv.common.dao.IBaseDao;

public interface ITemplateTaskDetailDao extends IBaseDao<TemplateTaskDetail> {

	List<TemplateTaskDetail> selectByTemplateTaskType(String type);

}
