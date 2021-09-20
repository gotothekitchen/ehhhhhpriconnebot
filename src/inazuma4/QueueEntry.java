package inazuma4;

import java.time.Duration;
import java.time.LocalDateTime;

import net.dv8tion.jda.api.entities.Member;

public class QueueEntry {

    public String status;
    public LocalDateTime timeStamp;
    public Member u;
    
    public QueueEntry (String status, Member u) {
        this.status = status;
        this.u = u;
        timeStamp = LocalDateTime.now();
    }
    
    public String print() {
        //long diff = ChronoUnit.MINUTES.between(timeStamp, LocalDateTime.now());
        return "**" + u.getEffectiveName() + "** " + timeFormat() + status + "<:foomdespair:613948468129431584>";
    }
    
    private String timeFormat() {
        Duration d = Duration.between(timeStamp, LocalDateTime.now());
        int minutes = d.toMinutesPart();
        String m = "";
        if (minutes < 10) {
            m = "0" + minutes;
        } else {
            m = "" + minutes;
        }
        
        return d.toHours() + ":" + m;
    }
}
