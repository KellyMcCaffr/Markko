package bob.markkouserloginsignup;


public class businessAccountDatabaseProducts {
    int _id;
    protected static String _name;
    protected static String _email;
    protected static String _password;
    protected static String _company;
    protected static String _url;
    protected static String _plan;


    public businessAccountDatabaseProducts() {

    }
    public businessAccountDatabaseProducts(String name, String email, String password,String company, String url) {
        this._name = name;
        this._email = email;
        this._password = password;
        this._company=company;
        this._url=url;
    }
    public businessAccountDatabaseProducts(String name, String email, String password,String company, String url,String plan) {
        this._name = name;
        this._email = email;
        this._password = password;
        this._company=company;
        this._url=url;
        this._plan=plan;
    }
    public static void set_name(String _name) {
        businessAccountDatabaseProducts._name = _name;
    }

    public static void set_email(String _email) {
        businessAccountDatabaseProducts._email = _email;
    }

    public static void set_password(String _password) {
        businessAccountDatabaseProducts._password = _password;
    }

    public static void set_company(String _company) {
        businessAccountDatabaseProducts._company = _company;
    }

    public static void set_url(String _url) {
        businessAccountDatabaseProducts._url = _url;
    }

    public static void set_plan(String _plan) {
        businessAccountDatabaseProducts._plan = _plan;
    }

    public static String get_name() {
        return _name;
    }

    public static String get_email() {
        return _email;
    }

    public static String get_password() {
        return _password;
    }

    public static String get_company() {
        return _company;
    }

    public static String get_url() {
        return _url;
    }

    public static String get_plan() {
        return _plan;
    }


}
