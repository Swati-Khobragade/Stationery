package com.gl.adminportal.stationery.dao.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gl.adminportal.stationery.dao.RequestDao;
import com.gl.adminportal.stationery.dao.RequestedItemsDao;
import com.gl.adminportal.stationery.enums.REQUESTSTATE;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.model.RequestedItems;
import com.gl.adminportal.stationery.utils.Constants;
import com.gl.adminportal.stationery.utils.DateUtils;

@Repository("RequestDao")
@Transactional
public class RequestDaoImpl implements RequestDao {

	private static final Logger LOGGER = Logger.getLogger(RequestDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RequestedItemsDao requestedItemsDao;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	public Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	@Override
	public Long placeRequest(Request request) {
		LOGGER.info("Entered the itemDao - addItem ");
		Long requestId = (Long) this.sessionFactory.getCurrentSession().save(
				request);
		LOGGER.info("Generated RequestId - " + requestId + "for request"
				+ request);
		return requestId;
	}

	public List<Request> fetchRequestByItemId(Item item) {
		List<Request> requests = new LinkedList<Request>();
		List<RequestedItems> requestedItems = requestedItemsDao
				.getRequestedItemsByItemId(item);

		for (RequestedItems requestedItem : requestedItems) {
			Request request = this.fetchRequestById(requestedItem);
			requests.add(request);
		}
		return requests;
	}

	@Override
	public Request fetchRequestById(RequestedItems requestedItem) {
		try {
			return requestedItem.getRequestFk();
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching request by Id= "
					+ requestedItem.getId() + " is " + e);
			return null;
		}

	}

