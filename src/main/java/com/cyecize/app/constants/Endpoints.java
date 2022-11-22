package com.cyecize.app.constants;

public class Endpoints {
    //@formatter:off
    public static final String CATEGORIES                   = "/categories";
    public static final String CATEGORY                     = "/category/{id}";

    public static final String PRODUCTS                     = "/products";
    public static final String PRODUCT                      = "/products/{id}";
    public static final String PRODUCT_CREATE               = "/product/create";
    public static final String PRODUCT_GALLERY_ITEMS        = "/product/{id}/gallery_items";
    public static final String PRODUCT_GALLERY_ITEM         = "/product/{id}/gallery_items/{imageId}";

    public static final String SHOPPING_CART_SESSION        = "/shopping-cart/session";
    public static final String SHOPPING_CART                = "/shopping-cart/{session}";
    public static final String SHOPPING_CART_ITEM           = "/shopping-cart/{session}/items/{prodId}";
    public static final String SHOPPING_CART_PRICING        = "/shopping-cart/{session}/pricing";

    public static final String ORDERS_ANON                  = "/orders-anon";
    public static final String ORDERS                       = "/orders";

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

    public static final String USER_DETAILS                 = "/user-details";
}
