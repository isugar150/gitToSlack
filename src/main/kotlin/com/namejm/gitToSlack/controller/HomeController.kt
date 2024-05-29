package com.namejm.gitToSlack.controller

import com.namejm.gitToSlack.dto.RestResult
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@Controller
class HomeController {

    @Value("\${app.secret}")
    lateinit var secret: String

    @PostMapping(value = ["/request"])
    fun root(@RequestBody param: Map<String, Object>, request: HttpServletRequest): ResponseEntity<RestResult> {
        val result = RestResult()

        val headerSecret = request.getHeader("x-ncp-sourcecommit-signature-v1")

        println("[Request Secret Header]")
        println("secret: $secret")
        println("headerSecret: $headerSecret")

        println("[Request Body]")
        println(param.map { "${it.key}: ${it.value}" }.joinToString(", "))




        result.success = true
        return ResponseEntity(result, HttpStatus.OK)
    }

}