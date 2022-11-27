package modele.plateau;

public class Timer {
    private long startTime = System.currentTimeMillis();

    public long getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
    public String getStr() {
        return getElapsedTime()/60 + " : " + getElapsedTime()% 60;
    }
}