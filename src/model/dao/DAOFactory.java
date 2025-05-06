package model.dao;

import db.DB;
import model.dao.imp.DepartmentDAOJDBC;
import model.dao.imp.SellerDAOJDBC;

public class DAOFactory {

    public static SellerDAO createSellerDAO(){

        return new SellerDAOJDBC(DB.getConnection());
    }

    public static DepartmentDAO createDepartmentDAO(){
        return new DepartmentDAOJDBC(DB.getConnection());
    }
}
