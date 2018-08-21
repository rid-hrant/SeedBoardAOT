package com.seed.seedaot;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class FbIdService extends FirebaseMessagingService {

//fAIXj_wqoro:APA91bHaaDRRoL1x7V2MuYmkHu39xn4vjIu_XBdNJkOnp6BqTdAtkaxfq70lJ2IVQuhdGdhygtuMo3QJn8-hW1DO647OGyibtQhYbiDl_zSIwvIic6aiwWRefADLMVKGD0K2Uf8-u5lVJtuSmmb4FgVIUmrglCogKQ
    @Override
    public void onNewToken(String token) {
        Log.d("FIREBASE", "Token token: " + token);
    }
}
