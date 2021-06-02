package com.armboldmind.grandmarket.data.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Keys")
class Keys {
    @PrimaryKey(autoGenerate = true) var id: Long = 1

    /**
     * Global keys
     * **/
    @ColumnInfo(name = "cancel", defaultValue = "cancel") var cancel: String = "cancel"
    @ColumnInfo(name = "next", defaultValue = "next") var next: String = "next"
    @ColumnInfo(name = "save", defaultValue = "save") var save: String = "save"
    @ColumnInfo(name = "close", defaultValue = "close") var close: String = "close"
    @ColumnInfo(name = "yes", defaultValue = "yes") var yes: String = "yes"
    @ColumnInfo(name = "no", defaultValue = "no") var no: String = "no"
    @ColumnInfo(name = "done", defaultValue = "done") var done: String = "done"
    @ColumnInfo(name = "confirm", defaultValue = "confirm") var confirm: String = "confirm"
    @ColumnInfo(name = "retry", defaultValue = "retry") var retry: String = "retry"
    @ColumnInfo(name = "search", defaultValue = "search") var search: String = "search"
    @ColumnInfo(name = "edit", defaultValue = "edit") var edit: String = "edit"
    @ColumnInfo(name = "delete", defaultValue = "delete") var delete: String = "delete"
    @ColumnInfo(name = "other", defaultValue = "other") var other: String = "other"
    @ColumnInfo(name = "add", defaultValue = "add") var add: String = "add"
    @ColumnInfo(name = "change", defaultValue = "change") var change: String = "change"
    @ColumnInfo(name = "choose", defaultValue = "choose") var choose: String = "choose"

    /**
     *  Empty states keys
     * **/
    @ColumnInfo(name = "no_favorites", defaultValue = "no_favorites") var no_favorites: String = "no_favorites"
    @ColumnInfo(name = "no_products", defaultValue = "no_products") var no_products: String = "no_products"
    @ColumnInfo(name = "no_notifications", defaultValue = "no_notifications") var no_notifications: String = "no_notifications"
    @ColumnInfo(name = "no_orders", defaultValue = "no_orders") var no_orders: String = "no_orders"
    @ColumnInfo(name = "no_cards", defaultValue = "no_cards") var no_cards: String = "no_cards"
    @ColumnInfo(name = "no_addresses", defaultValue = "no_addresses") var no_addresses: String = "no_addresses"
    @ColumnInfo(name = "no_basket", defaultValue = "no_basket") var no_basket: String = "no_basket"
    @ColumnInfo(name = "no_requests", defaultValue = "no_requests") var no_requests: String = "no_requests"
    @ColumnInfo(name = "no_subscriptions", defaultValue = "no_subscriptions") var no_subscriptions: String = "no_subscriptions"

