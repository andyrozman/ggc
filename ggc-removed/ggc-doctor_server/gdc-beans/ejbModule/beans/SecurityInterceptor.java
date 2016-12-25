package beans;

import javax.annotation.*;
import javax.annotation.security.*;
import javax.ejb.*;
import javax.interceptor.*;

@DeclareRoles("employee")
public class SecurityInterceptor
{
    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object verifyAccess(InvocationContext context) throws Exception
    {
        System.out.println("SecurityInterceptor: Invoking method: " + context.getMethod().getName());
        if (sessionContext.isCallerInRole("employee"))
        {
            Object result = context.proceed();
            System.out.println("SecurityInterceptor: Returned from method: " + context.getMethod().getName());
            return result;
        }
        else
        {
            throw new EJBAccessException();
        }
    }
}
