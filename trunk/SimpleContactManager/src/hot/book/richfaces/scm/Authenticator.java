package book.richfaces.scm;

import javax.ejb.Local;

@Local
public interface Authenticator {

    boolean authenticate();

}