    /**
     *  Messages keys
     * **/
    @ColumnInfo(name = "default_error_message", defaultValue = "default_error_message") var default_error_message: String = "default_error_message"
    @ColumnInfo(name = "default_network_error_title", defaultValue = "default_network_error_title") var default_network_error_title: String = "default_network_error_title"
    @ColumnInfo(name = "default_network_error_description", defaultValue = "default_network_error_description") var default_network_error_description: String =
        "default_network_error_description"
    @ColumnInfo(name = "default_server_error_title", defaultValue = "default_server_error_title") var default_server_error_title: String = "default_server_error_title"
    @ColumnInfo(name = "default_server_error_description", defaultValue = "default_server_error_description") var default_server_error_description: String =
        "default_server_error_description"
    @ColumnInfo(name = "server_error", defaultValue = "server_error") var server_error: String = "server_error"
    @ColumnInfo(name = "network_error", defaultValue = "network_error") var network_error: String = "network_error"
    @ColumnInfo(name = "incorrect_fields", defaultValue = "incorrect_fields") var incorrect_fields: String = "incorrect_fields"
    @ColumnInfo(name = "enter_verification_code", defaultValue = "enter_verification_code") var enter_verification_code: String = "enter_verification_code"
    @ColumnInfo(name = "incorrect_password", defaultValue = "incorrect_password") var incorrect_password: String = "incorrect_password"
    @ColumnInfo(name = "passwords_dont_match", defaultValue = "passwords_dont_match") var passwords_dont_match: String = "passwords_dont_match"
    @ColumnInfo(name = "incorrect_email", defaultValue = "incorrect_email") var incorrect_email: String = "incorrect_email"
    @ColumnInfo(name = "incorrect_phone", defaultValue = "incorrect_phone") var incorrect_phone: String = "incorrect_phone"
    @ColumnInfo(name = "wrong_code_message", defaultValue = "wrong_code_message") var wrong_code_message: String = "wrong_code_message"
    @ColumnInfo(name = "email_or_phone_required", defaultValue = "email_or_phone_required") var email_or_phone_required: String = "email_or_phone_required"
    @ColumnInfo(name = "to_many_request_message", defaultValue = "to_many_request_message") var to_many_request_message: String = "to_many_request_message"
    @ColumnInfo(name = "account_with_phone_exist", defaultValue = "account_with_phone_exist") var account_with_phone_exist: String = "account_with_phone_exist"
    @ColumnInfo(name = "please_read_terms_and_conditions", defaultValue = "please_read_terms_and_conditions") var please_read_terms_and_conditions: String =
        "please_read_terms_and_conditions"
    @ColumnInfo(name = "gallery_permission_denied", defaultValue = "gallery_permission_denied") var gallery_permission_denied: String = "gallery_permission_denied"
    @ColumnInfo(name = "camera_permission_denied", defaultValue = "camera_permission_denied") var camera_permission_denied: String = "camera_permission_denied"
    @ColumnInfo(name = "photo_already_selected", defaultValue = "photo_already_selected") var photo_already_selected: String = "photo_already_selected"
    @ColumnInfo(name = "info_changed_successfully", defaultValue = "info_changed_successfully") var info_changed_successfully: String = "info_changed_successfully"
    @ColumnInfo(name = "password_changed_successfully", defaultValue = "password_changed_successfully") var password_changed_successfully: String = "password_changed_successfully"
    @ColumnInfo(name = "photo_added_successfully", defaultValue = "photo_added_successfully") var photo_added_successfully: String = "photo_added_successfully"
    @ColumnInfo(name = "photo_changed_successfully", defaultValue = "photo_changed_successfully") var photo_changed_successfully: String = "photo_changed_successfully"
    @ColumnInfo(name = "photo_deleted_successfully", defaultValue = "photo_deleted_successfully") var photo_deleted_successfully: String = "photo_deleted_successfully"
    @ColumnInfo(name = "verification_code_required", defaultValue = "verification_code_required") var verification_code_required: String = "verification_code_required"

    /**
     *  Dialogs keys
     * **/
    @ColumnInfo(name = "your_message_was_succesfuly_sent", defaultValue = "your_message_was_succesfuly_sent") var your_message_was_succesfuly_sent: String =
        "your_message_was_succesfuly_sent"
    @ColumnInfo(name = "thank_you", defaultValue = "thank_you") var thank_you: String = "thank_you"
    @ColumnInfo(name = "sign_out", defaultValue = "sign_out") var sign_out: String = "sign_out"
    @ColumnInfo(name = "sign_out_popup_text", defaultValue = "sign_out_popup_text") var sign_out_popup_text: String = "sign_out_popup_text"
    @ColumnInfo(name = "delete_card_title", defaultValue = "delete_card_title") var delete_card_title: String = "delete_card_title"
    @ColumnInfo(name = "delete_card_description", defaultValue = "delete_card_description") var delete_card_description: String = "delete_card_description"
    @ColumnInfo(name = "delete_photo_title", defaultValue = "delete_photo_title") var delete_photo_title: String = "delete_photo_title"
    @ColumnInfo(name = "delete_photo_description", defaultValue = "delete_photo_description") var delete_photo_description: String = "delete_photo_description"
    @ColumnInfo(name = "delete_address_title", defaultValue = "delete_address_title") var delete_address_title: String = "delete_address_title"
    @ColumnInfo(name = "delete_address_description", defaultValue = "delete_address_description") var delete_address_description: String = "delete_address_description"
    @ColumnInfo(name = "default_address_title", defaultValue = "default_address_title") var default_address_title: String = "default_address_title"
    @ColumnInfo(name = "default_address_description", defaultValue = "default_address_description") var default_address_description: String = "default_address_description"
    @ColumnInfo(name = "request_created_description", defaultValue = "request_created_description") var request_created_description: String = "request_created_description"
    @ColumnInfo(name = "account_with_phone_not_exist", defaultValue = "account_with_phone_not_exist") var account_with_phone_not_exist: String = "account_with_phone_not_exist"
    @ColumnInfo(name = "account_with_email_not_exist", defaultValue = "account_with_email_not_exist") var account_with_email_not_exist: String = "account_with_email_not_exist"

