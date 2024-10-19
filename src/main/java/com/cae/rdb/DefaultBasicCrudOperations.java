package com.cae.rdb;


import com.cae.mapped_exceptions.specifics.InternalMappedException;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public abstract class DefaultBasicCrudOperations<T extends TableSchema<I>, I> implements BasicCrudOperations<T, I> {

    protected DefaultBasicCrudOperations(){
        DefaultBasicCrudOperations.mapGenericTypes(this);
        DefaultBasicCrudOperations.registerEntity(this);
    }

    @SuppressWarnings("unchecked")
    private static <I, T extends TableSchema<I>> void mapGenericTypes(DefaultBasicCrudOperations<T,I> instance) {
        try{
            Type superClass = instance.getClass().getGenericSuperclass();
            if (superClass instanceof ParameterizedType parameterized) {
                instance.entityType = (Class<T>) parameterized.getActualTypeArguments()[0];
                instance.entityName = instance.entityType.getAnnotation(Entity.class).name();
                instance.identifierType = (Class<I>) parameterized.getActualTypeArguments()[1];
            }
        } catch (Exception exception){
            throw new InternalMappedException(
                    "Something went wrong while trying to get the entity and its ID types of the " + instance.getClass().getSimpleName() + " object ",
                    "More details: the entity and or ID types not specified"
            );
        }
    }

    private static <A extends TableSchema<I>, I> void registerEntity(DefaultBasicCrudOperations<A, I> instance) {
        EntityClassesProvider.addEntityClass(instance.entityType);
    }

    @Getter(AccessLevel.PROTECTED)
    private Class<T> entityType;
    @Getter(AccessLevel.PROTECTED)
    private String entityName;
    @Getter(AccessLevel.PROTECTED)
    private Class<I> identifierType;
    @Getter(AccessLevel.PROTECTED)
    protected SessionFactory sessionFactory;

    @Override
    public T createNew(T instance) {
        try (var session = HibernateConfigBootstrap.SESSION_FACTORY.openSession()){
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                var id = this.identifierType.cast(session.save(instance));
                transaction.commit();
                instance.setPrimaryKey(id);
                return instance;
            } catch (Exception exception){
                Optional.ofNullable(transaction).ifPresent(EntityTransaction::rollback);
                throw new InternalMappedException(
                        "Something went wrong while trying to save the instance",
                        exception.toString()
                );
            }
        }
    }

    @Override
    public void deleteById(I primaryKey) {
        try (var session = HibernateConfigBootstrap.SESSION_FACTORY.openSession()){
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                var instance = this.entityType.getDeclaredConstructor().newInstance();
                instance.setPrimaryKey(primaryKey);
                session.delete(instance);
                transaction.commit();
            } catch (Exception exception){
                Optional.ofNullable(transaction).ifPresent(EntityTransaction::rollback);
                throw new InternalMappedException(
                        "Something went wrong while trying to delete the instance. (In case your entity doesn't have a default constructor, please provide one)",
                        exception.toString()
                );
            }
        }
    }

    @Override
    public Optional<T> findById(I primaryKey) {
        try (var session = HibernateConfigBootstrap.SESSION_FACTORY.openSession()){
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                var instance = session.get(this.entityType, (Serializable) primaryKey);
                transaction.commit();
                return Optional.ofNullable(instance);
            } catch (Exception exception){
                Optional.ofNullable(transaction).ifPresent(EntityTransaction::rollback);
                throw new InternalMappedException(
                        "Something went wrong while trying to fetch the instance by its id",
                        exception.toString()
                );
            }
        }
    }

    @Override
    public Boolean existsById(I id){
        return Optional.ofNullable(this.findById(id)).isPresent();
    }

    @Override
    public List<T> retrieveAll() {
        try (var session = HibernateConfigBootstrap.SESSION_FACTORY.openSession()){
            Transaction transaction = null;
            try{
                var hql = "FROM " + this.entityName;
                transaction = session.beginTransaction();
                var allInstances = session.createQuery(hql, this.entityType).list();
                transaction.commit();
                return allInstances;
            } catch (Exception exception){
                Optional.ofNullable(transaction).ifPresent(EntityTransaction::rollback);
                throw new InternalMappedException(
                        "Something went wrong while trying to fetch all instances",
                        exception.toString()
                );
            }
        }
    }

    @Override
    public Page<T> retrievePaginated(Integer pageNumber, Integer pageSize) {
        try (var session = HibernateConfigBootstrap.SESSION_FACTORY.openSession()){
            Transaction transaction = null;
            try{
                var selectHql = "FROM " + this.entityName;
                transaction = session.beginTransaction();
                var paginatedItems = session.createQuery(selectHql, this.entityType)
                        .setFirstResult((pageNumber - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .list();
                var totalHql = "SELECT COUNT(e) FROM " + this.entityName + " e";
                var total = (Long) session.createQuery(totalHql).uniqueResult();
                transaction.commit();
                return new Page<>(pageNumber, pageSize, total, paginatedItems);
            } catch (Exception exception){
                Optional.ofNullable(transaction).ifPresent(EntityTransaction::rollback);
                throw new InternalMappedException(
                        "Something went wrong while trying to paginate instances",
                        exception.toString()
                );
            }
        }
    }

    @Override
    public void update(T instanceToUpdate) {
        try (var session = HibernateConfigBootstrap.SESSION_FACTORY.openSession()){
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                session.update(instanceToUpdate);
                transaction.commit();
            } catch (Exception exception){
                Optional.ofNullable(transaction).ifPresent(EntityTransaction::rollback);
                throw new InternalMappedException(
                        "Something went wrong while trying to update the instance",
                        exception.toString()
                );
            }
        }
    }

}
