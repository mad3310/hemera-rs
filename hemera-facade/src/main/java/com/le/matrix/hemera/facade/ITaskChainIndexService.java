package com.le.matrix.hemera.facade;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.hemera.model.TaskChainIndex;
import com.le.matrix.redis.facade.IBaseService;

@Path("taskChainIndex")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ITaskChainIndexService extends IBaseService<TaskChainIndex>{
	@GET
	@Path("name")
	TaskChainIndex  selectByServiceAndClusterName(@QueryParam("serviceName") String serviceName, 
			@QueryParam("clusterName") String clusterName);
	
	@GET
	@Path("fail")
	List<Map<String, Object>> selectFailedChainIndex();
}
