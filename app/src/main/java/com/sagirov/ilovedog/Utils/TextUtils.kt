package com.sagirov.ilovedog.Utils

import javax.inject.Inject

class TextUtils @Inject constructor() {

    fun textReduces(text: String): String {
        var reducedText = ""
        reducedText = if (text.length > 18) {
            text.substring(0, 20) + "..."
        } else {
            text
        }
        return reducedText
    }
}