package horseraceapp.controller.jsp.customtag;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class MessageTag extends SimpleTagSupport {
    private String message;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        
        try {
            out.print(message);
        } catch (java.io.IOException ex) {
            throw new JspException("Error in MessageTag tag", ex);
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
