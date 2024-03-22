package Utils.Events;

import Utils.Observer.Observer;
import Utils.Observer.Subject;

import java.util.ArrayList;

public class MockSubject implements Subject {

    private ArrayList<Observer> observers;

    public MockSubject (){
        this.observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {

        this.observers.add(observer);

    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notify(Event event) {
        for (Observer observer: observers){
            observer.update(this,event);
        }
    }
}