    /**
     *  Home page keys
     * **/
    @ColumnInfo(name = "request_here", defaultValue = "request_here") var request_here: String = "request_here"
    @ColumnInfo(name = "looking_for_product", defaultValue = "looking_for_product") var looking_for_product: String = "looking_for_product"
    @ColumnInfo(name = "what_are_you_looking_for", defaultValue = "what_are_you_looking_for") var what_are_you_looking_for: String = "what_are_you_looking_for"

    /**
     *  Categories page keys
     * **/
    @ColumnInfo(name = "categories", defaultValue = "categories") var categories: String = "categories"
    @ColumnInfo(name = "all_categories_title", defaultValue = "all_categories_title") var all_categories_title: String = "all_categories_title"

    /**
     *  Products page keys
     * **/
    @ColumnInfo(name = "filter", defaultValue = "filter") var filter: String = "filter"
    @ColumnInfo(name = "reset", defaultValue = "reset") var reset: String = "reset"
    @ColumnInfo(name = "price_range", defaultValue = "price_range") var price_range: String = "price_range"
    @ColumnInfo(name = "color", defaultValue = "color") var color: String = "color"
    @ColumnInfo(name = "size", defaultValue = "size") var size: String = "size"
    @ColumnInfo(name = "view_items", defaultValue = "view_items") var view_items: String = "view_items"
    @ColumnInfo(name = "quantity", defaultValue = "quantity") var quantity: String = "quantity"
    @ColumnInfo(name = "products_related_title", defaultValue = "products_related_title") var products_related_title: String = "products_related_title"
    @ColumnInfo(name = "subscribe", defaultValue = "subscribe") var subscribe: String = "subscribe"
    @ColumnInfo(name = "add_to_basket", defaultValue = "add_to_basket") var add_to_basket: String = "add_to_basket"

    /**
     *  Basket page keys
     * **/
    @ColumnInfo(name = "basket", defaultValue = "basket") var basket: String = "basket"

    /**
     *  Favorites page keys
     * **/
    @ColumnInfo(name = "favorites", defaultValue = "favorites") var favorites: String = "favorites"

    /**
     *  More page keys
     * **/
    @ColumnInfo(name = "welcome_title", defaultValue = "welcome_title") var welcome_title: String = "welcome_title"
    @ColumnInfo(name = "welcome_description", defaultValue = "welcome_description") var welcome_description: String = "welcome_description"

