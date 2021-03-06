package com.thailam.piggywallet.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thailam.piggywallet.R;
import com.thailam.piggywallet.data.model.Transaction;

import java.util.List;

public class TransactionInnerAdapter extends RecyclerView.Adapter<TransactionInnerAdapter.ViewHolder> {
    private OnItemClickListener mListener;
    private List<Transaction> mTransactions;

    TransactionInnerAdapter(List<Transaction> transactions, OnItemClickListener listener) {
        mListener = listener;
        mTransactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_wallet_detail_item_inside, parent, false);
        return new ViewHolder(parent.getContext(), itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(mTransactions.get(i));
    }

    @Override
    public int getItemCount() {
        return mTransactions == null ? 0 : mTransactions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        private Transaction mTransaction;
        private OnItemClickListener mListener;
        private TextView mTxtViewTitle;
        private TextView mTxtViewAmount;
        private ImageView mImageViewFeature;

        private ViewHolder(Context context, View itemView, OnItemClickListener listener) {
            super(itemView);
            mContext = context;
            mListener = listener;
            mTxtViewTitle = itemView.findViewById(R.id.text_view_wallet_detail_inside_title);
            mTxtViewAmount = itemView.findViewById(R.id.text_view_wallet_detail_inside_amount);
            mImageViewFeature = itemView.findViewById(R.id.image_view_wallet_detail_inside_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onItemClick(mTransaction);
        }

        private void bindData(Transaction transaction) {
            if (transaction == null) return;
            mTransaction = transaction;
            mImageViewFeature.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_circle_icons_money));
            displayWalletAmount(transaction.getAmount());
        }

        private void displayWalletAmount(double amount) {
            int colorId = amount >= 0 ? R.color.color_positive_balance : R.color.color_negative_balance;
            mTxtViewAmount.setTextColor(mContext.getResources().getColor(colorId));
            mTxtViewAmount.setText(String.valueOf(amount));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }
}
