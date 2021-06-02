package com.armboldmind.grandmarket

import com.armboldmind.grandmarket.data.mappers.UserMapper

class MapperTest {

    private val mUserMapper by lazy { UserMapper() }

    //    @TestFactory
    //    @DisplayName("user response format to user test")
    //    fun userMappingTest() = listOf(Triple(UserResponseModel(70), User(70), true), Triple(UserResponseModel(1), User(3), false), Triple(UserResponseModel(null), User(0), true)).map { (userResponseModel, user, expected) ->
    //        DynamicTest.dynamicTest("given \"$userResponseModel\", when formatting to \"$user\" model, then it should be reported as ${if (expected) "success" else "wrong"}") {
    //            mUserMapper.map(userResponseModel)
    //            assertThat(user.id == mUserMapper.map(userResponseModel).id, Matchers.equalTo(expected))
    //        }
    //    }
}