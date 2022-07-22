package com.cat.favorite.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cat.favorite.databinding.ActivityCatFavoriteBinding
import com.cat.activity.CatDetailActivity
import com.cat.adapter.CatAdapter
import com.cat.viewmodel.CatViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules

class CatFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatFavoriteBinding
    private val catViewModel: CatViewModel by viewModel()
    private lateinit var catAdapter: CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCatFavoriteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadKoinModules(viewModelModule)

        binding.imageviewBack.setOnClickListener { onBackPressed() }

        setCatAdapter()
        observeFavoriteCat()
    }

    private fun setCatAdapter() {
        catAdapter = CatAdapter()

        catAdapter.goToCatDetail = {
            startActivity(
                Intent(this@CatFavoriteActivity, CatDetailActivity::class.java).putExtra(
                    CatDetailActivity.EXTRA_CAT,
                    it
                )
            )
        }

        // Save your favorite cat locally
        catAdapter.setFavorite = {
            val isFavorite = it.isFavorite

            catViewModel.setFavoriteCat(it, !isFavorite)
        }
    }

    private fun observeFavoriteCat() {
        catViewModel.favoriteCat.observe(this) { resource ->
            if (resource.isEmpty()) {
                isRecyclerView(true)
            } else {
                isRecyclerView(false)

                catAdapter.submitList(resource)
            }

            with(binding.recyclerviewCat) {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                setHasFixedSize(true)
                adapter = catAdapter
            }
        }

        val recyclerView = binding.recyclerviewCat.layoutManager?.onSaveInstanceState()

        binding.recyclerviewCat.layoutManager?.onRestoreInstanceState(recyclerView)
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
}