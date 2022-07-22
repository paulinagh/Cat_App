package com.cat.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cat.R
import com.cat.core.Resource
import com.cat.core.domain.Cat
import com.cat.databinding.ActivityCatDetailBinding
import com.cat.placeImage
import com.cat.viewmodel.CatViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatDetailBinding
    private val catViewModel: CatViewModel by viewModel()
    private lateinit var cat: Cat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCatDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        cat = intent.getParcelableExtra(EXTRA_CAT)!!

        binding.textviewTitle.text = cat.id

        indicateIsFavorite(cat.isFavorite)

        binding.imageviewBack.setOnClickListener { onBackPressed() }
        binding.imageviewFavorite.setOnClickListener {
            val isFavorite = cat.isFavorite
            cat.isFavorite = !isFavorite

            catViewModel.setFavoriteCat(
                cat, cat.isFavorite
            )

            indicateIsFavorite(cat.isFavorite)
        }

        observeCat()
    }

    private fun observeCat() {
        catViewModel.getCat(cat.id).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    isLoadingSign(true)
                }
                is Resource.Success -> {
                    isLoadingSign(false)

                    placeImage(
                        this@CatDetailActivity,
                        resource.data!!.url,
                        binding.imageviewItemImage
                    )

                    binding.textviewLink.text =
                        "Image link: ${resource.data!!.url}"

                    binding.textviewOriginalFileName.text =
                        "Original file name: ${resource.data!!.originalFileName ?: '-'}"

                    binding.textviewCreatedAt.text =
                        "Created at: ${resource.data!!.createdAt ?: "Untold"}"
                }
                is Resource.Error -> {
                    isLoadingSign(false)

                    binding.textviewError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun indicateIsFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.imageviewFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.ic_baseline_star_24
                )
            )
        } else {
            binding.imageviewFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.ic_baseline_star_border_24
                )
            )
        }
    }

    private fun isLoadingSign(isLoading: Boolean) {
        binding.textviewError.visibility = View.GONE

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.imageviewItemImage.visibility = View.INVISIBLE
            binding.textviewLink.visibility = View.INVISIBLE
            binding.textviewOriginalFileName.visibility = View.INVISIBLE
            binding.textviewCreatedAt.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.imageviewItemImage.visibility = View.VISIBLE
            binding.textviewLink.visibility = View.VISIBLE
            binding.textviewOriginalFileName.visibility = View.VISIBLE
            binding.textviewCreatedAt.visibility = View.VISIBLE
        }
    }

    companion object {
        const val EXTRA_CAT = "extraCat"
    }
}