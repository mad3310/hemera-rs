package com.le.matrix.hemera.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.hemera.facade.ITaskChainIndexService;
import com.le.matrix.hemera.facade.ITaskChainService;
import com.le.matrix.hemera.facade.ITaskEngine;
import com.le.matrix.hemera.junitBase.AbstractTest;
 
public class TaskEngineRDS extends AbstractTest{

	@Autowired
	private ITaskEngine taskEngine;
	
	@Autowired
	private ITaskChainIndexService taskChainIndexService;
	@Autowired
	private ITaskChainService taskChainService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskEngineRDS.class);
	
    /**Methods Name: testRun <br>
     * Description: 创建流程并执行,带参数<br>
     * @author name: liuhao1
     */
    //@Test
    public void testRun5() {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("mclusterId", 44L);
    	params.put("dbId", 14L);
    	this.taskEngine.run(2L,params);
    }
    
}
