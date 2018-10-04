package manager;

import entity.Contact;
import entity.Customer;
import entity.Field;
import error.NoResultException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import session.CustomerSessionLocal;

public class CustomerManager {

    private CustomerSessionLocal customerSessionLocal;

    public CustomerManager() {
    }

    public CustomerManager(CustomerSessionLocal customerSessionLocal) {
        this.customerSessionLocal = customerSessionLocal;
    }

    public Customer getCustomer(Long cId) throws Exception {
        return customerSessionLocal.getCustomer(cId);
    }

    public void updateCustomer(Long cId, String name, byte gender, String dob) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Date dob1 = df.parse(dob);

        Customer c = new Customer();
        c.setId(cId);
        c.setName(name);
        c.setGender(gender);
        c.setDob(dob1);
        c.setCreated(new Date());

        customerSessionLocal.updateCustomer(c);
    }

    public void createCustomer(String name, byte gender, String dob) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Date dob1 = df.parse(dob);

        Customer c = new Customer();
        c.setName(name);
        c.setGender(gender);
        c.setDob(dob1);
        c.setCreated(new Date());

        customerSessionLocal.createCustomer(c);
    }

    public List<Customer> searchCustomer(String name) {
        return customerSessionLocal.searchCustomers(name);
    }

    public List<Customer> searchCustomersByField(String name, String value) {
        Field f = new Field();
        f.setName(name);
        f.setFieldValue(value);
        return customerSessionLocal.searchCustomersByField(f);
    }

    public List<Customer> searchCustomersByPhone(String phone) {
        Contact c = new Contact();
        c.setPhone(phone);
        return customerSessionLocal.searchCustomersByContact(c);
    }

    public List<Customer> searchCustomersByEmail(String email) {
        Contact c = new Contact();
        c.setEmail(email);
        return customerSessionLocal.searchCustomersByContact(c);
    }

    public Set<String> getAllFieldNames() {
        return customerSessionLocal.getAllFieldNames();
    }

    public void addField(Long cId, String name, String value) throws NoResultException {
        Field f = new Field();
        f.setName(name);
        f.setFieldValue(value);

        customerSessionLocal.addField(cId, f);
    }

    public void addPhone(Long cId, String value) throws NoResultException {
        Contact c = new Contact();
        c.setPhone(value);

        customerSessionLocal.addContact(cId, c);
    }

    public void addEmail(Long cId, String value) throws NoResultException {
        Contact c = new Contact();
        c.setEmail(value);

        customerSessionLocal.addContact(cId, c);
    }

    public void deleteField(Long cId, Long fId) throws NoResultException {
        customerSessionLocal.deleteField(cId, fId);
    }

    public void deleteContact(Long cId) throws NoResultException {
        customerSessionLocal.deleteContact(cId);
    }

    public void deleteCustomer(Long cId) throws NoResultException {
        customerSessionLocal.deleteCustomer(cId);
    }
}
