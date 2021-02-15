# 2021.02.15_월_AOP

인터셉터 - 가로챔

**오늘의 결론**  
학습 목표를 개념 이해에서 끝내지 말고 활용을 할 수 있도록 하자. (소스코드로 어떻게 표현되는지, 어떻게 구현되는지를 꼭 알야야하고 이거를 외우자!)

## AOP(Aspect Oriented Programming)

- 관점지향 프로그래밍
- 기존의 OOP(object oriented programming)언어를 보완하는 확장 형태로 사용됨
- 스프링에서 내세우는 간판 기능중 하나이다.
- 실질적으로 내부적으로는(내부동작) aop가 적용된다.
- 대표적으로 AspectJ, JBossAOP, SpringAOP가 있다.

### 특징

- 여러 함수에서 중복되는 기능들을 따로 빼서 만든 뒤에 실행중에(필요한 시점에) 주입시키듯이 집어넣어서 실행시키는 것
- 프레임워크 차원에서 개발자가 일일이 작성할 필요 없게 스프링에서 지원함  
- 핵심관점(업무, 비지니스 로직) + 횡단관점(공통로직-트랜잭션/로그/보안/인증 처리 등) 으로 관심의 분리,구분(Separation of Concerns)를 실현

- [참고 사이트 1](http://wiki.gurubee.net/pages/viewpage.action?pageId=26740833)  |  [참고 사이트 2](https://jeong-pro.tistory.com/171)

---

### 실습

- aop를 사용해서 `======== 로그기록=========`이 콘솔창에서  getList()함수가 실행되기전에 나오는것을 확인해보자.

> edu.bit.ex.board.aop

#### LogAdvice.java

```java
package edu.bit.ex.board.aop;

public class LogAdvice {
  public void printLogging() {
    System.out.println("======== 로그 기록=========");
  }
}
```

#### BoardService.java 

```java
public List<BoardVO> getList();
```

- 리스트를 불러와서( BoardService의 getList() 를 실행시켜서) 그 전에 aop가 작동하는지 확인해 보려고한다.
- 즉, 이 BoardService의 getList() 함수를 호출하기 전에 방금 위에서 만든 printLoffing() 함수를 집어 넣으려고하는 것! 그러기 위해서는 다음의 xml설정을 해야한다.  

!! 참고 !! aop-context.xml에 설정하는 방법이 안돼서 아래 내용을 servlet-context.xml에 넣었다.  
수정한 코드는 바로 아래에 이어서 작성했음 (이유는 복잡해서 패스했음)

> src > main > webapp > WEB-INF > spring

#### aop-context.xml (사용 안함 - 에러)

- root-context.xml 과 같은위치에 만들것 (생성 위치 주의)
- 내용은 선생님 파일 복붙해서 넣음 (아래 소스)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:mybatis-spring="http://mybatis.org/schema/mybatis-spring"
   xmlns:context="http://www.springframework.org/schema/context"
   xmlns:aop="http://www.springframework.org/schema/aop"
   xsi:schemaLocation="http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring-1.2.xsd
      http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
      http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
      http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd">
    
   <!-- Root Context: defines shared resources visible to all other web components -->
<!--    <aop:aspectj-autoproxy></aop:aspectj-autoproxy> -->
   
    <!-- 아직 안만들었으니까 잠시 주석 -->   
<!--    <bean id="logAop" class="edu.bit.board.aop.LogAop" /> -->
   <beans:bean id="logAdvice" class="edu.bit.board.aop.LogAdvice" />

<!--    AOP설정    -->
   <aop:config>
      <aop:aspect ref="logAdvice">
         <aop:pointcut id="publicM" expression="within(edu.bit.board.service.*)"/>
<!--       <aop:around pointcut-ref="publicM" method="loggerAop" /> -->
         <aop:before pointcut-ref="publicM" method="printLogging" />
      </aop:aspect>
   </aop:config>
   
    <!-- 잠시 주석 -->
  <!--  <aop:config>
      aspect id는 logger이고, logAop를 참조함
      <aop:aspect  ref="logAop">
          pointcut(핵심 기능)의 id는 publicM이고, edu.bit.ex.* 패키지에 있는 모든 클래스에 공통 기능을 적용
         <aop:pointcut id="publicM" expression="within(edu.bit.board.service.*)"/>
         loggerAop()라는 공통 기능을 publicM라는 pointcut에 적용
         <aop:around pointcut-ref="publicM" method="loggerAop" />          
      </aop:aspect>
   </aop:config>  -->         
</beans>
```

> src > main > webapp > WEB-INF > spring > appServlet

#### servlet-context.xml 설정
  
- 추가한 것만 작성함

```xml
xmlns:aop="http://www.springframework.org/schema/aop"

...
<!-- <aop:aspectj-autoproxy></aop:aspectj-autoproxy>  -->
<!--이거는 어노테이션 사용할 때 사용할거라 지금은 일단 주석처리함 -->

...
<beans:bean id="logAop" class="edu.bit.ex.board.aop.LogAop" />
<beans:bean id="logAdvice" class="edu.bit.ex.board.aop.LogAdvice" /> 

<!-- AOP설정 -->
<aop:config>
  <aop:aspect ref="logAdvice">
    <aop:pointcut id="publicM" expression="within(edu.bit.ex.board.service.*)" />
    <aop:before pointcut-ref="publicM" method="printLogging" />
  </aop:aspect>
</aop:config>

<!-- 이 아래 부분도 logAop말고 logAvice만 실행할때는 주석처리할것! -->
<aop:config> 
  <aop:aspect ref="logAop"> 
  <!--aspect id는 logger이고, logAop를 참조함-->
    <aop:pointcut id="publicM" expression="within(edu.bit.ex.board.service.*)" />
    <!-- pointcut(핵심 기능)의 id는 publicM이고, edu.bit.ex.* 패키지에 있는 모든 클래스에 공통 기능을 적용 -->
    <aop:around pointcut-ref="publicM" method="loggerAop" />
    <!--loggerAop()라는 공통 기능을 publicM라는 pointcut에 적용 -->
  </aop:aspect>
</aop:config>

<!--Aspect : 공통기능이 들어 있는 클래스(예제, 로깅... 트랜잭션 .. ) 
    Advice : Aspect 클래스에 들어 있는 공통기능(한마디로 Aspcect 안의 함수) 
    JointPoint : advice 함수가 적용되는 함수 
    PointCut : Jointpoint의 부분으로 실제로 적용되는 함수내의 지점 
    weaving: Advice를 적용하는 행위 -->
```

#### web.xml 설정

- 아래 와 같이 `<param-value>`에 방금 만든 `/WEB-INF/spring/aop-context.xml`를 추가해서 스캔할 수 있도록 하자. -> 따로 설정 할 필요 없어졌고, 원래 대로 사용하면 된다.

  ```xml
  <!-- The definition of the Root Spring Container shared by all Servlets and Filters -->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>
      /WEB-INF/spring/root-context.xml
      <!-- /WEB-INF/spring/aop-context.xml 이것도 지웠다. --> 
    </param-value>
  </context-param>
  ```

- 혹시나 두 개로 설정할 경우 절대 하지 말아햐 할것: 아래처럼 `<param-value>` 자체를 두개로 나눠서 만들면 절대로 안된다!!!

  ```xml
  <param-value>
      /WEB-INF/spring/root-context.xml
  </param-value>
  <param-value>
      /WEB-INF/spring/aop-context.xml
  </param-value>
  ```

#### servlet-context.xml

servlet-context.xml에서 지난시간에 사용한 인터셉터 관련 내용(아래 소스) 지움

```xml
<!-- 주소를 넣어서 객체를 생성 그리고 그것에 대해 설정할 수 잇다. 맵핑 -> list / 왜 갑자기 리스트로 들어가지? 세팅위치가 
  왜 서블렛.xml에 넣음? 왜 루트에도 갈수있는데 왜?  
  컨트롤러 이전에 있는 부분을 서블릿이라 고 표현 (디스패쳐 서블릿 ) 
  그래서 서블릿.엑셈엘 에 데이타소스 커넥션풀 , 디비관련 같이 컨트롤러 뒤에 부분에 있는거는 루트점엑셈엘에 넣는다.-->
  
<!-- 인터셉터 객체 생성 -->
<beans:bean id="boardInterceptor"
  class="edu.bit.ex.board.interceptor.BoardInterceptor">
</beans:bean>

<!-- Interceptor 설정 -->
<interceptors>
  <interceptor>
    <mapping path="/list" /> <!-- http://localhost:8282/ex/list -->
    <exclude-mapping path="/resources/**" />
    <beans:ref bean="boardInterceptor" />
  </interceptor>
</interceptors>
```

이제 돌아가는지 돌려보자.

#### console

```console
[2021-02-15 16:49:52] INFO : edu.bit.ex.board.controller.LoginController - home...
[2021-02-15 16:49:59] INFO : edu.bit.ex.board.controller.LoginController - post login
======== 로그기록=========
```

- 로그인 서비스가 실행되기 전에 함수가 실행되어 다음과 같이 콘솔 기록을 확인할 수 있어야함.
- 아래의 LoginService의 loginUser() 실행 전에 위에가 실행된 것

#### LoginService.java

```java
package edu.bit.ex.board.service;

import org.springframework.stereotype.Service;

import edu.bit.ex.board.mapper.LoginMapper;
import edu.bit.ex.board.vo.UserVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginService {

  LoginMapper loginMapper;

  public UserVO loginUser(String id,String pw)  {
    return loginMapper.logInUser(id,pw) ;
  }

}
```

- (↓ 이걸 저 함수 전에 집어넣겠다는 것!)

  ```java
  package edu.bit.ex.board.aop;

  public class LogAdvice {  //Aspect
    public void printLogging() {  // -> 이게 Advice
      System.out.println("======== 로그기록=========");
    }
  }
  ```

---

더 자세하게 aop를 사용하는 방법을 알아 보자.

---

### 첫 번째 방법: xml에서 설정하는 방법

- xml을 통해서 bean을 설정한다.
- 먼저 공통기능에 들어갈 걸 객체생성시킨다.

#### servlet-context.xml (여기에서 xml 설정함!)

```xml
<bean id="logAdvice" class="edu.bit.ex.board.aop.LogAdvice" />
```

#### 예시 1 ) before 사용 (앞에서 본 예제와 같음, 자세히 설명)

##### 용어 정리가 필요하다.

```xml
 <aop:config> 
       <aop:aspect ref="logAdvice"> 
       <!-- 이걸 적용하겠다. aspect 이걸 공통기능으로 삼겠다. 적용받는 대상은?  -->
         <aop:pointcut id="publicM" expression="within(edu.bit.ex.board.service.*)"/>
         <!--edu.bit.ex.board.service.* 여기 주소에 있는 함수들을 실행하기 전에(아래 before 썼으니까 전!) 
             (저 주소에는 대표적으로 BoradServiceImpl.java가 있음) -> 여기있는것을에 대해서 -->

        <!-- <aop:around pointcut-ref="publicM" method="loggerAop" /> -->
         <aop:before pointcut-ref="publicM" method="printLogging" /> 
         <!-- method="LogAdvice"의 printLogging()메소드를(공통기능) 적용하겠다.는것! -->
              
          <!-- 이거의 아이디를 id="publicM" 로 주고(변수이름) pointcut을 잡아들어감 
              그리고 레퍼런스(ref) 이름을 "publicM"로 주고 거기의 메소드들에 before를 적용하겠다. 
               (pointcut-ref="publicM" : 대상으로 method="printLogging" 적용!) -->
      </aop:aspect>
   </aop:config>
```

- Aspect : 공통기능이 들어 있는 클래스(예제, 로깅... 트랜잭션 .. )
- Advice : Aspect 클래스에 들어 있는 공통기능 (한마디로 Aspcect 안의 함수)
- JointPoint : advice 함수가 적용되는 함수 
  -> 어디에 적용? 보드서비스의 getList();에적용? 아니면 다른함수에도 적용? ..
- PointCut : Jointpoint의 부분으로 실제로 적용되는 함수내의 지점

    ```java
    //before 
    @Override
    public List<BoardVO> getList() {
      //before
      log.info("service_getList-----");
      return mapper.getList();
    }
    //after
    ```

- weaving: Advice를 적용하는 행위

- expression="within(edu.bit.ex.board.service.*) -> 조인포인트를 지정. 조인포인트에 대해 이름을 정해주고(id) 이 두개를 합쳐서 포인트컷이라고함

#### 예시2) around

```xml
<aop:config>
<!-- aspect id는 logger이고, logAop를 참조함 -->  
  <aop:aspect  ref="logAop">
      <!--  pointcut(핵심 기능)의 id는 publicM이고, edu.bit.ex.* 패키지에 있는 모든 클래스에 공통 기능을 적용 -->
      <aop:pointcut id="publicM" expression="within(edu.bit.board.service.*)"/>
      <!--loggerAop()라는 공통 기능을 publicM라는 pointcut에 적용  -->
      <aop:around pointcut-ref="publicM" method="loggerAop" />          
  </aop:aspect>
</aop:config> 
```

#### 예시 2 ) around 사용 : 가장 대표적인 around 예제

- 시간 체크(측정)하는 것! -> 함수가 걸리는 시간! 코드 실행시간을 측정하고 싶다. (실무에서 가끔 쓴다.)
- 어디서 가장 시간이 오래걸리나 체크할 필요가 있어서 각각 함수마다 넣어서 시간재기위해서 사용할 수 잇다.
- aop는 이런데 정말 써먹을만 하다.  

  ```java
  long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
          
  //실험할 코드 추가 -> 테스트할 함수를 여기에 집어넣음 어떻게 함? 그 소스코드가 바로 아래 있는 자바 코드
          
  long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
  long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
  System.out.println("시간차이(m) : "+secDiffTime);
  ```

  -> 출처 : https://hijuworld.tistory.com/2

이제 실습해보자.
  
> edu.bit.ex.board.aop

#### LogAop.java

```java
package edu.bit.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class LogAop {

  public Object loggerAop(ProceedingJoinPoint joinpoint) throws Throwable {
    //조인포인트 - 내부적으로 함수를 실행시킨다.
    String signatureStr = joinpoint.getSignature().toShortString();
    System.out.println( signatureStr + " is start."); // 이게 어디서? loggeraop함수 호출되게 만들어서 이다음에 겟리스트
    
    long st = System.currentTimeMillis(); // 여기서 시간을 재고
    
    try {
      Object obj = joinpoint.proceed(); //해당함수가 넘어온다고 생각 - 이게 핵심포인트 조인포인트가져와서, 이안에 들어가는거 아까 함수들이 이안에서 실행되기 시작함...? // 여기서 겟리스트 호출됨
      return obj;
    } finally {
    
      long et = System.currentTimeMillis();
      
      System.out.println( signatureStr + " is finished.");
      System.out.println( signatureStr + " 경과시간 : " + (et - st)); //걸리는 시간
    } 
  }
}
```

#### servlet-context.xml 에서 설정 추가함

```xml
<bean id="logAop" class="edu.bit.board.aop.LogAop" /> <!-- 객체 생성 -->

<!-- 나머지 설정은 아까랑 거의 똑같음 - around만 다름 -->  

  <!-- aspect id는 logger이고, logAop를 참조함 -->  
<aop:aspect  ref="logAop">
      <!--  pointcut(핵심 기능)의 id는 publicM이고, edu.bit.ex.* 패키지에 있는 모든 클래스에 공통 기능을 적용 -->
      <aop:pointcut id="publicM" expression="within(edu.bit.board.service.*)"/>
      <!--loggerAop()라는 공통 기능을 publicM라는 pointcut에 적용  -->
      <aop:around pointcut-ref="publicM" method="loggerAop" /> 
      <!-- 적용시키는 범위가 around이다. -->
  </aop:aspect>
</aop:config>
```

적용시킬함수는 LogAop의 loggerAop함수.

---

### 두 번째 방법 : annotation 설정하기

- @Aspect annotaion

#### LogAdvice2.java

```java
package edu.bit.board.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAdvice2 {

  @Before("within(edu.bit.board.service.*)")
  public void printLogging()  {
    System.out.println("공통기능2 - 프린트 로그");
  }
}
```

##### @Component

- 컴포넌트 달아주면 컴포넌트 스캔에서 이 객체를 스캔하면서 객체생성한다.

```xml
<context:component-scan base-package="edu.bit.ex.board.service"></context:component-scan>
```

- @Aspect 공통기능
- @Before("within(edu.bit.board.service.*)")
  : 정확하게 위치까지 적어주면 요 서비스에 함수 실행하기 전이다 라고 말하는 것

이 어노테이션을 사용하려면 다음의 한 줄이 더 필요하다.

#### servlet-context.xml(여기에 넣자. 원래 root에 있었음 -> 옮김!)

```xml
 <aop:aspectj-autoproxy></aop:aspectj-autoproxy> 
```

- 얘 자체가 의미하는건 애노테이션을 읽어들이는 객체들이 필요한데 그 때 애노테이션을 읽어들이기 위해서 저걸(autoproxy) 집어 넣어야한다.

나머지 이 아래 내용은 이제 2번 annotation방법에서는 필요 없으므로 과감하게 지우고 해보자.

```xml
<!-- 잠시 주석 -->
<bean id="logAop" class="edu.bit.board.aop.LogAop" />
<bean id="logAdvice" class="edu.bit.ex.board.aop.LogAdvice" />

<!-- AOP설정 -->
<aop:config>
  <aop:aspect ref="logAdvice">
    <aop:pointcut id="publicM"
      expression="within(edu.bit.ex.board.service.*)" />
    <!-- <aop:around pointcut-ref="publicM" method="loggerAop" /> -->
    <aop:before pointcut-ref="publicM" method="printLogging" />
  </aop:aspect>
</aop:config>
<!-- Aspect : 공통기능이 들어 있는 클래스(예제, 로깅... 트랜잭션 .. ) Advice : Aspect 클래스에 들어 
  있는 공통기능(한마디로 Aspcect 안의 함수) JointPoint : advice 함수가 적용되는 함수 PointCut : Jointpoint의 
  부분으로 실제로 적용되는 함수내의 지점 weaving: Advice를 적용하는 행위 -->

<!-- 잠시 주석 -->
<aop:config>
  <!-- aspect id는 logger이고, logAop를 참조함 -->
  <aop:aspect ref="logAop">
    <!-- pointcut(핵심 기능)의 id는 publicM이고, edu.bit.ex.* 패키지에 있는 모든 클래스에 공통 기능을 
      적용 -->
    <aop:pointcut id="publicM"
      expression="within(edu.bit.board.service.*)" />
    <!--loggerAop()라는 공통 기능을 publicM라는 pointcut에 적용 -->
    <aop:around pointcut-ref="publicM" method="loggerAop" />
  </aop:aspect>
</aop:config>
```

예전에는 `<aop:config>`로 xml설정하는 방법을 많이 썼지만 지금은 annotation을 많이 쓰는 추세이다.

`Object obj = joinpoint.proceed();`  
-> 이 코드(핵심 로직)를 기준으로 나머지 부분 넣으면 됨

이제 annotation사용하는 LogAop2를 만들어보자.

> edu.bit.ex.board.aop

#### LogAop2.java

```java
package edu.bit.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAop2 {

  @Around("within(edu.bit.board.service.*)") //포인트컷 설정 추가
  public Object loggerAop(ProceedingJoinPoint joinpoint) throws Throwable {
    String signatureStr = joinpoint.getSignature().toShortString();
    System.out.println( signatureStr + " is start.");
    
    long st = System.currentTimeMillis();
    
    try {
      Object obj = joinpoint.proceed();
      return obj;
    } finally {
    
      long et = System.currentTimeMillis();
      
      System.out.println( signatureStr + " is finished.");
      System.out.println( signatureStr + " 경과시간 : " + (et - st));
    }
  }
}
```

기본 소스코드는 똑같다. 아래 내용만 추가된 것!(annotation) 넣는것만 다름!)

```java
@Component
@Aspect
@Around("within(edu.bit.board.service.*)") 
//포인트컷 설정 추가 -> 경로 확인 주의!!
```

이제 확인해보자. 두 개가 적용되어야함
아래처럼 콘솔로그에서 확인할 수 있으면 된다.

```console
공통기능2 - 프린트 로그
LoginService.loginUser(..) is start.
...
LoginService.loginUser(..) is finished.
LoginService.loginUser(..) 경과시간 : 401
```

이런식으로 나오면 성공

---

## 정리

함수: 핵심 로직  
aop로 공통기능을 핵심 로직에 집어넣음 (aspect를 적용한다고 표현함?)

해당 핵심로직 바로 위(before)나 끝나고(After) 통틀어서(around) 집어넣을 수 있다.
어라운드만 주의하면 된다. (위치확인 잘 해서 넣어야함)

적용하기 위해서는 기본적으로

### 1)pom.xml에 다음 라이브러리 두 개가 필요함

#### pom.xml (설정)

```xml
<!-- AspectJ -->
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjrt</artifactId>
  <version>${org.aspectj-version}</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjweaver</artifactId>
  <version>${org.aspectj-version}</version>
</dependency>
```

#### 2) xml설정 -> aop설정 해야 함
  
- `<aop:aspectj-autoproxy></aop:aspectj-autoproxy>` 혹은 `<aop:config>` 설정
- 두 방법 동시에 사용해도 상관 없다.


#### 3) 그 다음에 Advice 종류 정해야 함

##### 기본적으로 제공하는 Advice 종류

```xml
<aop:before> : 메소드 실행 전에 advice실행
<aop:after-returning> : 정상적으로 메소드 실행 후에 advice실행
<aop:after-throwing> : 메소드 실행중 exception 발생시 advice실행
<aop:after> : 메소드 실행중 exception 이 발생하여도 advice실행
<aop:around> : 메서드 실행 전/후 및 exception 발생시 advice실행
<!--라운드 주의! 중요!-->
```

#### 애노테이션방법 - 쉬운데 잘 안쓰임

- @Aspect를 이용한 AOP구현

작업 순서 (spring_10_1_ex1_springex)

1) 의존 설정(pom.xml)
2) @Aspect 어노테이션을 이용한 Aspect클래스 제작
3) XML파일에 <aop:aspectj-autoproxy /> 설정

#### AspectJ Pointcut 표현식

Pointcut을 지정할 때 사용하는 표현식으로 AspectJ 문법을 사용

@Around("within(edu.bit.board.service.*)")  

- 포인트컷에서 해당함수지정하는방법 이게 조금 까다로울수있다.
- AspectJ에 대한 문법이다.
- 포인트컷 지정에서 힘든부분이 다음 부분! (spring 교재 ppt 11강 3페이지 내용)
  (따로 외울 필요는 없다. 할 때마다 찾아보면 됨)

```plain
* : 모든
. : 현재
.. : 0개 이상

(): 파라미터 없음
(*): 파라미터 있는 모든
*(): 모든 메소드
```

##### Excution

- 더 큰 범위를 지정할 수 있다. 좀 더 자세하게 지정할 때 사용
  예) public void get*(..) -> 모든 겟으로 시작하는 메소드
- com.java.ex.*.*() -> ex패키지에 파라미터가 없는 모든 메소드
- ex..*.*()
- 생각보다 excution잘 안먹힘...

##### within

- 패키지 단위로 지정함

##### bean

- 빈을 다이렉트로 적용하는 것

#### 프록시 패턴

- 참고: https://refactoring.guru/design-patterns/proxy
- 디자인 패턴중에 하나임
- 프록시: 대리

이게 프록시다.

```java
...
public class LogAop2 {
    ...
    long st = System.currentTimeMillis();
}
```

돌아가는 원리는?  
aop 객체를 생성하는건 당연함  
함수하나가진 저건데 저 함수를 프록시라는 객체가 있다. (콘솔보면 중간중간 보임)  
이 프록시로 하여금 aop 객체를 실행시켜서 해당함수를 먼저 실행시키는 것  
중간에 내부에 프록시 객체가 aop 객체를 생성시킨 다음에 객체를 실행시키고 그 다음에 프록시가 리스트를 실행시키고...aop가 중간에 프록시가 있어서 일을 시키는것이다.

#### aop 구현 방법

1) 0101만들 때 : 컴파일시에 넣는 방법  A.java ----(AOP)---> A.class 
2) 실행중에 런타임에 하는 방법 
3) 프록시 객체로 하여금 실행시키는방법 스프링에서는 프록시를 선택함
 (프록시 객체는 눈에는 안보임)

---

#### 오늘숙제

##### emp

- list 들어가면 여기에 이름 나오고($금액 아래부분) $금액 부분에는 부서명(dept에 있음)나오게 만들기(join문 사용 해야 함)
- 마이바티스에서 1:n 처리 방법 (emp-dept 카디널리티 관계 나옴)  
  -> 찾아서 이걸로 처리함  
  (힌트는: 마이바티스에서 키워드: resultMap, collectio / sql문을 어떻게 꾸밀지 알아볼 것)

- /10 -> restful형식으로 부서이름 나오게 할 것  
  - /10 으로 들어가면? ward가 나오고,  
  - /20 들어가면 20번 부서 사원 이름들이 다 나오게 하자.
- 사진도 랜덤처리 할 것

내일부터 스프링시큐리티 함! -> 선행학습 필요함!
