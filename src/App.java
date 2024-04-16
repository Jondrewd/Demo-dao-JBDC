import dao.SellerDao;
import daoImplements.DaoFactory;
import entities.Seller;

public class App {
    public static void main(String[] args) throws Exception {
      

        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(2); 
        

        System.out.println(seller);
    }
}
