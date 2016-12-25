package beans;

import javax.interceptor.*;

public class MethodInterceptor
{
    @AroundInvoke
    public Object methodLevel(InvocationContext context) throws Exception
    {
        System.out.println("Method Interceptor: Invoking method: " + context.getMethod().getName());
        Object result = context.proceed();
        System.out.println("Method Interceptor: Returned from method: " + context.getMethod().getName());
        return result;
    }
}
