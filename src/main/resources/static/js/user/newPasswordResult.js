const msg = "[[${session.MSG}]]";

if(msg) {
    alert(msg)
    location.href="/user/login"
} else {
    alert("비정상적인 접근입니다.")
    location.href="/user/login"
}