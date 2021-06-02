package com.armboldmind.grandmarket.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.data.models.requestmodels.searchProductsModel
import com.armboldmind.grandmarket.databinding.FragmentSubCategoriesBinding
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.categories.adapters.AdapterSubCategories
import com.armboldmind.grandmarket.ui.categories.adapters.AdapterSubCategoriesCoverPhoto

class SubCategoriesFragment : BaseFragment<FragmentSubCategoriesBinding>() {
    private val mArgs: SubCategoriesFragmentArgs by navArgs()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSubCategoriesBinding
        get() = FragmentSubCategoriesBinding::inflate

    override val toolbar: Toolbar
        get() = mBinding.toolbar

    override fun initView(binding: FragmentSubCategoriesBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            title = mArgs.category.categoryName
            keys = keysFromDb
            subCategories.adapter = ConcatAdapter(AdapterSubCategoriesCoverPhoto(mArgs.category.coverPhoto), AdapterSubCategories { subCategory ->
                view?.findNavController()
                        ?.navigate(SubCategoriesFragmentDirections.actionSubCategoriesFragmentToProductsFragment(searchProductsModel {
                            categoryIdList = arrayListOf<Long>().apply { add(subCategory.id) }
                            categoryName = subCategory.categoryName
                        }))
            }.apply { submitList(mArgs.category.subCategories) })
        }
    }

    override fun setLoading(isLoading: Boolean) {

    }

    override fun handleViewState(viewState: ViewState) {

    }

}