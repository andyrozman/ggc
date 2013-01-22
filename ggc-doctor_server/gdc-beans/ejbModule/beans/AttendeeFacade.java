package beans;

import javax.ejb.*;
import javax.persistence.*;

import com.atech.beans.jpa.AbstractFacade;

@Stateless
public class AttendeeFacade extends AbstractFacade<Attendee>
{
    @PersistenceContext(unitName = "RegistrationAppJPA")
    private EntityManager em;

    protected EntityManager getEntityManager()
    {
        return em;
    }

    public AttendeeFacade()
    {
        super(Attendee.class);
    }

}
