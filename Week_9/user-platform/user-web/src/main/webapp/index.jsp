<head>
<jsp:directive.include
	file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
<title>My Home Page</title>
</head>
<body>
	<div class="container-lg">
		<!-- Content here -->
		<c:choose>
			<c:when test="${empty userInfo}">
				<p style="color: #ff0000">提示：授权失败</p>
			</c:when>
			<c:otherwise>
				<p>授权成功，用户信息：${userInfo}</p>
			</c:otherwise>
		</c:choose>
		Hello,World 2021
	</div>
</body>