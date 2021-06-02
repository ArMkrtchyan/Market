package com.armboldmind.grandmarket.data.models.requestmodels

fun searchRequestModel(init: SearchRequestModel. () -> Unit): SearchRequestModel {
    return SearchRequestModel().apply { init() }
}

fun signUpRequestModel(init: SignUpRequestModel. () -> Unit): SignUpRequestModel {
    return SignUpRequestModel().apply { init() }
}

fun updateUserRequestModel(init: UpdateUserRequestModel. () -> Unit): UpdateUserRequestModel {
    return UpdateUserRequestModel().apply { init() }
}

fun contactUsRequestModel(init: ContactUsRequestModel. () -> Unit): ContactUsRequestModel {
    return ContactUsRequestModel().apply { init() }
}

fun brandsRequestModel(init: BrandsRequestModel. () -> Unit): BrandsRequestModel {
    return BrandsRequestModel().apply { init() }
}

fun productDetailsRequestModel(init: ProductDetailsRequestModel. () -> Unit): ProductDetailsRequestModel {
    return ProductDetailsRequestModel().apply { init() }
}

fun searchProductsModel(init: SearchProductsModel. () -> Unit): SearchProductsModel {
    return SearchProductsModel().apply { init() }
}

fun searchAttributes(init: SearchProductsModel.SearchAttributes. () -> Unit): SearchProductsModel.SearchAttributes {
    return SearchProductsModel.SearchAttributes()
            .apply { init() }
}

fun SearchProductsModel.createNewFrom(): SearchProductsModel {
    return searchProductsModel {
        priceFrom = this@createNewFrom.priceFrom
        priceTo = this@createNewFrom.priceTo
        this@createNewFrom.brandIds?.let { brand ->
            brand.map {
                brandIds?.let { brands -> brands.add(it) } ?: run {
                    brandIds = arrayListOf<Long>().apply { add(it) }
                }
            }
        }
        this@createNewFrom.searchAttributes?.let { brand ->
            brand.map {
                if (!it.attributeIds.isNullOrEmpty()) searchAttributes?.let { brands -> brands.add(it) } ?: run {
                    searchAttributes = arrayListOf<SearchProductsModel.SearchAttributes>().apply {
                        add(it)
                    }
                }
            }
        }
        this@createNewFrom.searchCharacteristic?.let { brand ->
            brand.map {
                if (!it.characteristicIds.isNullOrEmpty()) searchCharacteristic?.let { brands -> brands.add(it) } ?: run {
                    searchCharacteristic = arrayListOf<SearchProductsModel.SearchCharacteristic>().apply {
                        add(it)
                    }
                }
            }
        }
        text = this@createNewFrom.text
        categoryIdList = this@createNewFrom.categoryIdList
        collectionId = this@createNewFrom.collectionId
        discounted = this@createNewFrom.discounted
        categoryName = this@createNewFrom.categoryName
    }
}