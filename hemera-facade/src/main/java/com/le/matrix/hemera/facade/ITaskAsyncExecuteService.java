package com.le.matrix.hemera.facade;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.hemera.model.TaskAsyncExecute;
import com.le.matrix.redis.facade.IBaseService;

@Path("asyncTask")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ITaskAsyncExecuteService extends IBaseService<TaskAsyncExecute> {
	@GET
	@Path("chain/{chainId}")
	TaskAsyncExecute selectByTaskChainId(@PathParam("chainId") Long taskChainId);
	
	/**
	 * 工作流失败步骤异步重试
	 */
	@GET
	@Path("executer")
	void taskAsyncRetry();
	
	@DELETE
	@Path("name")
	void deleteByMclusterName(@QueryParam("clusterName") String mclusterName);

}
