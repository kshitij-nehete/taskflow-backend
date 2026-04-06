package com.taskflow.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing // Hey Spring, autofill data fields for me
public class MongoConfig {
    // No code needed - the annotation does all the work
}
