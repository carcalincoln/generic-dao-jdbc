package jdbc.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

/**
 * Abstrai uma Entidade ou classe de objetos a ser persistida no banco de dados.
 * 
 * @author thiago-amm
 * @version v1.0.0 04/11/2017
 * @since v1.0.0
 * @param <T>
 */
public abstract class DAO<T> implements Comparable<DAO<T>>, Serializable {
   
   private static final long serialVersionUID = 1L;
   protected DataSource dataSource;
   protected Class<?> entityClass;
   protected Long id;
   protected Boolean persisted;
   
   public DAO() {
      Type genericSuperClass = getClass().getGenericSuperclass();
      ParameterizedType parameterizedType = (ParameterizedType) genericSuperClass;
      Type typeArgument = parameterizedType.getActualTypeArguments()[0];
      entityClass = (Class<?>) typeArgument;
      id = 0l;
      persisted = false; 
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public boolean equals(Object obj) {
      if (obj == null) return false;
      if (this == obj) return true;
      if (this.getClass() != obj.getClass()) return false;
      DAO<T> other = (DAO<T>) obj;
      if (this.persisted && !other.persisted()) return false;
      if (!this.persisted() && other.persisted) return false;
      if (this.id != other.id) return false;
      return true;
   }
   
   @Override
   public int compareTo(DAO<T> other) {
      if (this.id < other.id) {
         return -1;
      }
      if (this.id > other.id) {
         return 1;
      }
      return 0;
   }
   
   public DataSource getDataSource() {
      return dataSource;
   }
   
   public DataSource dataSource() {
      return getDataSource();
   }
   
   public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
   }
   
   public DAO<T> dataSource(DataSource dataSource) {
      setDataSource(dataSource);
      return this;
   }
   
   public Long getId() {
      return id;
   }
   
   public Long id() {
      return getId();
   }
   
   public void setId(Long id) {
      this.id = id == null ? 0l : id;
   }
   
   public DAO<T> id(Long id) {
      setId(id);
      return this;
   }
   
   public void setId(Integer id) {
      this.id = id == null ? 0l : (long) id;
   }
   
   public DAO<T> id(Integer id) {
      setId(id);
      return this;
   }
   
   public Boolean getPersisted() {
      return persisted == null ? false : persisted;
   }
   
   public Boolean persisted() {
      return getPersisted();
   }
   
   public void setPersisted(Boolean persisted) {
      persisted = persisted == null ? false : true;
      this.persisted = persisted;
   }
   
   public DAO<T> persisted(Boolean persisted) {
      setPersisted(persisted);
      return this;
   }
   
   protected Connection connect() {
      Connection connection = null;
      if (dataSource != null) {
         connection = dataSource.getConnection();
      }
      return connection;
   }
   
   protected void disconnect(Connection connection) {
      if (dataSource != null) {
         dataSource.disconnect(connection);
      }
   }
   
   protected void close(Object o) {
      if (o != null && o instanceof AutoCloseable) {
         try {
            ((AutoCloseable) o).close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   protected void close(Object... objects) {
      if (objects != null) {
         for (Object o : objects) {
            close(o);
         }
      }
   }
   
   public abstract void insert(T object);
   public abstract void update(T object);
   public abstract void save(T object);
   public abstract void delete(T object);
   public abstract List<T> findAll();
   
}
