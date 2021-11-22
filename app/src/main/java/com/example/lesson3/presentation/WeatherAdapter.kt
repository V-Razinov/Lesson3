package com.example.lesson3.presentation

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.lesson3.databinding.*

class WeatherAdapter : ListAdapter<BaseItemAdapterItem, WeatherAdapter.BaseViewHolder<BaseItemAdapterItem>>(ItemCallback()) {

    companion object {
        const val VIEW_TYPE_TEMPERATURE = 0
        const val VIEW_TYPE_PRESSURE = 1
        const val VIEW_TYPE_HUMIDITY = 2
        const val VIEW_TYPE_WIND = 3
        const val VIEW_TYPE_CLOUDS = 4
        const val VIEW_TYPE_WEATHER = 5
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseItemAdapterItem> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_TEMPERATURE -> TemperatureViewHolder(
                binding = ItemTemperatureBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_PRESSURE -> PressureViewHolder(
                binding = ItemPressureBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_HUMIDITY -> HumidityViewHolder(
                binding = ItemHumidityBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_WIND -> WindViewHolder(
                binding = ItemWindBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_CLOUDS -> CloudsViewHolder(
                binding = ItemCloudsBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_WEATHER -> WeatherViewHolder(
                binding = ItemWeatherBinding.inflate(inflater, parent, false)
            )
            else -> throw NotImplementedError("viewType: $viewType")
        } as BaseViewHolder<BaseItemAdapterItem>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseItemAdapterItem>, position: Int) {
        holder.itemView.isClickable = true
        holder.bind(getItem(position))
    }

    override fun getItemViewType(
        position: Int
    ) = when (getItem(position)) {
        is BaseItemAdapterItem.Temperature -> VIEW_TYPE_TEMPERATURE
        is BaseItemAdapterItem.Pressure -> VIEW_TYPE_PRESSURE
        is BaseItemAdapterItem.Humidity -> VIEW_TYPE_HUMIDITY
        is BaseItemAdapterItem.Wind -> VIEW_TYPE_WIND
        is BaseItemAdapterItem.Clouds -> VIEW_TYPE_CLOUDS
        is BaseItemAdapterItem.Weather -> VIEW_TYPE_WEATHER
    }

    private class ItemCallback : DiffUtil.ItemCallback<BaseItemAdapterItem>() {
        override fun areItemsTheSame(
            oldItem: BaseItemAdapterItem,
            newItem: BaseItemAdapterItem
        ): Boolean {
            if (oldItem is BaseItemAdapterItem.Weather && newItem is BaseItemAdapterItem.Weather)
                return oldItem.id == newItem.id
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BaseItemAdapterItem,
            newItem: BaseItemAdapterItem
        ): Boolean = oldItem == newItem
    }

    abstract class BaseViewHolder<Item : BaseItemAdapterItem>(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(item: Item)
    }

    class TemperatureViewHolder(
        private val binding: ItemTemperatureBinding
    ) : BaseViewHolder<BaseItemAdapterItem.Temperature>(binding.root) {

        override fun bind(item: BaseItemAdapterItem.Temperature) {
            binding.root.text = item.temp
        }
    }

    class PressureViewHolder(
        private val binding: ItemPressureBinding
    ) : BaseViewHolder<BaseItemAdapterItem.Pressure>(binding.root) {
        override fun bind(item: BaseItemAdapterItem.Pressure) {
            binding.root.text = item.value
        }
    }

    class HumidityViewHolder(
        private val binding: ItemHumidityBinding
    ) : BaseViewHolder<BaseItemAdapterItem.Humidity>(binding.root) {
        override fun bind(item: BaseItemAdapterItem.Humidity) {
            binding.root.text = item.value
        }
    }

    class WindViewHolder(
        private val binding: ItemWindBinding
    ) : BaseViewHolder<BaseItemAdapterItem.Wind>(binding.root) {
        override fun bind(item: BaseItemAdapterItem.Wind) {
            binding.speed.text = item.speed
            binding.degree.text = item.degree
        }
    }

    class CloudsViewHolder(
        private val binding: ItemCloudsBinding
    ) : BaseViewHolder<BaseItemAdapterItem.Clouds>(binding.root) {
        override fun bind(item: BaseItemAdapterItem.Clouds) {
            binding.root.text = item.value
        }
    }

    class WeatherViewHolder(
        private val binding: ItemWeatherBinding
    ) : BaseViewHolder<BaseItemAdapterItem.Weather>(binding.root) {

        override fun bind(item: BaseItemAdapterItem.Weather) {
            binding.root.text = item.description
            Glide.with(binding.root)
                .load(item.iconUrl)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                        binding.root.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            resource,
                            null,
                            null,
                            null,
                        )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
    }

}