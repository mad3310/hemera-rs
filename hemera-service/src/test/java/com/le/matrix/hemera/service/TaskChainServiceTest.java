package com.le.matrix.hemera.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.hemera.enumeration.TaskExecuteStatus;
import com.le.matrix.hemera.facade.ITaskChainIndexService;
import com.le.matrix.hemera.facade.ITaskChainService;
import com.le.matrix.hemera.facade.ITemplateTaskChainService;
import com.le.matrix.hemera.facade.ITemplateTaskDetailService;
import com.le.matrix.hemera.facade.ITemplateTaskService;
import com.le.matrix.hemera.junitBase.AbstractTest;
import com.le.matrix.hemera.model.TaskChain;
import com.le.matrix.hemera.model.TemplateTask;
import com.le.matrix.hemera.model.TemplateTaskChain;
import com.le.matrix.hemera.model.TemplateTaskDetail;

public class TaskChainServiceTest extends AbstractTest{

	@Autowired
	private ITaskChainService taskChainService;
	@Autowired
	private ITemplateTaskChainService templateTaskChainService;
	@Autowired
	private ITemplateTaskService templateTaskService;
	@Autowired
	private ITemplateTaskDetailService templateTaskDetailService;
	@Autowired
	private ITaskChainIndexService taskChainIndexService;
	
	private final static Logger logger = LoggerFactory.getLogger(
			TaskChainServiceTest.class);
	
	private Long taskId = null;
	private Long taskDetailId = null;
	private Long taskChainId = null;
	
	private Long chainIndexId = null;
	private Long chainId = null;
	
    
    @Test
    public void testInsertOneAndCheckSize() throws InterruptedException {
    	//模板初始化
    	taskId = insertTask();
    	taskDetailId = insertTaskDetail();
    	taskChainId = insertTaskChain(taskId, taskDetailId);
    	
    	chainIndexId = insertTaskChainIndex(taskId);
    	
    	
    	TemplateTask templateTask = this.templateTaskService.selectById(taskId);
    	// 使用流程模板ID获取流程的所有单元定义信息
		List<TemplateTaskChain> templateTaskChains = this.templateTaskChainService.selectByTemplateTaskId(templateTask.getId());
    	
		// 创建所有任务单元实例
		List<TaskChain> chains = new ArrayList<TaskChain>();
		for (TemplateTaskChain templateTaskChain : templateTaskChains) {
			TaskChain tc = new TaskChain();
			tc.setTaskId(templateTaskChain.getTaskId());
			tc.setTaskDetailId(templateTaskChain.getTaskDetailId());
			tc.setExecuteOrder(templateTaskChain.getExecuteOrder());
			tc.setChainIndexId(chainIndexId);
			tc.setStatus(TaskExecuteStatus.UNDO);
			// 设置单元可重试次数
			TemplateTaskDetail ttd = this.templateTaskDetailService.selectById(tc.getTaskDetailId());
			tc.setRetry(ttd.getRetry());
			tc.setUrl(templateTaskChain.getUrl());
			tc.setParams("{\"key\":\"test\"}");
			chains.add(tc);
		}
		this.taskChainService.insertBatch(chains);
    	
    	testQuery();
    }
    
    public void testQuery() {
    	List<TaskChain> chains = taskChainService.selectAllChainByIndexId(chainIndexId);
    	Assert.assertEquals(1, chains.size());
    	
    	chainId = chains.get(0).getId();
    }
    
    @After
    public void testDeleteAfterAndCheck() {
    	//删除实例
    	TaskChain chain = new TaskChain();
    	chain.setId(chainId);
    	this.taskChainService.delete(chain);
    	deleteTaskChainIndex(chainIndexId);
    	
    	//删除模板
    	deleteTaskChain(taskChainId);
    	deleteTaskDetail(taskDetailId);
    	deleteTask(taskId);
    	
    }

}