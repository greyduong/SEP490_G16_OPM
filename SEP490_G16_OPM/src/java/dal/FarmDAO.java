package dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Farm;
import model.Page;
import model.constant.FarmStatus;

public class FarmDAO extends DBContext {

    /**
     * Fetch farms data
     * @param pageNumber page number (default 1)
     * @param pageSize page size (default 10)
     * @return 
     */
    public Page<Farm> getAllFarm(int pageNumber, int pageSize) {
        String query = "SELECT * FROM Farm ORDER BY FarmID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        Page<Farm> page = new Page<>();
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            statement.setInt(index++, (pageNumber - 1) * pageSize);
            statement.setInt(index++, pageSize);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Farm farm = new Farm();
                    farm.setFarmID(result.getInt("FarmID"));
                    farm.setCreatedAt(result.getTimestamp("CreatedAt"));
                    farm.setDescription(result.getString("Description"));
                    farm.setFarmName(result.getString("FarmName"));
                    farm.setLocation(result.getString("Location"));
                    farm.setSellerID(result.getInt("SellerID"));
                    farm.setStatus(FarmStatus.valueOf(result.getString("Status").toUpperCase()));
                    page.getData().add(farm);
                }
            }
            if (!page.getData().isEmpty()) {
                page.setPageNumber(pageNumber);
            }
            return page;
        } catch (SQLException ex) {
            Logger.getLogger(FarmDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Farm getFarm(int farmId) {
        String query = "SELECT * FROM Farm WHERE FarmID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            statement.setInt(index++, farmId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Farm farm = new Farm();
                    farm.setFarmID(result.getInt("FarmID"));
                    farm.setCreatedAt(result.getTimestamp("CreatedAt"));
                    farm.setDescription(result.getString("Description"));
                    farm.setFarmName(result.getString("FarmName"));
                    farm.setLocation(result.getString("Location"));
                    farm.setSellerID(result.getInt("SellerID"));
                    farm.setStatus(FarmStatus.valueOf(result.getString("Status").toUpperCase()));
                    return farm;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FarmDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
