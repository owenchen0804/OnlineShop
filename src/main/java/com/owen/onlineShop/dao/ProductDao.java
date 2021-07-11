package com.owen.onlineShop.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.owen.onlineShop.entity.Product;

@Repository
public class ProductDao {

    @Autowired
    private SessionFactory sessionFactory; // 通过Bean inject进来

    public void addProduct(Product product) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(product);  // 添加商品之后要在数据库保存
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteProduct(int productId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Product product = session.get(Product.class, productId);
            session.delete(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    public void updateProduct(Product product) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    public Product getProductById(int productId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Product.class, productId); // .class就是转成Product object, 不需要再强制转化类型
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        try (Session session = sessionFactory.openSession()) {
            products = session.createCriteria(Product.class).list();
            // 把要访问的表的类型加上，也就是Product.class，这里没有任何搜索条件，所以：
            // createCriteria表示要搜索这个表，返回的应该是整个和product相关的list
            // 上个method里面的get()方法只是单个object，无法执行后面的.list()操作
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}
