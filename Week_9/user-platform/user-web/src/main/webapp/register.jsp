<head>
	<jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
	<jsp:directive.include file="/WEB-INF/jsp/prelude/variables.jspf" />
	<title>My Register Page</title>
	<style>
		.bd-placeholder-img {
			font-size: 1.125rem;
			text-anchor: middle;
			-webkit-user-select: none;
			-moz-user-select: none;
			-ms-user-select: none;
			user-select: none;
		}

		.text-error {
			color: #ff0000;
			white-space: pre-line;
		}
		.hidden {
			display: none;
		}

		@media (min-width: 768px) {
			.bd-placeholder-img-lg {
				font-size: 3.5rem;
			}
		}
	</style>
</head>
<body>
<div class="container">
	<form method="post" action="${contextURI}/register" class="form-signin">
		<h1 class="h3 mb-3 font-weight-normal">注册</h1>

		<label for="inputUsername" class="sr-only">用户名</label>
		<input type="text" id="inputUsername" name="username"
			   class="form-control" value="kafka" placeholder="请输入用户名" autofocus>

		<label for="inputPassword" class="sr-only">密码</label>
		<input type="password" id="inputPassword" name="password"
			   class="form-control" value="123456" placeholder="请输入密码">

		<label for="inputEmail" class="sr-only">邮箱</label>
		<input type="text" id="inputEmail" name="email"
			   class="form-control" value="123@qq.com" placeholder="请输入邮箱">

		<label for="inputPhoneNumber" class="sr-only">手机号</label>
		<input type="text" id="inputPhoneNumber" name="phoneNumber"
			   class="form-control" value="13688888888" placeholder="请输入手机号">

		<c:if test="${!empty errorMessage}">
			<p class="mt-2 mb-2 text-error">提示：${errorMessage}</p>
		</c:if>

		<button class="btn btn-lg btn-primary btn-block" type="submit">注册</button>
		<p class="mt-5 mb-3 text-muted">&copy; 2017-2021</p>
	</form>
</div>
</body>