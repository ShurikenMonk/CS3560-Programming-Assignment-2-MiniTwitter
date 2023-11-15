public class Driver{
    public static void main(String[] args){
        AdminPanel admin = AdminPanel.getAdmin();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run(){
                admin.setVisible(true);
            }
        });
    }
}