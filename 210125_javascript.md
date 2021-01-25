### 2021_01_25_월 

15-7강, 16강 이벤트 
17강  jQuery 까지 함 + 문제풀이

#### 숙제 피드백

##### 4번 

'만약에 90점 입니다.' 
```html
<p><span ="score">90</span>점입니다.</p>
```


블럭태그 <div>

```html
  <div id="wrap">
    <div id="objDiv">Object Divistion</div>
    <p><span id="score">90</span>점입니다.</p>
  </div>
```
##### 5번 
body - addChild로 부모자식으로 처리해야함!
-> 이게 DOM의 핵심
-> 핸들러, 표준모델인 addEventListent로 하는게 좋다.
<br><br />

### 15-7 이벤트 객체 : this -> 이벤트가 발생한 객체

false는 (프로파간다) 나중에.
```javascript
window.onload = function() {
  function addHandler(){
    console.log("click!!");
    console.log("this : " + this);
    
    this.style.backgroundColor = "#0000ff";
    
  };

  var objD = document.getElementById("objDiv");
  objD.addEventListener("click", addHandler, false);

  var objP = document.getElementById("objPar");
  objP.addEventListener("click", addHandler, false);
};
```

### 15-8 : 이벤트 객체 (ie8버전 고려)
(Ex : 15_06.html)  event -> 이벤트 객체

```javascript
  window.onload = function() {
    function addHandler(e){
      console.log("click!!");
      console.log("this : " + this);
      
      //IE8 이전버전을 고려
      var event = e || window.event;
      for( var key in event ) {
        console.log(key + " : " + event[key]);
      }
    };

    var objD = document.getElementById("objDiv");
    objD.addEventListener("click", addHandler, false);

    var objP = document.getElementById("objPar");
    objP.addEventListener("click", addHandler, false);
  };
```

클릭을 했을 때 스크린 위치를 알고싶다. 이때 이 이벤트 처리를하고
생각보다 많이 쓰인다. 이 e는 내가 이벤트를 click하면 활성화.
-> screenX, screenY... 등등 콘솔창에서 확인할 수 있다.


## 16강_javascript 이벤트 실전

### 16-1 마우스 이벤트
어떠한 이벤트들이 있는지 확인하는 것.
```javascript
window.onload = function(){
				var me = document.getElementById("mouseEvent");
				me.addEventListener("click", function(){  //상자 클릭 시
					console.log("click event!!");
				}, false);
				
				me.addEventListener("mouseover", function(){ // 상자로 진입 시
					console.log("mouseover event!!");
				}, false);
				
				me.addEventListener("mouseout", function(){ //
					console.log("mouseout event!!");
				}, false);
				
				me.addEventListener("mousemove", function(e){
					console.log("mousemove event!!");
					//console.log("x : " + e.clientX + ", y : " + e.clientX);
				}, false);
				
				me.addEventListener("mousedown", function(e){
					console.log("mousedown event!!");
				}, false);
				
				me.addEventListener("mouseup", function(e){ //마우스가 
					console.log("mouseup event!!");
				}, false);
				
				me.addEventListener("dblclick", function(e){
					console.log("dblclick event!!");
				}, false);
      }
```
그냥넘어간다.. 뭔지 모르겠지만...허허...

### 16-2 : 양식(폼) 이벤트
(Ex : 16_02.html)
많이씀 
input 타입을 submit으로 안주고 button으로 줌. 이유는 이벤트를 먹이기 위해서

- **.onclick 이벤트** 
```javascript wrap
		window.onload = function(){

        //이게바로 자바스크립트 유효성검사이다!!!! (대표적인 방법!!!)
        
				var sbmBtn = document.getElementById("sbmBtn");  // 버튼형식으로 주고 sbmBtm받아와서
				sbmBtn.onclick = function() {   //클릭했을 때 이게 일어나라고
					if(document.getElementById("uId").value == "") {    //value가 없으면 빠져나와라
						alert("user id blank!!");
					} else if(document.getElementById("uPw").value == "") { //객체 찾아서 value없으면 
						alert("user pw blank!!");
					} else {
						alert("login ok!!");  // 다 아닐 경우에만 ok 전송한다. submit()
						document.getElementById("loginForm").submit();
					}
				};
				
				var resBtn = document.getElementById("resBtn");
				resBtn.onclick = function() {
					alert("reset ok!!");
					document.getElementById("loginForm").reset(); //이건 form 태그 초기화 reset
				};
				
      }
```

