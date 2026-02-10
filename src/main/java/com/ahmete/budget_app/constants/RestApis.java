package com.ahmete.budget_app.constants;

public final class RestApis {
	private RestApis() {}
	
	public static final String API = "/api/v1";
	
	public static final class User {
		private User() {}
		public static final String ROOT = API + "/users";
		public static final String BY_ID = "/{id}";
	}
	
	public static final class Budget {
		private Budget() {}
		public static final String ROOT = API + "/budgets";
		public static final String ACTIVE = "/active";
	}
	
	public static final class Expense {
		private Expense() {}
		public static final String ROOT = API + "/expenses";
		public static final String TOTAL = "/total";
	}
	
	public static final class Alert {
		private Alert() {}
		public static final String ROOT = API + "/alerts";
	}
	public static final class Common {
		private Common() {}
		public static final String BY_ID = "/{id}";
	}
	
}