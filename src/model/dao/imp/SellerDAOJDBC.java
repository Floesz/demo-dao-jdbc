package model.dao.imp;

import db.DB;
import exception.DbExec;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDAOJDBC implements SellerDAO {

    private Connection conn;

    public SellerDAOJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1,seller.getName());
            st.setString(2,seller.getEmail());
            st.setDate(3,new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4,seller.getBaseSalary());
            st.setInt(5,seller.getDepartment().getId());

            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbExec("Unexpected error! No rows affected!");
            }

        }catch (SQLException e){
            throw new DbExec(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE seller "
                            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            + "WHERE Id = ?");

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbExec(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

            st.setInt(1,id);

            st.executeUpdate();
        }catch (SQLException e){
            throw new DbExec(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try{
            stmt = conn.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE seller.Id = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()){

                Department dept = instantiateDepartment(rs);
                Seller obj = instantiateSeller(rs,dept);
                return obj;

            }return null;
        } catch (SQLException e) {
            throw new DbExec("Erro ao procurar o vendedor pelo ID!");
        }
        finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs,Department dept) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setDepartment(dept);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setId(rs.getInt("DepartmentId"));
        dept.setName(rs.getString("DepName"));
        return dept;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName" +
                    "FROM seller INNER JOIN department" +
                    "ON seller.DepartmentId = department.Id" +
                    "ORDER BY Name");

            rs= st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer,Department> map = new HashMap<>();
            while(rs.next()){

                Department dep = map.get(rs.getInt("DepartmentId"));
                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"),dep);
                }
                Seller seller = instantiateSeller(rs,dep);
                list.add(seller);

            }return list;
        } catch (SQLException e) {
            throw new DbExec(e.getMessage());

        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName" +
                    "FROM seller INNER JOIN department" +
                    "ON seller.DepartmentId = department.Id" +
                    "WHERE DepartmentId = ?" +
                    "ORDER BY Name");

            st.setInt(1,department.getId());
            rs= st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer,Department> map = new HashMap<>();
            while(rs.next()){

                Department dep = map.get(rs.getInt("DepartmentId"));
                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"),dep);
                }
                Seller seller = instantiateSeller(rs,dep);
                list.add(seller);

            }return list;
        } catch (SQLException e) {
            throw new DbExec(e.getMessage());

        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }
}
