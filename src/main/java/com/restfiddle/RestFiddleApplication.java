/*
 * Copyright 2014 Ranjan Kumar
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
package com.restfiddle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用入口类 main函数
 */

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan({"com.restfiddle"})
@SpringBootApplication
public class RestFiddleApplication {

    public RestFiddleApplication() {
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RestFiddleApplication.class, args);
    }

}

/**
 * * Indicates a {@link org.springframework.context.annotation.Configuration configuration} class that declares one or
 * more
 * {@link org.springframework.context.annotation.Bean @Bean} methods and also triggers {@link
 * org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * auto-configuration} and {@link org.springframework.context.annotation.ComponentScan component scanning}. This is a
 * convenience
 * annotation that is equivalent to declaring {@code @Configuration},
 * {@code @EnableAutoConfiguration} and {@code @ComponentScan}.
 *
 * @author Phillip Webb
 * @return the classes to exclude
 * @Target(ElementType.TYPE)
 * @Retention(RetentionPolicy.RUNTIME)
 * @Documented
 * @Inherited
 * @Configuration
 * @EnableAutoConfiguration
 * @ComponentScan public @interface SpringBootApplication {
 *
 * /**
 * Exclude specific auto-configuration classes such that they will never be applied.
 *
 * Class<?>[] exclude()default {};
 *
 * }
 * @since 1.2.0
 */
