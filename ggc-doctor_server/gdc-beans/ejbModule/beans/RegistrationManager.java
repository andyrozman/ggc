package beans;

import javax.ejb.*;
import javax.interceptor.Interceptors;

@Stateful
@Interceptors(SimpleInterceptor.class)
public class RegistrationManager
{
    @EJB
    AttendeeFacade attendeeFacade;
    Attendee attendee;

    
    @Interceptors({MethodInterceptor.class, ValidationInterceptor.class, SecurityInterceptor.class})
    public Attendee register(String name, String title, String company)
    {
        attendee = new Attendee(name, title, company);
        attendeeFacade.create(attendee);
        return attendee;
    }
}
