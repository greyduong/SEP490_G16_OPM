package dal;

import model.Farm;

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
}
