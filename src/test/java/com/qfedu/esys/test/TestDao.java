package com.qfedu.esys.test;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qfedu.esys.dao.IRoleDao;
import com.qfedu.esys.entity.Dictionary;
import com.qfedu.esys.entity.Role;
import com.qfedu.esys.service.IDictionaryService;

@RunWith(SpringJUnit4ClassRunner.class) // 表示整合JUnit4进行测试
@ContextConfiguration(locations = { "classpath:applicationContext.xml" }) // 加载spring配置文件
public class TestDao {

	@Resource
	private IRoleDao roleDao;

	@Resource
	private SessionFactory sessionFactory;

	private final static Logger LOG = LogManager.getLogger(TestDao.class);

	@Test
	@Transactional
	@Rollback
	public void test00() {
		Role r = new Role();
		r.setName("test01");
		r.setDescription("test");
		roleDao.create(r);
		Role r2 = roleDao.findById(r.getId());
		Assert.assertEquals(false, r2 == null);
		Assert.assertEquals("test01", r2.getName());
	}

	@Test
	// @Transactional
	// @Rollback(false)
	public void test01() {
		Role r = new Role();
		r.setName("test01");
		r.setDescription("test");
		Session s = sessionFactory.openSession();
		// roleDao.create(r);
		Transaction t = s.beginTransaction();
		s.persist(r);
		LOG.info("persist1...");
		// sessionFactory.getCurrentSession().close();
		t.commit();
		s.close();
	}

	@Resource
	private IDictionaryService dictionaryService;

	@Test
	// 级联
	public void test02() {
		Dictionary dto = new Dictionary();
		dto.setId("4");
		dto.setDicType("sex");
		dto.setDescription("nv1---");
		dto.setVal("2");
		dto.setName("女");
		dictionaryService.updatePO(dto);
	}

	@Test
	// 查询缓存
	public void test03() {
		// 开启第一个session
		Session s = sessionFactory.openSession();
		LOG.info("------------------" + s);
		s.createQuery("from Dictionary d where d.dicType = 'sex'", Dictionary.class).setCacheable(true).list();
		LOG.info("Query - session1-1 ...");
		s.createQuery("from Dictionary d where d.dicType = 'sex'", Dictionary.class).setCacheable(true).list();
		// roleDao.create(r);
		LOG.info("Query - session1-2 ...");
		// sessionFactory.getCurrentSession().close();
		s.close();
		// 开启第二个session
		Session s2 = sessionFactory.openSession();
		LOG.info("------------------" + s2);
		s2.createQuery("from Dictionary d where d.dicType = 'sex'", Dictionary.class).setCacheable(true).list();
		// roleDao.create(r);
		LOG.info("Query...");
		// sessionFactory.getCurrentSession().close();
		s2.close();
	}

	@Test
	// 一二级缓存
	public void test04() {
		// 开启第一个session
		Session s = sessionFactory.openSession();
		s.get(Dictionary.class, "1");
		// 此处使用了一级缓存
		s.get(Dictionary.class, "1");
		LOG.info("Query...");
		s.close();
		// 开启第二个session
		s = sessionFactory.openSession();
		s.get(Dictionary.class, "1");
		// 此处使用了一级缓存
		s.get(Dictionary.class, "1");
		LOG.info("Query...");
		s.close();
	}

}
