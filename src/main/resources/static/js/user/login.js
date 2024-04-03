/* ====================== *
 *  Toggle Between        *
 *  Sign Up / Login       *
 * ====================== */

let userIdCheck = "Y";

$(document).ready(function(){

    /*#########*/
    /* 회원가입 */
    /*#########*/

    let f = document.getElementById("form-signup");

    $("#btnUserId").on("click", function (){
        userIdExists(f)
    })

    $("#btnAddr").on("click", function (){
        kakaoPost(f);
    })

    $("#btnSend").on("click", function (){
        doSubmit(f);
    })

    /*#########*/
    /*  로그인  */
    /*#########*/

    $("#btnUserReg").on("click", function (){
        location.href = "/user/userRegForm";
    })

    $("#btnMain").on("click", function (){
        location.href = "/html/index"
    })

    $("#btnLogin").on("click", function () {

        if (idLogin.value === "") {
            alert("아이디를 입력하세요.");
            idLogin.focus();
            return;
        }

        if (passwordLogin.value === "") {
            alert("비밀번호를 입력하세요.");
            passwordLogin.focus();
            return;
        }

        let userId = idLogin.value;
        let password = passwordLogin.value;


        $.ajax({
            url : "/user/loginProc",
            type : "post",
            dataType : "JSON",
            data : { "userId": userId,
                     "password": password},
            success: function (json) {

                if (json.result === 1) {
                    alert(json.msg);
                    location.href = "/user/loginSuccess";
                    $("#IdLogin").focus();
                } else {
                    alert(json.msg);
                    $("#IdLogin").focus();
                }
            }
        })
    })

    $('#goRight').on('click', function(){
        $('#slideBox').animate({
            'marginLeft' : '0'
        });
        $('.topLayer').animate({
            'marginLeft' : '100%'
        });
    });
    $('#goLeft').on('click', function(){
        if (window.innerWidth > 769){
            $('#slideBox').animate({
                'marginLeft' : '50%'
            });
        }
        else {
            $('#slideBox').animate({
                'marginLeft' : '20%'
            });
        }
        $('.topLayer').animate({
            'marginLeft': '0'
        });
    });
});

function userIdExists(f) {
    if (f.userId.value === "") {
        alert("아이디를 입력하세요.");
        f.userId.focus();
        return;
    }

    $.ajax({
        url : "/user/getUserIdExists",
        type : "post",
        dataType : "JSON",
        data : $("#form-signup").serialize(),
        success : function (json) {
            if (json.existsYn === "Y") {
                alert("이미 가입된 아이디가 존재합니다.");
                f.userId.focus();
            } else {
                alert("가입 가능한 아이디입니다.");
                userIdCheck = "N";
            }
        }
    })
}

function kakaoPost(f) {

    new daum.Postcode({
        oncomplete : function (data) {
            let address = data.address;
            let zonecode = data.zonecode;
            f.addr1.value = "(" + zonecode + ")" + address
        }
    }).open();
}

/*#########*/
/* 회원가입 */
/*#########*/

function doSubmit(f) {
    if (f.userId.value === "") {
        alert("아이디를 입력하세요.");
        f.userId.focus();
        return;
    }

    if (userIdCheck !== "N") {
        alert("아이디 중복 체크 및 중복되지 않은 아이디로 가입 바랍니다.");
        f.userId.focus();
        return;
    }

    if(f.userName.value === "") {
        alert("이름을 입력하세요.");
        f.userName.focus();
        return;
    }

    if (f.password.value === "") {
        alert("비밀번호를 입력하세요.");
        f.password.focus();
        return;
    }

    if (f.password2.value === "") {
        alert("비밀번호를 입력하세요.");
        f.password2.focus();
        return;
    }

    if (f.password.value !== f.password2.value) {
        alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        f.password.focus();
        return;
    }

    if (f.email.value === "") {
        alert("이메일을 입력하세요.");
        f.email.focus();
        return;
    }

    if (f.addr1.value === "") {
        alert("주소를 입력하세요.");
        f.addr1.focus();
        return;
    }

    if (f.addr2.value === "") {
        alert("상세주소를 입력하세요.");
        f.addr2.focus();
        return;
    }

    $.ajax({
        url : "/user/insertUserInfo",
        type : "post",
        dataType : "JSON",
        data : $("#form-signup").serialize(),
        success : function (json) {
            if (json.result === 1) {
                alert(json.msg);
                location.href = "/user/login";
            } else {
                alert(json.msg);
            }
        }
    })
}

