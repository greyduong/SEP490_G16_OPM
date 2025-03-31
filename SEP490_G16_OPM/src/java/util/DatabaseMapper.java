package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PrimaryKey;
import model.Table;

public class DatabaseMapper<T> {

    private final Map<String, Field> fields = new HashMap<>();
    private final Constructor<T> constructor;
    private Field id;
    private String tableName;

    public DatabaseMapper(Class<T> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            // capitalize field name
            fields.put(StringUtil.capitalize(field.getName()), field);
            if (field.getAnnotation(PrimaryKey.class) != null) {
                id = field;
            }
        }
        try {
            // get constructor
            constructor = clazz.getDeclaredConstructor();
            // get table name
            var table = clazz.getAnnotation(Table.class);
            if (table != null) this.tableName = table.value();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public Map<String, Object> getFields(T instance) {
        Map<String, Object> result = new HashMap<>();
        fields.forEach((key, field) -> {
            try {
                Object value = field.get(instance);
                if (value == null) return;
                result.put(key, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DatabaseMapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return result;
    }

    public T fromResultSet(ResultSet rs) throws SQLException {
        try {
            final T instance = constructor.newInstance();
            final ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String colName = meta.getColumnName(i);
                if (fields.containsKey(colName)) {
                    Object value = rs.getObject(i);
                    Field field = fields.get(colName);
                    field.set(instance, value);
                }
            }
            return instance;
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DatabaseMapper.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public String getPrimaryKey() {
        return StringUtil.capitalize(this.id.getName());
    }
    
    public int getPrimaryKey(T instance) {
        try {
            return (int) id.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(DatabaseMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Cannot get id");
    }
    
    public String getTableName() {
        return tableName;
    }
}
