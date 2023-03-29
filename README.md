#스프링과 스프링 Web MVC  
도서명 - 자바 웹 개발 워크북  
IDE - IntelliJ IDEA 2023.1 (Ultimate Edition)  
자바 버전 - JDK 11, JAVA EE 8  
웹 서버 - 톰캣 9.0.73  
DB - MariaDB 10.5(x64)  
SQL 툴 - HeidiSQL 11.3.0.6295  

1. build.gradle 설정
```
dependencies {
  compileOnly('javax.servlet:javax.servlet-api:4.0.1')

  testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

  // https://mvnrepository.com/artifact/org.springframework/spring-core
  implementation group: 'org.springframework', name: 'spring-core', version: '5.3.19'
  // https://mvnrepository.com/artifact/org.springframework/spring-context
  implementation group: 'org.springframework', name: 'spring-context', version: '5.3.19'
  // https://mvnrepository.com/artifact/org.springframework/spring-test
  testImplementation group: 'org.springframework', name: 'spring-test', version: '5.3.19'

  // https://mvnrepository.com/artifact/org.projectlombok/lombok
  compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
  annotationProcessor 'org.projectlombok:lombok:1.18.24'
  testCompileOnly 'org.projectlombok:lombok:1.18.24'
  testAnnotationProcessor 'org.projectlombok:lombok:1.18.24'

  implementation group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.17.2'
  implementation group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.17.2'
  implementation group: 'org.apache.logging.log4j', name: 'log4j-slf4j-impl', version: '2.17.2'

  implementation group: 'jstl', name: 'jstl', version: '1.2'
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
