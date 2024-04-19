package daoImplements;

import dao.DepartmentDao;
import dao.SellerDao;
import db.DB;

public class DaoFactory {
    public static  SellerDao createSellerDao(){
        return new SellerDaoJBDC(DB.getConnection());
    }
    public static  DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJBDC(DB.getConnection());
    }
}
