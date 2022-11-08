package mx.devapps.utils.components;

public abstract class HConstant {

    protected static ENVIRONMENT BUILD = ENVIRONMENT.DEV;
    public static final String SUPPORT_LINK = "https://devapps.mx/logger?title={1}&detail={2}";
    public static final String SUPPORT_TAG = "devapps";
    public static final String SUPPORT_EMAIL = "soporte@devapps.mx";

    protected static String HOST = "";
    protected static Boolean DEBUG = false;

    protected enum ENVIRONMENT {
        DEV,
        QA,
        PRD
    }

}
