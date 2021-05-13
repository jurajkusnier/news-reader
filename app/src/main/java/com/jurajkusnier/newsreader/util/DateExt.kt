package com.jurajkusnier.newsreader.util

import android.content.Context
import java.util.*

fun Date.toUIFormat(context: Context): String =
    android.text.format.DateFormat.getDateFormat(context).format(this)