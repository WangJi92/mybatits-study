package com.common.utils.dao;


import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * descrption: 处理一些常见的Hibernate对于Dao层的操作
 * authohr: wangji
 * date: 2017-08-16 11:04
 */
@Component
public class CommonHibernateDao extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(CommonHibernateDao.class);
    private HibernateTemplate hibernateTemplate;

    @Autowired
    public void setSessionFactoryEx(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
        hibernateTemplate = getHibernateTemplate();
    }

    //批量提交阀值
    protected static final int BATCH_COMMINT_THRESHOLD = 1000;
    //批量提交默认值
    protected static final int BATCH_COMMIT_DEFAULT_VALUE = 100;

    protected static final int OPER_TYPE_SAVE = 1;

    protected static final int OPER_TYPE_UPDATE = 2;

    protected static final int OPER_TYPE_SAVEORUPDATE = 3;

    @Description("强制让session的数据和数据库的数据同步")
    public void flush() {
        getHibernateTemplate().flush();
    }

    @Description("更新某个实体的信息")
    public void update(Object object) {
        hibernateTemplate.save(object);
    }

    @Description("更新一个List实体")
    public void update(List<?> list) {
        this.update(list, BATCH_COMMIT_DEFAULT_VALUE);
    }

    @Description("保存某个实体的信息，设置批量更新范围")
    public void update(List<?> list, int batchCommitSize) {
        this.doOperationList(list, batchCommitSize, OPER_TYPE_UPDATE);
    }

    @Description("保存某个实体的信息")
    public void save(Object object) {
        hibernateTemplate.save(object);
    }

    @Description("保存List实体的信息")
    public void save(List<?> list) {
        this.save(list, BATCH_COMMIT_DEFAULT_VALUE);
    }

    @Description("保存List实体的信息，设置批量更新范围")
    public void save(List<?> list, int batchCommitSize) {
        this.doOperationList(list, batchCommitSize, OPER_TYPE_SAVE);
    }

    @Description("保存或者更新实体")
    public void saveOrUpdate(Object object) {
        hibernateTemplate.saveOrUpdate(object);
    }

    @Description("保存或者更新List")
    public void saveOrUpdate(List<?> list) {
        this.saveOrUpdate(list, BATCH_COMMIT_DEFAULT_VALUE);
    }

    @Description("保存或者更新List,设置批量更新范围")
    public void saveOrUpdate(List<?> list, int batchCommitSize) {
        this.doOperationList(list, batchCommitSize, OPER_TYPE_SAVEORUPDATE);
    }

    @Description("删除实体的信息")
    public void delete(Object object) {
        hibernateTemplate.delete(object);
    }

    @Description("删除List实体的信息")
    public void deleteAll(Collection<?> object) {
        hibernateTemplate.deleteAll(object);
    }

    @Description("批量更新操作进行限制")
    private void doOperationList(List<?> list, int batchCommitSize, int operType) {
        if (list == null || list.isEmpty()) {
            return;
        }
        batchCommitSize = batchCommitSize > BATCH_COMMINT_THRESHOLD ? BATCH_COMMINT_THRESHOLD : batchCommitSize;
        if (list.size() <= batchCommitSize) {
            //1 stand for save
            if (operType == OPER_TYPE_SAVE) {
                for (int i = 0; i < list.size(); i++) {
                    hibernateTemplate.save(list.get(i));
                }
            } else if (operType == OPER_TYPE_UPDATE) {
                for (int i = 0; i < list.size(); i++) {
                    hibernateTemplate.update(list.get(i));
                }
            } else if (operType == OPER_TYPE_SAVEORUPDATE) {
                for (int i = 0; i < list.size(); i++) {
                    hibernateTemplate.saveOrUpdate(list.get(i));
                }
            } else {
                throw new IllegalArgumentException("Unknow operType:" + operType);
            }
            hibernateTemplate.flush();
        } else {
            if (operType == OPER_TYPE_SAVE) {
                for (int i = 0; i < list.size(); i++) {
                    hibernateTemplate.save(list.get(i));
                    if (i != 0 && i % batchCommitSize == 0) {
                        hibernateTemplate.flush();
                    }
                }
            } else if (operType == OPER_TYPE_UPDATE) {
                for (int i = 0; i < list.size(); i++) {
                    hibernateTemplate.update(list.get(i));
                    if (i != 0 && i % batchCommitSize == 0) {
                        hibernateTemplate.flush();
                    }
                }
            } else if (operType == OPER_TYPE_SAVEORUPDATE) {
                for (int i = 0; i < list.size(); i++) {
                    hibernateTemplate.saveOrUpdate(list.get(i));
                    if (i != 0 && i % batchCommitSize == 0) {
                        hibernateTemplate.flush();
                    }
                }
            } else {
                throw new IllegalArgumentException("Unknow operType:" + operType);
            }
            hibernateTemplate.flush();
        }
    }
    @Description("设置HQL的占位符,返回Query对象")
    protected Query createQuery(String hql, Object...values){
        Query query =currentSession().createQuery(hql);
        if(values!=null && values.length>0){
            for(int i=0;i<values.length;i++){
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }
    protected Query createQuery(String hql, List<?> values){
        Query query =currentSession().createQuery(hql);
        if(values!=null && values.size()>0){
            for(int i=0;i<values.size();i++){
                query.setParameter(i, values.get(i));
            }
        }

        return query;
    }
    @Description("设置SQL的占位符，返回Query对象")
    protected Query createSQLQuery(String sql,Object...values){
        Query query = currentSession().createSQLQuery(sql);
        if(values!=null && values.length>0){
            for(int i=0;i<values.length;i++){
                query.setParameter(i,values[i]);
            }
        }
        return  query;
    }
    protected Query createSQLQuery(String sql,List<?> values){
        Query query = currentSession().createSQLQuery(sql);
        if(values !=null && values.size()>0){
            for(int i=0;i<values.size();i++){
                query.setParameter(i,values.get(i));
            }
        }
        return  query;
    }
    @Description("根据HQL查找List数据信息")
    public List findListByHQL(String hql,Object...values){
        Query query = createQuery(hql,values);
        return query.list();
    }
    @Description("根据SQL查找List数据信息")
    public List findListBySQL(String sql,Object...values){
        Query query = createSQLQuery(sql,values);
        return query.list();
    }

//    Query query = session.createSQLQuery("select CN_ID as id ,CN_ACCOUNT as account  ,CN_PASSWORD as password from TN_USER")
//            .addScalar("id", Hibernate.INTEGER)//StandardBasicTypes.STRING 高版本使用这个
//            .addScalar("account",Hibernate.STRING)
//            .addScalar("password",Hibernate.STRING)
//            .setResultTransformer(Transformers.aliasToBean(U.class));

//final String sql = "select u.userName as name,u.userAge as age,u.userAddress as address,u.id from user u";
//    @SuppressWarnings({"unchecked"})
//    List<User> users = commonDao.findListBySQL(User.class,new CommonHibernateDao.AddScala() {
//        public void addScalaToBean(SQLQuery query) {
//            query.addScalar("name", StandardBasicTypes.STRING);
//            query.addScalar("age",StandardBasicTypes.INTEGER);
//            query.addScalar("address",StandardBasicTypes.STRING);
//            query.addScalar("id",StandardBasicTypes.INTEGER);
//        }
//    },sql,null);
    /**
     * List findListBySQL(String sql,Class aliasToBean,Object...values) 和这个的使用是一样的不用自己去进行类型的转换
     * @param aliasToBean
     * @param addScala
     * @param sql
     * @param values
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List findListBySQL(Class aliasToBean,AddScala addScala,String sql,List<?> values){
        SQLQuery sqlQuery = (SQLQuery) createSQLQuery(sql,values);
        addScala.addScalaToBean(sqlQuery);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(aliasToBean));
        return sqlQuery.list();
    }
    public List findListBySQL(String sql,Class aliasToBean,Object...values){
        SQLQuery sqlQuery = (SQLQuery) createSQLQuery(sql,values);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(aliasToBean));
        return sqlQuery.list();
    }
    @SuppressWarnings({"unchecked"})
    @Description("返回一个List Map的数据信息")
    public List<Map<String,Object>> findListMapBySQL(String sql,Object...values){
        SQLQuery sqlQuery = (SQLQuery) createSQLQuery(sql,values);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  (List<Map<String,Object>>)sqlQuery.list();
    }
    @SuppressWarnings({"unchecked"})
    @Description("返回一个List Map的数据信息")
    public List<Map<String,Object>> findListMapBySQL(String sql,List<?> values){
        SQLQuery sqlQuery = (SQLQuery) createSQLQuery(sql,values);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  (List<Map<String,Object>>)sqlQuery.list();
    }
    @Description("通过离线查询返回唯一的实例,查询结果有多个会异常")
    public <T> T findUniqueSingleByDetachedCriteria(final DetachedCriteria detachedCriteria){
        return (T) hibernateTemplate.executeWithNativeSession(new HibernateCallback<T>() {
            public T doInHibernate(Session session) throws HibernateException {
                Criteria criteria = detachedCriteria
                        .getExecutableCriteria(session);
                detachedCriteria
                        .setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
                return (T) criteria.uniqueResult();
            }
        });
    }

//    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
//    detachedCriteria.addOrder(Order.asc("name"));
//    detachedCriteria.add(Restrictions.like("name","%an%"));
//    List<User> users = commonDao.findALlByDetachedCriteria(detachedCriteria);
    @Description("通过离线查询返回多个实例")
    public List findALlByDetachedCriteria(final DetachedCriteria detachedCriteria){
        return (List) hibernateTemplate.executeWithNativeSession(new HibernateCallback<List>() {
            public List doInHibernate(Session session) throws HibernateException {
                Criteria criteria = detachedCriteria
                        .getExecutableCriteria(session);
                detachedCriteria
                        .setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
                return (List)criteria.list();
            }
        });
    }
    @Description("通过SQL更新,设置参数")
    public void executeUpdateSQL(final String sql,final Object...values){
        getHibernateTemplate().executeWithNativeSession(new HibernateCallback(){
            public Object doInHibernate(Session session)
                    throws HibernateException {
                if (sql == null || sql.trim().length()==0) {
                    log.warn("SQL语句为空，不进行更新");
                }else{
                    Query query = session.createSQLQuery(sql);
                    for(int i=0;i<values.length;i++){
                        query.setParameter(i,values[i]);
                    }
                    query.executeUpdate();
                }
                return null;
            }
        });
    }
    @Description("通过HQL更新,设置参数")
    public void executeUpdateHql(final String hql,final Object...values){
        getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException {
                if (hql == null || hql.trim().length()==0) {
                    log.warn("HQL语句为空，不进行更新");
                }else{
                    Query query = session.createQuery(hql);
                    for(int i=0;i<values.length;i++){
                        query.setParameter(i,values[i]);
                    }
                    query.executeUpdate();
                }
                return null;
            }
        });
    }

    public <T> T findById(Class<T> entityClass, Serializable id){
        return hibernateTemplate.get(entityClass,id);
    }
    public <T> List<T> findAll(Class<T> entityClass){
        return  hibernateTemplate.loadAll(entityClass);
    }

    @SuppressWarnings({"unchecked"})
    @Description("根据条件查询,某一个属性，多个属性等与可以使用离线查询")
    public <T> T findFirstBy(Class<T> entityClass,String propertyName, Object value){
        Criteria criteria = createCriteria(entityClass, Restrictions.eq(propertyName, value));
        List list = criteria.list();
        return (T)(list!=null?list.get(0):null);
    }
    public <T> T findUniqueBy(Class<T> entityClass,String propertyName, Object value){
        Criteria criteria = createCriteria(entityClass, Restrictions.eq(propertyName, value));
        return (T)criteria.uniqueResult();
    }
    @SuppressWarnings({"unchecked"})
    public<T> List<T> findListBy(Class<T> entityClass,String propertyName, Object value){
        Criteria criteria = createCriteria(entityClass,Restrictions.eq(propertyName, value));
        return  criteria.list();
    }
    @Description("根据条件进行查询")
    public Criteria createCriteria(Class entityClass,Criterion...criterions){
        Assert.notNull(entityClass, "this is not null");
        Session session = currentSession();
        //xxx.class
        Criteria criteria = session.createCriteria(entityClass);
        for(Criterion criterion: criterions){
            criteria.add(criterion);
        }
        return criteria;
    }
    @SuppressWarnings({"unchecked"})
    public boolean deleteById(Serializable id,Class entityCLass) {
        StringBuffer hql = new StringBuffer("DELETE FROM ");
        hql.append(entityCLass.getName());
        hql.append(" WHERE id=?");
        Query query = createQuery(hql.toString(),id);
        return query.executeUpdate() >0 ? true : false;
    }
    //in(a,b,c,d)
    public void deleteByOnePropers(final String propertyName, final String propertyValue, final Class entityClass) {
        getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                StringBuffer hql = new StringBuffer(50);
                hql.append("delete from ");
                hql.append(entityClass.getSimpleName());
                hql.append(" where ");
                hql.append(propertyName);
                hql.append(" in (");
                hql.append(propertyValue);
                hql.append(")");
                Query query = session.createQuery(hql.toString());
                query.executeUpdate();
                return null;
            }
        });
    }
    //多个属性匹配
    public void deleteByManyPropers(final List<String> propertyNames, final List<Object> propertyValues,final Class entityClass) {
        if (propertyNames.size() != propertyValues.size()) {
            throw new ArrayStoreException("数组长度不匹配");
        }
        getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                StringBuffer hql = new StringBuffer(50);
                hql.append("delete from ");
                hql.append(entityClass.getSimpleName());
                hql.append(" where ");
                hql.append(" 1=1 ");
                for (int i = 0, l = propertyNames.size(); i < l; i++) {
                    hql.append(" and  ");
                    hql.append(propertyNames.get(i));
                    if (propertyValues.get(i) instanceof Collection) {
                        hql.append(" in (");
                        hql.append(prdIds((Collection) propertyValues.get(i)));
                        hql.append(")");
                    } else {
                        hql.append("=");
                        hql.append(propertyValues.get(i));
                    }
                }
                Query query = session.createQuery(hql.toString());
                query.executeUpdate();
                return null;
            }
        });
    }

    private String prdIds(Collection propertyValue) {
        StringBuffer ids = new StringBuffer();
        for (Object value : propertyValue) {
            ids.append(value).append(",");
        }
        String result = ids.substring(0, ids.length() - 1);
        return result;
    }

    public  interface AddScala{
        void addScalaToBean(SQLQuery query);
    }

}
