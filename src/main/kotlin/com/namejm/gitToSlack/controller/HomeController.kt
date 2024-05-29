package com.namejm.gitToSlack.controller

import com.namejm.gitToSlack.dto.RestResult
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class HomeController {

    @GetMapping(value = ["/request"])
    fun root(request: HttpServletRequest): ResponseEntity<RestResult> {
        val result = RestResult()

        result.success = true
        return ResponseEntity(result, HttpStatus.OK)
    }

}