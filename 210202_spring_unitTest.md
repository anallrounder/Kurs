# 2021.02.02 화 테스트 (V model) 

### V model

각각의 단계마다 테스트가 들어간다.

<img src="https://github.com/anallrounder/Images/blob/main/v_model.png?raw=true">

- 참고 1: https://m.blog.naver.com/PostView.nhn?blogId=4rangi&logNo=70102856109&proxyReferer=https:%2F%2Fwww.google.com%2F  

- 참고2: https://m.blog.naver.com/PostView.nhn?blogId=xcripts&logNo=70120808518&proxyReferer=https:%2F%2Fwww.google.com%2F

### 테스트의 중요성

소프트웨어의 품질을 좌우한다.

---

### 단위 테스트

TDD, 애자일, ...  

설계 : 머리속으로 생각하는게 아니고 계획을 문서로 남기는 것이다.  

(받은 소스코드에 선생님 예제 파일 있음. 확인해볼수있다.)


함수(혹은 클래스) 만들고 바로 테스트 하는것. 자바에서는 JUnit을 통해서 함수 단위로 테스트를 한다. -> 이것을 단위 테스트라고 한다. 

- jUnit 사용하는 자세한 방법은 여기에 설명 :  
https://zimt.tk/STS-JUnit-539b76cbc8904101bcf317ffddc43f41


- pom.xml 설정

```xml
	<!-- Test -->
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
			<scope>test</scope>
		</dependency>
```

<br>

> src/main/java >  edu.bit.ex.calculator

- Calculator.java 

```java
package edu.bit.ex.calculator;

public class Calculator {
	
	public int sum(int num1, int num2) {
		return num1 + num2;
	}
}
```

> src/test/java > edu.bit.ex.calculator

- CalculatorTest.java

```java
package edu.bit.ex.calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculatorTest {

	@Test
	public void testSum() {	//sum함수에 대해서 테스트를 한다는 의미.
		Calculator cal = new Calculator();
		int result = cal.sum(10, 20);
		assertEquals(20,result,10);
	}
}
```

에릭 감마- 디자인 패턴 (중요...)   
스프링을 짠 기본원리도 디자인 패턴이다.  
(문서에 test driven 형태로 개발한다는 것, 실제 테스트한 창을 꼭 넣어야한다.)



#### 반드시 테스트해야하는 부분

1. 커넥션풀 (데이터소스 부분) - 당연
2. 맵퍼, 서비스, 컨트롤러(까다롭다.- 테스트환경에는 디스패쳐서블릿이 없기 때문이다. mock 를 사용한다.)  
3. ...

설계를 이렇게 했고 그래서 이런 테스트를 했다는 것을 말하는게 중요. 그냥 테스트를 보여주는거는 의미 없다.  

참고: https://epthffh.tistory.com/entry/Junit%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%8B%A8%EC%9C%84%ED%85%8C%EC%8A%A4%ED%8A%B8

---