- **html**
  ```html
    <form id="loginForm" action="http://www.google.com">
      USER ID : <input id="uId" type="text" name="uId"><br />
      USER PW : <input id="uPw" type="password" name="uPw"><br />
      <input id="sbmBtn" type="button" value="SUBMIT">
      <input id="resBtn" type="button" value="RESET">
    </form>
  ```  

- **.onclick -> .onsubmit 이벤트**  <br>
 두번째 submit누르면 이벤트가 발생하게끔 되는 구조이다.<br>
 폼태그가 가지고있는것.
  ```javascript
  window.onload = function(){
          var lf = document.getElementById("loginForm");
          lf.onsubmit = function() {
            if(document.getElementById("uId").value == "") {
              alert("user id blank!!");
              return false;
            } else if(document.getElementById("uPw").value == "") {
              alert("user pw blank!!");
              return false;
            } else {
              alert("login ok!!");
              return true;
            }
          };
        }
  ```

- **html**
  ```html
    <form id="loginForm" action="http://www.google.com">
      USER ID : <input id="uId" type="text" name="uId"><br />
      USER PW : <input id="uPw" type="password" name="uPw"><br />
      <input id="sbmBtn" type="submit" value="SUBMIT"> <!--button -> submit -->
      <input id="resBtn" type="reset" value="RESET">  <!--button -> reset -->
    </form>
  ```

---

## 17강_jQuery 개요 및 기본문법
### 17-1 jQuery 란?

- javascript : 조잡하고 복잡함, Dom관련기능 브라우져 이슈가 있다.
   -> 지금은 근데 바뀌지 않았나?? 지금은 표준이 나름 있다고 들었는데..! 
   -> ei는 이제 안녕...! 괜찮을듯...
   
   누군가 중엔에서 관리한것 -> 이게 jQuery

- jQuery : 자바스크립트를 한번 더 캡슐화 시킨 것! 인캡슐레이션

  > **자주 사용하는 기능을 함수(라이브러리)** 로 감싸서 쉽게 사용할 수 있도록 함.<br>
  • DOM 관련된 기능을 브라우저에 상관 없이 실행하도록 함(크로스 브라우저)<br>
  • 풍부한 함수(라이브러리) 제공 <br>
  • CSS선택자를 그대로 사용함 <br>
  • 대형 프로젝트 또는 규모가 큰 개발사의 경우 자체 프레임워크를 만들어 사용함. 

### 17-2 : jQuery 설치- I
자바스크립트는 버전을 탄다.
직접 다운로드 받는 방법!

### 17-2 : jQuery 설치- II
예전에js안에 table.html 게시판 가져와서 돌렸을 때, 자바스크립트 코드 가져왔다. 그렇게 주소로 가져오면 브라우저에 캐시 형태로 다운로드 받는 것!
라이브러이이다!

### 17-3 : 기본문법
```javascript
$(선택자).함수명(매개변수);
```

jQuery는 문법이 쉽다. ( 쉽게 사용하도록 만든 것이기 때문에 )
```javascript
document.getElementById("");
-> jQuery("p").css("color", "red");
-> $("p").css("fontWeight", "bolder");
```

- 코딩 실습
```html
<!DOCTYPE html>
<html lang="en">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!--똑같은 것이지만 min.js는 축약시킨것이다.-->

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>

  <script>
    window.onload = function () {

      jQuery("p").css("color", "red");
      $("p").css("fontWeight", "bolder");

      var myJQ = jQuery("p");
      myJQ.css("textDecoration", "underLine");

      myJQ.css("width", "300px").css("border", "1px solid #ff0000");

      $("p").css("fontSize", "2em").css("height", "300px").css("lineHeight", "300px").css("textAlign", "center");
    };
    // 왼쪽부터 순서대로 적용해서 myJQ로 리턴하니까 myJQ.css()로 계속 되면서 적용함
    // $("p")도 마찬가지임!

  </script>
  </title>

<body>

  <p>Hello jQuery!</p>

</body>

</html>
```

### 문제 : 배경색상 바꾸는것
- 버튼 클릭시에 랜덤하게 색상 바꾸기
```js
"#ff0", "#6c0", "#fcf", "#cf0", "#39f"
```

