package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTest {
	
	@Test
	public void testFreeMarker() throws Exception {
		//1.创建一个模板文件
		//2.创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3.设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("D:/client/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//4.模板文件的编码格式,一般为utf-8
		configuration.setDefaultEncoding("utf-8");
		//5.加载一个模板文件,创建一个模板对象
		//Template template = configuration.getTemplate("hello.ftl");
		//Template template = configuration.getTemplate("student.ftl");
		Template template = configuration.getTemplate("list.ftl");
		//6.创建一个数据集,pojo或者map,一般使用map,自由方便
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker");
		//创建一个pojo对象
		//Student student = new Student(1,"冲电小子",18,"浙江杭州");
		//data.put("student", student);
		//创建一个list
		List<Student> list = new ArrayList<>();
		list.add(new Student(1,"冲电小子",18,"浙江杭州"));
		list.add(new Student(2,"充电小子",18,"中国北京"));
		data.put("list", list);
		data.put("date", new Date());
		data.put("val", null);
		//7.创建一个Writer对象,指定输出文件的路径及文件名
		//Writer out = new FileWriter(new File("D:/studyfile/myprojectfile/freemarkertestfilepath/hello.txt"));
		//Writer out = new FileWriter(new File("D:/studyfile/myprojectfile/freemarkertestfilepath/student.html"));
		Writer out = new FileWriter(new File("D:/studyfile/myprojectfile/freemarkertestfilepath/list.html"));
		//8.生成静态页面
		template.process(data, out);
		//9.关闭流
		out.close();
		
	}

}
