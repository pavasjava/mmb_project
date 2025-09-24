package mmb.config;

public class AppConstants {

//	public static final String[] ADMIN_URL = {"/admin","/updateRole",};
	public static final String[] ADMIN_URL = { "/login", "/dashboard", "/articles", "/home", "/register",
			"/registerreq", "/loginreq", "/showImages", "/images/**", "/getAllRawMaterials", "/addNewRawMaterial",
			"/saveRawMaterial", "/editRawMaterial/**", "/deleteRawMaterial/**", "/quotations/**", "/generateBills/**",
			"/api/login", "/api/users/register", "/api/resGenerateBills", "/api/resGenerateBills/**", "api/quotations/**","/api/updateUserNameAndPwd" };
	public static final String[] BASE_API_URL = {"/login","/api/login","/index","/register", "/api/register","/duplicateEmail/**","/loginreq","/updateCredentials","/updateUserNameAndPwd",
			"/api/updatePassword", "/api/pwdValidation/**", "/images/**","frontend/dashboard.html","/bookings/**","/borewellTypes/**"};
//    	{"/login","/dashboard","/articles","/home","/register","/registerreq","/loginreq","/showImages","/images/**","/getAllRawMaterials",
//    		"/addNewRawMaterial","/saveRawMaterial","/editRawMaterial/**","/deleteRawMaterial/**","/quotations/**","/generateBills/**","/api/users/login","/api/users/register","/api/resGenerateBills/**"};

}
