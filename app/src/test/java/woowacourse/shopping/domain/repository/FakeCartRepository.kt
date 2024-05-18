package woowacourse.shopping.domain.repository

import woowacourse.shopping.data.model.CartItem
import kotlin.math.min

class FakeCartRepository(
    private val cartItems: MutableList<CartItem>,
) : CartRepository {
    private var id: Long = 14

    override fun addCartItem(
        productId: Long,
        quantity: Int,
    ): Long {
        cartItems.add(
            CartItem(
                id = id,
                productId = productId,
                quantity = quantity,
            ),
        )
        return id++
    }

    override fun removeCartItem(cartItemId: Long): Long {
        cartItems.removeIf { it.id == cartItemId }
        return cartItemId
    }

    override fun fetchCartItems(page: Int): List<CartItem> {
        val fromIndex = page * 5
        val toIndex = min(fromIndex + 5, cartItems.size)

        if (fromIndex > toIndex) return emptyList()

        return cartItems.subList(fromIndex, toIndex)
    }
}
