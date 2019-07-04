package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class HtmlGenController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genHtml")
	@ResponseBody
	public String genHtml() throws Exception{
		//从Spring容器中获取FreeMarkerConfigurer对象
		//从FreeMarkerConfigurer对象中获取Configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//使用Configuration对象获取Template对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建数据集
		Map data = new HashMap<>();
		data.put("hello", "冲电小子");
		//创建输出文件路径及文件名
		Writer writer = new FileWriter(new File("D:/studyfile/myprojectfile/freemarkertestfilepath/hello2.html"));
		//调用模板对象Template生成文件
		template.process(data, writer);
		//关闭流
		writer.close();
		return "OK";
	}

}
