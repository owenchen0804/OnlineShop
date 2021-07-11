package com.owen.onlineShop.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.owen.onlineShop.entity.Authorities;
import com.owen.onlineShop.entity.Customer;
import com.owen.onlineShop.entity.User;

@Repository //本质是component，实际用在和数据库相关的Dao上，用在Dao上都要用@Repository
public class CustomerDao {

    @Autowired // dao将来生成的时候Spring可以把生成好的sessionFactory给inject进来
    private SessionFactory sessionFactory; //是Hibernate提供的接口，定义在ApplicationConfig，是由Spring创建的
    // 这个sessionFactory可以用来连接数据库，把我们定义的entity map成database里面的table
    // sessionFactory是Singleton是unique的，但是它可以open session是多个的。

    public void addCustomer(Customer customer) { // 写入操作，添加customer
        // authorities算是POJO，简单逻辑，建立完设定ROLE_USER之后就存到database，可以GC，
        // 且没有什么dependency，不需要用injection
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER");
        authorities.setEmailId(customer.getUser().getEmailId());
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities); //目的是完成写入authorities, customer到database对应的表中
            session.save(customer);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback(); //
            // 可能由于网络异常等原因，导致需要update的表不是全都consistently updated，所以在此要回滚避免脏数据
            // 这个rollback()是Framework执行的，保证成功执行
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 这里userName是等价于emailId的，而只有user这个table里面才有emailId, 所以只能通过user来找了。
    public Customer getCustomerByUserName(String userName) { //读取操作，得到customer信息
        // 因为只是读操作，所以不需要rollback,也就是可以try with resource
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            //criteria相当于＋一些限制条件的SQL Query
            Criteria criteria = session.createCriteria(User.class);
            // User.class表明是要search in User table 所以这里的结果一定要转换成User class type
            user = (User) criteria.add(Restrictions.eq("emailId", userName)).uniqueResult();
            // select * from users where emailId = "userName"
            // 这样做的好处是不用写SQL了,而且可能SQL有的是MySQL有的是NoSQL，如果数据库发生迁移也不用改
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null)
            return user.getCustomer();
        return null;

        // 这里属于try with resource 写法，可以自动close，不需要像上面addCustomer那样手动session.close()
    }
}

