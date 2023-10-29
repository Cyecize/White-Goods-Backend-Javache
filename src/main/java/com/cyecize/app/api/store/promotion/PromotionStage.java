package com.cyecize.app.api.store.promotion;

/**
 * Define at which point should a promotion be applied
 */
public enum PromotionStage {
    // Regular is considered at the point where the order "total" is formed.
    REGULAR,

    // Additional is after the "total" is formed, there may be a need to apply an additional discount
    ADDITIONAL
}