    /**
     *  Authorization keys
     * **/
    @ColumnInfo(name = "sign_in", defaultValue = "sign_in") var sign_in: String = "sign_in"
    @ColumnInfo(name = "sign_in_description", defaultValue = "sign_in_description") var sign_in_description: String = "sign_in_description"
    @ColumnInfo(name = "email_or_phone_number", defaultValue = "email_or_phone_number") var email_or_phone_number: String = "email_or_phone_number"
    @ColumnInfo(name = "password", defaultValue = "password") var password: String = "password"
    @ColumnInfo(name = "forgot_password", defaultValue = "forgot_password") var forgot_password: String = "forgot_password"
    @ColumnInfo(name = "forgot_password_title", defaultValue = "forgot_password_title") var forgot_password_title: String = "forgot_password_title"
    @ColumnInfo(name = "forgot_password_description", defaultValue = "forgot_password_description") var forgot_password_description: String = "forgot_password_description"
    @ColumnInfo(name = "don_t_have_an_account_sign_up", defaultValue = "don_t_have_an_account_sign_up") var don_t_have_an_account_sign_up: String = "don_t_have_an_account_sign_up"
    @ColumnInfo(name = "sign_up", defaultValue = "sign_up") var sign_up: String = "sign_up"
    @ColumnInfo(name = "already_have_an_account_sign_in", defaultValue = "already_have_an_account_sign_in") var already_have_an_account_sign_in: String =
        "already_have_an_account_sign_in"
    @ColumnInfo(name = "phone_desc_for_sign_up", defaultValue = "phone_desc_for_sign_up") var phone_desc_for_sign_up: String = "phone_desc_for_sign_up"
    @ColumnInfo(name = "email_desc_for_sign_up", defaultValue = "email_desc_for_sign_up") var email_desc_for_sign_up: String = "email_desc_for_sign_up"
    @ColumnInfo(name = "sign_up_with_email_address", defaultValue = "sign_up_with_email_address") var sign_up_with_email_address: String = "sign_up_with_email_address"
    @ColumnInfo(name = "sign_up_with_phone_number", defaultValue = "sign_up_with_phone_number") var sign_up_with_phone_number: String = "sign_up_with_phone_number"
    @ColumnInfo(name = "enter_full_name", defaultValue = "enter_full_name") var enter_full_name: String = "enter_full_name"
    @ColumnInfo(name = "choose_date_of_birth", defaultValue = "choose_date_of_birth") var choose_date_of_birth: String = "choose_date_of_birth"
    @ColumnInfo(name = "enter_email_address", defaultValue = "enter_email_address") var enter_email_address: String = "enter_email_address"
    @ColumnInfo(name = "repeat_password", defaultValue = "repeat_password") var repeat_password: String = "repeat_password"
    @ColumnInfo(name = "confirm_terms_and_policy", defaultValue = "confirm_terms_and_policy") var confirm_terms_and_policy: String = "confirm_terms_and_policy"
    @ColumnInfo(name = "create_account", defaultValue = "create_account") var create_account: String = "create_account"
    @ColumnInfo(name = "please_enter_your_password", defaultValue = "please_enter_your_password") var please_enter_your_password: String = "please_enter_your_password"
    @ColumnInfo(name = "create_password", defaultValue = "create_password") var create_password: String = "create_password"
    @ColumnInfo(name = "resend_again", defaultValue = "resend_again") var resend_again: String = "resend_again"
    @ColumnInfo(name = "resend_in", defaultValue = "resend_in") var resend_in: String = "resend_in"
    @ColumnInfo(name = "sent_code_to_phone", defaultValue = "sent_code_to_phone") var sent_code_to_phone: String = "sent_code_to_phone"
    @ColumnInfo(name = "sent_code_to_email", defaultValue = "sent_code_to_email") var sent_code_to_email: String = "sent_code_to_email"
    @ColumnInfo(name = "enter_the_code", defaultValue = "enter_the_code") var enter_the_code: String = "enter_the_code"
    @ColumnInfo(name = "full_name", defaultValue = "full_name") var full_name: String = "full_name"
    @ColumnInfo(name = "verification", defaultValue = "verification") var verification: String = "verification"
    @ColumnInfo(name = "phone_number", defaultValue = "phone_number") var phone_number: String = "phone_number"
    @ColumnInfo(name = "phone_", defaultValue = "phone_") var phone_: String = "phone_"
    @ColumnInfo(name = "send", defaultValue = "send") var send: String = "send"
    @ColumnInfo(name = "email_", defaultValue = "email_") var email_: String = "email_"
    @ColumnInfo(name = "date_of_birth", defaultValue = "date_of_birth") var date_of_birth: String = "date_of_birth"
    @ColumnInfo(name = "enter_email_or_phone_number", defaultValue = "enter_email_or_phone_number") var enter_email_or_phone_number: String = "enter_email_or_phone_number"
    @ColumnInfo(name = "verify", defaultValue = "verify") var verify: String = "verify"
    @ColumnInfo(name = "forgot_password_", defaultValue = "forgot_password_") var forgot_password_: String = "forgot_password_"
    @ColumnInfo(name = "email_address", defaultValue = "email_address") var email_address: String = "email_address"
    @ColumnInfo(name = "confirm_new_password", defaultValue = "confirm_new_password") var confirm_new_password: String = "confirm_new_password"

