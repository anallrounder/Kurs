# 2021_01_27_수_javascript

오늘 수업: 18-19강

## 18강_jQuery 선택자

### 18-1 $(document).reday

javascript - onload -> 웹브라우저가 바디까지 다 읽고 실행 <Br>
jQuery

### 18-2 자주 사용하는 선택자 - 1

 • 전체 선택자 • 태그 선택자 • 아이디 선택자 • 클래스 선택자 • 후손 선택자 • 자손 선택자

- 제이쿼리에서는 $(document)돔객체를 제이쿼리 함수안에 넣고  .reday (함수?)를 만들었다.

   ```html
  <script>
    window.onload = function(){...} -> $(document).ready(function(){...})
  </script>
  ```

- css의 선택자 그대로 사용
- 달러 안에다가 function을 넣었다.
  
  ```html
  <script>
   $(document).ready(function(){...}) = $(function(){...})
  </script>
  ```

- 이 중 어떤게 뿌려질까? 
  
  ```html
  <script>

    $(document).ready(function () {
        document.write("아아아아아아아");
    });

    $(document).ready(function () {
        document.write("우우우우우");
    });

    $(function () { // 위에와 같은 함수 - 도큐멘트.reday 생략가능
        document.write("가가가가가");
    });

  </script>
  ```

  정답 : 다 뿌린다. <br>
  onload는 마지막거만 실행되지만, 제이쿼리를 통해서 같은 함수를 실행하면, 전부다 실행된다.

- 실습
  
  ```html
  <!DOCTYPE html>
  <html lang="en">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script>
      $(document).ready(function () {
        $("*").css("fontSize", "1,1em");    //전체 선택자 (모든)
        $("li").css("color", "#0000ff");    //태그 선택자
        $("#item").css("background", "#ffff00");  //아이디 선택자
        $(".seoul").css("background", "ff0000"); //클래스 선택자
        $("#wrap p").css("border", "2px solid #cccccc");  //후손 선택자
        $("#wrap > p").css("border", "5px solid #cccccc");  //자손 선택자
      });
    </script>
  </head>
  <body>
    <div id="wrap">
      <p>jQuery selector</p>
      <div id="centent">
        <h1>centent</h1>
        <p>ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅌ</p>
        <p>ABCDEFGHIJKLMN</p>
      </div>
      <div id="item">
        <ul>
          <li class="seoul">서울</li>
          <li class="gyeonggi_do">경기도</li>
          <li>충청도</li>
          <li>전라도</li>
          <li>경상도</li>
          <li>강원도</li>
          <li>제주도</li>
        </ul>
      </div>
    </div>

  </body>

  </html>
  ```

### 18-3 자주사용하는 선택자 - II

• 속성 선택자 • 필터 선택자 • 함수 선택자

- input에 체크되어 있는 것도 선택해서 css속성을 적용할 수 있다.
- 실습
  
  ```html
  <!DOCTYPE html>
  <html lang="en">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script>

      $(document).ready(function () {

        $("input[type='text']").css("color", "ff0000");   // 속성선택자
        $("input[type='passward']").css("color", "0000ff");   //속성 선택자
        $("input[type='submit']").css("fontSize", "1.2em");   // 필터 선택자
        var chk = $("input:checked").val();   //필터 선택자
        console.log("chk : " + chk);

        $("li:nth-child(2n+1)").css("background", "ffff00");  //함수 선택자

      });
    </script>

  <body>
    <form>
      UserID <input type="text">
      <br />
      UserPW <input type="password">
      <br />
      Submit <input type="submit" value="submit">
      <br />
      Reset <input type="reset" value="reset">
      <br />
      Food <br />
      사과<input type="checkbox" id="apple" value="사과" />
      바나나<input type="checkbox" id="banana" value="바나나" checked />
      수박<input type="checkbox" id="watermelon" value="수박" />
      <br />

      <ul>
        <li>이만기</li>
        <li>이승엽</li>
        <li>이종범</li>
        <li>이재학</li>
        <li>이순철</li>
        <li>이길동</li>
        <li>이동국</li>
      </ul>
    </form>
  </body>

  </html>
  ```


## 19강_jQuery DOM 조작

19-1 객체 생성 | 19-2 객체 삽입 | 19-3 객체 이동 | 19-4 객체 복제 <br>

### 19-1 : 객체 생성

- ```$():``` -> 이걸로 객채생성

```html
<head>
  <script>
    //문서 객체 생성
    $(document).ready(function () {

      var wrapObj = $("#wrap");
      // <div>객체 생성 및 추가
      var divObj = $("<div> Hello jquery! </div>"); //부모자식 관계를 만든다.
      divObj.appendTo(wrapObj);
      divObj.css("background", "ff0000");
    });
  </script>
</head>
<body>
  <div id="wrap">
  </div>
</body>
```

