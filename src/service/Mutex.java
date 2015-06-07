package service;

public class Mutex {
   
    private int value;
    
    public Mutex(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public void increment(){
        this.value++;
    }
    
    @Override
    public int hashCode(){
        return value;
    }
    
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Mutex)) return false;
        Mutex other = (Mutex)obj;
        return (this.value == other.value) ;
    }
    
    public String toString(){
        return ("" + value);
    }
}
