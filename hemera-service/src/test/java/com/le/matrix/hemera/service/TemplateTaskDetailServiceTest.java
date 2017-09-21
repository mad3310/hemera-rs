package com.le.matrix.hemera.service;

import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.hemera.facade.ITemplateTaskDetailService;
import com.le.matrix.hemera.junitBase.AbstractTest;
import com.le.matrix.hemera.model.TemplateTaskDetail;
import com.letv.common.paging.impl.Page;

public class TemplateTaskDetailServiceTest extends AbstractTest{

	@Autowired
	private ITemplateTaskDetailService templateTaskDetailService;
	
	private final static Logger logger = LoggerFactory.getLogger(
			TemplateTaskDetailServiceTest.class);
	
	private int totalSize = 0;
	private Long id = null;
	
	@Before
    public void testServiceBefore() {
		totalSize = getTotalSise();
    	logger.info("查询templateTask获取totalSize：{}", totalSize);
    }
	
	private int getTotalSise() {
		Page p = templateTaskDetailService.selectPageByParams(new Page(), new HashMap<String, Object>());
    	return p.getTotalRecords();
	}
    
    @Test
    public void testInsertOneAndCheckSize() throws InterruptedException {
    	id = insertTaskDetail();
    	
    	Assert.assertEquals(totalSize+1, getTotalSise());
    	
    	testQuery();
    }
    
    public void testQuery() {
    	TemplateTaskDetail task = templateTaskDetailService.selectById(id);
    	Assert.assertNotNull(task);
    	
    	List<TemplateTaskDetail> taskDetails = templateTaskDetailService.selectByTemplateTaskType(task.getTaskType());
    	logger.info("根据类型查询结果集，大小{}", taskDetails.size());
    }
    
    @After
    public void testDeleteAfterAndCheck() {
    	deleteTaskDetail(id);
    	
    	Assert.assertEquals(totalSize, getTotalSise());
    }

}