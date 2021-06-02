package com.armboldmind.grandmarket.data.models.requestmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class SearchProductsModel : Parcelable {
    var brandIds: ArrayList<Long>? = null
    var categoryIdList: List<Long>? = null
    var collectionId: Long? = null
    var discounted: Boolean? = null
    var priceFrom: Long? = null
    var labelType: Int? = null
    var priceTo: Long? = null
    var searchAttributes: ArrayList<SearchAttributes>? = null
    var searchCharacteristic: ArrayList<SearchCharacteristic>? = null
    var text: String? = null
    var categoryName: String? = null

    @Parcelize
    class SearchAttributes : Parcelable {
        var attributeGroupId: Long? = null
        var attributeIds: ArrayList<Long>? = null
        override fun toString(): String {
            return "SearchAttributes(attributeGroupId=$attributeGroupId, attributeIds=$attributeIds)"
        }

    }

    @Parcelize
    class SearchCharacteristic : Parcelable {
        var characteristicGroupId: Long? = null
        var characteristicIds: ArrayList<Long>? = null
        override fun toString(): String {
            return "SearchCharacteristic(characteristicGroupId=$characteristicGroupId, characteristicIds=$characteristicIds)"
        }

    }

    override fun toString(): String {
        return "SearchProductsModel(brandIds=$brandIds, categoryId=${Arrays.toString(categoryIdList?.toLongArray())}, collectionId=$collectionId, discounted=$discounted, priceFrom=$priceFrom, priceTo=$priceTo, searchAttributes=$searchAttributes, searchCharacteristic=$searchCharacteristic, text=$text, categoryName=$categoryName)"
    }

}
