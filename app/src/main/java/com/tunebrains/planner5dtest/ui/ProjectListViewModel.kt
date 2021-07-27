package com.tunebrains.planner5dtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunebrains.planner5dtest.data.Project
import com.tunebrains.planner5dtest.data.ProjectsRepository
import kotlinx.coroutines.launch

sealed class ProjectsListUiState {
    object ProjectsLoading : ProjectsListUiState()
    data class ProjectsList(val items: List<Project>) : ProjectsListUiState()
}

class ProjectListViewModel(
    private val repo: ProjectsRepository
) : ViewModel() {
    private val uiData = MutableLiveData<ProjectsListUiState>()

    fun ui(): LiveData<ProjectsListUiState> = uiData

    fun load() {
        viewModelScope.launch {
            uiData.value = ProjectsListUiState.ProjectsLoading
            uiData.value = ProjectsListUiState.ProjectsList(repo.list())
        }
    }
}