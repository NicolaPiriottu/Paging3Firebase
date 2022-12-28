package com.example.paging3firebase.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.paging3firebase.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), PostAdapter.Listener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * ViewModel
     * */
    private val viewModel: HomeViewModel by viewModels()

    /**
     * Adapter
     */
    private val adapter by lazy {
        PostAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getItems()

        binding.recyclerview.adapter = adapter
        setupObservers()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        // Use Case
        viewModel.useCaseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { useCase ->
                when (useCase) {
                    is HomeViewModel.UseCaseLiveData.ShowItems -> {
                        adapter.submitList(useCase.items)
                    }
                }
            }
        }
    }

    override fun onPostClicked(postModel: PostModel) {
        viewModel.updatePost(postModel)
    }
}