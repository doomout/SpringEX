#스프링과 스프링 Web MVC  

도서명 - 자바 웹 개발 워크북  
IDE - IntelliJ IDEA 2023.1 (Ultimate Edition)  
자바 버전 - JDK 11, JAVA EE 8  
웹 서버 - 톰캣 9.0.73  
DB - MariaDB 10.5(x64)  
SQL 툴 - HeidiSQL 11.3.0.6295  
스프링 프레임워크 버전 - 5.3.19

1. build.gradle 설정
```
dependencies {
  compileOnly('javax.servlet:javax.servlet-api:4.0.1')

  testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
  
  //Spring 관련
  implementation group: 'org.springframework', name: 'spring-core', version: '5.3.19'
  implementation group: 'org.springframework', name: 'spring-context', version: '5.3.19'
  testImplementation group: 'org.springframework', name: 'spring-test', version: '5.3.19'
  implementation group: 'org.springframework', name: 'spring-webmvc', version: '5.3.19'
  implementation group: 'org.springframework', name:'spring-jdbc', version: '5.3.19'
  implementation group: 'org.springframework', name:'spring-tx', version: '5.3.19'
  
  //Lombok 관련
  compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
  annotationProcessor 'org.projectlombok:lombok:1.18.24'
  testCompileOnly 'org.projectlombok:lombok:1.18.24'
  testAnnotationProcessor 'org.projectlombok:lombok:1.18.24'
  
  //Log4j2 관련
  implementation group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.17.2'
  implementation group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.17.2'
  implementation group: 'org.apache.logging.log4j', name: 'log4j-slf4j-impl', version: '2.17.2'
  
  //jstl 관련
  implementation group: 'jstl', name: 'jstl', version: '1.2'
  
  //MariaDB 관련
  implementation 'org.mariadb.jdbc:mariadb-java-client:3.0.4'
  implementation group: 'com.zaxxer', name: 'HikariCP', version: '5.0.1'
  
  // MyBatis 관련
  implementation group: 'org.mybatis', name: 'mybatis', version: '3.5.9'
  implementation group: 'org.mybatis', name: 'mybatis-spring', version: '2.0.7'
  
  //DTO 와 VO 변환을 위한 ModelMapper
  implementation group: 'org.modelmapper', name: 'modelmapper', version: '3.0.0'

  //DTO 검증을 위한 라이브러리
  implementation group: 'org.hibernate', name: 'hibernate-validator', version: '6.2.1.Final'
}
```
2. log4j2.xml 설정(보안 이슈 해결 버전인 2.17.0 이상을 권장)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="INFO">
    <Appenders>
        <!--콘솔-->
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout charset="UTF-8" pattern="%d{hh:mm:ss} %5p [%c] %m%n" />
        </Console>
    </Appenders>

    <Loggers>
        <logger name="org.springframework" level="INFO" additivity="false" >
            <Appender-ref ref="console" />
        </logger>
        <logger name="org.zerock" level="INFO" additivity="false" >
            <Appender-ref ref="console" />
        </logger>
        <root level="INFO" additivity="false">
            <AppenderRef ref="console" />
        </root>
    </Loggers>
</Configuration>
```
3. 생성자 주입 방식
    * 주입 받아야 하는 객체의 변수는 final 로 작성
    * 생성자를 이용해서 해당 변수를 생성자의 파라미터로 지정
    * 생성자 주입 방식은 객체 생성시 문제가 발생하는지 미리 확인 가능하여 선호되는 방식
    * Lombok 에서 작성시에는 @RequiredArgsConstructor 를 이용하여 생성자 함수를 자동으로 작성
4. web.xml 설정
```xml
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>WEB-INF/root-context.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
<servlet>
    <servlet-name>appServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/servlet-context.xml</param-value>
    </init-param>
    <init-param>
       <param-name>throwExceptionIfNoHandlerFound</param-name>
       <param-value>true</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>appServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```
5. XML 로 SQL 분리 
   * MyBatis 를 이용할 때 SQL은 @Select 와 같은 어노테이션으로 사용하기도 한다.
   * 하지만 대부분 SQL은 별도의 파일로 분리하여 사용한다.
   * XML 을 사용하는 이유는 SQL이 길어지거나, 변경되면 프로그램을 수정해야 하기에 따로 파일로 분리한다.
   * XML 과 mapper 인터페이스를 같이 결합할려면 다음과 같은 과정으로 해야 한다.
   * mapper 인터페이스 정의하고 메소드 선언
   * XML 파일 작성(파일명과 mapper 인터페이스 이름 동일하게), select 와 같은 태그 이용해서 SQL 작성
   * select, insert 등의 태그에 id 속성 값을 매퍼 인터페이스의 메소드 이름과 같게 작성
6. 스프링 MVC 컨트롤러
   * 상속이나 인터페이스를 구현하는 방식을 사용하지 않고 어노테이션으로만으로 처리 가능
   * 오버라이드 없이 필요한 메소드들을 정의
   * 메소드의 파라미터를 기본 자료형이나 객체 자료형을 마음대로 지정
   * 메소드의 리턴타입도 void, String, 객체 등 다양한 타입을 사용
7. RedirectAttributes 와 Redirect
   * addAttribute(키, 값) : 리다이렉트할 때 쿼리 스트링이 되는 값을 지정
   * addFlashAttribute(키, 값) : 일회용으로만 데이터를 전달하고 삭제되는 값을 지정
8. 스프링 MVC 에서 주로 사용하는 어노테이션들
   * ------컨트롤러------
   * @Controller : 스프링 빈의 처리됨을 명시
   * @RestController : REST 방식의 처리를 위한 컨트롤러임을 명시
   * @RequestMapping : 특정한 URL 패턴에 맞는 컨트롤러인지를 명시
   * ------메소드---------
   * @GetMapping/@PostMapping/@DeleteMapping/@PutMapping : HTTP 전송 방식에 따라 사용
   * @RequestMapping : GET/POST 방식 모두를 지원하는 경우 사용
   * @ResponseBody : REST 방식에서 사용
   * ------메소드의 파라미터--------
   * @RequestParam : Request에 있는 특정한 이름의 데이터를 파라미터로 받아서 처리 하는 경우
   * @PathVariable : URL 경로의 일부를 변수로 삼아서 처리하기 위해 사용
   * @ModelAttribute : 반드시 Model에 포함되어서 다시 뷰로 전달됨을 명시
   