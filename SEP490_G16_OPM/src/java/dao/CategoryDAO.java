package dao;

import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Category;

/**
 * DAO class for Category table operations
 *
 * @author your-name
 */
public class CategoryDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    // 1. Get all categories
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Category";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setName(rs.getString("Name"));
                category.setDescription(rs.getString("Description"));
                list.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    // 2. Get category by ID
    public Category getCategoryById(int id) {
        Category category = null;
        try {
            String sql = "SELECT * FROM Category WHERE CategoryID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                category = new Category();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setName(rs.getString("Name"));
                category.setDescription(rs.getString("Description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return category;
    }

    // 3. Add new category
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO Category (Name, Description) VALUES (?, ?)";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, category.getName());
            stm.setString(2, category.getDescription());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 4. Update category
    public boolean updateCategory(Category category) {
        String sql = "UPDATE Category SET Name = ?, Description = ? WHERE CategoryID = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, category.getName());
            stm.setString(2, category.getDescription());
            stm.setInt(3, category.getCategoryID());
            return stm.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 5. Delete category by ID
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM Category WHERE CategoryID = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    
}
