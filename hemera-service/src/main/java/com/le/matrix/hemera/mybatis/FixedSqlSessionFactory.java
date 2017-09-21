package com.le.matrix.hemera.mybatis;

import java.io.IOException;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedSqlSessionFactory extends SqlSessionFactoryBean{
	
	private Logger logger = LoggerFactory.getLogger(FixedSqlSessionFactory.class);
	
    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        try {
            return super.buildSqlSessionFactory();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }finally {
            ErrorContext.instance().reset();
        }
        return null;
    }
}