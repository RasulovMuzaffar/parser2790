import com.nm.db.WagData;
import com.nm.db.WagDataImpl;
import com.nm.pojo.Operations;
import org.joda.time.*;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyTest {

    private volatile int i = 0;
    private volatile Timestamp dOld;
    private volatile Timestamp startDate;
            int st = 0;

    @Test
    public void getOpers() {
        Timestamp oldDate;
        WagData wd = new WagDataImpl();
        List<Operations> listO = wd.getOpersByWagon(28002202);
        listO.stream().forEach(o -> {
            i++;
            if (i == 1)
                startDate = o.getDateTime();
            if (o.getOperation().equalsIgnoreCase("приб")) {
                dOld = o.getDateTime();
                st = o.getOperSt();
            }
            if (o.getOperation().equalsIgnoreCase("отпр")) {
                System.out.println(st + " " + o.getOperation() + " " + o.getDateTime() + " stay time:" + subDates(dOld, o.getDateTime()));
            }
            if (o.getOperation().equalsIgnoreCase("сдан")) {
                System.out.println(o.getOperSt() + " " + o.getOperation() + " " + o.getDateTime() + " stay time:" + subDates(startDate, o.getDateTime()));
            }
        });

        System.out.println(i);
    }
    private String subDates2(Timestamp dOld, Timestamp dNew) {
        long d = dNew.getTime() - dOld.getTime();

        return "";
    }
    private String subDates(Timestamp dOld, Timestamp dNew) {
        System.out.println(dOld + " " + dNew);
        LocalTime ltOld = dOld.toLocalDateTime().toLocalTime();
        LocalTime ltNew = dNew.toLocalDateTime().toLocalTime();

//        System.out.println(ltNew.minus(ltOld.toNanoOfDay(), ChronoUnit.NANOS));
        return "" + ltNew.minus(ltOld.toNanoOfDay(), ChronoUnit.NANOS);
        //        long diffInMillies = dNew.getTime() - dOld.getTime();
//
//        //create the list
//        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
//        Collections.reverse(units);
//
//        //create the result map of TimeUnit and difference
//        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
//        long milliesRest = diffInMillies;
//
//        for ( TimeUnit unit : units ) {
//
//            //calculate difference in millisecond
//            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
//            long diffInMilliesForUnit = unit.toMillis(diff);
//            milliesRest = milliesRest - diffInMilliesForUnit;
//
//            //put the result in the map
//            result.put(unit,diff);
//        }
//        return "result";


//        System.out.println(dOld);
//        System.out.println(dNew);
//
//        System.out.println(">>>>> " + Hours.hoursBetween(new DateTime(dOld), new DateTime(dNew)).getHours());
//        System.out.println(">>>>> " + Minutes.minutesBetween(new DateTime(dOld), new DateTime(dNew)).getMinutes());
//
//        long time = dNew.getTime() - dOld.getTime();
//        System.out.println("--- " + time);
//        ZoneId zid = ZoneId.of("Asia/Tashkent");
//        Format f = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
//        String strResult = f.format(time);
//
//        System.out.println(strResult);
////return strResult;
//
//        //создаём объект класса Calendar и присваиваем ему наше время в миллисекундах
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tashkent"));
//        System.out.println("----- " + cal.getTime());
//        cal.setTimeInMillis(time);
//
////Используя класс SimpleDateFormat создаём модель отображения времени
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//
////выводим отформатированное время в консоль
////        System.out.println(format.format(cal.getTime()));
//        return format.format(cal.getTime());
////        return String.format("%02d:%02d:%02d", time / 3600, time / 60 % 60, time % 60);
    }
}
