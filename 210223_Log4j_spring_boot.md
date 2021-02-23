# 2021.02.23_화_Log4j, Spring Boot

## 남은 일정

- 이번주: 프로젝트 조편성
- 3월2일 ~ 12일까지: 주제 선정 후 DB설계(+화면설계) 완료 & 발표 (최대 17일까지)
- 4월까지: 전체 구현 마무리
- 5월: ppt 발표 준비만 할 것
- 5월 6일: 프로젝트 발표 / 수료

### 추가로 공부할 것

- 스프링 부트
- jsp로 스프링 구현
- Tool 관련 - github / source tree / ...
- 부하테스트 방법

---

## Log4j

**참고** :

- 공식 사이트 : https://logging.apache.org/log4j/2.x/  
- 공식 - [Log4j 2 API](http://logging.apache.org/log4j/log4j-2.12.1/log4j-api/apidocs/index.html)
- 공식 Articles and Tutorials에 소개된 한국인 문서: [Log4j 2 환경설정 [설정 파일 사용 시] (May 14, 2014)](http://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte3:fdl:%EC%84%A4%EC%A0%95_%ED%8C%8C%EC%9D%BC%EC%9D%84_%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94_%EB%B0%A9%EB%B2%95)
- 참고 블로그: https://to-dy.tistory.com/20
  
### logging(로깅) 이란?

1) 시스템을 작동할 때 시스템의 작동 상태의 기록과 보존
2) 이용자의 습성 조사 및 시스템 동작의 분석 등을 하기 위해 작동중의 각종 정보를 기록

이런 기록을 로깅이라고 함

> 디버깅, 시스템 유지관리를 위한 핵심

#### System.out.println()의 단점

- 에러/장애 발생 시 추적할  수 있는 최소한의 정보(특히 시간 같은 것, 패키지 정보 등)가 없다.
- 디버깅을 위한 도구로 활용할 수는 있으나, 로깅을 위한 함수로는 적당하지 않다.
  (기능이 많이 부족하다.)
- 시스템 내에서 부하가 걸린다는 단점이 있다. (은행권에서는 함부로 넣지 않음)
- 로깅메세지에는 시간 및 로깅레벨(info, debug) 패키지까지를 포함하여 나타난다.
패키지명, 함수, 시간등은 나와야 로깅 메세지라고 할 수 있다.
- 단순히 System.out.println()으로 처리시 뿌려야 되는 정보가 많다. 그래서 로깅에 대한 라이브러리를 누군가 이미 만들었음 -> 그것이 **Log4j**

Log4j에 대한 라이브러리가 의외로 복잡하다.

#### pom.xml

```xml
<!-- Logging -->
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
  <version>${org.slf4j-version}</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>jcl-over-slf4j</artifactId>
  <version>${org.slf4j-version}</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>${org.slf4j-version}</version>
  <scope>runtime</scope>
</dependency>

<dependency>
  <groupId>log4j</groupId>
  <artifactId>log4j</artifactId>
  <version>1.2.15</version>
  <exclusions> 
    <!-- 제외할 의존성을 각각 하나의 <exclusion>엘리먼들로 정의함 -->
    <exclusion>
      <groupId>javax.mail</groupId>
      <artifactId>mail</artifactId>
    </exclusion>
    <exclusion>
      <groupId>javax.jms</groupId>
      <artifactId>jms</artifactId>
    </exclusion>
    <exclusion>
      <groupId>com.sun.jdmk</groupId>
      <artifactId>jmxtools</artifactId>
    </exclusion>
    <exclusion>
      <groupId>com.sun.jmx</groupId>
      <artifactId>jmxri</artifactId>
    </exclusion>
  </exclusions>
  <!-- <scope>runtime</scope> -->
</dependency>
```

> src/main/resources

- log4j.xml에서 세팅을 했었다.

