package com.example.eventalate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
public abstract class DatabaseDAO<T> {
    protected static final FirebaseFirestore db = FirebaseFirestore.getInstance();


}
