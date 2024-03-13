package job;

import dao.CouponsDAO;
import dbdao.CouponsDBDAO;
import exceptions.CLLogicException;

public class CouponExpirationDailyJob implements Runnable {
    private CouponsDAO couponsDAO;
    private Boolean quit;

    public CouponExpirationDailyJob() {
        setCouponsDAO(new CouponsDBDAO());
        setQuit(false);
    }

    public void setCouponsDAO(CouponsDAO couponsDAO) {
        this.couponsDAO = couponsDAO;
    }

    public void setQuit(Boolean quit) {
        this.quit = quit;
    }

    @Override
    public void run() {
        while (!quit) {
            try {
                couponsDAO.deleteExpiredCoupons();
                Thread.sleep(24 * 60 * 60 * 1000);
            }
            catch (CLLogicException | InterruptedException exception) {
                System.out.println(exception.getMessage());
            }
        }

    }

    public void stop() {
        setQuit(true);
    }
}
