package com.jurajkusnier.newsreader.util

import android.content.Context
import java.util.Date

fun Date.toUIFormat(context: Context): String =
    android.text.format.DateFormat.getDateFormat(context).format(this)

fun Date.toMediumUIFormat(context: Context): String =
    android.text.format.DateFormat.getMediumDateFormat(context).format(this)
