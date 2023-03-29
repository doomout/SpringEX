스프링과 스프링 Web MVC
도서명 - 자바 웹 개발 워크북
IDE - IntelliJ IDEA 2023.1 (Ultimate Edition)
자바 버전 - JDK 11
웹 서버 - 톰캣 9.0.73
DB - MariaDB 10.5(x64)
SQL 툴 - HeidiSQL 11.3.0.6295

JDBC 프로그래밍을 위한 API
java.sql.connection : db와 네트워크상의 연결,작업 후에는 반드시 close()로 연결 종료 해야 한다.
java.sql.Statement/PreparedStatement : SQL을 db로 보내기
Statement : SQL 내부에 모든 데이터를 같이 전송하는 방식
PreparedStatement : SQL을 먼저 전달 후 데이터 전송하는 방식
executeUpdate() : DML 실행하과 결과를 int 타입으로 반환한다.
executeQuery() : 쿼리를 실행할 때 사용, ResultSet 타입으로 리턴 받는다.
java.sql.ResultSet : 네트워크를 통해 데이터를 읽어들이기에 작업후 close()로 연결 종료
Connection pool : 미리 Connection 객체를 생성해서 보관하고 필요할 때마 쓰는 방식
javax.sql.DataSource : Connection pool을 자바에서 API 형태로 지원
DAO(Data Access Object) : 데이터를 전문으로 처리하는 객체
VO(Value Object) : 데이터를 객체 단위로 처리
build.gradle 설정
dependencies {
    compileOnly('javax.servlet:javax.servlet-api:4.0.1')

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    // https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
    implementation group: 'org.mariadb.jdbc', name: 'mariadb-java-client', version: '3.0.4'

    //Lombok 라이브러리 추가
    compileOnly 'org.projectlombok:lombok:1.18.26'
    annotationProcessor 'org.projectlombok:lombok:1.18.26'

    testCompileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
    testAnnotationProcessor group: 'org.projectlombok', name: 'lombok', version:'1.18.24'

    implementation group: 'com.zaxxer', name: 'HikariCP', version: '5.0.0'

    // https://mvnrepository.com/artifact/org.modelmapper/modelmapper
    implementation group: 'org.modelmapper', name: 'modelmapper', version: '3.0.0'

    implementation group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.17.2'
    implementation group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.17.2'
    implementation group: 'org.apache.logging.log4j', name: 'log4j-slf4j-impl', version: '2.17.2'

    implementation group: 'jstl', name: 'jstl', version: '1.2'
}
}
Lombok 의 @Cleanup 을 사용하면 try-catch 문을 생략하고, close()가 호출 되는 것을 보장한다.
public String getTime2() throws Exception {
    @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
    @Cleanup PreparedStatement preparedStatement = connection.prepareStatement("select now()");
    @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

    resultSet.next();

    String now = resultSet.getString(1);

    return now;
}
log4j2.xml 설정(보안 이슈 해결 버전인 2.17.0 이상을 권장)
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration xmlns="http://logging.apache.org/log4j/2.0/config" status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
