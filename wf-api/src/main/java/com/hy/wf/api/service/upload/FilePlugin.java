///*
// * Copyright 2017-2018 All rights reserved.
// * Support: JinXing
// * License:
//*/
//
//package com.hy.wf.api.service.upload;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//
//import javax.servlet.ServletContext;
//
//import org.apache.commons.io.FileUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.ServletContextAware;
//
///**
// * Plugin - 本地文件存储 <br/>
// *
// * @author JinXing Team
// * @email
// * @date: 2018-4-20 上午12:30:09
// * @version 1.0
// */
//@Component("FilePlugin")
//public class FilePlugin extends StoragePlugin implements ServletContextAware {
//
//	/** servletContext */
//	private ServletContext servletContext;
//
//	@Override
//	public void setServletContext(ServletContext servletContext) {
//		this.servletContext = servletContext;
//	}
//
//	@Override
//	public void upload(String path, InputStream inputStream, String contentType) {
//		File destFile = new File(servletContext.getRealPath(path));
//		try {
//			FileUtils.
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void delete(String fileName) {
//		FileUtils.deleteQuietly(new File(fileName));
//	}
//
//}
//
