package dao;

import dal.DBContext;
import java.util.List;
import java.util.UUID;
import model.Category;
import org.junit.Test;

public class CategoryDAOTest {
    /**
     * Test of getAllCategories method, of class CategoryDAO.
     */
    @Test
    public void testGetAllCategories() {
        CategoryDAO db = new CategoryDAO();
        List<Category> data = db.getAllCategories();
        assert !data.isEmpty();
        data.forEach(System.out::println);
    }
    
    /**
     * Test of testGetCategoryById method, of class CategoryDAO.
     */
    @Test
    public void testGetCategoryById() {
        CategoryDAO db = new CategoryDAO();
        Category category = db.getCategoryById(1);
        assert category != null;
        System.out.println(category);
    }
    
    @Test
    public void testCreateCategory() {
        // Setup
        CategoryDAO dao = new CategoryDAO();
        Category category = new Category();
        String name = UUID.randomUUID().toString();
        category.setName(name);
        category.setDescription(UUID.randomUUID().toString());
        // Execute
        dao.addCategory(category);
        // Assert
        DBContext db = new DBContext();
        Category found = db.fetchOne((rs) -> {
            Category obj = new Category();
            obj.setName(rs.getString("Name"));
            return obj;
        }, "SELECT * FROM Category WHERE Name = ?", name);
        assert found != null;
        System.out.println(found);
        // Clean up
        db.delete("DELETE FROM Category WHERE Name = ?", name);
    }
}
