package com.le.matrix.hemera.service;

import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.hemera.facade.ITaskChainIndexService;
import com.le.matrix.hemera.junitBase.AbstractTest;
import com.le.matrix.hemera.model.TaskChainIndex;
import com.le.matrix.hemera.model.TemplateTask;
import com.letv.common.paging.impl.Page;

public class TaskChainIndexServiceTest extends AbstractTest{

	@Autowired
	private ITaskChainIndexService taskChainIndexService;
	
	private final static Logger logger = LoggerFactory.getLogger(
			TaskChainIndexServiceTest.class);
	
	private int totalSize = 0;
	private Long id = null;
	private Long taskId = null;
	
	@Before
    public void testServiceBefore() {
		totalSize = getTotalSise();
    	logger.info("查询taskChainIndex获取totalSize：{}", totalSize);
    }
	
	private int getTotalSise() {
		Page p = taskChainIndexService.selectPageByParams(new Page(), new HashMap<String, Object>());
    	return p.getTotalRecords();
	}
    
    @Test
    public void testInsertOneAndCheckSize() throws InterruptedException {
    	taskId = insertTask();
    	id = insertTaskChainIndex(taskId);
    	
    	Assert.assertEquals(totalSize+1, getTotalSise());
    	
    	testQuery();
    }
    
    public void testQuery() {
    	TaskChainIndex index = taskChainIndexService.selectById(id);
    	Assert.assertNotNull(index);
    	
    	TaskChainIndex indexByName = taskChainIndexService.selectByServiceAndClusterName(index.getServiceName(), index.getClusterName());
    	Assert.assertNotNull(indexByName);
    	
    	taskChainIndexService.selectFailedChainIndex();
    }
    
    @After
    public void testDeleteAfterAndCheck() {
    	deleteTaskChainIndex(id);
    	deleteTask(taskId);
    	
    	Assert.assertEquals(totalSize, getTotalSise());
    }

}