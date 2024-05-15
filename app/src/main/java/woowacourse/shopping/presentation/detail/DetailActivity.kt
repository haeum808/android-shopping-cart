package woowacourse.shopping.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.R
import woowacourse.shopping.data.datasource.DefaultCart
import woowacourse.shopping.data.datasource.DefaultProducts
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), AddCompleteListener {
    private val binding: ActivityDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_detail)
    }
    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(
            this,
            DetailViewModelFactory(ProductRepositoryImpl(DefaultProducts), CartRepositoryImpl(DefaultCart)),
        )[DetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, DEFAULT_PRODUCT_ID)

        viewModel.loadProductInformation(productId)

        viewModel.productInformation.observe(this) {
            binding.product = it
        }
        binding.detailViewModel = viewModel
        binding.addCompleteListener = this
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXTRA_PRODUCT_ID = "extra_product_id"
        private const val DEFAULT_PRODUCT_ID = -1L

        fun newIntent(
            context: Context,
            productId: Long,
        ): Intent {
            return Intent(context, DetailActivity::class.java).putExtra(EXTRA_PRODUCT_ID, productId)
        }
    }

    override fun onAddComplete() {
        Toast.makeText(this, getString(R.string.message_add_to_cart_complete), Toast.LENGTH_SHORT).show()
    }
}