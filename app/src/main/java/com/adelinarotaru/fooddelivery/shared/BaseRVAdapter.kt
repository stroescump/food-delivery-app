package com.adelinarotaru.fooddelivery.shared

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

interface ItemAdapter {
    val id: Int
}

abstract class BaseRVAdapter<T : ViewBinding, R : ItemAdapter>() :
    RecyclerView.Adapter<BaseRVAdapter<T, R>.BaseVH>() {

    protected abstract val refreshUi: (T, R) -> Unit
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T

    inner class BaseVH(private val binding: T) :
        ViewHolder(binding.root) {
        fun setData(payload: R) = refreshUi(binding, payload)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH =
        BaseVH(bindingInflater(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        val currentItem = differ.currentList[position]
        holder.setData(currentItem)
    }

    private val differCallback = object : DiffUtil.ItemCallback<R>() {
        override fun areItemsTheSame(oldItem: R, newItem: R): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: R, newItem: R
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}