package inazuma4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class Boss {
    
    //name
    public ArrayList<String> order;
    public String bName;
    public String atName;
    public String intName;
    public String atStatus;
    public String rmName;
    public Role role;
    
    //user id and name
    public Map<String, QueueEntry> entries;
    
    Logger logger = Logger.getLogger("Boss");

    
    public Boss (String bName) {
        this.bName = bName;
        atName = "@" + bName;
        intName = "!" + bName;
        atStatus = atName + "_boss_status";
        rmName = "!rm " + bName;
        order = new ArrayList<String>();
        entries = new HashMap<String, QueueEntry>();
    }
    
    public void update(Member u, Message m, Guild g) {
        // Get any message
        String msg = m.getContentDisplay();
        if (msg.length() > 3) {
            msg = msg.substring(3);    
        } else {
            msg = "";
        }
        
        if (entries.keySet().contains(u.getId())) {
            //remove user if no message is attached
            if (msg == "") {
                entries.remove(u.getId());
                order.remove(u.getId());
                //remove role
                g.removeRoleFromMember(m.getAuthor().getId(), role).queue();
            } else {
                //update
                QueueEntry entry = entries.get(u.getId());
                entry.status = msg;
                //String newName = "**" + u.getEffectiveName() + "**" + msg;

                //users.put(u.getId(), newName);
                //I think we can be certain it will always find the index
                //order.set(order.indexOf(entry), newName);
                
            }

        } else {
            //add user
            //logger.info("adding user " + u.getEffectiveName());
            //String name = "**" + u.getEffectiveName() + "**" + msg;
            QueueEntry entity = new QueueEntry(msg, u);
            entries.put(u.getId(), entity);
            order.add(u.getId());
            //add role
            logger.fine("role pring " + role.getId() + " : " + role.getName());
            g.addRoleToMember(m.getAuthor().getId(), role).queue();;
        }
    }
    
    public void reset() {
        order = new ArrayList<String>();
        entries = new HashMap<String, QueueEntry>();
    }
    
    public void rm (Message m) {
        String s = m.getContentDisplay().substring(rmName.length());
        int index = Integer.parseInt(s.strip());
        if (index < order.size()) {
            String o = order.get(index);
            order.remove(index);
            entries.values().removeIf(a -> o.equals(a));
        }
    }
    
    public String printBoss() {
        String s = bName + "-------------------------------";
        for (String ss : order) {
            s = s.concat("\n " + entries.get(ss).print());
        }
        s = s.concat("\n");
        return s;
    }
}
