package com.dbcloner.common;
/*
 * Author Gokul CT
 */

public abstract class IController extends BaseController{
    public abstract void initialise();
    public abstract void initialise(Object[] parameter);
}
