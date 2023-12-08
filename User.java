import java.util.ArrayList;
import java.util.List;

public class User extends Subject implements Observer, SystemEntry{
    private String userId;
    private List<User> followersList;
    private List<User> followingList;

    private String groupName;
    private Feed personalFeed;
    private Feed newsFeed;
    private long creationTime;
    private long lastUpdateTime;

    private UserView userView;

    public User(String name){
        userId = name;
        groupName = "Root";
        followersList = new ArrayList<>();
        followingList = new ArrayList<>();
        newsFeed = new Feed();
        observers = new ArrayList<>();
        personalFeed = new Feed();
        creationTime = System.currentTimeMillis();
        lastUpdateTime = System.currentTimeMillis();
        setAllowsChildren(false);
        userView = new UserView(this);
        updateLUPQ();
    }

    public void followUser(User user){
        followingList.add(user);
        user.getFollowersList().add(this);
        newsFeed.mergeFeed(user.getPersonalFeed());
        user.attach(this);
        lastUpdateTime = System.currentTimeMillis();
        updateLUPQ();
    }

    public List<User> getFollowersList(){
        return followersList;
    }

    public List<User> getFollowingList(){
        return followingList;
    }

    public String getUser(){
        return userId;
    }

    public String getGroup(){
        return groupName;
    }

    public Feed getNewsFeed(){
        return newsFeed;
    }

    public Feed getPersonalFeed(){
        return personalFeed;
    }

    public UserView getUserView(){
        return userView;
    }

    public X getMostRecentX(){
        return newsFeed.getRevOrdTweetList().get(0);
    }

    public long getCreationTime(){
        return creationTime;
    }

    public long getLastUpdateTime(){
        return lastUpdateTime;
    }

    public boolean inGroup(){
        return groupName != "Root";
    }

    public void postX(String message){
        X newXMessage = new X(this, message);
        personalFeed.addToFeed(newXMessage);
        newsFeed.addToFeed(newXMessage);
        lastUpdateTime = System.currentTimeMillis();
        updateLUPQ();
    }

    @Override
    public void update(Subject subject){
        if(subject instanceof User){
            X newXMessage = ((User)subject).getMostRecentX();
            this.getNewsFeed().addToFeed(newXMessage);
            this.userView.updateNewsFeed();
        }
    }

    public String toString(){
        return userId;
    }

    private void updateLUPQ(){
        AdminPanel.getAdmin().getUserPriorityQueue().remove(this);
        AdminPanel.getAdmin().getUserPriorityQueue().add(this);
    }
}