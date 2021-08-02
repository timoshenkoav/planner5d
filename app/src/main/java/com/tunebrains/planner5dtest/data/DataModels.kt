package com.tunebrains.planner5dtest.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Project(val name:String, val hash:String):Parcelable