package com.bailleul.tanguy.go4lunch.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bailleul.tanguy.go4lunch.R;
import com.bailleul.tanguy.go4lunch.firestore.User;
import com.bailleul.tanguy.go4lunch.util.MyDateFormat;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class ListOfWorkmatesAdapter extends FirestoreRecyclerAdapter<User, ListOfWorkmatesAdapter.UserHolder> {

    private RequestManager glide;
    private Context context;


    public ListOfWorkmatesAdapter(@NonNull FirestoreRecyclerOptions<User> options, RequestManager glide) {
        super(options);
        this.glide = glide;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserHolder userHolder, int i, @NonNull User user) {
        MyDateFormat forToday = new MyDateFormat();
        String today = forToday.getTodayDate();
        String registeredDate = user.getRestoDate();

        // Default values
        String text;
        text = user.getUsername() + context.getString(R.string.not_decided);
        userHolder.textUser.setTypeface(null, Typeface.ITALIC);
        userHolder.textUser.setTextColor(context.getResources().getColor(R.color.colorMyGrey));

        // Specifications if a restaurant was chosen for today
        if (user.getRestoTodayName() != null && !user.getRestoTodayName().isEmpty()) {
            if (registeredDate.equals(today)) {
                text = user.getUsername() + context.getString(R.string.decided) + user.getRestoTodayName();
                userHolder.textUser.setTypeface(null, Typeface.NORMAL);
                userHolder.textUser.setTextColor(context.getResources().getColor(R.color.colorMyBlack));
            }
        }
        userHolder.textUser.setText(text);


    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates, parent, false);
        context = parent.getContext();
        return new UserHolder(view);
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView textUser;
        ImageView imageUser;

        UserHolder(@NonNull View itemView) {
            super(itemView);
            textUser = itemView.findViewById(R.id.workmates_TextView);
            imageUser = itemView.findViewById(R.id.workmates_ImageView);

        }
    }


}

