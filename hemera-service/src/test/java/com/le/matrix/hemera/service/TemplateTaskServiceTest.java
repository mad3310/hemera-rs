package com.le.matrix.hemera.service;

import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.hemera.facade.ITemplateTaskService;
import com.le.matrix.hemera.junitBase.AbstractTest;
import com.le.matrix.hemera.model.TemplateTask;
import com.letv.common.paging.impl.Page;

public class TemplateTaskServiceTest extends AbstractTest{

	@Autowired
	private ITemplateTaskService templateTaskService;
	
	private final static Logger logger = LoggerFactory.getLogger(
			TemplateTaskServiceTest.class);
	
	private int totalSize = 0;
	private Long id = null;
	
	@Before
    public void testServiceBefore() {
		totalSize = getTotalSise();
    	logger.info("查询templateTask获取totalSize：{}", totalSize);
    }
	
	private int getTotalSise() {
		Page p = templateTaskService.selectPageByParams(new Page(), new HashMap<String, Object>());
    	return p.getTotalRecords();
	}
    
    @Test
    public void testInsertOneAndCheckSize() throws InterruptedException {
    	id = insertTask();
    	
    	Assert.assertEquals(totalSize+1, getTotalSise());
    	
    	testQuery();
    }
    
    public void testQuery() {
    	TemplateTask task = templateTaskService.selectById(id);
    	Assert.assertNotNull(task);
    	
    	TemplateTask taskByName = templateTaskService.selectByName(task.getName());
    	Assert.assertNotNull(taskByName);
    }
    
    @After
    public void testDeleteAfterAndCheck() {
    	deleteTask(id);
    	
    	Assert.assertEquals(totalSize, getTotalSise());
    }

}