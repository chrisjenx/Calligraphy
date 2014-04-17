package uk.co.chrisjenx.calligraphy;

class ReflectionUtils {

    private ReflectionUtils() { }

    public static Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    public static <T> T getStaticFieldValue(Class klass, String name, T defValue) {
        try {
            return (T) klass.getField(name).get(null);
        } catch (Exception ignored) {
            return defValue;
        }
    }
}
