package com.tunebrains.planner5dtest.ui.details

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.tunebrains.planner5dtest.R
import com.tunebrains.planner5dtest.data.Project
import com.tunebrains.planner5dtest.databinding.FragmentProjectDetailsBinding
import com.tunebrains.planner5dtest.ui.details.render.RenderData
import kotlinx.android.parcel.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel

@Parcelize
data class DetailsArgs(
    val current: Project,
    val list: List<Project>
) : Parcelable

class ProjectDetailFragment : Fragment() {
    companion object {
        const val ARG_DATA = "arg:data"
    }

    private var binding: FragmentProjectDetailsBinding? = null
    private val vm by viewModel<ProjectDetailsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentProjectDetailsBinding.inflate(layoutInflater, container, false).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailsArgs = requireArguments().getParcelable(ARG_DATA)!!
        binding?.toolbar?.title = args.current.name
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding?.toolbar?.setNavigationIcon(R.drawable.ic_stat_name)
        vm.next().observe(this as LifecycleOwner) { data ->
            when (data) {
                is ProjectDetailsUiData.ProjectDetailsLoading -> {
                    binding?.nextProject?.visibility = View.GONE
                }
                is ProjectDetailsUiData.ProjectDetailsSuccess -> {
                    binding?.nextProject?.let {
                        it.setOnClickListener {
                            vm.load(data.data.hash, args.list)
                        }
                        it.visibility = View.VISIBLE
                    }
                }
                is ProjectDetailsUiData.ProjectDetailsNone -> {
                    binding?.nextProject?.visibility = View.GONE
                }
            }
        }
        vm.current().observe(this as LifecycleOwner) { data ->
            when (data) {
                is ProjectDetailsUiData.ProjectDetailsLoading -> {
                    //display progress
                }
                is ProjectDetailsUiData.ProjectDetailsSuccess -> {
                    binding?.toolbar?.title = data.data.name
                }
                is ProjectDetailsUiData.ProjectDetailsNone -> {
                    //display error
                }
            }
        }
        vm.render().observe(this as LifecycleOwner) { floor ->
            binding?.surface?.update(RenderData(floor.width, floor.height, floor.floor))
        }
        vm.load(args.current.hash, args.list)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}