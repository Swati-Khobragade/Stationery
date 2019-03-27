package com.gl.adminportal.stationery.dao.impl;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gl.adminportal.stationery.dao.RequestedItemsDao;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.RequestedItems;

@Repository("RequestItemsDao")
@Transactional
public class RequestedItemsDaoImpl implements RequestedItemsDao {
	
	private static final Logger LOGGER = Logger.getLogger(RequestedItemsDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	public List<RequestedItems> getRequestedItemsByItemId(Item item) {
		Session session=this.sessionFactory.getCurrentSession();
		Query query=session.createQuery("from RequestedItems ri where ri.itemFk = "+item.getId() );
		List<RequestedItems> requestedItems=query.list();
		return requestedItems;
	}

	@Override
	public void addBulkRequestedItems(Set<RequestedItems> requestedItems) {
		LOGGER.info("Entered the RequestedItemDao - addItem ");
		for(RequestedItems requestedItem:requestedItems){
		Long requestedItemId = (Long) this.sessionFactory.getCurrentSession().save(requestedItem);
		LOGGER.info("Generated Request & Items Mapping Id  - " + requestedItemId + "for RequestedItem " + requestedItem);
		}
	}

}
