package com.gl.adminportal.stationery.dao.impl;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.adminportal.stationery.dao.ItemDao;
import com.gl.adminportal.stationery.dao.ReportDao;
import com.gl.adminportal.stationery.dao.RequestDao;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.utils.DateUtils;

@Component("reportDaoImpl")
public class ReportDaoImpl implements ReportDao {
	
	private static final Logger LOGGER = Logger.getLogger(ReportDaoImpl.class);

	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private RequestDao requestDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	public Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	@Override
	public List<Item> geItemsListbyLocations(String locationName) {
		List<Item> allItems = itemDao.getAllItems(locationName, "");
		return allItems;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Request> getRequestByFilters(String empName, String startDate, String endDate) throws ParseException {
		LOGGER.info("Entered into RequestedItemDaoImpl - getRequestByFilters()");
		final Session session = getSession();
		
		Criteria criteria = session.createCriteria(Request.class);
		if(null != empName){
			criteria.add(Restrictions.like("employeeName", empName, MatchMode.ANYWHERE));
		}
		if(null != startDate){
			criteria.add(Restrictions.ge("request_creation_date", DateUtils.getDateFromString(startDate)));
		}
		if(null != endDate){
			criteria.add(Restrictions.le("request_creation_date", DateUtils.getDateFromString(endDate)));
		}
		return criteria.list();
	}
}