#### log4j.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE log4j:configuration SYSTEM "http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/doc-files/log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

   <!-- Appenders -->
   <appender name="console" class="org.apache.log4j.ConsoleAppender"> 콘솔(변수이름) 여기다 뿌려라
      <param name="Target" value="System.out" />
      <layout class="org.apache.log4j.PatternLayout">
         <param name="ConversionPattern" value="[%d{yyyy-MM-dd HH:mm:ss}] %-5p: %c - %m%n" /> 이렇게 뿌린다. 시간 %p(정렬)
      </layout>
   </appender>
   
   <appender name="fileLogger" class="org.apache.log4j.DailyRollingFileAppender">
        <param name="file" value="d://logs//spring//spring.Log"/>
        <param name="Append" value="true"/>
        <param name="dataPattern" value=".yyyy-MM-dd"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="[%d{yyyy-MM-dd HH:mm:ss}] %-5p: %F:%L - %m%n" />
        </layout>
    </appender>
   
   <!-- Application Loggers -->
   <logger name="edu.bit.ex">
      <level value="info" />
   </logger>
   
   <!-- 3rdparty Loggers -->
   <logger name="org.springframework.core">
      <level value="info" />
   </logger>
   
   <logger name="org.springframework.beans">
      <level value="info" />
   </logger>
   
   <logger name="org.springframework.context">
      <level value="info" />
   </logger>

   <logger name="org.springframework.web">
      <level value="info" />
   </logger>

   <!-- Root Logger -->
   <root>
      <priority value="info" />
      <appender-ref ref="console" />
<!--       <appender-ref ref="fileLogger"/> -->
   </root>
   
</log4j:configuration>
```

#### log4jdbc.log4j2.properties

```xml
log4jdbc.spylogdelegator.name=net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator
log4jdbc.dump.sql.maxlinelength=0
```

외울 필요는 없지만 컨트롤 하는 방법은 알아두자.

### LogLevel

- Log4j 2는 다음의 Log Level을 제공함
- info()같이 로깅 메서드를 이용해 로그를 출력할 수 있다.
- 로그레벨의 적용 순서 : FATAL > ERROR > WARN > INFO > DEBUG > TRACE
    예를들어 레벨을 info로 설졍하면 treace, debug레벨은 무시한다. (안나타난다.)  
_  
- com.freedly.sample 하위 패키지에서 로그설정
- additivity 가 false인 경우 상위로거 설정값을 상속받지 않는다.

| Level | Description                                                                                                                                                                    |
| ----- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| FATAL | 아주 심각한 에러가 발생한 상태를 나타냄. 시스템적으로 심각한 문제가 발생해서 어플리케이션 작동이 불가능할 경우가 해당하는데, 일반적으로는 어플리케이션에서는 사용할 일이 없음. |
| ALL   | All levels including custom levels.                                                                                                                                            |
| ERROR | 요청을 처리하는중 문제가 발생한 상태를 나타냄.                                                                                                                                 |
| WARN  | 처리 가능한 문제이지만, 향후 시스템 에러의 원인이 될 수 있는 경고성 메시지를 나타냄.                                                                                           |
| INFO  | 로그인, 상태변경과 같은 정보성 메시지를 나타냄.                                                                                                                                |
| DEBUG | 개발시 디버그 용도로 사용한 메시지를 나타냄.                                                                                                                                   |
| TRACE | 디버그 레벨이 너무 광범위한 것을 해결하기 위해서 좀더 상세한 상태를 나타냄.                                                                                                    |

최종 결정은 여기 루트 로거에서 함

```xml
<!-- Root Logger -->
<root>
  <priority value="info" />
  <appender-ref ref="console" />
<!-- <appender-ref ref="fileLogger"/> --> 
<!--선택하면 지정 경로에 log기록  파일로 저장됨-->
</root>
```

---

| 요소     | 설명                                                                         |
| -------- | ---------------------------------------------------------------------------- |
| Logger   | 출력할 메세지를 Appender에 전달한다.                                         |
| Appender | 전달된 로그를 어디에 출력할 지 결정한다.(콘솔 출력, 파일 기록, 디비 저장 등) |
| Layout   | 로그를 어떤 형식으로 출력할 지 결정한다.                                     |

#### Logger

```zxml
<!-- Application Loggers -->
   <logger name="edu.bit.ex">
      <level value="info" />
   </logger>
