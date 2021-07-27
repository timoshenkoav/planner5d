package com.tunebrains.planner5dtest.ui

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.tunebrains.planner5dtest.data.Project
import com.tunebrains.planner5dtest.databinding.ViewProjectListBinding

data class ProjectListItem(val name: String, val obj:Project)

fun projectListItemDelegate(clickAction:(Project)->Unit) =
    adapterDelegateViewBinding<ProjectListItem, Any, ViewProjectListBinding>(
        viewBinding = { inflater, parent ->
            ViewProjectListBinding.inflate(inflater, parent, false)
        }) {
        bind {
            binding.name.text = item.name
            binding.root.setOnClickListener {
                clickAction(item.obj)
            }
        }
    }