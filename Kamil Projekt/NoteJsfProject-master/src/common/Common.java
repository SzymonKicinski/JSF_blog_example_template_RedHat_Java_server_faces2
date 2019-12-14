package common;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class Common {
	
	public static String getKeyFromBundle(String bundleName, String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
	    ResourceBundle bundle = 
	        facesContext.getApplication().getResourceBundle(
	            facesContext, bundleName);
	    return bundle.getString(key);
	}

}
