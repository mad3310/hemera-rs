package com.le.matrix.hemera.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.matrix.hemera.dao.ITemplateTaskDetailDao;
import com.le.matrix.hemera.facade.ITemplateTaskDetailService;
import com.le.matrix.hemera.model.TemplateTaskDetail;
import com.letv.common.dao.IBaseDao;

@Service("templateTaskDetailService")
public class TemplateTaskDetailServiceImpl extends BaseServiceImpl<TemplateTaskDetail> implements ITemplateTaskDetailService{
	
	private final static Logger logger = LoggerFactory.getLogger(TemplateTaskDetailServiceImpl.class);

	@Resource
	private ITemplateTaskDetailDao templateTaskDetailDao;
	
	public TemplateTaskDetailServiceImpl() {
		super(TemplateTaskDetail.class);
	}
	
	@Override
	public IBaseDao<TemplateTaskDetail> getDao() {
		return templateTaskDetailDao;
	}

	@Override
	public List<TemplateTaskDetail> selectByTemplateTaskType(String type) {
		return this.templateTaskDetailDao.selectByTemplateTaskType(type);
	}
	
}
