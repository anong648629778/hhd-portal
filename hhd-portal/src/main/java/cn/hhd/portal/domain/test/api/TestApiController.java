package cn.hhd.portal.domain.test.api;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.hhd.portal.infrastructure.base.controller.BaseController;

@Controller
@RequestMapping("api/test")
public class TestApiController extends BaseController{

	@RequestMapping("detail")
	public void detail(@RequestParam(required = false, value = "name") String name, 
			@RequestParam(required = false, value = "callback") String callback, 
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		writeJsonAjaxResponseForMultiClient(response, callback, jsonObject.toString());
	}
}
