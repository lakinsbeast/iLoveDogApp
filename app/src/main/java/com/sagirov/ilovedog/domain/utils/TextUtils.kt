package com.sagirov.ilovedog.domain.utils

import javax.inject.Inject

class TextUtils @Inject constructor() {

    fun textReduces(text: String): String {
        var reducedText = ""
        reducedText = if (text.length > 18) {
            text.substring(0, 5) + "..."
        } else {
            text
        }
        return reducedText
    }
}