```

#### Appenders

Appender는 로그가 출력되는 위치를 나타낸다.

| Appenders           | 태그명          | 출력 위치                                                                        |
| ------------------- | --------------- | -------------------------------------------------------------------------------- |
| ConsoleAppneder     | `<Console>`     | 콘솔에 로그 메시지를 출력                                                        |
| FileAppneder        | `<File>`        | 로그 메시지를 지정된 파일에 기록                                                 |
| RollingFileAppneder | `<RollingFile>` | 파일 크기가 일정 수준 이상이 되면 기존 파일을 백업파일로 두고 처음부터 다시 기록 |
| JDBCAppender        | `<JDBC>`        | 일정 기간 단위로 로그 파일을 생성하고 기록한다.                                  |

예시)

```xml
 <appender name="console" class="org.apache.log4j.ConsoleAppender">
```

  -> 콘솔에 뿌리겠다.

#### Layout

로그를 어떤 형식으로 출력할 지 결정

```xml
value="[%d{yyyy-MM-dd HH:mm:ss}] %-5p: %F:%L - %m%n" />
```

패턴에 레이아웃 지정함

#### PatternLayout의 pattern

  %로 시작하고 %뒤에는 format modifiers와 conversion character로 정의한다.  
  예) %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

| 패턴            | 설명                                                                                           |
| --------------- | ---------------------------------------------------------------------------------------------- |
| c, logger       | 로깅 이벤트를 발생시키기 위해 선택한 로거의 이름을 출력                                        |
| C, class        | 로깅 이벤트가 발생한 클래스의 풀네임명을 출력                                                  |
| M, method       | 로깅 이벤트가 발생한 method명을 출력                                                           |
| F, file         | 로깅 이벤트가 발생한 클래스의 파일명을 출력                                                    |
| l, location     | 로깅 이벤트가 발생한 클래스의 풀네임명.메서드명(파일명:라인번호)를 출력                        |
| d, date         | 로깅 이벤트의 일자와 시간을 출력,\\SimpleDateFormat클래스에 정의된 패턴으로 출력 포맷 지정가능 |
| L, line         | 로깅 이벤트가 발생한 라인 번호를 출력                                                          |
| m, msg, message | 로그문에서 전달된 메시지를 출력                                                                |
| n               | 줄바꿈                                                                                         |
| p, level        | 로깅 이벤트의 레벨을 출력                                                                      |
| r, relative     | 로그 처리시간 (milliseconds)                                                                   |
| t, thread       | 로깅 이벤트가 발생한 스레드명을 출력                                                           |
| %%              | %를 출력하기 위해 사용하는 패턴                                                                |

---

## 실습

설정해서 로그레벨을 조정해보자.

`<!--jdbc 로거-->` : sql문에대한것

#### pom.xml

```xml
<dependency>
  <groupId>org.bgee.log4jdbc-log4j2</groupId>
  <artifactId>log4jdbc-log4j2-jdbc4</artifactId>
  <version>1.16</version>
</dependency>
```

이건 원래 logging쪽에 들어가는게 맞다.

#### root.xml

```xml
<property name="driverClassName" value="net.sf.log4jdbc.sql.jdbcapi.DriverSpy"></property>
```

여기에서 jdbcapi.DriverSpy 설정해야함

```xml
<bean id="hikariConfig" class="com.zaxxer.hikari.HikariConfig">
      <property name="driverClassName"
         value="net.sf.log4jdbc.sql.jdbcapi.DriverSpy"></property>
      <property name="jdbcUrl"
         value="jdbc:log4jdbc:oracle:thin:@localhost:1521:XE"></property>
      <property name="username" value="scott"></property>
      <property name="password" value="tiger"></property>
   </bean>
```

### 설정 추가 

```xml
...
</appender>

<!-- 예를 들어 로깅 레벨 설정을 "INFO"로 하였을 경우 "TRACE", "DEBUG" 레벨은 무시한다. -->

<!-- 출력되는 로그의 양 순서 : ERROR < WARN < INFO < DEBUG < TRACE -->
<!-- com.freedy.sample 하위 패키지에서 로그설정 -->
<!-- additivity가 false인 경우 상위로거의 설정값을 상속받지 않는다. -->

<!--  
- jdbc.sqlonly : SQL문만을 로그로 남기며, PreparedStatement일 경우 관련된 argument 값으로 대체된 SQL문이 보여진다. 
- jdbc.sqltiming : SQL문과 해당 SQL을 실행시키는데 수행된 시간 정보(milliseconds)를 포함한다. 
- jdbc.audit : ResultSet을 제외한 모든 JDBC 호출 정보를 로그로 남긴다. 
- jdbc.resultset : ResultSet을 포함한 모든 JDBC 호출 정보를 로그로 남긴다.
- jdbc.resultsettable : SQL 결과 조회된 데이터의 table을 로그로 남긴다. 
-->

  <!-- SQL Logger -->

