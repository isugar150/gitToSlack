package com.namejm.gitToSlack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GitToSlackApplication

fun main(args: Array<String>) {
    runApplication<GitToSlackApplication>(*args)
}
