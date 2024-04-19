package daoImplements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.DepartmentDao;
import db.DB;
import db.DbException;
import entities.Department;

public class DepartmentDaoJBDC implements DepartmentDao{

    private Connection conn;

    public DepartmentDaoJBDC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                """     
                INSERT INTO Department(Id, Name)
                VALUES
                (?,?)
                """, java.sql.Statement.RETURN_GENERATED_KEYS ); 

            st.setInt(1, obj.getId());
            st.setString(2, obj.getName());

            int rowsAffected = st.executeUpdate();
            
            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("Erro inesperado!");
            }
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                """     
                UPDATE department
                SET Name = ?
                WHERE Id = ?
                """); 

            st.setString(1, obj.getName());
            st.executeUpdate();
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM department WHERE id = ?");

            st.setInt(1, id);

            int rows = st.executeUpdate();
            if (rows == 0){
                throw new DbException("Id inexistente");
            }
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st= conn.prepareStatement(
                """
                    SELECT  * FROM department WHERE Id = ?
                """
            );

            st.setInt(1, id);
            rs = st.executeQuery();
            
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
               
                return dep;
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

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement(
                """
                SELECT department.*
                FROM seller INNER JOIN department
                ON departmentId = department.id
                ORDER BY department.id;
                """
            );
            rs = st.executeQuery();
 
            List<Department> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(rs.next()){
                Department dep = map.get(rs.getInt("department.id"));
            if (dep== null) {    
                dep = instantiateDepartment(rs);
                map.put(rs.getInt("department.id"), dep);
                list.add(dep);
            }}    
            return list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department(
            rs.getInt("department.id"), 
            rs.getString("Name")
            );
        return dep;
    }

}
