public class X implements Message, Visitable{
    private static int idCounter = 0;
    private int xId;
    private User author;
    private String message;

    public X(User user, String text){
        xId = idCounter;
        author = user;
        message = text;
        idCounter++;
    }

    public int getXID(){
        return xId;
    }

    @Override
    public User getAuthor(){
        return author;
    }

    @Override
    public String getMessage(){
        return message;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append('@');
        sb.append(author);
        sb.append(": ");
        sb.append(message);
        return sb.toString();
    }

    @Override
    public boolean accept(Visitor vis){
        return vis.visitX(this);
    }
}