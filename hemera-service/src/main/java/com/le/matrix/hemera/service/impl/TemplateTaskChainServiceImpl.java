package com.le.matrix.hemera.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.matrix.hemera.dao.ITemplateTaskChainDao;
import com.le.matrix.hemera.facade.ITemplateTaskChainService;
import com.le.matrix.hemera.model.TemplateTaskChain;
import com.letv.common.dao.IBaseDao;

@Service("templateTaskChainService")
public class TemplateTaskChainServiceImpl extends BaseServiceImpl<TemplateTaskChain> implements ITemplateTaskChainService{
	
	private final static Logger logger = LoggerFactory.getLogger(TemplateTaskChainServiceImpl.class);

	@Resource
	private ITemplateTaskChainDao templateTaskChainDao;
	
	public TemplateTaskChainServiceImpl() {
		super(TemplateTaskChain.class);
	}
	
	@Override
	public IBaseDao<TemplateTaskChain> getDao() {
		return templateTaskChainDao;
	}
	
	@Override
	public List<TemplateTaskChain> selectByTemplateTaskId(Long templateId) {
		return this.templateTaskChainDao.selectByTemplateTaskId(templateId);
	}

	
}
