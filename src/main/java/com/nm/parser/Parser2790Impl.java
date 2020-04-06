package com.nm.parser;

import com.nm.pojo.Operations;
import com.nm.pojo.Wagon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser2790Impl implements Parser2790 {
    private final Logger logger = LogManager.getLogger(Parser2790Impl.class.getName());
    private final String DH = "\\d{1,2}\\s+\\:\\s+(?<nv>\\d{8})\\s+[A-ZА-Я]{2}\\-(?<s>\\d{6})\\-(?<po>\\d{6})";
    private final String DB = "((?<nv>\\d{8})\\s+(?<sobst>\\d{2})\\s+" +
            "(?<ves>[A-ZА-Я]?\\d{0,3})\\s+(?<stnazn>\\d{4}\\s+)?" +
            "(?<gruz>\\d{5}\\s+)?(?<pol>\\d{4}\\s+)?)?" +
            "(?<state>\\+?\\-?[A-ZА-Я]?)(?<oper>\\s?[A-ZА-Я]{2,5})\\s+(?<date>\\d{2}\\/\\d{2}\\/\\d{2})\\s+" +
            "(?<time>\\d{2}\\:\\d{2})\\s+((?<stoper>\\d{5}\\s)?" +
            "(?<pp>\\s{0,7}\\d{2}\\/\\d{2}\\s)?(?<np>\\s{0,13}\\d{4}\\s)?" +
            "(?<ip>\\s{0,18}\\d{4}\\-\\d{3}\\-\\d{4}\\s)?(?<v>\\s{0,32}\\d{1,2}\\/\\d{1,2}\\s)?" +
            "(?<gh>\\s{0,38}\\S{1,10}\\-\\d{8})?)?";

    @Override
    public Wagon parsing(String str) {
        str = strReplace(str);
        Wagon wagon = new Wagon();
        List<Operations> listO = new ArrayList<>();

        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(DH);
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            wagon.setWagNo(Integer.parseInt(matcher.group("nv").trim()));
            wagon.setFromDate(parseToDate(matcher.group("s").trim()));
            wagon.setToDate(parseToDate(matcher.group("po").trim()));
        }

        pattern = Pattern.compile(DB);
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            Operations o = new Operations();
//            System.out.println("Вагон №:" + matcher.group("nv") +
//                    " sobst " + matcher.group("sobst") +
//                    " ves " + matcher.group("ves") +
//                    " stnazn " + matcher.group("stnazn") +
//                    " gruz " + matcher.group("gruz") +
//                    " pol " + matcher.group("pol") +
//                    " state " + matcher.group("state") +
//                    " oper " + matcher.group("oper") +
//                    " date " + matcher.group("date") +
//                    " time " + matcher.group("time") +
//                    " stoper " + matcher.group("stoper") +
//                    " pp " + matcher.group("pp") +
//                    " np " + matcher.group("np") +
//                    " ip " + matcher.group("ip") +
//                    " v " + matcher.group("v") +
//                    " gh " + matcher.group("gh"));
            o.setState(matcher.group("state") != null ? matcher.group("state").trim() : null);
            o.setOperation(matcher.group("oper").trim());
            o.setDateTime(getDateTime(matcher.group("date").trim(), matcher.group("time").trim()));
            o.setOperSt(matcher.group("stoper") != null ? Integer.parseInt(matcher.group("stoper").trim()) : 0);
            o.setParkPut(matcher.group("pp") != null ? matcher.group("pp").trim() : null);
            o.setNPoezd(matcher.group("np") != null ? Integer.parseInt(matcher.group("np").trim()) : 0);
            o.setIdx(matcher.group("ip") != null ? matcher.group("ip").trim() : null);
            o.setNWags(matcher.group("v") != null ? matcher.group("v").trim() : null);
            o.setGolXv(matcher.group("gh") != null ? matcher.group("gh").trim() : null);
            listO.add(o);
        }
        wagon.setList(listO);
//        System.out.println(wagon.toString());
        System.out.println("SO GOOD!!!");
        logger.info("So GOOD!!!!!");
        return wagon;
    }

    private Timestamp getDateTime(String date, String time) {
        java.util.Date newDate = new Date();
        try {
            SimpleDateFormat inputDateformat = new SimpleDateFormat("dd/MM/yy HH:mm");
            SimpleDateFormat outputDateformat = new SimpleDateFormat("dd.MM.yyyy");
            newDate = inputDateformat.parse(date + " " + time);
        } catch (ParseException ex) {
            logger.warn("ParseE: " + ex);
        }
        return new Timestamp(newDate.getTime());
    }

    private String strReplace(String str) {
        String text = str.replace("A", "А").replace("B", "В").
                replace("C", "С").replace("E", "Е").
                replace("H", "Н").replace("K", "К").
                replace("M", "М").replace("O", "О").
                replace("P", "Р").replace("T", "Т").
                replace("X", "Х").replace("Y", "У").
                replace("W", "Ш").replace("?", "Ш");
        return text;
    }

    private Timestamp parseToDate(String s) {
        String outputText = "";
        java.util.Date date = new Date();
        try {
            SimpleDateFormat inputDateformat = new SimpleDateFormat("ddMMyy");
            SimpleDateFormat outputDateformat = new SimpleDateFormat("dd.MM.yyyy");
            date = inputDateformat.parse(s);
            outputText = outputDateformat.format(date);
        } catch (ParseException ex) {
            logger.warn("ParseE: " + ex);
        }
        return new Timestamp(date.getTime());
//        return outputText;
    }
}
