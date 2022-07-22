package com.cat.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cat.R
import com.cat.adapter.CatAdapter
import com.cat.core.Resource
import com.cat.core.domain.Cat
import com.cat.databinding.ActivityMainBinding
import com.cat.viewmodel.CatViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val catViewModel: CatViewModel by viewModel()
    private lateinit var catAdapter: CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.toolbar.title = ""

        setSupportActionBar(binding.toolbar)
        setCatAdapter()

        binding.edittextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lifecycleScope.launch {
                    catViewModel.queryChannel.send(s.toString())
                }

                observeCat()
            }
        })

        observeCat()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.option_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_favorite -> {
                val uri = Uri.parse("cat://favorite")

                startActivity(Intent(Intent.ACTION_VIEW, uri))

                true
            }
            else -> true
        }

    private fun setCatAdapter() {
        catAdapter = CatAdapter()

        catAdapter.goToCatDetail = {
            startActivity(
                Intent(this@MainActivity, CatDetailActivity::class.java).putExtra(
                    CatDetailActivity.EXTRA_CAT,
                    it
                )
            )
        }

        // Save your favorite cat locally
        catAdapter.setFavorite = {
            val isFavorite = it.isFavorite

            catViewModel.setFavoriteCat(it, !isFavorite)

            val recyclerView = binding.recyclerviewCat.layoutManager?.onSaveInstanceState()

            binding.recyclerviewCat.layoutManager?.onRestoreInstanceState(recyclerView)
        }
    }

    private fun observeCat() {
        if (binding.edittextSearch.text.toString().isEmpty()) {
            catViewModel.catList.observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        isLoadingSign(true)
                    }
                    is Resource.Success -> {
                        isLoadingSign(false)

                        setToRecyclerView(resource.data!!)
                    }
                    is Resource.Error -> {
                        isLoadingSign(false)

                        binding.textviewError.visibility = View.VISIBLE
                    }
                }

                if (binding.edittextSearch.text.toString().isEmpty()) {
                    with(binding.recyclerviewCat) {
                        layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        setHasFixedSize(true)
                        adapter = catAdapter
                    }
                }

                catAdapter.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        } else {
            catViewModel.searchResult.observe(this@MainActivity) { resource ->
                resource.observe(this@MainActivity) { resourceCatList ->
                    setToRecyclerView(resourceCatList)
                }
            }
        }
    }

    private fun setToRecyclerView(catList: List<Cat>) {
        if (catList.isEmpty()) {
            isRecyclerView(true)
        } else {
            isRecyclerView(false)

            catAdapter.submitList(catList)
        }
    }

    private fun isRecyclerView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerviewCat.visibility = View.INVISIBLE
            binding.textviewNotAvailable.visibility = View.VISIBLE
        } else {
            binding.recyclerviewCat.visibility = View.VISIBLE
            binding.textviewNotAvailable.visibility = View.GONE
        }
    }

    private fun isLoadingSign(isLoading: Boolean) {
        binding.textviewError.visibility = View.GONE

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}