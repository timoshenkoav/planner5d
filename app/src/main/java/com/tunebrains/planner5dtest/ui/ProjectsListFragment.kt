package com.tunebrains.planner5dtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.tunebrains.planner5dtest.data.Project
import com.tunebrains.planner5dtest.databinding.FragmentProjectsListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProjectsListFragment : Fragment() {

    private val listVm by viewModel<ProjectListViewModel>()

    private var binding: FragmentProjectsListBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentProjectsListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listAdapter = ListDelegationAdapter(
            projectListItemDelegate {
                openProjectDetails(it)
            }
        )
        binding?.apply {
            with(recycler) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listAdapter
            }
        }
        listVm.ui().observe(this as LifecycleOwner) { uiState ->
            binding?.let {
                when (uiState) {
                    is ProjectsListUiState.ProjectsLoading -> {
                        listAdapter.items = emptyList()
                    }
                    is ProjectsListUiState.ProjectsList -> {
                        listAdapter.items = uiState.items.map {
                            ProjectListItem(it.name, it)
                        }
                        listAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        listVm.load()
    }

    private fun openProjectDetails(project: Project) {
        TODO("Not yet implemented")
    }
}