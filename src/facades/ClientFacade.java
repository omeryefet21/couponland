package facades;

import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import dbdao.CompaniesDBDAO;
import dbdao.CouponsDBDAO;
import dbdao.CustomersDBDAO;
import exceptions.CLInputException;
import exceptions.CLLogicException;


public abstract class ClientFacade {
    protected CompaniesDAO companiesDAO=new CompaniesDBDAO();
    protected CustomersDAO customersDAO=new CustomersDBDAO();
    protected CouponsDAO couponsDAO= new CouponsDBDAO();

    public abstract Boolean login(String email, String password) throws CLInputException, CLLogicException;

    public abstract void setClientId(String email, String password) throws CLInputException, CLLogicException;
    public abstract Integer getClientId();


}
