package beans;

import javax.interceptor.*;

public class SimpleInterceptor
{
    @AroundInvoke
    public Object simpleMethod(InvocationContext context) throws Exception
    {
        System.out.println("SimpleInterceptor entered: " + context.getMethod().getName());
        Object result = context.proceed();
        System.out.println("SimpleInterceptor exited: " + context.getMethod().getName());
        return result;
    }
}
