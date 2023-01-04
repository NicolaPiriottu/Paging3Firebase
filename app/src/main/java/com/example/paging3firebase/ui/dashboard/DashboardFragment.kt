package com.example.paging3firebase.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.paging3firebase.databinding.FragmentDashboardBinding
import com.example.paging3firebase.ui.home.HomeViewModel
import com.example.paging3firebase.ui.home.PostAdapter
import com.example.paging3firebase.ui.home.PostModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment(), PostAdapter.Listener {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * ViewModel
     * */
    private val viewModel: DashboardViewModel by viewModels()

    /**
     * Adapter
     */
    private val adapter by lazy {
        PostAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerview.adapter = adapter

        viewModel.getPosts()
        setupObservers()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        // Use Case
        lifecycleScope.launch {
            viewModel.uiState.collect {
                it.getContentIfNotHandled()?.let { useCase ->
                    when (useCase) {
                        is DashboardViewModel.UseCaseLiveData.ShowItems -> {
                            adapter.submitList(useCase.items)
                        }
                        is DashboardViewModel.UseCaseLiveData.ShowTitle -> {

                        }
                    }
                }
            }
        }
    }

    override fun onPostClicked(postModel: PostModel) {
        viewModel.updatePost(postModel)
    }
}