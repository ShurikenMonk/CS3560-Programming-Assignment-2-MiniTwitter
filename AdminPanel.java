import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.WindowConstants;

public class AdminPanel extends JFrame{
    private static AdminPanel adminInstance;

    protected static HashMap<String, Group> groups;
    protected static HashMap<String, User> users;

    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode userViewSelection;

    private JFrame popUpFrame;

    private JTree tree;

    private JScrollPane treeScrollPane;

    private JTextField addGroupTextArea;
    private JTextField addUserTextArea;

    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton countUsersButton;
    private JButton countXsButton;
    private JButton countGroupsButton;
    private JButton openUserViewButton;
    private JButton percentPositiveButton;

    private String newGroupName;
    private String newUserName;
    private String errorMessage;

    private static Pattern alnumPattern = Pattern.compile("^[a-zA-Z0-9]*$");
    
    private static final int SCREEN_HEIGHT = 600;
    private static final int SCREEN_WIDTH = 800;

    private AdminPanel(){
        if(adminInstance == null){
            adminInstance = this;
            groups = new HashMap<>();
            users = new HashMap<>();
            initComponents();
        }
    }

    private void initComponents(){
        setTree();
        setButtons();
        setCustomIcons();
        setTextArea();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mini X Admin Panel");
        setLayout(null);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setVisible(true);

        countGroupsButton.addActionListener(e -> {countGroups();});
        countUsersButton.addActionListener(e -> {countUsers();});

        addGroupButton.addActionListener(e -> {newGroupName = addGroupTextArea.getText();
        if(isAlphNum(newGroupName)){addUser(newGroupName);}else{errorMessage = "Group name must be an alphanumeric string with a length of at least one."; JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Error with Group Name", JOptionPane.ERROR_MESSAGE);}});

        addUserButton.addActionListener(e -> {newUserName = addUserTextArea.getText();
        if(isAlphNum(newUserName)){addUser(newUserName);}else{errorMessage = "Username must be an alphanumeric string with a length of at least one."; JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Error with Username", JOptionPane.ERROR_MESSAGE);}});
        
        openUserViewButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actEvent){
                userViewSelection = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                if(userViewSelection != null && !userViewSelection.getAllowsChildren()){((User)userViewSelection).getUserView().setVisible(true);}
                else{errorMessage = "Invalid selection.\n" + "Please select a valid user.";JOptionPane.showMessageDialog(new JFrame(), errorMessage,"Error in User View", JOptionPane.ERROR_MESSAGE);}
            }
        });

        countXsButton.addActionListener(e -> {countXs();});

        percentPositiveButton.addActionListener(e -> {double posPercent = calculatePositivePercent(); String posPercentFormat = String.format("%.1f", posPercent);
        if (Double.isNaN(posPercent)){JOptionPane.showMessageDialog(popUpFrame, "No xMessages currently posted, positive XMessage % cannot be calculated at this moment.");}
        else{JOptionPane.showMessageDialog(popUpFrame, "Positive XMessage Percentage: "+ posPercentFormat + "%.");}
        });
    }

    private double calculatePositivePercent(){
        Collection<X> XSet = new HashSet<X>();
        Collection<User> userSet = users.values();
        PosXVisitor visitor = new PosXVisitor();

        double posCount = 0;

        for(X xMessage : XSet){
            if(xMessage.accept(visitor)){
                posCount++;
            }
        }

        for(User user : userSet){
            XSet.addAll(user.getPersonalFeed().getXMap().values());
        }

        posCount /= XSet.size();
        return posCount *= 100;

    }

    private void countGroups(){
        JOptionPane.showMessageDialog(popUpFrame, "Total # of Groups: " + groups.size());
    }

    private void countXs(){
        int xCount = 0;
        for (User user : users.values()){xCount += user.getPersonalFeed().getXMap().size();}
        JOptionPane.showMessageDialog(popUpFrame, "Total # of XMessages: " + xCount);
    }

    private void countUsers(){
        JOptionPane.showMessageDialog(popUpFrame, "Total # of Users: " + users.size());
    }

    private void setButtons(){
        addGroupButton = new JButton("Add Group");
        addUserButton = new JButton("Add User");
        countGroupsButton = new JButton("Count Groups");
        countXsButton = new JButton("Count XMessages");
        countUsersButton = new JButton("Count Users");
        openUserViewButton = new JButton("Open User");
        percentPositiveButton = new JButton("Show Positive %");

        add(addGroupButton);
        add(addUserButton);
        add(countGroupsButton);
        add(countXsButton);
        add(countUsersButton);
        add(openUserViewButton);
        add(percentPositiveButton);

        addGroupButton.setBounds(600, 20, 100, 50);
        addUserButton.setBounds(600, 75, 100, 50);
        openUserViewButton.setBounds(400, 150, 350, 50);
        countGroupsButton.setBounds(600, 400, 150, 50);
        countXsButton.setBounds(400, 350, 150, 50);
        countUsersButton.setBounds(400, 400, 150, 50);
        percentPositiveButton.setBounds(600, 350, 150, 50);
    }

    private void setTree(){
        Group rootGroup = new Group("Root");
        groups.put("Root", rootGroup);
        root = new DefaultMutableTreeNode(rootGroup);
        tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeScrollPane = new JScrollPane(tree);
        add(treeScrollPane);
        treeScrollPane.setBounds(20,20,350,500);
    }

    private void setTextArea(){
        addGroupTextArea = new JTextField();
        addUserTextArea = new JTextField();

        add(addGroupTextArea);
        add(addUserTextArea);

        addGroupTextArea.setBounds(400, 20, 200, 50);
        addUserTextArea.setBounds(400, 75, 200, 50);
    }

    private void setCustomIcons(){
        tree.setCellRenderer(new DefaultTreeCellRenderer(){@Override
        public Component getTreeCellRendererComponent(JTree tr, Object value, boolean isSelected, boolean expanded, boolean isLeaf, int row, boolean hasFocus){
            Component result = super.getTreeCellRendererComponent(tr, value, isSelected, expanded, isLeaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            
            if(node instanceof Group){this.setIcon(UIManager.getIcon("FileView.directoryIcon"));}
            if(node instanceof User){this.setIcon(UIManager.getIcon("FileView.fileIcon"));}

            return result;
        }});
    }

    private boolean isAlphNum(String s){
        return s.length() > 0 && alnumPattern.matcher(s).find();
    }

    public static AdminPanel getAdmin(){
        if(adminInstance == null){
            adminInstance = new AdminPanel();
        }
        return adminInstance;
    }

    private void addGroup(String name){
        if(!groups.containsKey(name)){Group newGroup = new Group(name); groups.put(name, newGroup);
            
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

            if(node == null){
                root.add(newGroup);
                groups.get("Root").addSubgroup(newGroup);
                updateTree(root);
            }
            else{
                if(node.getAllowsChildren()){
                    node.add(newGroup);
                    groups.get(node.toString()).addSubgroup(newGroup);
                    updateTree(node);
                }
                else{
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
                    parent.add(newGroup);
                    groups.get(parent.toString()).addSubgroup(newGroup);
                    updateTree(parent);
                }
            }
        }
        else{
            errorMessage = "Group name exists, please enter a different one.";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Group Name Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addUser(String name){
        if(!users.containsKey(name)){User newUser = new User(name);users.put(name, newUser);
        
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

            if(node == null){
                root.add(newUser);
                groups.get("Root").addMember(newUser);
                updateTree(root);
            }
            else{
                if(node.getAllowsChildren()){
                    node.add(newUser);
                    groups.get(node.toString()).addMember(newUser);
                    updateTree(node);
                }
                else{
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
                    parent.add(newUser);
                    groups.get(parent.toString()).addMember(newUser);
                    updateTree(parent);
                }
            }
        }
        else{
            errorMessage = "Username exists, please enter a different one.";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Username Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTree(DefaultMutableTreeNode node){
        ((DefaultTreeModel) tree.getModel()).nodesWereInserted(node, new int[]{node.getChildCount()-1});
        for(int i = 0; i<tree.getRowCount();i++){
            tree.expandRow(i);
        }
    }

    public HashMap<String, Group> getGroups(){
        return groups;
    }

    public HashMap<String, User> getUsers(){
        return users;
    }
}