# cafe-2023
Cafe 2023

# Skill
* kotlin
  * coroutine
  * kotest
  * mockk
* spring 6.0
* spring boot 3.0
* spring security

# 작업하면서 배운거
* gradle
  * gradle version constants
    * [StackOverFlow - define variable](https://stackoverflow.com/questions/62959191/how-to-define-a-variable-for-all-gradle-subprojects-using-gradle-kotlin-dsl)
    * [gradle-buildSrc](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources)
* kotlin-coroutine
  * [coroutine guide](https://kotlinlang.org/docs/coroutines-guide.html)
  * [library](https://github.com/Kotlin/kotlinx.coroutines)
  * dependency
    ```
      dependencies {
          implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core: 1.6.4")
          implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor: 1.6.4")
      }
    ```
    * kotlinx-coroutines-core
      * [API](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/)
    * kotlinx-coroutines-reactor
      * [API](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-reactor/)
  * test
    * [kotlin coroutine test](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/)
    * [kotest test coroutine dispatcher](https://kotest.io/docs/framework/coroutines/test-coroutine-dispatcher.html)
    * mockk coroutine
      * https://mockk.io/#coroutines
      * https://notwoods.github.io/mockk-guidebook/docs/mocking/coroutines/
  * spring security
    * [WebFlux Security](https://docs.spring.io/spring-security/reference/reactive/configuration/webflux.html)
    * `@WebFluxTest` 사용시 추가 설정은 `@Import` 등을 사용 - [참조](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/reactive/WebFluxTest.html)
