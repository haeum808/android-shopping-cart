package woowacourse.shopping.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.model.Product
import woowacourse.shopping.domain.repository.ProductRepository

class HomeViewModel(
    private val productRepository: ProductRepository,
) : ViewModel() {
    private var page: Int = 0

    private val _products: MutableLiveData<List<Product>> =
        MutableLiveData<List<Product>>(emptyList())
    val products: LiveData<List<Product>>
        get() = _products

    private val _loadingAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    val loadingAvailable: LiveData<Boolean>
        get() = _loadingAvailable

    fun loadProducts() {
        _loadingAvailable.value = false
        _products.value = productRepository.fetchSinglePage(page++)
        products.value?.let {
            println(it.size)
            _loadingAvailable.value = it.size >= 20
            println(loadingAvailable.value)
        }
    }
}
