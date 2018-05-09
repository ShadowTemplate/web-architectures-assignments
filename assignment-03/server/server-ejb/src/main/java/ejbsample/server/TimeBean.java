package ejbsample.server;

import ejbsample.common.TimeI;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Stateless
@Remote(TimeI.class)
public class TimeBean implements TimeI {

    @Override
    public String getDate() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    }
}
