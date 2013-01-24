package msmb.commonUtilities;


public class MSMB_InterfaceChange<T> {
	T elementBefore = null;
	T elementAfter = null;
	 
	public T getElementBefore() {	return elementBefore; }
	public T getElementAfter() {	return elementAfter;}

	public void setElementBefore(T elementBefore) {	this.elementBefore = elementBefore;	}
	public void setElementAfter(T elementAfter) {	this.elementAfter = elementAfter;	}
	
	public MSMB_InterfaceChange(Class<T> cls)     {       type= cls;    }
	 private Class<T> type;
	 public Class<T> getType(){return type;}
	
}
