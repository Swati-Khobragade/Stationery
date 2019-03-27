package com.gl.adminportal.stationery.dao.impl;

import java.text.ParseException;
import java.util.Base64;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.gl.adminportal.stationery.controller.ItemController;
import com.gl.adminportal.stationery.dao.ItemDao;
import com.gl.adminportal.stationery.dao.RequestDao;
import com.gl.adminportal.stationery.enums.ITEMOPERATION;
import com.gl.adminportal.stationery.enums.REQUESTSTATE;
import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.ItemAuditTrail;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.request.ItemRequest;
import com.gl.adminportal.stationery.utils.Constants;
import com.gl.adminportal.stationery.utils.DateUtils;

@Repository("itemDao")
@Transactional
public class ItemDaoImpl implements ItemDao {

	private static final Logger LOGGER = Logger.getLogger(ItemController.class);

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
	public int addItem(List<ItemRequest> items, String locatioName) {
		LOGGER.info("Entered the itemDao - addItem " + items);
		int itemId = 0;
		for (ItemRequest i : items) {
			Item item = new Item();
			item.setName(i.getName());
			item.setPrice(i.getPrice());
			item.setQuantity(i.getQuantity());
			item.setNeedApproval(i.isNeedApproval());
			item.setItemCreationDate(DateUtils.getCurrentDate());
			item.setItemUpdationDate(DateUtils.getCurrentDate());
			item.setLocation(locatioName);
			item.setEnabled(true);
			item.setImage_stream(i.getImageStream());
			itemId = (int) this.sessionFactory.getCurrentSession().save(item);
		}
		return itemId;
	}

