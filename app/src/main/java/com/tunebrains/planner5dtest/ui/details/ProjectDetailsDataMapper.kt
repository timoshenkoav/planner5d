package com.tunebrains.planner5dtest.ui.details

import android.graphics.PointF
import com.tunebrains.planner5dtest.data.ProjectInfoResponse

class ProjectDetailsDataMapper {
    fun map(hash:String, data: ProjectInfoResponse): ProjectData {
        val first = data.items.first()
        return ProjectData(
            first.name,
            hash,
            first.data.width,
            first.data.height,
            first.data.floors?.mapIndexed { index, projectFloorDto ->
                FloorData(
                    index,
                    projectFloorDto.rooms?.filter { it.className == "Room" }?.map { room ->
                        RoomData(
                            room.x,
                            room.y,
                            room.walls.filter { it.className == "Wall" && !it.hidden }.map { wall ->
                                WallData(wall.width, wall.points.map { point ->
                                    PointF(point.x, point.y)
                                })
                            }
                        )
                    } ?: emptyList()
                )
            } ?: emptyList())
    }
}