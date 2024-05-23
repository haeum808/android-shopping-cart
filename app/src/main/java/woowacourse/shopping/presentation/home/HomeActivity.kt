package woowacourse.shopping.presentation.home

import android.os.Build.VERSION
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import woowacourse.shopping.data.datasource.DefaultCart
import woowacourse.shopping.data.datasource.DefaultProducts
import woowacourse.shopping.data.model.CartItem
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.databinding.ActivityHomeBinding
import woowacourse.shopping.presentation.cart.CartActivity
import woowacourse.shopping.presentation.detail.DetailActivity
import woowacourse.shopping.presentation.detail.DetailActivity.Companion.DETAIL_RESULT_OK
import woowacourse.shopping.presentation.detail.DetailActivity.Companion.EXTRA_CART_ITEM
import woowacourse.shopping.presentation.home.adapter.ProductAdapter
import woowacourse.shopping.presentation.home.adapter.ProductsGridLayoutManager
import woowacourse.shopping.presentation.home.viewmodel.HomeViewModel
import woowacourse.shopping.presentation.home.viewmodel.HomeViewModelFactory

class HomeActivity : AppCompatActivity() {
    private val detailActivityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == DETAIL_RESULT_OK) {
                val cartItem: CartItem? =
                    if (VERSION.SDK_INT >= TIRAMISU_SDK_VERSION) {
                        result.data?.getParcelableExtra(EXTRA_CART_ITEM, CartItem::class.java)
                    } else {
                        result.data?.getParcelableExtra(EXTRA_CART_ITEM)
                    }
                viewModel.updateOrder(cartItem)
            }
        }

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            HomeViewModelFactory(
                ProductRepositoryImpl(DefaultProducts),
                CartRepositoryImpl(DefaultCart),
            ),
        )[HomeViewModel::class.java]
    }
    private val adapter: ProductAdapter by lazy {
        ProductAdapter(viewModel, viewModel)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTotalCartCount()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initBinding()
        initObserver()
        initToolBar()
    }

    private fun initBinding() {
        binding.rvHome.layoutManager =
            GridLayoutManager(this, 2).apply {
                spanSizeLookup = ProductsGridLayoutManager(adapter)
            }
        binding.productAdapter = adapter
        binding.viewModel = viewModel
        binding.homeActionHandler = viewModel
        binding.lifecycleOwner = this
    }

    private fun initObserver() {
        viewModel.orders.observe(this) {
            adapter.addProducts(it)
        }

        viewModel.loadStatus.observe(this) {
            adapter.updateLoadStatus(it)
        }

        viewModel.onProductClicked.observe(this) {
            it.getContentIfNotHandled()?.let { productId ->
                detailActivityResultLauncher.launch(DetailActivity.newIntent(this, productId))
            }
        }

        viewModel.onCartClicked.observe(this) {
            it.getContentIfNotHandled()?.let {
                startActivity(CartActivity.newIntent(this))
            }
        }
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    companion object {
        private const val TIRAMISU_SDK_VERSION = 33
    }
}
