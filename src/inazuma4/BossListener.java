package inazuma4;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.ChannelManager;

public class BossListener  extends ListenerAdapter {

    private TextChannel sendChan;
    
    Logger logger = Logger.getLogger("BossListener");
    
    private Boss b1;
    private Boss b2;
    private Boss b3;
    private Boss b4;
    private Boss b5;
    private Boss of;

    private String currentBoss;
    
    private List<Boss> bossList;
    
    public BossListener() {

        currentBoss = "not set";
        
        b1 = new Boss("b1");
        b2 = new Boss("b2");
        b3 = new Boss("b3");
        b4 = new Boss("b4");
        b5 = new Boss("b5");
        of = new Boss("OF");

        bossList = new ArrayList<Boss>();
        bossList.add(b1);
        bossList.add(b2);
        bossList.add(b3);
        bossList.add(b4);
        bossList.add(b5);
        bossList.add(of);
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //logger.info("Welcome to bossmonland");
        setSendChannelAndBossRoles(event);

        Message msg = event.getMessage();
        // logger.info(msg.getContentDisplay());

        //Refresh is boss agnostic
        if (msg.getContentDisplay().startsWith("!refresh")) {
            printBosses();
        }
        
        for (Boss b : bossList) {
            if (msg.getContentDisplay().contains(b.atName)) {
                sendStatus(event, msg);
                if (!b.bName.equals("OF"))
                currentBoss = b.bName;
                printBosses();
                //updateChannel(sendChan, b.atStatus);
            } else if (msg.getContentDisplay().startsWith(b.intName)) {
                q(event, msg, b);
            } else if (msg.getContentDisplay().startsWith("!reset")) {
                if (adminCheck(event.getAuthor())) {
                    b.reset();
                }
            } else if (msg.getContentDisplay().startsWith(b.rmName)) {
                if (adminCheck(event.getAuthor())) {
                    b.rm(msg);
                }
            }
        }

    }
    
    private boolean adminCheck(User u) {
        if (BossStatus.adminList.contains(u.getId())) {
            return true;
        }
        return false;
        
    }

    private void q(MessageReceivedEvent event, Message msg, Boss b) {
        // update user
        // User u = event.getAuthor();
        Member m = event.getMember();
        b.update(m, msg, event.getGuild());
        // update chart.
        printBosses();
    }

    private void printBosses() {
        // art
        String s = "";
        s = s.concat("**=======================================**\n");
        s = s.concat("Current Boss: " + currentBoss + "\n");
        s = s.concat(b1.printBoss()
                .concat(b2.printBoss().concat(b3.printBoss())
                        .concat(b4.printBoss().concat(b5.printBoss().concat(of.printBoss())))));
        s = s.concat("**=======================================**");
        sendStatus2(s);
    }

    private void sendStatus2(String s) {
        sendChan.sendMessage("Queue ") /* => RestAction<Message> */
                .queue(response /* => Message */ -> {
                    response.editMessageFormat(s).queue();
                });
    }

    private void sendStatus(MessageReceivedEvent event, Message msg) {
        sendChan.sendMessage("setting boss status") /* => RestAction<Message> */
                .queue(response /* => Message */ -> {
                    response.editMessageFormat(msg.getContentDisplay()).queue();
                });
    }

    private void setSendChannelAndBossRoles(MessageReceivedEvent event) {
        if (sendChan == null) {
            //Set Send Channel
            for (GuildChannel gc : event.getGuild().getChannels()) {
                logger.info(gc.getName());
                if (gc.getName().contains("boss_status")) {
                    sendChan = event.getJDA().getTextChannelById(gc.getId());
                    logger.info("send channel name: " + sendChan.getName());
                }
            }
            //Set Boss Roles
            for (Boss b : bossList) {
                if (event.getGuild().getRolesByName(b.bName, true) != null &&
                        event.getGuild().getRolesByName(b.bName, true).size() > 0) {
                    b.role = event.getGuild().getRolesByName(b.bName, true).get(0);    
                }
            }
        }
    }

    private void updateChannel(TextChannel channel, String newTitle) {
        ChannelManager manager = channel.getManager(); // get the manager
        manager.setName(newTitle); // set the new values
        manager.queue(); // execute update, this updates both name and topic
    }
}