    /**
     *  Personal information page keys
     * **/
    @ColumnInfo(name = "change_password", defaultValue = "change_password") var change_password: String = "change_password"
    @ColumnInfo(name = "personal_information", defaultValue = "personal_information") var personal_information: String = "personal_information"
    @ColumnInfo(name = "login_information", defaultValue = "login_information") var login_information: String = "login_information"
    @ColumnInfo(name = "current_password", defaultValue = "current_password") var current_password: String = "current_password"
    @ColumnInfo(name = "delete_photo", defaultValue = "delete_photo") var delete_photo: String = "delete_photo"
    @ColumnInfo(name = "take_a_photo", defaultValue = "take_a_photo") var take_a_photo: String = "take_a_photo"
    @ColumnInfo(name = "choose_from_gallery", defaultValue = "choose_from_gallery") var choose_from_gallery: String = "choose_from_gallery"
    @ColumnInfo(name = "receive_code", defaultValue = "receive_code") var receive_code: String = "receive_code"
    @ColumnInfo(name = "new_password", defaultValue = "new_password") var new_password: String = "new_password"
    @ColumnInfo(name = "add_email_address", defaultValue = "add_email_address") var add_email_address: String = "add_email_address"
    @ColumnInfo(name = "change_email_address", defaultValue = "change_email_address") var change_email_address: String = "change_email_address"
    @ColumnInfo(name = "change_phone_number", defaultValue = "change_phone_number") var change_phone_number: String = "change_phone_number"
    @ColumnInfo(name = "add_phone_number", defaultValue = "add_phone_number") var add_phone_number: String = "add_phone_number"

    /**
     *  Addresses and add address pages keys
     * **/
    @ColumnInfo(name = "addresses", defaultValue = "addresses") var addresses: String = "addresses"
    @ColumnInfo(name = "address_", defaultValue = "address_") var address_: String = "address_"
    @ColumnInfo(name = "add_address", defaultValue = "add_address") var add_address: String = "add_address"
    @ColumnInfo(name = "set_as_default", defaultValue = "set_as_default") var set_as_default: String = "set_as_default"
    @ColumnInfo(name = "make_default", defaultValue = "make_default") var make_default: String = "make_default"
    @ColumnInfo(name = "add_new_address", defaultValue = "add_new_address") var add_new_address: String = "add_new_address"
    @ColumnInfo(name = "edit_address", defaultValue = "edit_address") var edit_address: String = "edit_address"
    @ColumnInfo(name = "delivery_address_information", defaultValue = "delivery_address_information") var delivery_address_information: String = "delivery_address_information"
    @ColumnInfo(name = "delivery_address_title", defaultValue = "delivery_address_title") var delivery_address_title: String = "delivery_address_title"
    @ColumnInfo(name = "enter_your_address", defaultValue = "enter_your_address") var enter_your_address: String = "enter_your_address"
    @ColumnInfo(name = "address__", defaultValue = "address__") var address__: String = "address__"
    @ColumnInfo(name = "apartment", defaultValue = "apartment") var apartment: String = "apartment"
    @ColumnInfo(name = "entrance", defaultValue = "entrance") var entrance: String = "entrance"
    @ColumnInfo(name = "floor", defaultValue = "floor") var floor: String = "floor"
    @ColumnInfo(name = "additional_information", defaultValue = "additional_information") var additional_information: String = "additional_information"
    @ColumnInfo(name = "title", defaultValue = "title") var title: String = "title"
    @ColumnInfo(name = "set_as_default_address", defaultValue = "set_as_default_address") var set_as_default_address: String = "set_as_default_address"
    @ColumnInfo(name = "comment", defaultValue = "comment") var comment: String = "comment"

    /**
     *  Cards page keys
     * **/
    @ColumnInfo(name = "cards", defaultValue = "cards") var cards: String = "cards"
    @ColumnInfo(name = "add_new_card", defaultValue = "add_new_card") var add_new_card: String = "add_new_card"
    @ColumnInfo(name = "add_card", defaultValue = "add_card") var add_card: String = "add_card"

