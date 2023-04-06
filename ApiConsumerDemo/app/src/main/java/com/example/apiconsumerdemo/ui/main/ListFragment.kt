package com.example.apiconsumerdemo.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apiconsumerdemo.R
import com.example.apiconsumerdemo.databinding.FragmentListBinding
import com.example.apiconsumerdemo.domain.DemoContent
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
internal class ListFragment : Fragment(), ContentListDelegate {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    private lateinit var binding: FragmentListBinding
    private val contentListAdapter by lazy { ContentListAdapter(this, picasso) }
    private val contentPlaceholders
        get() = DemoContent.getPlaceholders(8)

    @Inject
    lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        subscribeFlows()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listRefreshLayout.setOnRefreshListener { viewModel.reloadData() }
        setupListRecycler()
        viewModel.reloadData()
    }

    override fun onListItemPressed(content: DemoContent) {
        parentFragmentManager
            .beginTransaction()
            .addToBackStack(DetailFragment::class.java.name)
            .add(R.id.container, DetailFragment.newInstance(content.id))
            .commit()
    }

    private fun setupListRecycler() {
        binding.listRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contentListAdapter
        }
        contentListAdapter.updateData(contentPlaceholders)
    }

    private fun onDisplayDataUpdate(data: List<DemoContent>) {
        contentListAdapter.updateData(data.ifEmpty { contentPlaceholders }, true)
    }

    private fun onUiStateUpdate(newState: ListUiState) {
        binding.listRefreshLayout.isRefreshing = false
        when (newState) {
            is ListUiState.Loading -> binding.listRefreshLayout.isRefreshing = true
            is ListUiState.Content -> onDisplayDataUpdate(newState.data)
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