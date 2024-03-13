package loginmanager;

import facades.AdminFacade;
import facades.ClientFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;

import java.util.function.Supplier;

public enum ClientType {
    ADMINISTRATOR(AdminFacade::new),
    COMPANY(CompanyFacade::new),
    CUSTOMER(CustomerFacade::new);

    private final Supplier<ClientFacade> facadeSupplier;


    ClientType(Supplier<ClientFacade> supplier ) {
        this.facadeSupplier=supplier;

    }

    public ClientFacade getClientFacade() {
        return facadeSupplier.get();
    }

    public Class<? extends ClientFacade> getFacadeClass() {
        return facadeSupplier.get().getClass();
    }

}
