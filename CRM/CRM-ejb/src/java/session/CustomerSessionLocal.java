
package session;

import entity.Contact;
import entity.Customer;
import entity.Field;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import error.NoResultException;


@Local
public interface CustomerSessionLocal {
    public List<Customer> searchCustomers(String name);
    public Customer getCustomer(Long cId) throws NoResultException;
    public void createCustomer(Customer c);
    public void updateCustomer(Customer c) throws NoResultException;
    public void addContact(Long cId, Contact c) throws NoResultException;
    public void addField(Long cId, Field f) throws NoResultException;
    public void deleteContact(Long cId) throws NoResultException;
    public void deleteField(Long cId, Long fId) throws NoResultException;
    public void deleteCustomer(Long cId) throws NoResultException;
    public List<Customer> searchCustomersByContact(Contact c);
    public List<Customer> searchCustomersByField(Field f);
    public Set<String> getAllFieldNames();
}
