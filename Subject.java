import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class Subject extends DefaultMutableTreeNode{
    protected List<Observer> observers;

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void detach(Observer observer){
        observers.remove(observer);
    }
    
    public void notifyObservers(){
        for(Observer observer : observers){
            observer.update(this);
        }
    }
}