package beans;

import javax.interceptor.*;

public class DefaultInterceptor
{
    @AroundInvoke
    public Object defaultMethod(InvocationContext context) throws Exception
    {
        System.out.println("Default Interceptor: Invoking method: " + context.getMethod().getName());
        Object result = context.proceed();
        System.out.println("Default Interceptor: Returned from method: " + context.getMethod().getName());
        return result;
    }
}