appendTo()를 하면 아래와 같이 wrap의 자식으로 붙음 : 부모자식 관계를 만든다!

```html
<!-- 이렇게 표현되는 것과 같음 -->
<div id="wrap">
  <div> Hello jquery! </div>
</div>
```

```html
<script>
   $(document).ready(function () {
      // <img>객체 생성 및 추가
      var imgObj = $("<img src='img/logo.png' /><br>");
      imgObj.appendTo(wrapObj);

      // <img>, 속성객체 생성 및 추가
      var tempObj = {
        src: "img/arm_mbed.png",
        width: 297, //px
        height: 124,  //px
        border: "5px"
      };

      var imgAObj = $("<img>", tempObj);
      imgAObj.appendTo(wrapObj);

    });
  </script>
```

- 이것과 같음

```html
<div id="wrap">
  <div> Hello jquery! </div>
  <img src="img/logo.png">
  <br>
  <img src="img/arm_mbed.png" border="5px" 
    style="width: 297px; height: 124px;">
</div>
```

### 19-2 : 객체 삽입 

#### 객체 삽입 

- 부모 자식 관계로 태그 안에 삽입 시킴  
- .appendTo() : 자식.appendTo(부모)| 자식 태그들 중 끝부분에 삽입
- .append()   : 부모.append(자식) | 자식 태그들 중 시작부분에 삽입
- .prependTo() : 
- .prepend()
  
  
#### 객체 추가

- 동등관계에서 추가함 (동위원소)
- .insertAfter()
- .after()
- .insertBefore()
- .before()
  
참고: https://baessi.tistory.com/87

- 실습

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script>

      $(document).ready(function () {

        var wrapObj = $("#wrap");

        //객체 생성
        var divObj = $("<div> Hello jQuery! </div>");
        var divObj1 = $("<div> bye~ jQuery! </div>");

        /*
        객체 삽입
        */ 
        /*
        .append(), .appendTo()
        - 부모 자식 관계에서 붙여넣을 때 사용 (이 예시는 전부다 wrapObj가 부모)
          ( to부모 -> 부모에게 붙여라(삽입) )
        */

        // 자식.appendTo(부모)  
        divObj.appendTo(wrapObj);      // 자식 태그 중 뒤로 붙여넣음
        divObj1.appendTo(wrapObj);     // 그래서 이게 뒤로 붙음

        // 부모.append(자식)
        wrapObj.append(divObj);      // 앞에 거에 붙임 (자식으로)
        wrapObj.append(divObj1);     // 순서대로 붙임


        divObj.prependTo(wrapObj);   // 부모 기준으로  자식의 앞부분에 붙여넣음
        divObj1.prependTo(wrapObj); // 그래서 이게 결구 맨 앞에 붙음

        wrapObj.prepend(divObj);      // 부모기준 앞으로 붙임 
        wrapObj.prepend(divObj1);     // 그래서 이게 앞으로 붙음

        // 여기는 전부다 동등관계 (동위 원소)
        // after, before는 동등관계에서 붙여넣을 때 사용
        // 영어 그대로 해석하면 됨. 
        divObj.insertAfter(wrapObj);   // 랩 뒤에 이어서 붙여 넣음
        divObj1.insertAfter(wrapObj); // 랩 뒤에 바로 붙여넣어서 이게 위로 감

        wrapObj.after(divObj);      // 랩 뒤에 와라. div가
        wrapObj.after(divObj1);     // 이것도 바로 랩 뒤에 옴

        divObj.insertBefore(wrapObj);   // 랩 앞에 붙여 넣음
        divObj1.insertBefore(wrapObj);  // 이게 랩 바로 앞으로 붙여 넣어짐 

        wrapObj.before(divObj);      // 랩 앞에 붙여 넣음
        wrapObj.before(divObj1);    //  이게 랩 바로 앞으로 붙여 넣어짐 

      });
    </script>
  </head>
  <body>

    <div id="wrap">
      content
    </div>

  </body>
  </html>
  ```

### 19-3 : 객체 이동

여기서 결론을 내려야할 것은?
외울것 , 이해해야 하는 것.
>>> 부모자식관계

```html
<!--19-3-->
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <script>
    $(document).ready(function () {

      $("#wrap > .contents1 > .p").appendTo($("#wrap > .contents2"));

    });
  </script>

  <style>
    #wrap .contents1 .p {
      background: #ff0000;
    }

    #wrap .contents2 .p {
      background: #ffff00;
    }
  </style>
</head>

<body>
  <div id="wrap">
    <div class="contents1">
      <p class="p">contents1</p>
    </div>

    <div class="contents2">
      <p class="p">contents2</p>
    </div>
  </div>
</body>

</html>
```

### 19-4 : 객체 복제

예시는 복사한게 뒤에 붙음!

오늘 수업은 여기까지.