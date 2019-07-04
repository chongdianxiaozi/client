<html>
	<head>
		<title>student</title>
	</head>
	<body>
		学生信息列表:<br>
		<table border="1">
			<tr>
			    <th>序号</th>
				<th>学号</th>
				<th>姓名</th>
				<th>年龄</th>
				<th>地址</th>
			</tr>
			<#list list as student>
			<#if student_index % 2 == 0>
			<tr bgcolor="red">
			<#else>
			<tr bgcolor="blue">
			</#if>
				<td>${student_index}</td>
				<td>${student.id}</td>
				<td>${student.name}</td>
				<td>${student.age}</td>
				<td>${student.address}</td>
			</tr>
			</#list>
		</table>
		<br>
		当前日期:${date?date}
		<br>
		当前日期:${date?time}
		<br>
		当前日期:${date?datetime}
		<br>
		当前日期:${date?string('yyyy/MM/dd HH:mm:ss')}
		<br>
		null值的处理:<br>
		${val!}<br>
		null值的处理:<br>
		${val!""}<br>
		null值的处理:<br>
		${val!"123"}<br>
		<#if val??>
		val有内容<br>
		<#else>
		val值为null<br>
		</#if><br>
		<#include "hello.ftl">
	</body>
	
</html>