package com.tunebrains.planner5dtest.data

import com.google.gson.Gson
import com.tunebrains.planner5dtest.db.CacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectsRepository(
    private val plannerApi: PlannerApi,
    private val cacheRepository: CacheRepository,
    private val gson: Gson
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    suspend fun list(): List<Project> {
        return withContext(scope.coroutineContext) {
            listOf(
                Project("Desert House", "92704d74d3de7cb2f53256994ac15494"),
                Project("Scandinavian Beach House", "1e15df86b3ca859549cc93f723264a5c"),
                Project(
                    "luxurious and minimalist house ( white & gold )",
                    "2b9b43c47d5c2901e1816dbf03857932"
                ),
                Project(
                    "Dream bathroom: Design battle contest",
                    "956790cecc5f9e99156e8b41d2b54337"
                ),
                Project("The new Tiny House 2021", "8c48def9266782c903b784cfc707ff27"),
            )
        }
    }

    suspend fun info(hash: String): ProjectInfoResponse {
        return withContext(scope.coroutineContext) {
            val fromCache = cacheRepository.load(hash)
            if (fromCache != null) {
                gson.fromJson(fromCache, ProjectInfoResponse::class.java)
            } else {
                val fromApi = plannerApi.getProjectInfo(hash)
                cacheRepository.store(hash, gson.toJson(fromApi))
                fromApi
            }
        }
    }
}