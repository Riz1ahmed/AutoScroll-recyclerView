package riz1.automaticscroll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import riz1.automaticscroll.databinding.ItemAdapterBinding

class NewsAdapter(var dataArray: Array<String?>, var listener: View.OnClickListener) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binder: ItemAdapterBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bind() {
            dataArray[layoutPosition % dataArray.size]?.let { model ->
                binder.tvNews.setOnClickListener {
                    it?.setTag(R.id.model, dataArray[layoutPosition % dataArray.size])
                    listener.onClick(it)
                }
                binder.tvNews.text = model
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(
        ItemAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = Integer.MAX_VALUE
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) = holder.bind()
}