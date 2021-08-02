package com.tunebrains.planner5dtest

import android.app.Application
import com.tunebrains.planner5dtest.data.PlannerApi
import com.tunebrains.planner5dtest.data.ProjectsRepository
import com.tunebrains.planner5dtest.ui.details.ProjectDetailsDataMapper
import com.tunebrains.planner5dtest.ui.details.ProjectDetailsViewModel
import com.tunebrains.planner5dtest.ui.list.ProjectListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlannerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val api = module {
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
                ProjectsRepository(get())
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