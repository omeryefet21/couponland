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
