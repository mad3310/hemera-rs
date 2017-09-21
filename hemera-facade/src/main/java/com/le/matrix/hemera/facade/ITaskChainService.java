package com.le.matrix.hemera.facade;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.hemera.model.TaskChain;
import com.le.matrix.redis.facade.IBaseService;

@Path("taskChain")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ITaskChainService extends IBaseService<TaskChain> {
	@GET
	@Path("next")
	TaskChain selectNextChainByIndexAndOrder(@QueryParam("chainIndexId") Long chainIndexId, 
			@QueryParam("executeOrder") int executeOrder);
	
	@GET
	@Path("fail")
	TaskChain selectFailedChainByIndex(@QueryParam("chainIndexId") long chainIndexId);

	@GET
	@Path("chains")
	List<TaskChain> selectAllChainByIndexId(@QueryParam("chainIndexId") Long chainIndexId);

	@PUT
	@Path("status")
	void updateAfterDoingChainStatus(Map<String, Object> params);

	@POST
	@Path("batch")
	public void insertBatch(List<TaskChain> taskChains);

}
