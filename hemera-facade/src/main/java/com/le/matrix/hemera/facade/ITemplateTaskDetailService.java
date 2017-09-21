package com.le.matrix.hemera.facade;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.hemera.model.TemplateTaskDetail;
import com.le.matrix.redis.facade.IBaseService;

@Path("templateTaskDetail")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ITemplateTaskDetailService extends IBaseService<TemplateTaskDetail> {
	@GET
	@Path("type/{type}")
	List <TemplateTaskDetail> selectByTemplateTaskType(@PathParam("type") String type);
	
}
