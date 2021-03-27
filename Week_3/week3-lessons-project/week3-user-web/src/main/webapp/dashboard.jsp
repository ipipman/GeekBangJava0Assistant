<head>
<jsp:directive.include
	file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
<title>My Home Page</title>
</head>
<body>
	<div class="container-lg">
		欢迎来到<%=request.getAttribute("applicationName")%>，<%=request.getAttribute("name")%>
	</div>
</body>