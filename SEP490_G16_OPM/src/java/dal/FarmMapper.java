package dal;

import model.Farm;
import model.User;

public class FarmMapper {

    public static Mapper<Farm> toFarm() {
        return (rs) -> {
            Farm farm = new Farm();
            farm.setCreatedAt(rs.getTimestamp("CreatedAt"));
            farm.setDescription(rs.getString("Description"));
            farm.setFarmID(rs.getInt("FarmID"));
            farm.setFarmName(rs.getString("FarmName"));
            farm.setLocation(rs.getString("Location"));
            farm.setSellerID(rs.getInt("SellerID"));
            farm.setStatus(rs.getString("Status"));
            return farm;
        };
    }

    public static Mapper<Farm> toFarmWithSeller() {
        return (rs) -> {
            Farm farm = toFarm().fromResultSet(rs);
            User seller = new User();
            seller.setUsername(rs.getString("SellerName"));
            farm.setSeller(seller);
            return farm;
        };
    }
}
