package dev.lunaris.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.lunaris.app.data.repository.ProjectRepository

class ProjectViewModelFactory(
    private val repo: ProjectRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProjectViewModel(repo) as T
    }
}