	@Override
	public List<Item> getItemByNameAndLocation(ItemRequest itemReq,
			String locationName) {
		final Session session = getSession();
		List<Item> itemList = null;
		try {
			Criteria criteria = session.createCriteria(Item.class)
					.add(Restrictions.eq("name", itemReq.getName()))
					.add(Restrictions.eq("location", locationName));
			itemList = criteria.list();
			LOGGER.info("Item list " + itemList.toString());
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
		return itemList;
	}

	@Override
	public Item getItemById(int id, String locationName) {
		final Session session = getSession();
		Item item = null;
		try {
			Criteria criteria = session.createCriteria(Item.class)
					.add(Restrictions.eq("id", id))
					.add(Restrictions.eq("location", locationName));
			item = (Item) criteria.uniqueResult();
			if (null != item) {
				LOGGER.info("Item list " + item.toString());
			}
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
		return item;
	}

	@Override
	public Item getItemByIdRole(int id, String locationName, String role) {
		final Session session = getSession();
		Item item = null;
		try {
			Criteria criteria = session.createCriteria(Item.class)
					.add(Restrictions.eq("id", id))
					.add(Restrictions.eq("location", locationName));
			if (role.equalsIgnoreCase("Employee")) {
				// add restriction of the enabled flag else not
				criteria.add(Restrictions.eq("enabled", true));
			}
			item = (Item) criteria.uniqueResult();
			if (null != item) {
				LOGGER.info("Item list " + item.toString());
			}
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
		return item;
	}

	@Override
	public void updateItem(List<ItemRequest> itemRequest, String locationName) {
		final Session session = getSession();
		Transaction transaction = session.beginTransaction();
		Item item = null;
		try {
			for (ItemRequest itemReq : itemRequest) {
				Criteria criteria = session.createCriteria(Item.class).add(
						Restrictions.eq("id", itemReq.getId()));
				item = (Item) criteria.uniqueResult();
				if (null != item) {
					LOGGER.info("Item list " + item.toString());

					// set the quantity passed in request in database

					item.setQuantity(item.getQuantity() + itemReq.getQuantity());
					item.setPrice(itemReq.getPrice());
					item.setNeedApproval(itemReq.isNeedApproval());
					item.setItemUpdationDate(DateUtils.getCurrentDate());
					item.setImage_stream(itemReq.getImageStream());
					session.update(item);
					transaction.commit();
				}
			}
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
	}

	@Override
	public void deleteItem(List<ItemRequest> itemRequest, String locationName) {
		final Session session = getSession();
		Transaction transaction = session.beginTransaction();
		for (ItemRequest itemReq : itemRequest) {
			Item item = (Item) session.get(Item.class, itemReq.getId());
			session.delete(item);
			transaction.commit();
		}
	}

	@Override
	public List<Item> getAllItems(String locationName, String role) {
		final Session session = getSession();
		Query query = session
				.createQuery("from Item a where a.location = :location ");
		if (role.equalsIgnoreCase("Employee")) {
			query = null;
			query = session
					.createQuery("from Item a where a.location = :location and a.enabled = :enabled");
			query.setParameter("enabled", true);
		}
		query.setParameter("location", locationName);
		return query.list();
	}

	@Override
	public void addItemToAuditTrail(List<ItemRequest> items,
			String locationName, ITEMOPERATION itemOperation, int itemId) {
		LOGGER.info("Entered the itemDao - addItemToAuditTrail " + items);
		for (ItemRequest i : items) {
			ItemAuditTrail itemAuditTrail = new ItemAuditTrail();
			itemAuditTrail.setItemId(itemId);
			itemAuditTrail.setName(i.getName());
			itemAuditTrail.setPrice(i.getPrice());
			itemAuditTrail.setQuantity(i.getQuantity());
			itemAuditTrail.setTimestamp(DateUtils.getCurrentDate());
			itemAuditTrail.setLocationName(locationName);
			itemAuditTrail.setItemOperation(itemOperation.toString());
			itemAuditTrail.setTotalPrice(i.getPrice() * i.getQuantity());
			this.sessionFactory.getCurrentSession().save(itemAuditTrail);
		}

	}

	@Override
	public void updateItemInAuditTrail(List<ItemRequest> itemRequest,
			String locationName, ITEMOPERATION itemOperation) {
		LOGGER.info("Entered the itemDao - addItemToAuditTrail ");
		for (ItemRequest i : itemRequest) {
			// fetch item from db for populating its price and name
			Item item = getItemById(i.getId(), locationName);
			ItemAuditTrail itemAuditTrail = new ItemAuditTrail();
			itemAuditTrail.setItemId(i.getId());
			itemAuditTrail.setName(item.getName());
			itemAuditTrail.setPrice(item.getPrice());
			itemAuditTrail.setQuantity(i.getQuantity());
			itemAuditTrail.setTimestamp(DateUtils.getCurrentDate());
			itemAuditTrail.setLocationName(locationName);
			itemAuditTrail.setItemOperation(itemOperation.toString());
			itemAuditTrail.setTotalPrice(item.getPrice() * i.getQuantity());
			this.sessionFactory.getCurrentSession().save(itemAuditTrail);
		}
	}

	@Override
	public List<ItemAuditTrail> getAuditTrail(String locationName,
			String startDate, String endDate) {
		LOGGER.info("Entered the itemDao - getAuditTrail()");
		final Session session = getSession();
		Date sDate = null, eDate = null;
		Query query = session
				.createQuery("from ItemAuditTrail a where a.locationName = :location and a.timestamp >= :startdate and a.timestamp <= :endDate");
		if (null == startDate && null == endDate) {
			sDate = DateUtils.getOneMonthPriorDate();
			eDate = DateUtils.getCurrentDate();
		} else if (startDate != null && endDate == null) {
			eDate = DateUtils.getCurrentDate();
		} else {
			try {
				sDate = DateUtils.getDateFromString(startDate);
				eDate = DateUtils.getDateFromString(endDate);
			} catch (ParseException e) {
				LOGGER.error("Error parsing date " + e);

			}
		}
		query.setParameter("location", locationName);
		query.setParameter("startdate", sDate);
		query.setParameter("endDate", eDate);
		return query.list();

	}

	/**
	 * Disable item toggle the isEnabled flag.
	 * 
	 * @throws SystemException
	 */
	@Override
	public void disableItem(List<ItemRequest> itemRequest, String locationName)
			throws SystemException {
		final Session session = getSession();
		Transaction transaction = session.beginTransaction();

		for (ItemRequest itemReq : itemRequest) {
			Item item = getItemById(itemReq.getId(), locationName);
			List<Request> requests = requestDao.fetchRequestByItemId(item);
			
				for (Request request : requests) {
					if (request.getState().equals(
							REQUESTSTATE.CREATED.toString()) || request.getState()
							.equals(REQUESTSTATE.PENDING_APPROVAL.toString()) || request.getState()
							.equals(REQUESTSTATE.APPROVED.toString())
							 ) {
						throw new SystemException(
								Constants.FIVE_THOUSAND_THIRTEEN,
								String.format(
										Constants.REQUEST_IN_PENDING_STATE,
										item.getId(), request.getId()),
								HttpStatus.PRECONDITION_FAILED);
					}
				}
				String state = item.isEnabled() ? "enabled" : "disabled";
				if(item.isEnabled()){
					boolean itemState = itemReq.isEnable();
					if(itemState){
						throw new SystemException(
								Constants.FIVE_THOUSAND_ELEVEN,
								String.format(
										Constants.ITEM_ALEADY_IN_ENABLED_STATE,
										state),
								HttpStatus.BAD_REQUEST);
					}else{
						item.setEnabled(false);
					}
				}else{
					boolean itemState = itemReq.isEnable();
					if(!itemState){
						throw new SystemException(
								Constants.FIVE_THOUSAND_ELEVEN,
								String.format(
										Constants.ITEM_ALEADY_IN_ENABLED_STATE,
										state),
								HttpStatus.BAD_REQUEST);
					}else{
						item.setEnabled(true);
					}
				}
				session.update(item);
				transaction.commit();
			}
		
	}

	@Override
	public List<Item> serachItem(String itemName, String location) {
		Criteria criteria = getSession().createCriteria(Item.class);
		criteria.add(Restrictions.like("name", itemName, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("location", location));
		return criteria.list();
	}

	@Override
	public List<Item> getLatestItem(String locationName) {
		Criteria criteria = getSession().createCriteria(Item.class);
		criteria.add(Restrictions.eq("location", locationName));
		criteria.add(Restrictions.eq("enabled", true));
		criteria.addOrder(Order.desc("itemCreationDate"));
		criteria.setMaxResults(3);
		return criteria.list();
	}
}
