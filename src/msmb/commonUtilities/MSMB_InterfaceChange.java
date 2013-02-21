package msmb.commonUtilities;


/*public class MSMB_InterfaceChange<T> {
	T elementBefore = null;
	T elementAfter = null;
	 
	public T getElementBefore() {	return elementBefore; }
	public T getElementAfter() {	return elementAfter;}

	public void setElementBefore(T elementBefore) {	this.elementBefore = elementBefore;	}
	public void setElementAfter(T elementAfter) {	this.elementAfter = elementAfter;	}
	
	public MSMB_InterfaceChange(Class<T> cls)     {       type= cls;    }
	 private Class<T> type;
	 public Class<T> getType(){return type;}
	
}*/


public class MSMB_InterfaceChange {
	ChangedElement elementBefore = null;
	ChangedElement elementAfter = null;
	 
	public ChangedElement getElementBefore() {	return elementBefore; }
	public ChangedElement getElementAfter() {	return elementAfter;}

	public void setElementBefore(ChangedElement elementBefore) {	this.elementBefore = elementBefore;	}
	public void setElementAfter(ChangedElement elementAfter) {	this.elementAfter = elementAfter;	}
	
	public MSMB_InterfaceChange(MSMB_Element t)     {       type= t;    }
	 private MSMB_Element type;
	 public MSMB_Element getType(){return type;}
	
}

