
package session;

import entity.Contact;
import entity.Customer;
import entity.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import error.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class CustomerSession implements CustomerSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Customer> searchCustomers(String name) {
        Query q;
        if (name != null) {
            q = em.createQuery("SELECT c FROM Customer c WHERE "
                    + "LOWER(c.name) LIKE :name");
            q.setParameter("name", "%" + name.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT c FROM Customer c");
        }

        return q.getResultList();
    } //end searchCustomers
    
    @Override
    public Customer getCustomer(Long cId) throws NoResultException {
        Customer c = em.find(Customer.class, cId);

        if (c != null) {
            return c;
        } else {
            throw new NoResultException("Not found");
        }
    } //end getCustomer

    @Override
    public void createCustomer(Customer c) {
        em.persist(c);
    } //end createCustomer

    @Override
    public void updateCustomer(Customer c) throws NoResultException {
        Customer oldC = em.find(Customer.class, c.getId());

        if (oldC != null) {
            oldC.setDob(c.getDob());
            oldC.setGender(c.getGender());
            oldC.setName(c.getName());
        } else {
            throw new NoResultException("Not found");
        }
    } //end updateCustomer

    @Override
    public void addContact(Long cId, Contact c) throws NoResultException {
        Customer cust = em.find(Customer.class, cId);

        if (cust != null) {
            em.persist(c);
            cust.getContacts().add(c);
        } else {
            throw new NoResultException("Not found");
        }
    } //end addContact

    @Override
    public void addField(Long cId, Field f) throws NoResultException {
        Customer cust = em.find(Customer.class, cId);

        if (cust != null) {
            Query q = em.createQuery("SELECT f FROM Field f WHERE LOWER(f.name) = :name AND LOWER(f.fieldValue) = :fieldValue");
            q.setParameter("name", f.getName().toLowerCase());
            q.setParameter("fieldValue", f.getFieldValue().toLowerCase());

            try {
                Field found = (Field) q.getSingleResult();
                f = found;
            } catch (Exception e) {
                //not found
                em.persist(f);
            }

            //should check that the field is not found
            if (!cust.getFields().contains(f)){
                cust.getFields().add(f);
            }
        } else {
            throw new NoResultException("Not found");
        }
    } //end addField

    @Override
    public void deleteContact(Long cId) throws NoResultException {
        Contact c = em.find(Contact.class, cId);

        if (c != null) {
            Query q = em.createQuery("SELECT c FROM Customer c WHERE :contact MEMBER OF c.contacts");
            q.setParameter("contact", c);

            for (Object cust : q.getResultList()) {
                Customer cust1 = (Customer) cust;
                cust1.getContacts().remove(c);
            }

            em.remove(c);
        } else {
            throw new NoResultException("Not found");
        }
    } //end deleteContact

    @Override
    public void deleteField(Long cId, Long fId) throws NoResultException {
        Customer c = em.find(Customer.class, cId);
        Field f = em.find(Field.class, fId);

        if (c != null && f != null) {
            c.getFields().remove(f);

            //if no other association between field and customer, we are safe to delete this field
            Query q = em.createQuery("SELECT count(c) FROM Customer c WHERE :field MEMBER OF c.fields");
            q.setParameter("field", f);

            long count = (Long) q.getSingleResult();

            if (count == 0) {
                em.remove(f);
            }
        } else {
            throw new NoResultException("Not found");
        }
    } //end deleteField

    @Override
    public void deleteCustomer(Long cId) throws NoResultException {
        Customer c = em.find(Customer.class, cId);
        
        if (c == null){
            throw new NoResultException("Not found");
        }

        List<Field> fields = c.getFields();
        c.setFields(null);
        
        for (Field f : fields){
            //if no other association between field and customer, we are safe to delete this field
            Query q = em.createQuery("SELECT count(c) FROM Customer c WHERE :field MEMBER OF c.fields");
            q.setParameter("field", f);

            long count = (Long) q.getSingleResult();

            if (count == 0) {
                em.remove(f);
            }
        }
        
        em.remove(c);
    } //end deleteCustomer

    @Override
    public List<Customer> searchCustomersByContact(Contact c) {
        Query q;
        if (c.getPhone() != null) {
            q = em.createQuery("SELECT cust FROM Customer cust, Contact c  WHERE c MEMBER OF cust.contacts AND LOWER(c.phone) LIKE :phone");
            q.setParameter("phone", "%" + c.getPhone().toLowerCase() + "%");
        } else if (c.getEmail() != null) {
            q = em.createQuery("SELECT cust FROM Customer cust, Contact c  WHERE c MEMBER OF cust.contacts AND LOWER(c.email) LIKE :email");
            q.setParameter("email", "%" + c.getEmail().toLowerCase() + "%");
        } else {
            return new ArrayList<Customer>();
        }

        return q.getResultList();
    } //end searchCustomersByContact

    @Override
    public List<Customer> searchCustomersByField(Field f) {
        Query q = em.createQuery("SELECT cust FROM Customer cust, Field f  WHERE f MEMBER OF cust.fields AND LOWER(f.name) = :name AND LOWER(f.fieldValue) LIKE :fieldValue");

        q.setParameter("name", f.getName().toLowerCase());
        q.setParameter("fieldValue", "%" + f.getFieldValue().toLowerCase() + "%");

        return q.getResultList();
    } //end searchCustomersByField

    @Override
    public Set<String> getAllFieldNames() {
        Set<String> results = new HashSet<String>();
        Query q = em.createQuery("SELECT f FROM Field f");

        for (Object field : q.getResultList()) {
            Field field1 = (Field) field;
            results.add(field1.getName().toLowerCase());
        }

        return results;
    } //end listAllFieldNames
}