<logger name="jdbc.sqltiming" additivity="false">
  <level value="warn" />
  <appender-ref ref="console" />
</logger>

<logger name="jdbc.sqlonly" additivity="false">
  <level value="info" />
  <appender-ref ref="console" />
</logger>

<logger name="jdbc.audit" additivity="false">
  <level value="warn" />
  <appender-ref ref="console" />
</logger>

<logger name="jdbc.resultset" additivity="false">
  <level value="warn" />
  <appender-ref ref="console" />
</logger>

<logger name="jdbc.resultsettable" additivity="false">
  <level value="info" />
  <appender-ref ref="console" />
</logger>

<!-- Root Logger -->
...
```

- https://www.tutorialspoint.com/log4j/log4j_logging_levels.htm

정확히 어떻게 출력되는지 몰라서 어떤식으로 위치를 조정하는건지 모르겠다. 루트에 지정하고 다른것도 다 따로 설정해줘야하는건가;음.. 정확하게 어떤식으로 나오는지 다 써봐야 알거같다. 더 찾아보는걸로...

---
---

## 스프링부트

### 특징1

- 단독으로 실행이 가능한 스프링 애플리케이션을 생성
- 톰캣, Jetty, Undertow를 내장
- 기본설정이 되어있는 starter컴포넌트를 제공
- 가능한 자동으로 설정되어있음
- 상용화에 필요한 통계, 상태 체크, 외부 설정등을 제공
- 설정을 위한 xml코드를 생성하거나 요구하지 않음 (많이 편해진 기능)

///

- 속도도 빠르고 자동적으로 해주는 것이 많기 때문에 요즘 신규 프로젝트는 - 스프링부트를 많이 사용한다.
- 설정등 개선을 하고 코드 양을 줄였다.

---

### 프로젝트 생성

우클릭 > new >  spring starter project 

```java
Name: spring_boot_board  
Type: Maven  
Packaging: War  
Java Version : 8  
Language: Java  
Group: edu.bit  
Artifact: ex  
Package: edu.bit.ex  
```

next >

여기서 라이브러리 설정가능

```java
- Mybatis Framework
- Lombok
 
 위의 두 개는 잘 설정됨 
 나머지는 pom.xml 설정해야함(버전 안맞거나하는 문제가 있다.)

- Oracle Driver
- Spring Boot DevTools
```

next >

Finish >

---

#### pom.xml

직접 설정하지 말라고 했지만 수정해야하는 부분이 있다.
기존하고 조금 다른 라이브러리들이 많이 들어왔다.

web.xml은 없다. 
> src/main/java > edu.bit.ex >
이게 web.xml을 대신하는 클래스이다.

#### ServletInitializer.java

```java
package edu.bit.ex;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(SpringBootBoardApplication.class);
  }

}
```

DispatcherServlet등을 안보이도록 해버렸다. 
하지만 내부 동작은 똑같음

#### SpringBootBoardApplication.java

이제 mian이 보인다. 메인이니까 여기서부터 프로그램이 시작한다!

```java
package edu.bit.ex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootBoardApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootBoardApplication.class, args);
  }

}
```

내부적으로는 이거 하나로 수많은 객체를 생성시킨다.

참고: https://seongmun-hong.github.io/springboot/Spring-boot-EnableAutoConfiguration

> edu.bit.ex

#### HomeController.java

```java
package edu.bit.ex;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//디폴트 세팅이다.
public class HomeController {

  @RequestMapping("/")
  public String home() {
    return "<h1>Hello, Spring Boot!<h1>";
  }
}

```

이제 서버포트 지정하자.
-> 포트 설정을 application.properties <- 여기서 함(공식)

> src/main/resources

#### application.properties

```properties
#server port number
server.port = 8282
```

이제 돌려보는데, Run on Server로 하지 않는다. (내장이 되어있기 때문!)

Run As > String Boot App

설정에 문제가 있어서 실패함...

```consolelog
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.4.3)

