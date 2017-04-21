import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.OnConsoleStatusListener

statusListener(OnConsoleStatusListener)

def file = "${System.getProperty('log.dir', '')}user-data-%d.%i.log"

appender("FILE", RollingFileAppender) {
    // add a status message regarding the file property
    addInfo("Starting logging to $file")
    append = true
    encoder(PatternLayoutEncoder) { pattern = "%d{HH:mm:ss.SSS} %level %logger - %msg%n" }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "$file"
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) { maxFileSize = "10mb" }}
}

logger("com.repcar", Level.INFO)

logger("org.springframework.cloud.config.client.ConfigServicePropertySourceLocator", Level.WARN)
logger("com.netflix.discovery.shared.resolver.aws.ConfigClusterResolver", Level.WARN)

// Log SQL queries in Hibernate
//logger("org.hibernate.SQL", Level.DEBUG)
// Log parameters in SQL queris in Hibernate
//logger("org.hibernate.type.descriptor.sql", Level.TRACE)

// Log PredictionIO's client messages
//logger("com.ning.http", Level.DEBUG)
//logger("org.jboss.netty", Level.DEBUG)

root(Level.toLevel("${System.getProperty('log.level', 'INFO')}"), ["FILE"])