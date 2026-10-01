package com.example.splitwise.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Split(
    val userId: String = "",
    val userName: String = "",
    val amount: Double = 0.0,
    val share: Double? = null
): Parcelable