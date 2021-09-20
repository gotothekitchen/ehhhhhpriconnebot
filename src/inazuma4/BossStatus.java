package inazuma4;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class BossStatus {

    // main
    //private static String token = "token";
    // test
    private static String token = "token";

    Logger logger = Logger.getLogger("BossStatus");

    public static List<String> adminList;
    
    public static void main(String[] args) throws LoginException {

        adminList = new ArrayList<String>();
        //me
        adminList.add("discordid");

        new JDABuilder(token).addEventListeners(new BossListener()).setActivity(Activity.playing("Boss Monitor"))
                .build();
    }
}
