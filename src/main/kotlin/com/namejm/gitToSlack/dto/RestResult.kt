package com.namejm.gitToSlack.dto

import com.fasterxml.jackson.databind.ObjectMapper


data class RestResult(
    var success: Boolean = false,
    var message: String = "",
    var data: HashMap<String, Any>? = null,
//    var list: List<Any>? = null
) {

    fun setData(param: Any?) {
        this.data = ObjectMapper().convertValue(param, HashMap::class.java) as HashMap<String, Any>
    }

    fun setData(name: String?, param: Any?) {
        if (data == null) {
            data = HashMap()
        }
        data!![name!!] = param!!
    }

}