	@Override
	public List<Request> getRequestByEmpId(String empId, String locationName,
			String startDate, String endDate) {
		LOGGER.info("Entered into RequestedItemDaoImpl - getRequestByEmpId()");
		/*
		 * final Session session = getSession(); Date sDate = null, eDate = null; Query
		 * query = session
		 * .createQuery("from Request req where req.location = :location and req.emp_id = :emp_id and req.request_creation_date >= :startdate and req.request_creation_date <= :endDate ORDER BY req.request_creation_date DESC"
		 * );
		 * 
		 * if (null == startDate && null == endDate) { sDate =
		 * DateUtils.getOneMonthPriorDate(); eDate = DateUtils.getCurrentDate(); } else
		 * if (startDate != null && endDate == null) { eDate =
		 * DateUtils.getCurrentDate(); } else { try { sDate =
		 * DateUtils.getDateFromString(startDate); eDate =
		 * DateUtils.getDateFromString(endDate); } catch (ParseException e) {
		 * LOGGER.error("Error parsing date " + e);
		 * 
		 * } }
		 * 
		 * query.setParameter("location", locationName); query.setParameter("emp_id",
		 * Integer.valueOf(empId)); query.setParameter("startdate", sDate);
		 * query.setParameter("endDate", eDate);
		 */
		Criteria criteria = getSession().createCriteria(Request.class);
		criteria.add(Restrictions.eq("location", locationName));
		criteria.add(Restrictions.eq("emp_id", Integer.valueOf(empId)));
		if(startDate == null && endDate == null) {
			criteria.add(Restrictions.ge("request_creation_date", DateUtils.getOneMonthPriorDate()));
			criteria.add(Restrictions.le("request_creation_date", DateUtils.getCurrentDate()));
		}else {
			try {
				criteria.add(Restrictions.ge("request_creation_date", DateUtils.getDateFromString(startDate)));
				criteria.add(Restrictions.le("request_creation_date",  DateUtils.getDateFromString(endDate)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return criteria.list();
	}

	@Override
	public List<Request> getPendingRequestByApprover(String approverName,
			String locationName) {
		LOGGER.info("Entered into RequestedItemDaoImpl - getPendingRequestByApprover()");
		final Session session = getSession();
		Query query = session
				.createQuery("from Request req where req.location = :location and req.approved_by = :approved_by and req.state =:state");
		query.setParameter("location", locationName);
		query.setParameter("approved_by", approverName);
		query.setParameter("state", REQUESTSTATE.PENDING_APPROVAL.name());
		return query.list();
	}
	
	@Override
	public List<Request> getAllPendingRequests(String locationName, String fromDate, String toDate) {
		LOGGER.info("Entered into RequestedItemDaoImpl - getAllPendingRequests()");
		final Session session = getSession();
		Query query = session.createQuery(
				"from Request req where req.location = :location and  (req.state =:approvedstate or req.state =:createdstate) and req.request_creation_date >= :startdate and req.request_creation_date <= :enddate ORDER BY req.request_creation_date DESC");
		
		query.setParameter("location", locationName);
		query.setParameter("approvedstate", REQUESTSTATE.APPROVED.name());
		query.setParameter("createdstate", REQUESTSTATE.CREATED.name());
		
		Date sDate = null, eDate = null;
			
		if (null == fromDate && null == toDate) {
			sDate = DateUtils.getOneMonthPriorDate();
			eDate = DateUtils.getCurrentDate();
		} else if (fromDate != null && toDate == null) {
			eDate = DateUtils.getCurrentDate();
		} else {
			try {
				sDate = DateUtils.getDateFromString(fromDate);
				eDate = DateUtils.getDateFromString(toDate);
			} catch (ParseException e) {
				LOGGER.error("Error parsing date " + e);

			}
		}
		query.setParameter("startdate", sDate);
		query.setParameter("enddate", eDate);
		return query.list();
	}

	@Override
	public Request getRequestByReqIdAndApprover(long requestId,
			String approvedBy, String location) {
		final Session session = getSession();
		Request request = null;
		try {
			Criteria criteria = session.createCriteria(Request.class)
					.add(Restrictions.eq("id", requestId))
					.add(Restrictions.eq("approved_by", approvedBy))
					.add(Restrictions.eq("location", location));
			request = (Request) criteria.uniqueResult();
			if (null != request) {
				LOGGER.info("Request list " + request.toString());
			}
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
		return request;
	}

	@Override
	public void updateRequestState(List<Request> request, String state,
			String locationName) {
		final Session session = getSession();
		Transaction transaction = session.beginTransaction();
		Request requestObj = null;
		try {
			for (Request req : request) {
				Criteria criteria = session.createCriteria(Request.class).add(
						Restrictions.eq("id", req.getId()));
				requestObj = (Request) criteria.uniqueResult();
				if (null != requestObj) {
					LOGGER.info("Request list " + requestObj.toString());
					// set the quantity passed in request in database
					if (state.equals("reject")) {
						requestObj.setState(REQUESTSTATE.REJECTED.name());
					} else if (state.equals("approve")) {
						requestObj.setState(REQUESTSTATE.APPROVED.name());
					}
					requestObj.setRequest_updation_date(DateUtils
							.getCurrentDate());
					session.update(requestObj);
					transaction.commit();
				}
			}
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}

	}
	
	/**
	 * This method will update the state of all requested items in APPROVED/CREATED state to DELIVERED state. 
	 * */
	@Override
	public void updateRequestedItemState(List<Request> request, String locationName){
		final Session session = getSession();
		Transaction transaction = session.beginTransaction();
		Request requestObj = null;
		try {
			for (Request req : request) {
				Criteria criteria = session.createCriteria(Request.class).add(Restrictions.eq("id", req.getId()));
				requestObj = (Request) criteria.uniqueResult();
				if (null != requestObj) {
					//LOGGER.info("Request list " + requestObj.toString());
					// set the delivered state for the request in database
					requestObj.setState(REQUESTSTATE.DELIVERED.name());
					requestObj.setRequest_updation_date(DateUtils.getCurrentDate());
					session.update(requestObj);
					transaction.commit();
				}
			}
		} catch (Exception e) {
			LOGGER.equals("Exception retriving result from database " + e);
		} finally {
			session.close();
		}
	}

	public Request getRequestById(String requestId, String locationName) {
		final Session session = getSession();
		Query query = session
				.createQuery("from Request req where req.location = :location and req.id = :id");
		query.setParameter("location", locationName);
		query.setParameter("id", Long.valueOf(requestId));
		Request req = (Request) query.uniqueResult();
		session.close();
		return req;
	}

	@Override
	public void deleteRequest(String requestId, String locationName) {
		final Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("from Request req where req.location = :location and req.id = :id");
		query.setParameter("location", locationName);
		query.setParameter("id", Long.valueOf(requestId));
		Request req = (Request) query.uniqueResult();
		session.delete(req);
	
	}
	@Override
	public  Long getItemCount(int empId, String locationName, int itemId) {
		try {
		final Session session = getSession();
		Criteria criteria = session.createCriteria(Request.class);
		criteria.createAlias("requestedItems", "requestedItems", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("requestedItems.itemFk", "itemFk", JoinType.LEFT_OUTER_JOIN);
		criteria.setProjection(Projections.projectionList()
			      .add(Projections.sum("requestedItems.quantityOrderd")))
		          .add(Restrictions.le("request_creation_date", new Date()))
		          .add(Restrictions.ge("request_creation_date", DateUtils.getOneMonthPriorDate()))
		          .add(Restrictions.eq("requestedItems.itemFk.id", itemId))
		          .add(Restrictions.eq("location", locationName))
		          .add(Restrictions.eq("emp_id",empId));
		Long requestItem  = (Long) criteria.uniqueResult();
		if(requestItem!= null) {
			return requestItem;
		}else {
			return 0l;
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0l;
	}

	@Override
	public Request getLastRequestedItem(int itemId,int empId) {
		final Session session = getSession();
		Criteria criteria = session.createCriteria(Request.class);
		criteria.createAlias("requestedItems", "requestedItems", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("requestedItems.itemFk", "itemFk", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("requestedItems.itemFk.id", itemId));
		criteria.add(Restrictions.eq("emp_id", empId));
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("request_creation_date"));
		return (Request) criteria.uniqueResult();
	}
	
	@Override
	public List<Request> getAllRequestedItem(String locationName){
		final Session session = getSession();
		Criteria criteria = session.createCriteria(Request.class);
		criteria.createAlias("requestedItems", "requestedItems", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("location", locationName));
		criteria.addOrder(Order.desc("request_creation_date"));
		criteria.setMaxResults(4);
	//	criteria.add(Restrictions.eq("state", REQUESTSTATE.CREATED));
		return criteria.list();
		
	}
}
