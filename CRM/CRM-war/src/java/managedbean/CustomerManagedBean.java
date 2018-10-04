package managedbean;

import entity.Customer;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import session.CustomerSessionLocal;


@ManagedBean
@RequestScoped
public class CustomerManagedBean {
    @EJB
    CustomerSessionLocal customerSessionLocal;
    
    private String name;
    private byte gender;
    private Date dob;
    
    public CustomerManagedBean() {
    }
    
    public void addCustomer(ActionEvent evt){
        Customer c = new Customer();
        c.setName(name);
        c.setGender(gender);
        c.setDob(dob);
        c.setCreated(new Date());

        customerSessionLocal.createCustomer(c);
    } //end addCustomer

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the gender
     */
    public byte getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(byte gender) {
        this.gender = gender;
    }

    /**
     * @return the dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }
}
