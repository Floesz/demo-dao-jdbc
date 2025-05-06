package app;

import model.dao.DAOFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SellerDAO sellerDAO = DAOFactory.createSellerDAO();

         System.out.println("=== TEST 1: seller findById =====");
          Seller seller = sellerDAO.findById(3);
          System.out.println(seller);

         System.out.println("\n=== TEST 2: seller findByDepartment =====");
          Department department = new Department(2, null);
          List<Seller> list = sellerDAO.findByDepartment(department);
           for (Seller obj : list) {
                  System.out.println(obj);
               }

       System.out.println("\n=== TEST 3: seller findAll =====");
        list = sellerDAO.findAll();
        for (Seller obj : list) {
            System.out.println(obj);
        }

         System.out.println("TEST INSERT");
          Seller newSeller = new Seller(null,"Greg","greg@gmail.com",new Date(),4000.0,department);
         sellerDAO.insert(newSeller);

        System.out.println("TEST UPDATE");
        seller = sellerDAO.findById(14);
        seller.setName("Martha Waine");
        sellerDAO.update(seller);
        System.out.println("Updated!");

        System.out.println("TEST DELETE");
        System.out.println("Enter id for delete test: ");
        int id = sc.nextInt();
        sellerDAO.deleteById(id);
        System.out.println("Deleted!");
    }
}
