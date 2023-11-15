import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UserView extends JFrame{
    private User userInstance;

    private JTextArea followUserTextArea;
    private JTextArea xMessageTextArea;

    private JButton followUserButton;
    private JButton postXButton;

    private JList<String> followingList;
    private JList<String> newsFeedList;

    private DefaultListModel<String> followingListModel;
    private DefaultListModel<String> newsFeedModel;

    private JScrollPane followingScrollPane;
    private JScrollPane newsFeedScrollPane;

    private String followedUser;
    private String xContent;
    private String errorMessage;

    private static final int SCREEN_WIDTH = 750;
    private static final int SCREEN_HEIGHT = 550;

    public UserView(User user){
        userInstance = user;
        initComponents();
    }

    private void initComponents(){
        setButtons();
        setTextAreas();
        setLists();
        setTitle("User View - " + userInstance.toString());
        setLayout(null);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        followingScrollPane.setBounds(20, 100, 600, 100);
        newsFeedScrollPane.setBounds(20, 100, 600, 75);
        updateNewsFeed();

        followUserButton.addActionListener(e -> { followUser();});
        postXButton.addActionListener(e -> {postX();});
    }

    private void setButtons(){
        followUserButton = new JButton("Follow User");
        postXButton = new JButton("Post!");

        add(followUserButton);
        add(postXButton);

        followUserButton.setBounds(600, 20, 110, 50);
        postXButton.setBounds(600, 270, 110, 50);
    }

    private void setTextAreas(){
        followUserTextArea = new JTextArea();
        xMessageTextArea = new JTextArea();
        followUserTextArea.setLineWrap(true);
        xMessageTextArea.setLineWrap(true);

        add(followUserTextArea);
        add(xMessageTextArea);

        followUserTextArea.setBounds(20, 20, 500, 50);
        xMessageTextArea.setBounds(20, 270, 500, 50);
    }

    private void setLists(){
        followingListModel = new DefaultListModel<>();
        followingList = new JList<>(followingListModel);
        followingScrollPane = new JScrollPane(followingList);

        newsFeedModel = new DefaultListModel<>();
        newsFeedList = new JList<>(newsFeedModel);
        newsFeedScrollPane = new JScrollPane(newsFeedList);

        add(followingScrollPane);
        add(newsFeedScrollPane);

        updateFollowing();
        updateNewsFeed();
    }

    public void updateFollowing(){
        for (User user : userInstance.getFollowingList()){
            followingListModel.addElement(user.toString());
        }
    }

    public void updateNewsFeed(){
        newsFeedModel.removeAllElements();
        for(X xMessage : userInstance.getNewsFeed().getRevOrdTweetList()){
            newsFeedModel.addElement(xMessage.toString());
        }
    }

    private void followUser(){
        followedUser = followUserTextArea.getText();
        HashMap<String, User> userMap = AdminPanel.getAdmin().getUsers();
        if(followedUser.equals(userInstance.toString())){
            errorMessage = "You can't follow yourself.";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Follow User Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(userMap.containsKey(followedUser)){
            User followed = userMap.get(followedUser);
            if(!userInstance.getFollowingList().contains(followed)){
                userInstance.followUser(userMap.get(followedUser));
                followingListModel.add(0, followedUser);
            }
            else{
                errorMessage = "You are already following this user.";
                JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Follow User Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            errorMessage = "Given user does not exist.";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Follow User Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void postX(){
        xContent = xMessageTextArea.getText();
        if(xContent.length() > 0){
            userInstance.postX(xContent);
            newsFeedModel.add(0, userInstance.getMostRecentX().toString());
        }
        userInstance.notifyObservers();
    }
}