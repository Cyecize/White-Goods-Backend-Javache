package com.cyecize.app.constants;

public class Endpoints {

    //@formatter:off
    public static final String CATEGORIES                   = "/categories";
    public static final String CATEGORY                     = "/category/{id}";

    public static final String PROD_FE                      = "/prod/{id}";
    public static final String PRODUCTS                     = "/products";
    public static final String PRODUCT                      = "/products/{id}";
    public static final String PRODUCT_CREATE               = "/product/create";
    public static final String PRODUCT_GALLERY_ITEMS        = "/product/{id}/gallery_items";
    public static final String PRODUCT_GALLERY_ITEM         = "/product/{id}/gallery_items/{imageId}";
    public static final String PRODUCTS_SELECTIONS          = "/products/selections";
    public static final String PRODUCTS_SELECTION           = "/products/selections/{id}";

    public static final String SHOPPING_CART_SESSION        = "/shopping-cart/session";
    public static final String SHOPPING_CART                = "/shopping-cart/{session}";
    public static final String SHOPPING_CART_ITEM           = "/shopping-cart/{session}/items/{prodId}";
    public static final String SHOPPING_CART_PRICING        = "/shopping-cart/{session}/pricing";
    public static final String SHOPPING_CART_COUPON_CODE    = "/shopping-cart/{session}/coupon-code";

    public static final String ORDERS_ANON                  = "/orders-anon";
    public static final String ORDERS                       = "/orders";
    public static final String ORDER                        = "/orders/{id}";
    public static final String ORDERS_STATUS                = "/orders/status";
    public static final String ORDERS_SEARCH                = "/orders/search";

    public static final String HOME_CAROUSEL                = "/home-carousel";
    public static final String HOME_CAROUSEL_ITEM           = "/home-carousel/{id}";

    public static final String QUESTION                     = "/question";
    public static final String CONTACT_INFO                 = "/contact-info";

    public static final String SPECIFICATION_TYPES          = "/specification_types";
    public static final String SPECIFICATION_TYPE           = "/specification_types/{typeId}";
    public static final String SPECIFICATION_TYPES_SEARCH   = "/specification_types/search";
    public static final String SPECIFICATION_CATEGORY       = "/specification_types/{specTypeId}/category/{catId}";
    public static final String PRODUCT_SPECS_SEARCH         = "/product_specifications/search";
    public static final String PRODUCT_SPECIFICATION        = "/product_specifications/{id}";

    public static final String LOGIN                        = "/login";
    public static final String LOGOUT                       = "/logout";
    public static final String RECOVERY_EMAIL               = "/recovery-email";
    public static final String PASSWORD_RESET               = "/password-reset";

    public static final String USER_DETAILS                 = "/user-details";

    public static final String REGISTER                     = "/register";
    public static final String CHANGE_PASSWORD              = "/change-password";
    public static final String DELETE_ACCOUNT               = "/delete-account";

    public static final String USER_ADDRESSES               = "/user-addresses";
    public static final String USER_ADDRESS                 = "/user-addresses/{id}";

    public static final String WAREHOUSE_ITEM               = "/warehouse/items/{id}";
    public static final String WAREHOUSE_DELIVERIES         = "/warehouse/deliveries";
    public static final String WAREHOUSE_DELIVERY           = "/warehouse/deliveries/{id}";
    public static final String WAREHOUSE_REVISIONS          = "/warehouse/revisions";
    public static final String WAREHOUSE_REVISION           = "/warehouse/revisions/{id}";

    public static final String SEO_SITEMAP                  = "/seo/sitemap.xml";

    public static final String PROCESS_VISITORS             = "/process-visitors";
    public static final String VISITORS_MONTH               = "/visitors/{year}/{month}";

    public static final String PROMOTIONS_SEARCH            = "/promotions/search";
    public static final String PROMOTIONS                   = "/promotions";
    public static final String PROMOTION                    = "/promotions/{id}";

    public static final String COUPON_CODES_SEARCH          = "/coupon-codes/search";
    public static final String COUPON_CODES                 = "/coupon-codes";
    public static final String COUPON_CODE                  = "/coupon-code/{code}";
    public static final String COUPON_CODE_STATISTICS       = "/coupon-code/{code}/statistics";
}
