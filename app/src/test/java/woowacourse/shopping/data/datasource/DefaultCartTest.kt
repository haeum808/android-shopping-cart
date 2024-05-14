package woowacourse.shopping.data.datasource

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import woowacourse.shopping.data.model.CartItem

class DefaultCartTest {
    @Test
    fun `장바구니에 상품을 추가할 수 있다`() {
        // when
        with(DefaultCart) {
            val firstItemId = addCartItem(CartItem(1, 1, 1))
            val secondItemId = addCartItem(CartItem(2, 2, 1))
            assertAll(
                { assertThat(firstItemId).isEqualTo(1) },
                { assertThat(secondItemId).isEqualTo(2) },
            )
        }
    }

    @Test
    fun `페이지당 장바구니 상품의 수가 허용 범위 이내이다`() {
        with(DefaultCart) {
            repeat(6) {
                addCartItem(CartItem(productId = it.toLong() + 1))
            }
            val actualSize = getCartItems(0, 5).size
            assertThat(actualSize).isEqualTo(5)
        }
    }
}