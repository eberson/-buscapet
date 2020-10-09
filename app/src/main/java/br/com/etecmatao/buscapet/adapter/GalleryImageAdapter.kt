package br.com.etecmatao.buscapet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.dto.Image
import br.com.etecmatao.buscapet.helper.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_gallery_image.view.*

class GalleryImageAdapter(val listener: (image: Image) -> Unit): RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

    private val items: MutableList<Image> = mutableListOf()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(
            R.layout.item_gallery_image,
            parent,
            false
        )

        return ViewHolder(view)
    }

    fun setImages(images: List<Image>){
        items.clear()
        items.addAll(images)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind() {
            val image = items[adapterPosition]

            GlideApp.with(context!!)
                .load(image.contentURI)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.ivGalleryImage)

            itemView.container.setOnClickListener{
                listener(image)
            }
        }
    } }