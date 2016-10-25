package com.pi.stela.graphics;

/**
 * Created by Paul on 08/10/2016.
 */
import com.pi.stela.R;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.CountDownTimer;
import android.widget.RelativeLayout;

public class BackgroundAnimator {
    RelativeLayout MainStelaRL;
    Context context;
    TransitionDrawable trans;
    int duration = 5000;
    int TransitionNumber;
    public void AnimateBackground(RelativeLayout mainStelaRL2, Context cont){
        MainStelaRL = mainStelaRL2;
        context = cont;
        TransitionNumber = 2;
        trans = (TransitionDrawable) context.getResources().getDrawable(R.drawable.transition_background);
        MainStelaRL.setBackground(trans);
        trans.startTransition(duration);
        Counter timer = new Counter(duration, 1000);
        timer.start();
    }
    public void ContinueAnimateBackground(){
        if(TransitionNumber == 1){
            TransitionNumber = 2;
            trans = (TransitionDrawable) context.getResources().getDrawable(R.drawable.transition_background);
            MainStelaRL.setBackground(trans);
            trans.reverseTransition(duration);
        }else if(TransitionNumber == 2){
            TransitionNumber = 3;
            trans = (TransitionDrawable) context.getResources().getDrawable(R.drawable.transition_background_2);
            MainStelaRL.setBackground(trans);
            trans.startTransition(duration);
        }else if(TransitionNumber == 3){
            TransitionNumber = 1;
            trans = (TransitionDrawable) context.getResources().getDrawable(R.drawable.transition_background_3);
            MainStelaRL.setBackground(trans);
            trans.startTransition(duration);
        }
        Counter timer = new Counter(duration, 1000);
        timer.start();
    }
    public class Counter extends CountDownTimer{
        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
        }
        @Override
        public void onFinish() {
            ContinueAnimateBackground();
        }
    }
}

