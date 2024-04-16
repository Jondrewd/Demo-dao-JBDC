package daoImplements;

import dao.SellerDao;
import db.DB;

public class DaoFactory {
    public static  SellerDao createSellerDao(){
        return new SellerDaoJBDC(DB.getConnection());
    }
}
