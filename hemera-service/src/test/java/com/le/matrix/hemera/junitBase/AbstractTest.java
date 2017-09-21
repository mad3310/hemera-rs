package com.le.matrix.hemera.junitBase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.matrix.hemera.enumeration.TaskExecuteStatus;
import com.le.matrix.hemera.facade.ITaskChainIndexService;
import com.le.matrix.hemera.facade.ITemplateTaskChainService;
import com.le.matrix.hemera.facade.ITemplateTaskDetailService;
import com.le.matrix.hemera.facade.ITemplateTaskService;
import com.le.matrix.hemera.model.TaskChainIndex;
import com.le.matrix.hemera.model.TemplateTask;
import com.le.matrix.hemera.model.TemplateTaskChain;
import com.le.matrix.hemera.model.TemplateTaskDetail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-context.xml"})
public abstract class AbstractTest{
	
	private final static Logger logger = LoggerFactory.getLogger(
			AbstractTest.class);
	
	@Autowired
	private ITemplateTaskChainService templateTaskChainService;
	@Autowired
	private ITemplateTaskService templateTaskService;
	@Autowired
	private ITemplateTaskDetailService templateTaskDetailService;
	@Autowired
	private ITaskChainIndexService taskChainIndexService;
    
	@Before 
	public void setup() throws  Exception {
	}
	
	public Long insertTask() {
    	TemplateTask task = new TemplateTask();
    	task.setName("jutTest"+System.currentTimeMillis());
    	task.setTaskType("TEST");
    	task.setDescn("test insert task");
    	templateTaskService.insert(task);
    	logger.info("task template insert成功，id：{}", task.getId());
    	return task.getId();
    }
    
    public Long insertTaskDetail() {
    	TemplateTaskDetail taskDetail = new TemplateTaskDetail();
    	taskDetail.setName("jutTest"+System.currentTimeMillis());
    	taskDetail.setBeanName("testBeanName");
    	taskDetail.setRetry(1);
    	taskDetail.setAsyncRetry(2);
    	taskDetail.setTaskType("TT"+System.currentTimeMillis());
    	taskDetail.setDescn("test insert taskDetail");
    	templateTaskDetailService.insert(taskDetail);
    	logger.info("taskDetail template insert成功，id：{}", taskDetail.getId());
    	return taskDetail.getId();
    }
    
    public Long insertTaskChain(Long taskId, Long taskDetailId) {
    	TemplateTaskChain taskChain = new TemplateTaskChain();
    	taskChain.setTaskId(taskId);
    	taskChain.setTaskDetailId(taskDetailId);
    	taskChain.setExecuteOrder(1);
    	taskChain.setRetry(1);
    	taskChain.setUrl("http://test.test.com");
    	templateTaskChainService.insert(taskChain);
    	Long taskChainId = taskChain.getId();
    	logger.info("taskChain template insert成功，id：{}", taskChainId);
    	return taskChainId;
    }
    
    public Long insertTaskChainIndex(Long taskId) {
    	TaskChainIndex taskChainIndex = new TaskChainIndex();
    	taskChainIndex.setTaskId(taskId);
    	taskChainIndex.setClusterName("testClusterName"+System.currentTimeMillis());
    	taskChainIndex.setServiceName("testServiceName"+System.currentTimeMillis());
    	taskChainIndex.setStatus(TaskExecuteStatus.UNDO);
    	taskChainIndexService.insert(taskChainIndex);
    	Long taskChainIndexId = taskChainIndex.getId();
    	logger.info("taskChainIndex insert成功，id：{}", taskChainIndexId);
    	return taskChainIndexId;
    }
    
    public void deleteTask(Long taskId) {
    	TemplateTask task = new TemplateTask();
    	task.setId(taskId);
    	templateTaskService.delete(task);
    	logger.info("task template delete成功，id：{}", taskId);
    }
    
    public void deleteTaskDetail(Long taskDetailId) {
    	TemplateTaskDetail taskDetail = new TemplateTaskDetail();
    	taskDetail.setId(taskDetailId);
    	templateTaskDetailService.delete(taskDetail);
    	logger.info("taskDetail template delete成功，id：{}", taskDetailId);
    }
    
    public void deleteTaskChain(Long taskChainId) {
    	TemplateTaskChain taskChain = new TemplateTaskChain();
    	taskChain.setId(taskChainId);
    	templateTaskChainService.delete(taskChain);
    	logger.info("taskChain template delete成功，id：{}", taskChainId);
    }
    
    public void deleteTaskChainIndex(Long taskChainIndexId) {
    	TaskChainIndex taskChainIndex = new TaskChainIndex();
    	taskChainIndex.setId(taskChainIndexId);
    	taskChainIndexService.delete(taskChainIndex);
    	logger.info("taskChainIndex delete成功，id：{}", taskChainIndexId);
    }
    
}