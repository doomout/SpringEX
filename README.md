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
```
5. XML 로 SQL 분리
   * MyBatis 를 이용할 때 SQL은 @Select 와 같은 어노테이션으로 사용하기도 한다.
   * 하지만 대부분 SQL은 별도의 파일로 분리하여 사용한다.
   * XML 을 사용하는 이유는 SQL이 길어지거나, 변경되면 프로그램을 수정해야 하기에 따로 파일로 분리한다.
   * XML 과 mapper 인터페이스를 같이 결합할려면 다음과 같은 과정으로 해야 한다.
   * mapper 인터페이스 정의하고 메소드 선언
   * XML 파일 작성(파일명과 mapper 인터페이스 이름 동일하게), select 와 같은 태그 이용해서 SQL 작성
   * select, insert 등의 태그에 id 속성 값을 매퍼 인터페이스의 메소드 이름과 같게 작성