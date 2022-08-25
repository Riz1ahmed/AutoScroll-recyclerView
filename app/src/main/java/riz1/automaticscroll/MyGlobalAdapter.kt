package duggu.automaticscroll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * @author Riz1Ahmed
 * @param inflate inflater of your Item layout binding.
 *                Ex: YourItemLayoutBinding::inflate.
 * @param size size of recyclerView. whenever your data size change
 * then call updateSize(yourData.size)
 *
 * An Example Implementation code:
 *   val myAdapter = object : MyGlobalAdapter<YourItemLayoutBinding>(YourItemLayoutBinding::inflate, yourData.size) {
 *       override fun bind(binding: YourItemLayoutBinding, position: Int) {
 *       //Bind here the view. eg:
 *       binding.text="This is Global adapter item pos $position"
 *   }}
 *
 * NB: please call updateSize(yourData.size) when your data size become change.
 */
abstract class MyGlobalAdapter<viewType : ViewBinding>(
    private val inflate: Inflate<viewType>, private var size: Int
) :
    RecyclerView.Adapter<MyGlobalAdapter<viewType>.MyHolder>() {

    private var layoutInflater: LayoutInflater? = null

    fun updateSize(size: Int) {
        this.size = size
        notifyDataSetChanged()
    }

    fun <T> removeItemWithNotifyUI(items: ArrayList<T>, position: Int) {
        if (items.size > position) {
            size--
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size - position)
        }
    }

    fun notifyItemAdded(position: Int) {
        size++
        notifyItemInserted(position)
        notifyItemRangeChanged(position + 1, size - position - 1)
    }

    fun notifyItemDeleted(position: Int) {
        size--
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, size - position)
    }

    inner class MyHolder(private val holder: viewType) : RecyclerView.ViewHolder(holder.root) {
        fun bind(position: Int) {
            bind(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        layoutInflater = layoutInflater ?: LayoutInflater.from(parent.context)
        return MyHolder(inflate.invoke(layoutInflater!!, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(position)
    override fun getItemCount() = size
    abstract fun bind(binder: viewType, position: Int)
}
