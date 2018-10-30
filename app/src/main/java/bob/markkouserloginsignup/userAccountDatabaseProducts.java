package bob.markkouserloginsignup;

public class userAccountDatabaseProducts {
    int _id;
    protected static String _name;
    protected static String _email;
    protected static String _password;
    protected static int _points;

    protected static String _gender;
    protected static String _age;
    protected static String _race;
    protected static String _religion;
    protected static String _educationLevel;


    //An empty constructor so we are not constrained to passing params in
    public userAccountDatabaseProducts(){

    }

    public userAccountDatabaseProducts(String name, String email, String password) {
        this._name = name;
        this._email = email;
        this._password = password;
        this ._points=0;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_email() {
        return _email;
    }

    public String get_password() {
        return _password;
    }

    public int get_points() {
        return _points;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public void set_points(int _points) {
        this._points = _points;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public static void set_gender(String _gender) {
        userAccountDatabaseProducts._gender = _gender;
    }

    public static void set_age(String _age) {
        userAccountDatabaseProducts._age = _age;
    }

    public static void set_race(String _race) {
        userAccountDatabaseProducts._race = _race;
    }

    public static void set_religion(String _religion) {
        userAccountDatabaseProducts._religion = _religion;
    }

    public static void set_educationLevel(String _educationLevel) {
        userAccountDatabaseProducts._educationLevel = _educationLevel;
    }



    public static String get_gender() {
        return _gender;
    }

    public static String get_age() {
        return _age;
    }

    public static String get_race() {
        return _race;
    }

    public static String get_religion() {
        return _religion;
    }

    public static String get_educationLevel() {
        return _educationLevel;
    }
}