    /**
     *   Notifications page keys
     * **/
    @ColumnInfo(name = "notifications", defaultValue = "notifications") var notifications: String = "notifications"

    /**
     *  Order and Order details page keys
     * **/
    @ColumnInfo(name = "order_history", defaultValue = "order_history") var order_history: String = "order_history"
    @ColumnInfo(name = "orders", defaultValue = "orders") var orders: String = "orders"

    /**
     *  Requests and request details page keys
     * **/
    @ColumnInfo(name = "requests", defaultValue = "requests") var requests: String = "requests"
    @ColumnInfo(name = "request_product", defaultValue = "request_product") var request_product: String = "request_product"
    @ColumnInfo(name = "categories_", defaultValue = "categories_") var categories_: String = "categories_"
    @ColumnInfo(name = "brand_", defaultValue = "brand_") var brand_: String = "brand_"
    @ColumnInfo(name = "date_", defaultValue = "date_") var date_: String = "date_"
    @ColumnInfo(name = "request_info_description", defaultValue = "request_info_description") var request_info_description: String = "request_info_description"
    @ColumnInfo(name = "request_info_title", defaultValue = "request_info_title") var request_info_title: String = "request_info_title"
    @ColumnInfo(name = "request_info_hint_1", defaultValue = "request_info_hint_1") var request_info_hint_1: String = "request_info_hint_1"
    @ColumnInfo(name = "request_info_hint_2", defaultValue = "request_info_hint_2") var request_info_hint_2: String = "request_info_hint_2"
    @ColumnInfo(name = "request_info_hint_3", defaultValue = "request_info_hint_3") var request_info_hint_3: String = "request_info_hint_3"
    @ColumnInfo(name = "request_now", defaultValue = "request_now") var request_now: String = "request_now"

    /**
     *  Create request page keys
     * **/
    @ColumnInfo(name = "request_a_product_title_1", defaultValue = "request_a_product_title_1") var request_a_product_title_1: String = "request_a_product_title_1"
    @ColumnInfo(name = "request_a_product_title_2", defaultValue = "request_a_product_title_2") var request_a_product_title_2: String = "request_a_product_title_2"
    @ColumnInfo(name = "contact_information__", defaultValue = "contact_information__") var contact_information__: String = "contact_information__"
    @ColumnInfo(name = "select_category__", defaultValue = "select_category__") var select_category__: String = "select_category__"
    @ColumnInfo(name = "select_category", defaultValue = "select_category") var select_category: String = "select_category"
    @ColumnInfo(name = "select_brand__", defaultValue = "select_brand__") var select_brand__: String = "select_brand__"
    @ColumnInfo(name = "select_brand", defaultValue = "select_brand") var select_brand: String = "select_brand"
    @ColumnInfo(name = "product_name__", defaultValue = "product_name__") var product_name__: String = "product_name__"
    @ColumnInfo(name = "enter_product_name", defaultValue = "enter_product_name") var enter_product_name: String = "enter_product_name"
    @ColumnInfo(name = "description__", defaultValue = "description__") var description__: String = "description__"
    @ColumnInfo(name = "description", defaultValue = "description") var description: String = "description"
    @ColumnInfo(name = "upload_photo", defaultValue = "upload_photo") var upload_photo: String = "upload_photo"
    @ColumnInfo(name = "send_request", defaultValue = "send_request") var send_request: String = "send_request"

    /**
     *  Subscriptions and details keys
     * **/
    @ColumnInfo(name = "subscriptions", defaultValue = "subscriptions") var subscriptions: String = "subscriptions"