2021-02-23 15:07:47.021  INFO 7516 --- [  restartedMain] edu.bit.ex.SpringBootBoardApplication    : Starting SpringBootBoardApplication using Java 15.0.1 on DESKTOP-2BJM56G with PID 7516 (C:\Users\Hyeseon\Documents\workspace-sts-3.9.15.RELEASE\spring_boot_board\target\classes started by Hyeseon in C:\Users\Hyeseon\Documents\workspace-sts-3.9.15.RELEASE\spring_boot_board)
2021-02-23 15:07:47.030  INFO 7516 --- [  restartedMain] edu.bit.ex.SpringBootBoardApplication    : No active profile set, falling back to default profiles: default
2021-02-23 15:07:47.107  INFO 7516 --- [  restartedMain] o.s.b.devtools.restart.ChangeableUrls    : The Class-Path manifest attribute in C:\Users\Hyeseon\.m2\repository\com\oracle\database\jdbc\ojdbc8\19.8.0.0\ojdbc8-19.8.0.0.jar referenced one or more files that do not exist: file:/C:/Users/Hyeseon/.m2/repository/com/oracle/database/jdbc/ojdbc8/19.8.0.0/oraclepki.jar
2021-02-23 15:07:47.108  INFO 7516 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2021-02-23 15:07:47.108  INFO 7516 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2021-02-23 15:07:48.141  WARN 7516 --- [  restartedMain] o.m.s.mapper.ClassPathMapperScanner      : No MyBatis mapper was found in '[edu.bit.ex]' package. Please check your configuration.
2021-02-23 15:07:48.986  INFO 7516 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-02-23 15:07:49.008  INFO 7516 --- [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-02-23 15:07:49.009  INFO 7516 --- [  restartedMain] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.43]
2021-02-23 15:07:49.164  INFO 7516 --- [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-02-23 15:07:49.164  INFO 7516 --- [  restartedMain] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2055 ms
2021-02-23 15:07:49.463  INFO 7516 --- [  restartedMain] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2021-02-23 15:07:49.679  WARN 7516 --- [  restartedMain] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception; nested exception is org.springframework.boot.autoconfigure.jdbc.DataSourceProperties$DataSourceBeanCreationException: Failed to determine a suitable driver class
2021-02-23 15:07:49.681  INFO 7516 --- [  restartedMain] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
2021-02-23 15:07:49.684  INFO 7516 --- [  restartedMain] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2021-02-23 15:07:49.745  INFO 7516 --- [  restartedMain] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2021-02-23 15:07:49.781 ERROR 7516 --- [  restartedMain] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
  If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
  If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).
```

데이터베이스설정을 해야한다.

#### application.properties

포트 설정 아래 부분 내용 추가함

```properties
#server port number
server.port = 8282

#datasource (oracle)
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/xe
#spring.datasource.driver-class-name=net.sf.log4jdbc.sql.jdbcapi.DriverSpy
#spring.datasource.url=jdbc:log4jdbc:oracle:thin:@localhost:1521/xe
spring.datasource.username=scott
spring.datasource.password=tiger
```

#### pom.xml

이 부분 추가

```xml
<repositories>
  <repository>
    <id>oracle</id>
    <url>http://www.datanucleus.org/downloads/maven2/</url>
  </repository>
</repositories>

<dependencies>

  <!-- 오라클 JDBC 드라이버 -->
  <dependency>
    <groupId>oracle</groupId>
    <artifactId>ojdbc6</artifactId>
    <version>11.2.0.3</version>
  </dependency>
```

다시 실행해보자

##### consolelog

```consolelog

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.4.3)

