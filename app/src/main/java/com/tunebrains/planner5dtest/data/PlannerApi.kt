package com.tunebrains.planner5dtest.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

data class ProjectPointsDto(
    @SerializedName("x")
    val x:Float,
    @SerializedName("y")
    val y:Float
)
data class ProjectWallDto(
    @SerializedName("className")
    val className:String,
    @SerializedName("w")
    val width: Float,
    @SerializedName("hidden")
    val hidden:Boolean,
    @SerializedName("items")
    val points:List<ProjectPointsDto>
)
data class ProjectRoomDto(
    @SerializedName("className")
    val className:String,
    @SerializedName("x")
    val x:Float,
    @SerializedName("y")
    val y:Float,
    @SerializedName("items")
    val walls:List<ProjectWallDto>
)
data class ProjectFloorDto(
    @SerializedName("className")
    val className:String,
    @SerializedName("items")
    val rooms:List<ProjectRoomDto>?
)
data class ProjectDataDto(
    @SerializedName("width")
    val width:Float,
    @SerializedName("height")
    val height:Float,
    @SerializedName("className")
    val className:String,
    @SerializedName("items")
    val floors:List<ProjectFloorDto>?

)
data class ProjectInfoDto(
    @SerializedName("name")
    val name:String,
    @SerializedName("data")
    val data:ProjectDataDto

)
data class ProjectInfoResponse(
    @SerializedName("items")
    val items:List<ProjectInfoDto>
)
interface PlannerApi {
    @GET("/api/project/{hash}")
    suspend fun getProjectInfo(
        @Path("hash") hash:String
    ): ProjectInfoResponse
}