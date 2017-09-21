package com.le.matrix.hemera.facade;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;


/**
 * 
 * 工作流引擎入口
 * 
 * @author linzhanbo .
 * @since 2016年7月26日, 下午5:51:26 .
 * @version 1.0 .
 */
@Path("task")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ITaskEngine {

	/**
	 * 启动流程
	 * @param templateTaskName	流程模板名称
	 * @author linzhanbo .
	 * @since 2016年7月26日, 下午5:17:20 .
	 * @version 1.0 .
	 */
	@GET
	@Path("executer/name/{taskName}")
	public void run(@PathParam("taskName") String templateTaskName);
	
	/**
	 * 启动流程
	 * @param templateTaskName	流程模板名称
	 * @param params	参数
	 * @author linzhanbo .
	 * @since 2016年7月26日, 下午5:18:00 .
	 * @version 1.0 .
	 */
	@POST
	@Path("executer/name/{taskName}")
	public void run(@PathParam("taskName") String templateTaskName, Object params);
	
	/**
	 * 启动流程
	 * @param templateTaskId	流程模板ID
	 * @author linzhanbo .
	 * @since 2016年7月26日, 下午5:25:49 .
	 * @version 1.0 .
	 */
	@GET
	@Path("executer/id/{taskId}")
	public void run(@PathParam("taskId") Long templateTaskId);
	
	/**
	 * 启动流程
	 * @param templateTaskId	流程模板ID
	 * @param params	参数
	 * @author linzhanbo .
	 * @since 2016年7月26日, 下午5:28:35 .
	 * @version 1.0 .
	 */
	@POST
	@Path("executer/id/{taskId}")
	public void run(@PathParam("taskId") Long templateTaskId, Object params);
	
	/**
	 * 继续运行流程
	 * @param taskChainId	任务单元实例ID
	 * @author linzhanbo .
	 * @since 2016年7月26日, 下午5:48:59 .
	 * @version 1.0 .
	 */
	@GET
	@Path("proceeder/{chainId}")
	public void proceed(@PathParam("chainId") Long taskChainId);

}
