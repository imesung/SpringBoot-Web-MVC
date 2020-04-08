## 스프링 부트 JSP

스프링 부트에서 JSP를 사용하기 위해 필요한 depedency

~~~xml
<dependency>
<groupId>javax.servlet</groupId>
<artifactId>jstl</artifactId>
</dependency>
<dependency>
<groupId>org.apache.tomcat.embed</groupId>
<artifactId>tomcat-embed-jasper</artifactId>
<scope>provided</scope>
</dependency>
~~~



**war 파일**

- 프로젝트 생성 시 패키징을 jar가 아닌 war로 선택할 시 war 파일을 톰캣에 올려 웹 서버에 배포도 가능하다.
- 또한 실행 클래스(DemoApplication) 뿐만 아니라 ServletInitializer 클래스도 생성된다.
  - DemoApplicatoin : 독립적인 war 파일 실행 시 해당 클래스에서 실행된다.
  - ServletInitializer : 서블릿 엔진(톰켓)에서 war 파일을 실행 시 해당 클래스에서 실행된다. 
  - SpringBootServletInitializer -> implements WebApplicationInitializer

~~~java
//톰캣에 올려 
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DemoJspApplication.class);
	}

}
~~~



**스프링 부트에서 JSP**

- 스프링 부트에서는 JSP를 사용하는 것을 추천하지 않는다. 이유는,
  - JAR 프로젝트를 만들 수 없고, WAR 프로젝트로 만들어야 한다.
  - 언더토우(JBoss에서 만든 서블릿 컨테이너)는 JSP를 지원하지 않는다.
  - 가장 큰 이유는 스프링 부트에서 추천하지 않는다는 것이다.