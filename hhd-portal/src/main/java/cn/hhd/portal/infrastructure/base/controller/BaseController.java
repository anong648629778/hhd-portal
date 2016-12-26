package cn.hhd.portal.infrastructure.base.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;

public class BaseController {

	protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	// 每页显示的记录条数
	protected static final int PAGE_SIZE = 10;

	protected static final String SYSTEM_ERROR_MESSAGE = "系统繁忙，请稍候再试！";
	protected static final String RESULT_SUCCESS = "0";
	protected static final String RESULT_FAIL = "1";
	protected static final String ERROR = "error";
	protected static final String RESULT = "result";
	protected static final String RESULT_LIST = "resultList";
	protected static final String MESSAGE = "message";

	public static final String CODE = "code";
	protected static final int CODE_SUCC = 0;
	protected static final int CODE_ERROR = 1;

	@Resource
	protected HttpServletRequest request;
	@Resource
	protected HttpSession session;
	@Autowired
	private ResourceBundleMessageSource messageSource;

	/**
	 * 从请求中获取POST过来的内容
	 * 
	 * @return
	 */
	protected String getRequestPostContent() {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(request.getInputStream());
			byte[] contents = new byte[request.getContentLength()];
			bis.read(contents);
			return new String(contents, StandardCharsets.UTF_8.name());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}

	/**
	 * 向请求响应写消息
	 * 
	 * @param response
	 * @param message
	 */
	protected void writeMessageToResponse(HttpServletResponse response, String message) {
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			writer = response.getWriter();
			writer.write(message);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	/**
	 * 向请求响应写异步的json格式消息
	 * 
	 * @param response
	 * @param jsonString
	 */
	protected void writeJsonAjaxResponse(HttpServletResponse response, String jsonString) {
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType("application/json; charset=UTF-8");
			writer = response.getWriter();
			writer.write(jsonString);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	/**
	 * 向请求响应写异步的json格式消息（兼容app与H5）
	 * 
	 * @param response
	 * @param jsonString
	 */
	protected void writeJsonAjaxResponseForMultiClient(HttpServletResponse response, String callback, String jsonString) {
		PrintWriter writer = null;
		String responseString = null;
		try {
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType("application/json; charset=UTF-8");
			writer = response.getWriter();
			if (StringUtils.isNotEmpty(callback)) {
				responseString = callback + "(" + jsonString + ");";
			} else {
				responseString = jsonString;
			}
			writer.write(responseString);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	/**
	 * 获取某个路径在应用上下文中的绝对路径
	 * 
	 * @param path
	 * @return
	 */
	protected String getServletContextRealPath(String path) {
		return session.getServletContext().getRealPath(path);
	}

	/**
	 * 获取请求中的属性
	 * 
	 * @param key
	 * @return
	 */
	protected Object getRequestAttribute(String key) {
		if (null == request)
			return null;
		return request.getAttribute(key);
	}

	/**
	 * 设置请求中的属性
	 * 
	 * @param key
	 * @param val
	 */
	protected void setRequestAttribute(String key, Object val) {
		request.setAttribute(key, val);
	}

	/**
	 * 获取会话中存放的属性值
	 * 
	 * @param key
	 * @return
	 */
	protected Object getSessionAttribute(String key) {
		if (null == session)
			return null;
		return session.getAttribute(key);
	}

	/**
	 * 向会话中存放属性值
	 * 
	 * @param key
	 * @param val
	 */
	protected void setSessionAttribute(String key, Object val) {
		session.setAttribute(key, val);
	}

	/**
	 * 删除会话中的属性
	 * 
	 * @param key
	 */
	protected void removeSessionAttribute(String key) {
		session.removeAttribute(key);
	}

	/**
	 * 获取国际化资源文件中的提示信息
	 * 
	 * @param msgKey : 提示信息的key
	 * @param args ： 消息参数
	 * @return
	 */
	protected String getMessage(String msgKey, Object[] args) {
		return messageSource.getMessage(msgKey, args, SYSTEM_ERROR_MESSAGE, Locale.CHINA);
	}

	/**
	 * 获取国际化资源文件中的提示信息
	 * 
	 * @param msgKey :国际化资源文件中提示信息对应的key
	 * @return
	 */
	protected String getMessage(String msgKey) {
		return messageSource.getMessage(msgKey, null, Locale.CHINA);
	}

	/**
	 * 获取国际化资源文件中的提示信息
	 * 
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @param locale
	 * @return
	 */
	protected String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		String msg = messageSource.getMessage(code, args, defaultMessage, locale);
		return msg != null ? msg.trim() : msg;
	}

	protected HttpSession getSession() {
		return session;
	}

	protected HttpServletRequest getRequest() {
		return request;
	}

}
