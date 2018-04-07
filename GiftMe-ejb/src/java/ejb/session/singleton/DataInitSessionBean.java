/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.PromotionControllerLocal;
import ejb.session.stateless.ShopControllerLocal;
import entity.Customer;
import entity.Product;
import entity.Promotion;
import entity.Shop;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.ShopType;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Farhan Angullia
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "GiftMe-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerControllerLocal customerControllerLocal;
    @EJB
    private ProductControllerLocal productControllerLocal;
    @EJB
    private ShopControllerLocal shopControllerLocal;
    @EJB
    private PromotionControllerLocal promotionControllerLocal;

    
   @PostConstruct
  public void postConstruct() {
        try {
            customerControllerLocal.retrieveCustomerByEmail("giftmeadmin@gmail.com");

        } catch (CustomerNotFoundException ex) {
            initializeData();
        }

    }

   
    public void initializeData() {
        try {

            Product product = new Product();
            product.setProductName("Roses");
            product.setCategory("Flowers");
            product.setDescription("Red roses for your loved ones");
            product.setPrice(new BigDecimal("20"));
            product.setQuantityOnHand(50);
            product.setSkuCode("PROD005");
            product.setImgPath("../assets/img/products/PROD005.png");

            Shop shop1 = shopControllerLocal.createShop(new Shop("Kent Ridge Flora", "South West", ShopType.PREMIUM));
            product.setShop(shop1);
            em.persist(product);
            /*     List<Product> products = new ArrayList<>();
            products.add(product);

            shop1.setProducts(products);
            em.merge(shop1);
            
             */

            //      Product product2 = new Product();
            /*    product2.setProductName("Teddy Bear");
            product2.setCategory("Plushies");
            product2.setDescription("Soft and cute teddy bear");
            product2.setPrice(new BigDecimal("6"));
            product2.setQuantityOnHand(40);
            product2.setSkuCode("PROD006");
             */
            Shop shop = shopControllerLocal.createShop(new Shop("PlushRUs Store", "South West", ShopType.NORMAL));
            customerControllerLocal.createNewCustomer(new Customer("admin", "admin", "giftmeadmin@gmail.com", "password", "82222034"));
            Product product2 = productControllerLocal.createProduct(new Product(40, "Teddy Bear", "Soft and cute teddy bear", "Plushies", new BigDecimal("6"), "PROD006", "../assets/img/products/PROD006.png", shop));
            /*   List<Product> products2 = new ArrayList<>();
            products2.add(product2);
            shop.setProducts(products2);
            em.merge(shop);
             */
            
            promotionControllerLocal.createPromotion(new Promotion("5OFF", new BigDecimal("5"),true));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    } 

}
