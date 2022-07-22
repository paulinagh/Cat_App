package com.cat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cat.R
import com.cat.core.domain.Cat
import com.cat.databinding.ItemCatBinding
import com.cat.placeImage

class CatAdapter : ListAdapter<Cat, CatAdapter.CatViewHolder>(CALLBACK) {
    var goToCatDetail: ((Cat) -> Unit)? = null
    var setFavorite: ((Cat) -> Unit)? = null

    inner class CatViewHolder(private val binding: ItemCatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Cat) {
            placeImage(binding.root, cat.url, binding.imageviewItemImage)

            if (cat.isFavorite) {
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

            binding.imageviewItemImage.setOnClickListener {
                goToCatDetail?.invoke(cat)
            }

            binding.imageviewFavorite.setOnClickListener {
                setFavorite?.invoke(cat)

                absoluteAdapterPosition
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding =
            ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = getItem(position)

        if (cat != null) {
            holder.bind(cat)
        }
    }

    companion object {
        val CALLBACK: DiffUtil.ItemCallback<Cat> =
            object : DiffUtil.ItemCallback<Cat>() {
                override fun areItemsTheSame(oldItem: Cat, newItem: Cat) =
                    oldItem.id == newItem.id

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: Cat, newItem: Cat) =
                    oldItem == newItem
            }
    }
}