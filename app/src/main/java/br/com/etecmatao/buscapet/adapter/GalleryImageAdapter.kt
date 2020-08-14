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

class GalleryImageAdapter(private val items: List<Image>): RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

    private var context: Context? = null
    var listener: GalleryImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(
            R.layout.item_gallery_image,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind() {
            val image = items[adapterPosition]

            GlideApp.with(context!!)
                .load(image.imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.ivGalleryImage)

            itemView.container.setOnClickListener{
                listener?.onClick(adapterPosition)
            }
        }
    }

    interface GalleryImageClickListener {
        fun onClick(position: Int)
    }
}