package com.tunebrains.planner5dtest.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectsRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    suspend fun list():List<Project>{
        return withContext(scope.coroutineContext) {
            listOf(Project("123", "123"))
        }
    }
}