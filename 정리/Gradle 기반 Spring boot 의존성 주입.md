### Gradle 기반 SpringBoot

**build.gradle 세팅**

- Web, thymeleaf, lombok 의존성 추가

~~~html
...

dependencies {
    compile('org.springframework.boot:spring-boot-starter-web')
    compile('org.springframework.boot:spring-boot-starter-thymeleaf')
    testCompile('org.springframework.boot:spring-boot-starter-test')
    compileOnly('org.projectlombok:lombok')
    annotationProcessor('org.projectlombok:lombok')

}
~~~

- lombok 사용 시 command + ,로 preferences 접근 > Plugins > 'lombok' 검색 후 설치
- 설치 후 command + ,로 preferences 접근 > Compiler > Java Compiler > Enable Annotation Processing 체크

