import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

public class Group extends DefaultMutableTreeNode implements SystemEntry{
    private String groupName;
    private List<User> userList;
    private List<Group> groupList;
    private long creationTime;
    private long lastUpdateTime;

    public Group(String name, User... users){
        userList = new ArrayList<>();
        groupList = new ArrayList<>();
        for(User user : users){
            userList.add(user);
        }
        setAllowsChildren(true);
        groupName = name;
        creationTime = System.currentTimeMillis();
        lastUpdateTime = System.currentTimeMillis();
    }

    public void addMember(User user){
        if(!userList.contains(user) && !user.inGroup()){
            userList.add(user);
        }
        lastUpdateTime = System.currentTimeMillis();
    }

    public void addSubgroup(Group group){
        if(!groupList.contains(group)){
            groupList.add(group);
        }
        lastUpdateTime = System.currentTimeMillis();
    }

    public void setGroupName(String name){
        groupName = name;
    }

    public List<User> getUserList(){
        return userList;
    }

    public List<Group> getGroupList(){
        return groupList;
    }

    public String toString(){
        return groupName;
    }

    public long getCreationTime(){
        return creationTime;
    }

    public long getLastUpdateTime(){
        return lastUpdateTime;
    }
}