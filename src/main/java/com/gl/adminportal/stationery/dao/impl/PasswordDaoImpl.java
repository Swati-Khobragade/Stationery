package com.gl.adminportal.stationery.dao.impl;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gl.adminportal.stationery.controller.AuthenticationController;
import com.gl.adminportal.stationery.dao.PasswordDao;
import com.gl.adminportal.stationery.dao.RequestDao;
import com.gl.adminportal.stationery.enums.USERROLE;
import com.gl.adminportal.stationery.model.AdminSupportPassword;
import com.gl.adminportal.stationery.request.ResetPasswordRequest;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Repository("PasswordDaos")
@Transactional
public class PasswordDaoImpl implements PasswordDao {

	private static final Logger LOGGER = Logger
			.getLogger(AuthenticationController.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RequestDao requestDao;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	public Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	@Override
	public boolean validateAdminSupport(String userName, String password,
			String location) {
		final Session session = getSession();
		AdminSupportPassword adminSupPass = null;
		if (userName.equalsIgnoreCase("admin")) {
			userName = USERROLE.ADMINISTRATION.toString();
		} else {
			userName = USERROLE.SUPPORT.toString();
		}
		try {
			Query query = session
					.createQuery("from AdminSupportPassword a where a.location = :location and a.username = :username and a.password = :password");
			String encodedPass = Base64.encode(password.getBytes());
			query.setParameter("location", location);
			query.setParameter("username", userName);
			query.setParameter("password", encodedPass);
			adminSupPass = (AdminSupportPassword) query.uniqueResult();
			LOGGER.info("Admin support password " + adminSupPass);
		} catch (Exception e) {
			LOGGER.error("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
		return adminSupPass == null ? false : true;
	}

	@Override
	public void resetPassword(ResetPasswordRequest resetpasswrdReq,
			String locationName) {
		LOGGER.debug("Invoking reset Password ");
		final Session session = getSession();
		Transaction transaction = session.beginTransaction();

		AdminSupportPassword AdminSupportPass = null;
		try {
			Criteria criteria = session
					.createCriteria(AdminSupportPassword.class)
					.add(Restrictions.eq("username", resetpasswrdReq.getRole()))
					.add(Restrictions.eq("location", locationName));
			AdminSupportPass = (AdminSupportPassword) criteria.uniqueResult();
			if (null != AdminSupportPass) {
				AdminSupportPass.setPassword(resetpasswrdReq.getNewPassword());
				session.update(AdminSupportPass);
				transaction.commit();
			}
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}

	}

	@Override
	public AdminSupportPassword getOldPassword(String role, String locationName) {
		LOGGER.debug("Invoking get old Password ");
		final Session session = getSession();
		AdminSupportPassword AdminSupportPass = null;
		try {
			Criteria criteria = session.createCriteria(AdminSupportPassword.class)
					.add(Restrictions.eq("username", role))
					.add(Restrictions.eq("location", locationName));
			AdminSupportPass = (AdminSupportPassword) criteria.uniqueResult();
			
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
		return AdminSupportPass;
	}

}