2021-02-23 15:17:55.016  INFO 13140 --- [  restartedMain] edu.bit.ex.SpringBootBoardApplication    : Starting SpringBootBoardApplication using Java 15.0.1 on DESKTOP-2BJM56G with PID 13140 (C:\Users\Hyeseon\Documents\workspace-sts-3.9.15.RELEASE\spring_boot_board\target\classes started by Hyeseon in C:\Users\Hyeseon\Documents\workspace-sts-3.9.15.RELEASE\spring_boot_board)
2021-02-23 15:17:55.022  INFO 13140 --- [  restartedMain] edu.bit.ex.SpringBootBoardApplication    : No active profile set, falling back to default profiles: default
2021-02-23 15:17:55.119  INFO 13140 --- [  restartedMain] o.s.b.devtools.restart.ChangeableUrls    : The Class-Path manifest attribute in C:\Users\Hyeseon\.m2\repository\com\oracle\database\jdbc\ojdbc8\19.8.0.0\ojdbc8-19.8.0.0.jar referenced one or more files that do not exist: file:/C:/Users/Hyeseon/.m2/repository/com/oracle/database/jdbc/ojdbc8/19.8.0.0/oraclepki.jar
2021-02-23 15:17:55.119  INFO 13140 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2021-02-23 15:17:55.120  INFO 13140 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2021-02-23 15:17:56.461  WARN 13140 --- [  restartedMain] o.m.s.mapper.ClassPathMapperScanner      : No MyBatis mapper was found in '[edu.bit.ex]' package. Please check your configuration.
2021-02-23 15:17:57.248  INFO 13140 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8282 (http)
2021-02-23 15:17:57.264  INFO 13140 --- [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-02-23 15:17:57.265  INFO 13140 --- [  restartedMain] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.43]
2021-02-23 15:17:57.405  INFO 13140 --- [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-02-23 15:17:57.406  INFO 13140 --- [  restartedMain] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2284 ms
2021-02-23 15:17:57.748  INFO 13140 --- [  restartedMain] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2021-02-23 15:17:58.374  INFO 13140 --- [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2021-02-23 15:17:58.440  INFO 13140 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8282 (http) with context path ''
2021-02-23 15:17:58.468  INFO 13140 --- [  restartedMain] edu.bit.ex.SpringBootBoardApplication    : Started SpringBootBoardApplication in 4.226 seconds (JVM running for 5.759)

```

http://localhost:8282/ 로 접속해보면


### Hello, Spring Boot!

가 다음과 같이 출력되는것을 확인해볼 수 있다.

<img src="https://github.com/anallrounder/Images/blob/main/%ED%99%94%EB%A9%B4%20%EC%BA%A1%EC%B2%98%202021-02-23%20151912.png?raw=true">

---

### 연습2

project name: spring_boot_hello  
view: heollo world!

설정이 절반이다.  
버전을 잘 맞춰야한다.

---

### 추가설정!!!

jsp사용하기 위해서 다음 라이브러리를 추가한다.

#### pom.xml

```xml
<dependency>
  <groupId>org.apache.tomcat.embed</groupId>
  <artifactId>tomcat-embed-jasper</artifactId>
  <scope>provided</scope>
</dependency>

<!-- jstl 라이브러리 -->
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>jstl</artifactId>
</dependency>
```

---

> src/main/resources > static

- 이미지랑 js 들어감

---

### spirng boot 특징2

- jsp 권장하지 않음
- timeleap권장함 (일반 jsp문법보단 간단하다.)  
  jstl등등은 대부분 자바언어 기반. 자바 탈피하려고한다.  
  (구글의 코틀린 등 지원하기도 함)  
  `template/index.html`와 같이 확장자도 html권장

- 타임리프 문법(앞에 th붙는 등등의 문법)을 권장한다.  
  
  ```html
  <link th:href="@{/css/index.css}" rel="stylesheet" type="text/css">
  ```

- 스프링부트를 쓰더라도 컨트롤러 사용할 수 밖에 없다.
- 참고: https://jongminlee0.github.io/2020/03/12/thymeleaf/

---

### 오늘 과제

스프링부트로  

- project name: spring_boot_board_list  
- 게시판 list.jsp
- 부트스트랩 사용하기
- 타임리프 문법도 써보자

---

## 조별 스터디 [4조]

### mapper경로설정

1) mapper.xml 경로를 properties에 다음과 같이 추가함
  
#### - application.properties 

```xml
mybatis.mapper-locations=/mapper/*.xml
```

2) 메인 함수가 있는 SpringBootBoardListApplication.java에 @MapperScan 어노테이션을 붙이면 @Mapper 어노테이션을 안써도 알아서 스캔함

#### -SpringBootBoardListApplication.java

```java
@MapperScan(basePackageClasses = edu.bit.ex.mapper.BoardMapper.class)
@SpringBootApplication
public class SpringBootBoardListApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootBoardListApplication.class, args);
  }

}
```