/* ====================== *
 *  Initiate Canvas       *
 * ====================== */
paper.install(window);
paper.setup(document.getElementById("canvas"));

// Paper JS Variables
var canvasWidth,
    canvasHeight,
    canvasMiddleX,
    canvasMiddleY;

var shapeGroup = new Group();

var positionArray = [];

function getCanvasBounds() {
    // Get current canvas size
    canvasWidth = view.size.width;
    canvasHeight = view.size.height;
    canvasMiddleX = canvasWidth / 2;
    canvasMiddleY = canvasHeight / 2;
    // Set path position
    var position1 = {
        x: (canvasMiddleX / 2) + 100,
        y: 100,
    };

    var position2 = {
        x: 200,
        y: canvasMiddleY,
    };

    var position3 = {
        x: (canvasMiddleX - 50) + (canvasMiddleX / 2),
        y: 150,
    };

    var position4 = {
        x: 0,
        y: canvasMiddleY + 100,
    };

    var position5 = {
        x: canvasWidth - 130,
        y: canvasHeight - 75,
    };

    var position6 = {
        x: canvasMiddleX + 80,
        y: canvasHeight - 50,
    };

    var position7 = {
        x: canvasWidth + 60,
        y: canvasMiddleY - 50,
    };

    var position8 = {
        x: canvasMiddleX + 100,
        y: canvasMiddleY + 100,
    };

    positionArray = [position3, position2, position5, position4, position1, position6, position7, position8];
};


/* ====================== *
 * Create Shapes          *
 * ====================== */
function initializeShapes() {
    // Get Canvas Bounds
    getCanvasBounds();

    var shapePathData = [
        'M231,352l445-156L600,0L452,54L331,3L0,48L231,352',
        'M0,0l64,219L29,343l535,30L478,37l-133,4L0,0z',
        'M0,65l16,138l96,107l270-2L470,0L337,4L0,65z',
        'M333,0L0,94l64,219L29,437l570-151l-196-42L333,0',
        'M331.9,3.6l-331,45l231,304l445-156l-76-196l-148,54L331.9,3.6z',
        'M389,352l92-113l195-43l0,0l0,0L445,48l-80,1L122.7,0L0,275.2L162,297L389,352',
        'M 50 100 L 300 150 L 550 50 L 750 300 L 500 250 L 300 450 L 50 100',
        'M 700 350 L 500 350 L 700 500 L 400 400 L 200 450 L 250 350 L 100 300 L 150 50 L 350 100 L 250 150 L 450 150 L 400 50 L 550 150 L 350 250 L 650 150 L 650 50 L 700 150 L 600 250 L 750 250 L 650 300 L 700 350 '
    ];

    for (var i = 0; i <= shapePathData.length; i++) {
        // Create shape
        var headerShape = new Path({
            strokeColor: 'rgba(255, 255, 255, 0.5)',
            strokeWidth: 2,
            parent: shapeGroup,
        });
        // Set path data
        headerShape.pathData = shapePathData[i];
        headerShape.scale(2);
        // Set path position
        headerShape.position = positionArray[i];
    }
};

initializeShapes();

/* ====================== *
 * Animation              *
 * ====================== */
view.onFrame = function paperOnFrame(event) {
    if (event.count % 4 === 0) {
        // Slows down frame rate
        for (var i = 0; i < shapeGroup.children.length; i++) {
            if (i % 2 === 0) {
                shapeGroup.children[i].rotate(-0.1);
            } else {
                shapeGroup.children[i].rotate(0.1);
            }
        }
    }
};

view.onResize = function paperOnResize() {
    getCanvasBounds();

    for (var i = 0; i < shapeGroup.children.length; i++) {
        shapeGroup.children[i].position = positionArray[i];
    }

    if (canvasWidth < 700) {
        shapeGroup.children[3].opacity = 0;
        shapeGroup.children[2].opacity = 0;
        shapeGroup.children[5].opacity = 0;
    } else {
        shapeGroup.children[3].opacity = 1;
        shapeGroup.children[2].opacity = 1;
        shapeGroup.children[5].opacity = 1;
    }
};
