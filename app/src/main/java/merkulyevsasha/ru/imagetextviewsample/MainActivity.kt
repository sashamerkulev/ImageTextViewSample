package merkulyevsasha.ru.imagetextviewsample

import android.content.Context
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.bill
import kotlinx.android.synthetic.main.activity_main.imageTextViewClient
import kotlinx.android.synthetic.main.activity_main.imageTextViewDiscount
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.item_client.view.clientBonuses
import kotlinx.android.synthetic.main.item_client.view.clientName
import kotlinx.android.synthetic.main.item_product.view.productName
import kotlinx.android.synthetic.main.item_product.view.productPrice
import kotlinx.android.synthetic.main.item_product.view.productQuantity
import merkulyevsasha.ru.imagetextviewsample.controls.ImageTextView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val INACTIVE = 1
        private const val ACTIVE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageTextViewClient.tag = INACTIVE
        imageTextViewClient.setOnClickListener {
            clickMaintainer(imageTextViewClient,
                R.drawable.ic_client_icon_black_36dp, R.drawable.ic_client_icon_orange_36dp)
        }

        imageTextViewDiscount.tag = INACTIVE
        imageTextViewDiscount.setOnClickListener {
            clickMaintainer(imageTextViewDiscount,
                R.drawable.ic_discount_icon_black_36dp, R.drawable.ic_discont_icon_orange_36dp)
        }

        bill.tag = INACTIVE
        bill.setOnClickListener { view ->
            val tag = view.tag as Int
            if (tag == INACTIVE) {
                bill.addCircleText("7")
                bill.tag = ACTIVE
            } else {
                bill.tag = INACTIVE
                bill.removeCircleText()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = Adapter(this, createItems())
    }

    private fun clickMaintainer(
        view: ImageTextView,
        @DrawableRes blackDrawableResId: Int,
        @DrawableRes orangeDrawableResId: Int
    ) {
        val tag = view.tag as Int
        if (tag == INACTIVE) {
            view.setIcon(orangeDrawableResId)
            view.setColor(R.color.blood_orange)
            view.tag = ACTIVE
        } else {
            view.setIcon(blackDrawableResId)
            view.setColor(R.color.black_80)
            view.tag = INACTIVE
        }
    }

    private fun createItems(): List<Item> {
        return listOf(
            Item.Client("Иванов Иван Иваныч", "100500 бонусных балов"),
            Item.Product("картошка", "1 x 100", "8,00 \u20BD"),
            Item.Product("селедка", "1 x 100", "8,00 \u20BD"),
            Item.Product("капуста", "1 x 100", "8,00 \u20BD"),
            Item.Product("морковь", "1 x 100", "8,00 \u20BD"),
            Item.Product("свекла", "1 x 100", "8,00 \u20BD"),
            Item.Product("икра, красная", "10 x 400", "80,00 \u20BD"),
            Item.Product("икра, черная", "11 x 300", "89,00 \u20BD"),
            Item.Product("икра, заморская", "2 x 1000", "108,00 \u20BD"),
            Item.Product("говядина", "1 x 1000", "89,00 \u20BD"),
            Item.Product("свинина", "1 x 1000", "80,00 \u20BD"),
            Item.Product("кура-гриль", "1 x 1000", "80,00 \u20BD")
        )
    }

    private class Adapter(private val context: Context, private val items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val inflater: LayoutInflater = LayoutInflater.from(context);

        override fun getItemViewType(position: Int): Int = if (items[position] is Item.Client) 1 else 2

        override fun getItemCount(): Int = items.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                1 -> ClientViewHolder(parent, inflater)
                2 -> ProductViewHolder(parent, inflater)
                else -> throw IllegalStateException()
            }
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            val item = items[position]
            when (viewHolder) {
                is ClientViewHolder -> {
                    val client = item as Item.Client
                    with(viewHolder.itemView) {
                        clientName.text = client.name
                        clientBonuses.text = client.bonuses
                    }
                }
                is ProductViewHolder -> {
                    val product = item as Item.Product
                    with(viewHolder.itemView) {
                        productName.text = product.name
                        productQuantity.text = product.quantity
                        productPrice.text = product.price
                    }
                }
            }
        }

    }

    class ClientViewHolder(parent: ViewGroup, inflater: LayoutInflater) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_client, parent, false))

    class ProductViewHolder(parent: ViewGroup, inflater: LayoutInflater) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_product, parent, false))

    private sealed class Item {
        data class Product(
            val name: String,
            val quantity: String,
            val price: String
        ) : Item()

        data class Client(
            val name: String,
            val bonuses: String
        ) : Item()
    }

}
