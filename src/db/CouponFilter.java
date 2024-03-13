package db;

import local.CouponlandSystem;

public enum CouponFilter implements Filter {
    ID(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_ID),
    COMPANY_ID(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_COMPANY_ID),
    CATEGORY_ID(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_CATEGORY_ID),
    TITLE(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_TITLE),
    DESCRIPTION(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_DESCRIPTION),
    START_DATE(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_START_DATE),
    END_DATE(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_END_DATE),
    AMOUNT(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_AMOUNT),
    PRICE(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_PRICE),
    IMAGE(CouponlandSystem.getInstance().getLoadedSqlCommands().FILTER_BY_IMAGE);

    private final String filterCommand;

    CouponFilter(String filterCommand) {
        this.filterCommand = filterCommand;
    }

    /**
     * Returns a single filter, to be used with a base command and others coupon filters - enabling custom coupon searching.
     * @return a String - to be concatenated with a base command.
     */
    public String getFilterCommand() {
        return filterCommand;
    }
}
