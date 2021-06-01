package com.example.moove.database;

import android.util.Log;

import com.example.moove.exceptions.UninitializedDatabaseException;
import com.example.moove.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public class DBManager {
    private FirebaseFirestore db;
    private CollectionReference usersCollectionRef;

    private static class SingletonHelper {
        private static final DBManager instance = new DBManager();
    }

    public static DBManager getInstance() {
        return SingletonHelper.instance;
    }

    public void initDB() {
        db = FirebaseFirestore.getInstance();
        usersCollectionRef = db.collection("users");
    }

    public void createUser(User user) throws UninitializedDatabaseException {
        if (db == null)
            throw new UninitializedDatabaseException();

        String userId = user.getId();

        Query query = usersCollectionRef.whereEqualTo("userId", userId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    String uid = documentSnapshot.getString("userId");
                    if (uid.equals(userId)) {
                        return;
                    }
                }
            }
            if (task.getResult().size() == 0) {
                db.collection("users")
                    .add(user.getHashedData())
                    .addOnSuccessListener(docRef -> {})
                    .addOnFailureListener(e -> {});
            }
        });
    }

    public void updateUser(Map<String, ?> dataMap, String userId) throws UninitializedDatabaseException {
        if (db == null)
            throw new UninitializedDatabaseException();

        Query query = usersCollectionRef.whereEqualTo("userId", userId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : task.getResult())
                    usersCollectionRef.document(documentSnapshot.getId()).set(dataMap, SetOptions.merge());


            }
        });
    }

    public CollectionReference getUsersCollectionRef() {
        return usersCollectionRef;
    }
}