    /**
     *  About us and About App page keys
     * **/
    @ColumnInfo(name = "about_us", defaultValue = "about_us") var about_us: String = "about_us"
    @ColumnInfo(name = "about_app", defaultValue = "about_app") var about_app: String = "about_app"
    @ColumnInfo(name = "rate_app", defaultValue = "rate_app") var rate_app: String = "rate_app"
    @ColumnInfo(name = "powered_by", defaultValue = "powered_by") var powered_by: String = "powered_by"
    @ColumnInfo(name = "version_of_the_app", defaultValue = "version_of_the_app") var version_of_the_app: String = "version_of_the_app"
    @ColumnInfo(name = "find_us_on_facebook", defaultValue = "find_us_on_facebook") var find_us_on_facebook: String = "find_us_on_facebook"
    @ColumnInfo(name = "about_us_bottom_text", defaultValue = "about_us_bottom_text") var about_us_bottom_text: String = "about_us_bottom_text"
    @ColumnInfo(name = "about_us_top_text", defaultValue = "about_us_top_text") var about_us_top_text: String = "about_us_top_text"
    @ColumnInfo(name = "about_us_title", defaultValue = "about_us_title") var about_us_title: String = "about_us_title"

    /**
     *  Privacy and terms page keys
     * **/
    @ColumnInfo(name = "privacy_police", defaultValue = "privacy_police") var privacy_police: String = "privacy_police"
    @ColumnInfo(name = "terms_and_conditions", defaultValue = "terms_and_conditions") var terms_and_conditions: String = "terms_and_conditions"

    /**
     *  FAQ page keys
     * **/
    @ColumnInfo(name = "frequently_asked_questions", defaultValue = "frequently_asked_questions") var frequently_asked_questions: String = "frequently_asked_questions"
    @ColumnInfo(name = "faq_title", defaultValue = "faq_title") var faq_title: String = "faq_title"
    @ColumnInfo(name = "faq", defaultValue = "faq") var faq: String = "faq"
    @ColumnInfo(name = "faq_placeholder", defaultValue = "faq_placeholder") var faq_placeholder: String = "faq_placeholder"
    @ColumnInfo(name = "faq_description", defaultValue = "faq_description") var faq_description: String = "faq_description"
    @ColumnInfo(name = "we_are_here_to_help_you", defaultValue = "we_are_here_to_help_you") var we_are_here_to_help_you: String = "we_are_here_to_help_you"
    @ColumnInfo(name = "email_value", defaultValue = "email_value") var email_value: String = "email_value"

    /**
     *  News and events page keys
     * **/
    @ColumnInfo(name = "news_and_events", defaultValue = "news_and_events") var news_and_events: String = "news_and_events"

    /**
     *  Contact us page keys
     * **/
    @ColumnInfo(name = "select_the_topic", defaultValue = "select_the_topic") var select_the_topic: String = "select_the_topic"
    @ColumnInfo(name = "select_topic", defaultValue = "select_topic") var select_topic: String = "select_topic"
    @ColumnInfo(name = "leave_your_message", defaultValue = "leave_your_message") var leave_your_message: String = "leave_your_message"
    @ColumnInfo(name = "contact_information", defaultValue = "contact_information") var contact_information: String = "contact_information"
    @ColumnInfo(name = "contact_us", defaultValue = "contact_us") var contact_us: String = "contact_us"
    @ColumnInfo(name = "let_s_collaborate", defaultValue = "let_s_collaborate") var let_s_collaborate: String = "let_s_collaborate"
    @ColumnInfo(name = "message__", defaultValue = "message__") var message__: String = "message__"
    @ColumnInfo(name = "fill_out_the_form_below", defaultValue = "fill_out_the_form_below") var fill_out_the_form_below: String = "fill_out_the_form_below"
    @ColumnInfo(name = "address_value", defaultValue = "address_value") var address_value: String = "address_value"
    @ColumnInfo(name = "phone_value", defaultValue = "phone_value") var phone_value: String = "phone_value"
    @ColumnInfo(name = "grant_market_help", defaultValue = "grant_market_help") var grant_market_help: String = "grant_market_help"
    @ColumnInfo(name = "full_name__", defaultValue = "full_name__") var full_name__: String = "full_name__"
    @ColumnInfo(name = "leave_message", defaultValue = "leave_message") var leave_message: String = "leave_message"
    @ColumnInfo(name = "select_the_topic_", defaultValue = "select_the_topic_") var select_the_topic_: String = "select_the_topic_"
    @ColumnInfo(name = "products", defaultValue = "products") var products: String = "products"
    @ColumnInfo(name = "technical_support", defaultValue = "technical_support") var technical_support: String = "technical_support"
    @ColumnInfo(name = "collaboration", defaultValue = "collaboration") var collaboration: String = "collaboration"
    @ColumnInfo(name = "advertising_proposal", defaultValue = "advertising_proposal") var advertising_proposal: String = "advertising_proposal"
    @ColumnInfo(name = "review", defaultValue = "review") var review: String = "review"
    @ColumnInfo(name = "comments_and_suggestions", defaultValue = "comments_and_suggestions") var comments_and_suggestions: String = "comments_and_suggestions"

