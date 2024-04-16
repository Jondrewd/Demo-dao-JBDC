package daoImplements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dao.SellerDao;
import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;

public class SellerDaoJBDC implements SellerDao{

    private Connection conn;

    public SellerDaoJBDC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Seller obj) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void deleteById(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st= conn.prepareStatement(
                """
                SELECT seller.*,department.Name as DepName
                FROM seller INNER JOIN department
                ON seller.DepartmentId = department.id
                WHERE seller.Id = ?
                """
            );

            st.setInt(1, id);
            rs = st.executeQuery();
            
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
               
                Seller obj = instantiateSeller(rs, dep);
                    return obj;
            }
            return null;
        }
        catch(SQLException e){
            throw new DbException("Erro.");
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
       Seller seller = new Seller(
            rs.getInt("Id"), 
            rs.getString("Name"), 
            rs.getString("Email"), 
            rs.getDate("BirthDate"), 
            rs.getDouble("BaseSalary"), 
            dep);
        return seller;
    
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department(
            rs.getInt("DepartmentId"), 
            rs.getString("DepName")
            );
        return dep;
    }

    @Override
    public List<Seller> findAll() {

        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
    
}
