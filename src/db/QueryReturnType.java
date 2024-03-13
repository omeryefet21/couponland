package db;

import local.CouponlandSystem;

public enum QueryReturnType {
    COMPANY(CouponlandSystem.getInstance().getLoadedSqlCommands().GET_ALL_COMPANIES),
    CUSTOMER(CouponlandSystem.getInstance().getLoadedSqlCommands().GET_ALL_CUSTOMERS),
    COUPON(CouponlandSystem.getInstance().getLoadedSqlCommands().GET_ALL_COUPONS),
    C_VS_C(CouponlandSystem.getInstance().getLoadedSqlCommands().GET_ALL_CVC),
    BOOLEAN(null),
    CATEGORY(null);

    private final String filterBaseCommand;

    QueryReturnType(String filterBaseCommand) {
        this.filterBaseCommand = filterBaseCommand;
    }

    /**
     * @return a String containing a basic search command for the selected type, to be used with the relevant filters.
     */
    public String getFilterCommand() {
        return filterBaseCommand;
    }
}
