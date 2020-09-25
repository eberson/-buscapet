package br.com.etecmatao.buscapet.adapter

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.fragments.HomeFragmentDirections
import br.com.etecmatao.buscapet.model.AdType
import br.com.etecmatao.buscapet.model.Advertisement
import kotlinx.android.synthetic.main.ad_item_layout.view.*
import java.util.*


class AdvertisementAdapter internal constructor(context: Context): RecyclerView.Adapter<AdvertisementAdapter.AdvertisementViewHolder>() {

    private val dateFormat = DateFormat.getDateFormat(context)
    private val inflater = LayoutInflater.from(context)
    private val items: MutableList<Advertisement> = mutableListOf()

    inner class AdvertisementViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            val item = items[position]

            itemView.txtAdTitle.text = item.title
            itemView.txtAdDescription.text = item.description
            itemView.txtDate.text = dateFormat.format(item.date ?: Date())
            itemView.imgAdType.setImageResource(item.type.resourceImage)
            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeToPost(item.id)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisementViewHolder {
        val view = inflater.inflate(R.layout.ad_item_layout, parent, false)
        return AdvertisementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdvertisementViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = items.size

    fun addItems(items: List<Advertisement>){
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }
}