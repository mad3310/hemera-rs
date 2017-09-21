package com.le.matrix.hemera.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.hemera.facade.ITemplateTaskChainService;
import com.le.matrix.hemera.facade.ITemplateTaskDetailService;
import com.le.matrix.hemera.facade.ITemplateTaskService;
import com.le.matrix.hemera.junitBase.AbstractTest;
import com.le.matrix.hemera.model.TemplateTaskChain;

public class TemplateTaskChainServiceTest extends AbstractTest{

	@Autowired
	private ITemplateTaskChainService templateTaskChainService;
	@Autowired
	private ITemplateTaskService templateTaskService;
	@Autowired
	private ITemplateTaskDetailService templateTaskDetailService;
	
	private final static Logger logger = LoggerFactory.getLogger(
			TemplateTaskChainServiceTest.class);
	
	private Long taskId = null;
	private Long taskDetailId = null;
	private Long taskChainId = null;
	
    @Test
    public void testInsertOneAndCheckSize() throws InterruptedException {
    	taskId = insertTask();
    	taskDetailId = insertTaskDetail();
    	taskChainId = insertTaskChain(taskId, taskDetailId);
    	
    	List<TemplateTaskChain> taskChains = getQuery();
    	Assert.assertEquals(1, taskChains.size());
    }
    
    
    public List<TemplateTaskChain> getQuery() {
    	List<TemplateTaskChain> taskChains = templateTaskChainService.selectByTemplateTaskId(taskId);
    	return taskChains;
    	
    }
    
    @After
    public void testDeleteAfterAndCheck() {
    	deleteTaskChain(taskChainId);
    	deleteTaskDetail(taskDetailId);
    	deleteTask(taskId);
    	
    	List<TemplateTaskChain> taskChains = getQuery();
    	Assert.assertEquals(0, taskChains.size());
    }

}