package com.tunebrains.planner5dtest

import android.app.Application
import com.google.gson.Gson
import com.tunebrains.planner5dtest.data.PlannerApi
import com.tunebrains.planner5dtest.data.ProjectsRepository
import com.tunebrains.planner5dtest.db.CacheDb
import com.tunebrains.planner5dtest.db.CacheRepository
import com.tunebrains.planner5dtest.ui.details.ProjectDetailsDataMapper
import com.tunebrains.planner5dtest.ui.details.ProjectDetailsViewModel
import com.tunebrains.planner5dtest.ui.list.ProjectListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PlannerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val api = module {
            single {
                Gson()
            }
            single {
                Retrofit.Builder()
                    .baseUrl("https://planner5d.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            single {
                val retrofit = get<Retrofit>()
                retrofit.create(PlannerApi::class.java)
            }
        }
        val repoModule = module {
            single {
                CacheDb(this@PlannerApp, "cache.db", null)
            }
            single {
                CacheRepository(get(), TimeUnit.HOURS.toMillis(12))
            }
            single {
                ProjectsRepository(get(), get(),get())
            }
        }
        val vmModule = module {
            viewModel {
                ProjectListViewModel(get())
            }
            viewModel {
                ProjectDetailsViewModel(get(), ProjectDetailsDataMapper())
            }
        }
        startKoin {
            modules(api, repoModule, vmModule)
        }
    }
}