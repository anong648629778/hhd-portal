package cn.hhd.portal.infrastructure.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class AntiSqlInjectionFilter implements Filter {

	// 过滤掉的sql关键字，可以手动添加
	// 现阶段只能做最少限度匹配（存在误判情况），后续考虑更好方法
	private static final String SQL_WORD = "exec|execute|insert into|delete from|drop table|master|truncate|net user|xp_cmdshell|create table|create database|grant|group_concat|column_name|information_schema.columns|table_schema";
	
	/**
	 * 向请求响应写异步的json格式消息
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

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest args0, ServletResponse args1, FilterChain chain) throws IOException, ServletException {
		// if (!EnvUtil.isTestEnv()) {
		HttpServletRequest req = (HttpServletRequest) args0;
		HttpServletResponse res = (HttpServletResponse) args1;
		// 获得所有请求参数名
		Enumeration<String> params = req.getParameterNames();
		String sql = "";
		String callback = "";
		while (params.hasMoreElements()) {
			// 得到参数名
			String name = params.nextElement().toString();
			// 得到callback
			callback = name.equals("callback") ? req.getParameterValues(name)[0] : callback;
			// 得到参数对应值
			String[] value = req.getParameterValues(name);
			for (int i = 0; i < value.length; i++) {
				sql = sql + value[i];
			}
		}

		// 有sql关键字，直接返回jsonp
		if (sqlValidate(sql)) {
			System.out.println("aliyun sql check unpass!!! [url] is: " + req.getRequestURL());
			writeJsonAjaxResponseForMultiClient(res, callback, "{\"code\":1}");
			return;
		}
		// }

		chain.doFilter(args0, args1);
	}

	// 效验
	protected static boolean sqlValidate(String content) {
		return Pattern.compile(SQL_WORD).matcher(content.toLowerCase()).find();
	}
}