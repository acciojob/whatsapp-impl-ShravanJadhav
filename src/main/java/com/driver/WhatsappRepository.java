package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    //HashSet<User> user = new HashSet<>();
    //HashSet<Message>message = new HashSet<>();

    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile)){
            throw new Exception("User already there");
        }

        User user = new User(name,mobile);
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group creatGroup(List<User> users) {
        String groupName = "";
        if(users.size()>2){
            groupName =  "Group "+ this.customGroupCount++;
        }else{
            groupName = users.get(1).getName();
        }
         Group group = new Group(groupName, users.size());
        groupMessageMap.put(group, new ArrayList<Message>());
        groupUserMap.put(group,users);
        adminMap.put(group,users.get(0));

        return group;
    }


    public int createMessage(String content) {

        Message message = new Message(this.messageId++, content);

        return message.getId();

    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");

        boolean isMember = false;
        List<User> userList = groupUserMap.get(group);
        for(User user : userList){
            if(user.equals(sender)){
                isMember=true;
                break;
            }
        }
        if(isMember==false) throw new Exception("You are not allowed to send message");

//        if(!groupUserMap.get(group).contains(sender)){
//            throw new Exception("You are not allowed to send message");
//        }

        List<Message> messageList = new ArrayList<>();
        if(groupMessageMap.containsKey(group)){
            messageList = groupMessageMap.get(group);
        }
        messageList.add(message);
        groupMessageMap.put(group,messageList);
        return messageList.size();
    }


    public String chnageAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if(!adminMap.get(group).equals(approver)) throw new Exception("Approver does not have rights");


        boolean isMember= false;
        List<User> userList = groupUserMap.get(group);
        for(User user1 : userList){
            if(user1.equals(user)){
                isMember=true;
                break;
            }
        }
        if(isMember==false) throw new Exception("User is not a participant");

//        if(!groupUserMap.get(group).contains(user)){
//            throw new Exception("User is not a participant");
//        }

        adminMap.put(group, user);
        return "SUCCESS";
    }


//    public int removeUser(User user)throws Exception {
//
//        return 0;
//    }

//    public String findMessage(Date start, Date end, int k) throws Exception {
//         List<Message>
//    }
}
