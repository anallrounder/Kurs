# 2021.02.16_화_spring security

**결론**: 스프링 시큐리티는 인증과 권한에 대한 프레임워크이다.

## Spring security, Spring Framework & Spring Core Contatiner

- 참고: https://spring.io/
- 코어를 만들어 두고 로드 존슨이 만들어 둔 법칙에 따라서 다른 기능들을 조립해 넣을 수 있게 만들었다.
- 코어에 넣는 것 한 개 한 개가 조립품이기 때문에 이 한 개 한 개의 설정은 다 다르다.  
  -> pom.xml에 설정 다 따로 있는게 그 이유다.  
  예) Log, aop, spring security, mybatis 이런것들도 다 그 설정이 따로있다는 것!
- 그러므로 스프링 시큐리티도, sop도 코어에 끼워넣는 기능 중 하나!
- 또한 하나의 Framework이라고 할 수잇다 (이거의 뜻은? 개발자가 사용하기 편하게하기위해 캡슐화시킨것이다.)
- 그래서 조립이라는 개념을 이해해야한다. 하나씩 집어넣는(끼워넣는)거라는 것.
- [참고 core container](https://gmlwjd9405.github.io/2018/10/26/spring-framework.html)

---

## 설정

### 1. pom.xml 4개의 라이브러리 설정 - 버전주의

(메이븐 레포지토리에서 코어버전과 호환 확인)- 돈주고관리하는데는 버전이 문제가 안되는데 무료를 사용하면 버전을 맞춰도 에러가 나기도 한다.

스프링 시큐리티도 하나의 기능이기 때문에 스프링 코어에 점클래스를 파일을 끌고와야한다.(폼점 엑셈엘에) 그건 4개다.

#### pom.xml

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-core</artifactId>
    <version>${org.security-version}</version>
</dependency>

<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-web</artifactId>
  <version>${org.security-version}</version>
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-config</artifactId>
    <version>${org.security-version}</version>
</dependency>

<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-taglibs</artifactId>
  <version>${org.security-version}</version>
</dependency>
```

이 개념이 소스코드 상에서 어디에 맵핑되는가.

```xml
<properties>
    <java-version>1.8</java-version>
    <org.springframework-version>5.0.7.RELEASE</org.springframework-version> ->스프링코어버전
    <org.aspectj-version>1.6.10</org.aspectj-version>
    <org.slf4j-version>1.6.6</org.slf4j-version>
    <org.security-version>5.0.6.RELEASE</org.security-version>
    ->스프링 코어버전보다 낮아야한다.                             
</properties>
```

- 시큐리티는 인증과 권한을 다룬다. (가장 기본)  
- 즉, 인증과 권한을 컨트롤하기위해서 위의 라이브러리를 가져온다.  
- 이제 인증(Authentication)과 권한(Authorization)에 대한 개념을 알아야한다.

### 2. web.xml 설정 -주의 (필터설정)

- 한글 처리 및에 시큐리 객체 생성 (한글필터-위치도중요)
- contextConfigLocation에 해당 xml 집어 넣음
  (스프링시큐리티필터체인 이름도 바꾸면안된다.이건 한글 필터 아래넣어야한다.)

#### web.xml

```xml
  <!-- 한글 인코딩 필터 -->
  <filter>
    <filter-name>encoding</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
  </filter>

  <filter-mapping>
    <filter-name>encoding</filter-name>
    <servlet-name>appServlet</servlet-name>
  </filter-mapping>

  <!-- Spring Security Filter -->
  <filter>
      <filter-name>springSecurityFilterChain</filter-name>
      <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
  </filter>

  <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
      <url-pattern>/*</url-pattern>
  </filter-mapping>
</web-app>
```

이런게 있다는것만 기억
12개의 필터로 이루어져있다.

#### security-context.xml 만들기 (root-context.xml 복붙)

- 선생님 코드 복붙!

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans:beans
  xmlns="http://www.springframework.org/schema/security"
  xmlns:beans="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd
      http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

  <http>
    <form-login />
  </http>

  <!-- provider -->
  <authentication-manager>

  </authentication-manager>


</beans:beans>
```

#### web.xml (경로 추가)

 `/WEB-INF/spring/appServlet/root-context.xml` 아래에  
 `/WEB-INF/spring/appServlet/security-context.xml` <- 이 경로 추가

```xml
<!-- Processes application requests -->
  <servlet>
    <servlet-name>appServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>
        /WEB-INF/spring/appServlet/servlet-context.xml
        /WEB-INF/spring/appServlet/security-context.xml 
      </param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
```

이제 어떤 일이 일어나는지 일단 돌려 보자.  
일단 홈까지 나오면 된다. 그리고 여기서 /login으로 들어가보자.  
-> 내가 구현하지 않은 로그인 창이 뜬다!

저렇게 로그인창이 뜨는거에 대해서 정리해야할 개념은 무엇일까?
다시한번 정리하면서 알아보자.

```console
[참고] 자식컨테이너를 시작하는 중 오류발생 하면?
이건 예전에 ex로 돌린적이 있어서 그런거라서 clean을 해줘야한다.
```

---

### 가장기본적인 셋팅(설명1)

```xml
<http> 
  <form-login />
</http> 

<!-- provider --> 
<authentication-manager>

</authentication-manager>
```

이 설정을하고보니까,

/login에 대한 응답을 누군가가 해주고있다. 내가 만든 페이지가 아닌데도

  1. /login - 누군하가 로그인 페이지를 응답해주고있다.
  2. /login - 누군가가 처리하고있다.
  3. /login - 누군가가(=스프링 시큐리티가) 낚아 채고 있음 (왜? 내가 컨트롤러에서 처리하고 있지 않으므로)

내가 처리해주지 않는이상 누군가가 낚아채고있다는게 핵심이다.

어디서 낚아채는지 보면 된다. 그냥 ui페이지 찾아보면 알 수 있다.  
(.class 파일도 열어보면 저렇게 코드로 보이네... 몰랐는데..신기...)

### 📚 Maven Dependencies 

#### 🍯 spring-security-web-5.0.6.RELEASE.jar

##### 📦 org.springframework.security.web.authentication.ui

###### 📝 DefaultLoginPageGeneratingFilter.class

```java
/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.web.authentication.ui;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * For internal use with namespace configuration in the case where a user doesn't
 * configure a login page. The configuration code will insert this filter in the chain
 * instead.
 *
 * Will only work if a redirect is used to the login page.
 *
 * @author Luke Taylor
 * @since 2.0
 */
public class DefaultLoginPageGeneratingFilter extends GenericFilterBean {
  public static final String DEFAULT_LOGIN_PAGE_URL = "/login";
  public static final String ERROR_PARAMETER_NAME = "error";
  private String loginPageUrl;
  private String logoutSuccessUrl;
  private String failureUrl;
  private boolean formLoginEnabled;
  private boolean openIdEnabled;
  private boolean oauth2LoginEnabled;
  private String authenticationUrl;
  private String usernameParameter;
  private String passwordParameter;
  private String rememberMeParameter;
  private String openIDauthenticationUrl;
  private String openIDusernameParameter;
  private String openIDrememberMeParameter;
  private Map<String, String> oauth2AuthenticationUrlToClientName;
  private Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs = request -> Collections
    .emptyMap();


  public DefaultLoginPageGeneratingFilter() {
  }

  public DefaultLoginPageGeneratingFilter(AbstractAuthenticationProcessingFilter filter) {
    if (filter instanceof UsernamePasswordAuthenticationFilter) {
      init((UsernamePasswordAuthenticationFilter) filter, null);
    }
    else {
      init(null, filter);
    }
  }

  public DefaultLoginPageGeneratingFilter(
      UsernamePasswordAuthenticationFilter authFilter,
      AbstractAuthenticationProcessingFilter openIDFilter) {
    init(authFilter, openIDFilter);
  }

  private void init(UsernamePasswordAuthenticationFilter authFilter,
      AbstractAuthenticationProcessingFilter openIDFilter) {
    this.loginPageUrl = DEFAULT_LOGIN_PAGE_URL;
    this.logoutSuccessUrl = DEFAULT_LOGIN_PAGE_URL + "?logout";
    this.failureUrl = DEFAULT_LOGIN_PAGE_URL + "?" + ERROR_PARAMETER_NAME;
    if (authFilter != null) {
      formLoginEnabled = true;
      usernameParameter = authFilter.getUsernameParameter();
      passwordParameter = authFilter.getPasswordParameter();

      if (authFilter.getRememberMeServices() instanceof AbstractRememberMeServices) {
        rememberMeParameter = ((AbstractRememberMeServices) authFilter
            .getRememberMeServices()).getParameter();
      }
    }

    if (openIDFilter != null) {
      openIdEnabled = true;
      openIDusernameParameter = "openid_identifier";

      if (openIDFilter.getRememberMeServices() instanceof AbstractRememberMeServices) {
        openIDrememberMeParameter = ((AbstractRememberMeServices) openIDFilter
            .getRememberMeServices()).getParameter();
      }
    }
  }

  /**
    * Sets a Function used to resolve a Map of the hidden inputs where the key is the
    * name of the input and the value is the value of the input. Typically this is used
    * to resolve the CSRF token.
    * @param resolveHiddenInputs the function to resolve the inputs
    */
  public void setResolveHiddenInputs(
    Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs) {
    Assert.notNull(resolveHiddenInputs, "resolveHiddenInputs cannot be null");
    this.resolveHiddenInputs = resolveHiddenInputs;
  }

  public boolean isEnabled() {
    return formLoginEnabled || openIdEnabled || oauth2LoginEnabled;
  }

  public void setLogoutSuccessUrl(String logoutSuccessUrl) {
    this.logoutSuccessUrl = logoutSuccessUrl;
  }

  public String getLoginPageUrl() {
    return loginPageUrl;
  }

  public void setLoginPageUrl(String loginPageUrl) {
    this.loginPageUrl = loginPageUrl;
  }

  public void setFailureUrl(String failureUrl) {
    this.failureUrl = failureUrl;
  }

  public void setFormLoginEnabled(boolean formLoginEnabled) {
    this.formLoginEnabled = formLoginEnabled;
  }

  public void setOpenIdEnabled(boolean openIdEnabled) {
    this.openIdEnabled = openIdEnabled;
  }

  public void setOauth2LoginEnabled(boolean oauth2LoginEnabled) {
    this.oauth2LoginEnabled = oauth2LoginEnabled;
  }

  public void setAuthenticationUrl(String authenticationUrl) {
    this.authenticationUrl = authenticationUrl;
  }

  public void setUsernameParameter(String usernameParameter) {
    this.usernameParameter = usernameParameter;
  }

  public void setPasswordParameter(String passwordParameter) {
    this.passwordParameter = passwordParameter;
  }

  public void setRememberMeParameter(String rememberMeParameter) {
    this.rememberMeParameter = rememberMeParameter;
    this.openIDrememberMeParameter = rememberMeParameter;
  }

  public void setOpenIDauthenticationUrl(String openIDauthenticationUrl) {
    this.openIDauthenticationUrl = openIDauthenticationUrl;
  }

  public void setOpenIDusernameParameter(String openIDusernameParameter) {
    this.openIDusernameParameter = openIDusernameParameter;
  }

  public void setOauth2AuthenticationUrlToClientName(Map<String, String> oauth2AuthenticationUrlToClientName) {
    this.oauth2AuthenticationUrlToClientName = oauth2AuthenticationUrlToClientName;
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    boolean loginError = isErrorPage(request);
    boolean logoutSuccess = isLogoutSuccess(request);
    if (isLoginUrlRequest(request) || loginError || logoutSuccess) {
      String loginPageHtml = generateLoginPageHtml(request, loginError,
          logoutSuccess);
      response.setContentType("text/html;charset=UTF-8");
      response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
      response.getWriter().write(loginPageHtml);

      return;
    }

    chain.doFilter(request, response);
  }

  private String generateLoginPageHtml(HttpServletRequest request, boolean loginError,
      boolean logoutSuccess) {
    String errorMsg = "none";

    if (loginError) {
      HttpSession session = request.getSession(false);

      if (session != null) {
        AuthenticationException ex = (AuthenticationException) session
            .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        errorMsg = ex != null ? ex.getMessage() : "none";
      }
    }

    StringBuilder sb = new StringBuilder();

    sb.append("<html><head><title>Login Page</title></head>");

    if (formLoginEnabled) {
      sb.append("<body onload='document.f.").append(usernameParameter)
          .append(".focus();'>\n");
    }

    if (loginError) {
      sb.append("<p style='color:red;'>Your login attempt was not successful, try again.<br/><br/>Reason: ");
      sb.append(errorMsg);
      sb.append("</p>");
    }

    if (logoutSuccess) {
      sb.append("<p style='color:green;'>You have been logged out</p>");
    }

    if (formLoginEnabled) {
      sb.append("<h3>Login with Username and Password</h3>");
      sb.append("<form name='f' action='").append(request.getContextPath())
          .append(authenticationUrl).append("' method='POST'>\n");
      sb.append("<table>\n");
      sb.append("	<tr><td>User:</td><td><input type='text' name='");
      sb.append(usernameParameter).append("' value='").append("'></td></tr>\n");
      sb.append("	<tr><td>Password:</td><td><input type='password' name='")
          .append(passwordParameter).append("'/></td></tr>\n");

      if (rememberMeParameter != null) {
        sb.append("	<tr><td><input type='checkbox' name='")
            .append(rememberMeParameter)
            .append("'/></td><td>Remember me on this computer.</td></tr>\n");
      }

      sb.append("	<tr><td colspan='2'><input name=\"submit\" type=\"submit\" value=\"Login\"/></td></tr>\n");
      renderHiddenInputs(sb, request);
      sb.append("</table>\n");
      sb.append("</form>");
    }

    if (openIdEnabled) {
      sb.append("<h3>Login with OpenID Identity</h3>");
      sb.append("<form name='oidf' action='").append(request.getContextPath())
          .append(openIDauthenticationUrl).append("' method='POST'>\n");
      sb.append("<table>\n");
      sb.append("	<tr><td>Identity:</td><td><input type='text' size='30' name='");
      sb.append(openIDusernameParameter).append("'/></td></tr>\n");

      if (openIDrememberMeParameter != null) {
        sb.append("	<tr><td><input type='checkbox' name='")
            .append(openIDrememberMeParameter)
            .append("'></td><td>Remember me on this computer.</td></tr>\n");
      }

      sb.append("	<tr><td colspan='2'><input name=\"submit\" type=\"submit\" value=\"Login\"/></td></tr>\n");
      sb.append("</table>\n");
      renderHiddenInputs(sb, request);
      sb.append("</form>");
    }

    if (oauth2LoginEnabled) {
      sb.append("<h3>Login with OAuth 2.0</h3>");
      sb.append("<table>\n");
      for (Map.Entry<String, String> clientAuthenticationUrlToClientName : oauth2AuthenticationUrlToClientName.entrySet()) {
        sb.append(" <tr><td>");
        sb.append("<a href=\"").append(request.getContextPath()).append(clientAuthenticationUrlToClientName.getKey()).append("\">");
        sb.append(HtmlUtils.htmlEscape(clientAuthenticationUrlToClientName.getValue(), "UTF-8"));
        sb.append("</a>");
        sb.append("</td></tr>\n");
      }
      sb.append("</table>\n");
    }

    sb.append("</body></html>");

    return sb.toString();
  }

  private void renderHiddenInputs(StringBuilder sb, HttpServletRequest request) {
    for(Map.Entry<String, String> input : this.resolveHiddenInputs.apply(request).entrySet()) {
      sb.append("	<input name=\"" + input.getKey()
          + "\" type=\"hidden\" value=\"" + input.getValue() + "\" />\n");
    }
  }

  private boolean isLogoutSuccess(HttpServletRequest request) {
    return logoutSuccessUrl != null && matches(request, logoutSuccessUrl);
  }

  private boolean isLoginUrlRequest(HttpServletRequest request) {
    return matches(request, loginPageUrl);
  }

  private boolean isErrorPage(HttpServletRequest request) {
    return matches(request, failureUrl);
  }

  private boolean matches(HttpServletRequest request, String url) {
    if (!"GET".equals(request.getMethod()) || url == null) {
      return false;
    }
    String uri = request.getRequestURI();
    int pathParamIndex = uri.indexOf(';');

    if (pathParamIndex > 0) {
      // strip everything after the first semi-colon
      uri = uri.substring(0, pathParamIndex);
    }

    if (request.getQueryString() != null) {
      uri += "?" + request.getQueryString();
    }

    if ("".equals(request.getContextPath())) {
      return uri.equals(url);
    }

    return uri.equals(request.getContextPath() + url);
  }
  }

```

그리고 또 이곳!

##### 📦 org.springframework.security.web.server.ui

###### 📝 LoginPageGeneratingWebFilter.class

```java
/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.web.server.ui;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * Generates a default log in page used for authenticating users.
 *
 * @author Rob Winch
 * @since 5.0
 */
public class LoginPageGeneratingWebFilter implements WebFilter {
  private ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers
    .pathMatchers(HttpMethod.GET, "/login");

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return this.matcher.matches(exchange)
      .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
      .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
      .flatMap(matchResult -> render(exchange));
  }

  private Mono<Void> render(ServerWebExchange exchange) {
    ServerHttpResponse result = exchange.getResponse();
    result.setStatusCode(HttpStatus.OK);
    result.getHeaders().setContentType(MediaType.TEXT_HTML);
    return result.writeWith(createBuffer(exchange));
  }

  private Mono<DataBuffer> createBuffer(ServerWebExchange exchange) {
    MultiValueMap<String, String> queryParams = exchange.getRequest()
      .getQueryParams();
    Mono<CsrfToken> token = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
    return token
      .map(LoginPageGeneratingWebFilter::csrfToken)
      .defaultIfEmpty("")
      .map(csrfTokenHtmlInput -> {
        boolean isError = queryParams.containsKey("error");
        boolean isLogoutSuccess = queryParams.containsKey("logout");
        byte[] bytes = createPage(isError, isLogoutSuccess, csrfTokenHtmlInput);
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        return bufferFactory.wrap(bytes);
      });
  }

  private static byte[] createPage(boolean isError, boolean isLogoutSuccess, String csrfTokenHtmlInput) {
    String page =  "<!DOCTYPE html>\n"
      + "<html lang=\"en\">\n"
      + "  <head>\n"
      + "    <meta charset=\"utf-8\">\n"
      + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n"
      + "    <meta name=\"description\" content=\"\">\n"
      + "    <meta name=\"author\" content=\"\">\n"
      + "    <title>Please sign in</title>\n"
      + "    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n"
      + "    <link href=\"http://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" crossorigin=\"anonymous\"/>\n"
      + "  </head>\n"
      + "  <body>\n"
      + "     <div class=\"container\">\n"
      + "      <form class=\"form-signin\" method=\"post\" action=\"/login\">\n"
      + "        <h2 class=\"form-signin-heading\">Please sign in</h2>\n"
      + createError(isError)
      + createLogoutSuccess(isLogoutSuccess)
      + "        <p>\n"
      + "          <label for=\"username\" class=\"sr-only\">Username</label>\n"
      + "          <input type=\"text\" id=\"username\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n"
      + "        </p>\n"
      + "        <p>\n"
      + "          <label for=\"password\" class=\"sr-only\">Password</label>\n"
      + "          <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required>\n"
      + "        </p>\n"
      + csrfTokenHtmlInput
      + "        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n"
      + "      </form>\n"
      + "    </div>\n"
      + "  </body>\n"
      + "</html>";

    return page.getBytes(Charset.defaultCharset());
  }

  private static String csrfToken(CsrfToken token) {
    return "          <input type=\"hidden\" name=\"" + token.getParameterName() + "\" value=\"" + token.getToken() + "\">\n";
  }

  private static String createError(boolean isError) {
    return isError ? "<div class=\"alert alert-danger\" role=\"alert\">Invalid credentials</div>" : "";
  }

  private static String createLogoutSuccess(boolean isLogoutSuccess) {
    return isLogoutSuccess ? "<div class=\"alert alert-success\" role=\"alert\">You have been signed out</div>" : "";
  }
}
```

---

### 인증(Authentication)과 권한(Authorization)이란?

#### 인증(Authentication)

- 자신을 증명하는 것: 로그인에서 아이디와 비밀 번호
- 예를들면 직원 ID카드로 회사에 출입할 수 있는 것을 말함

#### 권한(Authorization)

- 권한(인가) - 남에 의한 자격부여 - admin 과 일반user(리소스에 대한 접근 권한이 달라짐)
- 예를들면 이미 회사건물에 들어온 같은 직원이더라도 모바일팀 직원은 반도체실에들어갈 수 없다.(권한이 없다)

##### 정리

- 이렇게 거의 모든 서버는 2중으로 체크하게 되어있다.
- 우리가 알아야할 것은 이 인증과 권한을 어떻게 스프링 시큐리티에서 표현되는지 알아야하는것(어떻게 소스코드를 구성할 것인지)
- 인증과 권한은 소셜로그인(카카오 로그인, 네이버 로그인 등)의 핵심이다.

---

### 스프링 시큐리티 정리 (4개의 라이브러리)

1. 인증(Authentication)과 권한(Authorization)에 대한 프레임워크
2. 암호화(암호학아님주의) - 인코딩과 디코딩
3. CSRF, XSS에대한 개념, 방어요령
4. 스프링 시큐리티 - 세션(Session)객체
5. JSP에서 스프링 시큐리티 태그 쓰는 방법

---

- 오늘 과제 : 어제거 마무리
- 내일 과제 : 카카오 로그인 직접 다시 구현해보기
