<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">    
<title>로그인</title>
<style>
	.login {
		/* 해상도 세로 길이 100%로 지정 */
		height: 100vh;
		display: flex;
		/* 가운데 정렬 */
		justify-content: center;
		/* 수직 가운데 정렬 */
		align-items: center;		
	}
	.loginForm {
		width: 100%;
		/* 최대 가로길이 지정 */
		max-width: 400px;
		padding: 20px;
		border-radius: 15px;
	}
</style>
</head>
<body>
	<div class="login container d-flex w-50 justify-content-center">
		<div class="loginForm">
			<h2 class="text-center mb-4">로그인</h2>
			<form name="loginForm" action="/login.do" method="post" onsubmit="return validateForm(this);">
				<div class="input-group mb-3">
				  <span class="input-group-text" id="inputGroup-sizing-default">ID</span>
				  <input type="text" id="mb_id" name="mb_id" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">
				</div>
				<div class="input-group mb-3">
				  <span class="input-group-text" id="inputGroup-sizing-default">PW</span>
				  <input type="password"  id="mb_password" name="mb_password"  class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">
				</div>
				<div>
					<a href="/member/findId.do">ID찾기/</a><a href="/member/findPassword.do">비밀번호찾기</a>
				</div>
				<div class="d-grid gap-2">
					<button type="submit" class="btn btn-outline-success" onclick="checkValue();" value="로그인">로그인</button>
					<input type="button" class="btn btn-outline-success" value="회원가입" onclick="location.href='/register.do'"/>
				</div>
			</form>
		</div>
	</div>
</body>
</html>