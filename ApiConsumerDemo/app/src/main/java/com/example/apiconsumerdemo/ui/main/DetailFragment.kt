package com.example.apiconsumerdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.apiconsumerdemo.databinding.FragmentDetailBinding
import com.example.apiconsumerdemo.domain.DemoContent
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
internal class DetailFragment : Fragment() {
    companion object {
        private const val ARG_ITEM_ID = "ARG_ITEM_ID"
        fun newInstance(id: String) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ITEM_ID, id)
            }
        }
    }

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding

    @Inject
    lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        subscribeFlows()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(ARG_ITEM_ID)?.let {
            viewModel.loadContent(it)
        }
    }

    private fun updateContentDetails(content: DemoContent) {
        binding.detailImage.run {
            picasso
                .load(content.image)
                .noFade()
                .noPlaceholder()
                .centerCrop()
                .fit() //some images are unnecessarily big - performance impact
                .tag(this)
                .into(this)
        }
        binding.detailId.text = content.id
        binding.detailTitle.text = content.title
        binding.detailDescription.text = content.description
    }

    private fun onUiStateUpdate(newState: DetailUiState) {
        binding.detailProgressBar.isVisible = false
        binding.detailErrorMessage.text = ""
        when (newState) {
            is DetailUiState.Loading -> binding.detailProgressBar.isVisible = true
            is DetailUiState.Content -> updateContentDetails(newState.data)
            is DetailUiState.Error -> binding.detailErrorMessage.text = newState.message
        }
    }

    private fun subscribeFlows() {
        lifecycleScope.launchWhenResumed {
            viewModel.uiState.onEach {
                withContext(Dispatchers.Main) {
                    onUiStateUpdate(it)
                }
            }.launchIn(this)
        }
    }

}