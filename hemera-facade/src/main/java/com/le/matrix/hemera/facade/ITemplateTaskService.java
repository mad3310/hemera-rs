package com.le.matrix.hemera.facade;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.hemera.model.TemplateTask;
import com.le.matrix.redis.facade.IBaseService;

@Path("templateTask")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ITemplateTaskService extends IBaseService<TemplateTask>{
	@GET
	@Path("name/{name}")
	public TemplateTask selectByName(@PathParam("name") String name);
}
