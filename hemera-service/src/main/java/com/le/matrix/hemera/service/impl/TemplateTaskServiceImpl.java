package com.le.matrix.hemera.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.matrix.hemera.dao.ITemplateTaskDao;
import com.le.matrix.hemera.facade.ITemplateTaskService;
import com.le.matrix.hemera.model.TemplateTask;
import com.letv.common.dao.IBaseDao;

@Service("templateTaskService")
public class TemplateTaskServiceImpl extends BaseServiceImpl<TemplateTask> implements ITemplateTaskService{
	
	private final static Logger logger = LoggerFactory.getLogger(TemplateTaskServiceImpl.class);

	@Resource
	private ITemplateTaskDao templateTaskDao;
	
	public TemplateTaskServiceImpl() {
		super(TemplateTask.class);
	}
	
	@Override
	public IBaseDao<TemplateTask> getDao() {
		return templateTaskDao;
	}

	@Override
	public TemplateTask selectByName(String name) {
		return this.templateTaskDao.selectByName(name);
	}
}
