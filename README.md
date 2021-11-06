# Demo Project with spring boot and jaeger

The purpose of this demo is to give an easy way to show how to send info to the tracing service in jaeger using spring
cloud sleuth and zipkin as helpers.

Also, I tried to explore some performance issues with JPA using criteria API making 3 calls to the API one with N +1
problem, other with memory pagination (collection relationships with max/first result set)
and an optimized way doing fetch with different queries inside the same transaction

Then all of those transactions being traced and register in Jaeger

### Running the project

first, make sure you have Docker then pull Jaeger's all-in-one docker image

to make this easy in the folder `/scripts` there is a `.sh` to make you execute the jaeger all previously configured

you can use shell to run it

```shell
sh ./scripts/docker_jaeger.sh
```

the script inside has the following:

```shell
docker pull jaegertracing/all-in-one
docker run -d --name jaeger \
#this line is important to tell Jaeger to collect all the zipking traces from spring boot
  -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \ 
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 14250:14250 \
  -p 9411:9411 \
  jaegertracing/all-in-one
```

then you can go to `http://localhost:16686` on your browser and see Jaeger UI

this app has actuator with all of its endpoints open way JMX or WEB, so you can see algo with Spring Boot Admin

to run it execute with your IDE of preference or using gradle

```cmd
.\gradlew clean build bootRun
```

* you can check the admin UI over `http://localhost:8080/effective-jpa/admin`
* and swagger (OpenAPI) over `http://localhost:8080/effective-jpa/swagger-ui.html`

#### Reference:

- [Spring Boot + Jaeger](https://ryanharrison.co.uk/2021/08/06/distributed-tracing-spring-boot-jaeger.html)
- [Baeldung: Guide for Spring Boot Admin](https://www.baeldung.com/spring-boot-admin)
- [Baeldung: Jackson Serialization](https://www.baeldung.com/jackson)
- [Vladmihalcea: N+1](https://vladmihalcea.com/n-plus-1-query-problem/) also, thanks to his book!
- [Vladmihalcea: Transactional](https://vladmihalcea.com/read-write-read-only-transaction-routing-spring/)
- [Marco Behler: @Transactional In Depth](https://www.marcobehler.com/guides/spring-transaction-management-transactional-in-depth)
- [Reflectoring: Hexagonal Architecture in spring](https://reflectoring.io/spring-hexagonal/)
- [Reflectoring: Spring Data Specifications](https://reflectoring.io/spring-data-specifications/)
- [Philip Riecks (rieckpil): Testing Spring Boot Apps with TestContainers](https://rieckpil.de/howto-write-spring-boot-integration-tests-with-a-real-database/)

Thanks to the authors of those post to help me understand all of these concepts applied to this project
