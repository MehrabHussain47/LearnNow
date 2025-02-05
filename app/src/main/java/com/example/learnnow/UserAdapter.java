package com.example.learnnow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserModel> userList;
    private OnUserActionListener listener;

    public interface OnUserActionListener {
        void onEditUser(UserModel user);
        void onDeleteUser(int userId);
    }

    public UserAdapter(List<UserModel> userList, OnUserActionListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.textViewName.setText(user.getFullName());
        holder.textViewEmail.setText(user.getEmail());
        holder.textViewRole.setText(user.getRole());

        holder.buttonEdit.setOnClickListener(v -> listener.onEditUser(user));
        holder.buttonDelete.setOnClickListener(v -> listener.onDeleteUser(user.getId()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewEmail, textViewRole;
        Button buttonEdit, buttonDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
