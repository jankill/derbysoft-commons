package com.derbysoft.common.util;

import com.derbysoft.common.util.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhupan
 * @version 1.1
 * @since 2012-5-25
 */
public abstract class EmailUtils {

    public static boolean checkEmail(String mail) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mail);
        return m.find();
    }

    public static List<String> toEmailList(String emails) {
        List<String> emailList = Lists.newArrayList();
        for (String email : SplitUtils.split(emails)) {
            if (checkEmail(email)) {
                emailList.add(email);
            }
        }
        return emailList;
    }
    public static boolean checkEmails(String emails) {
        return checkEmails(SplitUtils.split(emails));
    }

    public static boolean checkEmails(String[] emails) {
        for (String email : emails) {
            if (!EmailUtils.checkEmail(email)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkEmails(List<String> emails) {
        return checkEmails(emails.toArray(new String[0]));
    }
}
