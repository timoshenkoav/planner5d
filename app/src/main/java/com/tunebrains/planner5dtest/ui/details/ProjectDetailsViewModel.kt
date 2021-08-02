package com.tunebrains.planner5dtest.ui.details

import android.graphics.PointF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunebrains.planner5dtest.data.Project
import com.tunebrains.planner5dtest.data.ProjectsRepository
import kotlinx.coroutines.launch

data class FloorData(
    val idx: Int,
    val rooms: List<RoomData>
)

data class WallData(
    val width: Float,
    val hidden: Boolean,
    val points: List<PointF>
)

data class RoomData(
    val x: Float,
    val y: Float,
    val walls: List<WallData>
)

data class ProjectData(
    val name: String,
    val hash: String,
    val width: Float,
    val height: Float,
    val floors: List<FloorData>
)

data class CurrentData(
    val width: Float,
    val height: Float,
    val floor: FloorData
)

sealed class ProjectDetailsUiData {
    object ProjectDetailsLoading : ProjectDetailsUiData()
    object ProjectDetailsNone : ProjectDetailsUiData()
    data class ProjectDetailsSuccess(
        val data: ProjectData
    ) : ProjectDetailsUiData()
}

class ProjectDetailsViewModel(
    private val projectsRepository: ProjectsRepository,
    private val mapper: ProjectDetailsDataMapper
) : ViewModel() {
    private val currentProjectData = MutableLiveData<ProjectDetailsUiData>()
    private val floorData = MutableLiveData<CurrentData>()
    private val nextProjectData = MutableLiveData<ProjectDetailsUiData>()

    fun current(): LiveData<ProjectDetailsUiData> = currentProjectData
    fun next(): LiveData<ProjectDetailsUiData> = nextProjectData
    fun render(): LiveData<CurrentData> = floorData

    fun load(hash: String, list: List<Project>) {
        viewModelScope.launch {
            currentProjectData.value = ProjectDetailsUiData.ProjectDetailsLoading
            nextProjectData.value = ProjectDetailsUiData.ProjectDetailsLoading

            val cur = loadProject(hash)
            currentProjectData.value = uiEvent(cur)

            renderProject(cur)

            val nextProject = findNext(hash, list)
            val next = loadProject(nextProject?.hash)
            nextProjectData.value = uiEvent(next)
        }
    }

    private fun uiEvent(mappedProject: ProjectData?): ProjectDetailsUiData {
        return mappedProject?.let {
            ProjectDetailsUiData.ProjectDetailsSuccess(it)
        } ?: ProjectDetailsUiData.ProjectDetailsNone
    }

    private fun renderProject(mappedProject: ProjectData?) {
        if (mappedProject != null)
            floorData.value =
                CurrentData(mappedProject.width, mappedProject.height, mappedProject.floors.first())
    }

    private suspend fun loadProject(hash: String?): ProjectData? {
        return if (hash == null) {
            null
        } else {
            val data = projectsRepository.info(hash)
            val mappedProject = mapper.map(hash, data)
            mappedProject
        }
    }

    private fun findNext(hash: String, list: List<Project>): Project? {
        val next = list.asReversed().takeWhile { it.hash != hash }.reversed()
        return next.firstOrNull()
    }

}