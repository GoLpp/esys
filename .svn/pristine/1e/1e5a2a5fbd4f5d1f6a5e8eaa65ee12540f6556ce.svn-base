package com.qfedu.esys.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qfedu.esys.entity.Role;
import com.qfedu.esys.entity.User;

@RunWith(SpringJUnit4ClassRunner.class) // 表示整合JUnit4进行测试
@ContextConfiguration(locations = { "classpath:applicationContext.xml" }) // 加载spring配置文件
public class TestHibernate {

	@Resource
	private SessionFactory sessionFactory;

	private final static Logger LOG = LogManager.getLogger(TestHibernate.class);

	@Test
	public void test01() {
		// 创建角色实体
		Role r = new Role();
		r.setName("cascade-test");
		r.setDescription("cascade-test");
		// 创建用户实体
		User u = new User ();
		u.setLoginName("cascade-test");
		// 设置用户和角色的关系数据
		List<Role> roles = new ArrayList<Role>();
		roles.add(r);
		u.setRoles(roles);
		// 开启一个session
		Session s = sessionFactory.openSession();
		// 启动事务
		Transaction t = s.beginTransaction();
		// 插入用户实体数据
		s.persist(u);
		LOG.info("persist1...");
		// 提交事务
		t.commit();
		// 关闭session
		s.close();
	}

	@Test
	public void test02() {
		Session s = sessionFactory.openSession();
		Transaction t = s.beginTransaction();
		User u = s.get(User.class, "8a873abe61ff601f0161ff60252c0000");
		s.delete(u);
		LOG.info("delete...");
		// sessionFactory.getCurrentSession().close();
		t.commit();
		s.close();
	}
}
