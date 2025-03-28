package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassConvertor<T> {

    private final Map<String, Field> fields = new HashMap<>();
    private final Constructor<T> constructor;

    public ClassConvertor(Class<T> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fields.put(field.getName().toLowerCase(), field);
        }
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public T fromResultSet(ResultSet rs) throws SQLException {
        try {
            final T instance = constructor.newInstance();
            final ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String colName = meta.getColumnName(i).toLowerCase();
                if (fields.containsKey(colName)) {
                    Object value = rs.getObject(i);
                    Field field = fields.get(colName);
                    if (field.getType().isEnum()) {
                        Method valueOf = field.getType().getMethod("valueOf", String.class);
                        String enumName = ((String) value).toUpperCase();
                        Object enumValue = valueOf.invoke(null, enumName);
                        field.set(instance, enumValue);
                    } else {
                        field.set(instance, value);
                    }

                }
            }
            return instance;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ClassConvertor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
}
