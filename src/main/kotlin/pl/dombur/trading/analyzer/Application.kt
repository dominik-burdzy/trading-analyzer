package pl.dombur.trading.analyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = ["pl.dombur.trading.**"],
    exclude = [DataSourceAutoConfiguration::class],
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
