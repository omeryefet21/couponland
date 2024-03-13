package db;

import local.CouponlandSystem;

public enum CVCFilter implements Filter{
    CUSTOMER_ID(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_CUSTOMER_ID),
    COUPON_ID(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_COUPON_ID);
    private final String filterCommand;

    CVCFilter(String filterCommand) {
        this.filterCommand = filterCommand;
    }

    /**
     * Returns a single filter, to be used with a base command and others CVC filters - enabling custom purchase searching.
     * @return a String - to be concatenated with a base command.
     */
    @Override
    public String getFilterCommand() {
        return filterCommand;
    }
}
