## jpa 在repository或者service中没有添加事务控制


```java
Caused by: javax.persistence.TransactionRequiredException: No EntityManager with actual transaction available for current thread - cannot reliably process 'remove' call
	at org.springframework.orm.jpa.SharedEntityManagerCreator$SharedEntityManagerInvocationHandler.invoke(SharedEntityManagerCreator.java:289)
	at com.sun.proxy.$Proxy96.remove(Unknown Source)
	at org.springframework.data.jpa.repository.query.JpaQueryExecution$DeleteExecution.doExecute(JpaQueryExecution.java:292)
	at org.springframework.data.jpa.repository.query.JpaQueryExecution.execute(JpaQueryExecution.java:91)
	at org.springframework.data.jpa.repository.query.AbstractJpaQuery.doExecute(AbstractJpaQuery.java:136)
	at org.springframework.data.jpa.repository.query.AbstractJpaQuery.execute(AbstractJpaQuery.java:125)
	at org.springframework.data.repository.core.support.RepositoryFactorySupport$QueryExecutorMethodInterceptor.doInvoke(RepositoryFactorySupport.java:590)
	at org.springframework.data.repository.core.support.RepositoryFactorySupport$QueryExecutorMethodInterceptor.invoke(RepositoryFactorySupport.java:578)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:185)
	at org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.invoke(DefaultMethodInvokingMethodInterceptor.java:59)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:185)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:294)
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:98)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:185)
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:139)
	... 65 common frames omitted
2018-09-27 11:06:28.980 INFO  org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext Line:993 - Closing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@38423462: startup date [Thu Sep 27 11:01:44 CST 2018]; root of context hierarchy
2018-09-27 11:06:28.985 INFO  org.springframework.jmx.export.annotation.AnnotationMBeanExporter Line:451 - Unregistering JMX-exposed beans on shutdown
2018-09-27 11:06:28.986 INFO  org.springframework.jmx.export.annotation.AnnotationMBeanExporter Line:185 - Unregistering JMX-exposed beans
2018-09-27 11:06:28.989 INFO  org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean Line:597 - Closing JPA EntityManagerFactory for persistence unit 'default'
2018-09-27 11:06:28.993 INFO  com.zaxxer.hikari.HikariDataSource Line:381 - DatebookHikariCP - Shutdown initiated...
2018-09-27 11:06:29.272 INFO  com.zaxxer.hikari.HikariDataSource Line:383 - DatebookHikariCP - Shutdown completed.

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2018-09-27 11:06:30.154 INFO  com.asiainfo.projectmg.ProjectMgApplication Line:50  - Starting ProjectMgApplication on King-Pan-PC with PID 8612 (/Users/king-pan/git_repository/project-mg/target/classes started by king-pan in /Users/king-pan/git_repository/project-mg)
2018-09-27 11:06:30.155 INFO  com.asiainfo.projectmg.ProjectMgApplication Line:666 - The following profiles are active: dev
2018-09-27 11:06:30.160 INFO  org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext Line:590 - Refreshing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@31bbefcf: startup date [Thu Sep 27 11:06:30 CST 2018]; root of context hierarchy
2018-09-27 11:06:31.888 INFO  org.springframework.context.support.PostProcessorRegistrationDelegate$BeanPostProcessorChecker Line:326 - Bean 'org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration' of type [org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration$$EnhancerBySpringCGLIB$$2b0228a6] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-09-27 11:06:32.211 INFO  org.springframework.boot.web.embedded.tomcat.TomcatWebServer Line:91  - Tomcat initialized with port(s): 8888 (http)
2018-09-27 11:06:32.213 INFO  org.apache.coyote.http11.Http11NioProtocol Line:180 - Initializing ProtocolHandler ["http-nio-8888"]
2018-09-27 11:06:32.213 INFO  org.apache.catalina.core.StandardService Line:180 - Starting service [Tomcat]
2018-09-27 11:06:32.214 INFO  org.apache.catalina.core.StandardEngine Line:180 - Starting Servlet Engine: Apache Tomcat/8.5.32
2018-09-27 11:06:32.237 INFO  org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/api] Line:180 - Initializing Spring embedded WebApplicationContext
2018-09-27 11:06:32.238 INFO  org.springframework.web.context.ContextLoader Line:285 - Root WebApplicationContext: initialization completed in 2078 ms
2018-09-27 11:06:32.274 INFO  org.springframework.boot.web.servlet.ServletRegistrationBean Line:187 - Servlet dispatcherServlet mapped to [/]
2018-09-27 11:06:32.275 INFO  org.springframework.boot.web.servlet.FilterRegistrationBean Line:245 - Mapping filter: 'characterEncodingFilter' to: [/*]
2018-09-27 11:06:32.275 INFO  org.springframework.boot.web.servlet.FilterRegistrationBean Line:245 - Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2018-09-27 11:06:32.276 INFO  org.springframework.boot.web.servlet.FilterRegistrationBean Line:245 - Mapping filter: 'httpPutFormContentFilter' to: [/*]
2018-09-27 11:06:32.276 INFO  org.springframework.boot.web.servlet.FilterRegistrationBean Line:245 - Mapping filter: 'requestContextFilter' to: [/*]
2018-09-27 11:06:32.396 INFO  com.zaxxer.hikari.HikariDataSource Line:110 - DatebookHikariCP - Starting...
2018-09-27 11:06:32.910 INFO  com.zaxxer.hikari.HikariDataSource Line:123 - DatebookHikariCP - Start completed.
2018-09-27 11:06:32.963 INFO  org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean Line:361 - Building JPA container EntityManagerFactory for persistence unit 'default'
2018-09-27 11:06:32.963 INFO  org.hibernate.jpa.internal.util.LogHelper Line:31  - HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
2018-09-27 11:06:33.013 INFO  org.hibernate.dialect.Dialect Line:157 - HHH000400: Using dialect: org.hibernate.dialect.MySQL5InnoDBDialect
2018-09-27 11:06:33.165 INFO  org.hibernate.orm.connections.access Line:47  - HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@115ed56c] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
2018-09-27 11:06:33.566 INFO  org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean Line:415 - Initialized JPA EntityManagerFactory for persistence unit 'default'
2018-09-27 11:06:33.661 INFO  org.hibernate.hql.internal.QueryTranslatorFactoryInitiator Line:47  - HHH000397: Using ASTQueryTranslatorFactory
2018-09-27 11:06:33.671 WARN  org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext Line:558 - Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'demandServiceImpl': Unsatisfied dependency expressed through field 'cardService'; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.asiainfo.projectmg.service.CardService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}
2018-09-27 11:06:33.672 INFO  org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean Line:597 - Closing JPA EntityManagerFactory for persistence unit 'default'
2018-09-27 11:06:33.672 INFO  com.zaxxer.hikari.HikariDataSource Line:381 - DatebookHikariCP - Shutdown initiated...
2018-09-27 11:06:33.904 INFO  com.zaxxer.hikari.HikariDataSource Line:383 - DatebookHikariCP - Shutdown completed.
2018-09-27 11:06:33.905 INFO  org.apache.catalina.core.StandardService Line:180 - Stopping service [Tomcat]
2018-09-27 11:06:33.920 INFO  org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener Line:101 - 
```