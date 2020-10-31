package entity.ui;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Marquee extends Text {
	private int speedSec = 15;
	
	private StackPane pnlMain = null;
    private TranslateTransition marqueeTT;  
    
    public Marquee () {
        this("");
    }
    
    public Marquee (String text){
        this.setText(text);
    }
     
    public void setScrollDuration(int seconds) {
        this.speedSec = seconds;
    }
    
    public void play() {
        marqueeTT.play();
    }
    
    public void pause() {
        marqueeTT.pause();
    }   
    
    public void setBoundsFrom(StackPane pnl){
        this.pnlMain = pnl;
    }  
    
    public StackPane getBoundsPane() {
    	return this.pnlMain;
    }

    public void run() {
        marqueeTT = new TranslateTransition(Duration.seconds(this.speedSec), this); 
        
        marqueeTT.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                reRunMarquee(false);
            }
    	});

        runMarquee(true);
    }    
    
    public void stop() {
    	marqueeTT.stop();
    	
    	this.setTranslateX(0);
    }
    
    private void runMarquee(boolean firstTime) {
        reRunMarquee(firstTime);
    }
        
    private void reRunMarquee(boolean firstTime) {
        marqueeTT.setDuration(Duration.seconds(this.speedSec));
        marqueeTT.setInterpolator(Interpolator.LINEAR);
        marqueeTT.stop();
        marqueeTT.setToX(-(this.maxWidth(0)+50));
        
    	marqueeTT.setFromX((this.pnlMain != null && !firstTime) ? this.pnlMain.getWidth() : 0);
        
        marqueeTT.playFromStart();
    }
}