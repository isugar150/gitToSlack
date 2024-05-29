package com.namejm.gitToSlack.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.namejm.gitToSlack.dto.RestResult
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.text.SimpleDateFormat
import java.util.*


@RestController
class HomeController {

    @Value("\${app.slack.url}")
    lateinit var url: String

    @PostMapping(value = ["/request"])
    fun root(@RequestBody param: Map<String, Any>, request: HttpServletRequest): ResponseEntity<RestResult> {
        val responseData = RestResult()
        val objectMapper = ObjectMapper()

//        val headerSecret = request.getHeader("x-ncp-sourcecommit-signature-v1")

//        println("[Request Secret Header]")
//        println("secret: $secret")
//        println("headerSecret: $headerSecret")

        println("[Request Body]")
        println(param.map { "${it.key}: ${it.value}" }.joinToString(", "))

        val repository = objectMapper.convertValue(param["repository"], object:TypeReference<Map<String, Any>>() {})
//        println("[Request Body - repository]")
//        println(repository.map { "${it.key}: ${it.value}" }.joinToString(", "))

        val sender = objectMapper.convertValue(param["sender"], object:TypeReference<Map<String, Any>>() {})
//        println("[Request Body - sender]")
//        println(sender.map { "${it.key}: ${it.value}" }.joinToString(", "))

        val event = objectMapper.convertValue(param["event"], object:TypeReference<Map<String, Any>>() {})
//        println("[Request Body - event]")
//        println(event.map { "${it.key}: ${it.value}" }.joinToString(", "))

        val eventPayload = objectMapper.convertValue(event["payload"], object:TypeReference<Map<String, Any>>() {})
        println("[Request Body - event - payload]")
        println(eventPayload.map { "${it.key}: ${it.value}" }.joinToString(", "))

        val eventPayloadHeadCommit = objectMapper.convertValue(eventPayload["head_commit"], object:TypeReference<List<Map<String, Any>>>() {})

        val eventPayloadCommits = objectMapper.convertValue(eventPayload["commits"], object:TypeReference<List<Map<String, Any>>>() {})

        val repoName = repository["name"].toString()
        val type = event["type"].toString()
        val branch = eventPayload["ref"].toString().replace("refs/heads/", "")
        val committer = sender["user_id"].toString()
        val time = convertLongToTime(eventPayloadHeadCommit[0]["timestamp"].toString().toLong())
        val commitMessage = eventPayloadHeadCommit[0]["commit_message"].toString()

        val payload = "{\"blocks\":[{\"type\":\"header\",\"text\":{\"type\":\"plain_text\",\"text\":\"${repoName}에 ${type} 이벤트가 발생하였습니다.\",\"emoji\":true}},{\"type\":\"context\",\"elements\":[{\"type\":\"mrkdwn\",\"text\":\"*branch* ${branch}\"},{\"type\":\"mrkdwn\",\"text\":\"*committer* ${committer}\"},{\"type\":\"mrkdwn\",\"text\":\"*time* ${time}\"},{\"type\":\"mrkdwn\",\"text\":\"*message* ${commitMessage}\"}]},{\"type\":\"divider\"}]}"

        val result  = HashMap<String, Any>()
        val factory = HttpComponentsClientHttpRequestFactory()
        factory.setConnectTimeout(5000)

        val restTemplate = RestTemplate(factory)

        val header = HttpHeaders()
        header.contentType= MediaType.parseMediaType("application/json")

        val entity = HttpEntity<String>(payload, header)
        restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)

//        result["statusCode"] = resultMap.getStatusCodeValue()
//        result["header"] = resultMap.getHeaders()
//        resultMap.body?.let { result.put("body", it) }

        responseData.success = true
        return ResponseEntity(responseData, HttpStatus.OK)
    }

    fun convertLongToTime(time: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        return format.format(Date(time * 1000))
    }

}