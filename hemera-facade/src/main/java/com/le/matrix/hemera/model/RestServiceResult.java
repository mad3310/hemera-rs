package com.le.matrix.hemera.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class RestServiceResult {

	private Meta meta;
	private Response response;
	
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	
	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public static class Meta {
		private String code;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true,value = {"message","errorDetail","code","containers"} )
	public static class Response {
		private String message;
		private String errorDetail;
		private String code;
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getErrorDetail() {
			return errorDetail;
		}
		public void setErrorDetail(String errorDetail) {
			this.errorDetail = errorDetail;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		
	}

}
