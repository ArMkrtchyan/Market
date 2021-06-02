package com.armboldmind.grandmarket.ui.more.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.armboldmind.grandmarket.base.BaseFragment
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.databinding.FragmentRequestProductInfoBinding
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.armboldmind.grandmarket.ui.MainActivity

class RequestProductInfoFragment : BaseFragment<FragmentRequestProductInfoBinding>() {
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRequestProductInfoBinding
        get() = FragmentRequestProductInfoBinding::inflate

    override fun initView(binding: FragmentRequestProductInfoBinding, keysFromDb: Keys) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            keys = keysFromDb
            requestProduct.setOnClickListener {
                view?.findNavController()
                    ?.navigate(RequestProductInfoFragmentDirections.actionRequestProductInfoFragmentToRequestAProductFragment())
            }
            toolbar.setNavigationOnClickListener { (activity as MainActivity).onBackPressed() }
        }
    }

    override fun setLoading(isLoading: Boolean) {

    }

    override fun handleViewState(viewState: ViewState) {

    }
}