package com.icelero.system;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Servlet to Initialize the Log4j logging, Edition Cache and Article Statistics
 * related tasks
 * 
 */
public class SystemInitializer extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	private static final long serialVersionUID = 4103947682029887943L;

	/**
	 * Article statistics synchronizer task scheduler
	 */
	@SuppressWarnings("unused")
	private ScheduledExecutorService scheduler;

	/**
	 * Logger, will be initialized in the init method.
	 */
	private Logger logger;

	/**
	 * Default Constructor
	 * 
	 */
	public SystemInitializer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Configure the Log4J Logger. Should be the first act, since the logger
		// will be used by other methods
		try {
			configureLog4J(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("Service Unavailable");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("Service Unavailable");
	}

	private void configureLog4J(ServletConfig config) {
		String logConfigurationFilePath = getInitParameter("log4j-init-file");
		if (logConfigurationFilePath != null) {
			DOMConfigurator.configureAndWatch(logConfigurationFilePath, 1000);
		} else {
			logger.error("Unable to configure logging, please check the log4j configuration file");
		}
		logger = Logger.getLogger(SystemInitializer.class);
		logger.info("Log Configuration File Path: " + logConfigurationFilePath);
	}

}
