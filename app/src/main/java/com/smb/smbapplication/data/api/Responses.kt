package com.smb.smbapplication.data.api

import com.smb.smbapplication.utils.logger.Log
import java.util.regex.Pattern

object Responses {

    class ApiEmptyResponse<T> : ApiResponse<T>()

    data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

    data class ApiSuccessResponse<T>(
        val body: T,
        val links: Map<String, String>
    ) : ApiResponse<T>() {
        constructor(body: T, linkHeader: String?) : this(
            body = body,
            links = linkHeader?.extractLinks() ?: emptyMap()
        )

        val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
            links[NEXT_LINK]?.let { next ->
                val matcher = PAGE_PATTERN.matcher(next)
                if (!matcher.find() || matcher.groupCount() != 1) {
                    null
                } else {
                    try {
                        Integer.parseInt(matcher.group(1))
                    } catch (ex: NumberFormatException) {
                        Log.w("cannot parse next page from %s", next)
                        null
                    }
                }
            }
        }

        companion object {
            private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
            private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
            private const val NEXT_LINK = "next"

            private fun String.extractLinks(): Map<String, String> {
                val links = mutableMapOf<String, String>()
                val matcher = LINK_PATTERN.matcher(this)

                while (matcher.find()) {
                    val count = matcher.groupCount()
                    if (count == 2) {
                        links[matcher.group(2)] = matcher.group(1)
                    }
                }
                return links
            }

        }
    }

}