    /**
     *  Settings page keys
     * **/
    @ColumnInfo(name = "settings", defaultValue = "settings") var settings: String = "settings"
    @ColumnInfo(name = "language", defaultValue = "language") var language: String = "language"
    @ColumnInfo(name = "notifications_about_subscriptions", defaultValue = "notifications_about_subscriptions") var notifications_about_subscriptions: String =
        "notifications_about_subscriptions"
    @ColumnInfo(name = "notifications_about__discounts", defaultValue = "notifications_about__discounts") var notifications_about__discounts: String =
        "notifications_about__discounts"
    @ColumnInfo(name = "marketing_notifications", defaultValue = "marketing_notifications") var marketing_notifications: String = "marketing_notifications"
    @ColumnInfo(name = "email_notifications", defaultValue = "email_notifications") var email_notifications: String = "email_notifications"
    @ColumnInfo(name = "push_notifications", defaultValue = "push_notifications") var push_notifications: String = "push_notifications"
    @ColumnInfo(name = "select_the_app_language", defaultValue = "select_the_app_language") var select_the_app_language: String = "select_the_app_language"

    /**
     *  Other keys
     * **/
    @ColumnInfo(name = "brands", defaultValue = "brands") var brands: String = "brands"
    @ColumnInfo(name = "new_products", defaultValue = "new_products") var new_products: String = "new_products"
    @ColumnInfo(name = "last_name", defaultValue = "last_name") var last_name: String = "last_name"
    @ColumnInfo(name = "message", defaultValue = "message") var message: String = "message"
    @ColumnInfo(name = "home_page_1_text", defaultValue = "home_page_1_text") var home_page_1_text: String = "home_page_1_text"
    @ColumnInfo(name = "catalog", defaultValue = "catalog") var catalog: String = "catalog"
    @ColumnInfo(name = "learn_more", defaultValue = "learn_more") var learn_more: String = "learn_more"
    @ColumnInfo(name = "filter_by", defaultValue = "filter_by") var filter_by: String = "filter_by"
    @ColumnInfo(name = "card_list", defaultValue = "card_list") var card_list: String = "card_list"
    @ColumnInfo(name = "home_page_2_text", defaultValue = "home_page_2_text") var home_page_2_text: String = "home_page_2_text"
    @ColumnInfo(name = "copy_right_text", defaultValue = "copy_right_text") var copy_right_text: String = "copy_right_text"
    @ColumnInfo(name = "discounts", defaultValue = "discounts") var discounts: String = "discounts"
    @ColumnInfo(name = "price", defaultValue = "price") var price: String = "price"
    @ColumnInfo(name = "newest", defaultValue = "newest") var newest: String = "newest"
    @ColumnInfo(name = "select_your_display_language", defaultValue = "select_your_display_language") var select_your_display_language: String = "select_your_display_language"
    @ColumnInfo(name = "new_arrivals", defaultValue = "new_arrivals") var new_arrivals: String = "new_arrivals"
    @ColumnInfo(name = "filter_by_price", defaultValue = "filter_by_price") var filter_by_price: String = "filter_by_price"
    @ColumnInfo(name = "name", defaultValue = "name") var name: String = "name"
    @ColumnInfo(name = "bestsellers", defaultValue = "bestsellers") var bestsellers: String = "bestsellers"
    @ColumnInfo(name = "follow_us", defaultValue = "follow_us") var follow_us: String = "follow_us"
    @ColumnInfo(name = "sort_by", defaultValue = "sort_by") var sort_by: String = "sort_by"
    @ColumnInfo(name = "view_all", defaultValue = "view_all") var view_all: String = "view_all"
    @ColumnInfo(name = "company", defaultValue = "company") var company: String = "company"
}