package com.gl.adminportal.stationery.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

import com.gl.adminportal.stationery.dao.EmpFeedbackDao;
import com.gl.adminportal.stationery.model.EmpFeedback;
import com.gl.adminportal.stationery.utils.DateUtils;

@Repository("empFeedbackDao")
@Transactional
public class EmpFeedbackDaoImpl implements EmpFeedbackDao {

	private static final Logger LOGGER = Logger.getLogger(EmpFeedbackDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	public Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	@Override
	public int saveFeedback(EmpFeedback empFeedback, String locationName) {
		int empFeedBackId = 0;
		try {
			LOGGER.debug("Invoking savefeedback ");
			empFeedback.setLocationName(locationName);
			empFeedback.setFeedbackCreationDate(DateUtils.getCurrentDate());
			empFeedback.setFeedbackUpdatedDate(DateUtils.getCurrentDate());
			//empFeedBackId = (int) this.sessionFactory.getCurrentSession().save(empFeedback);
			String result =  (String) this.sessionFactory.getCurrentSession().save(empFeedback);
			LOGGER.debug("Saved feedback successfully!");
		} catch (Exception e) {
			LOGGER.error("Exception while saving item: " + e);
		}
		return Integer.valueOf(empFeedBackId);
	}

	@Override
	public EmpFeedback getEmpFeedbackById(String locationName, String feedbackId) {
		final Session session = getSession();
		EmpFeedback empFeedback = null;
		try {
			Criteria criteria = session.createCriteria(EmpFeedback.class).add(
					Restrictions.eq("id", feedbackId));
			empFeedback = (EmpFeedback) criteria.uniqueResult();
			if (null != empFeedback) {
				LOGGER.info("Feedback list " + empFeedback.toString());
			}
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
		return empFeedback;
	}
	
	@Override
	public List<EmpFeedback> getEmpFeedbackByEmpId(String locationName,
			String empId) {
		final Session session = getSession();
		Query query = session.createQuery("from EmpFeedback e where e.locationName = :locationName and e.empId = :empId");
		query.setParameter("locationName", locationName);
		query.setParameter("empId", empId);
		return query.list();
	}


	@Override
	public List<EmpFeedback> getEmpFeedbackByStatus(String locationName, String status) {
		final Session session = getSession();
		Query query = session.createQuery("from EmpFeedback e where e.locationName = :locationName and e.status = :status");
		query.setParameter("locationName", locationName);
		query.setParameter("status", status);
		return query.list();
	}

	@Override
	public void changeFeedbackStatus(String locationName, String feedbackId,
			String status) {
		final Session session = getSession();
		Transaction transaction = session.beginTransaction();
		EmpFeedback empFeedback = null;
		try {
				Criteria criteria = session.createCriteria(EmpFeedback.class).add(
						Restrictions.eq("id", feedbackId));
				empFeedback = (EmpFeedback) criteria.uniqueResult();
				if (null != empFeedback) {
					LOGGER.info("Emp Feedback " + empFeedback.toString());
					empFeedback.setStatus(status);
					empFeedback.setFeedbackUpdatedDate(DateUtils.getCurrentDate());
					session.update(empFeedback);
					transaction.commit();
				}
			
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
	}

	@Override
	public List<EmpFeedback> getEmpFeedback(String locationName, Integer pageNo, String status) {
		final Session  session = getSession();
		Criteria criteria = session.createCriteria(EmpFeedback.class);
		criteria.add(Restrictions.eq("locationName", locationName));
		if(status != null) {
			criteria.add(Restrictions.eq("status", status));
		}
		return criteria.list();
	}
}
