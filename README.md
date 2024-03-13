Welcome to Couponland!

1.	Initialize CouponlandSystem.
	CouponlandSystem.initializeCouponlandSystem();

2.	Create a new installation and note the returned installation id:
	CouponlandSystem.createAndSaveInstallation(new String[]{DBUrl,DBUsername,DBPassword,DBSchemaName});

3.	Load an installation and run using your installation ID:
	CouponlandSystem.getInstance().loadInstallationAndRun(installationId);

4.	Log in:
	login(email, password, clientType);



ADMIN_EMAIL = "admin@admin.com";
ADMIN_PASSWORD = "admin";

CLLogicException

"Client is already logged in", 121

""Incorrect username and/or password", 101

"Coupon not found", 141

"Company not found",143

"Customer "+customer.getId()+" not found", 146

"No customers found", 148

"No coupons found for selected company",142

"No coupons found for selected customer", 147

"No companies found",144

"No coupons found",145

"Coupon title: " + coupon.getTitle() + " already exists for company "+coupon.getCompanyID(), 151

"Purchase not found",181

"Client id is already set", 191

"Illegal operation - coupon not owned by company",221

"Illegal operation - Can not update company name", 251

"Illegal operation - Can not update a coupon's company id", 271

"Illegal operation - can not delete a logged-in company",261

"Illegal operation - can not delete a logged-in customer", 262

"Company email: "+company.getEmail()+" already exist",311

"Coupon "+coupon.getId()+" was already purchased by customer "+getCustomerID(),351

"Coupon " + coupon.getId() + " is out of stock", 352

"Coupon " + coupon.getId() + " is inactive", 353

"Coupon " + coupon.getId() + " has expired", 354

"Company name: "+company.getName()+" already exist",312

"Couponland system not initialized",451

"Installation creation failed",452

"I/O exception: "+ioException.getMessage(),453

"Serialization error: "+classNotFoundException.getMessage(),454

"Installation "+id+" not found",455

"Server not responding - check db url",456

"Access denied - invalid db username and/or password",457

"Connection pool not initialized",481

"Operation interrupted - " + exception.getMessage(), 498

"Unknown sql error: "+exception.getMessage(),499


CLInputException

"rs to list error",999

"rs to list error-null/s sent",899

"Invalid id value - not in range",701

"Invalid email length and/or pattern",702

"Invalid company name length and/or pattern",703

"Invalid first name length and/or pattern",704

"Invalid last name length and/or pattern",705

"Invalid password length",706

"Invalid coupon title length and/or pattern",707

"Invalid coupon description length and/or pattern",708

"Invalid coupon image(path) length and/or pattern", 709

"Invalid category name length and/or pattern",710

"Invalid amount value - not in range", 711

"Invalid price value - not in range", 712

"Invalid start date value", 721

"Invalid end date value", 722

"Invalid db username length and/or pattern", 731

"Invalid db password length and/or pattern", 732

"Invalid db URL length and/or pattern", 733

"Invalid db schema name length and/or pattern", 734

"Invalid category - does not exist on database", 735
